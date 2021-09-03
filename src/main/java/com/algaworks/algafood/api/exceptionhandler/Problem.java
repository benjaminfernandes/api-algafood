package com.algaworks.algafood.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@ApiModel("Problema")
@JsonInclude(Include.NON_NULL)
@Getter
@Builder
public class Problem {

	@ApiModelProperty(example = "400", position = 1)
	private Integer status;
	@ApiModelProperty(example = "https://algafood.com.br/dados-invalidos", position = 5)
	private String type;
	@ApiModelProperty(example = "Dados inválidos", position = 10)
	private String title;
	@ApiModelProperty(example = "Um ou mais campos estão inválidos, Faça o preenchimento correto", position = 15)
	private String detail;
	@ApiModelProperty(example = "Um ou mais campos estão inválidos, Faça o preenchimento correto", position = 20)
	private String userMessage;
	@ApiModelProperty(example = "2021-09-03T18:28:02.70844Z", position = 25)
	private OffsetDateTime timestamp;
	@ApiModelProperty(value = "Objetos ou campos que geraram o erro", position = 30)
	private List<Object> objects;
	
	@ApiModel("ObjetoProblema")
	@Getter
	@Builder
	public static class Object {
		
		@ApiModelProperty(example = "Preço")
		private String name;
		@ApiModelProperty(example = "Preço é obrigatório")
		private String userMessage;
	}

}
