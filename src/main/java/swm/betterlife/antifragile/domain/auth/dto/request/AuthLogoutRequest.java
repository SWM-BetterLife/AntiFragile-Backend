package swm.betterlife.antifragile.domain.auth.dto.request;

public record AuthLogoutRequest(
    String refreshToken
) {
}
