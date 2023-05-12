package com.apigaworks.algafood.domain.dto.formaPagamento;

import com.apigaworks.algafood.domain.model.FormaPagamento;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record FormaPagamentoListDto(
        Long id,

        String descricao
) {
    public FormaPagamentoListDto(FormaPagamento formasPagamento) {
        this(formasPagamento.getId(), formasPagamento.getDescricao());
    }

    public static Set<FormaPagamentoListDto> converterLista(Set<FormaPagamento> formasPagamento) {
        return formasPagamento.stream().map(FormaPagamentoListDto::new).collect(Collectors.toSet());
    }
}
