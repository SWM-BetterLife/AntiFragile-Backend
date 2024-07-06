package swm.betterlife.antifragile.domain.auth.dto;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import swm.betterlife.antifragile.domain.member.entity.LoginType;

public record LoginRequest(
    String email,
    LoginType loginType
) {

}
