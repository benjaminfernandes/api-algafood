package com.algaworks.algafood.api.v1.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.converter.Converter;
import com.algaworks.algafood.api.v1.Algalinks;
import com.algaworks.algafood.api.v1.controller.PedidoController;
import com.algaworks.algafood.api.v1.model.PedidoModel;
import com.algaworks.algafood.api.v1.model.input.Pedidoinput;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoConverter extends RepresentationModelAssemblerSupport<Pedido, PedidoModel> implements Converter<Pedido, PedidoModel, Pedidoinput> {

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private Algalinks algalinks;
	@Autowired
	private AlgaSecurity algaSecurity;
	
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
		
		pedidoModel.add(algalinks.linkToPedidos("pedidos"));
		
		if(this.algaSecurity.podeGerenciarPedidos(domain.getCodigo())) {
			if(domain.podeSerConfirmado()) {
				pedidoModel.add(algalinks.linkToConfirmacaoPedido(domain.getCodigo(), "confirmar"));
			}
			
			if(domain.podeSerCancelado()) {
				pedidoModel.add(algalinks.linkToCancelamentoPedido(domain.getCodigo(), "cancelar"));
			}
			
			if(domain.podeSerEntregue()) {
				pedidoModel.add(algalinks.linkToEntregaPedido(domain.getCodigo(), "entregar"));
			}
		}
		
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
