package com.medilabosolutions.gateway_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(auth -> auth
                        .pathMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()
                        .pathMatchers("/patients/**", "/patient/**").authenticated()
                        .anyExchange().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .authenticationSuccessHandler((webFilterExchange, authentication) -> {
                            // Redirection vers /patients après login
                            webFilterExchange.getExchange().getResponse()
                                    .setStatusCode(HttpStatus.FOUND);
                            webFilterExchange.getExchange().getResponse()
                                    .getHeaders().setLocation(URI.create("/front-app/patients"));
                            return webFilterExchange.getExchange().getResponse().setComplete();
                        })
                        .authenticationFailureHandler((webFilterExchange, exception) -> {
                            // Redirection vers /login?error en cas d'échec
                            webFilterExchange.getExchange().getResponse()
                                    .setStatusCode(HttpStatus.FOUND);
                            webFilterExchange.getExchange().getResponse()
                                    .getHeaders().setLocation(URI.create("/login?error"));
                            return webFilterExchange.getExchange().getResponse().setComplete();
                        })
                )
                .logout(logout -> logout.logoutUrl("/logout"));

        return http.build();
    }


    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        var user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("1234")
                .roles("ADMIN")
                .build();
        return new MapReactiveUserDetailsService(user);
    }
}
