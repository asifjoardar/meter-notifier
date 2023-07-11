package com.asif.meternotifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MeterNotifierApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeterNotifierApplication.class, args);
	}

}
