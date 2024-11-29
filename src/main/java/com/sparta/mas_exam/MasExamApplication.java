package com.sparta.mas_exam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MasExamApplication {

	public static void main(String[] args) {
		SpringApplication.run(MasExamApplication.class, args);
	}

}
