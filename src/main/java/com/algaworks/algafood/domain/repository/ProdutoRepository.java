package com.algaworks.algafood.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;

@Repository
public interface ProdutoRepository extends CustomJpaRepository<Produto, Long> {

	public List<Produto> findByRestaurante(Restaurante restaurante);
	
	@Query("from Produto where restaurante.id = :restaurante and id = :produto")
	public Optional<Produto> findById(@Param("restaurante") Long restauranteId, @Param("produto") Long produtoId);
}