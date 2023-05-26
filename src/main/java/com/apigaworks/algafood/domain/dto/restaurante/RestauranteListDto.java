package com.apigaworks.algafood.domain.dto.restaurante;

import com.apigaworks.algafood.domain.dto.cozinha.CozinhaDto;
import com.apigaworks.algafood.domain.dto.endereco.EnderecoDto;
import com.apigaworks.algafood.domain.model.Restaurante;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public record RestauranteListDto(

        Long id,

        String nome,

        BigDecimal taxaFrete,

        CozinhaDto cozinha,

        Boolean ativo,

        Boolean aberto



) {

    public RestauranteListDto(Restaurante restaurante) {
        this(restaurante.getId(), restaurante.getNome(),restaurante.getTaxaFrete(),
                new CozinhaDto(restaurante.getCozinha()), restaurante.getAtivo(), restaurante.getAberto());
    }
    public static List<RestauranteListDto> converterLista(Collection<Restaurante> lista) {
        return lista.stream().map(RestauranteListDto::new).collect(Collectors.toList());
    }
}
