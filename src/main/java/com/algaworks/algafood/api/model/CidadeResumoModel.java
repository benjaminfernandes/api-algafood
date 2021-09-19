package com.algaworks.algafood.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CidadeResumoModel {

	@ApiModelProperty(example = "1")
	private Long id;
	@ApiModelProperty(example = "Guaíra")
	private String nome;
	@ApiModelProperty(example = "Paraná")
	private String estado;
}
