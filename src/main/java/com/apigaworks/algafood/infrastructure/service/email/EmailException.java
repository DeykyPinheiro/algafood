package com.apigaworks.algafood.infrastructure.service.email;

import com.apigaworks.algafood.domain.exception.NegocioException;

public class EmailException extends NegocioException {

    public EmailException(String mensagem) {
        super(mensagem);
    }

    public EmailException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
