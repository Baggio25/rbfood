package com.baggio.projeto.rbfood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.baggio.projeto.rbfood.domain.model.Cozinha;
import com.baggio.projeto.rbfood.domain.service.CadastroCozinhaService;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

	@Autowired
	private CadastroCozinhaService cadastroCozinhaService;

	@GetMapping
	public List<Cozinha> listar() {
		return cadastroCozinhaService.listar();
	}

	@GetMapping(value = "/{id}")
	public Cozinha buscar(@PathVariable Long id) {
		return cadastroCozinhaService.buscar(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cozinha adicionar(@RequestBody @Valid Cozinha cozinha) {
		return cadastroCozinhaService.salvar(cozinha);
	}

	@PutMapping(value = "/{id}")
	public Cozinha atualizar(@RequestBody @Valid Cozinha cozinha, @PathVariable Long id) {
		Cozinha cozinhaSalva = cadastroCozinhaService.buscar(id);

		BeanUtils.copyProperties(cozinha, cozinhaSalva, "id");
		return cadastroCozinhaService.salvar(cozinhaSalva);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> remover(@PathVariable Long id) {
		cadastroCozinhaService.remover(id);
		return ResponseEntity.noContent().build();
	}

}
