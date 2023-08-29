package com.apigaworks.algafood.domain.dto.itempedido;

import com.apigaworks.algafood.domain.dto.pedido.PedidoListDto;
import com.apigaworks.algafood.domain.model.ItemPedido;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record ItemPedidoPedidoDto(

        @Schema(example = "10")
        Long produtoId,

        @Schema(example = "Bife ancho")
        String produtoNome,

        @Schema(example = "2")
        Integer quantidade,

        @Schema(example = "10.00")
        BigDecimal precoUnitario,

        @Schema(example = "20.00")
        BigDecimal precoTotal,

        @Schema(example = "sem cebola")
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
