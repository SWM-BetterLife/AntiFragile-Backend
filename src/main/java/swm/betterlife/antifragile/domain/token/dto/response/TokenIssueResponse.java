package swm.betterlife.antifragile.domain.token.dto.response;

public record TokenIssueResponse(
        String accessToken,
        String refreshToken
) {
}
