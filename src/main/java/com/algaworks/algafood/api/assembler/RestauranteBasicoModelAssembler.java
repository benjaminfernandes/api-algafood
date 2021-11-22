package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.Algalinks;
import com.algaworks.algafood.api.controller.RestauranteController;
import com.algaworks.algafood.api.model.RestauranteBasicoModel;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteBasicoModelAssembler extends RepresentationModelAssemblerSupport<Restaurante, RestauranteBasicoModel> {

	public RestauranteBasicoModelAssembler() {
        super(RestauranteController.class, RestauranteBasicoModel.class);
    }

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private Algalinks algaLinks;
	 
	   @Override
	   public RestauranteBasicoModel toModel(Restaurante restaurante) {
	        RestauranteBasicoModel restauranteModel = createModelWithId(
	                restaurante.getId(), restaurante);
	        
	        modelMapper.map(restaurante, restauranteModel);
	        
	        restauranteModel.add(algaLinks.linkToRestaurantes("restaurantes"));
	        
	        restauranteModel.getCozinha().add(
	                algaLinks.linkToCozinha(restaurante.getCozinha().getId()));
	        
	        return restauranteModel;
	    }
	    
	    @Override
	    public CollectionModel<RestauranteBasicoModel> toCollectionModel(Iterable<? extends Restaurante> entities) {
	        return super.toCollectionModel(entities)
	                .add(algaLinks.linkToRestaurantes());
	    }
	 
}
