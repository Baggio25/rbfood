package com.baggio.projeto.rbfood.api.exceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.baggio.projeto.rbfood.domain.exception.EntidadeEmUsoException;
import com.baggio.projeto.rbfood.domain.exception.EntidadeNaoEncontradaException;
import com.baggio.projeto.rbfood.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

@ControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {
	
	public static final String MSG_ERRO_GENERICA_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema. Tente novamente e se "
			+ "o problema persistir, entre em contato com o administrador do sistema.";

	@Autowired
	private MessageSource messageSource;
		
	
	/**
	 * Trata erros internos do sistema
	 * 
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
		String detail = MSG_ERRO_GENERICA_USUARIO_FINAL;

		ex.printStackTrace();

		APIErro apiErro = createAPIErroBuilder(status, problemType, detail).userMessage(detail).build();

		return handleExceptionInternal(ex, apiErro, new HttpHeaders(), status, request);
	}

	/**
	 * Trata recursos/entidades n??o encontradas ao realizar requisi????es
	 * 
	 */
	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException e,
			WebRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		ProblemType problemType = ProblemType.ENTIDADE_NAO_ENCONTRADA;
		String details = e.getMessage();

		APIErro apiErro = createAPIErroBuilder(status, problemType, details).build();
		return handleExceptionInternal(e, apiErro, new HttpHeaders(), status, request);
	}

	/**
	 * Trata tentativa de exclus??o de entidades que possuem v??nculo com entidades filhas
	 * 
	 */
	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException e, WebRequest request) {
		HttpStatus status = HttpStatus.CONFLICT;
		ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
		String details = e.getMessage();

		APIErro apiErro = createAPIErroBuilder(status, problemType, details).build();
		return handleExceptionInternal(e, apiErro, new HttpHeaders(), status, request);
	}

	/**
	 * Constr??i a valida????o de erros gen??ricos de neg??cio ( Ex.: Erros de digita????o no corpo da requisi????o )
	 * 
	 */
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocioException(NegocioException e, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_NEGOCIO;
		String details = e.getMessage();

		APIErro apiErro = createAPIErroBuilder(status, problemType, details).build();
		return handleExceptionInternal(e, apiErro, new HttpHeaders(), status, request);
	}
	
	/**
	 * Constr??i a valida????o de campos inv??lidos ( ex.: campos nulos )
	 * 
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.DADOS_INVALIDOS;
		String detail = "Um ou mais campos est??o inv??lidos. Fa??a o preenchimento correto e tente novamente.";
		BindingResult bindingResult = ex.getBindingResult();
		List<APIErro.Field> fields = bindingResult.getFieldErrors().stream()
				.map(fieldError -> { 
					String msg = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
					
					return APIErro.Field.builder()
						.name(fieldError.getField())
						.userMessage(msg)
						.build(); 
				})
				.collect(Collectors.toList());
		
		APIErro apiErro = createAPIErroBuilder(status, problemType, detail)
				.userMessage(detail)
				.fields(fields)
				.build();

		return handleExceptionInternal(ex, apiErro, headers, status, request);
	}

	/**
	 * Constr??i a valida????o de recursos n??o encontrados
	 * 
	 */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = String.format("O recurso %s, que voc?? tentou acessar, ?? inexistente.", ex.getRequestURL());

		APIErro apiErro = createAPIErroBuilder(status, problemType, detail).userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();

		return handleExceptionInternal(ex, apiErro, headers, status, request);
	}

	/**
	 * Constr??i a valida????o de argumentos inv??lidos do controlador
	 * 
	 */
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (ex instanceof MethodArgumentTypeMismatchException) {
			return handleMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) ex, headers, status, request);
		}

		return super.handleTypeMismatch(ex, headers, status, request);
	}

	/**
	 * Monta o corpo das mensagens de valida????o conforme a excess??o
	 * 
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (body == null) {
			body = APIErro.builder().title(status.getReasonPhrase()).status(status.value()).build();
		} else if (body instanceof String) {
			body = APIErro.builder().title((String) body).status(status.value()).build();
		}

		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	/**
	 * Realiza a valida????o de requisi????es com erro de sintaxe
	 * 
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Throwable rootCause = ExceptionUtils.getRootCause(ex);
		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
		} else if (rootCause instanceof PropertyBindingException) {
			return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
		}

		ProblemType problemType = ProblemType.MENSAGEN_INCOMPREENSIVEL;
		String details = "O corpo da requisi????o est?? inv??lido. Verifique erro de sintaxe.";

		APIErro apiErro = createAPIErroBuilder(status, problemType, details).build();
		return handleExceptionInternal(ex, apiErro, new HttpHeaders(), status, request);
	}

	/**
	 * Realiza a valida????o de par??metros inv??lidos na requisi????o
	 * 
	 */
	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;

		String detail = String.format(
				"O par??metro de URL '%s' recebeu o valor '%s', "
						+ "que ?? de um tipo inv??lido. Corrija e informe um valor compat??vel com o tipo %s.",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

		APIErro apiErro = createAPIErroBuilder(status, problemType, detail).userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();

		return handleExceptionInternal(ex, apiErro, headers, status, request);
	}

	/**
	 * Realiza a valida????o de propriedades da requisi????o que s??o inexistentes
	 * 
	 */
	private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		String path = joinPath(ex.getPath());
		ProblemType problemType = ProblemType.MENSAGEN_INCOMPREENSIVEL;
		String detail = String.format(
				"A propriedade '%s' n??o existe, " + "Corrija ou remova essa propriedade e tente novamente.", path);
		APIErro apiErro = createAPIErroBuilder(status, problemType, detail).build();
		;

		return handleExceptionInternal(ex, apiErro, headers, status, request);
	}

	/**
	 * Realiza a valida????o de propriedades da requisi????o com valores inv??lidos
	 * 
	 */
	private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		String path = joinPath(ex.getPath());

		ProblemType problemType = ProblemType.MENSAGEN_INCOMPREENSIVEL;
		String detail = String.format(
				"A propriedade '%s' recebeu o valor '%s', "
						+ "que ?? de um tipo inv??lido. Corrija e informe um valor compat??vel com o tipo %s.",
				path, ex.getValue(), ex.getTargetType().getSimpleName());
		APIErro apiErro = createAPIErroBuilder(status, problemType, detail).build();
		;

		return handleExceptionInternal(ex, apiErro, headers, status, request);
	}

	private String joinPath(List<Reference> references) {
		return references.stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
	}

	private APIErro.APIErroBuilder createAPIErroBuilder(HttpStatus status, ProblemType problemType, String details) {
		return APIErro.builder().status(status.value()).type(problemType.getPath()).title(problemType.getTitulo())
				.detail(details);
	}

}
