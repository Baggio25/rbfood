package com.baggio.projeto.rbfood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.baggio.projeto.rbfood.api.assembler.RestauranteDTOAssembler;
import com.baggio.projeto.rbfood.api.assembler.RestauranteModelAsssembler;
import com.baggio.projeto.rbfood.api.dto.RestauranteDTO;
import com.baggio.projeto.rbfood.domain.exception.CozinhaNaoEncontradaException;
import com.baggio.projeto.rbfood.domain.exception.NegocioException;
import com.baggio.projeto.rbfood.domain.model.Restaurante;
import com.baggio.projeto.rbfood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {

	@Autowired
	private CadastroRestauranteService cadastroRestauranteService;
	
	@Autowired
	private RestauranteDTOAssembler restauranteDTOAssembler;
	
	@Autowired 
	private RestauranteModelAsssembler restauranteModelAsssembler;

	@GetMapping
	public List<RestauranteDTO> listar() {
		return restauranteDTOAssembler.toCollectionDTO(cadastroRestauranteService.listar());
	}

	@GetMapping(value = "/{id}")
	public RestauranteDTO buscar(@PathVariable Long id) {
		Restaurante restaurante = cadastroRestauranteService.buscar(id);
		return restauranteDTOAssembler.toDTO(restaurante);
	}

	@PutMapping(value = "/{id}")
	public RestauranteDTO atualizar(@PathVariable Long id, @Valid @RequestBody RestauranteDTO restauranteDTO) {
		
		try {
			
			Restaurante restaurante = restauranteModelAsssembler.toModel(restauranteDTO);
			
			Restaurante restauranteSalvo = cadastroRestauranteService.buscar(id);
			BeanUtils.copyProperties(restaurante, restauranteSalvo, "id", "formasPagamento", "endereco", "dataCadastro");
	
			return restauranteDTOAssembler.toDTO(cadastroRestauranteService.salvar(restauranteSalvo));
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}

	}

	@PatchMapping(value = "/{id}")
	public RestauranteDTO atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> campos,
			HttpServletRequest request) {
		
		Restaurante restauranteSalvo = cadastroRestauranteService.buscar(id);
		merge(campos, restauranteSalvo, request);
		
		RestauranteDTO restauranteDTO = restauranteDTOAssembler.toDTO(restauranteSalvo);	
		return atualizar(id, restauranteDTO);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteDTO adicionar(@RequestBody @Valid Restaurante restaurante) {
		try {
			return restauranteDTOAssembler.toDTO(cadastroRestauranteService.salvar(restaurante));
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@DeleteMapping(value = "/{id}")
	public void remover(@PathVariable Long id) {
		cadastroRestauranteService.remover(id);
	}

	private void merge(Map<String, Object> camposOrigem, Restaurante restauranteDestino, HttpServletRequest request) {
		ServletServerHttpRequest serverRequest = new ServletServerHttpRequest(request);
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
			
			Restaurante restauranteOrigem = objectMapper.convertValue(camposOrigem, Restaurante.class);
	
			camposOrigem.forEach((nomePropriedade, valorPropriedade) -> {
				Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
				field.setAccessible(true);
	
				Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
	
				ReflectionUtils.setField(field, restauranteDestino, novoValor);
			});
		}catch (IllegalArgumentException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverRequest);
		}
	}

	

}
