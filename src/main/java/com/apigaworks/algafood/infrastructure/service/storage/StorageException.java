package com.apigaworks.algafood.infrastructure.service.storage;

import com.apigaworks.algafood.domain.exception.NegocioException;

public class StorageException extends NegocioException {

    public StorageException(String mensagem) {
        super(mensagem);
    }

    public StorageException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
