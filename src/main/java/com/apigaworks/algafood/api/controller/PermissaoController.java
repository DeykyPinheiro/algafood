package com.apigaworks.algafood.api.controller;

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
public class PermissaoController {

    @Autowired
    private PermissaoService permissaoService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PermissaoDto salvar(@RequestBody @Valid PermissaoSaveDto permissaoDto) {
        return permissaoService.salvar(permissaoDto);
    }

    @GetMapping("/{permissaoId}")
    public PermissaoDto buscar(@PathVariable Long permissaoId) {
        return permissaoService.buscarOuFalhar(permissaoId);
    }

    @GetMapping
    public List<PermissaoListDto> listar() {
        return permissaoService.listar();
    }

    @PutMapping("/{permissaoId}")
    public PermissaoDto atualizar(@PathVariable Long permissaoId, @RequestBody @Valid PermissaoUpdateDto permissaoDto) {
        return permissaoService.atualizar(permissaoId, permissaoDto);
    }

    @DeleteMapping("/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long permissaoId) {
        permissaoService.remover(permissaoId);
    }


}
