package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.domain.model.Cozinha;
import com.apigaworks.algafood.domain.service.CozinhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

    @Autowired
    private CozinhaService cozinhaService;

    @GetMapping
    public List<Cozinha> listar() {
        List<Cozinha> a = cozinhaService.listar();
        return a;
    }

    @GetMapping("/{id}")
    public Cozinha buscar(@PathVariable Long id) {
        return cozinhaService.buscarOuFalhar(id);
    }

    @PostMapping
    public Cozinha salvar(@RequestBody Cozinha cozinha) {
        return cozinhaService.salvar(cozinha);
    }

    @PutMapping
    public Cozinha atualizar(@RequestBody Cozinha cozinha) {
//        faco isso apra verificar se a cozinha existe e caso nao, estouro uma exception
        Cozinha c = cozinhaService.buscarOuFalhar(cozinha.getId());
        return cozinhaService.atualizar(c);
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        cozinhaService.remover(id);
    }


//    TESTE DAS AULAS DE JPA


    //    para query params é só receber o parametro na funcao do controller
//    que é passada pelo path e recebida automaticamente e feito o bind pelo spring
//    caso nao tenha o spring é só usar o @RequestParam  e colocar o nome do paramento
//    o bind ta feito
    @GetMapping("/search")
    public List<String> search(String dia, String mes, String ano) {
        List<String> params = new ArrayList<>();
        params.add(dia);
        params.add(mes);
        params.add(ano);
        return params;
    }

    @GetMapping("/primeiro")
    public Cozinha buscarPrimeiro() {
        return cozinhaService.buscarPrimeiro();
    }
}
