package com.baggio.projeto.rbfood.api.exceptionHandler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.baggio.projeto.rbfood.domain.exception.EntidadeEmUsoException;
import com.baggio.projeto.rbfood.domain.exception.EntidadeNaoEncontradaException;
import com.baggio.projeto.rbfood.domain.exception.NegocioException;

@ControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException e,
			WebRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		String detail = e.getMessage();
		APIErro apiErro = APIErro.builder()
				.status(status.value())
				.type("http://rbfood.com.br/entidade-nao-encontrada")
				.title("Entidade não encontrada")
				.detail(detail)
				.build();
		return handleExceptionInternal(e, apiErro, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException e, WebRequest request) {
		return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
	}

	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocioException(NegocioException e, WebRequest request) {
		return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (body == null) {
			body = retornaAPIErro(status, status.getReasonPhrase());
		} else if( body instanceof String) {
			body = retornaAPIErro(status, (String) body);
		}
		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	private Object retornaAPIErro(HttpStatus status, String title) {
		Object body;
		body = APIErro.builder()
				.title(title)
				.status(status.value())
				.build();
		return body;
	}

}
