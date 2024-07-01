package swm.betterlife.antifragile.domain.member.dto;

import lombok.Builder;
import swm.betterlife.antifragile.domain.member.entity.LoginType;
import swm.betterlife.antifragile.domain.member.entity.Member;

@Builder
public record MemberDetailResponse(
        Long id,
        String email,
        String nickName,
        LoginType loginType
) {
    public static MemberDetailResponse from(Member member) {
        return MemberDetailResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickName(member.getNickName())
                .loginType(member.getLoginType())
                .build();
    }
}
