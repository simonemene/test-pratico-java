package com.java.test.configuration;

import com.java.test.repository.UtenteRepository;
import com.java.test.security.CustomAuthenticationProvider;
import com.java.test.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

@Configuration
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity security)
			throws Exception {

			return security.authorizeHttpRequests(auth->
					auth.requestMatchers(HttpMethod.POST,"/api/prodotto").hasRole("ADMIN")
					.requestMatchers(HttpMethod.GET,"/api/prodotto/**").hasAnyRole("ADMIN","USER")
							.anyRequest().permitAll())
		.formLogin(AbstractHttpConfigurer::disable)
				.httpBasic(Customizer.withDefaults())
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(session->
						session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public CompromisedPasswordChecker compromisedPasswordChecker() {
		return new HaveIBeenPwnedRestApiPasswordChecker();
	}

	@Bean
	public AuthenticationProvider authenticationProvider(UserDetailsService service)
	{
		return new CustomAuthenticationProvider(service,passwordEncoder());
	}

	@Bean
	public UserDetailsService userDetailsService(UtenteRepository repository)
	{
		return new CustomUserDetailsService(repository);
	}



}
