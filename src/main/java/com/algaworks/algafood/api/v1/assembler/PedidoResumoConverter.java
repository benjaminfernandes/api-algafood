package com.algaworks.algafood.api.v1.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.Algalinks;
import com.algaworks.algafood.api.v1.controller.PedidoController;
import com.algaworks.algafood.api.v1.model.PedidoResumoModel;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoResumoConverter extends RepresentationModelAssemblerSupport<Pedido, PedidoResumoModel> {

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private Algalinks algaLinks;
	
	public PedidoResumoConverter() {
		super(PedidoController.class, PedidoResumoModel.class);
	}

	public PedidoResumoModel toModel(Pedido domain) {
		PedidoResumoModel pedidoResumoModel = createModelWithId(domain.getCodigo(), domain);
		this.modelMapper.map(domain, pedidoResumoModel);
		//pedidoResumoModel.add(linkTo(PedidoController.class).withSelfRel());
		pedidoResumoModel.add(algaLinks.linkToPedidos("pedidos"));
		//pedidoResumoModel.getCliente().add(algaLinks.linkToUsuario(domain.getCliente().getId()));
		return pedidoResumoModel;
	}

	public List<PedidoResumoModel> paraModeloColecao(Collection<Pedido> list) {
		return list.stream().map(pedido -> toModel(pedido)).collect(Collectors.toList());
	}
	
}
