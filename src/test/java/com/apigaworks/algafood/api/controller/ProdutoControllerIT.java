package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.common.io.Base64ProtocolResolver;
import com.apigaworks.algafood.domain.dto.produto.ProdutoSaveDto;
import com.apigaworks.algafood.domain.model.Cozinha;
import com.apigaworks.algafood.domain.model.Produto;
import com.apigaworks.algafood.domain.model.Restaurante;
import com.apigaworks.algafood.domain.repository.*;
import com.apigaworks.algafood.domain.service.GrupoService;
import com.apigaworks.algafood.domain.service.ProdutoService;
import com.apigaworks.algafood.domain.service.UsuarioService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.Duration;

import static com.apigaworks.algafood.util.ResourceUtils.getContentFromResource;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
@ContextConfiguration(initializers = Base64ProtocolResolver.class)
public class ProdutoControllerIT {

    public static final String CAMINHO_RELATIVO = "src/test/java/com/apigaworks/algafood/json";

    public static final int ID_PRODUTO_NAO_EXISTENTE = 100;

    public static final int ID_RESTAURANTE_NAO_EXISTENTE = 100;

    @LocalServerPort
    private int port;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ProdutoRespository produtoRespository;

    private  int quantidadeTotalCadastrados;

    private  int quantidadeAtivosCadastrados;



    private String jsonProdutoValido;

    private String jsonProdutoAtualizadoValido;

    private Restaurante restaurantePreCadastrado;

    Produto p1 = new Produto("p1", "esse é o p1", new BigDecimal("10.1"), true);

    Produto p2;

    Produto p3;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private PermissaoRepository permissaoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private GrupoService grupoService;

    private UserLogin login;

    private String tokenGer;

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


        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/restaurantes";

        jsonProdutoValido = getContentFromResource(CAMINHO_RELATIVO +
                "/correto/produto-valido.json");

        jsonProdutoAtualizadoValido = getContentFromResource(CAMINHO_RELATIVO +
                "/correto/produto-atualizado-valido.json");

        prepararDados();
        tokenGer = login.logarGer(port);
    }

    @Test
    void deveRetornarStatus201_QuandoCriarProduto() {
        RestAssured.given()
                .body(jsonProdutoValido)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .pathParam("idRestaurante", restaurantePreCadastrado.getId())
                .when()
                .post("/{idRestaurante}/produtos")
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }


    @Test
    void deveRetornarCorpoValido_QuandoCadastrarProdutoValido() {
        RestAssured.given()
                .body(jsonProdutoValido)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .pathParam("idRestaurante", restaurantePreCadastrado.getId())
                .when()
                .post("/{idRestaurante}/produtos")
                .then()
                .body("nome", equalTo("produto1"))
                .body("descricao", equalTo("isso é o produto1"))
                .body("preco", equalTo(10.50f))
                .body("ativo", equalTo(true));
    }

    @Test
    void deveRetornarStatus200_QuandoListarProdutos() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .pathParam("idRestaurante", restaurantePreCadastrado.getId())
                .when()
                .get("/{idRestaurante}/produtos")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deveRetornarQuantidadeIgualProdutosCadastrados_QuandoListarProdutos(){
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .pathParam("idRestaurante", restaurantePreCadastrado.getId())
                .when()
                .get("/{idRestaurante}/produtos")
                .then()
                .body("", hasSize(quantidadeAtivosCadastrados));
    }

    @Test
    void deveRetornarCorpoEStatusCorreto_QuandoAtualizarProdutoExistente(){
        RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .body(jsonProdutoAtualizadoValido)
                .pathParam("idRestaurante", restaurantePreCadastrado.getId())
                .pathParam("idProduto", p1.getId())
                .when()
                .put("/{idRestaurante}/produtos/{idProduto}")
                .then()
                .body("nome", equalTo("atualizado"))
                .body("descricao", equalTo("isso é o atualizado"));
    }

    @Test
    void deveRetornarStatus204_QuadoExcluirUmProdutoValido(){
        RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .pathParam("idRestaurante", restaurantePreCadastrado.getId())
                .pathParam("idProduto", p1.getId())
                .when()
                .delete("/{idRestaurante}/produtos/{idProduto}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void deveRetornarProdutoValido_QuandoPesquisarProdutoPorRestaurante(){
        RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .pathParam("idRestaurante", restaurantePreCadastrado.getId())
                .pathParam("idProduto", p1.getId())
                .when()
                .get("/{idRestaurante}/produtos/{idProduto}")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deveLancarRetornarStatus404_QuandoPesquisarProdutoPorRestauranteInexistente(){
        RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .pathParam("idRestaurante", ID_PRODUTO_NAO_EXISTENTE)
                .pathParam("idProduto", p1.getId())
                .when()
                .get("/{idRestaurante}/produtos/{idProduto}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }



    private void prepararDados() {

        this.login.salvarUsuariosComGruposEPermissoes();

//        criacao de restaurante
        Cozinha cozinhaBrasileira = new Cozinha();
        cozinhaBrasileira.setNome("Brasileira");
        cozinhaRepository.save(cozinhaBrasileira);
        Restaurante comidaMineiraRestaurante = new Restaurante();
        comidaMineiraRestaurante.setNome("Comida Mineira");
        comidaMineiraRestaurante.setTaxaFrete(new BigDecimal(10));
        comidaMineiraRestaurante.setCozinha(cozinhaBrasileira);
        comidaMineiraRestaurante.setAberto(true);
        restaurantePreCadastrado = restauranteRepository.save(comidaMineiraRestaurante);


        p2 = new Produto("p2", "esse é o p2", new BigDecimal("10.2"), true);
        p3 = new Produto("p3", "esse é o p3", new BigDecimal("10.3"), false);
        p1 = produtoRespository.findById(produtoService.salvar(restaurantePreCadastrado.getId(), new ProdutoSaveDto(p1)).id()).get();
        produtoService.salvar(restaurantePreCadastrado.getId(), new ProdutoSaveDto(p2));
        produtoService.salvar(restaurantePreCadastrado.getId(), new ProdutoSaveDto(p3));


        quantidadeTotalCadastrados = (int) produtoRespository.count();
        quantidadeAtivosCadastrados = (int) produtoRespository.findAtivosByRestaurante(restaurantePreCadastrado).stream().count();

    }


}
