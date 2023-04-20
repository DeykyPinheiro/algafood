package com.apigaworks.algafood.domain.exception;

public class CozinhaNaoEncontradaException extends EntidadeNaoEncontradaException {

    public CozinhaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public CozinhaNaoEncontradaException(Long id) {
             this(String.format("Não existe um cadastro de cozinha com código %d", id));
    }
}
