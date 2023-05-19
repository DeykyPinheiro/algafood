package com.apigaworks.algafood.domain.exception;

public class PedidoNaoEncontratoException extends  EntidadeNaoEncontradaException{

    public PedidoNaoEncontratoException(String mensagem) {
        super(mensagem);
    }

    public PedidoNaoEncontratoException(Long pedidoId) {
        this(String.format("Não existe um pedido código %d", pedidoId));
    }
}

