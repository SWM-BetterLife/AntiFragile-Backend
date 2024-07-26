package swm.betterlife.antifragile.domain.member.dto.request;

import swm.betterlife.antifragile.domain.member.entity.Gender;

public record MemberProfileModifyRequest(
    String nickname,
    Integer age,
    Gender gender,
    String job
) {
}
