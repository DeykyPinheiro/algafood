package com.apigaworks.algafood.domain.dto.pedido;

import com.apigaworks.algafood.domain.dto.endereco.EnderecoDto;
import com.apigaworks.algafood.domain.dto.formaPagamento.FormaPagamentoDto;
import com.apigaworks.algafood.domain.dto.itempedido.ItemPedidoPedidoDto;
import com.apigaworks.algafood.domain.dto.restaurante.RestaurantePedidoDto;
import com.apigaworks.algafood.domain.dto.usuario.UsuarioDto;
import com.apigaworks.algafood.domain.enumerated.StatusPedido;
import com.apigaworks.algafood.domain.model.Pedido;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collection;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record PedidoListDto(

        @Schema(example = "1")
        Long id,

        @Schema(example = "10")
        BigDecimal subtotal,

        @Schema(example = "1.00")
        BigDecimal taxaFrete,

        @Schema(example = "11.00")
        BigDecimal valorTotal,

        @Schema(example = "CRIADO")
        StatusPedido statusPedido,

        @Schema(example = "2023-08-28T22:27:33Z")
        OffsetDateTime dataCriacao,

        @Schema(example = "2023-08-28T22:27:33Z")
        OffsetDateTime dataConfirmacao,

        RestaurantePedidoDto restaurante,

        UsuarioDto cliente
) {

    public PedidoListDto(Pedido pedido) {
        this(pedido.getId(), pedido.getSubtotal(), pedido.definirFrete(),  pedido.calcularValorTotal(),
                pedido.getStatusPedido(), pedido.getDataCriacao(), pedido.getDataConfirmacao(),
                new  RestaurantePedidoDto(pedido.getRestaurante()), new UsuarioDto(pedido.getCliente()));
    }

    public static List<PedidoListDto> converterLista(Collection<Pedido> listaPedidos){
        return listaPedidos.stream().map(PedidoListDto::new).collect(Collectors.toList());
    }
}
