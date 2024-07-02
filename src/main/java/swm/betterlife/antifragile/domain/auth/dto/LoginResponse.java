package swm.betterlife.antifragile.domain.auth.dto;

import lombok.Builder;
import swm.betterlife.antifragile.domain.member.dto.MemberDetailResponse;
import swm.betterlife.antifragile.domain.member.entity.Member;
import swm.betterlife.antifragile.domain.token.dto.TokenIssueResponse;

@Builder
public record LoginResponse(

        MemberDetailResponse memberDetail,
        TokenIssueResponse tokenIssue
) {
    public static LoginResponse from(
            Member member,
            TokenIssueResponse tokenIssue
    ) {
        return LoginResponse.builder()
                .tokenIssue(tokenIssue)
                .memberDetail(MemberDetailResponse.from(member))
                .build();
    }
}
