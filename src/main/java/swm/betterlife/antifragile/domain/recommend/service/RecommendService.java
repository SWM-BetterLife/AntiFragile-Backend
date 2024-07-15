package swm.betterlife.antifragile.domain.recommend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import swm.betterlife.antifragile.domain.recommend.dto.request.OpenAIRequest;
import swm.betterlife.antifragile.domain.recommend.dto.response.OpenAIResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService {
    private final RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;
    public OpenAIResponse chatGpt(String prompt) {
        OpenAIRequest request = new OpenAIRequest(
            model, prompt, 1, 256, 1, 2, 2);
        return restTemplate.postForObject(
            apiURL, request, OpenAIResponse.class);
    }
}
