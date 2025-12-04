package com.medilabosolutions.riskservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.medilabosolutions.riskservice.client")
public class RiskServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RiskServiceApplication.class, args);
	}

}
