package com.algaworks.algafood.infraestructure.repository.spec;

import java.util.ArrayList;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.algaworks.algafood.domain.filter.PedidoFilter;
import com.algaworks.algafood.domain.model.Pedido;
//Aula 16.3.
public class PedidoSpecs {

	public static Specification<Pedido> usandoFiltro(PedidoFilter filtro){
		return (root, query, builder) -> {
			if(Pedido.class.equals(query.getResultType())) {
				root.fetch("restaurante").fetch("cozinha");
				root.fetch("cliente");
				root.fetch("formaPagamento");
			}
			var predicates = new ArrayList<Predicate>();
			
			if (filtro.getClienteId() != null) {
				predicates.add(builder.equal(root.get("cliente").get("id"), filtro.getClienteId()));
			}
			if (filtro.getRestauranteId() != null) {
				predicates.add(builder.equal(root.get("restaurante").get("id"), filtro.getRestauranteId()));
			}
			if (filtro.getDataCriacaoInicio() != null) {
				predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoInicio()));
			}
			if (filtro.getDataCriacaoFim() != null) {
				predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoFim()));
			}
			
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}
	
	public static Specification<Pedido> porProduto(Long produtoId) {
		return (root, query, criteriaBuilder) -> criteriaBuilder
				.literal(produtoId)
				.in(root.join("itens").get("produto").get("id"));
	}

}
