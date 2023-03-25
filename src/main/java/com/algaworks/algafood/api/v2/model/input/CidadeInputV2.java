package com.algaworks.algafood.api.v2.model.input;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CidadeInputV2 {

	@NotBlank
	private String nomeCidade;
	@Valid
	@NotNull
	private Long idEstado;

}
