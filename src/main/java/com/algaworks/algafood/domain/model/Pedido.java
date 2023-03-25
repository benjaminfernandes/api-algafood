package com.algaworks.algafood.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.AbstractAggregateRoot;

import com.algaworks.algafood.domain.event.PedidoCanceladoEvent;
import com.algaworks.algafood.domain.event.PedidoConfirmadoEvent;
import com.algaworks.algafood.domain.event.PedidoSaiuEntregaEvent;
import com.algaworks.algafood.domain.exception.NegocioException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
public class Pedido extends AbstractAggregateRoot<Pedido> implements Serializable {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String codigo;
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
    
    public void confirmar() {
    	setStatus(StatusPedido.CONFIRMADO);
    	setDataConfirmacao(OffsetDateTime.now());
    	registerEvent(new PedidoConfirmadoEvent(this));
    }
    
    public void entregar() {
    	setStatus(StatusPedido.ENTREGUE);
    	setDataEntrega(OffsetDateTime.now());
    	
    	registerEvent(new PedidoSaiuEntregaEvent(this));
    }
    
    public void cancelar() {
    	setStatus(StatusPedido.CANCELADO);
    	setDataCancelamento(OffsetDateTime.now());
    	
    	registerEvent(new PedidoCanceladoEvent(this));
    }
    
    private void setStatus(StatusPedido novoStatus) {
    	if(getStatus().naoPodeAlterarPara(novoStatus)) {
    		throw new NegocioException(String.format("Status do pedido %s não pode ser alterado de %s para %s", 
    				getCodigo(), getStatus().getDescricao(), novoStatus.getDescricao()));
    	}
    	
    	this.status = novoStatus;
    }
    
    public boolean podeSerConfirmado() {
    	return getStatus().podeAlterarPara(StatusPedido.CONFIRMADO);
    }
    
    public boolean podeSerEntregue() {
    	return getStatus().podeAlterarPara(StatusPedido.ENTREGUE);
    }
    
    public boolean podeSerCancelado() {
    	return getStatus().podeAlterarPara(StatusPedido.CANCELADO);
    }
    
    @PrePersist
    private void gerarCodigo() {
    	setCodigo(UUID.randomUUID().toString());
    }
}
