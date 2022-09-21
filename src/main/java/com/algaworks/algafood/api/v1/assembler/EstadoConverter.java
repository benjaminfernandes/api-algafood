package com.algaworks.algafood.api.v1.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.converter.Converter;
import com.algaworks.algafood.api.v1.Algalinks;
import com.algaworks.algafood.api.v1.controller.EstadoController;
import com.algaworks.algafood.api.v1.model.EstadoModel;
import com.algaworks.algafood.api.v1.model.input.EstadoInput;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Estado;

@Component
public class EstadoConverter extends RepresentationModelAssemblerSupport<Estado, EstadoModel> 
	implements Converter<Estado, EstadoModel, EstadoInput> {

	public EstadoConverter() {
		super(EstadoController.class, EstadoModel.class);
	}

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private Algalinks algalinks;
	@Autowired
	private AlgaSecurity algaSecurity; 
	
	@Override
	public Estado toDomain(EstadoInput inputModel) {
		return this.modelMapper.map(inputModel, Estado.class);
	}

	@Override
	public EstadoModel toModel(Estado domain) {
		EstadoModel estadoModel = createModelWithId(domain.getId(), domain);
		this.modelMapper.map(domain, estadoModel);
		
		//estadoModel.add(linkTo(methodOn(EstadoController.class).listar()).withRel("estados"));
		if (algaSecurity.podeConsultarEstados()) {
			estadoModel.add(algalinks.linkToEstados("estados"));
		}
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
		CollectionModel<EstadoModel> collectionModel = super.toCollectionModel(entities);
	    
	    if (algaSecurity.podeConsultarEstados()) {
	        collectionModel.add(algalinks.linkToEstados());
	    }
	    
	    return collectionModel;
	}
}
