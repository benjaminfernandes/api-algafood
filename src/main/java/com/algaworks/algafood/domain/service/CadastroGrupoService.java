package com.algaworks.algafood.domain.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.GrupoNaoEncontradaException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.repository.GrupoRepository;

@Service
public class CadastroGrupoService implements CadastroService<Grupo> {

	@Autowired
	private GrupoRepository grupoRepository;
	@Autowired
	private CadastroPermissaoService permissaoService;
	
	@Override
	@Transactional
	public Grupo salvar(Grupo entity) {
		return this.grupoRepository.save(entity);
	}

	@Override
	@Transactional
	public void excluir(Long id) {
		try {
			this.grupoRepository.deleteById(id);
			this.grupoRepository.flush();
		}catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format("Não foi possível excluir o grupo de código %s", id));
		}catch(EmptyResultDataAccessException e) {
			throw new GrupoNaoEncontradaException(id);
		}
	}

	@Override
	public Grupo buscarOuFalhar(Long id) {
		return this.grupoRepository.findById(id).orElseThrow(() -> new GrupoNaoEncontradaException(id));
	}
	
	@Transactional
	public void associarPermissao(Long grupoId, Long permissaoId) {
		Grupo grupo = buscarOuFalhar(grupoId);
		Permissao permissao = this.permissaoService.buscarOuFalhar(permissaoId);
		
		grupo.adicionaPermissao(permissao);
	}
	
	@Transactional
	public void desassociarPermissao(Long grupoId, Long permissaoId) {
		Grupo grupo = buscarOuFalhar(grupoId);
		Permissao permissao = this.permissaoService.buscarOuFalhar(permissaoId);
		
		grupo.retiraPermissao(permissao);
	}
}
