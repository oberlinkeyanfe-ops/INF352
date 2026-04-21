package com.inf352.bankapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final ApiTokenAuthenticationFilter apiTokenAuthenticationFilter;

    public SecurityConfig(ApiTokenAuthenticationFilter apiTokenAuthenticationFilter) {
        this.apiTokenAuthenticationFilter = apiTokenAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/api-docs", "/api-docs/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/users")
                        .permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());

        http.addFilterBefore(apiTokenAuthenticationFilter,
                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
