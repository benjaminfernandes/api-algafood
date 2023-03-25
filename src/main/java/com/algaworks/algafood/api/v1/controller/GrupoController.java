package com.algaworks.algafood.api.v1.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.assembler.GrupoConverter;
import com.algaworks.algafood.api.v1.model.GrupoModel;
import com.algaworks.algafood.api.v1.model.input.GrupoInput;
import com.algaworks.algafood.api.v1.openapi.controller.GrupoControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.repository.GrupoRepository;
import com.algaworks.algafood.domain.service.CadastroGrupoService;

@RestController
@RequestMapping(path = "/v1/grupos", produces = MediaType.APPLICATION_JSON_VALUE)
public class GrupoController implements GrupoControllerOpenApi {

	@Autowired
	private GrupoRepository grupoRepository;
	@Autowired
	private CadastroGrupoService cadastroGrupoService;
	@Autowired
	private GrupoConverter grupoConverter;
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
	@Override
	@GetMapping
	public CollectionModel<GrupoModel> listar(){
		return this.grupoConverter.toCollectionModel(this.grupoRepository.findAll())
				.removeLinks();
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
	@Override
	@GetMapping("/{codigo}")
	public GrupoModel buscar(@PathVariable("codigo") Long codigo) {
		Grupo grupo = this.cadastroGrupoService.buscarOuFalhar(codigo);
		return this.grupoConverter.toModel(grupo);
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public GrupoModel adicionar(@Valid @RequestBody GrupoInput grupoinput) {
		Grupo grupo = this.grupoConverter.toDomain(grupoinput);
		return this.grupoConverter.toModel(this.cadastroGrupoService.salvar(grupo));
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@Override
	@PutMapping("/{codigo}")
	public GrupoModel alterar(@PathVariable("codigo") Long codigo, @Valid @RequestBody GrupoInput grupoInput) {
		Grupo grupo = this.cadastroGrupoService.buscarOuFalhar(codigo);
		this.grupoConverter.copyToDomainObject(grupoInput, grupo);
		return this.grupoConverter.toModel(this.cadastroGrupoService.salvar(grupo));
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@Override
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable("codigo") Long codigo) {
		this.cadastroGrupoService.excluir(codigo);
	}
}
