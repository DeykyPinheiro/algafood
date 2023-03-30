package com.apigaworks.algafood.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table
@Data
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private BigDecimal taxaFrete;

    private Boolean ativo;

    private Boolean aberto;

    @JsonIgnore
    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private Date dataCadastro;

    @JsonIgnore
    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private Date dataAtualizacao;


    @ManyToOne
    @JsonIgnoreProperties("restaurante")
    private Cozinha cozinha;

    @ManyToMany
    @JoinTable(name = "restaurante_forma_pagamento",
            joinColumns = {@JoinColumn(name = "restaurante_id")},
            inverseJoinColumns = {@JoinColumn(name = "forma_pagamento_id")})
    private List<FormaPagamento> formasPagamento ;

    @Embedded
    @JsonIgnore
    private Endereco endereco;

    @OneToMany(mappedBy = "restaurante")
    private List<Produto> produtos ;

}
