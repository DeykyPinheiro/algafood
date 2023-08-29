package com.apigaworks.algafood.domain.dto.formaPagamento;

import com.apigaworks.algafood.domain.model.FormaPagamento;
import io.swagger.v3.oas.annotations.media.Schema;

public record FormaPagamentoDto(

        @Schema(example = "1")
        Long id,

        @Schema(example = "Cartao de Credito")
        String descricao) {

    public FormaPagamentoDto(FormaPagamento data) {
        this(data.getId(), data.getDescricao());
    }



}
