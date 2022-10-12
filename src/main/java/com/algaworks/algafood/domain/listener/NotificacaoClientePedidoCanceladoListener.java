package com.algaworks.algafood.domain.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.algaworks.algafood.domain.event.PedidoCanceladoEvent;
import com.algaworks.algafood.domain.listener.mensagem.MontaMensagemPadrao;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.service.EnvioEmailService;

@Component
public class NotificacaoClientePedidoCanceladoListener {

	@Autowired
	private EnvioEmailService envioEmail;
	@Autowired
	private MontaMensagemPadrao montaMensagem;
	
	@TransactionalEventListener
	public void aoCancelarPedido(PedidoCanceladoEvent event) {
		Pedido pedido = event.getPedido();
		
		var mensagem = this.montaMensagem.getMensagemPadrao(pedido, 
				"emails/pedido-cancelado.html");
		
		envioEmail.enviar(mensagem);
	}
}
