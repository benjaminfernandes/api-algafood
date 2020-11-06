package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.CozinhaModel;
import com.algaworks.algafood.api.model.input.CozinhaInput;
import com.algaworks.algafood.domain.model.Cozinha;

@Component
public class CozinhaConverter implements Converter<Cozinha, CozinhaModel, CozinhaInput> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Cozinha toDomain(CozinhaInput inputModel) {
		return this.modelMapper.map(inputModel, Cozinha.class);
	}

	@Override
	public CozinhaModel toModel(Cozinha domain) {
		
		return this.modelMapper.map(domain, CozinhaModel.class);
	}

	@Override
	public List<CozinhaModel> toCollectionModel(Collection<Cozinha> list) {
		return list.stream().map(cozinha -> toModel(cozinha)).collect(Collectors.toList());
	}

	@Override
	public void copyToDomainObject(CozinhaInput cozinhaInput, Cozinha cozinha) {
		
		this.modelMapper.map(cozinhaInput, cozinha);
	}

	
}
