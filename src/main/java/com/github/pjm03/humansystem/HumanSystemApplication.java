package com.github.pjm03.humansystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class HumanSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(HumanSystemApplication.class, args);
	}
}
