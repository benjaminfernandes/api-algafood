package com.algaworks.algafood.domain.exception;

public class PedidoNaoEncontradoException extends EntidadeNaoEncontradaException {
	
	private static final long serialVersionUID = 1L;
	
	public PedidoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public PedidoNaoEncontradoException(Long id) {
		super(String.format("Não foi possível encontrar um pedido com o id %s", id));
	}

}
