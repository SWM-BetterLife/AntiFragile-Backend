package swm.betterlife.antifragile.domain.auth.dto;

public record LogoutRequest(
    String refreshToken
) {
}
