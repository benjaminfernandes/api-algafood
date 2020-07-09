package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.CidadeRepository;

@Service
public class CadastroCidadeService implements CadastroService<Cidade> {

	private static final String MSG_CIDADE_EM_USO = "Esta cidade com código %d está em uso";
	private static final String MSG_CIDADE_NAO_ENCONTRADA = "Não foi possível encontrar a cidade com o código %d";
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private CadastroService<Estado> cadastroEstado;

	@Override
	public Cidade salvar(Cidade cidade) {

		Long estadoId = cidade.getEstado().getId();
		Estado estado = cadastroEstado.buscarOuFalhar(estadoId);
		cidade.setEstado(estado);
		
		return this.cidadeRepository.save(cidade);
	}

	@Override
	public void excluir(Long cidadeId) {

		try {
			this.cidadeRepository.deleteById(cidadeId);
		} catch (EmptyResultDataAccessException e) {
			throw new CidadeNaoEncontradaException(
					String.format(MSG_CIDADE_NAO_ENCONTRADA, cidadeId));
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_CIDADE_EM_USO, cidadeId));
		}
	}

	public Cidade buscarOuFalhar(Long cidadeId) {
		return this.cidadeRepository.findById(cidadeId).orElseThrow(() -> new CidadeNaoEncontradaException(
				String.format("Não existe um cadastro de cidade com o código %s", cidadeId)));
	}
}
