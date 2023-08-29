package com.apigaworks.algafood.domain.model;

import com.apigaworks.algafood.domain.dto.cidade.CidadeUpdateDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Cidade {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(example = "1")
    private Long id;

    @Schema(example = "São Paulo")
    private String nome;

    @ManyToOne
    @JsonIgnoreProperties(value = {"cidade", "nome"}, allowGetters = true)
    private Estado estado;

    public Cidade(String nome, Estado estado) {
        this.nome = nome;
        this.estado = estado;
    }

    public Cidade(CidadeUpdateDto cidade) {
        this.id = cidade.id();
    }

    public Cidade(Long cidadeId) {
        this.id = cidadeId;
    }
}
