package com.apigaworks.algafood.domain.service;

import jakarta.mail.MessagingException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

public interface EnvioEmailService {

    void enviar(Mensagem mensagem) throws MessagingException;


    @Getter
    @Setter
    @Builder
    class Mensagem {

        //        o set serve para nao ter usuario repitidos
        private Set<String> destinatarios;

        private String assunto;

        private String corpo;
    }
}
