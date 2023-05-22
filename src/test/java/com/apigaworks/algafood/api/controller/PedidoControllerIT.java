package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.domain.dto.formaPagamento.FormaPagamentoDto;
import com.apigaworks.algafood.domain.dto.produto.ProdutoDto;
import com.apigaworks.algafood.domain.dto.produto.ProdutoSaveDto;
import com.apigaworks.algafood.domain.dto.usuario.UsuarioDto;
import com.apigaworks.algafood.domain.dto.usuario.UsuarioSaveDto;
import com.apigaworks.algafood.domain.model.*;
import com.apigaworks.algafood.domain.repository.*;
import com.apigaworks.algafood.domain.service.*;
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
import java.util.jar.JarOutputStream;

import static com.apigaworks.algafood.util.ResourceUtils.getContentFromResource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class PedidoControllerIT {

    public static final String CAMINHO_RELATIVO = "src/test/java/com/apigaworks/algafood/json";

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRespository produtoRespository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CidadeService cidadeService;

    @Autowired
    private EstadoService estadoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private CozinhaService cozinhaService;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    private FormaPagamentoService formaPagamentoService;




    private String jsonPedidoValido;

    Estado estado = new Estado("Brasiliariouuu");

    FormaPagamento formaPagamento1 = new FormaPagamento("pix");

    private Usuario usuario1 = new Usuario("nome1", "email1", "12345678");

    private Restaurante restaurante = new Restaurante("Restaurante", new BigDecimal("10.0"));


    @BeforeEach
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/pedidos";


        jsonPedidoValido = getContentFromResource(CAMINHO_RELATIVO +
                "/correto/pedido-valido.json");

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

    @Test
    void deveRetornarStatus204_QuandoCadastrarUmPedidoValido() {
        RestAssured.given()
                .body(jsonPedidoValido)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

//


    private void prepararDados() {
        UsuarioSaveDto usuarioDto = new UsuarioSaveDto(usuario1);
        Long userId = usuarioService.salvar(usuarioDto).id();
        Usuario usuario = usuarioRepository.findById(userId).get();

        FormaPagamentoDto formaPagamentoDto = new FormaPagamentoDto(formaPagamento1);
        Long formaPagamentoId = formaPagamentoService.salvar(formaPagamentoDto).id();
        formaPagamento1 = formaPagamentoRepository.findById(formaPagamentoId).get();


        estado = estadoService.salvar(estado);
        Cidade cidade = new Cidade("cidade teste", estado);
        cidade = cidadeService.salvar(cidade);


        Cozinha cozinhaBrasileira = new Cozinha("Brasileira");
        cozinhaBrasileira = cozinhaService.salvar(cozinhaBrasileira);




        restaurante.setCozinha(cozinhaBrasileira);
        restaurante.setTaxaFrete(new BigDecimal("10.99"));
        restaurante.associarFormaPagamento(formaPagamento1);

        restaurante = restauranteService.salvar(restaurante);

        Produto produto1 = new Produto("produto1", "descricao produto1", new BigDecimal("10"),true);
        Long produtoId1 = produtoService.salvar(restaurante.getId(), new ProdutoSaveDto(produto1)).id();
        produto1 = produtoRespository.findById(produtoId1).get();

        Produto produto2 = new Produto("produto2", "descricao produto2", new BigDecimal("20"),true);
        Long produtoId2 = produtoService.salvar(restaurante.getId(), new ProdutoSaveDto(produto2)).id();
        produto2 = produtoRespository.findById(produtoId2).get();


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
