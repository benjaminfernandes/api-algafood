package com.algaworks.algafood.api.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariable.VariableType;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.controller.CidadeController;
import com.algaworks.algafood.api.controller.FormaPagamentoController;
import com.algaworks.algafood.api.controller.PedidoController;
import com.algaworks.algafood.api.controller.RestauranteController;
import com.algaworks.algafood.api.controller.RestauranteProdutoController;
import com.algaworks.algafood.api.controller.UsuarioController;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.input.Pedidoinput;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoConverter extends RepresentationModelAssemblerSupport<Pedido, PedidoModel> implements Converter<Pedido, PedidoModel, Pedidoinput> {

	@Autowired
	private ModelMapper modelMapper;
	
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
		
		TemplateVariables pageVariables = new TemplateVariables(
				new TemplateVariable("page", VariableType.REQUEST_PARAM),
				new TemplateVariable("size", VariableType.REQUEST_PARAM),
				new TemplateVariable("sort", VariableType.REQUEST_PARAM));
		
		String pedidosUrl = linkTo(PedidoController.class).toUri().toString();
		
		pedidoModel.add(new Link(UriTemplate.of(pedidosUrl, pageVariables), "pedidos"));
		//pedidoModel.add(linkTo(PedidoController.class).withRel("pedidos"));
		
		pedidoModel.getFormaPagamento().add(linkTo(methodOn(FormaPagamentoController.class)
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
