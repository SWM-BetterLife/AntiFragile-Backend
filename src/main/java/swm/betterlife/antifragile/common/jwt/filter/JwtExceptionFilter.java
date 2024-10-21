package swm.betterlife.antifragile.common.jwt.filter;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import swm.betterlife.antifragile.common.exception.HumanMemberCannotBeAccessedException;
import swm.betterlife.antifragile.common.exception.TokenExpiredException;
import swm.betterlife.antifragile.common.response.ResponseBody;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws IOException {
        /* ControllerAdvice와 같은 ExHandler 역할 수행 */

        try {
            filterChain.doFilter(request, response);
        } catch (TokenExpiredException | HumanMemberCannotBeAccessedException e) {
            ResponseBody<Void> responseBody
                    = ResponseBody.fail(e.getMessage());
            setErrorResponse(response, e, responseBody, FORBIDDEN.value());
        } catch (AuthenticationException e) {
            ResponseBody<Void> responseBody
                    = ResponseBody.fail(e.getMessage());
            setErrorResponse(response, e, responseBody, UNAUTHORIZED.value());
        } catch (Exception e) {
            ResponseBody<Void> responseBody
                    = ResponseBody.fail(e.getMessage());
            setErrorResponse(response, e, responseBody, BAD_REQUEST.value());
        }
    }

    private void setErrorResponse(
            HttpServletResponse response,
            Exception e,
            ResponseBody responseBody,
            int status
    ) throws IOException {
        response.setStatus(status);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        String bodyString = objectMapper
                .writeValueAsString(responseBody);
        response.getWriter().write(bodyString);
    }
}
