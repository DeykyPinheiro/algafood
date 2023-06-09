package com.apigaworks.algafood.domain.model;

import com.apigaworks.algafood.common.validation.TaxaFrete;
import com.apigaworks.algafood.common.validation.ValorZeroIncluiDescricao;
import com.apigaworks.algafood.domain.dto.restaurante.RestauranteUpdateDto;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "restaurante_usuario_responsavel",
            joinColumns = @JoinColumn(name = "restaurante_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private Set<Usuario> listaUsuario = new HashSet<>();


    //    TaxaFrete foi usado apenas para exemplo
//    @TaxaFrete
//    Multiplo tbm nao tem necessidade
//    vai ser apenas usado como exemplo
//    @Multiplo(numero = 5)
    @TaxaFrete
    @NotNull
    private BigDecimal taxaFrete;

    private Boolean ativo = Boolean.TRUE;

    //    isso Boolean.TRUE faz com que o padrao seja true, sem precisar incluir nos teste
    private Boolean aberto = Boolean.TRUE;

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
//    mudei para Set pq o set é uma collection que nao aceita objetos iguais
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "restaurante_forma_pagamento",
            joinColumns = {@JoinColumn(name = "restaurante_id")},
            inverseJoinColumns = {@JoinColumn(name = "forma_pagamento_id")})
    private Set<FormaPagamento> formasPagamento = new HashSet<>();

    @Embedded
//    @JsonIgnore
    private Endereco endereco;

    //    @JsonIgnore
    @OneToMany(mappedBy = "restaurante", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Produto> listaProdutos;

    //    @JsonIgnore
    @OneToMany(mappedBy = "restaurante", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Pedido> listaPedidos;


    public Restaurante(Long id, String nome, Cozinha cozinha) {
        this.id = id;
        this.nome = nome;
        this.cozinha = cozinha;
    }

    public Restaurante(String nome, BigDecimal taxaFrete, long cozinhaId) {
        this.taxaFrete = taxaFrete;
        this.nome = nome;
        this.cozinha.setId(cozinhaId);
    }

    public Restaurante(RestauranteUpdateDto data) {
        this.nome = data.nome();
        this.taxaFrete = data.taxaFrete();
        this.cozinha = new Cozinha(data.cozinha());
        this.endereco = new Endereco(data.endereco());
    }

    public Restaurante(String nome, BigDecimal taxaFrete) {
        this.nome = nome;
        this.taxaFrete = taxaFrete;
    }

    public Restaurante(Long id) {
        this.id = id;
    }

    public void ativar() {
        setAtivo(true);
    }

    public void desativar() {
        setAtivo(false);
    }

    public void associarFormaPagamento(FormaPagamento formaPagamento) {
        this.getFormasPagamento().add(formaPagamento);
    }

    public void desassociarFormaPagamento(FormaPagamento formaPagamento) {
        this.getFormasPagamento().remove(formaPagamento);
    }

    public void adicionarProduto(Produto produto) {
        this.getListaProdutos().add(produto);
    }

    public void removerProduto(Produto produto) {
        this.getListaProdutos().remove(produto);
    }

    public void abrirRestaurante() {
        setAberto(true);
    }

    public void fecharRestaurante() {
        setAberto(false);
    }

//    associacoes podem ser feitas assim, contudo, tem que ter
//    @ManyToMany
//      @JoinTable(name = "restaurante_usuario_responsavel",
//      joinColumns = @JoinColumn(name = "restaurante_id"),
//      inverseJoinColumns = @JoinColumn(name = "usuario_id"))
//    na model que esta fazendo a associacao, nao sei o pq

    public void associarUsuario(Usuario usuario) {
        this.getListaUsuario().add(usuario);
    }

    public void desassociarUsuario(Usuario usuario) {
        this.getListaUsuario().remove(usuario);
    }

    public Boolean ehFormaPagamentoAceita(FormaPagamento formaPagamento) {
        return getFormasPagamento().contains(formaPagamento);
    }

    public Boolean produtoExisteNoRestaurante(Produto produto){
        return this.listaProdutos.contains(produto);
    }
}
