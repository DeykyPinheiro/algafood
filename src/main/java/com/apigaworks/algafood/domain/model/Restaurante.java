package com.apigaworks.algafood.domain.model;

import com.apigaworks.algafood.core.validation.Multiplo;
import com.apigaworks.algafood.core.validation.TaxaFrete;
import com.apigaworks.algafood.core.validation.ValorZeroIncluiDescricao;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ValorZeroIncluiDescricao(valorField = "taxaFrete", descricaoField = "nome",
        descricaoObrigatoria = "Frete Gratis")
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;


    //    TaxaFrete foi usado apenas para exemplo
//    @TaxaFrete
//    Multiplo tbm nao tem necessidade
//    vai ser apenas usado como exemplo
    @Multiplo(numero = 5)
    @TaxaFrete
    @NotNull
    private BigDecimal taxaFrete;

    private Boolean ativo;

    private Boolean aberto;

    public Restaurante(Long id, String nome, Cozinha cozinha) {
        this.id = id;
        this.nome = nome;
        this.cozinha = cozinha;
    }

    public Restaurante(String nome, BigDecimal taxaFrete, long cozinhaId) {
        this.taxaFrete = taxaFrete;
        this.nome = nome;
        this.cozinha.setId(cozinhaId) ;
    }


    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private Date dataCadastro;


    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private Date dataAtualizacao;


//    isso serve pra ignorar o nome que vem da origem restaraunte
//    na serializacao, na desserializacao, ele mostra, ou seja
//    nao ignora
//    @JsonIgnoreProperties(value = "nome", allowGetters = true)
    @Valid
    @NotNull
    @ManyToOne
    private Cozinha cozinha;

//    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "restaurante_forma_pagamento",
            joinColumns = {@JoinColumn(name = "restaurante_id")},
            inverseJoinColumns = {@JoinColumn(name = "forma_pagamento_id")})
    private List<FormaPagamento> formasPagamento;

    @Embedded
//    @JsonIgnore
    private Endereco endereco;

//    @JsonIgnore
    @OneToMany(mappedBy = "restaurante", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Produto> produtos;

//    @JsonIgnore
    @OneToMany(mappedBy = "restaurante", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Pedido> listaPedidos;


}
