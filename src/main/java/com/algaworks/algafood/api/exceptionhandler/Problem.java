package com.algaworks.algafood.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@JsonInclude(Include.NON_NULL)
@Getter
@Builder
@Schema(name = "Problema")
public class Problem {

	@Schema(example = "400")
	private Integer status;
	@Schema(example = "https://algafood.com.br/dados-inválidos")
	private String type;
	
	@Schema(example = "Dados inválidos")
	private String title;
	
	@Schema(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente")
	private String detail;
	
	@Schema(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente")
	private String userMessage;
	
	@Schema(example = "2023-07-15T01:01:50.9022458Z")
	private OffsetDateTime timestamp;
	
	@Schema(description = "Lista de objetos ou campos que geraram o erro")
	private List<Object> objects;
	
	@Getter
	@Builder
	@Schema(name = "Objeto problema")
	public static class Object {
		
		@Schema(example = "preço")
		private String name;
		@Schema(example = "O preço é inválido")
		private String userMessage;
	}

}
