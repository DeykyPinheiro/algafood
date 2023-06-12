package com.apigaworks.algafood.domain.listener;

import com.apigaworks.algafood.domain.event.PedidoConfirmadoEvent;
import com.apigaworks.algafood.domain.model.Pedido;
import com.apigaworks.algafood.domain.service.EnvioEmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificacaoPedidoConfirmadoListener {

    @Autowired
    private EnvioEmailService envioEmailService;


    //    tenho que criar uma funcao, mas pode ter qualquer nome
//    @EventListener //marca como um escutador de eventos
//    @TransactionalEventListener // só ler transacao depois que o evento for commitado no banco
//    aqui ele só completa as acoes se tudo der certo BEFORE_COMMIT
    @TransactionalEventListener(phase =  TransactionPhase.BEFORE_COMMIT)
    public void aoConfirmarPedido(PedidoConfirmadoEvent event) throws MessagingException {

        Pedido pedido = event.getPedido();

//        enviar e-mail
        var mensagem = EnvioEmailService.Mensagem.builder()
                .assunto(pedido.getRestaurante().getNome() + " - pedido confirmado")
                .corpo("O pedido de codigo <strong> " + pedido.getId() + "</strong> foi confirmado")
                .destinatario(pedido.getCliente().getEmail())
                .build();

        System.out.println("confirmei o pediders krai");
        envioEmailService.enviar(mensagem);

    }
}
