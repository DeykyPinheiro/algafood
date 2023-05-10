package com.apigaworks.algafood.domain.exception;

public class GrupoNaoEncontratoException extends EntidadeNaoEncontradaException {



    public GrupoNaoEncontratoException(String mensagem) {
        super(mensagem);
    }

    public GrupoNaoEncontratoException(Long id) {
        this(String.format("Não existe um cadastro de grupo com código %d", id));
    }
}
