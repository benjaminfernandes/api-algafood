package com.algaworks.algafood.api.v1.model.input;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Pedidoinput {

	@Valid
	@NotNull
	private FormaPagamentoIdInput formaPagamento;
	@Valid
	@NotNull
	private RestauranteIdInput restaurante;
	@Valid
	@NotNull
	private EnderecoInput endereco;
	@Valid
	@NotNull
	@Size(min = 1)
	private List<ItemPedidoInput> itens;
}
