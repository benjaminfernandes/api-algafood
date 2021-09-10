package com.algaworks.algafood.api.controller.openapi;

import java.util.List;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.api.model.input.GrupoInput;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Grupos")
public interface GrupoControllerOpenApi {

	@ApiOperation("Lista os grupos")
	List<GrupoModel> listar();

	@ApiOperation("Busca o grupo por id")
	  @ApiResponses({
	        @ApiResponse(code = 400, message = "ID da grupo inválido", response = Problem.class),
	        @ApiResponse(code = 404, message = "Grupo não encontrado", response = Problem.class)
	    })
	GrupoModel buscar(@ApiParam(value = "ID de um grupo", example = "1") Long codigo);

	 @ApiOperation("Cadastra um grupo")
	    @ApiResponses({
	        @ApiResponse(code = 201, message = "Grupo cadastrado"),
	    })
	GrupoModel adicionar(@ApiParam(name = "corpo", value = "Representação de um novo grupo") GrupoInput grupoinput);
	 
	 @ApiOperation("Atualiza um grupo por ID")
	    @ApiResponses({
	        @ApiResponse(code = 200, message = "Grupo atualizado"),
	        @ApiResponse(code = 404, message = "Grupo não encontrado", response = Problem.class)
	    })
	GrupoModel alterar(@ApiParam(value = "ID de um grupo", example = "1") 
						Long codigo, 
						@ApiParam(name = "corpo", value = "Representação de um grupo com os novos dados")
						GrupoInput grupoInput);

	   @ApiOperation("Exclui um grupo por ID")
	    @ApiResponses({
	        @ApiResponse(code = 204, message = "Grupo excluído"),
	        @ApiResponse(code = 404, message = "Grupo não encontrado", response = Problem.class)
	    })
	   
	void remover(@ApiParam(value = "ID de um grupo", example = "1") Long codigo);

}