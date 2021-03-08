package com.algaworks.algafood.domain.exception;

public class FotoNaoEncontradoException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;

	public FotoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}

	public FotoNaoEncontradoException(Long restauranteId, Long produtoId) {
		super(String.format("Não existe uma foto para o produto com id %s no restaurante de código %s", produtoId, restauranteId));
	}
	
	public FotoNaoEncontradoException(Long produtoId) {
		super(String.format("Não existe uma foto com id %s", produtoId));
	}
	
}
