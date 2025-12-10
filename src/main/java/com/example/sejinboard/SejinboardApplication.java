package com.example.sejinboard;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@ConfigurationPropertiesScan
@SpringBootApplication
public class SejinboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(SejinboardApplication.class, args);
	}

}
