package com.algaworks.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.model.Usuario;
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
	@Autowired
	private CadastroFormaPagamentoService cadastroFormaPagamentoService;
	@Autowired
	private CadastroUsuarioService usuarioService;

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
	
	@Transactional
	public void ativar(List<Long> restauranteIds) {
		restauranteIds.forEach(this::ativar);
	}
	
	@Transactional
	public void inativar(List<Long> restauranteIds) {
		restauranteIds.forEach(this::inativar);
	}
	
	@Transactional
	public void abrir(Long restauranteId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		restaurante.abrir();
	}
	
	@Transactional
	public void fechar(Long restauranteId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		restaurante.fechar();
	}
	
	@Transactional
	public void desassociarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		FormaPagamento formaPagamento = this.cadastroFormaPagamentoService.buscarOuFalhar(formaPagamentoId);
		restaurante.removerFormaPagamento(formaPagamento);
	}
	
	@Transactional
	public void associarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		FormaPagamento formaPagamento = this.cadastroFormaPagamentoService.buscarOuFalhar(formaPagamentoId);
		restaurante.adicionarFormaPagamento(formaPagamento);
	}
	
	@Transactional
	public void associarResponsavel(Long restaurante_id, Long usuario_id) {
		Restaurante restaurante = buscarOuFalhar(restaurante_id);
		Usuario usuario = this.usuarioService.buscarOuFalhar(usuario_id);
		
		restaurante.adicionarResponsavel(usuario);
	}
	
	@Transactional
	public void desassociarResponsavel(Long restaurante_id, Long usuario_id) {
		Restaurante restaurante = buscarOuFalhar(restaurante_id);
		Usuario usuario = this.usuarioService.buscarOuFalhar(usuario_id);
		
		restaurante.removerResponsavel(usuario);
	}

	public Restaurante buscarOuFalhar(Long id) {
		return this.restauranteRepository.findById(id).orElseThrow(() -> new RestauranteNaoEncontradoException(
				String.format("Não existe um cadastro de restaurante com o código %s", id)));
	}
}
