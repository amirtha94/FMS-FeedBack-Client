package com.fms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FmsFeedbackClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(FmsFeedbackClientApplication.class, args);
	}

}
