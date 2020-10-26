package com.algaworks.algafood.domain.service;

public interface CadastroService<T> {

	public T  salvar(T entity);
	public void excluir(Long id);
	public T buscarOuFalhar(Long id);
}
