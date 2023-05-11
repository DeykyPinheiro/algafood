package com.apigaworks.algafood.domain.exception;

public class UsuarioNaoEncontratoException extends EntidadeNaoEncontradaException {

    public UsuarioNaoEncontratoException(String mensagem) {
        super(mensagem);
    }

    public UsuarioNaoEncontratoException(Long id) {
        this(String.format("Não existe um cadastro de usuario com código %d", id));
    }
}
