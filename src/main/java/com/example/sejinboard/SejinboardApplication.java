package com.example.sejinboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SejinboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(SejinboardApplication.class, args);
	}

}
