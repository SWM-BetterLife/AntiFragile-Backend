package swm.betterlife.antifragile.domain.member.dto.response;

import lombok.Builder;
import swm.betterlife.antifragile.domain.member.entity.LoginType;
import swm.betterlife.antifragile.domain.member.entity.Member;

@Builder
public record MemberDetailResponse(
        String id,
        String email,
        String nickname,
        String profileImgUrl,
        Integer point,
        LoginType loginType //todo: 연속 작성일 수, 총 작성일 수 추가
) {
    public static MemberDetailResponse from(Member member) {
        return MemberDetailResponse.builder()
            .id(member.getId().toString())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .profileImgUrl(member.getProfileImgUrl())
            .point(member.getPoint())
            .loginType(member.getLoginType())
            .build();
    }
}