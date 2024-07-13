package swm.betterlife.antifragile.domain.auth.service;

import static swm.betterlife.antifragile.domain.member.entity.RoleType.ROLE_USER;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.betterlife.antifragile.common.jwt.util.JwtProvider;
import swm.betterlife.antifragile.domain.auth.dto.request.AuthLoginRequest;
import swm.betterlife.antifragile.domain.auth.dto.request.AuthLogoutRequest;
import swm.betterlife.antifragile.domain.auth.dto.request.AuthSignUpRequest;
import swm.betterlife.antifragile.domain.auth.dto.response.AuthLoginResponse;
import swm.betterlife.antifragile.domain.auth.dto.response.AuthSignUpResponse;
import swm.betterlife.antifragile.domain.member.entity.LoginType;
import swm.betterlife.antifragile.domain.member.entity.Member;
import swm.betterlife.antifragile.domain.member.repository.MemberRepository;
import swm.betterlife.antifragile.domain.token.dto.response.TokenIssueResponse;
import swm.betterlife.antifragile.domain.token.service.TokenService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${spring.security.oauth-password.google}")
    private String googlePassword;
    @Value("${spring.security.oauth-password.kakao}")
    private String kakaoPassword;
    @Value("${spring.security.oauth-password.naver}")
    private String naverPassword;

    private final JwtProvider jwtProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional
    public AuthLoginResponse login(AuthLoginRequest authLoginRequest) {
        String password = getPasswordByLoginType(authLoginRequest.loginType());

        String username
            = authLoginRequest.loginType().name() + ":" + authLoginRequest.email(); //todo: common분리
        Authentication authentication = getAuthenticate(username, password);

        Member member = memberRepository.getMember(
            authLoginRequest.email(), authLoginRequest.loginType()
        );
        TokenIssueResponse tokenIssueResponse
                = jwtProvider.issueToken(authentication);
        return AuthLoginResponse.from(member, tokenIssueResponse);
    }

    @Transactional
    public AuthSignUpResponse signUp(AuthSignUpRequest authSignUpRequest) {
        if (memberRepository.existsByEmailAndLoginType(
            authSignUpRequest.email(), authSignUpRequest.loginType())
        ) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");  //todo: Custom Ex
        }

        String password = getPasswordByLoginType(authSignUpRequest.loginType());
        String encodedPassword = passwordEncoder.encode(password);

        Member member = Member.builder()
                .email(authSignUpRequest.email())
                .password(encodedPassword)
                .loginType(authSignUpRequest.loginType())
                .roleType(ROLE_USER)
                .build();

        return AuthSignUpResponse.from(memberRepository.save(member));

    }

    @Transactional
    public void logout(AuthLogoutRequest authLogoutRequest) {
        tokenService.deleteToken(authLogoutRequest.refreshToken());
    }

    private Authentication getAuthenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken
            = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    }

    private String getPasswordByLoginType(LoginType loginType) {
        return switch (loginType) {
            case GOOGLE -> googlePassword;
            case KAKAO -> kakaoPassword;
            case NAVER -> naverPassword;
        };
    }

}
