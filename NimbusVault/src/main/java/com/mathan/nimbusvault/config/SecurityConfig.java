
package com.mathan.nimbusvault.config;

import com.mathan.nimbusvault.security.JwtAutheFilter;
import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAutheFilter jwtAutheFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)

            .authorizeHttpRequests(auth -> auth
            	    .requestMatchers(
            	        "/api/v1/auth/register",
            	        "/api/v1/auth/login",
            	        "/api/v1/auth/refresh-token",
            	        "/api/v1/auth/verify-email",
            	        "/api/v1/auth/google",
            	        "/api/v1/auth/forgot-password",
            	        "/api/v1/auth/forgot-password/**",
            	        "/api/v1/auth/verify-mobile-otp",
            	        "/api/v1/auth/reset-password",
            	        "/api/v1/auth/resend-mobile-otp",
            	        "/api/v1/auth/resend-email-verification"
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
