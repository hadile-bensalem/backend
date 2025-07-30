package com.example.hadilprojectspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.hadilprojectspring")
public class HadilprojectspringApplication {

	public static void main(String[] args) {
		SpringApplication.run(HadilprojectspringApplication.class, args);
	}

}
