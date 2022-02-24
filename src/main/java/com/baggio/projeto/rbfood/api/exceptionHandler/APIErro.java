package com.baggio.projeto.rbfood.api.exceptionHandler;

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
	private String type;
	private String title;
	private String detail;
	
}
