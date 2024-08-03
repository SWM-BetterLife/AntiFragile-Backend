package swm.betterlife.antifragile.domain.recommend.service;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.ChannelStatistics;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
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
import swm.betterlife.antifragile.domain.recommend.dto.request.OpenAiRequest;
import swm.betterlife.antifragile.domain.recommend.dto.request.RecommendPromptRequest;
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

    public OpenAiResponse chatGpt(RecommendPromptRequest request) {
        OpenAiRequest openAiRequest = new OpenAiRequest(
            model, request.prompt(), 1);
        return restTemplate.postForObject(
            apiUrl, openAiRequest, OpenAiResponse.class);
    }

    public YouTubeResponse youTubeRecommend(RecommendPromptRequest prompt) throws IOException {
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

                    String videoTitle = searchResult.getSnippet().getTitle();
                    String videoDescription = searchResult.getSnippet().getDescription();
                    String thumbnailUrl = searchResult.getSnippet()
                        .getThumbnails().getDefault().getUrl();
                    String channelId = searchResult.getSnippet().getChannelId();
                    String channelTitle = searchResult.getSnippet().getChannelTitle();

                    // 채널 정보 요청 생성
                    YouTube.Channels.List channelRequest = youtube.channels()
                        .list(Collections.singletonList("snippet,statistics"));
                    channelRequest.setKey(apiKey);
                    channelRequest.setId(Collections.singletonList(channelId));

                    // 채널 요청 실행 및 응답 받아오기
                    ChannelListResponse channelResponse = channelRequest.execute();
                    Channel channel = channelResponse.getItems().get(0);

                    // 채널의 구독자 수 및 채널 이미지 URL 가져오기
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
