package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.domain.model.Cozinha;
import com.apigaworks.algafood.domain.model.Restaurante;
import com.apigaworks.algafood.domain.repository.CozinhaRepository;
import com.apigaworks.algafood.domain.repository.RestauranteRepository;
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

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class RestauranteControllerIT {

    public static final String CAMINHO_RELATIVO = "src/test/java/com/apigaworks/algafood/json";

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    private String jsonRestauranteCorreto;
    private String jsonRestauranteSemFrete;
    private String jsonRestauranteSemCozinha;
    private String jsonRestauranteComCozinhaInexistente;
    Restaurante comidaMineiraRestaurante = new Restaurante();

    private Restaurante burgerTopRestaurante;

    @BeforeEach
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/restaurantes";

        jsonRestauranteCorreto = getContentFromResource(CAMINHO_RELATIVO +
                "/correto/restaurante-new-york-barbecue.json");

        jsonRestauranteSemFrete = getContentFromResource(
                CAMINHO_RELATIVO + "/incorreto/restaurante-new-york-barbecue-sem-frete.json");

        jsonRestauranteSemCozinha = getContentFromResource(
                CAMINHO_RELATIVO + "/incorreto/restaurante-new-york-barbecue-sem-cozinha.json");

        jsonRestauranteComCozinhaInexistente = getContentFromResource(
                CAMINHO_RELATIVO + "/incorreto/restaurante-new-york-barbecue-com-cozinha-inexistente.json");

        databaseCleaner.clearTables();
        prepararDados();


    }

    @Test
    public void deveRetornarStatus200_QuandoConsultarRestaurantes() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void deveRetornarStatus201_QuandoCadastrarRestaurante() {
        given()
                .body(jsonRestauranteCorreto)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void deveRetornarStatus204_QuandoAbrirRestaurante() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .pathParam("id", comidaMineiraRestaurante.getId())
                .put("/{id}/abertura")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deveRetornarStatus204_QuandoFecharRestaurante() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .pathParam("id", comidaMineiraRestaurante.getId())
                .delete("/{id}/fechamento")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    private void prepararDados() {

        Cozinha cozinhaBrasileira = new Cozinha();
        cozinhaBrasileira.setNome("Brasileira");
        cozinhaRepository.save(cozinhaBrasileira);

        Cozinha cozinhaAmericana = new Cozinha();
        cozinhaAmericana.setNome("Americana");
        cozinhaRepository.save(cozinhaAmericana);

        burgerTopRestaurante = new Restaurante();
        burgerTopRestaurante.setNome("Burger Top");
        burgerTopRestaurante.setTaxaFrete(new BigDecimal(10));
        burgerTopRestaurante.setCozinha(cozinhaAmericana);
        restauranteRepository.save(burgerTopRestaurante);

//        Restaurante comidaMineiraRestaurante = new Restaurante();
        comidaMineiraRestaurante.setNome("Comida Mineira");
        comidaMineiraRestaurante.setTaxaFrete(new BigDecimal(10));
        comidaMineiraRestaurante.setCozinha(cozinhaBrasileira);
        restauranteRepository.save(comidaMineiraRestaurante);
    }


}