package com.algaworks.algafood.core.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.anyRequest().authenticated()
			.and()
			.cors()//Spring security permite o OPTIONS 
			.and()
			.oauth2ResourceServer().opaqueToken();
				
			/*.and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS) //sem session ID, API Stateless.
			
				.and()
					.csrf().disable(); */
		//csrf evita que o seja usado o sessionId em requisições quando a API não é stateless, se o sessionID é capturado por um hacker ele pode usá-lo para enviar requisições sem as credenciais
	}
	
}
