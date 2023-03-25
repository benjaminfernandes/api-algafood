package com.algaworks.algafood.api.v1.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.assembler.CozinhaConverter;
import com.algaworks.algafood.api.v1.model.CozinhaModel;
import com.algaworks.algafood.api.v1.model.input.CozinhaInput;
import com.algaworks.algafood.api.v1.openapi.controller.CozinhaControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroService;

@RestController
@RequestMapping(value = "/v1/cozinhas")
public class CozinhaController implements CozinhaControllerOpenApi{

	@Autowired
	private CozinhaRepository cozinhaRepository;
	@Autowired
	private CadastroService<Cozinha> cadastroCozinha;
	@Autowired
	private CozinhaConverter cozinhaConverter;
	@Autowired
	private PagedResourcesAssembler<Cozinha> pagedResourcesAssembler;

	@CheckSecurity.Cozinhas.PodeConsultar
	@GetMapping
	public PagedModel<CozinhaModel> listar(@PageableDefault(size = 10) Pageable pageable) {
		//System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities()); //verifica os escopos que o jwt possui
		Page<Cozinha> cozinhasPage = cozinhaRepository.findAll(pageable);
		
		//Aula hateoas 19.15
		PagedModel<CozinhaModel> cozinhasPagedModel = this.pagedResourcesAssembler
				.toModel(cozinhasPage, cozinhaConverter);
		
		return cozinhasPagedModel;
	}

	@CheckSecurity.Cozinhas.PodeConsultar
	@GetMapping("/{cozinhaId}")
	public CozinhaModel buscar(@PathVariable Long cozinhaId) {
		Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);
		
		return this.cozinhaConverter.toModel(cozinha);

		/*
		 * Optional<Cozinha> cozinha = cozinhaRepository.findById(cozinhaId);
		 * 
		 * if (cozinha.isPresent()) { return ResponseEntity.ok(cozinha.get()); }
		 * 
		 * return ResponseEntity.notFound().build();
		 */
	}

	@CheckSecurity.Cozinhas.PodeEditar
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CozinhaModel adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) {
		
		Cozinha cozinha = this.cozinhaConverter.toDomain(cozinhaInput);
		
		return this.cozinhaConverter.toModel(cadastroCozinha.salvar(cozinha));
	}

	@CheckSecurity.Cozinhas.PodeEditar
	@PutMapping("/{cozinhaId}")
	public CozinhaModel atualizar(@PathVariable Long cozinhaId, @RequestBody @Valid CozinhaInput cozinhaInput) {

		Cozinha cozinhaAtual = cadastroCozinha.buscarOuFalhar(cozinhaId);

		this.cozinhaConverter.copyToDomainObject(cozinhaInput, cozinhaAtual);
		
		return this.cozinhaConverter.toModel(cadastroCozinha.salvar(cozinhaAtual));
	}

	/*
	 * @DeleteMapping("/{cozinhaId}") public ResponseEntity<?> remover(@PathVariable
	 * Long cozinhaId) { try { cadastroCozinha.excluir(cozinhaId);
	 * 
	 * return ResponseEntity.noContent().build();
	 * }catch(EntidadeNaoEncontradaException e) { return
	 * ResponseEntity.notFound().build();
	 * 
	 * } catch (EntidadeEmUsoException e) { return
	 * ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); } }
	 */

	@CheckSecurity.Cozinhas.PodeEditar
	@DeleteMapping("/{cozinhaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cozinhaId) {

		cadastroCozinha.excluir(cozinhaId);
	}
}
