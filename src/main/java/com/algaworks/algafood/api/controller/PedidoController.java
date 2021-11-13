package com.algaworks.algafood.api.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.PedidoConverter;
import com.algaworks.algafood.api.assembler.PedidoResumoConverter;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.api.model.input.Pedidoinput;
import com.algaworks.algafood.api.openapi.controller.PedidoControllerOpenApi;
import com.algaworks.algafood.core.data.PageableTranslator;
import com.algaworks.algafood.domain.filter.PedidoFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.repository.infraestructure.spec.PedidoSpecs;
import com.algaworks.algafood.domain.service.CadastroPedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController implements PedidoControllerOpenApi{

	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private CadastroPedidoService pedidoService;
	@Autowired
	private PedidoConverter pedidoConverter;
	@Autowired
	private PedidoResumoConverter pedidoResumoConverter;
	@Autowired
	private PagedResourcesAssembler<Pedido> pagedResourcesAssembler;
	
	@GetMapping
	public PagedModel<PedidoResumoModel> pesquisar(PedidoFilter pedidoFilter,
			@PageableDefault(size=10) Pageable pageable){
		
		traduzirPageable(pageable);
		
		Page<Pedido> pedidosPage = this.pedidoRepository
				.findAll(PedidoSpecs.usandoFiltro(pedidoFilter), pageable);
		
		/*List<PedidoResumoModel> pedidosModel = this.pedidoResumoConverter
				.toCollectionModel(pedidosPage.getContent());
		
		Page<PedidoResumoModel> pedidoResumoModelPage = new 
				PageImpl<PedidoResumoModel>(pedidosModel, pageable, 
						pedidosPage.getTotalElements());
		*/
		PagedModel<PedidoResumoModel> pagedPedidoResumoModel = this.pagedResourcesAssembler
				.toModel(pedidosPage, pedidoResumoConverter);
		
		return pagedPedidoResumoModel;
	}
	
	@GetMapping("/{codigoPedido}")
	public PedidoModel buscar(@PathVariable String codigoPedido) {
		Pedido pedido = this.pedidoService.buscarOuFalhar(codigoPedido);
		return this.pedidoConverter.toModel(pedido);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PedidoModel salvar(@Valid @RequestBody Pedidoinput pedidoInput) {
		Pedido pedido = this.pedidoConverter.toDomain(pedidoInput);
		// TODO pegar usuário autenticado
        pedido.setCliente(new Usuario());
        pedido.getCliente().setId(1L);
	
        pedido = this.pedidoService.salvar(pedido);
        
        return this.pedidoConverter.toModel(pedido);
	}
	//Aula 13.11
	private Pageable traduzirPageable(Pageable apiPageable) {
		var mapeamento = Map.of(
				"codigo", "codigo",
				"subTotal", "subTotal",
				"restaurante.nome", "restaurante.nome",
				"nomeCliente", "cliente.nome",
				"valorTotal", "valorTotal"
			);
		
		return PageableTranslator.translate(apiPageable, mapeamento);
	}

}
