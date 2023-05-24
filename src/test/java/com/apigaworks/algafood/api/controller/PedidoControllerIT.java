package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.domain.dto.endereco.EnderecoPedidoDto;
import com.apigaworks.algafood.domain.dto.formaPagamento.FormaPagamentoDto;
import com.apigaworks.algafood.domain.dto.itempedido.ItemPedidoPedidoSaveDto;
import com.apigaworks.algafood.domain.dto.pedido.PedidoSaveDto;
import com.apigaworks.algafood.domain.dto.produto.ProdutoSaveDto;
import com.apigaworks.algafood.domain.dto.restaurante.RestaurantePedidoDto;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.apigaworks.algafood.util.ResourceUtils.getContentFromResource;
import static org.hamcrest.Matchers.*;

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

    @Autowired
    private PedidoService pedidoService;

    private int quatidadePedidosCadastrados = 0;

    private Long pedidoId;

    PedidoSaveDto pedidoDto;

    private String jsonPedidoValido;

    Estado estado = new Estado("Brasiliariouuu");

    Produto produto1;

    Produto produto2;

    FormaPagamento formaPagamento1 = new FormaPagamento("pix");

    private Usuario usuario1 = new Usuario("nome1", "email1", "12345678");

    private Restaurante restaurante = new Restaurante("Restaurante", new BigDecimal("10.0"));

    ItemPedidoPedidoSaveDto itemPedidoA;

    ItemPedidoPedidoSaveDto itemPedidoB;

    Float precoTotalItemA;

    Float precoTotalItemB;

    Float valorTotalPedido;

    Float subtotalPedido;

    Locale locale = Locale.US;

    DecimalFormat decimalFormat = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(locale));


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

//   -[] TODO falta validar o corpo na criacao de um pedido

    @Test
    void deveRetornarQuantidadeIgualAPedidosCadastrados_QuandoListarPedidos() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .body("", hasSize(quatidadePedidosCadastrados));
    }

    @Test
    void deveRetornarRespostaEStatus_QuandoConsultarPedidoExistente() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .pathParam("pedidoId", pedidoId)
                .when()
                .get("/{pedidoId}")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    //    TODO
//    terminar de testar todasa as hipoteses
    @Test
    void deveRetornarRespostaComItensSalvosValidos_QuandoConsultarPedidoExistente() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .pathParam("pedidoId", pedidoId)
                .when()
                .get("/{pedidoId}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(pedidoId.intValue()))
                .body("itens.produtoNome", hasItems(produto1.getNome(), produto2.getNome()))
                .body("itens.produtoNome", hasItems(produto1.getNome(), produto2.getNome()))
                .body("itens.quantidade", hasItems(itemPedidoA.quantidade(), itemPedidoB.quantidade()))
                .body("itens[0].precoUnitario", equalTo(produto1.getPreco().floatValue()))
                .body("itens[1].precoUnitario", equalTo(produto2.getPreco().floatValue()))
                .body("itens[1].precoUnitario", equalTo(produto2.getPreco().floatValue()))
                .body("itens[0].observacao", equalTo(itemPedidoA.observacao()))
                .body("itens[1].observacao", equalTo(itemPedidoB.observacao()))
                .body("itens[0].precoTotal", equalTo(precoTotalItemA))
                .body("itens[1].precoTotal", equalTo(precoTotalItemB))
                .body("subtotal", equalTo(subtotalPedido))
                .body("taxaFrete", equalTo(restaurante.getTaxaFrete().floatValue()))
                .body("valorTotal", equalTo(valorTotalPedido))
                .body("restaurante.id", equalTo((restaurante.getId().intValue())))
                .body("restaurante.nome", equalTo((restaurante.getNome())))
                .body("formaPagamento.id", equalTo((formaPagamento1.getId().intValue())))
                .body("formaPagamento.descricao", equalTo((formaPagamento1.getDescricao())))
                .body("cliente.id", equalTo((usuario1.getId().intValue())))
                .body("cliente.nome", equalTo((usuario1.getNome())))
                .body("cliente.email", equalTo((usuario1.getEmail())));
    }

//    TODO TESTAR com uma casa decimal e com duas
//    descobri que isso só acontece no teste pq do hasItem faz uma conversao
//    JSON path itens.precoUnitario doesn't match.
//    Expected: (a collection containing "10.50" and a collection containing "20.10")
//    Actual: <[10.5, 20.1]>


//


    private void prepararDados() {
        UsuarioSaveDto usuarioDto = new UsuarioSaveDto(usuario1);
        Long userId = usuarioService.salvar(usuarioDto).id();
        usuario1 = usuarioRepository.findById(userId).get();

        FormaPagamentoDto formaPagamentoDto = new FormaPagamentoDto(formaPagamento1);
        Long formaPagamentoId = formaPagamentoService.salvar(formaPagamentoDto).id();
        formaPagamento1 = formaPagamentoRepository.findById(formaPagamentoId).get();


        estado = estadoService.salvar(estado);
        Cidade cidade = new Cidade("cidade teste", estado);
        cidade = cidadeService.salvar(cidade);


        Cozinha cozinhaBrasileira = new Cozinha("Brasileira");
        cozinhaBrasileira = cozinhaService.salvar(cozinhaBrasileira);


        restaurante.setCozinha(cozinhaBrasileira);
        restaurante.setTaxaFrete(new BigDecimal("10.09"));
        restaurante.associarFormaPagamento(formaPagamento1);

        restaurante = restauranteService.salvar(restaurante);

        produto1 = new Produto("produto1", "descricao produto1", new BigDecimal("10.51"), true);
        Long produtoId1 = produtoService.salvar(restaurante.getId(), new ProdutoSaveDto(produto1)).id();
        produto1 = produtoRespository.findById(produtoId1).get();

        produto2 = new Produto("produto2", "descricao produto2", new BigDecimal("20.1"), true);
        Long produtoId2 = produtoService.salvar(restaurante.getId(), new ProdutoSaveDto(produto2)).id();
        produto2 = produtoRespository.findById(produtoId2).get();


        itemPedidoA = new ItemPedidoPedidoSaveDto(produtoId1, 10, null);
        itemPedidoB = new ItemPedidoPedidoSaveDto(produtoId2, 20, "com bastante cebola");


        List<ItemPedidoPedidoSaveDto> listaPedidos = new ArrayList<>();
        listaPedidos.add(itemPedidoA);
        listaPedidos.add(itemPedidoB);


        EnderecoPedidoDto enderecoDto = new EnderecoPedidoDto("040000000", "rua dos testes", "404", "apt", "vilas dos erros", cidade.getId());
        pedidoDto = new PedidoSaveDto(restaurante.getId(), formaPagamentoId, enderecoDto, listaPedidos);

        pedidoId = pedidoService.salvar(pedidoDto).id();
        Pedido pedido = pedidoRepository.findById(pedidoId).get();

//        colocar o decimal com duas casas no max
        decimalFormat.setMaximumFractionDigits(2);

        String aux = decimalFormat.format((itemPedidoA.quantidade() * produto1.getPreco().floatValue()));
        precoTotalItemA = Float.parseFloat(aux);

        aux = decimalFormat.format((itemPedidoB.quantidade() * produto2.getPreco().floatValue()));
        precoTotalItemB = Float.parseFloat(aux);

        subtotalPedido = precoTotalItemA + precoTotalItemB;
        valorTotalPedido = subtotalPedido.floatValue() + restaurante.getTaxaFrete().floatValue();

        quatidadePedidosCadastrados = (int) pedidoRepository.count();
    }


    // -[]    consulta e listagem de pedidos em PEDIDOS
}
