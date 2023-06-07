package com.apigaworks.algafood.domain.model;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FotoProduto {

    @Column(name = "nome_arquivo_foto")
    private String nomeArquivo = "sem arquivo";

    @Column(name = "content_type_foto")
    private String contentType = "sem arquivo";

    @Column(name = "tamanho_foto")
    private Long tamanho = 0L;




    public FotoProduto(Produto produto) {
        this.nomeArquivo = produto.getFotoProduto().getNomeArquivo();
        this.contentType =  produto.getFotoProduto().contentType;
        this.tamanho =  produto.getFotoProduto().tamanho;
    }
}
