package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.input.Pedidoinput;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoConverter implements Converter<Pedido, PedidoModel, Pedidoinput> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Pedido toDomain(Pedidoinput inputModel) {
		return modelMapper.map(inputModel, Pedido.class);
	}

	@Override
	public PedidoModel toModel(Pedido domain) {
		return this.modelMapper.map(domain, PedidoModel.class);
	}

	@Override
	public void copyToDomainObject(Pedidoinput inputModel, Pedido model) {
		this.modelMapper.map(inputModel, model);
	}

	@Override
	public List<PedidoModel> toCollectionModel(Collection<Pedido> list) {
		
		return list.stream().map(pedido -> toModel(pedido)).collect(Collectors.toList());
	}

}
