package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.Algalinks;
import com.algaworks.algafood.api.controller.GrupoController;
import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.api.model.input.GrupoInput;
import com.algaworks.algafood.domain.model.Grupo;

@Component
public class GrupoConverter extends RepresentationModelAssemblerSupport<Grupo, GrupoModel> implements Converter<Grupo, GrupoModel, GrupoInput> {

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
    private Algalinks algaLinks;
    
    public GrupoConverter() {
        super(GrupoController.class, GrupoModel.class);
    }
	
	@Override
	public Grupo toDomain(GrupoInput inputModel) {
		return this.modelMapper.map(inputModel, Grupo.class);
	}

	@Override
	public GrupoModel toModel(Grupo domain) {
		
		GrupoModel grupoModel = createModelWithId(domain.getId(), domain);
		this.modelMapper.map(domain, grupoModel);
		grupoModel.add(algaLinks.linkToGrupos("grupos"));
		grupoModel.add(algaLinks.linkToGrupoPermissoes(domain.getId(), "permissoes"));
		
		return grupoModel;
	}

	@Override
	public List<GrupoModel> paraModeloColecao(Collection<Grupo> list) {
		
		return list.stream().map(grupo -> toModel(grupo)).collect(Collectors.toList());
	}

	@Override
	public void copyToDomainObject(GrupoInput inputModel, Grupo grupo) {
		this.modelMapper.map(inputModel, grupo);
	}
	
	@Override
	public CollectionModel<GrupoModel> toCollectionModel(Iterable<? extends Grupo> entities) {
		return super.toCollectionModel(entities)
				.add(algaLinks.linkToGrupos());
	}
}
