package com.algaworks.algafood.api.v1.assembler;

import java.util.Collection;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.Algalinks;
import com.algaworks.algafood.api.v1.controller.RestauranteProdutoFotoController;
import com.algaworks.algafood.api.v1.model.FotoProdutoModel;
import com.algaworks.algafood.api.v1.model.input.FotoProdutoInput;
import com.algaworks.algafood.domain.model.FotoProduto;

@Component
public class FotoProdutoModelAssembler extends RepresentationModelAssemblerSupport<FotoProduto, FotoProdutoModel> implements Converter<FotoProduto, FotoProdutoModel, FotoProdutoInput> {

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
    private Algalinks algaLinks;
	
	public FotoProdutoModelAssembler() {
        super(RestauranteProdutoFotoController.class, FotoProdutoModel.class);
    }
	
	@Override
	public FotoProduto toDomain(FotoProdutoInput inputModel) {
		return modelMapper.map(modelMapper, FotoProduto.class);
	}

	@Override
	public FotoProdutoModel toModel(FotoProduto domain) {
		 FotoProdutoModel fotoProdutoModel = modelMapper.map(domain, FotoProdutoModel.class);
	        
	        fotoProdutoModel.add(algaLinks.linkToFotoProduto(
	                domain.getRestauranteId(), domain.getProduto().getId()));
	        
	        fotoProdutoModel.add(algaLinks.linkToProduto(
	                domain.getRestauranteId(), domain.getProduto().getId(), "produto"));
	        
	        return fotoProdutoModel;
	}

	@Override
	public void copyToDomainObject(FotoProdutoInput inputModel, FotoProduto model) {
	}

	@Override
	public List<FotoProdutoModel> paraModeloColecao(Collection<FotoProduto> list) {
		return null;
	}
}
