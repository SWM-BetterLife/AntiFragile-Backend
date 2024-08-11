package swm.betterlife.antifragile.domain.member.dto.response;

import swm.betterlife.antifragile.domain.member.entity.Gender;

public record MemberProfileModifyResponse(
    String nickname,
    Integer age,
    Gender gender,
    String job,
    String profileImgUrl
) {
}
