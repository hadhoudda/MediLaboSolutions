package com.medilabosolutions.front_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.medilabosolutions.front_app.proxies")
public class FrontAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrontAppApplication.class, args);
	}

}
