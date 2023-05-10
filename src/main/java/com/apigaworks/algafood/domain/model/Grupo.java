package com.apigaworks.algafood.domain.model;

import com.apigaworks.algafood.domain.dto.grupo.GrupoDto;
import com.apigaworks.algafood.domain.dto.grupo.GrupoSaveDto;
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
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToMany
    @JoinTable(name = "grupo_permissao",
            joinColumns = @JoinColumn(name = "grupo_id"),
            inverseJoinColumns = @JoinColumn(name = "permissao_id"))
    private List<Permissao> listaPermissao;

    @ManyToMany(mappedBy = "listaGrupos")
    private List<Usuario> listaUsuario;

    public Grupo(GrupoSaveDto grupo) {
        this.nome = grupo.nome();
    }

    public Grupo(String nome) {
        this.nome = nome;
    }

    public Grupo(GrupoDto grupo) {
        this.id = grupo.id();
        this.nome = grupo.nome();
    }
}
