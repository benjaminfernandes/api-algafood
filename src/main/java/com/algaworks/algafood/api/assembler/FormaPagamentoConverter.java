package com.algaworks.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.api.model.input.FormaPagamentoInput;
import com.algaworks.algafood.domain.model.FormaPagamento;

@Component
public class FormaPagamentoConverter implements Converter<FormaPagamento, FormaPagamentoModel, FormaPagamentoInput>{

	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public FormaPagamento toDomain(FormaPagamentoInput inputModel) {
		return modelMapper.map(inputModel, FormaPagamento.class);
	}

	@Override
	public FormaPagamentoModel toModel(FormaPagamento domain) {
		return modelMapper.map(domain, FormaPagamentoModel.class);
	}

	@Override
	public List<FormaPagamentoModel> toCollectionModel(List<FormaPagamento> list) {
		return list.stream().map(formaPagamento -> toModel(formaPagamento)).collect(Collectors.toList());
	}

	@Override
	public void copyToDomainObject(FormaPagamentoInput formaPagamentoInput, FormaPagamento formaPagamento) {
		this.modelMapper.map(formaPagamentoInput, formaPagamento);
		
	}

}
