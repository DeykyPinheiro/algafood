package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.api.openapi.controller.OpenApiCidadeController;
import com.apigaworks.algafood.common.security.CheckSecurity;
import com.apigaworks.algafood.domain.model.Cidade;
import com.apigaworks.algafood.domain.service.CidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cidades")
public class CidadeController implements OpenApiCidadeController {


    @Autowired
    private CidadeService cidadeService;

    @Override
    @GetMapping("/{id}")
    @CheckSecurity.Cidade.PodeConsultar
    public Cidade buscar(@PathVariable Long id) {
        return cidadeService.buscarOuFalhar(id);
    }

    @Override
    @GetMapping
    @CheckSecurity.Cidade.PodeConsultar
    public List<Cidade> listar() {
        return cidadeService.listar();
    }

    @Override
    @PostMapping
    @CheckSecurity.Cidade.PodeEditar
    public Cidade adicionar(@RequestBody Cidade cidade) {
        return cidadeService.salvar(cidade);
    }

    //    padrao na hora de atualizar é receber o id no path e os
//    dados a serem atualizados todos no body
    @Override
    @PutMapping("/{id}")
    @CheckSecurity.Cidade.PodeEditar
    public Cidade atualizar(@PathVariable Long id, @RequestBody Cidade cidade) {
        cidade = cidadeService.atualizar(id, cidade);
        return cidade;
    }

    @Override
    @DeleteMapping("/{id}")
    @CheckSecurity.Cidade.PodeEditar
    public void excluir(@PathVariable Long id) {
        cidadeService.remover(id);
    }


    //    tratando exception dentro do controlador, apenas requisicoes ref
    //    a cidadeController
//    @ExceptionHandler(EntidadeNaoEncontradaException.class)
//    public ResponseEntity<?> tratarEntidadeNaoEncontradaException(EntidadeNaoEncontradaException e) {
//
////        usando a classe que eu criei para retonar problemas personalizados
//        Problema problema = Problema.builder()
//                .dataHora(LocalDateTime.now())
//                .mensagem(e.getMessage()).build();
//
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(problema);
//
////        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//////                .body(e.getMessage());
//    }
//
//    //    tratamento especifico de negocio
//    @ExceptionHandler(NegocioException.class)
//    public ResponseEntity<?> tratarNegocioException(NegocioException e) {
//
//        Problema problema = Problema.builder()
//                .dataHora(LocalDateTime.now())
//                .mensagem(e.getMessage()).build();
//
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(problema);
//
////        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//////                .body(e.getMessage());
//    }


}
