package swm.betterlife.antifragile.domain.recommend.service;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.ChannelStatistics;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.VideoStatus;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import swm.betterlife.antifragile.common.util.AgeConverter;
import swm.betterlife.antifragile.domain.member.entity.Member;
import swm.betterlife.antifragile.domain.recommend.dto.request.OpenAiRequest;
import swm.betterlife.antifragile.domain.recommend.dto.response.OpenAiResponse;
import swm.betterlife.antifragile.domain.recommend.dto.response.YouTubeResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService {
    private final RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${youtube.api.key}")
    private String apiKey;

    public String createPrompt(List<String> emotions, Member member) {

        String emotionString = String.join(", ", emotions);

        return String.format(
            "%s 감정을 가진 나이가 %d인 %s에게 추천할만한 영상의 키워드를 하나만 반환해줘.",
            emotionString,
            AgeConverter.convertDateToAge(member.getBirthDate()),
            member.getJob()
        );
    }

    public OpenAiResponse chatGpt(String prompt) {
        OpenAiRequest openAiRequest = new OpenAiRequest(
            model, prompt, 1);
        return restTemplate.postForObject(
            apiUrl, openAiRequest, OpenAiResponse.class);
    }

    public YouTubeResponse youTubeRecommend(String prompt) throws IOException {
        OpenAiResponse openAiResponse = chatGpt(prompt);
        String recommendedKeyword = openAiResponse.choices().get(0).message().content();
        log.info("Recommended Keyword: {}", recommendedKeyword);
        JsonFactory jsonFactory = new JacksonFactory();

        // YouTube 객체를 빌드하여 API에 접근할 수 있는 YouTube 클라이언트 생성
        YouTube youtube = new YouTube.Builder(
            new com.google.api.client.http.javanet.NetHttpTransport(),
            jsonFactory,
            request -> {})
            .setApplicationName("Antifragile")
            .build();

        // YouTube Search API를 사용하여 동영상 검색을 위한 요청 객체 생성
        YouTube.Search.List search = youtube.search()
            .list(Collections.singletonList("id,snippet"));

        search.setKey(apiKey);
        search.setQ(recommendedKeyword);
        search.setType(Collections.singletonList("video"));
        search.setMaxResults(50L);

        List<YouTubeResponse.YouTubeApiInfo> youTubeApiInfos = new ArrayList<>();

        String nextPageToken = null;
        while (youTubeApiInfos.size() < 5) {
            search.setPageToken(nextPageToken);
            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();

            if (searchResultList != null && !searchResultList.isEmpty()) {
                for (SearchResult searchResult : searchResultList) {
                    if (youTubeApiInfos.size() >= 5) {
                        break;
                    }

                    String videoId = searchResult.getId().getVideoId();
                    if (videoId == null) {
                        continue;
                    }

                    // 영상의 임베딩 가능 여부 확인
                    YouTube.Videos.List videoRequest = youtube.videos()
                        .list(Collections.singletonList("status"));
                    videoRequest.setKey(apiKey);
                    videoRequest.setId(Collections.singletonList(videoId));
                    VideoListResponse videoResponse = videoRequest.execute();
                    Video video = videoResponse.getItems().get(0);
                    VideoStatus status = video.getStatus();

                    if (status.getEmbeddable() == null || !status.getEmbeddable()) {
                        continue; // 임베딩이 불가능한 동영상 건너뛰기
                    }

                    String videoTitle = searchResult.getSnippet().getTitle();
                    String videoDescription = searchResult.getSnippet().getDescription();
                    String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/sddefault.jpg";
                    String channelId = searchResult.getSnippet().getChannelId();
                    String channelTitle = searchResult.getSnippet().getChannelTitle();

                    // 채널 정보 요청 생성 및 실행
                    YouTube.Channels.List channelRequest = youtube.channels()
                        .list(Collections.singletonList("snippet,statistics"));
                    channelRequest.setKey(apiKey);
                    channelRequest.setId(Collections.singletonList(channelId));
                    ChannelListResponse channelResponse = channelRequest.execute();
                    Channel channel = channelResponse.getItems().get(0);
                    ChannelStatistics statistics = channel.getStatistics();
                    Long subscriberCount = Optional.ofNullable(statistics)
                        .map(ChannelStatistics::getSubscriberCount)
                        .map(Number::longValue)
                        .orElse(null);
                    String channelImageUrl = channel.getSnippet()
                        .getThumbnails().getDefault().getUrl();

                    youTubeApiInfos.add(new YouTubeResponse.YouTubeApiInfo(
                        videoTitle,
                        videoDescription,
                        thumbnailUrl,
                        subscriberCount,
                        channelTitle,
                        channelImageUrl,
                        "https://www.youtube.com/watch?v=" + videoId
                    ));
                }
            }
            nextPageToken = searchResponse.getNextPageToken();
            if (nextPageToken == null) {
                break;
            }
        }
        return new YouTubeResponse(youTubeApiInfos);
    }
}
