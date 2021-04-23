package com.algaworks.algafood.domain.repository.infraestructure.service.email;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FakeEnvioEmailService extends SmtpEnvioEmailService{

	@Override
	public void enviar(Mensagem mensagem) {
		
		log.info("Enviando e-mail fake");
		
		String corpo = processarTemplate(mensagem);
		
		log.info("[FAKE E-MAIL] Para: {}\n{}", mensagem.getDestinatarios(), corpo);
		
	}

}
