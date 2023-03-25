package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.algaworks.algafood.core.validation.TaxaFrete;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurante {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String nome;
	@TaxaFrete //9.15 ESR
	@Column(name = "taxa_frete", nullable = false)
	private BigDecimal taxaFrete;
	@ManyToOne//(fetch = FetchType.LAZY)
	@JoinColumn(name = "cozinha_id", nullable = false)
	private Cozinha cozinha;
	@Column(name="ativo")
	private Boolean ativo = Boolean.TRUE;
	@Column(name="aberto")
	private Boolean aberto = Boolean.FALSE;
	@Embedded
	private Endereco endereco;
	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dataCadastro;
	@UpdateTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dataAtualizacao;
	@ManyToMany
	@JoinTable(name="restaurante_forma_pagamento", joinColumns = @JoinColumn(name="restaurante_id"), 
				inverseJoinColumns = @JoinColumn(name="forma_pagamento_id"))
	private Set<FormaPagamento> formasPagamento = new HashSet<>();
	@OneToMany(mappedBy = "restaurante")
	private List<Produto> produtos = new ArrayList<>();
	@ManyToMany
	@JoinTable(name="restaurante_usuario_responsavel", joinColumns = @JoinColumn(name="restaurante_id"),
			inverseJoinColumns = @JoinColumn(name="usuario_id"))
	private Set<Usuario> responsaveis = new HashSet<>();
	
	public void abrir() {
		setAberto(Boolean.TRUE);
	}
	
	public void fechar() {
		setAberto(Boolean.FALSE);
	}
	
	public boolean aceitaFormaPagamento(FormaPagamento formaPagamento) {
		return getFormasPagamento().contains(formaPagamento);
	}
	
	public boolean naoAceitaFormaPagamento(FormaPagamento formaPagamento) {
		return !aceitaFormaPagamento(formaPagamento);
	}
	
	public boolean removerFormaPagamento(FormaPagamento formaPagamento) {
		return getFormasPagamento().remove(formaPagamento);
	}
	
	public boolean adicionarFormaPagamento(FormaPagamento formaPagamento) {
		return getFormasPagamento().add(formaPagamento);
	}
	
	public boolean adicionarResponsavel(Usuario usuario) {
		return getResponsaveis().add(usuario);
	}
	
	public boolean removerResponsavel(Usuario usuario) {
		return getResponsaveis().remove(usuario);
	}
	
	public void ativar() {
		setAtivo(true);
	}
	public void inativar() {
		setAtivo(false);
	}
	
	public boolean isAberto() {
	    return this.aberto;
	}

	public boolean isFechado() {
	    return !isAberto();
	}

	public boolean isInativo() {
	    return !isAtivo();
	}

	public boolean isAtivo() {
	    return this.ativo;
	}

	public boolean aberturaPermitida() {
	    return isAtivo() && isFechado();
	}

	public boolean ativacaoPermitida() {
	    return isInativo();
	}
	
	public boolean inativacaoPermitida() {
	    return isAtivo();
	}

	public boolean fechamentoPermitido() {
	    return isAberto();
	} 
}
