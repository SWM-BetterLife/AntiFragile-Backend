package swm.betterlife.antifragile.domain.llm.dto.response;

import lombok.Builder;

@Builder
public record LlmModelUrlResponse(
    String modelUrl
) {
}
