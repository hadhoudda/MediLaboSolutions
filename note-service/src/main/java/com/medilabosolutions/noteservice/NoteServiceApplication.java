package com.medilabosolutions.back_patient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BackPatientApplication {

	public static void main(String[] args) {

		SpringApplication.run(BackPatientApplication.class, args);
	}

}
