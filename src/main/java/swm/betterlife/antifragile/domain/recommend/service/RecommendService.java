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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import swm.betterlife.antifragile.domain.recommend.dto.request.OpenAiRequest;
import swm.betterlife.antifragile.domain.recommend.dto.response.OpenAiResponse;
import swm.betterlife.antifragile.domain.recommend.dto.response.YouTubeResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService {
    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RecommendService.class);
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${youtube.api.key}")
    private String apiKey;

    public OpenAiResponse chatGpt(String prompt) {
        OpenAiRequest request = new OpenAiRequest(
            model, prompt, 1);
        return restTemplate.postForObject(
            apiUrl, request, OpenAiResponse.class);
    }

    public YouTubeResponse youTubeRecommend(String prompt) throws IOException {
        OpenAiResponse openAiResponse = chatGpt(prompt);
        String recommendedKeyword = openAiResponse.choices().get(0).message().content();
        logger.info("Recommended Keyword: {}", recommendedKeyword);
        JsonFactory jsonFactory = new JacksonFactory();

        // YouTube 객체를 빌드하여 API에 접근할 수 있는 YouTube 클라이언트 생성
        YouTube youtube = new YouTube.Builder(
            new com.google.api.client.http.javanet.NetHttpTransport(),
            jsonFactory,
            request -> {}
        )
        .setApplicationName("Antifragile")
        .build();

        // YouTube Search API를 사용하여 동영상 검색을 위한 요청 객체 생성
        YouTube.Search.List search = youtube.search()
            .list(Collections.singletonList("id,snippet"));

        // API 키 설정
        search.setKey(apiKey);

        // 검색어 설정
        search.setQ(recommendedKeyword);

        // 검색 요청 실행 및 응답 받아오기
        SearchListResponse searchResponse = search.execute();

        // 검색 결과에서 동영상 목록 가져오기
        List<SearchResult> searchResultList = searchResponse.getItems();
        List<YouTubeResponse.YouTubeApiInfo> youTubeApiInfos = new ArrayList<>();

        if (searchResultList != null && !searchResultList.isEmpty()) {
            for (SearchResult searchResult : searchResultList) {
                String videoId = searchResult.getId().getVideoId();

                // videoId가 null인 경우에는 무시하거나 대체 값을 설정합니다.
                if (videoId == null) {
                    continue; // null인 경우 결과를 무시하거나 기본값으로 처리
                }

                // 동영상의 제목, 설명, 썸네일 이미지 URL 가져오기
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
                Long subscriberCount = (statistics != null) ?
                    statistics.getSubscriberCount().longValue() : null;
                String channelImageUrl = channel
                    .getSnippet().getThumbnails().getDefault().getUrl();

                // YouTubeApiInfo 레코드 추가
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
        return new YouTubeResponse(youTubeApiInfos);
    }
}
