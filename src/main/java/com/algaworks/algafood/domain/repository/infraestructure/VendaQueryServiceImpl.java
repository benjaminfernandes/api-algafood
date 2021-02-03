package com.algaworks.algafood.domain.repository.infraestructure;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.StatusPedido;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;
import com.algaworks.algafood.domain.service.VendaQueryService;

@Repository
public class VendaQueryServiceImpl implements VendaQueryService {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro) {
		var builder = manager.getCriteriaBuilder();
		var query = builder.createQuery(VendaDiaria.class);//tipo do retorno
		var root = query.from(Pedido.class);

		var functionDateDataCriacao = builder.function("date", Date.class, 
				root.get("dataCriacao"));
		
		var selection = builder.construct(VendaDiaria.class, 
				functionDateDataCriacao, 
				builder.count(root.get("id")),
				builder.sum(root.get("valorTotal")));
		
		List<Predicate> predicates = new ArrayList<>();
		
		filtrarDados(filtro, builder, root, predicates);
		
		query.where(predicates.toArray(new Predicate[0]));
		query.select(selection);
		query.groupBy(functionDateDataCriacao);
		
		return manager.createQuery(query).getResultList();
	}

	private void filtrarDados(VendaDiariaFilter filtro, CriteriaBuilder builder, Root<Pedido> root,
			List<Predicate> predicates) {
		if(filtro.getDataCriacaoInicio() != null) {
			predicates.add(builder.greaterThan(root.get("dataCriacao"), 
					filtro.getDataCriacaoInicio()));
		}
		
		if(filtro.getDataCriacaoFim() != null) {
			predicates.add(builder.lessThan(root.get("dataCriacao"), 
					filtro.getDataCriacaoFim()));
		}
		
		if(filtro.getRestauranteId() != null) {
			predicates.add(builder.equal(root.get("restaurante"), 
					filtro.getRestauranteId()));
		}
		
		predicates.add(root.get("status").in(StatusPedido.CONFIRMADO, 
				StatusPedido.ENTREGUE));
	}
}
