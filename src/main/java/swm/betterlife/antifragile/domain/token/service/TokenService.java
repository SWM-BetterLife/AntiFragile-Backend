package swm.betterlife.antifragile.domain.token.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.betterlife.antifragile.domain.token.entity.Token;
import swm.betterlife.antifragile.domain.token.repository.TokenRepository;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public void save(Token token) {
        tokenRepository.save(token);
    }

    public Token getToken(String id) {
        return tokenRepository.getToken(id);
    }
}
