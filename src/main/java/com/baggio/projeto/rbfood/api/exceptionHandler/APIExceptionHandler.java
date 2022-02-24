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
		ProblemType problemType = ProblemType.ENTIDADE_NAO_ENCONTRADA;
		String details = e.getMessage();
		
		APIErro apiErro = createAPIErroBuilder(status, problemType, details).build();
		return handleExceptionInternal(e, apiErro, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException e, WebRequest request) {
		HttpStatus status = HttpStatus.CONFLICT;
		ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
		String details = e.getMessage();
		
		APIErro apiErro = createAPIErroBuilder(status, problemType, details).build();
		return handleExceptionInternal(e, apiErro, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocioException(NegocioException e, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_NEGOCIO;
		String details = e.getMessage();
		
		APIErro apiErro = createAPIErroBuilder(status, problemType, details).build();
		return handleExceptionInternal(e, apiErro, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (body == null) {
			body = APIErro.builder()
				.title(status.getReasonPhrase())
				.status(status.value())
				.build();
		} else if (body instanceof String) {
			body = APIErro.builder()
				.title((String) body)
				.status(status.value())
				.build();
		}
		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	private APIErro.APIErroBuilder createAPIErroBuilder(HttpStatus status, ProblemType problemType, 
			String details) {
		return APIErro.builder()
				.status(status.value())
				.type(problemType.getPath())
				.title(problemType.getTitulo())
				.detail(details);
	}

}
