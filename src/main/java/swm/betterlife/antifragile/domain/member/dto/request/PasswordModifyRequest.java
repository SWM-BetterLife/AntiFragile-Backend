package swm.betterlife.antifragile.domain.member.dto.request;

public record PasswordModifyRequest(
    String curPassword,
    String newPassword
) {
}