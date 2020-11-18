package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.ProdutoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.repository.ProdutoRepository;

@Service
public class CadastroProdutoService implements CadastroService<Produto> {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Override
	public Produto salvar(Produto entity) {
		return this.produtoRepository.save(entity);
	}

	@Override
	public void excluir(Long id) {
		Produto produto = this.buscarOuFalhar(id);
		
		this.produtoRepository.delete(produto);
	}

	@Override
	public Produto buscarOuFalhar(Long id) {
		return this.produtoRepository.findById(id).orElseThrow(() -> new ProdutoNaoEncontradoException(id));
	}

	public Produto buscarOuFalhar(Long restauranteId, Long produtoId) {
		return this.produtoRepository.findById(restauranteId, produtoId).orElseThrow(() -> new ProdutoNaoEncontradoException(restauranteId, produtoId));
	}
	
}
