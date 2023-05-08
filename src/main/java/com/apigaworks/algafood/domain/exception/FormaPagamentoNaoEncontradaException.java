package com.apigaworks.algafood.domain.exception;

public class FormaPagamentoNaoEncontradaException extends  EntidadeNaoEncontradaException {
    public FormaPagamentoNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public FormaPagamentoNaoEncontradaException(Long id) {
        this(String.format("Nao exite uma Forma de Pagamento com codigo %d", id));
    }
}
