package com.apigaworks.algafood.domain.listener;

import com.apigaworks.algafood.domain.event.PedidoCanceladoEvent;
import com.apigaworks.algafood.domain.event.PedidoConfirmadoEvent;
import com.apigaworks.algafood.domain.model.Pedido;
import com.apigaworks.algafood.domain.service.EnvioEmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificacaoPedidoCanceladoListener {

    @Autowired
    private EnvioEmailService envioEmailService;

    @TransactionalEventListener(phase =  TransactionPhase.BEFORE_COMMIT)
    public void aoCancelarPedido(PedidoCanceladoEvent event) throws MessagingException {

        Pedido pedido = event.getPedido();

//        enviar e-mail
        var mensagem = EnvioEmailService.Mensagem.builder()
                .assunto(pedido.getRestaurante().getNome() + " - pedido cancelado")
                .corpo("O pedido de codigo <strong> " + pedido.getId() + "</strong> foi cancelado")
                .destinatario(pedido.getCliente().getEmail())
                .build();

        envioEmailService.enviar(mensagem);

    }
}
