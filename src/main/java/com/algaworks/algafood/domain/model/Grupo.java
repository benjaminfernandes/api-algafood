package com.algaworks.algafood.domain.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Grupo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	@Column(nullable = false)
	private String nome;
	@ManyToMany
	@JoinTable(name="grupo_permissao", joinColumns = @JoinColumn(name="grupo_id"), inverseJoinColumns = @JoinColumn(name="permissao_id"))
	private List<Permissao> permissoes = new ArrayList<Permissao>();
	
	public void adicionaPermissao(Permissao permissao) {
		permissoes.add(permissao);
	}
	
	public void retiraPermissao(Permissao permissao) {
		permissoes.remove(permissao);
	}
}
