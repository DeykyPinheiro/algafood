package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.common.security.CheckSecurity;
import com.apigaworks.algafood.domain.dto.grupo.GrupoDto;
import com.apigaworks.algafood.domain.dto.grupo.GrupoListDto;
import com.apigaworks.algafood.domain.dto.grupo.GrupoSaveDto;
import com.apigaworks.algafood.domain.dto.grupo.GrupoUpdateDto;
import com.apigaworks.algafood.domain.dto.permissao.PermissaoListDto;
import com.apigaworks.algafood.domain.model.Grupo;
import com.apigaworks.algafood.domain.model.Permissao;
import com.apigaworks.algafood.domain.service.GrupoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grupos")
public class GrupoController implements com.apigaworks.algafood.api.openapi.controller.OpenApiGrupoController {

    @Autowired
    private GrupoService grupoService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CheckSecurity.UsuarioGruposPermissoes.PodeEditar
    public GrupoDto adicionar(@RequestBody @Valid GrupoSaveDto grupo) {
        return grupoService.salvar(grupo);
    }

    @Override
    @GetMapping
    @CheckSecurity.UsuarioGruposPermissoes.PodeConsultar
    public List<GrupoListDto> listar() {
        return grupoService.listar();
    }

    @Override
    @GetMapping("/{id}")
    @CheckSecurity.UsuarioGruposPermissoes.PodeConsultar
    public GrupoDto buscar(@PathVariable Long id) {
        return grupoService.buscarOuFalhar(id);
    }

    @Override
    @PutMapping("/{id}")
    @CheckSecurity.UsuarioGruposPermissoes.PodeEditar
    public GrupoDto atualizar(@PathVariable Long id, @RequestBody @Valid GrupoUpdateDto grupo) {
        GrupoDto g = grupoService.atualizar(id, grupo);
        return g;
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckSecurity.UsuarioGruposPermissoes.PodeEditar
    public void remover(@PathVariable Long id) {
        grupoService.remover(id);
    }

    @Override
    @GetMapping("/{grupoId}/permissoes")
    public List<PermissaoListDto> listaPermissoesPorGrupo(@PathVariable Long grupoId) {
        return grupoService.listaPermissoesPorGrupo(grupoId);
    }

    @Override
    @PutMapping("/{grupoId}/permissoes/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associarPermissao(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
        grupoService.associarPermissao(grupoId, permissaoId);
    }

    @Override
    @DeleteMapping("/{grupoId}/permissoes/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desassociarPermissao(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
        grupoService.desassociarPermissao(grupoId, permissaoId);
    }


}
