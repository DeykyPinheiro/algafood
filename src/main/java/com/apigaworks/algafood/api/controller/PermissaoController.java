package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.api.openapi.controller.OpenApiPermissaoController;
import com.apigaworks.algafood.common.security.CheckSecurity;
import com.apigaworks.algafood.domain.dto.permissao.PermissaoDto;
import com.apigaworks.algafood.domain.dto.permissao.PermissaoListDto;
import com.apigaworks.algafood.domain.dto.permissao.PermissaoSaveDto;
import com.apigaworks.algafood.domain.dto.permissao.PermissaoUpdateDto;
import com.apigaworks.algafood.domain.model.Permissao;
import com.apigaworks.algafood.domain.service.PermissaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissoes")
public class PermissaoController implements OpenApiPermissaoController {

    @Autowired
    private PermissaoService permissaoService;


    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CheckSecurity.UsuarioGruposPermissoes.PodeEditar
    public PermissaoDto salvar(@RequestBody @Valid PermissaoSaveDto permissaoDto) {
        return permissaoService.salvar(permissaoDto);
    }

    @Override
    @GetMapping("/{permissaoId}")
    @CheckSecurity.UsuarioGruposPermissoes.PodeConsultar
    public PermissaoDto buscar(@PathVariable Long permissaoId) {
        return permissaoService.buscarOuFalhar(permissaoId);
    }

    @Override
    @GetMapping
    @CheckSecurity.UsuarioGruposPermissoes.PodeConsultar
    public List<PermissaoListDto> listar() {
        return permissaoService.listar();
    }

    @Override
    @PutMapping("/{permissaoId}")
    @CheckSecurity.UsuarioGruposPermissoes.PodeEditar
    public PermissaoDto atualizar(@PathVariable Long permissaoId, @RequestBody @Valid PermissaoUpdateDto permissaoDto) {
        return permissaoService.atualizar(permissaoId, permissaoDto);
    }

    @Override
    @DeleteMapping("/{permissaoId}")
    @CheckSecurity.UsuarioGruposPermissoes.PodeEditar
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long permissaoId) {
        permissaoService.remover(permissaoId);
    }


}
