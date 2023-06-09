package com.apigaworks.algafood.domain.model;


import com.apigaworks.algafood.domain.dto.itempedido.ItemPedidoPedidoSaveDto;
import com.apigaworks.algafood.domain.dto.pedido.PedidoSaveDto;
import com.apigaworks.algafood.domain.enumerated.StatusPedido;
import com.apigaworks.algafood.domain.event.PedidoCanceladoEvent;
import com.apigaworks.algafood.domain.event.PedidoConfirmadoEvent;
import com.apigaworks.algafood.domain.exception.NegocioException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
//AbstractAggregateRoot serve para disparar eventos
public class Pedido extends AbstractAggregateRoot<Pedido> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal subtotal;

    private BigDecimal taxaFrete;

    private BigDecimal valorTotal;

    @JsonIgnore
    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private OffsetDateTime dataCriacao;

    @JsonIgnore
    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private OffsetDateTime dataConfirmacao;

    @JsonIgnore
    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private OffsetDateTime dataCancelamento;

    @JsonIgnore
    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private OffsetDateTime dataEntrega;


    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemPedido> itens = new ArrayList<>();

    @Enumerated(EnumType.STRING) // adicionar se nao, erro de conversao
    private StatusPedido statusPedido = StatusPedido.CRIADO;

    @Embedded
    private Endereco endereco;

    @ManyToOne
    @JoinColumn(name = "usuario_cliente_id")
    private Usuario cliente;

    @ManyToOne
    private Restaurante restaurante;

    @ManyToOne(fetch = FetchType.LAZY)
    private FormaPagamento formaPagamento;

    public Pedido(PedidoSaveDto pedidoDto) {
        this.restaurante = new Restaurante(pedidoDto.restauranteId());
        this.formaPagamento = new FormaPagamento(pedidoDto.formaPagamentoId());
        this.endereco = new Endereco(pedidoDto.enderecoEntrega());
        this.itens = ItemPedido.converterLista(pedidoDto.itens());
    }

    public BigDecimal definirFrete() {
        setTaxaFrete(getRestaurante().getTaxaFrete());
        return taxaFrete;
    }

    public void atribuirPedidoAosItens() {
        getItens().forEach(item -> item.setPedido(this));
    }


    public BigDecimal calcularValorTotal() {
        this.subtotal = getItens().stream()
                .map(item -> item.getPrecoTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.valorTotal = this.subtotal.add(this.taxaFrete);
        return valorTotal;
    }

    public void adicionarItens(List<ItemPedido> itensPedidos) {
        itensPedidos.forEach(item -> {
            getItens().add(item);
        });
    }

    public void confirmarPedido() {
        setStatusPedido(StatusPedido.CONFIRMADO);
        setDataConfirmacao(OffsetDateTime.now());
//        aqui só registramos o evento e tudo necessario junto
//        this é a instancia atual do pedido chamado
        registerEvent(new PedidoConfirmadoEvent(this));
    }

    public void entregarPedido() {
        setStatusPedido(StatusPedido.ENTREGUE);
        setDataEntrega(OffsetDateTime.now());
    }

    public void cancelarPedido() {
        setStatusPedido(StatusPedido.CANCELADO);
        setDataCancelamento(OffsetDateTime.now());
        registerEvent(new PedidoCanceladoEvent(this));
    }

    private void setStatusPedido(StatusPedido novoStatus) {
        if (getStatusPedido().naoPodeAlterarStatus(novoStatus)) {
            throw new NegocioException(
                    String.format("nao pode mudar status do pedido de %s para %s",
                            getStatusPedido(), novoStatus.getDescricao()));
        }
        this.statusPedido = novoStatus;
    }


}
