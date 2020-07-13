package com.algaworks.algafood;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CadastroCozinhaIntegrationTests {

	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	@Test
	public void testarCadastroCozinhaComSucesso() {
		//cenário
		
		var novaCozinha = new Cozinha();
		novaCozinha.setNome("Paraguaya");
		
		//ação
		
		novaCozinha = this.cadastroCozinha.salvar(novaCozinha);
		
		//validação
		
		assertThat(novaCozinha).isNotNull();
		assertThat(novaCozinha.getId()).isNotNull();
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void deveFalhar_QuandoCadastrarCozinhaSemNome() {
		var novaCozinha = new Cozinha();
		novaCozinha.setNome(null);
		
		novaCozinha = this.cadastroCozinha.salvar(novaCozinha);
	}
	
	@Test(expected = EntidadeEmUsoException.class)
	public void deveFalhar_QuandoExcluirCozinhaEmUso() {
		
		this.cadastroCozinha.excluir(1L);
	}
	
	@Test(expected = CozinhaNaoEncontradaException.class)
	public void deveFalhar_QuandoExcluirCozinhaInexistente() {
		
		this.cadastroCozinha.excluir(25L);
	}

}
