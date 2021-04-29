package com.brewmes.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.brewmes.*")
public class CoreApplication {

	public static void main(String[] args) {
		System.out.println("hello CI");
		SpringApplication.run(CoreApplication.class, args);
	}

}
