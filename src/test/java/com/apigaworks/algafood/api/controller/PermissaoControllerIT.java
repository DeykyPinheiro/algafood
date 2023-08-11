package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.common.io.Base64ProtocolResolver;
import com.apigaworks.algafood.domain.model.Permissao;
import com.apigaworks.algafood.domain.repository.GrupoRepository;
import com.apigaworks.algafood.domain.repository.PermissaoRepository;
import com.apigaworks.algafood.domain.repository.UsuarioRepository;
import com.apigaworks.algafood.domain.service.GrupoService;
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

import java.time.Duration;

import static com.apigaworks.algafood.util.ResourceUtils.getContentFromResource;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
@ContextConfiguration(initializers = Base64ProtocolResolver.class)
public class PermissaoControllerIT {

    public static final String CAMINHO_RELATIVO = "src/test/java/com/apigaworks/algafood/json";

    public static final int ID_GRUPO_NAO_EXISTENTE = 100;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private PermissaoRepository permissaoRepository;

    @Autowired
    private GrupoRepository grupoRepository;


    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private GrupoService grupoService;

    private UserLogin login;

    private String tokenGer;


    @LocalServerPort
    private int port;

    private int quantidadePermissaoCadastrados = 0;

    private String jsonPermissaoCorreto;

    private String jsonPermissaoAtualizacaoCorreto;

    private Permissao p1 = new Permissao("p1", "essa é a p1");
    private Permissao p2 = new Permissao("p2", "essa é a p2");
    private Permissao p3 = new Permissao("p3", "essa é a p3");

    @Autowired
    private RegisteredClientRepository registeredClientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    void setUp() throws Exception { databaseCleaner.clearTables();
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
        RestAssured.basePath = "/permissoes";

        jsonPermissaoCorreto = getContentFromResource(CAMINHO_RELATIVO +
                "/correto/permissao-correta.json");

        jsonPermissaoAtualizacaoCorreto = getContentFromResource(CAMINHO_RELATIVO +
                "/correto/permissao-atualizacao-correta.json");


        prepararDados();
        tokenGer = login.logarGer(port);
    }

    @Test
    void deveRetornarStatus201_QuandoCadastrarPermissao() {
        RestAssured.given()
                .body(jsonPermissaoCorreto)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void deveRetornarStatuEcorpoCorreto_QuandoBuscarPorIdValido(){
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .pathParam("permissaoId", p1.getId())
                .when()
                .get("/{permissaoId}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo(p1.getNome()))
                .body("descricao", equalTo(p1.getDescricao()));
    }

    @Test
    void deveRetornarCorpoEStatusCorreto_QuandoCadastrarPermissao() {
        RestAssured.given()
                .body(jsonPermissaoCorreto)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("nome", equalTo("permissao nome teste"))
                .body("descricao", equalTo("permissao descricao teste"));
    }

    @Test
    void deveRetornarStatus200_QuandoListarPermissoes() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deveRetornarQuantidadeIgualPermissoesCadastrados_QuandoListarPermissoes() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("", hasSize(quantidadePermissaoCadastrados));
    }

    @Test
    void deveRetornarCorpoEStatusCorreto_QuandoAtualizarPermissao() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .body(jsonPermissaoAtualizacaoCorreto)
                .pathParam("permissaoId", p1.getId())
                .when()
                .put("/{permissaoId}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo("atualizacao"))
                .body("descricao", equalTo("atualizacao descricao"));
    }

    @Test
    void deveRetornarStatus204_QuandoExcluirPermissaoValida() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .pathParam("permissaoId", p1.getId())
                .when()
                .delete("/{permissaoId}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }


    private void prepararDados() {
        this.login.salvarUsuariosComGruposEPermissoes();
        p1 = permissaoRepository.save(p1);
        p2 = permissaoRepository.save(p2);
        p3 = permissaoRepository.save(p3);

        quantidadePermissaoCadastrados = (int) permissaoRepository.count();
    }


}
