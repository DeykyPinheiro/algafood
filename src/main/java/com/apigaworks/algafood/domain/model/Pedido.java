package com.apigaworks.algafood.domain.model;


import com.apigaworks.algafood.domain.enumerated.StatusPedido;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Pedido {

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


    @OneToMany(mappedBy = "pedido")
    private List<ItemPedido> itens;

    @Enumerated(EnumType.STRING) // adicionar se nao, erro de conversao
    private StatusPedido statusPedido;

    @Embedded
    private Endereco endereco;

    @ManyToOne
    @JoinColumn(name = "usuario_cliente_id")
    private Usuario cliente;

    @ManyToOne
    private Restaurante restaurante;

    @ManyToOne(fetch = FetchType.LAZY)
    private FormaPagamento formaPagamento;

    public Pedido(BigDecimal subtotal, BigDecimal taxaFrete, BigDecimal valorTotal, OffsetDateTime dataCriacao, OffsetDateTime dataConfirmacao, OffsetDateTime dataCancelamento, OffsetDateTime dataEntrega, List<ItemPedido> itens, StatusPedido statusPedido, Endereco endereco, Usuario cliente, Restaurante restaurante, FormaPagamento formaPagamento) {
        this.subtotal = subtotal;
        this.taxaFrete = taxaFrete;
        this.valorTotal = valorTotal;
        this.dataCriacao = dataCriacao;
        this.dataConfirmacao = dataConfirmacao;
        this.dataCancelamento = dataCancelamento;
        this.dataEntrega = dataEntrega;
        this.itens = itens;
        this.statusPedido = statusPedido;
        this.endereco = endereco;
        this.cliente = cliente;
        this.restaurante = restaurante;
        this.formaPagamento = formaPagamento;
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
}
