package swm.betterlife.antifragile.domain.member.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.Builder;
import swm.betterlife.antifragile.domain.member.entity.Gender;
import swm.betterlife.antifragile.domain.member.entity.Member;

@Builder
public record MemberDetailInfoResponse(
        String nickname,
        String profileImgUrl,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
        LocalDate birthDate,
        String job,
        Gender gender
) {
    public static MemberDetailInfoResponse from(Member member, String profileImgUrl) {
        return MemberDetailInfoResponse.builder()
            .nickname(member.getNickname())
            .profileImgUrl(profileImgUrl)
            .birthDate(member.getBirthDate())
            .job(member.getJob())
            .gender(member.getGender())
            .build();
    }
}
