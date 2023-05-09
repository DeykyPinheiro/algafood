package com.apigaworks.algafood.domain.dto.formaPagamento;

import com.apigaworks.algafood.domain.model.FormaPagamento;

public record FormaPagamentoDto(Long id, String descricao) {

    public FormaPagamentoDto(FormaPagamento data) {
        this(data.getId(), data.getDescricao());
    }



}
