package com.algaworks.algafood.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Pedido;

@Repository
public interface PedidoRepository extends CustomJpaRepository<Pedido, Long>, 
	JpaSpecificationExecutor<Pedido>{

	@Query("from Pedido p join fetch p.cliente join fetch p.restaurante r join fetch r.cozinha join fetch p.formaPagamento")
	List<Pedido> findAll();
	
	@Query("from Pedido p join fetch p.itens it join fetch it.produto prod join fetch p.cliente join fetch p.restaurante r join fetch r.cozinha join fetch p.formaPagamento where p.codigo = :codigo")
	Optional<Pedido> findByCodigo(String codigo);
	
}
