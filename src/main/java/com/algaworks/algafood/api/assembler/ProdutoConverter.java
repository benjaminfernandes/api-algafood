package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.Algalinks;
import com.algaworks.algafood.api.controller.RestauranteProdutoController;
import com.algaworks.algafood.api.model.ProdutoModel;
import com.algaworks.algafood.api.model.input.ProdutoInput;
import com.algaworks.algafood.domain.model.Produto;

@Component
public class ProdutoConverter extends RepresentationModelAssemblerSupport<Produto, ProdutoModel> implements Converter<Produto, ProdutoModel, ProdutoInput> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private Algalinks algaLinks;
	
	public ProdutoConverter() {
        super(RestauranteProdutoController.class, ProdutoModel.class);
    }
	
	@Override
	public Produto toDomain(ProdutoInput inputModel) {
		return this.modelMapper.map(inputModel, Produto.class);
	}

	@Override
	public ProdutoModel toModel(Produto domain) {
		ProdutoModel produtoModel = createModelWithId(
                domain.getId(), domain, domain.getRestaurante().getId());
        
        modelMapper.map(domain, produtoModel);
        
        produtoModel.add(algaLinks.linkToProdutos(domain.getRestaurante().getId(), "produtos"));
        
        produtoModel.add(algaLinks.linkToFotoProduto(
                domain.getRestaurante().getId(), domain.getId(), "foto"));
      
        return produtoModel;
	}

	@Override
	public void copyToDomainObject(ProdutoInput inputModel, Produto model) {
		this.modelMapper.map(inputModel, model);
	}

	@Override
	public List<ProdutoModel> paraModeloColecao(Collection<Produto> list) {
		return list.stream().map(produto -> toModel(produto)).collect(Collectors.toList());
	}

}
