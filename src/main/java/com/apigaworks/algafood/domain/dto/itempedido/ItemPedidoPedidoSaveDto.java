package com.apigaworks.algafood.domain.dto.itempedido;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItemPedidoPedidoSaveDto(

        @NotNull
        Long produtoId,

        @NotNull
        @Min(1)
        Integer quantidade,

        String observacao
) {
}
