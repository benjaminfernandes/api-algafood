package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.PedidoConverter;
import com.algaworks.algafood.api.assembler.PedidoResumoConverter;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.api.model.input.Pedidoinput;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.repository.filter.PedidoFilter;
import com.algaworks.algafood.domain.repository.infraestructure.spec.PedidoSpecs;
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
	public List<PedidoResumoModel> pesquisar(PedidoFilter pedidoFilter){
		
		return this.pedidoResumoConverter
				.toCollectionModel(this.pedidoRepository.findAll(PedidoSpecs.usandoFiltro(pedidoFilter)));
	}
	
	@GetMapping("/{codigoPedido}")
	public PedidoModel buscar(@PathVariable String codigoPedido) {
		Pedido pedido = this.pedidoService.buscarOuFalhar(codigoPedido);
		return this.pedidoConverter.toModel(pedido);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PedidoModel salvar(@Valid @RequestBody Pedidoinput pedidoInput) {
		Pedido pedido = this.pedidoConverter.toDomain(pedidoInput);
		// TODO pegar usuário autenticado
        pedido.setCliente(new Usuario());
        pedido.getCliente().setId(1L);
	
        pedido = this.pedidoService.salvar(pedido);
        
        return this.pedidoConverter.toModel(pedido);
	}
}
