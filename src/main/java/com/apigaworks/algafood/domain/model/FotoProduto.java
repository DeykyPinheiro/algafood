package com.apigaworks.algafood.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class FotoProduto {

    @Column(name = "nome_arquivo_foto")
    private String nomeArquivo = "sem arquivo";

    @Column(name = "content_type_foto")
    private String contentType = "sem arquivo";

    @Column(name = "tamanho_foto")
    private Long tamanho = 0L;


}
