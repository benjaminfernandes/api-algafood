package com.algaworks.algafood.api.v1.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.converter.Converter;
import com.algaworks.algafood.api.v1.Algalinks;
import com.algaworks.algafood.api.v1.controller.FormaPagamentoController;
import com.algaworks.algafood.api.v1.model.FormaPagamentoModel;
import com.algaworks.algafood.api.v1.model.input.FormaPagamentoInput;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.FormaPagamento;

@Component
public class FormaPagamentoConverter extends RepresentationModelAssemblerSupport<FormaPagamento, FormaPagamentoModel>
	implements Converter<FormaPagamento, FormaPagamentoModel, FormaPagamentoInput>{

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private AlgaSecurity algaSecurity;
	
	@Autowired
    private Algalinks algaLinks;
	
	 public FormaPagamentoConverter() {
	        super(FormaPagamentoController.class, FormaPagamentoModel.class);
	    }
	
	@Override
	public FormaPagamento toDomain(FormaPagamentoInput inputModel) {
		return modelMapper.map(inputModel, FormaPagamento.class);
	}

	@Override
	public FormaPagamentoModel toModel(FormaPagamento domain) {
		FormaPagamentoModel formaPagamentoModel = 
                createModelWithId(domain.getId(), domain);
        
        this.modelMapper.map(domain, formaPagamentoModel);
        if (algaSecurity.podeConsultarFormasPagamento()) {
        	formaPagamentoModel.add(algaLinks.linkToFormasPagamento("formasPagamento"));
        }
        
        return formaPagamentoModel;
	}

	@Override
	public List<FormaPagamentoModel> paraModeloColecao(Collection<FormaPagamento> list) {
		return list.stream().map(formaPagamento -> toModel(formaPagamento)).collect(Collectors.toList());
	}

	@Override
	public void copyToDomainObject(FormaPagamentoInput formaPagamentoInput, FormaPagamento formaPagamento) {
		this.modelMapper.map(formaPagamentoInput, formaPagamento);
	}
	
	@Override
    public CollectionModel<FormaPagamentoModel> toCollectionModel(Iterable<? extends FormaPagamento> entities) {
		CollectionModel<FormaPagamentoModel> collectionModel = super.toCollectionModel(entities);
	    
	    if (algaSecurity.podeConsultarFormasPagamento()) {
	        collectionModel.add(algaLinks.linkToFormasPagamento());
	    }
	        
	    return collectionModel;
    }   

}
