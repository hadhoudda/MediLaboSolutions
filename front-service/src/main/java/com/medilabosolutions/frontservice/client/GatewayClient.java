package com.medilabosolutions.frontservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "gateway-service",
        url = "${gateway.url}"
)
public interface GatewayClient {
}