package com.algaworks.algafood.api.converter;

import java.util.Collection;
import java.util.List;

public interface Converter<T, S, U> {

	public T toDomain(U inputModel);
	public S toModel(T domain);
	public void copyToDomainObject(U inputModel, T model);
	public List<S> paraModeloColecao(Collection<T> list);
}
