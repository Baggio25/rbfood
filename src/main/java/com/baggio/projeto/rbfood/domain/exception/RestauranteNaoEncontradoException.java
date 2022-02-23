package com.baggio.projeto.rbfood.domain.exception;

public class RestauranteNaoEncontradoException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;

	public RestauranteNaoEncontradoException(String msg) {
		super(msg);
	}
	
}
