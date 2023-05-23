package com.apigaworks.algafood.domain.dto.pedido;

import com.apigaworks.algafood.domain.dto.endereco.EnderecoPedidoDto;
import com.apigaworks.algafood.domain.dto.itempedido.ItemPedidoPedidoSaveDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PedidoSaveDto(

        @NotNull
        Long restauranteId,

        @NotNull
        Long formaPagamentoId,


        @NotNull
        @Valid
        EnderecoPedidoDto enderecoEntrega,

        @NotNull
        @Valid
        @Size(min = 1)
        List<ItemPedidoPedidoSaveDto> itens

) {


}
