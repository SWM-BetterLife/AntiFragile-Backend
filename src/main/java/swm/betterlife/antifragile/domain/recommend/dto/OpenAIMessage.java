package swm.betterlife.antifragile.domain.recommend.dto;

public record OpenAIMessage(
    String role,
    String content
) {
}