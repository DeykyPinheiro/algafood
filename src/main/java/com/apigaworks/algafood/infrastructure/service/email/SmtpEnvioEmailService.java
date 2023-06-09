package com.apigaworks.algafood.infrastructure.service.email;

import com.apigaworks.algafood.core.email.EmailProperties;
import com.apigaworks.algafood.domain.service.EnvioEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SmtpEnvioEmailService implements EnvioEmailService {

    //    para enviar um email preciso da instacia de JavaMailSender
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailProperties emailProperties;

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
            helper.setTo(mensagem.getDestinatarios().toArray(new String[0]));
            helper.setFrom(emailProperties.getRemetente());

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new EmailException("Nao foi possivel enviar email", e);
        }
    }


}
