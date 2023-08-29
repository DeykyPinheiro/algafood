package com.apigaworks.algafood.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(example = "1")
    private Long id;

    @Schema(example = "São Paulo")
    private String nome;

    @JsonIgnore
    @OneToMany(mappedBy = "estado", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties("estado")
    private List<Cidade> cidadeList;

    public Estado(long id) {
        this.id = id;
    }

    public Estado(long id, String nome) {
        this.nome = nome;
        this.id = id;
    }

    public Estado(String nome) {
        this.nome = nome;
    }
}
