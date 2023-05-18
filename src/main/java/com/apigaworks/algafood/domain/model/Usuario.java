package com.apigaworks.algafood.domain.model;

import com.apigaworks.algafood.domain.dto.usuario.UsuarioDto;
import com.apigaworks.algafood.domain.dto.usuario.UsuarioSaveDto;
import com.apigaworks.algafood.domain.dto.usuario.UsuarioUpdateDto;
import com.apigaworks.algafood.domain.dto.usuario.UsuarioUpdateSenhaDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    private String senha;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime dataCadastro;

    @ManyToMany
    @JoinTable(name = "usuario_grupo",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "grupo_id"))
    private Set<Grupo> listaGrupos;

//    @ManyToMany
//    @JoinTable(name = "restaurante_usuario_responsavel",
//            joinColumns = @JoinColumn(name = "usuario_id"),
//            inverseJoinColumns = @JoinColumn(name = "restaurante_id"))
    @ManyToMany(mappedBy = "listaUsuario")
    private Set<Restaurante> listaResraurantes;


    @OneToMany(mappedBy = "cliente")
    private List<Pedido> listaPedidos;

    public Usuario(UsuarioSaveDto usuarioDto) {
        this.nome = usuarioDto.nome();
        this.email = usuarioDto.email();
        this.senha = usuarioDto.senha();
    }

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public Usuario(UsuarioDto usuario) {
        this.id = usuario.id();
        this.nome = usuario.nome();
        this.email = usuario.email();
    }

    public Usuario(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public Usuario(UsuarioUpdateDto usuario) {
        this.nome = usuario.nome();
        this.email = usuario.email();
    }

    public Usuario(UsuarioUpdateSenhaDto usuario) {
        this.senha = usuario.novaSenha();
    }

    public void associarGrupo(Grupo grupo) {
        this.getListaGrupos().add(grupo);
    }

    public void desassociarGrupo(Grupo grupo) {
        this.getListaGrupos().remove(grupo);
    }
}
