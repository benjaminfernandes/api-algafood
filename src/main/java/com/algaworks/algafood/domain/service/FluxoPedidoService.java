package com.algaworks.algafood.domain.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.repository.PedidoRepository;

@Service
public class FluxoPedidoService {

	@Autowired
	private CadastroPedidoService pedidoService;
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Transactional
	public void confirmar(String codigoPedido) {
		Pedido pedido = this.pedidoService.buscarOuFalhar(codigoPedido);
		pedido.confirmar();
		
		//adicionado para enviar o evento que tem no método confirmar dentro de pedido
		//Aula ESR 15.11
		this.pedidoRepository.save(pedido);
	}
	
	@Transactional
	public void entregar(String codigoPedido) {
		Pedido pedido = this.pedidoService.buscarOuFalhar(codigoPedido);
		pedido.entregar();
		
		this.pedidoRepository.save(pedido);
	}

	@Transactional
	public void cancelar(String codigoPedido) {
		Pedido pedido = this.pedidoService.buscarOuFalhar(codigoPedido);
		pedido.cancelar();
		
		this.pedidoRepository.save(pedido);
	}
}
