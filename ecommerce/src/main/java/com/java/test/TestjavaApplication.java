package com.java.test;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@OpenAPIDefinition(
		info= @Info(
				title = "",
				description= "",
				version = ""
		)
)
@EnableCaching
@SpringBootApplication
public class TestjavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestjavaApplication.class, args);
	}

}
