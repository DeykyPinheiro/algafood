package com.apigaworks.algafood.domain.dto.pedido;

import com.apigaworks.algafood.domain.dto.endereco.EnderecoPedidoDto;
import com.apigaworks.algafood.domain.dto.itempedido.ItemPedidoPedidoSaveDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PedidoSaveDto(

        @Schema(example = "1")
        @NotNull
        Long restauranteId,

        @Schema(example = "1")
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
