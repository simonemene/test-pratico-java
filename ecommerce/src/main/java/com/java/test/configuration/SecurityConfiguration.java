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
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

@EnableMethodSecurity
@Configuration
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity security)
			throws Exception {

			return security.authorizeHttpRequests(auth->
					auth.requestMatchers("/h2-console/**").permitAll()
							.requestMatchers(
									"/swagger-ui/**",
									"/v3/api-docs/**",
									"/swagger-ui.html"
							).permitAll()
							.requestMatchers(HttpMethod.POST,"/api/prodotto").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.GET,"/api/prodotto/**").hasAnyRole("ADMIN","USER")
							.requestMatchers(HttpMethod.PATCH, "/api/ordine/*/stato/*")
							.hasRole("ADMIN")
							.requestMatchers(HttpMethod.GET, "/api/ordine").hasRole("ADMIN")
							.requestMatchers(HttpMethod.GET, "/api/ordine/paginati").hasRole("ADMIN")
							.requestMatchers(HttpMethod.GET, "/api/ordine/*/paginati").hasRole("ADMIN")
							.requestMatchers(HttpMethod.GET, "/api/ordine/utente/paginati").hasAnyRole("USER","ADMIN")
							.requestMatchers("/api/ordine/**").hasAnyRole("ADMIN","USER")
							.requestMatchers("/api/utente/informazioni").hasRole("USER")
							.requestMatchers("/api/utente/*/informazioni").hasRole("ADMIN")
							.requestMatchers(HttpMethod.GET,"/api/utente").hasRole("ADMIN")
							.requestMatchers(HttpMethod.POST,"/api/utente").permitAll()
							.requestMatchers("/api/**").authenticated()
							.anyRequest().permitAll())
		.formLogin(AbstractHttpConfigurer::disable)
				.httpBasic(Customizer.withDefaults())
				.csrf(AbstractHttpConfigurer::disable)

				.sessionManagement(session->
						session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.headers(headers -> headers
							.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
					)
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
