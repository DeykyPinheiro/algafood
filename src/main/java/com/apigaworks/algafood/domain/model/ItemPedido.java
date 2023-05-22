package com.apigaworks.algafood.domain.model;

import com.apigaworks.algafood.domain.dto.itempedido.ItemPedidoPedidoSaveDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantidade;

    private BigDecimal precoUnitario;

    private BigDecimal precoTotal;

    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY)
    private Produto produto;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pedido pedido;


    public ItemPedido(ItemPedidoPedidoSaveDto pedido) {
        this.id = pedido.produtoId();
        this.quantidade = pedido.quantidade();
        this.observacao = pedido.observacao();
    }

    public  static List<ItemPedido> converterLista(Collection<ItemPedidoPedidoSaveDto> lista){
        return  lista.stream().map(ItemPedido::new).collect(Collectors.toList());
    }

    public void calcularPrecoTotal() {
        BigDecimal precoUnitario = this.getPrecoUnitario();
        Integer quantidade = this.getQuantidade();

        if (precoUnitario == null) {
            precoUnitario = BigDecimal.ZERO;
        }

        if (quantidade == null) {
            quantidade = 0;
        }

        this.setPrecoTotal(precoUnitario.multiply(new BigDecimal(quantidade)));
    }

}



