package com.algaworks.algafood.api.v1.model.input;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlterarSenhaUsuario {

	@NotBlank
	private String senhaAtual;
	@NotBlank
	private String novaSenha;
}
