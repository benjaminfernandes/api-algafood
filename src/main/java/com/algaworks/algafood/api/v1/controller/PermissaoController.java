package com.algaworks.algafood.api.v1.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
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

import com.algaworks.algafood.api.v1.assembler.PermissaoConverter;
import com.algaworks.algafood.api.v1.model.PermissaoModel;
import com.algaworks.algafood.api.v1.model.input.PermissaoInput;
import com.algaworks.algafood.api.v1.openapi.controller.PermissaoControllerOpenApi;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.repository.PermissaoRepository;
import com.algaworks.algafood.domain.service.CadastroPermissaoService;

@RestController
@RequestMapping("/permissoes")
public class PermissaoController implements PermissaoControllerOpenApi {

	@Autowired
	private PermissaoRepository permissaoRepository;
	@Autowired
	private PermissaoConverter permissaoConverter;
	@Autowired
	private CadastroPermissaoService permissaoService;
	
	@GetMapping
	public CollectionModel<PermissaoModel> listar(){
		return this.permissaoConverter.toCollectionModel(this.permissaoRepository.findAll());
	}
	
	@GetMapping("/{permissaoId}")
	public PermissaoModel buscar(@PathVariable Long permissaoId) {
		Permissao permissao = this.permissaoService.buscarOuFalhar(permissaoId);
		return this.permissaoConverter.toModel(permissao);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PermissaoModel salvar(@RequestBody @Valid PermissaoInput permissaoInput) {
		Permissao permissao = this.permissaoConverter.toDomain(permissaoInput);
		permissao = this.permissaoService.salvar(permissao);
		return this.permissaoConverter.toModel(permissao);
	}
	
	@PutMapping("/{permissaoId}")
	public PermissaoModel atualizar(@PathVariable Long permissaoId, @RequestBody @Valid PermissaoInput permissaoInput) {
		Permissao permissaoAtual = this.permissaoService.buscarOuFalhar(permissaoId);
		this.permissaoConverter.copyToDomainObject(permissaoInput, permissaoAtual);
		permissaoAtual = this.permissaoService.salvar(permissaoAtual);
		
		return this.permissaoConverter.toModel(permissaoAtual);
		
	}
	
	@DeleteMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long permissaoId) {
		this.permissaoService.excluir(permissaoId);
	}
}
