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
        Integer diaryTotalNum,
        LoginType loginType //todo: 연속 작성일 수 추가
) {
    public static MemberDetailResponse from(
        Member member, Integer point,
        Integer diaryTotalNum, String profileImgUrl
    ) {
        return MemberDetailResponse.builder()
            .id(member.getId())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .profileImgUrl(profileImgUrl)
            .point(point)
            .diaryTotalNum(diaryTotalNum)
            .loginType(member.getLoginType())
            .build();
    }
}
