package com.algaworks.algafood.domain.service;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradaException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;

@Service
public class CadastroEstadoService implements CadastroService<Estado> {

	private static final String ESTADO_EM_USO = "O estado com código %d está em uso e não pôde ser excluído!";
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Transactional
	@Override
	public Estado salvar(Estado estado) {
			
		return this.estadoRepository.save(estado);
	}
	@Transactional
	@Override
	public void excluir(Long estadoId) {
		try {
			this.estadoRepository.deleteById(estadoId);
			this.estadoRepository.flush();
		}catch (EmptyResultDataAccessException e) {
			throw new EstadoNaoEncontradaException(estadoId);
		}catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(ESTADO_EM_USO, estadoId));
		}
	}

	public Estado buscarOuFalhar(Long id) {
		return this.estadoRepository.findById(id).orElseThrow(() -> new EstadoNaoEncontradaException(
				String.format("Não existe um cadastro de estado com o código %s", id)));
	}
	
}
