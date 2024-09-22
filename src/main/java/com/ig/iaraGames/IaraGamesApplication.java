package com.ig.iaraGames;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.ig.iaraGames")
@EnableJpaRepositories("com.ig.iaraGames")
public class IaraGamesApplication {
	public static void main(String[] args) {
		SpringApplication.run(IaraGamesApplication.class, args);
	}
}