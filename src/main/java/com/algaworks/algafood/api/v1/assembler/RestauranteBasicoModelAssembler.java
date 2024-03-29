package com.algaworks.algafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.Algalinks;
import com.algaworks.algafood.api.v1.controller.RestauranteController;
import com.algaworks.algafood.api.v1.model.RestauranteBasicoModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
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
	@Autowired
	private AlgaSecurity algaSecurity;
	 
	   @Override
	   public RestauranteBasicoModel toModel(Restaurante restaurante) {
	        RestauranteBasicoModel restauranteModel = createModelWithId(
	                restaurante.getId(), restaurante);
	        
	        modelMapper.map(restaurante, restauranteModel);
	        
	        if (algaSecurity.podeConsultarRestaurantes()) {
	        	restauranteModel.add(algaLinks.linkToRestaurantes("restaurantes"));
	        }
	        
	        if (algaSecurity.podeConsultarCozinhas()) {
	        	restauranteModel.getCozinha().add(
		                algaLinks.linkToCozinha(restaurante.getCozinha().getId()));
	        }
	        
	        return restauranteModel;
	    }
	    
	    @Override
	    public CollectionModel<RestauranteBasicoModel> toCollectionModel(Iterable<? extends Restaurante> entities) {
	    	CollectionModel<RestauranteBasicoModel> collectionModel = super.toCollectionModel(entities);
	    	
	    	if(algaSecurity.podeConsultarRestaurantes()) {
	    		collectionModel.add(algaLinks.linkToRestaurantes());
	    	}
	    	
	    	return collectionModel;
	    }
	 
}
