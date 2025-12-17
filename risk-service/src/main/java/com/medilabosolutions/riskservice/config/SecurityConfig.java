package com.medilabosolutions.riskservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the Risk Service application.
 * <p>
 * Configures HTTP security, authentication, and user details service.
 * Basic authentication is used with an in-memory user.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the HTTP security filter chain.
     * <p>
     * - Disables CSRF protection (for simplicity or API usage).
     * - Requires authentication for "/api/**" endpoints.
     * - Permits public access to static resources and login page.
     * - Enables HTTP Basic authentication.
     * - Disables form-based login.
     *
     * @param http HttpSecurity object to configure
     * @return Configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").authenticated() // Protect API endpoints
                        .requestMatchers("/", "/login", "/images/**", "/css/**", "/js/**").permitAll() // Allow public access
                        .anyRequest().permitAll() // Permit all other requests
                )
                .httpBasic(Customizer.withDefaults()) // Enable HTTP Basic authentication
                .formLogin(AbstractHttpConfigurer::disable); // Disable default form login

        return http.build();
    }

    /**
     * Provides a password encoder bean using BCrypt.
     *
     * @return PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides an in-memory user details service.
     * <p>
     * Creates a single user with username "user" and password "1234" (BCrypt-encoded) with role "USER".
     *
     * @param encoder Password encoder to hash the user password
     * @return UserDetailsService instance with in-memory user
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails user = User.withUsername("user")
                .password(encoder.encode("1234")) // Encode password
                .roles("USER") // Assign role
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}
