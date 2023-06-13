package com.apigaworks.algafood.common.email;

import com.apigaworks.algafood.domain.service.EnvioEmailService;
import com.apigaworks.algafood.infrastructure.service.email.FakeEnvioEmailService;
import com.apigaworks.algafood.infrastructure.service.email.SmtpEnvioEmailService;

//seleciona a implementacao do email usada, real ou teste
public enum Implementacao {

    SMTP {
        @Override
        public EnvioEmailService emailService() {
            return new SmtpEnvioEmailService();
        }
    },

    FAKE {
        @Override
        public EnvioEmailService emailService() {
            return new FakeEnvioEmailService();
        }
    };

    public abstract EnvioEmailService emailService();
}