package com.algaworks.algafood.core.validation;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;
//Aula 9.16 ESR e Aula 14.3
public class FileContentTypeValidator implements ConstraintValidator<FileContentType, MultipartFile>{

	private List<String> allowedContentType;
	
	@Override
	public void initialize(FileContentType constraintAnnotation) {
		this.allowedContentType = Arrays.asList(constraintAnnotation.allowed());
	}
	
	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		return value.getContentType() == null || 
				this.allowedContentType.contains(value.getContentType());
	}
}
