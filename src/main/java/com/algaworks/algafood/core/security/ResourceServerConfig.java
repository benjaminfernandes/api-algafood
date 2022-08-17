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
			.oauth2ResourceServer()
			.jwt();
			//.opaqueToken(); para tokens opacos e que utilizem introspeccao 
				
			/*.and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS) //sem session ID, API Stateless.
			
				.and()
					.csrf().disable(); */
		//csrf evita que o seja usado o sessionId em requisições quando a API não é stateless, se o sessionID é capturado por um hacker ele pode usá-lo para enviar requisições sem as credenciais
	}
	
	//Configuração quando utiliza chave simétrica - utiliza o mesmo segredo
	/*@Bean
	public JwtDecoder jwtDecoder() {
		var secret = new SecretKeySpec("8a9sf5asdf6a4sd6f48sd45fa4sd65f48asd4f65ad4sf8d5d5d5d8sa65".getBytes(), "HmacSHA256");
		
		return NimbusJwtDecoder.withSecretKey(secret).build();
	}*/
	
	
}
