package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;

import com.algaworks.algafood.api.v1.model.CidadeModel;
import com.algaworks.algafood.api.v1.model.input.CidadeInput;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_auth")
public interface CidadeControllerOpenApi {

	CollectionModel<CidadeModel> listar();

	CidadeModel salvar(CidadeInput cidadeInput);

	CidadeModel buscar(Long cidadeId);

	
	CidadeModel atualizar(Long cidadeId, CidadeInput cidadeInput);

	
	void excluir(Long cidadeId);
	
}
