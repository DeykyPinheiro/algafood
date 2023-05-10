package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.util.DatabaseCleaner;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import static com.apigaworks.algafood.util.ResourceUtils.getContentFromResource;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class UsuarioControllerIT {

    public static final String CAMINHO_RELATIVO = "src/test/java/com/apigaworks/algafood/json";

    public static final int ID_USUARIO_NAO_EXISTENTE = 100;

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    private String jsonUsuarioCorreto;

    private int quantidadeUsuariosCadastrados = 0;

    @BeforeEach
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/usuarios";


        jsonUsuarioCorreto = getContentFromResource(CAMINHO_RELATIVO +
                "/correto/usuario-correto.json");


        databaseCleaner.clearTables();
        prepararDados();
    }

    @Test
    void deveRetornar201_QuandoCadatrarUsuario() {
        RestAssured.given()
                .body(jsonUsuarioCorreto)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("nome", equalTo("jose"))
                .body("email", equalTo("email@email"))
                .body("$", not(hasKey("senha")));
//                serve para verificar que nao existe no body
    }


    private void prepararDados() {
    }

}
