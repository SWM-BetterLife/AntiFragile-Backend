package swm.betterlife.antifragile.common.jwt.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) {
        log.error("CustomAuthenticationEntryPoint : Not Authenticated Request", authException);
        sendResponse(authException);
    }

    private void sendResponse(AuthenticationException authException) {
        if (authException instanceof BadCredentialsException) {
            log.info("Bad credentials");
            throw new BadCredentialsException(authException.getMessage());  //todo: Custom ex
        } else if (authException instanceof InternalAuthenticationServiceException) {
            log.info("Internal authentication service exception");
            throw new InsufficientAuthenticationException(authException.getMessage());
        } else if (authException instanceof InsufficientAuthenticationException) {
            log.info("Insufficient authentication");
            throw new InsufficientAuthenticationException(authException.getMessage());
        } else {
            log.info("Authentication exception : {}", authException.getMessage());
        }
    }
}
