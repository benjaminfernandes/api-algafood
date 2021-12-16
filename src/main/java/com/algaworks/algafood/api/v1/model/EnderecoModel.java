package com.algaworks.algafood.api.v1.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoModel {

	@ApiModelProperty(example = "859800-000")
	private String cep;
	@ApiModelProperty(example = "Rua Francisco Murtinho")
	private String logradouro;
	@ApiModelProperty(example = "\"1500\"")
	private String numero;
	@ApiModelProperty(example = "casa")
	private String complemento;
	@ApiModelProperty(example = "Centro")
	private String bairro;
	private CidadeResumoModel cidade;
}
