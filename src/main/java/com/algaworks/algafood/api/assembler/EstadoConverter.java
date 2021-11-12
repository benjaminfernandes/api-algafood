package com.algaworks.algafood.api.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.controller.EstadoController;
import com.algaworks.algafood.api.model.EstadoModel;
import com.algaworks.algafood.api.model.input.EstadoInput;
import com.algaworks.algafood.domain.model.Estado;

@Component
public class EstadoConverter extends RepresentationModelAssemblerSupport<Estado, EstadoModel> 
	implements Converter<Estado, EstadoModel, EstadoInput> {

	public EstadoConverter() {
		super(EstadoController.class, EstadoModel.class);
	}

	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Estado toDomain(EstadoInput inputModel) {
		return this.modelMapper.map(inputModel, Estado.class);
	}

	@Override
	public EstadoModel toModel(Estado domain) {
		EstadoModel estadoModel = createModelWithId(domain.getId(), domain);
		this.modelMapper.map(domain, estadoModel);
		
		estadoModel.add(linkTo(methodOn(EstadoController.class).listar()).withRel("estados"));
		
		return estadoModel;
	}

	@Override
	public List<EstadoModel> paraModeloColecao(Collection<Estado> list) {
		return list.stream().map(estado -> toModel(estado)).collect(Collectors.toList());
	}

	@Override
	public void copyToDomainObject(EstadoInput restauranteInput, Estado restaurante) {
		this.modelMapper.map(restauranteInput, restaurante);
	}

	@Override
	public CollectionModel<EstadoModel> toCollectionModel(Iterable<? extends Estado> entities) {
		return super.toCollectionModel(entities)
				.add(linkTo(EstadoController.class).withSelfRel());
	}
}
