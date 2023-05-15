package com.apigaworks.algafood.domain.exception;

public class ProdutoNaoEncontratoException extends  EntidadeNaoEncontradaException {

    public ProdutoNaoEncontratoException(String mensagem) {
        super(mensagem);
    }

    public ProdutoNaoEncontratoException(Long id) {
        this(String.format("Produto com codigo %d nao encontrado.", id));
    }
}
