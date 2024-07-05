package swm.betterlife.antifragile.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
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

import java.util.List;
import swm.betterlife.antifragile.common.oauth.Oauth2LoginSuccessHandler;
import swm.betterlife.antifragile.common.oauth.Oauth2UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final Oauth2UserService oauth2UserService;
    private final Oauth2LoginSuccessHandler oauth2LoginSuccessHandler;

    private static final String[] PERMIT_PATHS = {
            "/auth/**", "/token/re-issuance", "/**"
    };

    private static final String[] PERMIT_PATHS_POST_METHOD = {

    };

    private static final String[] PERMIT_PATHS_GET_METHOD = {

    };

    private static final String[] ALLOW_ORIGINS = {
            "http://localhost:8080",
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
                .requestMatchers(PERMIT_PATHS).permitAll()
//                .requestMatchers(POST, PERMIT_PATHS_POST_METHOD).permitAll()
//                .requestMatchers(GET, PERMIT_PATHS_GET_METHOD).permitAll()
                .anyRequest().authenticated()
        );

        http.exceptionHandling(exceptionHandling -> {
            exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint);
        });

        http.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(
                        userInfoEndpoint -> userInfoEndpoint.userService(oauth2UserService))
                .successHandler(oauth2LoginSuccessHandler)
//                .failureHandler(oauth2LoginFailureHandler)
        );

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthFilter.class);

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

    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled",havingValue = "true")
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console());
    }

}
