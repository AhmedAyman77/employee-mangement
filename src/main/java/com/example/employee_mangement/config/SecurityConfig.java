package com.example.employee_mangement.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(c -> {
                CorsConfigurationSource source = corsConfigurationSource();
                c.configurationSource(source);
            })
            // .cors(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers(
                    "/auth/signup",
                    "/auth/login",
                    "auth/forgot-password/{username}",
                    "auth/reset-password"
                ).permitAll()
                .requestMatchers(HttpMethod.GET, "/employees").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/employees/{id}").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.POST, "/employees").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/employees/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/employees/{id}").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.POST, "/employees/{id}/leave-requests").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.GET, "/employees/{id}/leave-requests").hasAnyRole("ADMIN", "USER")
                .anyRequest()
                .authenticated();
                // instead of .authenticated() i can use .hasRole("ADMIN"); <- will not let normal users to access the endpoint.
            })
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            // .httpBasic(Customizer.withDefaults())
            .authenticationManager(authenticationManager(http));
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        var authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
