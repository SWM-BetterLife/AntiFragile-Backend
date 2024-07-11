package swm.betterlife.antifragile.domain.auth.dto.request;

public record LogoutRequest(
    String refreshToken
) {
}
