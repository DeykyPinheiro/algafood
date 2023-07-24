package com.apigaworks.algafood.common.security.authorizationserver;


import com.apigaworks.algafood.domain.model.Usuario;
import com.apigaworks.algafood.domain.repository.UsuarioRepository;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import java.io.InputStream;
import java.security.KeyStore;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;


// Resource Owner Password Credentials Grant ou Password Credentials grant ou Password Flow ou Password Grant Type
//EVITAR USAR ESSE FLUXO


@Configuration
//@Import(OAuth2AuthorizationServerConfiguration.class)
@EnableWebSecurity
//@EnableAuthorizationServer // nao funciona dai tennho que fazer assim @Import(OAuth2AuthorizationServerConfiguration.class)
//mudei grande parte do que vem no curso, pois a solucao foi deprecada, mas aqui estou fazendo o equivalente
//VOU TER QUE REESCREVER, POR ISSO TA COMENTADA
public class AuthorizationServerConfig {


//    INFORMACOES SOBRE O SERVER ESTAO NO AUTHSERVER

    //    isso está sendo obrigatorio, mas no curso mudamos, entao vou deixar ai temporariamente
//    bean que guarda os clientes do server de autorizacao, implementa em memoria
//    to injetando via bean, um bean especifico de jdbc
    @Bean
    public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder,
                                                                 JdbcOperations jdbcOperations) {

////        authorization code com refresh token, pkce funciona por padrao, passando os campos ele vai usar
//        RegisteredClient autorizationcode = RegisteredClient
//                .withId("3")
//                .clientId("autorizationcode")
//                .clientSecret(passwordEncoder.encode("123"))
//                .scope("READ")
//                .redirectUri("https://oidcdebugger.com/debug")
//                .redirectUri("https://oauthdebugger.com/debug")
//                .redirectUri("http://127.0.0.1:8080/swagger-ui/oauth2-redirect.html")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .tokenSettings(TokenSettings.builder()
//                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
//                        .accessTokenTimeToLive(Duration.ofMinutes(30))
//                        .refreshTokenTimeToLive(Duration.ofMinutes(30))
//                        .reuseRefreshTokens(false)
//                        .build())
//                .clientSettings(ClientSettings.builder()
//                        .requireAuthorizationConsent(true)  //obriga a tela de consentimento aparecer
//                        .requireProofKey(false)
//                        .build())
//                .build();

        JdbcRegisteredClientRepository jdbcRegisteredClientRepository = new JdbcRegisteredClientRepository(jdbcOperations);
        return jdbcRegisteredClientRepository;
//        return new InMemoryRegisteredClientRepository( autorizationcode);
    }


    //    @Order(1) posso usar isso tbm para dar as ordens de precedencia dos filtros
    //server pra ser o primeiro filtro a passar, depois segue o fluxo, isso pro login funcionar de maneira adequada
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        return http.formLogin(Customizer.withDefaults()).build();
    }

//    @Bean
//    @Order(2)
//    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(
//                        authorize -> authorize.anyRequest().authenticated())
//                .formLogin(Customizer.withDefaults());
//        return http.build();
//    }

    //eles renomearam ProviderSettings  para AuthorizationServerSettings, mas em essencia é a mesma coisa
//    isso diz quem vai assinar os tokens, no local seria 8080
//    em prod seria o dns  da api
    //essa funcao diz quem é o authorization server que vai assinar os tokens, LEIA O EXEMPLO ACIMA
    @Bean
    public AuthorizationServerSettings serverSettings(AlgafoodSecurityProperties properties) {
//        System.out.println("estou dentro da classe: "+ properties.getProviderUrl()); // ta dando certo
        return AuthorizationServerSettings.builder()
                .issuer(properties.getProviderUrl()) // é a url do autorizationServer
                .build();
    }


    // bean responsavel por salvar os tokens em banco
    @Bean
    public OAuth2AuthorizationService oAuth2AuthorizationService(JdbcOperations jdbcOperations,
                                                                 RegisteredClientRepository registeredClientRepository) {

        return new JdbcOAuth2AuthorizationService(jdbcOperations, registeredClientRepository);
    }

    //    esse Bean serve para abrir o keystore e usar a chave privada pra assinar
    @Bean
    public JWKSource<SecurityContext> jwkSource(JwtKeyStoreProperties properties) throws Exception {
        char[] keyStorePass = properties.getPassword().toCharArray();
        String keyParAlias = properties.getKeypairAlias();

        Resource jksLocation = properties.getJksLocation();
        InputStream inputStream = properties.getJksLocation().getInputStream();
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(inputStream, keyStorePass);

        RSAKey rsaKey = RSAKey.load(keyStore, keyParAlias, keyStorePass);
        return new ImmutableJWKSet<>(new JWKSet(rsaKey));
    }

    //    customizar o token jwt antes de ser emitido
//    tem um template variable, mas nao sei como funciona, só preciso implementar a interface quem no  OAuth2TokenCustomizer
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer(UsuarioRepository usuarioRepository) {

//no context tem tudo o que esta dentro do token a ser emitido
        return context -> {


//            Authentication authentication = context.getPrincipal(); //é um var template, mas no padrao do spring é authentication
//            if (authentication.getPrincipal() instanceof User) {
            Authentication authentication = context.getPrincipal();
            var a = authentication.getPrincipal(); // quando vem o client_credentiais,o principal nao existe,
//            ele tomo como principal o nome do user, por isso o vale para o autentication_code
            if (authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();


                Usuario usuario = usuarioRepository.findByEmail(user.getUsername()).orElseThrow(() -> new RuntimeException("usuario nao encontrado"));

                Set<String> authorityList = new HashSet<>(); // monstando a lista de permissoes para por no token JWT
                for (GrantedAuthority authority : user.getAuthorities()) {
                    authorityList.add(authority.getAuthority());
                }


//                inserir dados no JWT
                context.getClaims().claim("usuario_id", usuario.getId().toString());
                context.getClaims().claim("authorities", authorityList);
            }

        };
    }


}
