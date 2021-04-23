package com.algaworks.algafood.core.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.algaworks.algafood.core.email.EmailProperties.Impl;
import com.algaworks.algafood.domain.repository.infraestructure.service.email.FakeEnvioEmailService;
import com.algaworks.algafood.domain.repository.infraestructure.service.email.SmtpEnvioEmailService;
import com.algaworks.algafood.domain.service.EnvioEmailService;

@Configuration
public class EmailConfig {

	@Autowired
	private EmailProperties emailProperties;
	
	@Bean
	public EnvioEmailService envioEmailService() {
		if(emailProperties.getImpl().equals(Impl.SMTP)) {
			return new SmtpEnvioEmailService();
		}else {
			return new FakeEnvioEmailService();
		}
	}
}
