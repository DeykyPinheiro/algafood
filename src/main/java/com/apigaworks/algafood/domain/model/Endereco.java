package com.apigaworks.algafood.domain.model;

import com.apigaworks.algafood.domain.dto.endereco.EndecoUpdateDto;
import com.apigaworks.algafood.domain.dto.endereco.EnderecoPedidoDto;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Embeddable
@Data
public class Endereco {

    @Column(name = "endereco_cep")
    private String cep;
    @Column(name = "endereco_logradouro")
    private String logradouro;
    @Column(name = "endereco_numero")
    private String numero;
    @Column(name = "endereco_complemento")
    private String complemento;
    @Column(name = "endereco_bairro")
    private String bairro;
    @ManyToOne
    @JoinColumn(name = "endereco_cidade_id")
    private Cidade cidade;

    public Endereco(EndecoUpdateDto endereco) {
        this.cep = endereco.cep();
        this.logradouro = endereco.logradouro();
        this.numero = endereco.numero();
        this.complemento = endereco.complemento();
        this.bairro = endereco.bairro();
        this.cidade = new Cidade(endereco.cidade());
    }

    public Endereco() {
    }

    public Endereco(EnderecoPedidoDto endereco) {
        this.cep = endereco.cep();
        this.logradouro = endereco.logradouro();
        this.numero = endereco.numero();
        this.complemento = endereco.complemento();
        this.bairro = endereco.bairro();
        this.cidade = new Cidade(endereco.cidadeId());
    }
}
