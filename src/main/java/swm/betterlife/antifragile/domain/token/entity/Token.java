package swm.betterlife.antifragile.domain.token.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import swm.betterlife.antifragile.domain.member.entity.LoginType;

@RedisHash(value = "Token", timeToLive = 120)
@AllArgsConstructor
@Getter
@ToString
public class Token {
    @Id
    private String id;
    private String tokenValue;

    public static Token of(LoginType loginType, String email, String tokenValue) {
        return new Token(loginType.name() + email, tokenValue);
    }
}
