package com.algaworks.algafood.domain.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.algaworks.algafood.domain.event.PedidoSaiuEntregaEvent;
import com.algaworks.algafood.domain.listener.mensagem.MontaMensagemPadrao;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.service.EnvioEmailService;

@Component
public class NotificacaoClienteSaiuEntregaListener {

	@Autowired
	private EnvioEmailService envioEmail;
	@Autowired
	private MontaMensagemPadrao montaMensagem;
	
	@TransactionalEventListener
	private void aoConfirmarEntrega(PedidoSaiuEntregaEvent event) {
		Pedido pedido = event.getPedido();
		
		var mensagem = this.montaMensagem.getMensagemPadrao(pedido, 
				"emails/pedido-saiu-entrega.html");
		
		envioEmail.enviar(mensagem);
	}
}
