package swm.betterlife.antifragile.domain.token.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.betterlife.antifragile.common.exception.LoginRequiredException;
import swm.betterlife.antifragile.common.exception.RefreshTokenNotValidatedException;
import swm.betterlife.antifragile.common.jwt.util.JwtProvider;
import swm.betterlife.antifragile.domain.token.dto.TokenIssueResponse;
import swm.betterlife.antifragile.domain.token.dto.TokenReIssueRequest;
import swm.betterlife.antifragile.domain.token.entity.Token;
import swm.betterlife.antifragile.domain.token.repository.TokenRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;

    public TokenIssueResponse getNewTokenIssue(TokenReIssueRequest request) {
        if (!jwtProvider.validateToken(request.refreshToken())) {
            throw new RefreshTokenNotValidatedException();
        }

        Authentication authentication = jwtProvider.getAuthentication(request.refreshToken());
        String tokenId = authentication.getName();

        log.info("TokenService.tokenId: {}", tokenId);

        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(RefreshTokenNotValidatedException::new);

        if (!token.getTokenValue().equals(request.refreshToken())) {
            tokenRepository.delete(token);
            throw new LoginRequiredException();
        }
        return jwtProvider.issueToken(authentication);
    }

    @Transactional
    public void deleteToken(String refreshToken) {

        Authentication authentication = jwtProvider.getAuthentication(refreshToken);
        String tokenId = authentication.getName();

        tokenRepository.deleteById(tokenId);
    }


}
