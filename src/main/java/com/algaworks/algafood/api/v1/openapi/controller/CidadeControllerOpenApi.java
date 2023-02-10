package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;

import com.algaworks.algafood.api.v1.model.CidadeModel;
import com.algaworks.algafood.api.v1.model.input.CidadeInput;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Cidades")
public interface CidadeControllerOpenApi {

	@Operation(summary = "Listar as cidades")
	CollectionModel<CidadeModel> listar();
	
	@Operation(summary = "Cadastra uma cidade", description = "Cadastro de uma cidade necessida de um estado e um nome válido")
	CidadeModel salvar(@RequestBody(description = "Representação de uma nova cidade", required = true) CidadeInput cidadeInput);
	
	@Operation(summary = "Busca uma cidade", responses = {
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "400", description = "ID da cidade inválido", content = @Content(schema = @Schema(ref = "Problema"))),//retira o schema de resposta, pois ele acaba utilizando o mesmo da resposta 200
			@ApiResponse(responseCode = "404", description = "Cidade não encontrada",
			content = @Content(schema = @Schema(ref = "Problema"))) //Para utilizar a resposta customizada deve remover a referencia global na classe SpringDocConfig
	})
	CidadeModel buscar(@Parameter(description = "Id de uma cidade", example = "1", required = true) Long cidadeId);
	
	@Operation(summary = "Atualiza uma cidade")
	CidadeModel atualizar(@Parameter(description = "Id de uma cidade", example = "1", required = true) Long cidadeId, 
			@RequestBody(description = "Representação de uma cidade com dados atualizados", required = true) CidadeInput cidadeInput);
	
	@Operation(summary = "Exclui uma cidade")	
	void excluir(@Parameter(description = "Id de uma cidade", example = "1", required = true) Long cidadeId);
	
}
