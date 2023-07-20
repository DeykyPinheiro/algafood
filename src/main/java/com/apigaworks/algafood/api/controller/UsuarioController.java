package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.common.security.CheckSecurity;
import com.apigaworks.algafood.domain.dto.grupo.GrupoListDto;
import com.apigaworks.algafood.domain.dto.usuario.*;
import com.apigaworks.algafood.domain.model.Usuario;
import com.apigaworks.algafood.domain.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @CheckSecurity.UsuarioGruposPermissoes.PodeEditar
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDto salvar(@RequestBody @Valid UsuarioSaveDto usuario) {
        return usuarioService.salvar(usuario);
    }

    @GetMapping
    @CheckSecurity.UsuarioGruposPermissoes.PodeConsultar
    public List<UsuarioListDto> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    @CheckSecurity.UsuarioGruposPermissoes.PodeConsultar
    public UsuarioDto buscar(@PathVariable Long id) {
        return usuarioService.buscarOuFalhar(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        usuarioService.remover(id);
    }

    @PutMapping("/{id}")
    @CheckSecurity.UsuarioGruposPermissoes.PodeAlterarUsuario
    public UsuarioDto atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioUpdateDto usuario){
        return usuarioService.atualizar(id, usuario);
    }

    @PutMapping("/{id}/senha")
    @CheckSecurity.UsuarioGruposPermissoes.PodeAlterarPropriaSenha
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarSenha(@PathVariable Long id, @RequestBody @Valid UsuarioUpdateSenhaDto usuario){
        usuarioService.atualizarSenha(id, usuario);
    }

    @GetMapping("/{userId}/grupos")
    @CheckSecurity.UsuarioGruposPermissoes.PodeConsultar
    public List<GrupoListDto> listarGruposPorUsuario(@PathVariable Long userId){
        return usuarioService.listarGruposPorUsuario(userId);
    }

    @PutMapping("/{userId}/grupos/{grupoId}")
    @CheckSecurity.UsuarioGruposPermissoes.PodeEditar
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associarGrupo(@PathVariable Long userId, @PathVariable Long grupoId){
        usuarioService.associarGrupo(userId, grupoId);
    }

    @DeleteMapping("/{userId}/grupos/{grupoId}")
    @CheckSecurity.UsuarioGruposPermissoes.PodeEditar
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desassociarGrupo(@PathVariable Long userId, @PathVariable Long grupoId){
        usuarioService.desassociarGrupo(userId, grupoId);
    }
}
