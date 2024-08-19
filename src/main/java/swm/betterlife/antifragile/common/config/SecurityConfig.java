package swm.betterlife.antifragile.common.config;

import static org.springframework.http.HttpMethod.DELETE;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import swm.betterlife.antifragile.common.jwt.filter.JwtAuthFilter;
import swm.betterlife.antifragile.common.jwt.filter.JwtAuthenticationEntryPoint;
import swm.betterlife.antifragile.common.jwt.filter.JwtExceptionFilter;
import swm.betterlife.antifragile.common.jwt.filter.SoftDeleteFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final SoftDeleteFilter softDeleteFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private static final String[] PERMIT_PATHS = {
        "/auth", "/auth/**", "token/**", "/health-check"
    };

    private static final String[] PERMIT_QUERY_PARAM_PATHS = {
        "/members/duplication-check", "/members/status", "/llm-models"
    };

    private static final String[] AUTH_DELETE_PATHS = {
        "/auth",
    };

    private static final String[] ALLOW_ORIGINS = {
        "http://localhost:8080"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.httpBasic(AbstractHttpConfigurer::disable);
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests(authorize -> authorize
            .requestMatchers(DELETE, AUTH_DELETE_PATHS).authenticated()
            .requestMatchers(PERMIT_PATHS).permitAll()
            .requestMatchers(
                request -> Arrays.stream(PERMIT_QUERY_PARAM_PATHS)
                    .anyMatch(path -> request.getRequestURI().startsWith(path))
            ).permitAll()
            .anyRequest().authenticated()
        );

        http.exceptionHandling(exceptionHandling -> {
            exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint);
        });

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthFilter.class)
                .addFilterAfter(softDeleteFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(ALLOW_ORIGINS));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.addExposedHeader("Authorization");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
