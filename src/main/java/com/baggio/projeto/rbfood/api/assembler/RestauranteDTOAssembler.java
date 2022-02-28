package com.baggio.projeto.rbfood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baggio.projeto.rbfood.api.dto.RestauranteDTO;
import com.baggio.projeto.rbfood.domain.model.Restaurante;

@Component
public class RestauranteDTOAssembler {
	
	@Autowired
	private ModelMapper modelMapper;

	public RestauranteDTO toDTO(Restaurante restaurante) {
		return modelMapper.map(restaurante, RestauranteDTO.class);
	}
	
	public List<RestauranteDTO> toCollectionDTO(List<Restaurante> restaurantes) {
		return restaurantes.stream()
				.map(restaurante -> toDTO(restaurante))
				.collect(Collectors.toList());
	}
	
}
