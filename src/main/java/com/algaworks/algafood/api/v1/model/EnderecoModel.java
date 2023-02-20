package com.algaworks.algafood.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoModel {

	@Schema(example = "85980-000")
	private String cep;
	@Schema(example = "Rua Luciano Soares")
	private String logradouro;
	@Schema(example = "\"126\"")
	private String numero;
	@Schema(example = "Apto 801")
	private String complemento;
	@Schema(example = "Centro")
	private String bairro;
	private CidadeResumoModel cidade;
}
