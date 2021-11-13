package com.algaworks.algafood.api.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.controller.CozinhaController;
import com.algaworks.algafood.api.model.CozinhaModel;
import com.algaworks.algafood.api.model.input.CozinhaInput;
import com.algaworks.algafood.domain.model.Cozinha;

@Component
public class CozinhaConverter extends RepresentationModelAssemblerSupport<Cozinha, CozinhaModel> 
	implements Converter<Cozinha, CozinhaModel, CozinhaInput> {

	@Autowired
	private ModelMapper modelMapper;
	
	public CozinhaConverter() {
		super(CozinhaController.class, CozinhaModel.class);
	}
	
	@Override
	public Cozinha toDomain(CozinhaInput inputModel) {
		return this.modelMapper.map(inputModel, Cozinha.class);
	}

	@Override
	public CozinhaModel toModel(Cozinha domain) {
		CozinhaModel cozinhaModel = createModelWithId(domain.getId(), domain);
		this.modelMapper.map(domain, cozinhaModel);
		
		cozinhaModel.add(linkTo(CozinhaController.class).withRel("cozinhas"));
		
		return cozinhaModel;
	}

	@Override
	public List<CozinhaModel> paraModeloColecao(Collection<Cozinha> list) {
		return list.stream().map(cozinha -> toModel(cozinha)).collect(Collectors.toList());
	}

	@Override
	public void copyToDomainObject(CozinhaInput cozinhaInput, Cozinha cozinha) {
		
		this.modelMapper.map(cozinhaInput, cozinha);
	}

	
}
