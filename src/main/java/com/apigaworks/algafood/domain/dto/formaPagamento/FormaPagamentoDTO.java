package com.apigaworks.algafood.domain.dto.formaPagamento;

import com.apigaworks.algafood.domain.model.FormaPagamento;

public record FormaPagamentoDTO(Long id, String descricao) {

    public FormaPagamentoDTO(FormaPagamento data) {
        this(data.getId(), data.getDescricao());
    }



}
