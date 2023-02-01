package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;

import com.algaworks.algafood.api.v1.model.CidadeModel;
import com.algaworks.algafood.api.v1.model.input.CidadeInput;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Cidades")
public interface CidadeControllerOpenApi {

	@Operation(summary = "Listar as cidades")
	CollectionModel<CidadeModel> listar();
	@Operation(summary = "Cadastra uma cidade", description = "Cadastro de uma cidade necessida de um estado e um nome válido")
	CidadeModel salvar(@RequestBody(description = "Representação de uma nova cidade", required = true) CidadeInput cidadeInput);
	@Operation(summary = "Busca uma cidade")
	CidadeModel buscar(@Parameter(description = "Id de uma cidade", example = "1", required = true) Long cidadeId);
	@Operation(summary = "Atualiza uma cidade")
	CidadeModel atualizar(@Parameter(description = "Id de uma cidade", example = "1", required = true) Long cidadeId, 
			@RequestBody(description = "Representação de uma cidade com dados atualizados", required = true) CidadeInput cidadeInput);
	@Operation(summary = "Exclui uma cidade")	
	void excluir(@Parameter(description = "Id de uma cidade", example = "1", required = true) Long cidadeId);
	
}
