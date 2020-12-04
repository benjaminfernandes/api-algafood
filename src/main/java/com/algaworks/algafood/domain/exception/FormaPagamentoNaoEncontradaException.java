package com.algaworks.algafood.domain.exception;

public class FormaPagamentoNaoEncontradaException extends EntidadeNaoEncontradaException {

	public FormaPagamentoNaoEncontradaException(String mensagem) {
		super(mensagem);
	}
	
	public FormaPagamentoNaoEncontradaException(Long formaPagamentoId) {
		super(String.format("Não foi possível encontrar a forma de pagamento com código %d", formaPagamentoId));
	}

	private static final long serialVersionUID = 1L;

	
}
