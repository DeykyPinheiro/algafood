package com.apigaworks.algafood.infrastructure.service.email;

import com.apigaworks.algafood.common.email.EmailProperties;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SandboxEnvioEmailService extends SmtpEnvioEmailService {

    @Autowired
    private EmailProperties emailProperties;

    @Autowired
    private JavaMailSender mailSender;

    // Separei a criação de MimeMessage em um método na classe pai (criarMimeMessage),
    // para possibilitar a sobrescrita desse método aqui
    @Override
    public void enviar(Mensagem mensagem) {
        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();

//        ajuda a construir as coisas dentro do mime message de uma forma mais pratica
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
//            assunto, segundo é o corpo, e o pramentro é pra dizer se é texto ou html,true quer dizer
//            que é html
            helper.setSubject(mensagem.getAssunto());
            helper.setText(mensagem.getCorpo(), true);
            helper.setTo(emailProperties.getSandbox().getDestinatario());
            helper.setFrom(emailProperties.getRemetente());

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new EmailException("Nao foi possivel enviar email", e);
        }
    }



}