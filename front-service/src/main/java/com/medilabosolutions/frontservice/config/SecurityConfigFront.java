package com.medilabosolutions.frontservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration for the front application.
 *
 * <p>This class defines authentication and authorization rules,
 * login and logout behavior, and password encoding strategy.</p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfigFront {

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * @param http the {@link HttpSecurity} to configure
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if a security configuration error occurs
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection (acceptable for this application context)
                .csrf(cs -> cs.disable())
                .authorizeHttpRequests(auth -> auth
                        // Publicly accessible endpoints
                        .requestMatchers("/login", "/images/**", "/css/**", "/js/**", "/h2-console").permitAll()
                        // API endpoints require authentication
                        .requestMatchers("/api/**").authenticated()
                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                // Form-based login configuration
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/patients", true)
                        .permitAll()
                )
                // Logout configuration
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                // Enable HTTP Basic authentication
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    /**
     * Defines the password encoder used for hashing user passwords.
     *
     * @return a {@link PasswordEncoder} implementation
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures an in-memory user details service.
     *
     * <p>This configuration is suitable for development or demonstration
     * purposes. A persistent user store should be used in production.</p>
     *
     * @param encoder the password encoder used to encode the user password
     * @return a {@link UserDetailsService} instance
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails user = User.builder()
                .username("user")
                .password(encoder.encode("1234"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}
