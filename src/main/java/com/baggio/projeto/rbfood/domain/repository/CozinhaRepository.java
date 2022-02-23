package com.baggio.projeto.rbfood.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.baggio.projeto.rbfood.domain.model.Cozinha;

@Repository
public interface CozinhaRepository extends JpaRepository<Cozinha, Long>{

	//List<Cozinha> findByNomeCotainingIgnoreCase(String nome);
	
}
