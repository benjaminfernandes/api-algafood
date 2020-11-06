package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.EstadoModel;
import com.algaworks.algafood.api.model.input.EstadoInput;
import com.algaworks.algafood.domain.model.Estado;

@Component
public class EstadoConverter implements Converter<Estado, EstadoModel, EstadoInput> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Estado toDomain(EstadoInput inputModel) {
		return this.modelMapper.map(inputModel, Estado.class);
	}

	@Override
	public EstadoModel toModel(Estado domain) {
		return modelMapper.map(domain, EstadoModel.class);
	}

	@Override
	public List<EstadoModel> toCollectionModel(Collection<Estado> list) {
		return list.stream().map(estado -> toModel(estado)).collect(Collectors.toList());
	}

	@Override
	public void copyToDomainObject(EstadoInput restauranteInput, Estado restaurante) {
		this.modelMapper.map(restauranteInput, restaurante);
	}

}
