package swm.betterlife.antifragile.domain.auth.service;

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
import swm.betterlife.antifragile.domain.auth.dto.LoginRequest;
import swm.betterlife.antifragile.domain.auth.dto.LoginResponse;
import swm.betterlife.antifragile.domain.auth.dto.SignUpRequest;
import swm.betterlife.antifragile.domain.auth.dto.TokenIssueResponse;
import swm.betterlife.antifragile.domain.member.dto.MemberDetailResponse;
import swm.betterlife.antifragile.domain.member.entity.LoginType;
import swm.betterlife.antifragile.domain.member.entity.Member;
import swm.betterlife.antifragile.domain.member.repository.MemberRepository;

import static swm.betterlife.antifragile.domain.member.entity.RoleType.ROLE_USER;

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

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        log.info("google password: {}", googlePassword);
        log.info("kakao password: {}", kakaoPassword);
        log.info("naver password: {}", naverPassword);
        String password = getPasswordByLoginType(loginRequest.loginType());

        Authentication authentication = getAuthenticate(loginRequest.email(), password);

        Member member = memberRepository.getMember(loginRequest.email());
        String accessToken
                = jwtProvider.generateAccessToken(authentication, loginRequest.loginType());
        return LoginResponse.from(member, new TokenIssueResponse(accessToken));
    }

    @Transactional
    public MemberDetailResponse signUp(SignUpRequest signUpRequest) {
        if (memberRepository.existsByEmail(signUpRequest.email())) {
            log.info("이미 존재하는 이메일 : {}", signUpRequest.email());
            throw new RuntimeException("이미 존재하는 이메일입니다.");  //todo: Custom Ex
        }

        String password = getPasswordByLoginType(signUpRequest.loginType());
        String encodedPassword = passwordEncoder.encode(password);

        Member member = Member.builder()
                .email(signUpRequest.email())
                .password(encodedPassword)
                .loginType(signUpRequest.loginType())
                .roleType(ROLE_USER)
                .build();

        return MemberDetailResponse.from(memberRepository.save(member));

    }

    private Authentication getAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
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
