package com.apigaworks.algafood.domain.model;

import com.apigaworks.algafood.domain.dto.formaPagamento.FormaPagamentoDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class FormaPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @ManyToMany(mappedBy = "formasPagamento")
    private List<Restaurante> listaRestaurantes;

    @OneToMany(mappedBy = "formaPagamento")
    private List<Pedido> listaPedidos;

    public FormaPagamento(String descricao) {
        this.descricao = descricao;
    }

    public FormaPagamento(FormaPagamentoDto formaPagamento) {
        this.descricao = formaPagamento.descricao();
    }

    public FormaPagamento(Long formaPagamentoId) {
        this.id = formaPagamentoId;
    }
}
