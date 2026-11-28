
package com.mathan.nimbusvault.config;

import com.mathan.nimbusvault.security.JwtAutheFilter;
import com.mathan.nimbusvault.security.CustomAuthenticationEntryPoint;
import com.mathan.nimbusvault.security.CustomAccessDeniedHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAutheFilter jwtAutheFilter;

    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    private final CustomAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(
            JwtAutheFilter jwtAutheFilter,
            CustomAuthenticationEntryPoint authenticationEntryPoint,
            CustomAccessDeniedHandler accessDeniedHandler) {

        this.jwtAutheFilter = jwtAutheFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)

            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            )

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/google-login.html",
                    "/api/v1/auth/register",
                    "/api/v1/auth/login",
                    "/api/v1/auth/refresh-token",
                    "/api/v1/auth/verify-email",
                    "/api/v1/auth/forgot-password",
                    "/api/v1/auth/forgot-password/**",
                    "/api/v1/auth/verify-mobile-otp",
                    "/api/v1/auth/reset-password",
                    "/api/v1/auth/resend-mobile-otp",
                    "/api/v1/auth/resend-email-verification",
                    "/api/v1/auth/google"
                ).permitAll()
                .anyRequest().authenticated()
            )

            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .addFilterBefore(
                jwtAutheFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
