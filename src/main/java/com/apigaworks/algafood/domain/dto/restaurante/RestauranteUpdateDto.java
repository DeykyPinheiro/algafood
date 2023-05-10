package com.apigaworks.algafood.domain.dto.restaurante;

import com.apigaworks.algafood.domain.dto.cozinha.CozinhaUpdateDto;
import com.apigaworks.algafood.domain.dto.endereco.EndecoUpdateDto;
import com.apigaworks.algafood.domain.model.Cozinha;
import com.apigaworks.algafood.domain.model.Endereco;
import com.apigaworks.algafood.domain.model.Restaurante;
import com.apigaworks.algafood.domain.repository.RestauranteRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RestauranteUpdateDto(
        String nome,

        BigDecimal taxaFrete,

        Boolean ativo,

        Boolean aberto,

        @Valid
        @NotNull
        CozinhaUpdateDto cozinha,

        @Valid
        @NotNull
        EndecoUpdateDto endereco) {

    public RestauranteUpdateDto(Restaurante restaurante) {
        this(restaurante.getNome(), restaurante.getTaxaFrete(), restaurante.getAtivo(), restaurante.getAberto(),
                new CozinhaUpdateDto(restaurante.getCozinha()), new EndecoUpdateDto(restaurante.getEndereco()));
    }
}
