package com.medilabosolutions.patientservice.config;

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
 * Security configuration for the Patient Service.
 * This class sets up basic HTTP security with:
 * - HTTP Basic authentication
 * - Disabled CSRF for simplicity (use with caution in production)
 * - In-memory user for testing purposes
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain.
     *
     * @param http HttpSecurity object to configure security rules
     * @return SecurityFilterChain object
     * @throws Exception in case of configuration errors
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        // Require authentication for all /api/** endpoints
                        .requestMatchers("/api/**").authenticated()
                        // Permit all requests to static resources and public pages
                        .requestMatchers("/", "/login", "/images/**", "/css/**", "/js/**").permitAll()
                        .anyRequest().permitAll()
                )

                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form.disable());

        return http.build();
    }

    /**
     * Password encoder bean using BCrypt.
     * BCrypt is a strong hashing function recommended for storing passwords.
     *
     * @return PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures an in-memory user details service.
     *
     * Creates a single user with username "user" and password "1234" (encoded with BCrypt).
     * In a real application, replace with a database-backed UserDetailsService.
     *
     * @param encoder PasswordEncoder used to encode the user password
     * @return UserDetailsService instance
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails user = User.withUsername("user")
                .password(encoder.encode("1234"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

}
