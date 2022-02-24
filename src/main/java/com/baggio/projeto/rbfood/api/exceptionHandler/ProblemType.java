package com.baggio.projeto.rbfood.api.exceptionHandler;

import lombok.Getter;

@Getter
public enum ProblemType {

	ENTIDADE_NAO_ENCONTRADA("/entidade-nao-encontrada", "Entidade não encontrada");
	
	private String titulo;
	private String path;
	
	private ProblemType(String titulo, String path) {
		this.path = "http://rbfood.com.br" + path;
		this.titulo = titulo;
	}
	
}
