package swm.betterlife.antifragile.domain.recommend.service;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LambdaService {

    private final ObjectMapper objectMapper;
    private final AWSLambda awsLambda;
    private static final String FUNCTION_NAME = "bedrock_api"; // Lambda 함수 이름

    public List<String> getRecommendations(String emotion, String diarySummary) {
        try {
            // 요청 데이터 구성
            Map<String, String> payload = new HashMap<>();
            payload.put("emotion", emotion);
            payload.put("diary_summary", diarySummary);

            // Lambda 호출 요청 생성
            InvokeRequest request = new InvokeRequest()
                .withFunctionName(FUNCTION_NAME)
                .withPayload(new ObjectMapper().writeValueAsString(payload));

            // Lambda 호출
            log.info("Invoking Lambda function with payload: {}", payload);
            InvokeResult result = awsLambda.invoke(request);

            // 응답 처리
            if (result.getFunctionError() != null) {
                log.error("Lambda function error: {}", result.getFunctionError());
                throw new RuntimeException("Lambda function error: " + result.getFunctionError());
            }

            String response = new String(result.getPayload().array(), StandardCharsets.UTF_8);
            log.info("Lambda response: {}", response);

            // 응답 파싱 (Lambda 응답 형식에 맞게 수정 필요)
            return parseLambdaResponse(response);

        } catch (Exception e) {
            log.error("Failed to invoke Lambda function", e);
            throw new RuntimeException("Failed to get recommendations", e);
        }
    }

    private List<String> parseLambdaResponse(String response) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(response);
        List<String> recommendations = new ArrayList<>();

        // API Gateway 형식의 응답을 처리
        if (root.has("body")) {
            String body = root.get("body").asText();
            // body가 문자열로 된 JSON이라면 다시 파싱
            JsonNode bodyNode = objectMapper.readTree(body);

            if (bodyNode.has("recommendations")) {
                JsonNode recommendationsNode = bodyNode.get("recommendations");
                if (recommendationsNode.isArray()) {
                    for (JsonNode item : recommendationsNode) {
                        recommendations.add(item.asText());
                    }
                }
            }
        }

        return recommendations;
    }
}
