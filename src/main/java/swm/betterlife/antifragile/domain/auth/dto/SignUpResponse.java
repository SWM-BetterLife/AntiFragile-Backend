package swm.betterlife.antifragile.domain.auth.dto;

import swm.betterlife.antifragile.domain.member.entity.LoginType;

public record SignUpResponse(
    Long id,
    String email,
    String nickname,
    LoginType loginType,
    String accessToken
) {

}
