package com.medilabosolutions.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Security configuration for the Gateway service using Spring WebFlux.
 *
 * <p>Defines HTTP security rules for API endpoints and static resources.
 * Configures HTTP Basic authentication for APIs and sets up a reactive
 * in-memory user for authentication.</p>
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfigGateway {

    /**
     * Configures the security filter chain for the Gateway.
     *
     * <p>Protects /api/** routes with authentication, allows public access
     * to front-end assets and other paths, disables CSRF and form login.</p>
     *
     * @param http ServerHttpSecurity instance to configure
     * @return SecurityWebFilterChain configured for the Gateway
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(auth -> auth
                        // Protected API routes
                        .pathMatchers("/api/**").authenticated()

                        // Publicly accessible routes (frontend assets)
                        .pathMatchers("/**","/login", "/images/**", "/css/**", "/js/**").permitAll()

                        // Everything else allowed
                        .anyExchange().permitAll()
                )
                .httpBasic(Customizer.withDefaults()) // HTTP Basic auth for /api/** only
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable); // Disable form login
        return http.build();
    }

    /**
     * Password encoder bean using BCrypt.
     *
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * In-memory reactive user details service.
     *
     * <p>Defines a single user with username "user", password "1234" (encoded),
     * and role "USER".</p>
     *
     * @param encoder PasswordEncoder bean
     * @return MapReactiveUserDetailsService containing the user
     */
    @Bean
    public MapReactiveUserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails user = User.withUsername("user")
                .password(encoder.encode("1234"))
                .roles("USER")
                .build();

        return new MapReactiveUserDetailsService(user);
    }
}
