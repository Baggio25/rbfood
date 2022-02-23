package com.baggio.projeto.rbfood.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.baggio.projeto.rbfood.domain.model.Restaurante;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

	//@Query("from Restaurante r join fetch r.cozinha left join fetch r.formasPagamento")
	@Query("from Restaurante r join fetch r.cozinha")
	List<Restaurante> findAll();	
	
	// ****************************//
	// *** Feito como teste **//
	List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);

	@Query("from Restaurante "
			+ " where nome like %:nome% "
			+ " and cozinha.id = :id ")
	List<Restaurante> consultarPorNomeECozinha(String nome, @Param("id") Long cozinhaId);

	Optional<Restaurante> findFirstByNomeContaining(String nome);

	List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);
	
	List<Restaurante> findCriteria(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);
	
	// ****************************//
	// ****************************//
}
