package com.apigaworks.algafood.common.email;

import com.apigaworks.algafood.domain.service.EnvioEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {

    //    usa as propriedades que foram injetadas aqui
    @Autowired
    private EmailProperties emailProperties;

    @Bean
    public EnvioEmailService envioEmailService() {
//        retorna o que foi selecionado
        return emailProperties.getImpl().emailService();
    }

}
