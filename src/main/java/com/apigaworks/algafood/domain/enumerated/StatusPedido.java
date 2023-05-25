package com.apigaworks.algafood.domain.enumerated;

import java.util.Arrays;
import java.util.List;

public enum StatusPedido {
    CRIADO("Criado"),
    CONFIRMADO("Confirmado", CRIADO),
    ENTREGUE("Entregue", CONFIRMADO),
    CANCELADO("Cancelado", CRIADO, CONFIRMADO);

    private String descricao;

    private List<StatusPedido> statusAnteriores;

    //    var args é igual o argc argv do C
    StatusPedido(String descricao, StatusPedido... statusPedidos) {
        this.statusAnteriores = Arrays.asList(statusPedidos);
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean naoPodeAlterarStatus(StatusPedido novoStatus) {
//        this aqui é o status atual, da classe que foi chamada
        return !novoStatus.statusAnteriores.contains(this);
    }
}
