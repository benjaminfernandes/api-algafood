package com.algaworks.algafood.core.security.authorizationserver;

import java.io.InputStream;
import java.security.KeyStore;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class AuthorizationServerConfig {

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE) // Teremos diversos filterChain, um para o AS e outro para o resource server.
										// Com esta anotação ele inicia com prioridade
	public SecurityFilterChain authFilterChain(HttpSecurity http) throws Exception {
		// OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http); //Aplica
		// diversas configs padrões do AS

		// Customizando
		// Aula 27.18 Customizando a página de consentimento de scopos
		OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer<>();
		authorizationServerConfigurer.authorizationEndpoint(customizer -> customizer.consentPage("/oauth2/consent"));

		RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

		http.requestMatcher(endpointsMatcher)
				.authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
				.csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher)).apply(authorizationServerConfigurer);

		return http.formLogin(customizer -> customizer.loginPage("/login")).build();
	}

	// Responsável por escrever quem é o AS que assina os tokens - URL
	@Bean
	public ProviderSettings providerSettings(AlgaFoodSecurityProperties properties) {
		return ProviderSettings.builder().issuer(properties.getProviderUrl()).build();
	}

	// Aqui guarda os clientes do AS
	// Configuração para token opaco com cliente em memória
	/*
	 * @Bean public RegisteredClientRepository
	 * registeredClientRepository(PasswordEncoder encoder) {
	 * 
	 * RegisteredClient algafoodBackend = RegisteredClient .withId("1")//Id que vai
	 * no BD - não o Id do cliente .clientId("algafood-backend")
	 * .clientSecret(encoder.encode("backend123"))
	 * .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)//
	 * Basic .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
	 * .scope("READ") .tokenSettings(TokenSettings.builder()
	 * .accessTokenFormat(OAuth2TokenFormat.REFERENCE) //Token opaco
	 * .accessTokenTimeToLive(Duration.ofMinutes(30)) .build()) .build();
	 * 
	 * return new
	 * InMemoryRegisteredClientRepository(Arrays.asList(algafoodBackend)); }
	 */

	// Aqui guarda os clientes do AS
	// Configuração para token JWT com cliente em memória
	@Bean
	public RegisteredClientRepository registeredClientRepository(PasswordEncoder encoder, JdbcOperations operations) {

		/*
		 * Clients in memory //Config Client Credentials RegisteredClient
		 * algafoodBackend = RegisteredClient .withId("1")//Id que vai no BD - não o Id
		 * do cliente .clientId("algafood-backend")
		 * .clientSecret(encoder.encode("backend123"))
		 * .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)//
		 * Basic .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
		 * .scope("READ") .tokenSettings(TokenSettings.builder()
		 * .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED) //Token JWT transparente
		 * .accessTokenTimeToLive(Duration.ofMinutes(30)) .build()) .build();
		 * 
		 * //Config Authorization Code RegisteredClient algafoodWeb = RegisteredClient
		 * .withId("2") .clientId("algafood-web")
		 * .clientSecret(encoder.encode("web123"))
		 * .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
		 * .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
		 * .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN) .scope("READ")
		 * .scope("WRITE") .tokenSettings(TokenSettings.builder()
		 * .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED) //Token JWT transparente
		 * .accessTokenTimeToLive(Duration.ofMinutes(15))
		 * .reuseRefreshTokens(false)//opção recomendável por questões de segurança
		 * .refreshTokenTimeToLive(Duration.ofDays(1)) .build())
		 * .redirectUri("http://127.0.0.1:8080/authorized")
		 * .redirectUri("http://127.0.0.1:8080/swagger-ui/oauth2-redirect.html")
		 * .clientSettings(ClientSettings.builder() .requireAuthorizationConsent(true)
		 * .build()) .build();
		 * 
		 * 
		 * RegisteredClient foodAnalytics = RegisteredClient .withId("3")
		 * .clientId("foodanalytics") .clientSecret(encoder.encode("web123"))
		 * .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
		 * .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
		 * .scope("READ") .scope("WRITE") .tokenSettings(TokenSettings.builder()
		 * .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED) //Token JWT transparente
		 * .accessTokenTimeToLive(Duration.ofMinutes(30)) .build())
		 * .redirectUri("http://www.foodanalytics,local:8082")
		 * .clientSettings(ClientSettings.builder() .requireAuthorizationConsent(false)
		 * .build()) .build();
		 */
		JdbcRegisteredClientRepository repository = new JdbcRegisteredClientRepository(operations);
		// Usado somente para criar rapidamente os clients
		// repository.save(algafoodBackend);
		// repository.save(algafoodWeb);
		// repository.save(foodAnalytics);

		return repository;
		// return new InMemoryRegisteredClientRepository(Arrays.asList(algafoodBackend,
		// algafoodWeb, foodAnalytics));
	}

	@Bean
	public OAuth2AuthorizationService oAuth2AuthorizationService(JdbcOperations jdbcOperations,
			RegisteredClientRepository registeredClientRepository) {

		return new JdbcOAuth2AuthorizationService(jdbcOperations, registeredClientRepository);
	}

	@Bean
	public JWKSource<SecurityContext> jwtSource(JwtKeyStoreProperties properties) throws Exception {
		char[] keyStorePass = properties.getPassword().toCharArray();
		String keypairAlias = properties.getKeypairAlias();

		Resource jksLocation = properties.getJksLocation();
		InputStream inputStream = jksLocation.getInputStream();

		// Cria o KeyStore com o certificado
		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(inputStream, keyStorePass);

		// Carrego o keyStore
		RSAKey rsaKey = RSAKey.load(keyStore, keypairAlias, keyStorePass);

		return new ImmutableJWKSet<>(new JWKSet(rsaKey));
	}

	// Esta config foi feita para resolver o problema demonstrado na aula 27.12 que
	// alterou o retorno do método loadUserByUsername da classe
	// JpaUserDetailsService
	@Bean
	public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer(UsuarioRepository usuarioRepository) {
		return context -> {
			Authentication authentication = context.getPrincipal();
			if (authentication.getPrincipal() instanceof User) {// esta regra não se aplica para o client credentials
				User user = (User) authentication.getPrincipal();

				Usuario usuario = usuarioRepository.findByEmail(user.getUsername()).orElseThrow();

				Set<String> authorities = new HashSet<>();
				for (GrantedAuthority authority : user.getAuthorities()) {
					authorities.add(authority.getAuthority());
				}

				context.getClaims().claim("usuario_id", usuario.getId());
				context.getClaims().claim("authorities", authorities);
			}
		};
	}

	@Bean
	public OAuth2AuthorizationConsentService consentService(JdbcOperations operations,
			RegisteredClientRepository repository) {
		// return new InMemoryOAuth2AuthorizationConsentService();
		return new JdbcOAuth2AuthorizationConsentService(operations, repository);
	}
	
	@Bean
	public Oauth2AuthorizationQueryService authorizationQueryService(JdbcOperations operations) {
		return new JdbcOauth2AuthorizationQueryService(operations);
	}

}
