package com.algaworks.algafood.api.model.input;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
//usado para ser a referencia de cozinha na classe RestauranteInput, aula 11.11.
@Setter
@Getter
public class CozinhaIdInput {

	@NotNull
	private Long id;
	
}
