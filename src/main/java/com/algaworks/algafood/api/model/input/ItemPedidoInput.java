package com.algaworks.algafood.api.model.input;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemPedidoInput {

	@NotNull
	@PositiveOrZero
	private Long quantidade;
	@NotNull
	private Long produtoId;
	private String observacao;
}
