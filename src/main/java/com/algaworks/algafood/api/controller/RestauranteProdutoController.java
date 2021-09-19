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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.ProdutoConverter;
import com.algaworks.algafood.api.model.ProdutoModel;
import com.algaworks.algafood.api.model.input.ProdutoInput;
import com.algaworks.algafood.api.openapi.controller.RestauranteProdutoControllerOpenApi;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import com.algaworks.algafood.domain.service.CadastroProdutoService;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos")
public class RestauranteProdutoController implements RestauranteProdutoControllerOpenApi {

	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private CadastroProdutoService produtoService;
	@Autowired
	private ProdutoConverter produtoConverter;
	@Autowired
	private CadastroRestauranteService restauranteService;
	
	@GetMapping
	public List<ProdutoModel> listar(@PathVariable Long restauranteId, @RequestParam(required = false) boolean incluirInativos){
		
		Restaurante restaurante = this.restauranteService.buscarOuFalhar(restauranteId);
		List<Produto> todosProdutos = null;
		if(incluirInativos) {
			todosProdutos = produtoRepository.findByRestaurante(restaurante);
		}else {
			todosProdutos = this.produtoRepository.findAtivosByRestaurante(restaurante);
		}
		
		return this.produtoConverter.toCollectionModel(todosProdutos);
	}
	
	@GetMapping("/{produtoId}")
	public ProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		
		Produto produto = this.produtoService.buscarOuFalhar(restauranteId, produtoId);
		return this.produtoConverter.toModel(produto);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProdutoModel adicionar(@PathVariable Long restauranteId, @RequestBody @Valid ProdutoInput produtoInput) {
		Restaurante restaurante = this.restauranteService.buscarOuFalhar(restauranteId);
		Produto produto = this.produtoConverter.toDomain(produtoInput);
		produto.setRestaurante(restaurante);
		produto = this.produtoService.salvar(produto);
		return this.produtoConverter.toModel(produto);
	}
	
	@PutMapping("/{produtoId}")
	public ProdutoModel atualizar(@PathVariable Long restauranteId ,@PathVariable Long produtoId,@RequestBody @Valid ProdutoInput produtoInput) {
		Produto produto = this.produtoService.buscarOuFalhar(produtoId);
		this.produtoConverter.copyToDomainObject(produtoInput, produto);
		produto = this.produtoService.salvar(produto);
		return this.produtoConverter.toModel(produto);
	}
	
	@DeleteMapping("produtoId")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long produtoId) {
		
		this.produtoService.excluir(produtoId);
	}
}
