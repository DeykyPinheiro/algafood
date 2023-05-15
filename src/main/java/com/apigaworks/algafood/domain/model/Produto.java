package com.apigaworks.algafood.domain.model;

import com.apigaworks.algafood.domain.dto.produto.ProdutoDto;
import com.apigaworks.algafood.domain.dto.produto.ProdutoSaveDto;
import com.apigaworks.algafood.domain.dto.produto.ProdutoUpdateDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String descricao;

    private BigDecimal preco;

    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;

    public Produto(ProdutoSaveDto produto) {
        this.nome = produto.nome();
        this.descricao = produto.descricao();
        this.preco = produto.preco();
        this.ativo = produto.ativo();
    }

    public Produto(String nome, String descricao, BigDecimal preco, boolean ativo) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.ativo = ativo;
    }

    public Produto(ProdutoUpdateDto produto) {
        this.nome = produto.nome();
        this.descricao = produto.descricao();
        this.preco = produto.preco();
        this.ativo = produto.ativo();
    }

    public Produto(ProdutoDto produto) {
        this.id = produto.id();
        this.nome = produto.nome();
        this.descricao = produto.descricao();
        this.preco = produto.preco();
        this.ativo = produto.ativo();
    }
//    @ManyToOne
//    private ItemPedido itemPedido;
}
