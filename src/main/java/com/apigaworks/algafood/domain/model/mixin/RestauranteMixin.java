package com.apigaworks.algafood.domain.model.mixin;

import com.apigaworks.algafood.domain.model.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;


public class RestauranteMixin {

    @JsonIgnore
    private Date dataCadastro;

    @JsonIgnore
    private Date dataAtualizacao;


    @JsonIgnoreProperties(value = "nome", allowGetters = true)
    private Cozinha cozinha;

    @JsonIgnore
    private List<FormaPagamento> formasPagamento;

    @JsonIgnore
    private Endereco endereco;

    @JsonIgnore
    private List<Produto> produtos;

    @JsonIgnore
    private List<Pedido> listaPedidos;


}

