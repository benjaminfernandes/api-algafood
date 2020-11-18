package com.algaworks.algafood.domain.exception;

public class ProdutoNaoEncontradoException extends EntidadeNaoEncontradaException {
	
	public ProdutoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}

	public ProdutoNaoEncontradoException(Long restauranteId, Long produtoId) {
		super(String.format("Não existe um produto com id %s no restaurante de código %s", produtoId, restauranteId));
	}
	
	public ProdutoNaoEncontradoException(Long produtoId) {
		super(String.format("Não existe um produto com id %s", produtoId));
	}
	private static final long serialVersionUID = 1L;

}
