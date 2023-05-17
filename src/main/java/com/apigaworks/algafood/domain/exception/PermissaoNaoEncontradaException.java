package com.apigaworks.algafood.domain.exception;

public class PermissaoNaoEncontradaException extends  EntidadeNaoEncontradaException{

    public PermissaoNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public PermissaoNaoEncontradaException(Long permissaoId) {
        this(String.format("Permissao com codigo %d nao encontrada", permissaoId));
    }
}
