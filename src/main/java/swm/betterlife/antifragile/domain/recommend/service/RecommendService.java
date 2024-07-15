package swm.betterlife.antifragile.domain.recommend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import swm.betterlife.antifragile.domain.recommend.dto.request.OpenAiRequest;
import swm.betterlife.antifragile.domain.recommend.dto.response.OpenAiResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService {
    private final RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    public OpenAiResponse chatGpt(String prompt) {
        OpenAiRequest request = new OpenAiRequest(
            model, prompt, 1, 256, 1, 2, 2);
        return restTemplate.postForObject(
            apiUrl, request, OpenAiResponse.class);
    }
}
