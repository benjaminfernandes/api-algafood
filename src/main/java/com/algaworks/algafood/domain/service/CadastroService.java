package com.algaworks.algafood.domain.service;

public interface CadastroService<T> {

	T  salvar(T entity);
	void excluir(Long id);
	T buscarOuFalhar(Long id);
}
