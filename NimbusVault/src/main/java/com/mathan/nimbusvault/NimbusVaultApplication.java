package com.mathan.nimbusvault;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NimbusVaultApplication {

	public static void main(String[] args) {
		
		 TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));		
		SpringApplication.run(NimbusVaultApplication.class, args);
	}

}
