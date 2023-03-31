package com.apigaworks.algafood.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table
public class FormaPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @ManyToMany(mappedBy = "formasPagamento")
    private List<Restaurante> listaRestaurantes;

    @OneToMany
    private List<Pedido> listaPedidos;

}
