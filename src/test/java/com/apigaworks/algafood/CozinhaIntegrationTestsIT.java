package com.apigaworks.algafood;

import com.apigaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.apigaworks.algafood.domain.model.Cozinha;
import com.apigaworks.algafood.domain.service.CozinhaService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

//deixei esse aqui a titulo de exemplo
// de como pegas as excepition de um teste de integracao
// esse inclusive foi apagado no modulo do curso
@SpringBootTest
class CozinhaIntegrationTestsIT {

    @Autowired
    private CozinhaService cozinhaService;

    @Test
    void deveAtribuirId_QuandoCadastrarCozinhaComDadosCorretos() {
//		cenario
        Cozinha c = new Cozinha();
        c.setNome("teste");

//		acao
        c = cozinhaService.salvar(c);

//		validacao
        Assertions.assertTrue(c.getId() != null);
        Assertions.assertTrue(c != null);
    }

    @Test
    void deveFalhar_QuandoCadastrarCozinhaSemNome() {
        Cozinha c = new Cozinha();
        c.setNome(null);


//confirma a exception lancada
        ConstraintViolationException erroEsperado =
                Assertions.assertThrows(ConstraintViolationException.class, () -> {
                    cozinhaService.salvar(c);
                });
        Assertions.assertTrue(erroEsperado != null);
    }

    @Test
    void deveFalhar_QuandoExcluirCozinhaEmUso() {

        EntidadeEmUsoException erroEsperado =
                Assertions.assertThrows(EntidadeEmUsoException.class, () -> {
                    cozinhaService.remover(1L);
                });

        Assertions.assertTrue(erroEsperado != null);
    }

    @Test
    void deveFalhar_QuandoExcluirCozinhaInexistente() {

        NoSuchElementException erroEsperado =
                Assertions.assertThrows(NoSuchElementException.class, () -> {
                    cozinhaService.remover(100L);
                });

        Assertions.assertTrue(erroEsperado != null);
    }

}
