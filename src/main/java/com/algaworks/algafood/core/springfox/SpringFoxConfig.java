package com.algaworks.algafood.core.springfox;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.model.CidadeModel;
import com.algaworks.algafood.api.v1.model.CozinhaModel;
import com.algaworks.algafood.api.v1.model.EstadoModel;
import com.algaworks.algafood.api.v1.model.FormaPagamentoModel;
import com.algaworks.algafood.api.v1.model.GrupoModel;
import com.algaworks.algafood.api.v1.model.PedidoResumoModel;
import com.algaworks.algafood.api.v1.model.PermissaoModel;
import com.algaworks.algafood.api.v1.model.ProdutoModel;
import com.algaworks.algafood.api.v1.model.RestauranteBasicoModel;
import com.algaworks.algafood.api.v1.model.UsuarioModel;
import com.algaworks.algafood.api.v1.openapi.model.CidadesModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.CozinhasModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.EstadosModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.FormasPagamentoModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.GruposModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.LinksModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.PageableModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.PedidosResumoModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.PermissoesModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.ProdutosModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.RestaurantesBasicoModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.UsuariosModelOpenApi;
import com.algaworks.algafood.api.v2.model.CidadeModelV2;
import com.algaworks.algafood.api.v2.openapi.model.CidadesModelV2OpenApi;
import com.fasterxml.classmate.TypeResolver;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxConfig implements WebMvcConfigurer {

	//Se quiser excluir a documentação da V1, basta excluir o método apiDocketV1()
	@Bean
	public Docket apiDocketV1() {
		var typeResolver = new TypeResolver();
		
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("V1")
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.algaworks.algafood.api"))
				.paths(PathSelectors.ant("/v1/**"))
				// .paths(PathSelectors.ant("/restaurantes/*"))
				.build().useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.GET, globalGetResponseMessages())
				.globalResponseMessage(RequestMethod.POST, globalPostResponseMessages())
				.globalResponseMessage(RequestMethod.PUT, globalPutResponseMessages())
				.globalResponseMessage(RequestMethod.DELETE, globalDeleteResponseMessages())
				.securitySchemes(Arrays.asList(securityScheme()))
				.securityContexts(Arrays.asList(securityContext()))
				.apiInfo(apiInfoV1())
				/*.globalOperationParameters(Arrays.asList(
						new ParameterBuilder()
							.name("campos")
							.description("Nomes das proprietadas para filtrar na responsa separados por vírgula")
							.parameterType("query")
							.modelRef(new ModelRef("string"))
							.build()))//aula 20.25 */
				.additionalModels(typeResolver.resolve(Problem.class))
				.ignoredParameterTypes(ServletWebRequest.class, URL.class, 
						URI.class, URLStreamHandler.class, Resource.class, File.class, 
						InputStream.class)//Aula 18.22 e 18.36
				.directModelSubstitute(Pageable.class, PageableModelOpenApi.class)//aula 18.20
				.directModelSubstitute(Links.class, LinksModelOpenApi.class)
				.alternateTypeRules(AlternateTypeRules.newRule(
						typeResolver.resolve(PagedModel.class, CozinhaModel.class),
						CozinhasModelOpenApi.class)) //Aula 18.21
				.alternateTypeRules(AlternateTypeRules.newRule(
	                    typeResolver.resolve(PagedModel.class, PedidoResumoModel.class),
	                    PedidosResumoModelOpenApi.class))
				.alternateTypeRules(AlternateTypeRules.newRule(
	                    typeResolver.resolve(CollectionModel.class, CidadeModel.class),
	                    CidadesModelOpenApi.class))//Aula 19.40
				.alternateTypeRules(AlternateTypeRules.newRule(
				        typeResolver.resolve(CollectionModel.class, EstadoModel.class),
				        EstadosModelOpenApi.class))
				.alternateTypeRules(AlternateTypeRules.newRule(
					    typeResolver.resolve(CollectionModel.class, FormaPagamentoModel.class),
					    FormasPagamentoModelOpenApi.class))
				.alternateTypeRules(AlternateTypeRules.newRule(
					    typeResolver.resolve(CollectionModel.class, GrupoModel.class),
					    GruposModelOpenApi.class))
				.alternateTypeRules(AlternateTypeRules.newRule(
					        typeResolver.resolve(CollectionModel.class, PermissaoModel.class),
					        PermissoesModelOpenApi.class))
				.alternateTypeRules(AlternateTypeRules.newRule(
						    typeResolver.resolve(CollectionModel.class, ProdutoModel.class),
						    ProdutosModelOpenApi.class))
				.alternateTypeRules(AlternateTypeRules.newRule(
					    typeResolver.resolve(CollectionModel.class, RestauranteBasicoModel.class),
					    RestaurantesBasicoModelOpenApi.class))
					.alternateTypeRules(AlternateTypeRules.newRule(
					        typeResolver.resolve(CollectionModel.class, UsuarioModel.class),
					        UsuariosModelOpenApi.class))
				.tags(new Tag("Cidades", "Gerencia as cidades"),
						new Tag("Grupos", "Gerencia os grupos"),
						new Tag("Cozinhas", "Gerencia as cozinhas"),
						new Tag("Formas de pagamento", "Gerencia as formas de pagamento"),
						new Tag("Pedidos", "Gerencia os pedidos"), 
						new Tag("Restaurantes", "Gerencia os restaurantes"), 
						new Tag("Estados", "Gerencia os estados"), 
						new Tag("Produtos", "Gerencia os produtos de restaurantes"),
						new Tag("Usuários", "Gerencia os usuários"),
						new Tag("Estatísticas", "Estatísticas da AlgaFood"),
						new Tag("Permissões", "Gerencia as permissões"));
	}
	
	@Bean
	public Docket apiDocketV2() {
		var typeResolver = new TypeResolver();
		
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("V2")
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.algaworks.algafood.api"))
				.paths(PathSelectors.ant("/v2/**"))
				.build().useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.GET, globalGetResponseMessages())
				.globalResponseMessage(RequestMethod.POST, globalPostResponseMessages())
				.globalResponseMessage(RequestMethod.PUT, globalPutResponseMessages())
				.globalResponseMessage(RequestMethod.DELETE, globalDeleteResponseMessages())
				.securitySchemes(Arrays.asList(securityScheme()))
				.securityContexts(Arrays.asList(securityContext()))
				.apiInfo(apiInfoV2())
				.additionalModels(typeResolver.resolve(Problem.class))
				.ignoredParameterTypes(ServletWebRequest.class, URL.class, 
						URI.class, URLStreamHandler.class, Resource.class, File.class, 
						InputStream.class)
				.directModelSubstitute(Pageable.class, PageableModelOpenApi.class)//aula 18.20
				.directModelSubstitute(Links.class, LinksModelOpenApi.class)
				.alternateTypeRules(AlternateTypeRules.newRule(
					        typeResolver.resolve(CollectionModel.class, CidadeModelV2.class),
					        CidadesModelV2OpenApi.class))
				.tags(new Tag("Cidades", "Gerencia as cidades"));
	}

	private List<ResponseMessage> globalGetResponseMessages() {
		return Arrays.asList(

				new ResponseMessageBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
						.message("Erro interno do servidor")
						.responseModel(new ModelRef("Problema")).build(),

				new ResponseMessageBuilder().code(HttpStatus.NOT_ACCEPTABLE.value())
						.message("Recurso não possui representação que poderia ser aceita pelo consumidor").build());
	}

	private List<ResponseMessage> globalPostResponseMessages() {
		return Arrays.asList(

				new ResponseMessageBuilder().code(HttpStatus.BAD_REQUEST.value())
						.message("Esta requisição não pode ser concluída")
						.responseModel(new ModelRef("Problema")).build(),

				new ResponseMessageBuilder().code(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
						.message("Requisição recusada porque o corpo está em um formato não suportado")
						.responseModel(new ModelRef("Problema")).build(),

				new ResponseMessageBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
						.message("Erro interno do servidor")
						.responseModel(new ModelRef("Problema")).build(),

				new ResponseMessageBuilder().code(HttpStatus.NOT_ACCEPTABLE.value())
						.message("Recurso não possui representação que poderia ser aceita pelo consumidor").build());
	}

	private List<ResponseMessage> globalPutResponseMessages() {
		return Arrays.asList(

				new ResponseMessageBuilder().code(HttpStatus.BAD_REQUEST.value())
						.message("Requisição inválida")
						.responseModel(new ModelRef("Problema"))
						.build(),

				new ResponseMessageBuilder().code(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
						.message("Requisição recusada porque o corpo está em um formato não suportado")
						.responseModel(new ModelRef("Problema"))
						.build(),

				new ResponseMessageBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
						.message("Erro interno do servidor")
						.responseModel(new ModelRef("Problema")).build(),

				new ResponseMessageBuilder().code(HttpStatus.NOT_ACCEPTABLE.value())
						.message("Recurso não possui representação que poderia ser aceita pelo consumidor").build());
	}

	private ApiInfo apiInfoV1() {
		return new ApiInfoBuilder().title("AlgaFood Api (Depreciada)")
				.description("API Aberta para clientes e restaurantes. <br>"
						+"<strong>Essa versão da API está depreciada e deixará de existir a partir de 31/12/2021</strong>"
						+"<br> Use a versão mais atual da API")	
				.version("1").contact(new Contact("Benjamin", "https:www.benja.com.br", 
						"benjamin@benja.com.br"))
				.build();
	}
	
	private ApiInfo apiInfoV2() {
		return new ApiInfoBuilder().title("AlgaFood Api")
				.description("API Aberta para clientes e restaurantes")
				.version("2")
				.contact(new Contact("Benjamin", "https:www.benja.com.br", 
						"benjamin@benja.com.br"))
				.build();
	}


	private List<ResponseMessage> globalDeleteResponseMessages() {
		return Arrays.asList(

				new ResponseMessageBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
						.message("Erro interno do servidor")
						.responseModel(new ModelRef("Problema"))
						.build(),

				new ResponseMessageBuilder().code(HttpStatus.BAD_REQUEST.value())
					.message("Requisição inválida")
					.responseModel(new ModelRef("Problema"))
					.build());
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
	
	//instancia para descrever a técnica de segurança utilizada na API
	//Aula 23.42
	private SecurityScheme securityScheme() {
		
		return new OAuthBuilder()
				.name("Algafood")
				.grantTypes(grantTypes())
				.scopes(scopes())
				.build();
	}
	
	private SecurityContext securityContext() {
		var securityReference = SecurityReference.builder()
				.reference("Algafood")
				.scopes(scopes().toArray(new AuthorizationScope[0]))
				.build();
		
		return SecurityContext.builder().securityReferences(Arrays.asList(securityReference))
				.forPaths(PathSelectors.any())
				.build();
	}
	
	private List<GrantType> grantTypes(){
		return Arrays.asList(new ResourceOwnerPasswordCredentialsGrant("/oauth/token"));
	}
	
	private List<AuthorizationScope> scopes(){
		return Arrays.asList(new AuthorizationScope("READ", "Acesso de leitura"),
				new AuthorizationScope("WRITE", "Acesso de escrita"));
	}
}
