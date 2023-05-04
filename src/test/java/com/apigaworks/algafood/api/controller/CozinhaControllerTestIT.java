package com.apigaworks.algafood.api.controller;

import static io.restassured.RestAssured.*;

import com.apigaworks.algafood.domain.model.Cozinha;
import com.apigaworks.algafood.domain.repository.CozinhaRepository;
import com.apigaworks.algafood.util.DatabaseCleaner;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.client.methods.HttpTrace;
import org.flywaydb.core.Flyway;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;


//todos que estao nessa pasta serao usado como teste de API
//sobe um servidor aleatorio e usa a porta
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
class CozinhaControllerTestIT {

    //    essa anotacao injeta o numero da porta que esta sendo ultiliza  na variavel
    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @BeforeEach
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/cozinhas";

        databaseCleaner.clearTables();
        prepararDados();
    }

    @Test
    void deveRetornar200_QuandoConsultarCozinhas() {
        given()
                .accept(ContentType.JSON)
        .when()
                .get()
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deveRetornarCozinhas_QuandoConsultarCozinhas() {
        given()
                .accept(ContentType.JSON)
        .when()
                .get()
        .then()
                .body("", hasSize(2))
                .body("nome", hasItems("Indiana", "Tailandesa"));
    }

    @Test
    void deveRetornarStatus201_QuandoCadastrarCozinha(){
        given()
                .body("{\"nome\":\"chinesa\"}")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.CREATED.value());
    }


    private void prepararDados() {
        Cozinha cozinha1 = new Cozinha();
        cozinha1.setNome("Tailandesa");
        cozinhaRepository.save(cozinha1);

        Cozinha cozinha2 = new Cozinha();
        cozinha2.setNome("Indiana");
        cozinhaRepository.save(cozinha2);
    }
}

//  MESMAS CLASSES DE CIMA ESCRITA DE UMA OUTRA FORMA, UM POUCO MAIS SIMPLES
//    @Test
//    void deveRetornar200_QuandoConsultarCozinhas() {
//
////        isso habilita o log da resposta errada
////        dai mostra exatamente o que foi feito e a resposta tbm
//        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
//
////        quando acessar o endpoit, na porta, aceitando json
////        usado get
////        deverá retornar 200 status code
//        given()
//                .basePath("/cozinhas")
//                .port(port)
//                .accept(ContentType.JSON)
//                .when()
//                .get()
//                .then()
//                .statusCode(HttpStatus.OK.value());
//    }
//
//    @Test
//    void deveRetornar4Cozinhas_QuandoConsultarCozinhas() {
//        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
//
////        verificar se tenho 4 cozinhas
////        verificar se tenho os item indiana e tailandesa no campo de nome
//
//        given()
//                .basePath("/cozinhas")
//                .port(port)
//                .accept(ContentType.JSON)
//                .when()
//                .get()
//                .then()
//                .body("", hasSize(4))
//                .body("nome", hasItems("Indiana", "Tailandesa"));
//    }