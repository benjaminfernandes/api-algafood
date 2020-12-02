package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.PedidoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.repository.PedidoRepository;

@Service
public class CadastroPedidoService implements CadastroService<Pedido> {

	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Override
	public Pedido salvar(Pedido entity) {
		
		return null;
	}

	@Override
	public void excluir(Long id) {
		
	}

	@Override
	public Pedido buscarOuFalhar(Long id) {
		
		return this.pedidoRepository.findById(id).orElseThrow(() -> new PedidoNaoEncontradoException(id));
	}

}
