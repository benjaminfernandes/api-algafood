package com.algaworks.algafood.api.v2.controller;

import java.util.List;

import javax.validation.Valid;

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

import com.algaworks.algafood.api.ResourceUriHelper;
import com.algaworks.algafood.api.v2.assembler.CidadeConverterV2;
import com.algaworks.algafood.api.v2.model.CidadeModelV2;
import com.algaworks.algafood.api.v2.model.input.CidadeInputV2;
import com.algaworks.algafood.api.v2.openapi.controller.CidadeControllerV2OpenApi;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroService;

import lombok.extern.slf4j.Slf4j;

//Ajustes a serem feitos aula 20.11
@Slf4j
@RestController
@RequestMapping(path = "/v2/cidades", produces = MediaType.APPLICATION_JSON_VALUE)
public class CidadeControllerV2 implements CidadeControllerV2OpenApi{

	//private static final Logger logger = LoggerFactory.getLogger(CidadeControllerV2.class);
	
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private CadastroService<Cidade> cadastroCidadeService;
	@Autowired
	private CidadeConverterV2 cidadeConverter;
	
	@GetMapping
	public CollectionModel<CidadeModelV2> listar() {
		//logger.info("Listando cidades como teste {}...", "inserido por parâmetro");
		log.info("Listando cidades como teste {}...", "inserido por parâmetro");//aqui usando o lombok
		List<Cidade> todasCidades = this.cidadeRepository.findAll();
		//cidadesCollectionModel.add(linkTo(CidadeController.class).withSelfRel());
		
		return this.cidadeConverter.toCollectionModel(todasCidades);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeModelV2 salvar(@RequestBody @Valid CidadeInputV2 cidadeInput) {
		try {
			Cidade cidade = this.cidadeConverter.toDomain(cidadeInput);
			cidade = this.cadastroCidadeService.salvar(cidade);
			
			CidadeModelV2 cidadeModel =  this.cidadeConverter.toModel(cidade);
			//Aula 19.2
			ResourceUriHelper.addUriInResponseHeader(cidadeModel.getIdCidade());
			
			return cidadeModel;
		} catch (EstadoNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@GetMapping("/{cidadeId}")
	public CidadeModelV2 buscar(@PathVariable Long cidadeId) {
		Cidade cidade = this.cadastroCidadeService.buscarOuFalhar(cidadeId);
		
		CidadeModelV2 cidadeModel = this.cidadeConverter.toModel(cidade);
		
		//Link link = linkTo(methodOn(CidadeController.class).buscar(cidadeModel.getId())).withSelfRel();
		
		//cidadeModel.add(link);
		
		//Aula 19.8 
		//cidadeModel.add(linkTo(CidadeController.class)
				//.slash(cidadeModel.getId()).withSelfRel());
		
		//cidadeModel.add(new Link("http://localhost:8080/cidades/1", IanaLinkRelations.SELF));
		//cidadeModel.add(new Link("http://localhost:8080/cidades/1"));
		
		//cidadeModel.add(new Link("http://localhost:8080/cidades", 
				 //IanaLinkRelations.COLLECTION));
		
		//cidadeModel.add(new Link("http://localhost:8080/cidades", "cidades"));
		//cidadeModel.add(linkTo(CidadeController.class)
				//.withRel("cidades"));
		
		//cidadeModel.add(linkTo(methodOn(CidadeController.class).listar()).withRel("cidades"));
		
		//cidadeModel.getEstado().add(new Link("http://localhost:8080/estados/1"));
		
		//cidadeModel.getEstado().add(linkTo(EstadoController.class)
				//.slash(cidadeModel.getEstado().getId()).withSelfRel());
		
		//cidadeModel.getEstado().add(linkTo(methodOn(EstadoController.class)
				//.buscar(cidadeModel.getEstado().getId())).withSelfRel());
		
		return cidadeModel;
	}

	@PutMapping("/{cidadeId}")
	public CidadeModelV2 atualizar(@PathVariable Long cidadeId, 
					@RequestBody @Valid CidadeInputV2 cidadeInput) {

		try {
			Cidade cidadeAtual = this.cadastroCidadeService.buscarOuFalhar(cidadeId);
			this.cidadeConverter.copyToDomainObject(cidadeInput, cidadeAtual);
			return this.cidadeConverter.toModel(this.cadastroCidadeService.salvar(cidadeAtual));
		} catch (EstadoNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@DeleteMapping("/{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long cidadeId) {
		this.cadastroCidadeService.excluir(cidadeId);
	}

}
