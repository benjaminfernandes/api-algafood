package com.algaworks.algafood.api.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoModel {

	@ApiModelProperty(example = "f9981ca4-5a5e-4da3-af04-933861df3e55")
	private String codigo;
	@ApiModelProperty(example = "298.90")
	private BigDecimal subTotal;
	@ApiModelProperty(example = "10.00")
	private BigDecimal taxaFrete;
	@ApiModelProperty(example = "308.90")
	private BigDecimal valorTotal;
	@ApiModelProperty(example = "2019-12-01T20:34:04Z")
	private OffsetDateTime dataCriacao;
	@ApiModelProperty(example = "2019-12-01T20:35:10Z")
	private OffsetDateTime dataConfirmacao;
	@ApiModelProperty(example = "2019-12-01T20:35:00Z")
    private OffsetDateTime dataCancelamento;
	@ApiModelProperty(example = "2019-12-01T20:55:30Z")
    private OffsetDateTime dataEntrega;
    @ApiModelProperty(example = "CRIADO")
    private String status;
	private EnderecoModel endereco;
    private FormaPagamentoModel formaPagamento;
    private RestauranteResumoModel restaurante;
    private UsuarioModel cliente;
    private List<ItemPedidoModel> itens = new ArrayList<>();
}
