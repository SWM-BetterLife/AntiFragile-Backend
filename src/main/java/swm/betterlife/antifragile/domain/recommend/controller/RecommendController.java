package swm.betterlife.antifragile.domain.recommend.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.betterlife.antifragile.common.response.ResponseBody;
import swm.betterlife.antifragile.domain.recommend.dto.request.BedrockRequest;
import swm.betterlife.antifragile.domain.recommend.dto.request.RecommendPromptRequest;
import swm.betterlife.antifragile.domain.recommend.dto.response.OpenAiResponse;
import swm.betterlife.antifragile.domain.recommend.dto.response.YouTubeResponse;
import swm.betterlife.antifragile.domain.recommend.service.BedrockService;
import swm.betterlife.antifragile.domain.recommend.service.CredentialTestService;
import swm.betterlife.antifragile.domain.recommend.service.RecommendService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/recommends")
public class RecommendController {
    private final RecommendService recommendService;
    private final BedrockService bedrockService;
    private final CredentialTestService credentialTestService;

    @PostMapping("/chat-gpt")
    public ResponseBody<OpenAiResponse> chatGpt(
        @RequestBody RecommendPromptRequest request
    ) {
        return ResponseBody.ok(
            recommendService.chatGpt(request.prompt()));
    }

    @PostMapping("/youtube")
    public ResponseBody<YouTubeResponse> youTubeRecommend(
        @RequestBody RecommendPromptRequest request
    ) throws IOException {
        return ResponseBody.ok(
            recommendService.youTubeRecommend(request.prompt()));
    }

    @PostMapping("/bedrock")
    public Map<String, Object> callBedrockApi(
        @RequestBody BedrockRequest request
    ) {
        try {
            boolean credentialsValid = credentialTestService.testCredentials();

            if (!credentialsValid) {
                return Map.of("status", "error", "message", "Invalid AWS credentials");
            }

            List<String> recommendedVideos = bedrockService.callBedrockApi(request.emotion(), request.diarySummary());

            // 추천 ID 리스트를 JSON 형태로 응답
            return Map.of("status", "success", "recommended_videos", recommendedVideos);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("status", "error", "message", e.getMessage());
        }
    }
}
