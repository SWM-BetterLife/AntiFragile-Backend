package swm.betterlife.antifragile.domain.token.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import swm.betterlife.antifragile.common.exception.LoginRequiredException;
import swm.betterlife.antifragile.common.exception.RefreshTokenNotValidatedException;
import swm.betterlife.antifragile.common.jwt.util.JwtProvider;
import swm.betterlife.antifragile.common.security.PrincipalDetails;
import swm.betterlife.antifragile.domain.member.entity.LoginType;
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

    public TokenIssueResponse getTokenIssue(TokenReIssueRequest request) {
        if(!jwtProvider.validateToken(request.refreshToken())) {
            throw new RefreshTokenNotValidatedException();
        }
        
        Authentication authentication = jwtProvider.getAuthentication(request.refreshToken());
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        String email = principal.getUsername();
        LoginType loginType = principal.loginType();
        String id = loginType.name() + email;

        Token token = tokenRepository.findById(id)
                .orElseThrow(RefreshTokenNotValidatedException::new);

        if (!token.getTokenValue().equals(request.refreshToken())) {
            tokenRepository.delete(token);
            throw new LoginRequiredException();
        }
        return jwtProvider.issueToken(authentication, loginType);
    }

}
