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

import com.algaworks.algafood.api.v1.assembler.UsuarioConverter;
import com.algaworks.algafood.api.v1.model.UsuarioModel;
import com.algaworks.algafood.api.v1.model.input.SenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioComSenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioInput;
import com.algaworks.algafood.api.v1.openapi.controller.UsuarioControllerOpenApi;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;
import com.algaworks.algafood.domain.service.CadastroUsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController implements UsuarioControllerOpenApi {

	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;
	@Autowired
	private UsuarioConverter usuarioConverter;
	
	@GetMapping
	public CollectionModel<UsuarioModel> listar(){
		return this.usuarioConverter.toCollectionModel(this.usuarioRepository.findAll());
	}
	
	@GetMapping("/{codigo}")
	public UsuarioModel buscar(@PathVariable Long codigo) {
		return this.usuarioConverter.toModel(this.cadastroUsuarioService.buscarOuFalhar(codigo));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UsuarioModel adicionar(@Valid @RequestBody UsuarioComSenhaInput usuarioComSenhaInput) {
		Usuario usuario = this.usuarioConverter.toDomain(usuarioComSenhaInput);
		return this.usuarioConverter.toModel(this.cadastroUsuarioService.salvar(usuario));
	}
	
	@PutMapping("/{codigo}")
	public UsuarioModel atualizar(@PathVariable Long codigo, @Valid @RequestBody UsuarioInput usuarioInput) {
		Usuario usuario = this.cadastroUsuarioService.buscarOuFalhar(codigo);
		this.usuarioConverter.copyToDomainObject(usuarioInput, usuario);
		return this.usuarioConverter.toModel(this.cadastroUsuarioService.salvar(usuario));
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long codigo) {
		this.cadastroUsuarioService.excluir(codigo);
	}
	
	@PutMapping("/{codigo}/senha")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void alterarSenha(@PathVariable Long codigo, @Valid @RequestBody SenhaInput senhaInput) {
		this.cadastroUsuarioService.alterarSenha(codigo, senhaInput.getSenhaAtual(), senhaInput.getNovaSenha());
	}
}
