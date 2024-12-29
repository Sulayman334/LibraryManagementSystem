package com.example.LibraryManagementSystem.security;

/*
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class LibrarySecurityConfig {

    private static final String [] SECURED_URLs = {"/users/**","/students/**","/books/**"};
    private static final String [] UN_SECURED_URLs = {
            "/books/all",
            "/books/get/{bookId}",
            "/students/get/{studentId}",
            "/students/all",
            "/book-transaction/**",
            "/library-attendance/**",
            "/authenticate/**",
            "/users/all", // working
            "/users/get/{userId}" //working


    };

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(UN_SECURED_URLs).permitAll()
                        .requestMatchers(SECURED_URLs).hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                */
/*.formLogin(AbstractAuthenticationFilterConfigurer::permitAll
                )*//*

                .httpBasic(basic -> basic
                        .realmName("My Security Realm")
                )
                .build();
    }
}
*/

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class LibrarySecurityConfig {

    private static final String[] SECURED_URLs = {"/users/**", "/students/**", "/books/**"};
    private static final String[] UN_SECURED_URLs = {
            "/books/all",
            "/books/get/{bookId}",
            "/students/get/{studentId}",
            "/students/all",
            "/book-transaction/**",
            "/library-attendance/**",
            "/authenticate/**",
            "/users/all",
            "/users/get/{userId}",
            "/auth/login",    // Added login endpoint
            "/auth/logout"    // Added logout endpoint
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(UN_SECURED_URLs).permitAll()
                        .requestMatchers(SECURED_URLs).hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/auth/login")
                        .successHandler((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value());
                            response.getWriter().write("{\"message\": \"Login successful\"}");
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.getWriter().write("{\"error\": \"Invalid credentials\"}");
                        })
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value());
                            response.getWriter().write("{\"message\": \"Logout successful\"}");
                        })
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                )
                .httpBasic(basic -> basic
                        .realmName("My Security Realm")
                )
                .build();
    }
}