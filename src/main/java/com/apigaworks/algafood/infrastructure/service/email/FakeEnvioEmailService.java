package com.apigaworks.algafood.infrastructure.service.email;

import com.apigaworks.algafood.domain.service.EnvioEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


//Classe que simula o envio de e-mail logando na tela o que foi enviado
@Service
@Slf4j
public class FakeEnvioEmailService implements EnvioEmailService {


    @Override
    public void enviar(Mensagem mensagem) throws MessagingException {
        try {
            log.info("[FAKE E-MAIL] Para: {}\n{}", mensagem.getDestinatarios(), mensagem.getCorpo());

        } catch (Exception e) {
            throw new EmailException("Nao foi possivel enviar email", e);
        }
    }
}
