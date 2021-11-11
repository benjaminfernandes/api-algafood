package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.api.model.input.GrupoInput;
import com.algaworks.algafood.domain.model.Grupo;

@Component
public class GrupoConverter implements Converter<Grupo, GrupoModel, GrupoInput> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Grupo toDomain(GrupoInput inputModel) {
		return this.modelMapper.map(inputModel, Grupo.class);
	}

	@Override
	public GrupoModel toModel(Grupo domain) {
		return this.modelMapper.map(domain, GrupoModel.class);
	}

	@Override
	public List<GrupoModel> paraModeloColecao(Collection<Grupo> list) {
		
		return list.stream().map(grupo -> toModel(grupo)).collect(Collectors.toList());
	}

	@Override
	public void copyToDomainObject(GrupoInput inputModel, Grupo grupo) {
		this.modelMapper.map(inputModel, grupo);
	}
}
