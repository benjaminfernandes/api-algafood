package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.algaworks.algafood.api.assembler.GrupoConverter;
import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.api.model.input.GrupoInput;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.repository.GrupoRepository;
import com.algaworks.algafood.domain.service.CadastroGrupoService;

@RestController
@RequestMapping("/grupos")
public class GrupoController {

	@Autowired
	private GrupoRepository grupoRepository;
	@Autowired
	private CadastroGrupoService cadastroGrupoService;
	@Autowired
	private GrupoConverter grupoConverter;
	
	@GetMapping
	public List<GrupoModel> listar(){
		return this.grupoConverter.toCollectionModel(this.grupoRepository.findAll());
	}
	
	@GetMapping("/{codigo}")
	public GrupoModel buscar(@PathVariable("codigo") Long codigo) {
		Grupo grupo = this.cadastroGrupoService.buscarOuFalhar(codigo);
		return this.grupoConverter.toModel(grupo);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public GrupoModel adicionar(@Valid @RequestBody GrupoInput grupoinput) {
		Grupo grupo = this.grupoConverter.toDomain(grupoinput);
		return this.grupoConverter.toModel(this.cadastroGrupoService.salvar(grupo));
	}
	
	@PutMapping("/{codigo}")
	public GrupoModel alterar(@PathVariable("codigo") Long codigo, @Valid @RequestBody GrupoInput grupoInput) {
		Grupo grupo = this.cadastroGrupoService.buscarOuFalhar(codigo);
		this.grupoConverter.copyToDomainObject(grupoInput, grupo);
		return this.grupoConverter.toModel(this.cadastroGrupoService.salvar(grupo));
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable("codigo") Long codigo) {
		this.cadastroGrupoService.excluir(codigo);
	}
}
