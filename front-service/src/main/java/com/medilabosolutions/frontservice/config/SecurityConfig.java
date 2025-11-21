package com.medilabosolutions.frontservice.config;


//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//
//@Configuration
//@EnableWebFluxSecurity
public class SecurityConfig {
//
//    @Bean
//    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeExchange(auth -> auth
//                        .pathMatchers("/login", "/images/**").permitAll() // FRONT
//                        .pathMatchers("/api/**").authenticated()          // BACK
//                        .anyExchange().permitAll()
//                )
//                // Activation formLogin pour Thymeleaf
//                .formLogin(form -> form
//                        .loginPage("/login")          // page login Thymeleaf
//                        .authenticationSuccessHandler((webFilterExchange, authentication) -> {
//                            // après login, redirige vers /patients
//                            return webFilterExchange.getExchange().getResponse().setComplete();
//                        })
//                )
//                // Désactiver httpBasic pour l’UI, mais garder pour API si tu veux
//                .httpBasic(Customizer.withDefaults());
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public MapReactiveUserDetailsService userDetailsService(PasswordEncoder encoder) {
//        UserDetails user = User.withUsername("user")
//                .password(encoder.encode("1234"))
//                .roles("USER")
//                .build();
//
//        return new MapReactiveUserDetailsService(user);
//    }
//}
}