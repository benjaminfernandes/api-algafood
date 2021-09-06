package com.algaworks.algafood.api.controller.openapi;

import java.util.List;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.api.model.input.CidadeInput;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Cidades")
public interface CidadeControllerOpenApi {

	@ApiOperation("Lista as cidades")
	public List<CidadeModel> listar();

	@ApiOperation("Salvar cidade")
	@ApiResponses({		
		@ApiResponse(code = 201, message = "Cidade cadastrada")
	})
	public CidadeModel salvar(@ApiParam(name = "corpo", value = "Representação de uma nova cidade") 
		CidadeInput cidadeInput);

	@ApiOperation("Busca uma cidade por id")
	@ApiResponses({
		@ApiResponse(code = 400, message = "ID da cidade inválido", response = Problem.class),
		@ApiResponse(code = 404, message = "Cidade não encontrada!", response = Problem.class)
	})
	public CidadeModel buscar(@ApiParam(value = "ID de uma cidade", example = "1") Long cidadeId);

	
	@ApiOperation("Atualiza a cidade")
	public CidadeModel atualizar(@ApiParam(value = "ID de uma cidade", example = "1") Long cidadeId, 
			@ApiParam(name = "corpo", value = "Representação de uma cidade com os novos dados")
			CidadeInput cidadeInput);

	
	@ApiOperation("Exclui uma cidade por id")
	@ApiResponses({
		@ApiResponse(code = 204, message = "Cidade excluída"),
		@ApiResponse(code = 404, message = "Cidade não encontrada!", response = Problem.class)
	})
	public void excluir(@ApiParam(value = "ID de uma cidade", example = "1") Long cidadeId);
	
}
