package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.PermissaoNaoEncontradaException;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.repository.PermissaoRepository;

@Service
public class CadastroPermissaoService implements CadastroService<Permissao> {

	private static final String MSG_PERMISSAO_EM_USO = "A permissão de código %d não pode ser removida, pois está em uso";
	
	@Autowired
	private PermissaoRepository permissaoRepository;
	
	@Transactional
	@Override
	public Permissao salvar(Permissao entity) {
		
		return this.permissaoRepository.save(entity);
	}

	@Transactional
	@Override
	public void excluir(Long id) {
		
		try {
			this.permissaoRepository.deleteById(id);
			this.permissaoRepository.flush();
		}catch (EmptyResultDataAccessException e) {
			throw new PermissaoNaoEncontradaException(id);
		}catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_PERMISSAO_EM_USO, id));
		}
	}

	@Transactional
	@Override
	public Permissao buscarOuFalhar(Long id) {
		return this.permissaoRepository.findById(id).orElseThrow(() -> new PermissaoNaoEncontradaException(id));
	}

}
