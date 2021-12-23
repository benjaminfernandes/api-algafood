package com.algaworks.algafood.api.v2.model.input;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.sun.istack.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("CidadeInput")
@Getter
@Setter
public class CidadeInputV2 {

	@ApiModelProperty(example = "Guaíra", required = true)
	@NotBlank
	private String nomeCidade;
	@ApiModelProperty(example = "1", required = true)
	@Valid
	@NotNull
	private Long idEstado;

}
