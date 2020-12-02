package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.PedidoConverter;
import com.algaworks.algafood.api.assembler.PedidoResumoConverter;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.service.CadastroPedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private CadastroPedidoService pedidoService;
	@Autowired
	private PedidoConverter pedidoConverter;
	@Autowired
	private PedidoResumoConverter pedidoResumoConverter;
	
	@GetMapping
	public List<PedidoResumoModel> listar(){
		
		return this.pedidoResumoConverter.toCollectionModel(this.pedidoRepository.findAll());
	}
	
	@GetMapping("/{codigo}")
	public PedidoModel buscar(@PathVariable Long codigo) {
		Pedido pedido = this.pedidoService.buscarOuFalhar(codigo);
		return this.pedidoConverter.toModel(pedido);
	}
}
