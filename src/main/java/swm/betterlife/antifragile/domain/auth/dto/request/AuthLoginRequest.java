package swm.betterlife.antifragile.domain.auth.dto.request;

import swm.betterlife.antifragile.domain.member.entity.LoginType;

public record AuthLoginRequest(
    String email,
    LoginType loginType
) {

}
