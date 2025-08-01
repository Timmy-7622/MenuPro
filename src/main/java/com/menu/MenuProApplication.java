package com.menu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.menu.repository")
public class MenuProApplication {

	public static void main(String[] args) {
		SpringApplication.run(MenuProApplication.class, args);
	}

}
