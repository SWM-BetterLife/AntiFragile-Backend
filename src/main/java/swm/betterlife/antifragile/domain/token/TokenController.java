package swm.betterlife.antifragile.domain.token;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.betterlife.antifragile.domain.token.entity.Token;
import swm.betterlife.antifragile.domain.token.repository.TokenRepository;

import static swm.betterlife.antifragile.domain.member.entity.LoginType.KAKAO;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final TokenRepository tokenRepository;

    @GetMapping("/auth/redis")
    public String test() {
        tokenRepository.save(Token.of(KAKAO, "test@test.com", "dafekldfjae"));
        return "success";
    }
}
