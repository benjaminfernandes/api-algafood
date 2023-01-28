package com.algaworks.algafood.api.v2.model.input;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.sun.istack.NotNull;

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
