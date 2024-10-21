package swm.betterlife.antifragile.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import swm.betterlife.antifragile.domain.member.entity.Gender;
import swm.betterlife.antifragile.domain.member.entity.LoginType;

public record AuthSignUpRequest(
    String email,
    LoginType loginType,
    String nickname,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    LocalDate birthDate,
    Gender gender,
    String job
) {

}
