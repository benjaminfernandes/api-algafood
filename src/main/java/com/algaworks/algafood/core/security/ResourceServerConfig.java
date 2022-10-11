package com.algaworks.algafood.core.security;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.formLogin()
			.and()
			.authorizeRequests()
				.antMatchers("/oauth/**").authenticated()//utilizado no fluxo authorization code
			.and()
			.csrf().disable()
			.cors()//Spring security permite o OPTIONS 
			.and()
			.oauth2ResourceServer()
			.jwt()
			.jwtAuthenticationConverter(jwtAuthenticationConverter());
			//.opaqueToken(); para tokens opacos e que utilizem introspeccao 
				
			/*.and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS) //sem session ID, API Stateless.
			
				.and()
					.csrf().disable(); */
		//csrf evita que o seja usado o sessionId em requisições quando a API não é stateless, se o sessionID é capturado por um hacker ele pode usá-lo para enviar requisições sem as credenciais
	}
	
	//Extrai as authorities do jwt recebido
	private JwtAuthenticationConverter jwtAuthenticationConverter() {
		var jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
			var authorities = jwt.getClaimAsStringList("authorities");
			
			if(authorities == null) {
				authorities = Collections.emptyList();
			}
			
			//Aula 23.25
			//recupera os escopos
			var scopesAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
			Collection<GrantedAuthority> grantedAuthorities = scopesAuthoritiesConverter.convert(jwt);
			
			grantedAuthorities.addAll(authorities.stream().map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList()));
			
			return grantedAuthorities;
		});
		
		
		return jwtAuthenticationConverter;
	}
	
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	//Configuração quando utiliza chave simétrica - utiliza o mesmo segredo
	/*@Bean
	public JwtDecoder jwtDecoder() {
		var secret = new SecretKeySpec("8a9sf5asdf6a4sd6f48sd45fa4sd65f48asd4f65ad4sf8d5d5d5d8sa65".getBytes(), "HmacSHA256");
		
		return NimbusJwtDecoder.withSecretKey(secret).build();
	}*/
	
	
}
