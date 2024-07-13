package swm.betterlife.antifragile.domain.auth.dto.response;

import lombok.Builder;
import swm.betterlife.antifragile.domain.member.entity.LoginType;
import swm.betterlife.antifragile.domain.member.entity.Member;

@Builder
public record AuthSignUpResponse(
    String id,
    String email,
    String nickname,
    LoginType loginType
) {
    public static AuthSignUpResponse from(Member member) {
        return AuthSignUpResponse.builder()
            .id(member.getId())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .loginType(member.getLoginType())
            .build();
    }
}
