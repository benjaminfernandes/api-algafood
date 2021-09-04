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

import com.algaworks.algafood.api.assembler.CidadeConverter;
import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.api.model.input.CidadeInput;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Cidades")
@RestController
@RequestMapping("/cidades")
public class CidadeController {

	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private CadastroService<Cidade> cadastroCidadeService;
	@Autowired
	private CidadeConverter cidadeConverter;
	
	@ApiOperation("Lista as cidades")
	@GetMapping
	public List<CidadeModel> listar() {
		return this.cidadeConverter.toCollectionModel(this.cidadeRepository.findAll());
	}

	@ApiOperation("Salvar cidade")
	@ApiResponses({		
		@ApiResponse(code = 201, message = "Cidade cadastrada")
	})
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeModel salvar(@ApiParam(name = "corpo", value = "Representação de uma nova cidade") 
		@RequestBody @Valid CidadeInput cidadeInput) {
		try {
			Cidade cidade = this.cidadeConverter.toDomain(cidadeInput);
			return this.cidadeConverter.toModel(this.cadastroCidadeService.salvar(cidade));
		} catch (EstadoNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@ApiOperation("Busca uma cidade por id")
	@ApiResponses({
		@ApiResponse(code = 400, message = "ID da cidade inválido", response = Problem.class),
		@ApiResponse(code = 404, message = "Cidade não encontrada!", response = Problem.class)
	})
	@GetMapping("/{cidadeId}")
	public CidadeModel buscar(@ApiParam(value = "ID de uma cidade", example = "1") @PathVariable Long cidadeId) {
		Cidade cidade = this.cadastroCidadeService.buscarOuFalhar(cidadeId);
		return this.cidadeConverter.toModel(cidade);
	}

	@ApiOperation("Atualiza a cidade")
	@PutMapping("/{cidadeId}")
	public CidadeModel atualizar(@ApiParam(value = "ID de uma cidade", example = "1") @PathVariable Long cidadeId, 
			@ApiParam(name = "corpo", value = "Representação de uma cidade com os novos dados")
			@RequestBody @Valid CidadeInput cidadeInput) {

		try {
			Cidade cidadeAtual = this.cadastroCidadeService.buscarOuFalhar(cidadeId);
			this.cidadeConverter.copyToDomainObject(cidadeInput, cidadeAtual);
			return this.cidadeConverter.toModel(this.cadastroCidadeService.salvar(cidadeAtual));
		} catch (EstadoNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@ApiOperation("Exclui uma cidade por id")
	@ApiResponses({
		@ApiResponse(code = 204, message = "Cidade excluída"),
		@ApiResponse(code = 404, message = "Cidade não encontrada!", response = Problem.class)
	})
	@DeleteMapping("/{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@ApiParam(value = "ID de uma cidade", example = "1") @PathVariable Long cidadeId) {
		this.cadastroCidadeService.excluir(cidadeId);
	}

}
