package com.algaworks.algafood.api.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.algaworks.algafood.domain.model.FormaPagamento;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoModel {

	private String codigo;
	private BigDecimal subTotal;
	private BigDecimal taxaFrete;
	private BigDecimal valorTotal;
	private EnderecoModel endereco;
	private OffsetDateTime dataCriacao;
	private OffsetDateTime dataConfirmacao;
    private OffsetDateTime dataCancelamento;
    private OffsetDateTime dataEntrega;
    private String status;
    private FormaPagamento formaPagamento;
    private RestauranteResumoModel restaurante;
    private UsuarioModel cliente;
    private List<ItemPedidoModel> itens = new ArrayList<>();
}
