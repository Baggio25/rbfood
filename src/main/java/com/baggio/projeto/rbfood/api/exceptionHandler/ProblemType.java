package com.baggio.projeto.rbfood.api.exceptionHandler;

import lombok.Getter;

@Getter
public enum ProblemType {

	ENTIDADE_NAO_ENCONTRADA("/entidade-nao-encontrada", "Entidade não encontrada"),
	ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
	ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio"),
	MENSAGEN_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensivel"),
	ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sistema"),
	RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado"),
	PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro inválido"),
	DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos");
	
	private String titulo;
	private String path;
	
	private ProblemType(String titulo, String path) {
		this.path = "http://rbfood.com.br" + path;
		this.titulo = titulo;
	}
	
}
