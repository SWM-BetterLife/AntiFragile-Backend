package swm.betterlife.antifragile.domain.recommend.dto;

public record OpenAiMessage(
    String role,
    String content
) {
}