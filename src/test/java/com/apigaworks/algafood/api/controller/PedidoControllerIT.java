package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.common.io.Base64ProtocolResolver;
import com.apigaworks.algafood.domain.dto.endereco.EnderecoPedidoDto;
import com.apigaworks.algafood.domain.dto.formaPagamento.FormaPagamentoDto;
import com.apigaworks.algafood.domain.dto.itempedido.ItemPedidoPedidoSaveDto;
import com.apigaworks.algafood.domain.dto.pedido.PedidoSaveDto;
import com.apigaworks.algafood.domain.dto.produto.ProdutoSaveDto;
import com.apigaworks.algafood.domain.dto.usuario.UsuarioSaveDto;
import com.apigaworks.algafood.domain.enumerated.StatusPedido;
import com.apigaworks.algafood.domain.model.*;
import com.apigaworks.algafood.domain.repository.*;
import com.apigaworks.algafood.domain.service.*;
import com.apigaworks.algafood.util.DatabaseCleaner;
import com.apigaworks.algafood.util.UserLogin;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.apigaworks.algafood.util.ResourceUtils.getContentFromResource;
import static org.hamcrest.Matchers.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.JWT;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
@ContextConfiguration(initializers = Base64ProtocolResolver.class) //preciso de initializar quando adicionei na inicializacao
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

    private Long pedidoId1;

    PedidoSaveDto pedidoDto1;

    private Long pedidoId2;

    private Long pedidoId3;

    private Long pedidoId4;

    PedidoSaveDto pedidoDto2;

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


    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private PermissaoRepository permissaoRepository;


    private UserLogin login;

    private String tokenGer;

    private Jwt reverseTokenJwt(String jwt, int port){
        JwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri("http://localhost:" + port + "/oauth2/jwks").build();
        return jwtDecoder.decode(jwt);
    }

    @Autowired
    private RegisteredClientRepository registeredClientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() throws Exception {
        databaseCleaner.clearTables();

        RegisteredClient registeredClient = RegisteredClient
                .withId("5")
                .clientId("autorizationcode")
                .clientSecret(passwordEncoder.encode("123"))
                .scope("READ")
                .redirectUri("https://oidcdebugger.com/debug")
                .redirectUri("https://oauthdebugger.com/debug")
                .redirectUri("http://127.0.0.1:8080/swagger-ui/oauth2-redirect.html")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(new AuthorizationGrantType("custom_password"))
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .refreshTokenTimeToLive(Duration.ofMinutes(30))
                        .reuseRefreshTokens(false)
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .requireProofKey(false)
                        .build())
                .build();
        registeredClientRepository.save(registeredClient);

        login = new UserLogin(grupoRepository, permissaoRepository, usuarioService, usuarioRepository, grupoService);
        this.login.salvarUsuariosComGruposEPermissoes();
        tokenGer = login.logarGer(port);


        Jwt jwt = reverseTokenJwt(tokenGer, port);

        Authentication authentication = new UsernamePasswordAuthenticationToken(jwt,null);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);


        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/pedidos";


        jsonPedidoValido = getContentFromResource(CAMINHO_RELATIVO +
                "/correto/pedido-valido.json");



        prepararDados();
    }

    @Test
    void deveRetornarStatus200_QuandoListarPedidos() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer "+ tokenGer)
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
                .header("Authorization", "Bearer "+ tokenGer)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

//   -[] TODO falta validar o corpo na criacao de um pedido

//    como eu nao sei como pegar o numero da pagina que foi passada,
//    eu vou deixar esse teste de fora por um tempo, infelizmente =/
//    @Test
//    void deveRetornarQuantidadeIgualAPedidosCadastrados_QuandoListarPedidos() {
//        RestAssured.given()
//                .accept(ContentType.JSON)
//                .when()
//                .get()
//                .then()
//                .body("", hasSize(quatidadePedidosCadastrados));
//    }

    @Test
    void deveRetornarRespostaEStatus_QuandoConsultarPedidoExistente() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer "+ tokenGer)
                .pathParam("pedidoId", pedidoId1)
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
                .header("Authorization", "Bearer "+ tokenGer)
                .pathParam("pedidoId", pedidoId1)
                .when()
                .get("/{pedidoId}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(pedidoId1.intValue()))
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


    @Test
    void deveRetornarStatus204_QuandoAlterarStatusParaConfirmado() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer "+ tokenGer)
                .pathParam("pedidoId", pedidoId1)
                .when()
                .put("/{pedidoId}/confirmacao")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void deveRetornarStatus400_QuandoAlterarStatusParaConfirmado() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer "+ tokenGer)
                .pathParam("pedidoId", pedidoId2)
                .when()
                .put("/{pedidoId}/confirmacao")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void deveRetornarStatus204_QuandoAlterarDeConfimadoParaEntregue(){
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer "+ tokenGer)
                .pathParam("pedidoId", pedidoId2)
                .when()
                .put("/{pedidoId}/entrega")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void deveRetornarStatus400_QuandoAlterarDeCriadoParaEntregue(){
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer "+ tokenGer)
                .pathParam("pedidoId", pedidoId1)
                .when()
                .put("/{pedidoId}/entrega")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void deveRetornarStatus204_QuandoAlterarDeCriadoParaCancelado(){
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer "+ tokenGer)
                .pathParam("pedidoId", pedidoId1)
                .when()
                .put("/{pedidoId}/cancelamento")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    //    como agora posso cancelar se tiver confirmado, esse teste teve que ser comentado
//    @Test
//    void deveRetornarStatus404_QuandoAlterarDeConfirmadoParaCancelado(){
//        RestAssured.given()
//                .accept(ContentType.JSON)
//                .pathParam("pedidoId", pedidoId2)
//                .when()
//                .put("/{pedidoId}/cancelamento")
//                .then()
//                .statusCode(HttpStatus.NOT_FOUND.value());
//    }



    private void prepararDados()  {
//        this.login.salvarUsuariosComGruposEPermissoes();
//        tokenGer = login.logarGer(port);

//        UsuarioSaveDto usuarioDto = new UsuarioSaveDto(usuario1);
//        Long userId = usuarioService.salvar(usuarioDto).id();
        usuario1 = usuarioRepository.findById(1L).get();

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
        pedidoDto1 = new PedidoSaveDto(restaurante.getId(), formaPagamentoId, enderecoDto, listaPedidos);

        pedidoId1 = pedidoService.salvar(pedidoDto1).id();
        Pedido pedido = pedidoRepository.findById(pedidoId1).get();

        Pedido pedido2 =  pedidoRepository.findById(pedidoService.salvar(pedidoDto1).id()).get();
        pedido2.confirmarPedido();
        pedidoRepository.save(pedido2);
        pedidoId2 = pedido2.getId();

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


    // -[]    TODO consulta e listagem de pedidos em PEDIDOS
}
