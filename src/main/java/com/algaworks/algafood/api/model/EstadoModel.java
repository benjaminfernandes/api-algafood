package com.algaworks.algafood.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstadoModel {

	@ApiModelProperty(example = "1", required = true)
	private Long id;
	@ApiModelProperty(example = "Paraná")
	private String nome;
}
