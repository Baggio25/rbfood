package com.baggio.projeto.rbfood.api.exceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;

//utilizando o padrão RCF7807
@JsonInclude(Include.NON_NULL)
@Getter
@Builder
public class APIErro {
	
	private Integer status;
	private LocalDateTime timestamp;
	private String type;
	private String title;
	private String detail;
	private String userMessage;
	private List<Field> fields;
	
	@Getter
	@Builder
	public static class Field {
		private String name;
		private String userMessage;
	}
	
}
