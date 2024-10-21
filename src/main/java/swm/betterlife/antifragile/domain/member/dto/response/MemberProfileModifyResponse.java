package swm.betterlife.antifragile.domain.member.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import swm.betterlife.antifragile.domain.member.entity.Gender;

public record MemberProfileModifyResponse(
    String nickname,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    LocalDate birthDate,
    Gender gender,
    String job,
    String profileImgUrl
) {
}
