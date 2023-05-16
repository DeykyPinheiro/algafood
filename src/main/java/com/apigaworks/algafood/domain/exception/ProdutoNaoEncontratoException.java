package com.apigaworks.algafood.domain.exception;

public class ProdutoNaoEncontratoException extends  EntidadeNaoEncontradaException {

    public ProdutoNaoEncontratoException(String mensagem) {
        super(mensagem);
    }

    public ProdutoNaoEncontratoException(Long id) {
        this(String.format("Produto com codigo %d nao encontrado.", id));
    }

    public ProdutoNaoEncontratoException(Long idRestaurante, Long idProduto) {
        this(String.format("Restaurante %d Produto com codigo %d nao encontrado.", idRestaurante, idProduto));
    }
}
