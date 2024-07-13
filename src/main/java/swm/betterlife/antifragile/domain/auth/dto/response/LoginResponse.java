package swm.betterlife.antifragile.domain.auth.dto.response;

import lombok.Builder;
import swm.betterlife.antifragile.domain.member.dto.response.MemberDetailResponse;
import swm.betterlife.antifragile.domain.member.entity.LoginType;
import swm.betterlife.antifragile.domain.member.entity.Member;
import swm.betterlife.antifragile.domain.token.dto.response.TokenIssueResponse;

@Builder
public record LoginResponse(
    String id,
    String email,
    String nickname,
    LoginType loginType,
    MemberDetailResponse memberDetail,
    TokenIssueResponse tokenIssue
) {
    public static LoginResponse from(
            Member member,
            TokenIssueResponse tokenIssue
    ) {
        return LoginResponse.builder()
            .id(member.getId())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .loginType(member.getLoginType())
            .tokenIssue(tokenIssue)
            .build();
    }
}
