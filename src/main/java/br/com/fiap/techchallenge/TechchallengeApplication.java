package br.com.fiap.techchallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TechchallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechchallengeApplication.class, args);
	}

}
