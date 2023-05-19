package com.apigaworks.algafood.domain.dto.restaurante;

import com.apigaworks.algafood.domain.model.Restaurante;

public record RestaurantePedidoDto(

        Long id,

        String nome
) {
    public RestaurantePedidoDto(Restaurante restaurante) {
        this(restaurante.getId(), restaurante.getNome());
    }
}
