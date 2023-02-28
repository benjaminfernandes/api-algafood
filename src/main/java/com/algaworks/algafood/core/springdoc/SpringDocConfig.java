package com.algaworks.algafood.core.springdoc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.algaworks.algafood.api.exceptionhandler.Problem;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
@SecurityScheme(name = "security_auth", type = SecuritySchemeType.OAUTH2, // utilizado o fluxo authorization code, pois
																			// é o fluxo mais recomendado para interagir
																			// com o frontend
		flows = @OAuthFlows(authorizationCode = @OAuthFlow(authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}", tokenUrl = "${springdoc.oAuthFlow.tokenUrl}", scopes = {
				@OAuthScope(name = "READ", description = "read scope"),
				@OAuthScope(name = "WRITE", description = "write scope") })))
public class SpringDocConfig {

	private static final String badRequestResponse = "BadRequestResponse";
	private static final String notFoundResponse = "NotFoundResponse";
	private static final String notAcceptableResponse = "NotAcceptableResponse";
	private static final String internalServerErrorResponse = "InternalServerErrorResponse";

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.info(new Info().title("Algafood API").version("v1").description("REST API AlgaFood")
						.license(new License().name("Apache 2.0").url("http://springdoc.com")))
				.externalDocs(new ExternalDocumentation().description("Documentação externa detalhada")
						.url("https://algaworks.com"))
				.tags(Arrays.asList(
						new Tag().name("Cidades").description("Gerencia as cidades"),
						new Tag().name("Grupos").description("Gerencia os grupos"),
						new Tag().name("Cozinhas").description("Gerencia as cozinhas"),
						new Tag().name("Formas de pagamento").description("Gerencia as formas de pagamento"),
						new Tag().name("Pedidos").description("Gerencia os pedidos"),
						new Tag().name("Restaurantes").description("Gerencia os restaurantes"), 
						new Tag().name("Estados").description("Gerencia os estados"),
						new Tag().name("Produtos").description("Gerencia os produtos")))
				.components(new Components().schemas(gerarSchemas()).responses(gerarResponses()));
	}

	// Aula 26.13
	@Bean
	public OpenApiCustomiser openApiCustomiser() {
		return openApi -> {
			openApi.getPaths().values()
					.forEach(pathItem -> pathItem.readOperationsMap().forEach((httpMethod, operation) -> {
						ApiResponses responses = operation.getResponses();
						switch (httpMethod) {
						case GET:
							responses.addApiResponse("404", new ApiResponse().$ref(notFoundResponse));
							responses.addApiResponse("406", new ApiResponse().$ref(notAcceptableResponse));
							responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
							break;
						case POST:
							responses.addApiResponse("400", new ApiResponse().$ref(badRequestResponse));
							responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
							break;
						case PUT:
							responses.addApiResponse("404", new ApiResponse().$ref(notFoundResponse));
							responses.addApiResponse("400", new ApiResponse().$ref(badRequestResponse));
							responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
							break;
						case DELETE:
							responses.addApiResponse("404", new ApiResponse().$ref(notFoundResponse));
							responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
							break;
						default:
							responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
							break;
						}

					})

					);
			;
		};
	}

	private Map<String, ApiResponse> gerarResponses() {
		final Map<String, ApiResponse> apiResponseMap = new HashMap<>();

		Content content = new Content().addMediaType(APPLICATION_JSON_VALUE,
				new MediaType().schema(new Schema<Problem>().$ref("Problema")));

		apiResponseMap.put(badRequestResponse, new ApiResponse().description("Requisição inválida").content(content));

		apiResponseMap.put(notFoundResponse, new ApiResponse().description("Recurso não encontrado").content(content));

		apiResponseMap.put(notAcceptableResponse,
				new ApiResponse().description("Recurso não possui representação que poderia ser aceita pelo consumidor")
						.content(content));

		apiResponseMap.put(internalServerErrorResponse,
				new ApiResponse().description("Erro interno no servidor").content(content));

		return apiResponseMap;
	}

	@SuppressWarnings("rawtypes")
	private Map<String, Schema> gerarSchemas() {
		final Map<String, Schema> schemaMap = new HashMap<>();

		final Map<String, Schema> problemSchema = ModelConverters.getInstance().read(Problem.class);
		final Map<String, Schema> problemObjectSchema = ModelConverters.getInstance().read(Problem.Object.class);

		schemaMap.putAll(problemSchema);
		schemaMap.putAll(problemObjectSchema);

		return schemaMap;
	}

	/*
	 * //Configuração quando houver a necessidade de ter mais de uma documentação no
	 * projeto
	 * 
	 * @Bean public GroupedOpenApi groupedOpenApiV2() { return
	 * GroupedOpenApi.builder() .group("Algafood API V2") .pathsToMatch("/v2/**")
	 * .addOpenApiCustomiser(openApi -> { openApi.info(new
	 * Info().title("Algafood API V2 ") .version("v2")
	 * .description("REST API AlgaFood") .license(new License() .name("Apache 2.0")
	 * .url("http://springdoc.com"))) .externalDocs(new ExternalDocumentation()
	 * .description("Documentação externa detalhada") .url("https://algaworks.com"))
	 * .tags(Arrays.asList(new
	 * Tag().name("Cidades").description("Gerencia as cidades"))); }) .build(); }
	 * 
	 * @Bean public GroupedOpenApi groupedOpenApiV1() { return
	 * GroupedOpenApi.builder() .group("Algafood API") .pathsToMatch("/v1/**")
	 * .addOpenApiCustomiser(openApi -> { openApi.info(new
	 * Info().title("Algafood API") .version("v1") .description("REST API AlgaFood")
	 * .license(new License() .name("Apache 2.0") .url("http://springdoc.com")))
	 * .externalDocs(new ExternalDocumentation()
	 * .description("Documentação externa detalhada") .url("https://algaworks.com"))
	 * .tags(Arrays.asList(new
	 * Tag().name("Cidades").description("Gerencia as cidades"))); }) .build(); }
	 */

}
