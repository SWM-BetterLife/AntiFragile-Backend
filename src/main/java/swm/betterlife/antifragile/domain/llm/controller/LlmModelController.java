package swm.betterlife.antifragile.domain.llm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.betterlife.antifragile.common.response.ResponseBody;
import swm.betterlife.antifragile.domain.llm.dto.response.LlmModelUrlResponse;
import swm.betterlife.antifragile.domain.llm.service.LlmModelService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/llm-models")
public class LlmModelController {

    private final LlmModelService llmModelService;

    @GetMapping()
    public ResponseBody<LlmModelUrlResponse> getLlmModelUrl() {
        return ResponseBody.ok(llmModelService.getLlmModelUrl());
    }
}
