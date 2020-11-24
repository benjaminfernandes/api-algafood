package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.PermissaoModel;
import com.algaworks.algafood.api.model.input.PermissaoInput;
import com.algaworks.algafood.domain.model.Permissao;

@Component
public class PermissaoConverter implements Converter<Permissao, PermissaoModel, PermissaoInput> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Permissao toDomain(PermissaoInput inputModel) {
		return this.modelMapper.map(inputModel, Permissao.class);
	}

	@Override
	public PermissaoModel toModel(Permissao domain) {
		return this.modelMapper.map(domain, PermissaoModel.class);
	}

	@Override
	public void copyToDomainObject(PermissaoInput inputModel, Permissao model) {
		this.modelMapper.map(inputModel, model);
	}

	@Override
	public List<PermissaoModel> toCollectionModel(Collection<Permissao> list) {
		return list.stream().map(permissaoModel -> toModel(permissaoModel)).collect(Collectors.toList());
	}
}
