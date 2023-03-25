package com.algaworks.algafood.api.v1.model.input;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoInput {

	@Schema(example = "38400-000")
	@NotBlank
	private String cep;
	@Schema(example = "Rua francisco murtinho")
	@NotBlank
	private String logradouro;
	@Schema(example = "\"271\"")
	@NotBlank
	private String numero;
	@Schema(example = "Apto 809")
	private String complemento;
	@Schema(example = "Centro")
	@NotBlank
	private String bairro;
	@Valid
	@NotNull
	private CidadeIdInput cidade;
}
