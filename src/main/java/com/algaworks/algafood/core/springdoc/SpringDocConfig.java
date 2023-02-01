package com.algaworks.algafood.core.springdoc;

import java.util.Arrays;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
@SecurityScheme(name = "security_auth", type = SecuritySchemeType.OAUTH2,  //utilizado o fluxo authorization code, pois é o fluxo mais recomendado para interagir com o frontend
flows = @OAuthFlows(authorizationCode = @OAuthFlow(authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}", 
tokenUrl = "${springdoc.oAuthFlow.tokenUrl}", 
scopes = {@OAuthScope(name = "READ", description = "read scope"), 
		@OAuthScope(name = "WRITE", description = "write scope")})))
public class SpringDocConfig {

	/*@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.info(new Info().title("Algafood API")
						.version("v1")
						.description("REST API AlgaFood")
						.license(new License()
								.name("Apache 2.0")
								.url("http://springdoc.com")))
				.externalDocs(new ExternalDocumentation()
						.description("Documentação externa detalhada")
						.url("https://algaworks.com"));
	}*/
	
	//Configuração quando houver a necessidade de ter mais de uma documentação no projeto
	
	@Bean
	public GroupedOpenApi groupedOpenApiV2() {
		return GroupedOpenApi.builder()
				.group("Algafood API V2")
				.pathsToMatch("/v2/**")
				.addOpenApiCustomiser(openApi -> {
					openApi.info(new Info().title("Algafood API V2 ")
							.version("v2")
							.description("REST API AlgaFood")
							.license(new License()
									.name("Apache 2.0")
									.url("http://springdoc.com")))
					.externalDocs(new ExternalDocumentation()
							.description("Documentação externa detalhada")
							.url("https://algaworks.com"))
					.tags(Arrays.asList(new Tag().name("Cidades").description("Gerencia as cidades")));
				})
				.build();
	}
	
	@Bean
	public GroupedOpenApi groupedOpenApiV1() {
		return GroupedOpenApi.builder()
				.group("Algafood API")
				.pathsToMatch("/v1/**")
				.addOpenApiCustomiser(openApi -> {
					openApi.info(new Info().title("Algafood API")
							.version("v1")
							.description("REST API AlgaFood")
							.license(new License()
									.name("Apache 2.0")
									.url("http://springdoc.com")))
					.externalDocs(new ExternalDocumentation()
							.description("Documentação externa detalhada")
							.url("https://algaworks.com"))
					.tags(Arrays.asList(new Tag().name("Cidades").description("Gerencia as cidades")));
				})
				.build();
	}
}
