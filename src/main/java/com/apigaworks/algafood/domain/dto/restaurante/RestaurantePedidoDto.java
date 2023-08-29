package com.apigaworks.algafood.domain.dto.restaurante;

import com.apigaworks.algafood.domain.model.Restaurante;
import io.swagger.v3.oas.annotations.media.Schema;

public record RestaurantePedidoDto(

        @Schema(example = "1")
        Long id,

        @Schema(example = "tuk tuk")
        String nome
) {
    public RestaurantePedidoDto(Restaurante restaurante) {
        this(restaurante.getId(), restaurante.getNome());
    }
}
