package swm.betterlife.antifragile.domain.recommend.dto.request;

public record BedrockRequest(
    String emotion,
    String diarySummary
) {
}