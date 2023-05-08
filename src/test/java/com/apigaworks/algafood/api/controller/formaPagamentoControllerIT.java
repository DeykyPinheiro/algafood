package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.domain.model.FormaPagamento;
import com.apigaworks.algafood.domain.repository.FormaPagamentoRepository;
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
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class formaPagamentoControllerIT {

    public static final String CAMINHO_RELATIVO = "src/test/java/com/apigaworks/algafood/json";

    public static final int ID_PAGAMENTO_NAO_EXISTENTE = 100;

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    private String jsonFormaPagamentoCorreto;


    private  int quantidadeFormaPagamentoCadastradas = 0;

    FormaPagamento formaPagamento1 = new FormaPagamento("pix");


    @BeforeEach
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/formaPagamentos";


        jsonFormaPagamentoCorreto = getContentFromResource(CAMINHO_RELATIVO +
                "/correto/pagamento-cartao.json");

        databaseCleaner.clearTables();
        prepararDados();
    }

    @Test
    void deveRetornarStatus200_QuandoConsultarFormaPagamentos(){
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("descricao", hasItems("pix"));
    }

    @Test
    void deveRetornarTotalDeFormaPagamentosCadastradas_QuandoConsultarCozinhas(){
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .body("", hasSize(quantidadeFormaPagamentoCadastradas));
    }

    @Test
    void deveRetornarRepostaEStatusCorretos_QuandoConsultarFormaPagamentoExistente(){
        given()
                .accept(ContentType.JSON)
                .pathParam("id", formaPagamento1.getId())
                .when()
                .get("/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("descricao", equalTo(formaPagamento1.getDescricao()));
    }

    @Test
    void deveRetornarRepostaEStatusCorretos_QuandoConsultarFormaPagamentoInexistente(){
        given()
                .accept(ContentType.JSON)
                .pathParam("id",ID_PAGAMENTO_NAO_EXISTENTE)
                .when()
                .get("/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void deveRetornarStatus201_QuandoCadastrarPagamento(){
        given()
                .body(jsonFormaPagamentoCorreto)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void deveRetornar204_QuandoExcluirUmaFormaPagamento(){
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", formaPagamento1.getId())
                .when()
                .delete("/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());


    }




    private void prepararDados() {
//        FormaPagamento formaPagamento = new FormaPagamento("pix");
        formaPagamentoRepository.save(formaPagamento1);

        quantidadeFormaPagamentoCadastradas = (int) formaPagamentoRepository.count();
    }




}
