package com.medilabosolutions.gatewayservice.config;

import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Gateway routes.
 *
 * <p>Defines both dynamic routes (based on Eureka service discovery)
 * and static routes (e.g., for serving front-end images).</p>
 */
@Configuration
public class ConfigRoutes {

    /**
     * Creates dynamic routes based on services registered in Eureka.
     *
     * <p>Each service discovered via ReactiveDiscoveryClient will automatically
     * have a route generated according to the DiscoveryLocatorProperties configuration.</p>
     *
     * @param rdc  ReactiveDiscoveryClient for service discovery
     * @param dlp  DiscoveryLocatorProperties for route configuration
     * @return DiscoveryClientRouteDefinitionLocator enabling dynamic routes
     */
    @Bean
    public DiscoveryClientRouteDefinitionLocator dynamicRoutes(ReactiveDiscoveryClient rdc,
                                                               DiscoveryLocatorProperties dlp) {
        return new DiscoveryClientRouteDefinitionLocator(rdc, dlp);
    }

    /**
     * Defines custom static routes for the gateway.
     *
     * <p>This example exposes front-end images through the gateway by
     * routing requests with path "/images/**" to the front-end service on localhost:8090.</p>
     *
     * @param builder RouteLocatorBuilder used to build routes
     * @return a RouteLocator containing static route definitions
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("front_images", r -> r.path("/images/**")
                        .uri("http://localhost:8090")) // Front-end port
                .build();
    }
}
