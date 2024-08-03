package swm.betterlife.antifragile.domain.recommend.controller;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.betterlife.antifragile.common.response.ResponseBody;
import swm.betterlife.antifragile.domain.recommend.dto.request.RecommendPromptRequest;
import swm.betterlife.antifragile.domain.recommend.dto.response.OpenAiResponse;
import swm.betterlife.antifragile.domain.recommend.dto.response.YouTubeResponse;
import swm.betterlife.antifragile.domain.recommend.service.RecommendService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/recommends")
public class RecommendController {
    private final RecommendService recommendService;

    @PostMapping("/chat-gpt")
    public ResponseBody<OpenAiResponse> chatGpt(
        @RequestBody RecommendPromptRequest request
    ) {
        return ResponseBody.ok(
            recommendService.chatGpt(request));
    }

    @PostMapping("/youtube")
    public ResponseBody<YouTubeResponse> youTubeRecommend(
        @RequestBody RecommendPromptRequest prompt
    ) throws IOException {
        return ResponseBody.ok(
            recommendService.youTubeRecommend(prompt));
    }
}
