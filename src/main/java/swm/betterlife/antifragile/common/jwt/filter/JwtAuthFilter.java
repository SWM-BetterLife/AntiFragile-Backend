package swm.betterlife.antifragile.common.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static swm.betterlife.antifragile.common.jwt.constant.JwtConstant.AUTHORIZATION_HEADER;
import static swm.betterlife.antifragile.common.jwt.constant.JwtConstant.BEARER_PREFIX;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

//        private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = extractTokenFromRequest(request);

        log.info("JWT token: {}", token);

        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

//        String email = jwtProvider.getEmail(token);
//        String provider = jwtProvider.getProvider(token);

//        try {
//            jwtProvider
//                    .saveAuthInContextHolder(email, ProviderType.valueOf(provider));
//        } catch (MemberNotFoundException e) {
//            throw new TokenNotValidatedException();
//        }
        filterChain.doFilter(request, response);
    }


    private String extractTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}
