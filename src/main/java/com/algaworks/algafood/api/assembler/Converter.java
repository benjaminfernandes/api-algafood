package com.algaworks.algafood.api.assembler;

import java.util.List;

public interface Converter<T, S, U> {

	public T toDomain(U inputModel);
	public S toModel(T domain);
	public List<S> toCollectionModel(List<T> list);
	public void copyToDomainObject(U restauranteInput, T restaurante);
}
