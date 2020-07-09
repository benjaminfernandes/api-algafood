package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService implements CadastroService<Restaurante> {

	private static final String RESTAURANTE_NAO_ENCONTRADO = "Não foi encontrado o restaurante de código %d!";
	private static final String RESTAURANTE_EM_USO = "Este restaurante de código %d está em uso!";
	@Autowired
	private RestauranteRepository restauranteRepository;
	@Autowired
	private CadastroService<Cozinha> cadastroCozinhaService;

	@Override
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		
		Cozinha cozinha = cadastroCozinhaService.buscarOuFalhar(cozinhaId);

		restaurante.setCozinha(cozinha);
		return restauranteRepository.save(restaurante);
	}

	@Override
	public void excluir(Long id) {
		
		try {
			this.restauranteRepository.deleteById(id);
		}catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(RESTAURANTE_EM_USO, id));
		}catch (EmptyResultDataAccessException e) {
			throw new RestauranteNaoEncontradoException(String.format(RESTAURANTE_NAO_ENCONTRADO, id));
		}
		
	}

	public Restaurante buscarOuFalhar(Long id) {
		return this.restauranteRepository.findById(id).orElseThrow(() -> new RestauranteNaoEncontradoException(
				String.format("Não existe um cadastro de restaurante com o código %s", id)));
	}
}
