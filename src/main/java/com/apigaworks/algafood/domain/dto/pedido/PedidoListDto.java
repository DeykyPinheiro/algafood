package com.apigaworks.algafood.domain.dto.pedido;

import com.apigaworks.algafood.domain.dto.endereco.EnderecoDto;
import com.apigaworks.algafood.domain.dto.formaPagamento.FormaPagamentoDto;
import com.apigaworks.algafood.domain.dto.itempedido.ItemPedidoPedidoDto;
import com.apigaworks.algafood.domain.dto.restaurante.RestaurantePedidoDto;
import com.apigaworks.algafood.domain.dto.usuario.UsuarioDto;
import com.apigaworks.algafood.domain.enumerated.StatusPedido;
import com.apigaworks.algafood.domain.model.Pedido;

import java.util.Collection;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record PedidoListDto(

        Long id,

        BigDecimal subtotal,

        BigDecimal valorTotal,

        StatusPedido statusPedido,

        OffsetDateTime dataCriacao,

        OffsetDateTime dataConfirmacao,

        OffsetDateTime dataCancelamento,

        RestaurantePedidoDto restaurante,

        UsuarioDto cliente,

        FormaPagamentoDto formaPagamento,

        EnderecoDto endereco,

        List<ItemPedidoPedidoDto> itens

) {

    public PedidoListDto(Pedido pedido) {
        this(pedido.getId(), pedido.getSubtotal(), pedido.getValorTotal(),
                pedido.getStatusPedido(), pedido.getDataCriacao(), pedido.getDataConfirmacao(), pedido.getDataCancelamento(),
                new  RestaurantePedidoDto(pedido.getRestaurante()), new UsuarioDto(pedido.getCliente()),
                new FormaPagamentoDto(pedido.getFormaPagamento()), new EnderecoDto(pedido.getEndereco()),
                ItemPedidoPedidoDto.converterLista(pedido.getItens()));
    }

    public static List<PedidoListDto> converterLista(Collection<Pedido> listaPedidos){
        return listaPedidos.stream().map(PedidoListDto::new).collect(Collectors.toList());
    }
}
