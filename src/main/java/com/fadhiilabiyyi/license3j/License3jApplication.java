package com.fadhiilabiyyi.license3j;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class License3jApplication {

	public static void main(String[] args) {
		SpringApplication.run(License3jApplication.class, args);
	}

	public static ObjectMapper om = new ObjectMapper()
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
			.registerModule(new JavaTimeModule());;
}
