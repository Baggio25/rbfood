package com.baggio.projeto.rbfood.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModeMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
}
