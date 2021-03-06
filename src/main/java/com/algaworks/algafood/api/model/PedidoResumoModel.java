package com.algaworks.algafood.api.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;

//@JsonFilter("pedidoFilter")
@Getter
@Setter
public class PedidoResumoModel {

	private String codigo;
	private BigDecimal subTotal;
	private BigDecimal taxaFrete;
	private BigDecimal valorTotal;
	private OffsetDateTime dataCriacao;
    private String status;
    private RestauranteResumoModel restaurante;
    private UsuarioModel cliente;
    //private String nomeCliente;
}
