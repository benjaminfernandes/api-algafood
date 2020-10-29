package com.algaworks.algafood.domain.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cidade;
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
	private CadastroCozinhaService cadastroCozinhaService;
	@Autowired
	private CadastroCidadeService cadastroCidadeService;

	@Transactional
	@Override
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		Long cidadeId = restaurante.getEndereco().getCidade().getId();
		
		Cozinha cozinha = cadastroCozinhaService.buscarOuFalhar(cozinhaId);
		Cidade cidade = cadastroCidadeService.buscarOuFalhar(cidadeId);
		
		restaurante.setCozinha(cozinha);
		restaurante.getEndereco().setCidade(cidade);
		return restauranteRepository.save(restaurante);
	}

	@Transactional
	@Override
	public void excluir(Long id) {
		
		try {
			this.restauranteRepository.deleteById(id);
			this.restauranteRepository.flush();
		}catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(RESTAURANTE_EM_USO, id));
		}catch (EmptyResultDataAccessException e) {
			throw new RestauranteNaoEncontradoException(String.format(RESTAURANTE_NAO_ENCONTRADO, id));
		}
	}
	
	@Transactional
	public void ativar(Long restauranteId) {
		Restaurante restauranteAtual = buscarOuFalhar(restauranteId);
		restauranteAtual.ativar();
	}
	@Transactional
	public void inativar(Long restauranteId) {
		Restaurante restauranteAtual = buscarOuFalhar(restauranteId);
		restauranteAtual.inativar();
	}

	public Restaurante buscarOuFalhar(Long id) {
		return this.restauranteRepository.findById(id).orElseThrow(() -> new RestauranteNaoEncontradoException(
				String.format("Não existe um cadastro de restaurante com o código %s", id)));
	}
}
