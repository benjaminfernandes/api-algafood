package com.algaworks.algafood.api.v1.model.input;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EstadoIdInput {

	@NotNull
	private Long id;
}
