package com.apigaworks.algafood.domain.dto.pedido;

import com.apigaworks.algafood.domain.dto.endereco.EnderecoDto;
import com.apigaworks.algafood.domain.dto.formaPagamento.FormaPagamentoDto;
import com.apigaworks.algafood.domain.dto.itempedido.ItemPedidoPedidoDto;
import com.apigaworks.algafood.domain.dto.restaurante.RestaurantePedidoDto;
import com.apigaworks.algafood.domain.dto.usuario.UsuarioDto;
import com.apigaworks.algafood.domain.enumerated.StatusPedido;
import com.apigaworks.algafood.domain.model.Pedido;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public record PedidoDto(

        @Schema(example = "1")
        Long id,

        @Schema(example = "20.00")
        BigDecimal subtotal,

        @Schema(example = "1.00")
        BigDecimal taxaFrete,

        @Schema(example = "21.00")
        BigDecimal valorTotal,

        @Schema(example = "CRIADO")
        StatusPedido statusPedido,

        @Schema(example = "2023-08-28T22:27:33Z")
        OffsetDateTime dataCriacao,

        @Schema(example = "2023-08-28T22:27:33Z")
        OffsetDateTime dataConfirmacao,

        @Schema(example = "2023-08-28T22:27:33Z")
        OffsetDateTime dataCancelamento,

        RestaurantePedidoDto restaurante,

        UsuarioDto cliente,

        FormaPagamentoDto formaPagamento,

        EnderecoDto endereco,

        List<ItemPedidoPedidoDto> itens

) {

//    TODO nao esquecer colocar valores dinamicos
    public PedidoDto(Pedido pedido) {
        this(pedido.getId(),  pedido.getSubtotal(),pedido.getTaxaFrete(),  pedido.getValorTotal(),
                pedido.getStatusPedido(), pedido.getDataCriacao(), pedido.getDataConfirmacao(), pedido.getDataCancelamento(),
                new  RestaurantePedidoDto(pedido.getRestaurante()), new UsuarioDto(pedido.getCliente()),
                new FormaPagamentoDto(pedido.getFormaPagamento()), new EnderecoDto(pedido.getEndereco()),
                ItemPedidoPedidoDto.converterLista(pedido.getItens()));
    }

    public static List<PedidoDto> converterLista(Collection<Pedido> listaPedidos){
        return listaPedidos.stream().map(PedidoDto::new).collect(Collectors.toList());
    }
}
