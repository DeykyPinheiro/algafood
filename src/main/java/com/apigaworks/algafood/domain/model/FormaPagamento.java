package com.apigaworks.algafood.domain.model;

import com.apigaworks.algafood.domain.dto.formaPagamento.FormaPagamentoDTO;
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

    public FormaPagamento(FormaPagamentoDTO formaPagamento) {
        this.descricao = formaPagamento.descricao();
    }
}
