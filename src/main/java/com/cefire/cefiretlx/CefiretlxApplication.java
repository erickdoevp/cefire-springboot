package com.cefire.cefiretlx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CefiretlxApplication {

	public static void main(String[] args) {
		SpringApplication.run(CefiretlxApplication.class, args);
	}

}
