package com.javaschool.config;

import com.javaschool.security.JwtAuthenticationEntryPoint;
import com.javaschool.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint entryPoint;
    private final JwtAuthenticationFilter filter;

    public SecurityConfig(UserDetailsService userDetailsService, JwtAuthenticationEntryPoint entryPoint, JwtAuthenticationFilter filter) {
        this.userDetailsService = userDetailsService;
        this.entryPoint = entryPoint;
        this.filter = filter;
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(c -> c.disable())
                .cors( c -> c.disable())
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/api/auth/login",
                                        "/api/auth/register",
                                        "/api/auth/verify",
                                        "api/auth/resend",
                                        "/api-docs.yaml",
                                        "/api-docs/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "api/alarms/schedule")
                                .permitAll()
                                .anyRequest().authenticated())
                .exceptionHandling(e ->
                        e.authenticationEntryPoint(entryPoint)
                ).sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
