package com.algaworks.algafood.core.email;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Validated //anotação necessário para validar o @NotNull ao iniciar a aplicação
@Setter
@Getter
@Component
@ConfigurationProperties("algafood.email")
public class EmailProperties {

	@NotNull
	private String remetente;
	
	private Sandbox sandbox = new Sandbox();
	
	@NonNull
	private Impl impl;
	
	public enum Impl {
		FAKE, SMTP, SANDBOX
	}
	
	@Getter
	@Setter
	public class Sandbox {
	    
	    private String destinatario;
	    
	}
}
