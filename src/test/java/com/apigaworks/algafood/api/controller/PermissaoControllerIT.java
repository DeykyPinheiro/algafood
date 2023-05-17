package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.domain.model.Permissao;
import com.apigaworks.algafood.domain.repository.PermissaoRepository;
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
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class PermissaoControllerIT {

    public static final String CAMINHO_RELATIVO = "src/test/java/com/apigaworks/algafood/json";

    public static final int ID_GRUPO_NAO_EXISTENTE = 100;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private PermissaoRepository permissaoRepository;

    @LocalServerPort
    private int port;

    private int quantidadePermissaoCadastrados = 0;

    private String jsonPermissaoCorreto;

    private String jsonPermissaoAtualizacaoCorreto;

    private Permissao p1 = new Permissao("p1", "essa é a p1");
    private Permissao p2 = new Permissao("p2", "essa é a p2");
    private Permissao p3 = new Permissao("p3", "essa é a p3");


    @BeforeEach
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/permissoes";

        jsonPermissaoCorreto = getContentFromResource(CAMINHO_RELATIVO +
                "/correto/permissao-correta.json");

        jsonPermissaoAtualizacaoCorreto = getContentFromResource(CAMINHO_RELATIVO +
                "/correto/permissao-atualizacao-correta.json");

        databaseCleaner.clearTables();
        prepararDados();
    }

    @Test
    void deveRetornarStatus201_QuandoCadastrarPermissao() {
        RestAssured.given()
                .body(jsonPermissaoCorreto)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void deveRetornarStatuEcorpoCorreto_QuandoBuscarPorIdValido(){
        given()
                .contentType(ContentType.JSON)
                .pathParam("permissaoId", p1.getId())
                .when()
                .get("/{permissaoId}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo(p1.getNome()))
                .body("descricao", equalTo(p1.getDescricao()));
    }

    @Test
    void deveRetornarCorpoEStatusCorreto_QuandoCadastrarPermissao() {
        RestAssured.given()
                .body(jsonPermissaoCorreto)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("nome", equalTo("permissao nome teste"))
                .body("descricao", equalTo("permissao descricao teste"));
    }

    @Test
    void deveRetornarStatus200_QuandoListarPermissoes() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deveRetornarQuantidadeIgualPermissoesCadastrados_QuandoListarPermissoes() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("", hasSize(quantidadePermissaoCadastrados));
    }

    @Test
    void deveRetornarCorpoEStatusCorreto_QuandoAtualizarPermissao() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(jsonPermissaoAtualizacaoCorreto)
                .pathParam("permissaoId", p1.getId())
                .when()
                .put("/{permissaoId}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo("atualizacao"))
                .body("descricao", equalTo("atualizacao descricao"));
    }

    @Test
    void deveRetornarStatus204_QuandoExcluirPermissaoValida() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("permissaoId", p1.getId())
                .when()
                .delete("/{permissaoId}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }


    private void prepararDados() {
        p1 = permissaoRepository.save(p1);
        p2 = permissaoRepository.save(p2);
        p3 = permissaoRepository.save(p3);

        quantidadePermissaoCadastrados = (int) permissaoRepository.count();
    }


}
