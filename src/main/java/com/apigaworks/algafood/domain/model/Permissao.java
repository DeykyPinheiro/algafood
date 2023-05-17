package com.apigaworks.algafood.domain.model;

import com.apigaworks.algafood.domain.dto.permissao.PermissaoDto;
import com.apigaworks.algafood.domain.dto.permissao.PermissaoSaveDto;
import com.apigaworks.algafood.domain.dto.permissao.PermissaoUpdateDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Permissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String descricao;

    @ManyToMany(mappedBy = "listaPermissao")
    private Set<Grupo> listaGrupos;

    public Permissao(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public Permissao(PermissaoSaveDto permissaoDto) {
        this(permissaoDto.nome(), permissaoDto.descricao());
    }

    public Permissao(PermissaoDto permissaoDto) {
        this.id = permissaoDto.id();
        this.nome = permissaoDto.nome();
        this.descricao = permissaoDto.descricao();
    }

    public Permissao(PermissaoUpdateDto permissaoDto) {
        this.nome = permissaoDto.nome();
        this.descricao = permissaoDto.descricao();
    }
}
