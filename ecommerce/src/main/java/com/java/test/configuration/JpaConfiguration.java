package com.java.test.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfiguration {

	@Bean
	public AuditorAware<String> auditorProvider() {
		return ()->
		{
			SecurityContext contestoSecurity = SecurityContextHolder.getContext();
			Authentication auth = contestoSecurity.getAuthentication();

			if(null == auth || !auth.isAuthenticated())
			{
				return Optional.of("UTENTE DI SISTEMA");
			}

			return Optional.ofNullable(auth.getName());
		};

	}


}
