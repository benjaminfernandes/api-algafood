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

import com.algaworks.algafood.api.v1.assembler.EstadoConverter;
import com.algaworks.algafood.api.v1.model.EstadoModel;
import com.algaworks.algafood.api.v1.model.input.EstadoInput;
import com.algaworks.algafood.api.v1.openapi.controller.EstadoControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.service.CadastroService;

@RestController
@RequestMapping("/v1/estados")
public class EstadoController implements EstadoControllerOpenApi {

	@Autowired
	private EstadoRepository estadoRepository;
	@Autowired
	private CadastroService<Estado> cadastroEstadoService;
	@Autowired
	private EstadoConverter estadoConverter;

	@CheckSecurity.Estados.PodeConsultar
	@GetMapping
	public CollectionModel<EstadoModel> listar() {
		return this.estadoConverter.toCollectionModel(this.estadoRepository.findAll());
	}

	@CheckSecurity.Estados.PodeEditar
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EstadoModel salvar(@RequestBody @Valid EstadoInput estadoInput) {

		Estado estado = this.estadoConverter.toDomain(estadoInput);
		
		return this.estadoConverter.toModel(this.cadastroEstadoService.salvar(estado));
	}

	@CheckSecurity.Estados.PodeConsultar
	@GetMapping("/{estadoId}")
	public EstadoModel buscar(@PathVariable Long estadoId) {
		Estado estado = this.cadastroEstadoService.buscarOuFalhar(estadoId);
		return this.estadoConverter.toModel(estado);
	}

	@CheckSecurity.Estados.PodeEditar
	@PutMapping("/{estadoId}")
	public EstadoModel atualizar(@PathVariable Long estadoId, @RequestBody @Valid EstadoInput estadoInput) {

		Estado estadoAtual = this.cadastroEstadoService.buscarOuFalhar(estadoId);
		this.estadoConverter.copyToDomainObject(estadoInput, estadoAtual);
		return this.estadoConverter.toModel(this.cadastroEstadoService.salvar(estadoAtual));
	}

	@CheckSecurity.Estados.PodeEditar
	@DeleteMapping("/{estadoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long estadoId) {

			this.cadastroEstadoService.excluir(estadoId);
		
	}
}
