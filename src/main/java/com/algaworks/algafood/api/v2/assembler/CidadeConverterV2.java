package com.algaworks.algafood.api.v2.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.converter.Converter;
import com.algaworks.algafood.api.v2.AlgalinksV2;
import com.algaworks.algafood.api.v2.controller.CidadeControllerV2;
import com.algaworks.algafood.api.v2.model.CidadeModelV2;
import com.algaworks.algafood.api.v2.model.input.CidadeInputV2;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;

@Component
public class CidadeConverterV2 extends RepresentationModelAssemblerSupport<Cidade, CidadeModelV2> 
	implements Converter<Cidade, CidadeModelV2, CidadeInputV2> {

	public CidadeConverterV2() {
		super(CidadeControllerV2.class, CidadeModelV2.class);
	}

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private AlgalinksV2 algaLinks;

	@Override
	public Cidade toDomain(CidadeInputV2 inputModel) {
		return this.modelMapper.map(inputModel, Cidade.class);
	}

	@Override
	public CidadeModelV2 toModel(Cidade domain) {
		CidadeModelV2 cidadeModel = createModelWithId(domain.getId(), domain);
		this.modelMapper.map(domain, cidadeModel);
		
		//CidadeModel cidadeModel = this.modelMapper.map(domain, CidadeModel.class);
		
		//Adicionado na aula 19.11
		//Link link = linkTo(methodOn(CidadeController.class).buscar(cidadeModel.getId())).withSelfRel();
		//cidadeModel.add(link);		
	
		//cidadeModel.add(linkTo(methodOn(CidadeController.class).listar()).withRel("cidades"));
		
		//cidadeModel.getEstado().add(linkTo(methodOn(EstadoController.class)
				//.buscar(cidadeModel.getEstado().getId())).withSelfRel());
		
		cidadeModel.add(algaLinks.linkToCidades("cidades"));
		
		return cidadeModel;
	}

	@Override
	public List<CidadeModelV2> paraModeloColecao(Collection<Cidade> list) {
		return list.stream().map(cidade -> toModel(cidade)).collect(Collectors.toList());
	}

	@Override
	public void copyToDomainObject(CidadeInputV2 cidadeInput, Cidade cidade) {
		cidade.setEstado(new Estado());
		this.modelMapper.map(cidadeInput, cidade);
	}
	
	@Override
	public CollectionModel<CidadeModelV2> toCollectionModel(Iterable<? extends Cidade> entities) {
		return super.toCollectionModel(entities)
	            .add(algaLinks.linkToCidades());
	}

}
