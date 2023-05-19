package com.apigaworks.algafood.domain.dto.pedido;

import com.apigaworks.algafood.domain.dto.endereco.EnderecoDto;
import com.apigaworks.algafood.domain.dto.formaPagamento.FormaPagamentoDto;
import com.apigaworks.algafood.domain.dto.itempedido.ItemPedidoPedidoDto;
import com.apigaworks.algafood.domain.dto.restaurante.RestaurantePedidoDto;
import com.apigaworks.algafood.domain.dto.usuario.UsuarioDto;
import com.apigaworks.algafood.domain.enumerated.StatusPedido;
import com.apigaworks.algafood.domain.model.Pedido;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public record PedidoDto(

        Long id,

        BigDecimal subtotal,

        BigDecimal taxaFrete,

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

    public PedidoDto(Pedido pedido) {
        this(pedido.getId(), pedido.getSubtotal(), pedido.definirFrete(), pedido.calcularValorTotal(),
                pedido.getStatusPedido(), pedido.getDataCriacao(), pedido.getDataConfirmacao(), pedido.getDataCancelamento(),
                new  RestaurantePedidoDto(pedido.getRestaurante()), new UsuarioDto(pedido.getCliente()),
                new FormaPagamentoDto(pedido.getFormaPagamento()), new EnderecoDto(pedido.getEndereco()),
                ItemPedidoPedidoDto.converterLista(pedido.getItens()));
    }

    public static List<PedidoDto> converterLista(Collection<Pedido> listaPedidos){
        return listaPedidos.stream().map(PedidoDto::new).collect(Collectors.toList());
    }
}
