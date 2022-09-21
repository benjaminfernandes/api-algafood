package com.algaworks.algafood.api.v1.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.converter.Converter;
import com.algaworks.algafood.api.v1.Algalinks;
import com.algaworks.algafood.api.v1.controller.RestauranteController;
import com.algaworks.algafood.api.v1.model.RestauranteModel;
import com.algaworks.algafood.api.v1.model.input.RestauranteInput;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;

//recebe a entidade a ser persistida ou consultada - Restaurante
//tem o modelo para representar a entidade na resposta - Restaurante Model
//tem o modelo de entrada de dados - Restaurante Input

@Component
public class RestauranteConverter extends RepresentationModelAssemblerSupport<Restaurante, RestauranteModel>
		implements Converter<Restaurante, RestauranteModel, RestauranteInput> {

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private AlgaSecurity algaSecurity;
	@Autowired
	private Algalinks algaLinks;

	public RestauranteConverter() {
		super(RestauranteController.class, RestauranteModel.class);
	}

	// Converte o padrão DTO Input para um modelo a ser persistido no BD
	@Override
	public Restaurante toDomain(RestauranteInput inputModel) {
		return modelMapper.map(inputModel, Restaurante.class);
	}

	// Converte a entidade para o padrão DTO
	@Override
	public RestauranteModel toModel(Restaurante domain) {
		RestauranteModel restauranteModel = createModelWithId(domain.getId(), domain);
		modelMapper.map(domain, restauranteModel);

		if (algaSecurity.podeConsultarRestaurantes()) {
			restauranteModel.add(algaLinks.linkToRestaurantes("restaurantes"));
		}

		restauranteModel.getCozinha().add(algaLinks.linkToCozinha(domain.getCozinha().getId()));

		if (algaSecurity.podeConsultarCidades()) {
			if (restauranteModel.getEndereco() != null && restauranteModel.getEndereco().getCidade() != null) {
				restauranteModel.getEndereco().getCidade()
						.add(algaLinks.linkToCidade(domain.getEndereco().getCidade().getId()));
			}
		}

		if (algaSecurity.podeConsultarRestaurantes()) {
			restauranteModel.add(algaLinks.linkToRestauranteFormasPagamento(domain.getId(), "formas-pagamento"));
		}

		if (algaSecurity.podeGerenciarCadastroRestaurantes()) {
			restauranteModel.add(algaLinks.linkToRestauranteResponsaveis(domain.getId(), "responsaveis"));
		}

		if (algaSecurity.podeGerenciarCadastroRestaurantes()) {
			if (domain.ativacaoPermitida()) {
				restauranteModel.add(algaLinks.linkToRestauranteAtivacao(domain.getId(), "ativar"));
			}

			if (domain.inativacaoPermitida()) {
				restauranteModel.add(algaLinks.linkToRestauranteInativacao(domain.getId(), "inativar"));
			}
		}

		if (algaSecurity.podeGerenciarFuncionamentoRestaurantes(domain.getId())) {
			if (domain.aberturaPermitida()) {
				restauranteModel.add(algaLinks.linkToRestauranteAbertura(domain.getId(), "abrir"));
			}

			if (domain.fechamentoPermitido()) {
				restauranteModel.add(algaLinks.linkToRestauranteFechamento(domain.getId(), "fechar"));
			}
		}

		return restauranteModel;
	}

	// Gera lista para o padrão DTO
	@Override
	public List<RestauranteModel> paraModeloColecao(Collection<Restaurante> list) {
		return list.stream().map(restaurante -> toModel(restaurante)).collect(Collectors.toList());
	}

	@Override
	public void copyToDomainObject(RestauranteInput restauranteInput, Restaurante restaurante) { // usado no método de
																									// atualização de
																									// objetos
		// Para evitar identifier of an instance of
		// com.algaworks.algafood.domain.model.Cozinha was altered from 1 to 2
		restaurante.setCozinha(new Cozinha());

		if (restaurante.getEndereco() != null) {
			restaurante.getEndereco().setCidade(new Cidade());
		}

		modelMapper.map(restauranteInput, restaurante);
	}

	//TODO PAREI NO RESTAURANTEBASICOMODELCONVERTER
	@Override
	public CollectionModel<RestauranteModel> toCollectionModel(Iterable<? extends Restaurante> entities) {
		CollectionModel<RestauranteModel> collectionModel = super.toCollectionModel(entities);
		if (algaSecurity.podeConsultarRestaurantes()) {
			collectionModel.add(algaLinks.linkToRestaurantes());
		}
		return collectionModel;
	}
}
