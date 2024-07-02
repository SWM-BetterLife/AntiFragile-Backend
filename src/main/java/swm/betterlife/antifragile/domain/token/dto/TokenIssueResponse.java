package swm.betterlife.antifragile.domain.token.dto;

public record TokenIssueResponse(
        String accessToken,
        String refreshToken
) {
}
