package com.example.inicial1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Inicial1Application {
	private static final Logger logger = LoggerFactory.getLogger(Inicial1Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Inicial1Application.class, args);
//		System.out.println("Aplicación de detección de mutantes funcionando");
	}
}
