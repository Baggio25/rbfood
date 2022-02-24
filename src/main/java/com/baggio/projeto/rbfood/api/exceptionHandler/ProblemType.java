package com.baggio.projeto.rbfood.api.exceptionHandler;

import lombok.Getter;

@Getter
public enum ProblemType {

	ENTIDADE_NAO_ENCONTRADA("/entidade-nao-encontrada", "Entidade não encontrada"),
	ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
	ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio");
	
	private String titulo;
	private String path;
	
	private ProblemType(String titulo, String path) {
		this.path = "http://rbfood.com.br" + path;
		this.titulo = titulo;
	}
	
}
