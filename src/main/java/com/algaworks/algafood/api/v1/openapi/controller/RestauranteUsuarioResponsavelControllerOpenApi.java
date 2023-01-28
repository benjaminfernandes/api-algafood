package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.UsuarioModel;

public interface RestauranteUsuarioResponsavelControllerOpenApi {

	 CollectionModel<UsuarioModel> listar(Long restauranteId);

	 ResponseEntity<Void> desassociarResponsavel(Long restauranteId, Long usuarioId);

	 ResponseEntity<Void> associarResponsavel(Long restauranteId, Long usuarioId);
}
