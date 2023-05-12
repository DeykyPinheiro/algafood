package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.domain.model.Usuario;
import com.apigaworks.algafood.domain.repository.UsuarioRepository;
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

    @Autowired
    private UsuarioRepository usuarioRepository;

    private String jsonUsuarioCorreto;

    private String jsonUsuarioSemEmail;

    private String jsonUsuarioSemNome;

    private String jsonUsuarioSemSenha;

    private String jsonUsuarioVazio;

    private String jsonUsuarioEmailInvalido;

    private String jsonUsuarioAtualizadoComSenha;

    private String jsonUsuarioAtualizadoValido;

    private String jsonUsuarioUpdateSenhaValido;

    private String jsonUsuarioUpdateSenhaInvalido;

    private String jsonusuarioUpdateEmailInvalido;


    Usuario u4 = new Usuario("u4", "email4@email.com", "12345674");

    private int quantidadeUsuariosCadastrados = 0;

    @BeforeEach
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/usuarios";


        jsonUsuarioCorreto = getContentFromResource(CAMINHO_RELATIVO +
                "/correto/usuario-correto.json");

        jsonUsuarioSemEmail = getContentFromResource(CAMINHO_RELATIVO +
                "/incorreto/usuario-sem-email.json");

        jsonUsuarioSemNome = getContentFromResource(CAMINHO_RELATIVO +
                "/incorreto/usuario-sem-nome.json");

        jsonUsuarioSemSenha = getContentFromResource(CAMINHO_RELATIVO +
                "/incorreto/usuario-sem-senha.json");

        jsonUsuarioVazio = getContentFromResource(CAMINHO_RELATIVO +
                "/incorreto/usuario-vazio.json");

        jsonUsuarioEmailInvalido = getContentFromResource(CAMINHO_RELATIVO +
                "/incorreto/usuario-com-email-invalido.json");

        jsonUsuarioAtualizadoComSenha = getContentFromResource(CAMINHO_RELATIVO +
                "/incorreto/usuario-atualizado-com-senha.json");

        jsonUsuarioAtualizadoValido = getContentFromResource(CAMINHO_RELATIVO +
                "/correto/usuario-atualizado-valido.json");

        jsonUsuarioUpdateSenhaValido = getContentFromResource(CAMINHO_RELATIVO +
                "/correto/usuario-update-senha-valido.json");

        jsonUsuarioUpdateSenhaInvalido = getContentFromResource(CAMINHO_RELATIVO +
                "/incorreto/usuario-update-senha-invalido.json");


        jsonusuarioUpdateEmailInvalido = getContentFromResource(CAMINHO_RELATIVO +
                "/incorreto/usuario-update-email-invalido.json");

        databaseCleaner.clearTables();
        prepararDados();
    }

    @Test
    void deveRetornar201_QuandoCadatrarUsuarioCorreto() {
        RestAssured.given()
                .body(jsonUsuarioCorreto)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void deveRetornarCorpoSemSenha_QuandoCadastrarUsuarioCorreto() {
        RestAssured.given()
                .body(jsonUsuarioCorreto)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .body("nome", equalTo("jose"))
                .body("email", equalTo("email@email"))
                .body("$", not(hasKey("senha")));
//                serve para verificar que nao existe no body
    }


    @Test
    void deveRetornarStatus400_QuandoCadatrarUsuarioSemEmail() {
        RestAssured.given()
                .body(jsonUsuarioSemEmail)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void deveRetornarStatus400_QuandoCadatrarUsuarioSemNome() {
        RestAssured.given()
                .body(jsonUsuarioSemNome)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void deveRetornarStatus400_QuandoCadatrarUsuarioSemSenha() {
        RestAssured.given()
                .body(jsonUsuarioSemSenha)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void deveRetornarStatus400_QuandoCadatrarUsuarioVazio() {
        RestAssured.given()
                .body(jsonUsuarioVazio)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void deveRetornarStatus400_QuandoCadatrarUsuarioComEmailInvalido() {
        RestAssured.given()
                .body(jsonUsuarioEmailInvalido)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }


    @Test
    void deveRetornarStatus200_QuandoListarUsuario() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deveRetornarStatus200_QuandoBuscarUsuarioPorId() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .pathParam("id", u4.getId())
                .when()
                .get("/{id}")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deveRetornarCorpoCorretoSemSenha_QuandoBuscarUsuarioPorId() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .pathParam("id", u4.getId())
                .when()
                .get("/{id}")
                .then()
                .body("nome", equalTo(u4.getNome()))
                .body("email", equalTo(u4.getEmail()))
                .body("$", not(hasKey("senha")));
//                serve para verificar que nao existe no body
    }

    @Test
    void deveRetornarStatus404_QuandoBuscarUsuarioPorIdInexistente() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .pathParam("id", ID_USUARIO_NAO_EXISTENTE)
                .when()
                .get("/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void deveRetornarQuantidadeIgualDeUsuarioCadastrados_QuandoListarUsuario() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("", hasSize(quantidadeUsuariosCadastrados))
                .body("nome", hasItems("u1", "u2", "u3", "u4"));
    }

    @Test
    void deveRetornarStatus204_QuandoExcluirUmUsuarioValido() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("id", u4.getId())
                .when()
                .delete("/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void deveRetornarStatus200_QuandoAtualizarUsuarioValido() {
        RestAssured.given()
                .body(jsonUsuarioAtualizadoValido)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("id", u4.getId())
                .when()
                .put("/{id}")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deveRetornarStatus400_QuandoAtualizarUsuarioComEmailInvalido() {
        RestAssured.given()
                .body(jsonusuarioUpdateEmailInvalido)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .pathParam("id", u4.getId())
                .put("/{id}")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void deveRetornarCorpoValido_QuandoAtualizarUsuarioValido() {
        RestAssured.given()
                .body(jsonUsuarioAtualizadoValido)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("id", u4.getId())
                .when()
                .put("/{id}")
                .then()
                .body("nome", equalTo("update"))
                .body("email", equalTo("atualizado@email"))
                .body("$", not(hasKey("senha")));
    }

    @Test
    void deveRetornarStatus400_QuandoAtualizarUsuarioComSenha() {
        RestAssured.given()
                .body(jsonUsuarioAtualizadoComSenha)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("id", u4.getId())
                .when()
                .put("/{id}")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void deveRetornarStatus204_QuandoAtualizarSenhaDoUsuarioValido() {
        RestAssured.given()
                .body(jsonUsuarioUpdateSenhaValido)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("id", u4.getId())
                .when()
                .put("/{id}/senha")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void deveRetornarStatus400_QuandoSenhaAtualDiferenteDaSenhaDoUsuario() {
        RestAssured.given()
                .body(jsonUsuarioUpdateSenhaValido)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("id", u4.getId())
                .when()
                .put("/{id}/senha")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }


    private void prepararDados() {

        Usuario u1 = new Usuario("u1", "email1@email.com", "12345678");
        Usuario u2 = new Usuario("u2", "email2@email.com", "12345679");
        Usuario u3 = new Usuario("u3", "email3@email.com", "12345670");

        usuarioRepository.save(u1);
        usuarioRepository.save(u2);
        usuarioRepository.save(u3);
        usuarioRepository.save(u4);


        quantidadeUsuariosCadastrados = (int) usuarioRepository.count();
    }

}
