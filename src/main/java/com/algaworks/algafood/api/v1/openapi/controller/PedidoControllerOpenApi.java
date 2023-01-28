package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import com.algaworks.algafood.api.v1.model.PedidoModel;
import com.algaworks.algafood.api.v1.model.PedidoResumoModel;
import com.algaworks.algafood.api.v1.model.input.Pedidoinput;
import com.algaworks.algafood.domain.filter.PedidoFilter;

public interface PedidoControllerOpenApi {

    PagedModel<PedidoResumoModel> pesquisar(PedidoFilter filtro, Pageable pageable);
    
    PedidoModel salvar(Pedidoinput pedidoInput);
    
    PedidoModel buscar(String codigoPedido);   
}