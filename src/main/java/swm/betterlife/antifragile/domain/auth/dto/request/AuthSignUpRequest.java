package swm.betterlife.antifragile.domain.auth.dto.request;

import swm.betterlife.antifragile.domain.member.entity.LoginType;

public record AuthSignUpRequest(
    String email,
    LoginType loginType //todo: 암호화된 password로 받는 거 고민
) {

}
