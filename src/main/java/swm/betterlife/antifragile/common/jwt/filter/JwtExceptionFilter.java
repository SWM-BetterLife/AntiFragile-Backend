package swm.betterlife.antifragile.common.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
        } catch (AuthenticationException e) {
//            ResponseBody<Void> responseBody
//                    = ResponseBody.fail(e.getMessage());
//            completeResponse(response, e, responseBody, UNAUTHORIZED.value());
        } catch (Exception e) {
//            ResponseBody<Void> responseBody
//                    = ResponseBody.fail(e.getMessage());
//            completeResponse(response, e, responseBody, BAD_REQUEST.value());
        }
    }

    private void makeResponse(
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
