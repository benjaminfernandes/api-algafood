package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.FotoProdutoModel;
import com.algaworks.algafood.api.model.input.FotoProdutoInput;
import com.algaworks.algafood.domain.model.FotoProduto;

@Component
public class FotoProdutoModelAssembler implements Converter<FotoProduto, FotoProdutoModel, FotoProdutoInput> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public FotoProduto toDomain(FotoProdutoInput inputModel) {
		return modelMapper.map(modelMapper, FotoProduto.class);
	}

	@Override
	public FotoProdutoModel toModel(FotoProduto domain) {
		return this.modelMapper.map(domain, FotoProdutoModel.class);
	}

	@Override
	public void copyToDomainObject(FotoProdutoInput inputModel, FotoProduto model) {
	}

	@Override
	public List<FotoProdutoModel> paraModeloColecao(Collection<FotoProduto> list) {
		return null;
	}
}
