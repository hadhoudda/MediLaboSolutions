package com.medilabosolutions.frontservice.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * Feign configuration class.
 * <p>
 * This configuration adds a Basic Authentication interceptor
 * to all Feign client requests.
 */
public class FeignConfig {

    /**
     * Configures a Basic Authentication interceptor for Feign clients.
     *
     * @return a {@link BasicAuthRequestInterceptor} configured with credentials
     */
    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("user", "1234");
    }
}
