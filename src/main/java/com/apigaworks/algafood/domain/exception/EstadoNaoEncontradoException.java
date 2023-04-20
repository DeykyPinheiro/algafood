package com.apigaworks.algafood.domain.exception;

public class EstadoNaoEncontradoException extends EntidadeNaoEncontradaException {

    public EstadoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public EstadoNaoEncontradoException(Long id) {
             this(String.format("Não existe um cadastro de estado com código %d", id));
    }
}
