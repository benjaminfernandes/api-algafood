package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.ProdutoModel;
import com.algaworks.algafood.api.model.input.ProdutoInput;
import com.algaworks.algafood.domain.model.Produto;

@Component
public class ProdutoConverter implements Converter<Produto, ProdutoModel, ProdutoInput> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Produto toDomain(ProdutoInput inputModel) {
		return this.modelMapper.map(inputModel, Produto.class);
	}

	@Override
	public ProdutoModel toModel(Produto domain) {
		return this.modelMapper.map(domain, ProdutoModel.class);
	}

	@Override
	public void copyToDomainObject(ProdutoInput inputModel, Produto model) {
		this.modelMapper.map(inputModel, model);
	}

	@Override
	public List<ProdutoModel> toCollectionModel(Collection<Produto> list) {
		return list.stream().map(produto -> toModel(produto)).collect(Collectors.toList());
	}

}
