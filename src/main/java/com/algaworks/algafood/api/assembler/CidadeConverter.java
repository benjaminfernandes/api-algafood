package com.algaworks.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.api.model.input.CidadeInput;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;

@Component
public class CidadeConverter implements Converter<Cidade, CidadeModel, CidadeInput> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Cidade toDomain(CidadeInput inputModel) {
		return this.modelMapper.map(inputModel, Cidade.class);
	}

	@Override
	public CidadeModel toModel(Cidade domain) {
		return this.modelMapper.map(domain, CidadeModel.class);
	}

	@Override
	public List<CidadeModel> toCollectionModel(List<Cidade> list) {
		return list.stream().map(cidade -> toModel(cidade)).collect(Collectors.toList());
	}

	@Override
	public void copyToDomainObject(CidadeInput cidadeInput, Cidade cidade) {
		cidade.setEstado(new Estado());
		this.modelMapper.map(cidadeInput, cidade);
	}

}
