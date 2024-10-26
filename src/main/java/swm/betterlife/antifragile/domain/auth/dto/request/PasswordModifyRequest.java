package swm.betterlife.antifragile.domain.auth.dto.request;

public record PasswordModifyRequest(
    String curPassword,
    String newPassword
) {
}
