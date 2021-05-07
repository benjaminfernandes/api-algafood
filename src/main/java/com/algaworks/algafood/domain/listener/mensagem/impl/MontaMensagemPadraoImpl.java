package com.algaworks.algafood.domain.listener.mensagem.impl;

import org.springframework.stereotype.Component;

import com.algaworks.algafood.domain.listener.mensagem.MontaMensagemPadrao;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.service.EnvioEmailService.Mensagem;

@Component
public class MontaMensagemPadraoImpl implements MontaMensagemPadrao {

	@Override
	public Mensagem getMensagemPadrao(Pedido pedido, String corpo) {
		
		var mensagem = Mensagem.builder()
				.assunto(pedido.getRestaurante().getNome() + " - Pedido confirmado")
				.corpo(corpo)
				.variavel("pedido", pedido)
				.destinatario(pedido.getCliente().getEmail())
				.build();
		
		return mensagem;
	}

}
