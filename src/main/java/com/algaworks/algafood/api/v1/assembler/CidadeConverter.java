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
import com.algaworks.algafood.api.v1.controller.CidadeController;
import com.algaworks.algafood.api.v1.model.CidadeModel;
import com.algaworks.algafood.api.v1.model.input.CidadeInput;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;

@Component
public class CidadeConverter extends RepresentationModelAssemblerSupport<Cidade, CidadeModel> 
	implements Converter<Cidade, CidadeModel, CidadeInput> {

	public CidadeConverter() {
		super(CidadeController.class, CidadeModel.class);
	}

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private Algalinks algaLinks;
	@Autowired
	private AlgaSecurity algaSecurity;  

	@Override
	public Cidade toDomain(CidadeInput inputModel) {
		return this.modelMapper.map(inputModel, Cidade.class);
	}

	@Override
	public CidadeModel toModel(Cidade domain) {
		CidadeModel cidadeModel = createModelWithId(domain.getId(), domain);
		this.modelMapper.map(domain, cidadeModel);
		
		//CidadeModel cidadeModel = this.modelMapper.map(domain, CidadeModel.class);
		
		//Adicionado na aula 19.11
		//Link link = linkTo(methodOn(CidadeController.class).buscar(cidadeModel.getId())).withSelfRel();
		//cidadeModel.add(link);		
	
		//cidadeModel.add(linkTo(methodOn(CidadeController.class).listar()).withRel("cidades"));
		
		//cidadeModel.getEstado().add(linkTo(methodOn(EstadoController.class)
				//.buscar(cidadeModel.getEstado().getId())).withSelfRel());
		if (algaSecurity.podeConsultarCidades()) {
			cidadeModel.add(algaLinks.linkToCidades("cidades"));
		}
		
		if (algaSecurity.podeConsultarEstados()) {
			cidadeModel.getEstado().add(algaLinks.linkToEstado(cidadeModel.getEstado().getId()));
		}
		
		return cidadeModel;
	}

	@Override
	public List<CidadeModel> paraModeloColecao(Collection<Cidade> list) {
		return list.stream().map(cidade -> toModel(cidade)).collect(Collectors.toList());
	}

	@Override
	public void copyToDomainObject(CidadeInput cidadeInput, Cidade cidade) {
		cidade.setEstado(new Estado());
		this.modelMapper.map(cidadeInput, cidade);
	}
	
	@Override
	public CollectionModel<CidadeModel> toCollectionModel(Iterable<? extends Cidade> entities) {
		CollectionModel<CidadeModel> collectionModel = super.toCollectionModel(entities);
	    
	    if (algaSecurity.podeConsultarCidades()) {
	        collectionModel.add(algaLinks.linkToCidades());
	    }
	    
	    return collectionModel;
	}

}
