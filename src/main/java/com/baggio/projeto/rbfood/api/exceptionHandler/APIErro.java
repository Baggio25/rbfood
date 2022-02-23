package com.baggio.projeto.rbfood.api.exceptionHandler;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class APIErro {

	private LocalDateTime dataHora;
	private String mensagem;
	
	
}
