package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.Algalinks;
import com.algaworks.algafood.api.controller.PedidoController;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.input.Pedidoinput;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoConverter extends RepresentationModelAssemblerSupport<Pedido, PedidoModel> implements Converter<Pedido, PedidoModel, Pedidoinput> {

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private Algalinks algalinks;
	
	public PedidoConverter() {
		super(PedidoController.class, PedidoModel.class);
	}
	
	@Override
	public Pedido toDomain(Pedidoinput inputModel) {
		return modelMapper.map(inputModel, Pedido.class);
	}

	@Override
	public PedidoModel toModel(Pedido domain) {
		PedidoModel pedidoModel = createModelWithId(domain.getCodigo(), domain);
		this.modelMapper.map(domain, pedidoModel);
		
		pedidoModel.add(algalinks.linkToPedidos());
		
		pedidoModel.add(algalinks.linkToConfirmacaoPedido(domain.getCodigo(), "confirmar"));
		pedidoModel.add(algalinks.linkToCancelamentoPedido(domain.getCodigo(), "cancelar"));
		pedidoModel.add(algalinks.linkToEntregaPedido(domain.getCodigo(), "entregar"));
		
		//pedidoModel.add(linkTo(PedidoController.class).withRel("pedidos"));
		
		/*pedidoModel.getFormaPagamento().add(linkTo(methodOn(FormaPagamentoController.class)
				.buscar(domain.getFormaPagamento().getId(), null)).withSelfRel());
		
		pedidoModel.getRestaurante().add(linkTo(methodOn(RestauranteController.class)
                .buscar(domain.getRestaurante().getId())).withSelfRel());
        
        pedidoModel.getCliente().add(linkTo(methodOn(UsuarioController.class)
                .buscar(domain.getCliente().getId())).withSelfRel());
		
        pedidoModel.getEndereco().getCidade().add(linkTo(methodOn(CidadeController.class)
                .buscar(domain.getEndereco().getCidade().getId())).withSelfRel());
        
        pedidoModel.getItens().forEach(item -> {
            item.add(linkTo(methodOn(RestauranteProdutoController.class)
                    .buscar(pedidoModel.getRestaurante().getId(), item.getProdutoId()))
                    .withRel("produto"));
        });*/
	    modelMapper.map(domain, pedidoModel);
	    
	    pedidoModel.add(algalinks.linkToPedidos());
	    
	    pedidoModel.getRestaurante().add(
	            algalinks.linkToRestaurante(domain.getRestaurante().getId()));
	    
	    pedidoModel.getCliente().add(
	            algalinks.linkToUsuario(domain.getCliente().getId()));
	    
	    pedidoModel.getFormaPagamento().add(
	            algalinks.linkToFormaPagamento(domain.getFormaPagamento().getId()));
	    
	    pedidoModel.getEndereco().getCidade().add(
	            algalinks.linkToCidade(domain.getEndereco().getCidade().getId()));
	    
	    pedidoModel.getItens().forEach(item -> {
	        item.add(algalinks.linkToProduto(
	                pedidoModel.getRestaurante().getId(), item.getProdutoId(), "produto"));
	    });
	    
		return pedidoModel;
	}

	@Override
	public void copyToDomainObject(Pedidoinput inputModel, Pedido model) {
		this.modelMapper.map(inputModel, model);
	}

	@Override
	public List<PedidoModel> paraModeloColecao(Collection<Pedido> list) {
		
		return list.stream().map(pedido -> toModel(pedido)).collect(Collectors.toList());
	}

}
