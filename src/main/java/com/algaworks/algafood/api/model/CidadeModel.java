package com.algaworks.algafood.api.model;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "Cidade", description = "Representa uma cidade")
@Setter
@Getter
public class CidadeModel extends RepresentationModel<CidadeModel>{

	@ApiModelProperty(value = "ID da cidade", example = "1")
	private Long id;
	@ApiModelProperty(example = "Guaíra")
	private String nome;
	
	private EstadoModel estado;
}
