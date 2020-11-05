package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.List;

import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.domain.model.FormaPagamento;

public interface Converter<T, S, U> {

	public T toDomain(U inputModel);
	public S toModel(T domain);
	public void copyToDomainObject(U inputModel, T model);
	public List<FormaPagamentoModel> toCollectionModel(Collection<FormaPagamento> list);
}
