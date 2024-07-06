package swm.betterlife.antifragile.common.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import swm.betterlife.antifragile.domain.auth.dto.LoginRequest;
import swm.betterlife.antifragile.domain.auth.dto.LoginResponse;
import swm.betterlife.antifragile.domain.auth.service.AuthService;
import swm.betterlife.antifragile.domain.member.entity.LoginType;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthService authService;

    @Override
    @Transactional
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {

        DefaultOAuth2User defaultOauth2 = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attribute = defaultOauth2.getAttributes();

        log.info("SuccessHandler : attribute: {}", attribute);

        boolean isExist = (boolean) attribute.get("isExist");
        String email = (String) attribute.get("email");
        LoginType loginType = (LoginType) attribute.get("loginType");

        log.info("isExist: {}", isExist);
        log.info("email: {}", email);
        log.info("loginType: {}", loginType);

        if (isExist) {
            /* 바로 로그인 */
            LoginRequest loginRequest =  new LoginRequest(email, loginType);
            LoginResponse loginResponse = authService.login(loginRequest);
            response.getWriter().write(loginResponse.toString());   //todo: ResponseBody 감싸기
        } else {
            /* 회원 가입 필요 */
            log.info("회원 가입 필요");
            // todo: 회원가입 Redirect 예정
        }

    }
}