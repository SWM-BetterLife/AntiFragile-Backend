package swm.betterlife.antifragile.domain.recommend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swm.betterlife.antifragile.common.response.ResponseBody;
import swm.betterlife.antifragile.domain.recommend.dto.response.OpenAiResponse;
import swm.betterlife.antifragile.domain.recommend.service.RecommendService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/recommends")
public class RecommendController {
    private final RecommendService recommendService;

    @GetMapping("/chatGpt")
    public ResponseBody<OpenAiResponse> chatGpt(
        @RequestParam("prompt") String prompt
    ){
        return ResponseBody.ok(
            recommendService.chatGpt(prompt));
    }
}
