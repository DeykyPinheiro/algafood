package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.common.io.Base64ProtocolResolver;
import com.apigaworks.algafood.domain.model.FormaPagamento;
import com.apigaworks.algafood.domain.repository.FormaPagamentoRepository;
import com.apigaworks.algafood.domain.repository.GrupoRepository;
import com.apigaworks.algafood.domain.repository.PermissaoRepository;
import com.apigaworks.algafood.domain.repository.UsuarioRepository;
import com.apigaworks.algafood.domain.service.GrupoService;
import com.apigaworks.algafood.domain.service.UsuarioService;
import com.apigaworks.algafood.util.DatabaseCleaner;
import com.apigaworks.algafood.util.UserLogin;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.test.context.*;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.function.Supplier;

import static com.apigaworks.algafood.util.ResourceUtils.getContentFromResource;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.proxy;
import static org.hamcrest.Matchers.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
@ContextConfiguration(initializers = Base64ProtocolResolver.class)
//public class formaPagamentoControllerIT implements InitializingBean {
public class formaPagamentoControllerIT {


    public static final String CAMINHO_RELATIVO = "src/test/java/com/apigaworks/algafood/json";

    public static final int ID_PAGAMENTO_NAO_EXISTENTE = 100;


//    @PostConstruct
//    public void init() {
////        registerDynamicProperties();
//        portao = port;
//        System.out.println("sapora foi iniciada, numero da porta: " + port);
//        System.out.println("sapora foi iniciada, numero da portao: " + portao);
//    }


//    @PostConstruct
//    public void init(){
//        System.out.println("PostConstruct funcionando");
//        staticPort = this.port;
//    }

    //    public static int getStaticPort() {
//        return staticPort;
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//
//
//    }

    @LocalServerPort
    private Integer port;


//    private  static  int pooorra;

//    @BeforeTestClass
//    public static void beforeClass() {
//        pooorra = this.port;
//    }
//@DynamicPropertySource
//static void registerDynamicProperties(DynamicPropertyRegistry registry) {
//    registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri",
//            () -> "http://localhost:" + pooorra  + "/oauth2/jwks");
//}

//    @DynamicPropertySource
//    static void registerDynamicProperties(DynamicPropertyRegistry registry, @LocalServerPort int pooorra) {
//        registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri",
//                () -> "http://localhost:" + pooorra  + "/oauth2/jwks");
//    }

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    private String jsonFormaPagamentoCorreto;


    private int quantidadeFormaPagamentoCadastradas = 0;

    FormaPagamento formaPagamento1 = new FormaPagamento("pix");


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


//        String t = port.toString();
//
//        System.setProperty("server.port", t);
//        System.setProperty("spring.security.oauth2.resourceserver.jwt.jwk-set-uri", "http://localhost:" + t  + "/oauth2/jwks");

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
        RestAssured.port = this.port;
        RestAssured.basePath = "/formaPagamentos";


        jsonFormaPagamentoCorreto = getContentFromResource(CAMINHO_RELATIVO +
                "/correto/pagamento-cartao.json");


        prepararDados();
        tokenGer = login.logarGer(port);
    }

    @Test
    void deveRetornarStatus200_QuandoConsultarFormaPagamentosComAutorizacaoDeGerente() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("descricao", hasItems("pix"));
    }

    @Test
    void deveRetornarTotalDeFormaPagamentosCadastradas_QuandoConsultarCozinhas() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .when()
                .get()
                .then()
                .body("", hasSize(quantidadeFormaPagamentoCadastradas));
    }

    @Test
    void deveRetornarRepostaEStatusCorretos_QuandoConsultarFormaPagamentoExistente() {
        given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .pathParam("id", formaPagamento1.getId())
                .when()
                .get("/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("descricao", equalTo(formaPagamento1.getDescricao()));
    }

    @Test
    void deveRetornarRepostaEStatusCorretos_QuandoConsultarFormaPagamentoInexistente() {
        given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .pathParam("id", ID_PAGAMENTO_NAO_EXISTENTE)
                .when()
                .get("/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void deveRetornarStatus201_QuandoCadastrarPagamento() {
        given()
                .body(jsonFormaPagamentoCorreto)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void deveRetornar204_QuandoExcluirUmaFormaPagamento() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenGer)
                .pathParam("id", formaPagamento1.getId())
                .when()
                .delete("/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());


    }


    private void prepararDados() {
//        UserLogin login = new UserLogin(grupoRepository, permissaoRepository, usuarioService, usuarioRepository);
//        FormaPagamento formaPagamento = new FormaPagamento("pix");
        this.login.salvarUsuariosComGruposEPermissoes();
//        usuarioService.salvar(ger);
        formaPagamentoRepository.save(formaPagamento1);

        quantidadeFormaPagamentoCadastradas = (int) formaPagamentoRepository.count();
    }


}
