package com.apigaworks.algafood.domain.dto.formaPagamento;

import com.apigaworks.algafood.domain.model.FormaPagamento;

import java.util.*;
import java.util.stream.Collectors;
import java.util.Collection;

public record FormaPagamentoListDto(
        Long id,

        String descricao
) {
    public FormaPagamentoListDto(FormaPagamento formasPagamento) {
        this(formasPagamento.getId(), formasPagamento.getDescricao());
    }

    public static List<FormaPagamentoListDto> converterLista(Collection<FormaPagamento> formasPagamento) {
        return formasPagamento.stream().map(FormaPagamentoListDto::new).collect(Collectors.toList());
    }
}
