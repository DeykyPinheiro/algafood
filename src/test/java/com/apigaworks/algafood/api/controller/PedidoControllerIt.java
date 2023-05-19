package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.domain.model.ItemPedido;
import com.apigaworks.algafood.domain.model.Pedido;
import com.apigaworks.algafood.domain.model.Produto;
import com.apigaworks.algafood.domain.repository.PedidoRepository;
import com.apigaworks.algafood.domain.repository.ProdutoRespository;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class PedidoControllerIt {

    public static final String CAMINHO_RELATIVO = "src/test/java/com/apigaworks/algafood/json";

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRespository produtoRespository;


    @BeforeEach
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/pedidos";

        databaseCleaner.clearTables();
        prepararDados();
    }

    @Test
    void deveRetornarStatus200_QuandoListarPedidos() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value());
    }

//


    private void prepararDados() {
//        Produto produto1 = new Produto("produto1", "esse é o produto1", new BigDecimal("10"), true);
//        Produto produto2 = new Produto("produto2", "esse é o produto2", new BigDecimal("20"), true);
//        produto1 = produtoRespository.save(produto1);
//        produto2 = produtoRespository.save(produto2);
//
//        ItemPedido itemPedidoA = new ItemPedido(2, produto1.getPreco(), p);
//        ItemPedido itemPedidob = new ItemPedido();
//        List<ItemPedido> listaItensPedidos = new ArrayList<>();
//
//
//
//        Pedido pedido = new Pedido(new BigDecimal("10.5"), new BigDecimal("10.5"), new BigDecimal("10.5"), new Date(), new Date(), null, new Date(), );
    }


    // -[]    consulta e listagem de pedidos em PEDIDOS
}
