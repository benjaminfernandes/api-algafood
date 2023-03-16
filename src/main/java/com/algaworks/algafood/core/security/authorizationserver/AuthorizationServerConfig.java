package com.algaworks.algafood.core.security.authorizationserver;

import java.time.Duration;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AuthorizationServerConfig {

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)//Teremos diversos filterChain, um para o AS e outro para o resource server. Com esta anotação ele inicia com prioridade
	public SecurityFilterChain authFilterChain(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http); //Aplica diversas configs padrões do AS
		return http.build();
	}
	
	//Responsável por escrever quem é o AS que assina os tokens - URL
	@Bean
	public ProviderSettings providerSettings(AlgaFoodSecurityProperties properties) {
		return ProviderSettings.builder()
				.issuer(properties.getProviderUrl())
				.build();
	}
	
	//Aqui guarda os clientes do AS
	@Bean
	public RegisteredClientRepository registeredClientRepository(PasswordEncoder encoder) {
		
		RegisteredClient algafoodBackend = RegisteredClient
				.withId("1")//Id que vai no BD - não o Id do cliente
				.clientId("algafood-backend")
				.clientSecret(encoder.encode("backend123"))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)//Basic 
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.scope("READ")
				.tokenSettings(TokenSettings.builder()
						.accessTokenFormat(OAuth2TokenFormat.REFERENCE) //Token opaco ou JWT transparente
						.accessTokenTimeToLive(Duration.ofMinutes(30))
						.build())
				.build();
		
		return new InMemoryRegisteredClientRepository(Arrays.asList(algafoodBackend));
	}
}
