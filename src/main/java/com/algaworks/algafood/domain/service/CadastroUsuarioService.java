package com.algaworks.algafood.domain.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;

@Service
public class CadastroUsuarioService implements CadastroService<Usuario> {

	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private CadastroGrupoService grupoService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Transactional
	@Override
	public Usuario salvar(Usuario usuario) {
		
		this.usuarioRepository.detach(usuario);//Aula 12.11 Tira o objeto do contexto de persistencia
		Optional<Usuario> usuarioExistente = this.usuarioRepository.findByEmail(usuario.getEmail());
		
		if(usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)) {
			throw new NegocioException(String.format("Já existe um usuário cadastrado com o e-mail %s", usuario.getEmail()));
		}
		
		if (usuario.isNovo()) {
			usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
		}
		
		return this.usuarioRepository.save(usuario);
	}

	@Transactional
	@Override
	public void excluir(Long id) {
		try {
			this.usuarioRepository.deleteById(id);
		}catch (DataIntegrityViolationException e) {
			
		}catch (EmptyResultDataAccessException e) {
			throw new UsuarioNaoEncontradoException(id);
		}
	}
	
	@Override
	public Usuario buscarOuFalhar(Long id) {
		return this.usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException(id));
	}
	
	@Transactional
	public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
	    Usuario usuario = buscarOuFalhar(usuarioId);
	    
	    if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
	        throw new NegocioException("Senha atual informada não coincide com a senha do usuário.");
	    }
	    
	    usuario.setSenha(passwordEncoder.encode(novaSenha));
	}
	
	@Transactional
	public void associarGrupo(Long usuarioId, Long grupoId) {
		Usuario usuario = buscarOuFalhar(usuarioId);
		Grupo grupo = this.grupoService.buscarOuFalhar(grupoId);
		
		usuario.adicionaGrupo(grupo);
	}
	
	@Transactional
	public void desassociarGrupo(Long usuarioId, Long grupoId) {
		Usuario usuario = buscarOuFalhar(usuarioId);
		Grupo grupo = this.grupoService.buscarOuFalhar(grupoId);
		
		usuario.removerGrupo(grupo);
	}

}
