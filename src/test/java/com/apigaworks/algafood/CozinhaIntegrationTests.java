package com.apigaworks.algafood;

import com.apigaworks.algafood.domain.model.Cozinha;
import com.apigaworks.algafood.domain.service.CozinhaService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CozinhaIntegrationTests {

    @Autowired
    private CozinhaService cozinhaService;

    @Test
    void testarCadastroCozinha() {
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
    void testarCadastroCozinhaSemNome() {
        Cozinha c = new Cozinha();
        c.setNome(null);


//confirma a exception lancada
        ConstraintViolationException erroEsperado =
                Assertions.assertThrows(ConstraintViolationException.class, () -> {
                    cozinhaService.salvar(c);
                });
        Assertions.assertTrue(erroEsperado != null);
    }

}
