package com.algaworks.algafood.domain.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

@Service
public class CadastroCozinhaService implements CadastroService<Cozinha> {

	private static final String MSG_COZINHA_EM_USO = "Cozinha de código %d não pode ser removida, pois está em uso";
	private static final String MSG_COZINHA_NAO_ENCONTRADA = "Não existe um cadastro de cozinha com código %d";
	
	@Autowired
	private CozinhaRepository cozinhaRepository;

	@Transactional
	@Override
	public Cozinha salvar(Cozinha cozinha) {

		return cozinhaRepository.save(cozinha);
	}
	
	@Transactional
	@Override
	public void excluir(Long idCozinha) {
		try {
			cozinhaRepository.deleteById(idCozinha);

		} catch (EmptyResultDataAccessException e) {

			throw new CozinhaNaoEncontradaException(
					String.format(MSG_COZINHA_NAO_ENCONTRADA, idCozinha));

		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_COZINHA_EM_USO, idCozinha));
		}
	}
	
	public Cozinha buscarOuFalhar(Long cozinhaId) {
		return this.cozinhaRepository.findById(cozinhaId).orElseThrow(() -> new CozinhaNaoEncontradaException(
				String.format("Não existe um cadastro de cozinha com o código %s", cozinhaId)));
	}
}
