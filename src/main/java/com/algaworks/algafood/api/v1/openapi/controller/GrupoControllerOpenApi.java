package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;

import com.algaworks.algafood.api.v1.model.GrupoModel;
import com.algaworks.algafood.api.v1.model.input.GrupoInput;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Grupos")
public interface GrupoControllerOpenApi {

	@Operation(summary = "Lista os grupos")
	CollectionModel<GrupoModel> listar();
	@Operation(summary = "Busca um grupo por ID")
	GrupoModel buscar(Long codigo);
	@Operation(summary = "Cadastra um grupo")
	GrupoModel adicionar(GrupoInput grupoinput);
	@Operation(summary = "Atualiza um grupo por ID")
	GrupoModel alterar(Long codigo, GrupoInput grupoInput);
	@Operation(summary = "Exclui um grupo por ID")
	void remover(Long codigo);

}