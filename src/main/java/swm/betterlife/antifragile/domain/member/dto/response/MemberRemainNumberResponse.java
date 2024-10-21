package swm.betterlife.antifragile.domain.member.dto.response;

import lombok.Builder;

@Builder
public record MemberRemainNumberResponse(
    Integer remainNumber
) {
    public static MemberRemainNumberResponse from(Integer remainNumber) {
        return MemberRemainNumberResponse.builder()
            .remainNumber(remainNumber)
            .build();
    }
}
