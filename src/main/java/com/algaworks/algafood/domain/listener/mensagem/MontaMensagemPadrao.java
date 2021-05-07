package com.algaworks.algafood.domain.listener.mensagem;

import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.service.EnvioEmailService.Mensagem;

public interface MontaMensagemPadrao {

	Mensagem getMensagemPadrao(Pedido pedido, String corpo);
}
