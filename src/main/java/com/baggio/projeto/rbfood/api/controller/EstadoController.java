package com.baggio.projeto.rbfood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baggio.projeto.rbfood.domain.model.Estado;
import com.baggio.projeto.rbfood.domain.repository.EstadoRepository;

@RestController
@RequestMapping(value = "/estados")
public class EstadoController {

	@Autowired
	private EstadoRepository estadoRepository;
	
	@GetMapping
	public List<Estado> listar() {
		return estadoRepository.findAll();
	}
	
	@GetMapping(value = "/{id}")
	public Estado buscar(@PathVariable Long id) {
		return estadoRepository.findById(id).get();
	}
	
}
