package com.apigaworks.algafood.domain.dto.itempedido;

import com.apigaworks.algafood.domain.dto.pedido.PedidoListDto;
import com.apigaworks.algafood.domain.model.ItemPedido;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record ItemPedidoPedidoDto(

        Long produtoId,

        String produtoNome,

        Integer quantidade,

        BigDecimal precoUnitario,

        BigDecimal precoTotal,

        String observacao

) {

    public ItemPedidoPedidoDto(ItemPedido itemPedido) {
        this(itemPedido.getProduto().getId(), itemPedido.getProduto().getNome(),
                itemPedido.getQuantidade(), itemPedido.getPrecoUnitario(), itemPedido.getPrecoTotal(),
                itemPedido.getObservacao());
    }

    public static List<ItemPedidoPedidoDto> converterLista(Collection<ItemPedido> itensPedidos){
        return itensPedidos.stream().map(ItemPedidoPedidoDto::new).collect(Collectors.toList());
    }
}
