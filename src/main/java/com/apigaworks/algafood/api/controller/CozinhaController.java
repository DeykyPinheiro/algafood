package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.api.openapi.controller.OpenApiCozinhaController;
import com.apigaworks.algafood.common.security.CheckSecurity;
import com.apigaworks.algafood.common.security.PodeConsultarCozinhas;
import com.apigaworks.algafood.common.security.PodeEditarCozinhas;
import com.apigaworks.algafood.domain.model.Cozinha;
import com.apigaworks.algafood.domain.service.CozinhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController implements OpenApiCozinhaController {

    @Autowired
    private CozinhaService cozinhaService;

//    @PreAuthorize("isAuthenticated()")
//    @PodeConsultarCozinhas coloquei do jeito que ta em baixo
    @Override
    @CheckSecurity.Cozinhas.PodeConsultar
    @GetMapping
    public Page<Cozinha> listar(Pageable pageable) {
        return cozinhaService.listar(pageable);
    }

    @Override
    @CheckSecurity.Cozinhas.PodeConsultar
    @GetMapping("/{id}")
    public Cozinha buscar(@PathVariable Long id) {
        return cozinhaService.buscarOuFalhar(id);
    }

    @Override
    @CheckSecurity.Cozinhas.PodeEditar
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cozinha salvar(@RequestBody Cozinha cozinha) {
        return cozinhaService.salvar(cozinha);
    }

    @Override
    @CheckSecurity.Cozinhas.PodeEditar
    @PutMapping("/{id}")
    public Cozinha atualizar(@PathVariable Long id, @RequestBody Cozinha cozinha) {
//        faco isso apra verificar se a cozinha existe e caso nao, estouro uma exception
        cozinhaService.buscarOuFalhar(id);
        return cozinhaService.atualizar(id, cozinha);
    }

    @Override
    @CheckSecurity.Cozinhas.PodeEditar
    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        cozinhaService.remover(id);
    }


//    TESTE DAS AULAS DE JPA


    //    para query params é só receber o parametro na funcao do controller
//    que é passada pelo path e recebida automaticamente e feito o bind pelo spring
//    caso nao tenha o spring é só usar o @RequestParam  e colocar o nome do paramento
//    o bind ta feito
    @Override
    @GetMapping("/search")
    public List<String> search(String dia, String mes, String ano) {
        List<String> params = new ArrayList<>();
        params.add(dia);
        params.add(mes);
        params.add(ano);
        return params;
    }

    @Override
    @GetMapping("/primeiro")
    public Cozinha buscarPrimeiro() {
        return cozinhaService.buscarPrimeiro();
    }
}
