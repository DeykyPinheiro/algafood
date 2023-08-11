package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.common.io.Base64ProtocolResolver;
import com.apigaworks.algafood.domain.model.Cozinha;
import com.apigaworks.algafood.domain.repository.CozinhaRepository;
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
import static org.hamcrest.Matchers.*;


//todos que estao nessa pasta serao usado como teste de API
//sobe um servidor aleatorio e usa a porta
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
@ContextConfiguration(initializers = Base64ProtocolResolver.class)
class CozinhaControllerIT {

    public static final int ID_COZINHA_NAO_EXISTENTE = 100;
    public static final String CAMINHO_RELATIVO = "src/test/java/com/apigaworks/algafood/json";

    //    essa anotacao injeta o numero da porta que esta sendo ultiliza  na variavel
    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    String china = getContentFromResource(CAMINHO_RELATIVO + "/correto/cozinha-chinesa.json");

    Cozinha cozinha1 = new Cozinha("Tailandesa");

    int quantidadeCozinhasCadastradas = 0;

    private String tokenGer;

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

    @Autowired
    private RegisteredClientRepository registeredClientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    public void setUp() throws Exception {
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
        RestAssured.basePath = "/cozinhas";


        prepararDados();
        tokenGer = login.logarGer(port);
    }

    @Test
    void deveRetornar200_QuandoConsultarCozinhas() {
        given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deveRetornarQuantidadeIgualCadastradaCozinhas_QuandoConsultarCozinhas() {
        given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .when()
                .get()
                .then()
                .body("content", hasSize(quantidadeCozinhasCadastradas))
                .body("content[0].nome", equalTo("Tailandesa"))
                .body("content[1].nome", equalTo("Indiana"));
    }

    @Test
    void deveRetornarStatus201_QuandoCadastrarCozinha() {

        given()
                .body(china)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void deveRetornarRespostaEStatusCorretos_QuandoConsultarCozinhaExistente() {
        given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .pathParam("id", cozinha1.getId())
                .when()
                .get("/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo(cozinha1.getNome()));
    }

    @Test
    void deveRetornarRespostaEStatus404_QuandoConsultarCozinhaInexistente() {
        given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .pathParam("id", ID_COZINHA_NAO_EXISTENTE)
                .when()
                .get("/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }


    private void prepararDados() {

        this.login.salvarUsuariosComGruposEPermissoes();
        cozinhaRepository.save(cozinha1);

        Cozinha cozinha2 = new Cozinha();
        cozinha2.setNome("Indiana");
        cozinhaRepository.save(cozinha2);

        quantidadeCozinhasCadastradas = (int) cozinhaRepository.count();
    }
}

//  MESMAS CLASSES DE CIMA ESCRITA DE UMA OUTRA FORMA, UM POUCO MAIS SIMPLES
//    @Test
//    void deveRetornar200_QuandoConsultarCozinhas() {
//
////        isso habilita o log da resposta errada
////        dai mostra exatamente o que foi feito e a resposta tbm
//        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
//
////        quando acessar o endpoit, na porta, aceitando json
////        usado get
////        deverá retornar 200 status code
//        given()
//                .basePath("/cozinhas")
//                .port(port)
//                .accept(ContentType.JSON)
//                .when()
//                .get()
//                .then()
//                .statusCode(HttpStatus.OK.value());
//    }
//
//    @Test
//    void deveRetornar4Cozinhas_QuandoConsultarCozinhas() {
//        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
//
////        verificar se tenho 4 cozinhas
////        verificar se tenho os item indiana e tailandesa no campo de nome
//
//        given()
//                .basePath("/cozinhas")
//                .port(port)
//                .accept(ContentType.JSON)
//                .when()
//                .get()
//                .then()
//                .body("", hasSize(4))
//                .body("nome", hasItems("Indiana", "Tailandesa"));
//    }