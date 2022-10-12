package com.algaworks.algafood.domain.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.algaworks.algafood.domain.event.PedidoConfirmadoEvent;
import com.algaworks.algafood.domain.listener.mensagem.MontaMensagemPadrao;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.service.EnvioEmailService;

@Component
public class NotificacaoClientePedidoConfirmadoListener {

	@Autowired
	private EnvioEmailService envioEmail;
	@Autowired
	private MontaMensagemPadrao montaMensagemPadrao;
	
	//aula 15.13
	@TransactionalEventListener //Usado para enviar o e-mail antes de fazer o commit na transação
	public void aoConfirmarPedido(PedidoConfirmadoEvent event) {
		Pedido pedido = event.getPedido();
		
		var mensagem = this.montaMensagemPadrao.getMensagemPadrao(pedido, 
				"emails/pedido-confirmado.html");
		
		envioEmail.enviar(mensagem);
	}
}
