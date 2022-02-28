package com.baggio.projeto.rbfood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baggio.projeto.rbfood.api.dto.RestauranteDTO;
import com.baggio.projeto.rbfood.domain.model.Restaurante;

@Component
public class RestauranteModelAsssembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public Restaurante toModel(RestauranteDTO restauranteDTO) {
		return modelMapper.map(restauranteDTO, Restaurante.class);
	}
	
}
