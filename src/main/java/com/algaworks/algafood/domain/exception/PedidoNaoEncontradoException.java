package com.algaworks.algafood.domain.exception;

public class PedidoNaoEncontradoException extends EntidadeNaoEncontradaException {
	
	private static final long serialVersionUID = 1L;
	
	public PedidoNaoEncontradoException(String codigo) {
		super(String.format("Não foi possível encontrar um pedido com o id %s", codigo));
	}
	
	public PedidoNaoEncontradoException(Long id) {
		super(String.format("Não foi possível encontrar um pedido com o id %d", id));
	}

}
