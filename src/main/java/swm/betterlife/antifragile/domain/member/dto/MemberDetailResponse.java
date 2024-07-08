package swm.betterlife.antifragile.domain.member.dto;

import lombok.Builder;
import swm.betterlife.antifragile.domain.member.entity.LoginType;
import swm.betterlife.antifragile.domain.member.entity.Member;

@Builder
public record MemberDetailResponse(
        String id,
        String email,
        String nickName,
        String profileImgUrl,
        Integer point,
        LoginType loginType
) {
    public static MemberDetailResponse from(Member member) {
        return MemberDetailResponse.builder()
            .id(member.getId().toString())
            .email(member.getEmail())
            .nickName(member.getNickname())
            .profileImgUrl(member.getProfileImgUrl())
            .point(member.getPoint())
            .loginType(member.getLoginType())
            .build();
    }
}
