package com.algaworks.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.CozinhaModel;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;

//recebe a entidade a ser persistida ou consultada - Restaurante
//tem o modelo para representar a entidade na resposta - Restaurante Model
//tem o modelo de entrada de dados - Restaurante Input

@Component
public class RestauranteConverter implements Converter<Restaurante, RestauranteModel, RestauranteInput> {

	//Converte o padrão DTO Input para um modelo a ser persistido no BD
	@Override
	public Restaurante toDomain(RestauranteInput inputModel) {
		Restaurante restaurante = new Restaurante();
		restaurante.setNome(inputModel.getNome());
		restaurante.setTaxaFrete(inputModel.getTaxaFrete());
		
		Cozinha cozinha = new Cozinha();
		cozinha.setId(inputModel.getCozinha().getId());
		restaurante.setCozinha(cozinha);
		
		return restaurante;
	}

	//Converte a entidade para o padrão DTO
	@Override
	public RestauranteModel toModel(Restaurante domain) {
		CozinhaModel cozinhaModel = new CozinhaModel();
		cozinhaModel.setId(domain.getCozinha().getId());
		cozinhaModel.setNome(domain.getCozinha().getNome());
		
		RestauranteModel restauranteModel = new RestauranteModel();
		restauranteModel.setId(domain.getId());
		restauranteModel.setNome(domain.getNome());
		restauranteModel.setTaxaFrete(domain.getTaxaFrete());
		restauranteModel.setCozinha(cozinhaModel);
		return restauranteModel;
	}

	//Gera lista para o padrão DTO
	@Override
	public List<RestauranteModel> toCollectionModel(List<Restaurante> list) {
		return list.stream().map(restaurante -> toModel(restaurante)).collect(Collectors.toList());
	}
}
