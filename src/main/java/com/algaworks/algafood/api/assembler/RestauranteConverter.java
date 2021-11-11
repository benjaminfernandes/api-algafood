package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;

//recebe a entidade a ser persistida ou consultada - Restaurante
//tem o modelo para representar a entidade na resposta - Restaurante Model
//tem o modelo de entrada de dados - Restaurante Input

@Component
public class RestauranteConverter implements Converter<Restaurante, RestauranteModel, RestauranteInput> {

	@Autowired
	private ModelMapper modelMapper;
	
	//Converte o padrão DTO Input para um modelo a ser persistido no BD
	@Override
	public Restaurante toDomain(RestauranteInput inputModel) {
		return modelMapper.map(inputModel, Restaurante.class);
	}

	//Converte a entidade para o padrão DTO
	@Override
	public RestauranteModel toModel(Restaurante domain) {
		
		return modelMapper.map(domain, RestauranteModel.class);//Biblioteca para conversão de entidades, Aula 11.14
	}

	//Gera lista para o padrão DTO
	@Override
	public List<RestauranteModel> paraModeloColecao(Collection<Restaurante> list) {
		return list.stream().map(restaurante -> toModel(restaurante)).collect(Collectors.toList());
	}

	@Override
	public void copyToDomainObject(RestauranteInput restauranteInput, Restaurante restaurante) { // usado no método de atualização de objetos
		//Para evitar identifier of an instance of com.algaworks.algafood.domain.model.Cozinha was altered from 1 to 2
		restaurante.setCozinha(new Cozinha());
		
		if(restaurante.getEndereco() != null) {
			restaurante.getEndereco().setCidade(new Cidade());
		}
		
		modelMapper.map(restauranteInput, restaurante);
	}
}
