package swm.betterlife.antifragile.common.jwt.util;

import static swm.betterlife.antifragile.common.jwt.constant.JwtConstant.ACCESS_TOKEN_EXPIRE_TIME;
import static swm.betterlife.antifragile.common.jwt.constant.JwtConstant.AUTHORITIES_KEY;
import static swm.betterlife.antifragile.common.jwt.constant.JwtConstant.LOGIN_TYPE_KEY;
import static swm.betterlife.antifragile.common.jwt.constant.JwtConstant.MEMBER_ID_KEY;
import static swm.betterlife.antifragile.common.jwt.constant.JwtConstant.REFRESH_TOKEN_EXPIRE_TIME;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import swm.betterlife.antifragile.common.exception.TokenExpiredException;
import swm.betterlife.antifragile.common.security.PrincipalDetails;
import swm.betterlife.antifragile.domain.member.entity.LoginType;
import swm.betterlife.antifragile.domain.token.dto.response.TokenIssueResponse;
import swm.betterlife.antifragile.domain.token.entity.Token;
import swm.betterlife.antifragile.domain.token.repository.TokenRepository;


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
    }

    public TokenIssueResponse issueToken(Authentication authentication) {
        String accessToken = generateAccessToken(authentication);
        String refreshToken = generateRefreshToken(authentication);
        return new TokenIssueResponse(accessToken, refreshToken);
    }

    private String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, ACCESS_TOKEN_EXPIRE_TIME);
    }

    private String generateRefreshToken(Authentication authentication) {
        String refreshToken =  generateToken(authentication, REFRESH_TOKEN_EXPIRE_TIME);
        tokenRepository.save(new Token(authentication.getName(), refreshToken));
        return refreshToken;
    }

    private String generateToken(Authentication authentication, Long tokenExpiresIn) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date expiration = new Date(now + tokenExpiresIn);

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = principalDetails.email();
        String memberId = principalDetails.memberId();
        LoginType loginType = principalDetails.loginType();

        return getToken(email, authorities, memberId, expiration, loginType);
    }

    private String getToken(
            String email, String authorities, String memberId,
            Date accessTokenExpiresIn, LoginType loginType
    ) {
        return Jwts.builder()
            .setSubject(email)
            .claim(MEMBER_ID_KEY, memberId)
            .claim(LOGIN_TYPE_KEY, loginType)
            .claim(AUTHORITIES_KEY, authorities)
            .setExpiration(accessTokenExpiresIn)
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다."); //todo: CustomEx
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        String email = claims.getSubject();
        String memberId = (String) claims.get(MEMBER_ID_KEY);
        LoginType loginType = LoginType.valueOf((String) claims.get(LOGIN_TYPE_KEY));

        PrincipalDetails principal
            = PrincipalDetails.of(email, memberId, loginType);
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

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey)
                    .build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
