package com.algaworks.algafood.api.v1.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.Algalinks;
import com.algaworks.algafood.api.v1.controller.CozinhaController;
import com.algaworks.algafood.api.v1.model.CozinhaModel;
import com.algaworks.algafood.api.v1.model.input.CozinhaInput;
import com.algaworks.algafood.domain.model.Cozinha;

@Component
public class CozinhaConverter extends RepresentationModelAssemblerSupport<Cozinha, CozinhaModel> 
	implements Converter<Cozinha, CozinhaModel, CozinhaInput> {

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private Algalinks algaLinks;
	
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
		
		//cozinhaModel.add(linkTo(CozinhaController.class).withRel("cozinhas"));
		cozinhaModel.add(algaLinks.linkToCozinhas("cozinhas"));
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
