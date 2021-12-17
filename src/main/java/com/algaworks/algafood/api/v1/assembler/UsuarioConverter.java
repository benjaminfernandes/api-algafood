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
import com.algaworks.algafood.api.v1.controller.UsuarioController;
import com.algaworks.algafood.api.v1.model.UsuarioModel;
import com.algaworks.algafood.api.v1.model.input.UsuarioInput;
import com.algaworks.algafood.domain.model.Usuario;

@Component
public class UsuarioConverter extends RepresentationModelAssemblerSupport<Usuario, UsuarioModel> 
	implements Converter<Usuario, UsuarioModel, UsuarioInput> {

	@Autowired
	private Algalinks algaLinks;
	
	public UsuarioConverter() {
		super(UsuarioController.class, UsuarioModel.class);
	}

	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Usuario toDomain(UsuarioInput inputModel) {
		return this.modelMapper.map(inputModel, Usuario.class);
	}

	@Override
	public UsuarioModel toModel(Usuario domain) {
		UsuarioModel usuarioModel = createModelWithId(domain.getId(), domain);
		this.modelMapper.map(domain, usuarioModel);
		
		//usuarioModel.add(linkTo(methodOn(UsuarioController.class).listar()).withRel("usuarios"));
		
		//usuarioModel.add(linkTo(methodOn(UsuarioGrupoController.class)
				//.listar(usuarioModel.getId())).withRel("grupos-usuario"));
		
	    usuarioModel.add(algaLinks.linkToUsuarios("usuarios"));
	    usuarioModel.add(algaLinks.linkToGruposUsuario(domain.getId(), "grupos-usuario"));
		
		return usuarioModel;
	}

	@Override
	public List<UsuarioModel> paraModeloColecao(Collection<Usuario> list) {
		return list.stream().map(usuario -> toModel(usuario)).collect(Collectors.toList());
	}

	@Override
	public void copyToDomainObject(UsuarioInput inputModel, Usuario model) {
		this.modelMapper.map(inputModel, model);
	}
	
	@Override
	public CollectionModel<UsuarioModel> toCollectionModel(Iterable<? extends Usuario> entities) {
		return super.toCollectionModel(entities)
		        .add(algaLinks.linkToUsuarios());
	}

}
