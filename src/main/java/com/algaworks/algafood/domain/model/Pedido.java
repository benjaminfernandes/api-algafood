package com.algaworks.algafood.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Pedido implements Serializable {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private BigDecimal subtotal;
	@Column(name="taxa_frete")
	private BigDecimal taxaFrete;
	@Column(name="valor_total")
	private BigDecimal valorTotal;
	@Embedded
	private Endereco endereco;
	@CreationTimestamp
	@Column(name="data_criacao")
	private OffsetDateTime dataCriacao;
	@Column(name="data_confirmacao")
	private OffsetDateTime dataConfirmacao;
	@Column(name="data_cancelamento")
    private OffsetDateTime dataCancelamento;
	@Column(name="data_entrega")
    private OffsetDateTime dataEntrega;
    
    @Enumerated(EnumType.STRING)
    private StatusPedido status = StatusPedido.CRIADO;
    @ManyToOne
    @JoinColumn(nullable = false, name = "forma_pagamento_id")
    private FormaPagamento formaPagamento;
    @ManyToOne
    @JoinColumn(nullable = false, name = "restaurante_id")
    private Restaurante restaurante;
    @ManyToOne
    @JoinColumn(name = "usuario_cliente_id", nullable = false)
    private Usuario cliente;
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens = new ArrayList<>();
    
    public void calculaValorTotal() {
    	getItens().forEach(ItemPedido::calcularPrecoTotal);
    	
    	this.subtotal = getItens().stream().map(item -> item.getPrecoTotal()).reduce(BigDecimal.ZERO, BigDecimal::add);
    	
    	this.valorTotal = this.subtotal.add(this.taxaFrete);
    }
    
}
