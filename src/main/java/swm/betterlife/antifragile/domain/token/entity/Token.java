package swm.betterlife.antifragile.domain.token.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "Token", timeToLive = 120)
@AllArgsConstructor
@Getter
@ToString
public class Token {
    @Id
    private String id;
    private String tokenValue;

}
