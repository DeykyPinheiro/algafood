package com.apigaworks.algafood.domain.dto.itempedido;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItemPedidoPedidoSaveDto(

        @Schema(example = "10")
        @NotNull
        Long produtoId,

        @Schema(example = "2")
        @NotNull
        @Min(1)
        Integer quantidade,

        @Schema(example = "sem cebola")
        String observacao
) {
}
