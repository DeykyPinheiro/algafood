package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.common.io.Base64ProtocolResolver;
import com.apigaworks.algafood.domain.model.Cozinha;
import com.apigaworks.algafood.domain.model.Restaurante;
import com.apigaworks.algafood.domain.model.Usuario;
import com.apigaworks.algafood.domain.repository.*;
import com.apigaworks.algafood.domain.service.GrupoService;
import com.apigaworks.algafood.domain.service.RestauranteService;
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

import static com.apigaworks.algafood.util.ResourceUtils.getContentFromResource;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;
import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = Base64ProtocolResolver.class)
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

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    private String jsonRestauranteCorreto;
    private String jsonRestauranteSemFrete;
    private String jsonRestauranteSemCozinha;
    private String jsonRestauranteComCozinhaInexistente;

    Usuario u1 = new Usuario("u1", "email1@email.com", "12345678");
    Usuario u2 = new Usuario("u2", "email2@email.com", "12345679");
    Restaurante comidaMineiraRestaurante = new Restaurante();

    private Restaurante burgerTopRestaurante;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private PermissaoRepository permissaoRepository;


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

        jsonRestauranteCorreto = getContentFromResource(CAMINHO_RELATIVO +
                "/correto/restaurante-new-york-barbecue.json");

        jsonRestauranteSemFrete = getContentFromResource(
                CAMINHO_RELATIVO + "/incorreto/restaurante-new-york-barbecue-sem-frete.json");

        jsonRestauranteSemCozinha = getContentFromResource(
                CAMINHO_RELATIVO + "/incorreto/restaurante-new-york-barbecue-sem-cozinha.json");

        jsonRestauranteComCozinhaInexistente = getContentFromResource(
                CAMINHO_RELATIVO + "/incorreto/restaurante-new-york-barbecue-com-cozinha-inexistente.json");

        prepararDados();
        tokenGer = login.logarGer(port);


    }

    @Test
    public void deveRetornarStatus200_QuandoConsultarRestaurantes() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
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
                .header("Authorization", "Bearer " + tokenGer)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void deveRetornarStatus204_QuandoAbrirRestaurante() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .when()
                .pathParam("id", comidaMineiraRestaurante.getId())
                .put("/{id}/abertura")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void deveRetornarStatus204_QuandoFecharRestaurante() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .when()
                .pathParam("id", comidaMineiraRestaurante.getId())
                .delete("/{id}/fechamento")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void deveRetornarStatus200_QuandoListarUsuarioReponsaveisPeloRestaurantes() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .pathParam("restauranteId", comidaMineiraRestaurante.getId())
                .when()
                .get("/{restauranteId}/responsaveis")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deveRetornarCorpoCorretoEStatus200_QuandoListarUsuarioReponsaveisPeloRestaurantes() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .pathParam("restauranteId", comidaMineiraRestaurante.getId())
                .when()
                .get("/{restauranteId}/responsaveis")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", hasItems(u1.getNome()))
                .body("nome", hasItems(u2.getNome()));
    }

    @Test
    void deveRetornarStatus204_AssociarUsuarioAoRestaurante() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .pathParam("restauranteId", comidaMineiraRestaurante.getId())
                .pathParam("usuarioId", u2.getId())
                .when()
                .put("/{restauranteId}/responsaveis/{usuarioId}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void deveRetornarStatus204_DesssociarUsuarioAoRestaurante() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .pathParam("restauranteId", comidaMineiraRestaurante.getId())
                .pathParam("usuarioId", u2.getId())
                .when()
                .delete("/{restauranteId}/responsaveis/{usuarioId}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }


    private void prepararDados() {
        this.login.salvarUsuariosComGruposEPermissoes();

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

        u1 = usuarioRepository.save(u1);
        u2 = usuarioRepository.save(u2);
        comidaMineiraRestaurante = restauranteRepository.save(comidaMineiraRestaurante);

        restauranteService.associarUsuario(comidaMineiraRestaurante.getId(), u1.getId());
        restauranteService.associarUsuario(comidaMineiraRestaurante.getId(), u2.getId());
    }


}