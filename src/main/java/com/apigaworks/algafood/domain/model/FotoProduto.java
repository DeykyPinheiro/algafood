package com.apigaworks.algafood.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.util.Lazy;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class FotoProduto {
    @Id
    @Column(name = "produto_id")
    private  Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId //diz que é mapeada pelo id
    private Produto produto;

    private String nomeArquivo;

    private String descricao;

    private String contentType;

    private Long tamanho;
}
