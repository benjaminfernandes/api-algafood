package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;

import com.algaworks.algafood.api.v1.model.GrupoModel;
import com.algaworks.algafood.api.v1.model.input.GrupoInput;

public interface GrupoControllerOpenApi {

	CollectionModel<GrupoModel> listar();

	GrupoModel buscar(Long codigo);

	GrupoModel adicionar(GrupoInput grupoinput);
	 
	GrupoModel alterar(Long codigo, GrupoInput grupoInput);

	void remover(Long codigo);

}