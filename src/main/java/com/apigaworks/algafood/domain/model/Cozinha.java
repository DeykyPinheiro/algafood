package com.apigaworks.algafood.domain.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;



    @JsonIgnore
    @JsonIgnoreProperties("cozinha")
    @OneToMany(mappedBy = "cozinha", cascade = CascadeType.REMOVE)
    private List<Restaurante> restauranteList;

    public Cozinha(String nome) {
        this.nome = nome;
    }


    public Cozinha(long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
