package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.api.model.input.UsuarioInput;
import com.algaworks.algafood.domain.model.Usuario;

@Component
public class UsuarioConverter implements Converter<Usuario, UsuarioModel, UsuarioInput> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Usuario toDomain(UsuarioInput inputModel) {
		return this.modelMapper.map(inputModel, Usuario.class);
	}

	@Override
	public UsuarioModel toModel(Usuario domain) {
		return this.modelMapper.map(domain, UsuarioModel.class);
	}

	@Override
	public List<UsuarioModel> paraModeloColecao(Collection<Usuario> list) {
		return list.stream().map(usuario -> toModel(usuario)).collect(Collectors.toList());
	}

	@Override
	public void copyToDomainObject(UsuarioInput inputModel, Usuario model) {
		this.modelMapper.map(inputModel, model);
	}

}
