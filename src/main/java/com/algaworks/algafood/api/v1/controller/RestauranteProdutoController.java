package com.algaworks.algafood.api.v1.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.Algalinks;
import com.algaworks.algafood.api.v1.assembler.ProdutoConverter;
import com.algaworks.algafood.api.v1.model.ProdutoModel;
import com.algaworks.algafood.api.v1.model.input.ProdutoInput;
import com.algaworks.algafood.api.v1.openapi.controller.RestauranteProdutoControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import com.algaworks.algafood.domain.service.CadastroProdutoService;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping("/v1/restaurantes/{restauranteId}/produtos")
public class RestauranteProdutoController implements RestauranteProdutoControllerOpenApi {

	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private CadastroProdutoService produtoService;
	@Autowired
	private ProdutoConverter produtoConverter;
	@Autowired
	private CadastroRestauranteService restauranteService;
	@Autowired
	private Algalinks algaLinks;
	
	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping
	public CollectionModel<ProdutoModel> listar(@PathVariable Long restauranteId, @RequestParam(required = false) Boolean incluirInativos){
		
		Restaurante restaurante = this.restauranteService.buscarOuFalhar(restauranteId);
		
		List<Produto> todosProdutos = null;
		if(incluirInativos != null && incluirInativos) {
			todosProdutos = produtoRepository.findByRestaurante(restaurante);
		}else {
			todosProdutos = this.produtoRepository.findAtivosByRestaurante(restaurante);
		}
		
		return this.produtoConverter.toCollectionModel(todosProdutos)
				.add(algaLinks.linkToProdutos(restauranteId));
	}
	
	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping("/{produtoId}")
	public ProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		
		Produto produto = this.produtoService.buscarOuFalhar(restauranteId, produtoId);
		return this.produtoConverter.toModel(produto);
	}
	
	@CheckSecurity.Restaurantes.PodeEditar
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProdutoModel adicionar(@PathVariable Long restauranteId, @RequestBody @Valid ProdutoInput produtoInput) {
		Restaurante restaurante = this.restauranteService.buscarOuFalhar(restauranteId);
		Produto produto = this.produtoConverter.toDomain(produtoInput);
		produto.setRestaurante(restaurante);
		produto = this.produtoService.salvar(produto);
		return this.produtoConverter.toModel(produto);
	}
	
	@CheckSecurity.Restaurantes.PodeEditar
	@PutMapping("/{produtoId}")
	public ProdutoModel atualizar(@PathVariable Long restauranteId ,@PathVariable Long produtoId,@RequestBody @Valid ProdutoInput produtoInput) {
		Produto produto = this.produtoService.buscarOuFalhar(produtoId);
		this.produtoConverter.copyToDomainObject(produtoInput, produto);
		produto = this.produtoService.salvar(produto);
		return this.produtoConverter.toModel(produto);
	}
	
	@CheckSecurity.Restaurantes.PodeEditar
	@DeleteMapping("produtoId")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long produtoId) {
		
		this.produtoService.excluir(produtoId);
	}
}
