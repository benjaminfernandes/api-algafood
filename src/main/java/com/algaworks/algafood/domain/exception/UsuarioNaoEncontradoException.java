package com.algaworks.algafood.domain.exception;

public class UsuarioNaoEncontradoException extends EntidadeNaoEncontradaException {

	public UsuarioNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public UsuarioNaoEncontradoException(Long id) {
		super(String.format("Não foi possível encontrar um usuário com o id %s", id));
	}

	private static final long serialVersionUID = 1L;

}
