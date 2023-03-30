package com.apigaworks.algafood.domain.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class Cozinha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @OneToMany(mappedBy = "cozinha", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties("cozinha")
    private List<Restaurante> restauranteList;

    public Cozinha(String nome) {
        this.nome = nome;
    }


}
