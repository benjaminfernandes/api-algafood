package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.model.CozinhaModel;
import com.algaworks.algafood.api.v1.model.input.CozinhaInput;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Cozinhas")
public interface CozinhaControllerOpenApi {

	@ApiOperation("Lista as cozinhas")
	PagedModel<CozinhaModel> listar(Pageable pageable);

	@ApiOperation("Busca uma cozinha por id")
	@ApiResponses({
		@ApiResponse(code = 400, message = "ID da cozinha inválido", response = Problem.class),
		@ApiResponse(code = 404, message = "Cozinha não encontrada!", response = Problem.class)
	})
	CozinhaModel buscar(@ApiParam(value = "ID de uma cozinha", example = "1", required = true)
	Long cozinhaId);

	@ApiOperation("Adicionar cozinha")
	@ApiResponses({		
		@ApiResponse(code = 201, message = "Cozinha cadastrada")
	})
	CozinhaModel adicionar(
			@ApiParam(name = "corpo", value = "Representação de uma nova cozinha")
			CozinhaInput cozinhaInput);

	@ApiOperation("Atualiza a cozinha")
	CozinhaModel atualizar(@ApiParam(value = "ID de uma cozinha", example = "1", required = true)
	Long cozinhaId, 
		@ApiParam(name = "corpo", value = "Representação de uma cozinha com os novos dados")
		CozinhaInput cozinhaInput);

	@ApiOperation("Exclui uma cozinha por id")
	@ApiResponses({
		@ApiResponse(code = 204, message = "Cozinha excluída"),
		@ApiResponse(code = 404, message = "Cozinha não encontrada!", response = Problem.class)
	})
	void remover(@ApiParam(value = "ID de uma cozinha", example = "1", required = true) Long cozinhaId);
}
