package swm.betterlife.antifragile.common.config;

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
import swm.betterlife.antifragile.common.jwt.filter.JwtExceptionFilter;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    private static final String[] PERMIT_PATHS = {
            "/auth", "/auth/**", "/login/**",
    };

    private static final String[] PERMIT_PATHS_POST_METHOD = {

    };

    private static final String[] PERMIT_PATHS_GET_METHOD = {

    };

    private static final String[] ALLOW_ORIGINS = {
            "http://localhost:3000",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.httpBasic(AbstractHttpConfigurer::disable);
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        );

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(PERMIT_PATHS).permitAll()
                .requestMatchers(POST, PERMIT_PATHS_POST_METHOD).permitAll()
                .requestMatchers(GET, PERMIT_PATHS_GET_METHOD).permitAll()
                .requestMatchers("/v1/products/own").denyAll()
                .anyRequest().authenticated()
        );

//        http.exceptionHandling(exceptionHandling -> {
//            exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint);
//        });

//        http.oauth2Login(oauth2 -> oauth2
//                .userInfoEndpoint(
//                        userInfoEndpoint -> userInfoEndpoint.userService(oauth2UserService))
//                .successHandler(oauth2LoginSuccessHandler)
//                .failureHandler(oauth2LoginFailureHandler)
//        );

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


}
