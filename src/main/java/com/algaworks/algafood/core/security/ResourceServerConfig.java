package com.algaworks.algafood.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) //para funcionar os filters
@EnableWebSecurity
public class ResourceServerConfig {

	
	@Bean
	public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
		
		http
			.authorizeHttpRequests()
			.antMatchers("/oauth2/**").authenticated()
			.and()
			.csrf().disable()
			.cors().and()
			//.oauth2ResourceServer().opaqueToken(); quando é utilizado o token opaco
			.oauth2ResourceServer().jwt();
		
		return http.formLogin(Customizer.withDefaults())//É também adicionada esta config aqui pois o AS está no mesmo projeto do Resource Server
				.build();
	}
	
	
}
