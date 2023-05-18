package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.domain.model.Grupo;
import com.apigaworks.algafood.domain.model.Permissao;
import com.apigaworks.algafood.domain.repository.GrupoRepository;
import com.apigaworks.algafood.domain.repository.PermissaoRepository;
import com.apigaworks.algafood.domain.service.GrupoService;
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
import org.yaml.snakeyaml.events.Event;

import static com.apigaworks.algafood.util.ResourceUtils.getContentFromResource;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class GrupoControllerIT {

    public static final String CAMINHO_RELATIVO = "src/test/java/com/apigaworks/algafood/json";

    public static final int ID_GRUPO_NAO_EXISTENTE = 100;

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private PermissaoRepository permissaoRepository;

    private String jsonGrupoCorreto;

    private String jsonGrupoSemNome;

    private Grupo g1 = new Grupo("Gerente");

    private Permissao permissao1 = new Permissao("Professor", "é o professor");

    private int quantidadeGrupoCadastrados = 0;

    @BeforeEach
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/grupos";

        jsonGrupoCorreto = getContentFromResource(CAMINHO_RELATIVO +
                "/correto/grupo-teste.json");

        jsonGrupoSemNome = getContentFromResource(CAMINHO_RELATIVO +
                "/incorreto/grupo-sem-nome.json");


        databaseCleaner.clearTables();
        prepararDados();
    }


    @Test
    void deveRetornarStatus201_QuandoCadastrarGrupo() {
        RestAssured.given()
                .body(jsonGrupoCorreto)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("nome", equalTo("testeGrupo"));
    }

    @Test
    void deveRetornarStatus400_QuandoCadastrarGrupoSemNome() {
        RestAssured.given()
                .body(jsonGrupoSemNome)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void deveRetornarStatus200_QuandoListarGrupos() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deveRetornarQuantidadeIgualGruposCadastrados_QuandoListarGrupos() {
        given()
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .body("", hasSize(quantidadeGrupoCadastrados))
                .body("nome", hasItems("Gerente"));
    }

    @Test
    void deveRetornarCorpoEStatusCorreto_QuandoConsultarGrupoExistente() {
        given()
                .accept(ContentType.JSON)
                .pathParam("id", g1.getId())
                .when()
                .get("/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo(g1.getNome()));
    }


    @Test
    void deveRetornarRespostaEStatus404_QuandoConsultarGrupoInexistente() {
        given()
                .accept(ContentType.JSON)
                .pathParam("id", ID_GRUPO_NAO_EXISTENTE)
                .when()
                .get("/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void deveRetornarCorpoEStatusCorreto_QuandoAtualizarGrupoExistente() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("id", g1.getId())
                .body(jsonGrupoCorreto)
                .when()
                .put("/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo("testeGrupo"))
                .body("id", equalTo(g1.getId().intValue()));
    }

    @Test
    void deveRetornar204_QuandoExcluirUmGrupoValido() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", g1.getId())
                .when()
                .delete("/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void deveRetornar404_QuandoExcluirUmGrupoInexistente() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", ID_GRUPO_NAO_EXISTENTE)
                .when()
                .delete("/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void deveRetornarStatus200_QuandoListarAssociacoesNoGrupo() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("grupoId", g1.getId())
                .when()
                .get("/{grupoId}/permissoes")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deveRetornarStatus204_QuandoAssociarGrupoComPermissao(){
        RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("grupoId", g1.getId())
                .pathParam("permissaoId", permissao1.getId())
                .when()
                .put("/{grupoId}/permissoes/{permissaoId}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void deveRetornarStatus200_QuandoDesassociarGrupoComPermissao(){
        RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("grupoId", g1.getId())
                .pathParam("permissaoId", permissao1.getId())
                .when()
                .delete("/{grupoId}/permissoes/{permissaoId}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }


    private void prepararDados() {

        Grupo g2 = new Grupo("Vendedor");
        Grupo g3 = new Grupo("Secretária");
        Grupo g4 = new Grupo("Cadastrador");

//        Permissao permissao1 = new Permissao("Professor", "é o professor");
        Permissao permissao2 = new Permissao("teste", "é um teste");
        permissao1 = permissaoRepository.save(permissao1);
        permissao2 = permissaoRepository.save(permissao2);


        g1 = grupoRepository.save(g1);
        g2 = grupoRepository.save(g2);
        g3 = grupoRepository.save(g3);
        g4 = grupoRepository.save(g4);
//        grupoService.associarPermissao(permissao);

        quantidadeGrupoCadastrados = (int) grupoRepository.count();
    }
}
