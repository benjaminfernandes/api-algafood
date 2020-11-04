package com.algaworks.algafood.domain.exception;

public class GrupoNaoEncontradaException extends EntidadeNaoEncontradaException {

	public GrupoNaoEncontradaException(String mensagem) {
		super(mensagem);
	}
	public GrupoNaoEncontradaException(Long id) {
		super(String.format("Não foi possível encontrar o grupo com id %s", id));
	}

	private static final long serialVersionUID = 1L;

}
