package com.algaworks.algafood.api.v1.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.Algalinks;
import com.algaworks.algafood.api.v1.controller.PermissaoController;
import com.algaworks.algafood.api.v1.model.PermissaoModel;
import com.algaworks.algafood.api.v1.model.input.PermissaoInput;
import com.algaworks.algafood.domain.model.Permissao;

@Component
public class PermissaoConverter extends RepresentationModelAssemblerSupport<Permissao, PermissaoModel> implements Converter<Permissao, PermissaoModel, PermissaoInput> {

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
    private Algalinks algaLinks;
	
	public PermissaoConverter() {
		super(PermissaoController.class, PermissaoModel.class);
	}
	
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
	public List<PermissaoModel> paraModeloColecao(Collection<Permissao> list) {
		return list.stream().map(permissaoModel -> toModel(permissaoModel)).collect(Collectors.toList());
	}
	
	@Override
	public CollectionModel<PermissaoModel> toCollectionModel(Iterable<? extends Permissao> entities) {
		return super.toCollectionModel(entities)
				.add(algaLinks.linkToPermissoes());
	}
}
