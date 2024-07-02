package swm.betterlife.antifragile.common.jwt.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import swm.betterlife.antifragile.common.exception.TokenExpiredException;
import swm.betterlife.antifragile.common.security.PrincipalDetails;
import swm.betterlife.antifragile.domain.auth.dto.TokenIssueResponse;
import swm.betterlife.antifragile.domain.member.entity.LoginType;
import swm.betterlife.antifragile.domain.token.entity.Token;
import swm.betterlife.antifragile.domain.token.repository.TokenRepository;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static swm.betterlife.antifragile.common.jwt.constant.JwtConstant.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secretKey}")
    private String secretKeyPlain;
    private Key secretKey;

    private final TokenRepository tokenRepository;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKeyPlain);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        log.info("JWT Secret Key: {}", secretKeyPlain);
    }

    public TokenIssueResponse issueToken(Authentication authentication, LoginType loginType) {
        String accessToken = generateAccessToken(authentication, loginType);
        String refreshToken = generateRefreshToken(authentication, loginType);
        return new TokenIssueResponse(accessToken, refreshToken);
    }

    private String generateAccessToken(Authentication authentication, LoginType loginType) {
        return generateToken(authentication, ACCESS_TOKEN_EXPIRE_TIME, loginType);
    }

    private String generateRefreshToken(Authentication authentication, LoginType loginType) {
        String refreshToken =  generateToken(authentication, REFRESH_TOKEN_EXPIRE_TIME, loginType);
        tokenRepository.save(Token.of(loginType, authentication.getName(), refreshToken));
        return refreshToken;
    }

    private String generateToken(
            Authentication authentication, Long tokenExpiresIn, LoginType loginType
    ) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date expiration = new Date(now + tokenExpiresIn);

        return getToken(authentication, authorities, expiration, loginType);
    }

    private String getToken(
            Authentication authentication, String authorities,
            Date accessTokenExpiresIn, LoginType loginType
    ) {
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(LOGIN_TYPE_KEY, loginType)
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        LoginType loginType = LoginType.valueOf((String) claims.get(LOGIN_TYPE_KEY));

        PrincipalDetails principal = PrincipalDetails.of(claims.getSubject(), loginType);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다."); //todo : 만료일 때, Action ?
            throw new TokenExpiredException();
        }

        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey)
                    .build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
