package com.algaworks.algafood.api.v1.model;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Relation(collectionRelation = "estados")
@Getter
@Setter
public class EstadoModel extends RepresentationModel<EstadoModel> {

	@ApiModelProperty(example = "1", required = true)
	private Long id;
	@ApiModelProperty(example = "Paraná")
	private String nome;
}