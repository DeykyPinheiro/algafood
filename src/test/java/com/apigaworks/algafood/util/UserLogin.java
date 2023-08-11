package com.apigaworks.algafood.util;

import com.apigaworks.algafood.domain.dto.usuario.UsuarioSaveDto;
import com.apigaworks.algafood.domain.model.Grupo;
import com.apigaworks.algafood.domain.model.Permissao;
import com.apigaworks.algafood.domain.model.Usuario;
import com.apigaworks.algafood.domain.repository.GrupoRepository;
import com.apigaworks.algafood.domain.repository.PermissaoRepository;
import com.apigaworks.algafood.domain.repository.UsuarioRepository;
import com.apigaworks.algafood.domain.service.GrupoService;
import com.apigaworks.algafood.domain.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Getter
@Setter
@Component
public class UserLogin {

//    @Autowired
    public UserLogin(GrupoRepository grupoRepository,
                     PermissaoRepository permissaoRepository,
                     UsuarioService usuarioService,
                     UsuarioRepository usuarioRepository,
                     GrupoService grupoService) {

        Objects.requireNonNull(grupoRepository);
        Objects.requireNonNull(permissaoRepository);
        Objects.requireNonNull(usuarioService);
        Objects.requireNonNull(usuarioRepository);
        Objects.requireNonNull(grupoService);





        this.grupoRepository = grupoRepository;
        this.permissaoRepository = permissaoRepository;
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
        this.grupoService = grupoService;
    }


    private GrupoRepository grupoRepository;


    private PermissaoRepository permissaoRepository;


    private UsuarioService usuarioService;


    private UsuarioRepository usuarioRepository;

    private GrupoService grupoService;


    public record LoginDto(
            @NotBlank
            String username,

            @NotBlank
            String senha,
            @NotNull
            @Valid
            ClientDto clientDto

    ) {
    }

    public record ClientDto(
            @NotBlank
            String clientId,
            @NotBlank
            String secret

    ) {
    }

    public record AccessToken(
            String access_token,

            String refresh_token,

            String token_type,

            String expires_in
    ) {

    }

    private static String logarClienteViaCustomPasswordflow(LoginDto loginDto, int port) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder stringBuilder = new StringBuilder();
        HttpHeaders headers = new HttpHeaders();

        ClientDto client = loginDto.clientDto;
        String base64Client = Base64.encodeBase64String((client.clientId.toString() + ":" + client.secret.toString()).getBytes());
        headers.add("Authorization", ("Basic " + base64Client));


        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "custom_password");
        formData.add("scope", "READ");
        formData.add("username", loginDto.username);
        formData.add("password", loginDto.senha);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(formData, headers);


        String url = stringBuilder.append("http://localhost:" + port).append("/oauth2/token").toString();

        try {
            ResponseEntity<AccessToken> entity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    httpEntity,
                    AccessToken.class
            );

//            System.out.println(entity.getBody().access_token);
            String token = entity.getBody().access_token;
            return token;
        } catch (Exception ex) {
            throw new Exception(String.format("o usuario: %s nao foi autorizado", loginDto.username), ex);
        }
    }


    @Transactional
    public void  salvarUsuariosComGruposEPermissoes() {

        Grupo g1 = new Grupo(1L, "Gerente");
        Grupo g2 = new Grupo(2L, "Vendedor");
        Grupo g3 = new Grupo(3L,"Secretária");
        Grupo g4 = new Grupo(4L, "Cadastrador");

        g1 = grupoRepository.save(g1);
        g2 = grupoRepository.save(g2);
        g3 = grupoRepository.save(g3);
        g4 = grupoRepository.save(g4);

        Permissao p1 = new Permissao(1L, "EDITAR_COZINHAS", "");
        Permissao p2 = new Permissao(2L, "EDITAR_FORMAS_PAGAMENTO", "");
        Permissao p3 = new Permissao(3L, "EDITAR_CIDADES", "");
        Permissao p4 = new Permissao(4L, "EDITAR_ESTADOS", "");
        Permissao p5 = new Permissao(5L, "CONSULTAR_USUARIOS_GRUPOS_PERMISSOES", "");
        Permissao p6 = new Permissao(6L, "EDITAR_USUARIOS_GRUPOS_PERMISSOES", "");
        Permissao p7 = new Permissao(7L, "EDITAR_RESTAURANTES", "");
        Permissao p8 = new Permissao(8L, "CONSULTAR_PEDIDOS", "");
        Permissao p9 = new Permissao(9L, "GERENCIAR_PEDIDOS", "");
        Permissao p10 = new Permissao(10L, "GERAR_RELATORIOS", "");

        p1 = permissaoRepository.save(p1);
        p2 = permissaoRepository.save(p2);
        p3 = permissaoRepository.save(p3);
        p4 = permissaoRepository.save(p4);
        p5 = permissaoRepository.save(p5);
        p6 = permissaoRepository.save(p6);
        p7 = permissaoRepository.save(p7);
        p8 = permissaoRepository.save(p8);
        p9 = permissaoRepository.save(p9);
        p10 = permissaoRepository.save(p10);


        grupoService.associarPermissao(g1.getId(), p1.getId());
        grupoService.associarPermissao(g1.getId(), p2.getId());
        grupoService.associarPermissao(g1.getId(), p3.getId());
        grupoService.associarPermissao(g1.getId(), p4.getId());
        grupoService.associarPermissao(g1.getId(), p5.getId());
        grupoService.associarPermissao(g1.getId(), p6.getId());
        grupoService.associarPermissao(g1.getId(), p7.getId());
        grupoService.associarPermissao(g1.getId(), p8.getId());
        grupoService.associarPermissao(g1.getId(), p9.getId());
        grupoService.associarPermissao(g1.getId(), p10.getId());



        grupoService.associarPermissao(g2.getId(), p1.getId());
        grupoService.associarPermissao(g2.getId(), p2.getId());
        grupoService.associarPermissao(g2.getId(), p5.getId());
        grupoService.associarPermissao(g2.getId(), p8.getId());
        grupoService.associarPermissao(g2.getId(), p7.getId());


        grupoService.associarPermissao(g3.getId(), p1.getId());
        grupoService.associarPermissao(g3.getId(), p5.getId());
        grupoService.associarPermissao(g3.getId(), p8.getId());

        grupoService.associarPermissao(g4.getId(), p7.getId());


        UsuarioSaveDto ger = new UsuarioSaveDto("João da Silva", "joao.ger@algafood.com", "123");
        UsuarioSaveDto ven = new UsuarioSaveDto("Maria Joaquina", "maria.vnd@algafood.com", "123");
        UsuarioSaveDto aux = new UsuarioSaveDto("José Souza", "jose.aux@algafood.com", "123");
        UsuarioSaveDto cad = new UsuarioSaveDto("Sebastião Martins", "sebastiao.cad@algafood.com", "123");
        UsuarioSaveDto loja = new UsuarioSaveDto("Manoel Lima", "manoel.loja@gmail.com", "123");
        UsuarioSaveDto debora = new UsuarioSaveDto("Débora Mendonça", "inglezinho1+1@gmail.com", "123");
        UsuarioSaveDto carlos = new UsuarioSaveDto("Carlos Lima", "inglezinho1+2@gmail.com", "123");


        Usuario u1 = usuarioRepository.findById(usuarioService.salvar(ger).id()).get();
        Usuario u2 =  usuarioRepository.findById(usuarioService.salvar(ven).id()).get();
        Usuario u3 =  usuarioRepository.findById(usuarioService.salvar(aux).id()).get();
        Usuario u4 = usuarioRepository.findById(usuarioService.salvar(cad).id()).get();
        Usuario u5 = usuarioRepository.findById(usuarioService.salvar(loja).id()).get();
        Usuario u6 = usuarioRepository.findById(usuarioService.salvar(debora).id()).get();
        Usuario u7 =  usuarioRepository.findById(usuarioService.salvar(carlos).id()).get();

        usuarioService.associarGrupo(u1.getId(), g1.getId());
        usuarioService.associarGrupo(u1.getId(), g2.getId());


        usuarioService.associarGrupo(u2.getId(), g2.getId());

        usuarioService.associarGrupo(u3.getId(), g3.getId());

        usuarioService.associarGrupo(u4.getId(), g4.getId());


    }



    @Transactional(readOnly = true)
    public String logarGer(int port) throws Exception {
        ClientDto clientDto = new ClientDto("autorizationcode", "123");
        LoginDto loginDto = new LoginDto("joao.ger@algafood.com", "123", clientDto);
        return logarClienteViaCustomPasswordflow(loginDto, port);
    }

    @Transactional(readOnly = true)
    public String logarVdn(int port) throws Exception {
        ClientDto clientDto = new ClientDto("autorizationcode", "123");
        LoginDto loginDto = new LoginDto("maria.vnd@algafood.com", "123", clientDto);
        return logarClienteViaCustomPasswordflow(loginDto, port);
    }

    @Transactional(readOnly = true)
    public String logarAux(int port) throws Exception {
        ClientDto clientDto = new ClientDto("autorizationcode", "123");
        LoginDto loginDto = new LoginDto("jose.aux@algafood.com", "123", clientDto);
        return logarClienteViaCustomPasswordflow(loginDto, port);
    }

    @Transactional(readOnly = true)
    public String logarCad(int port) throws Exception {
        ClientDto clientDto = new ClientDto("autorizationcode", "123");
        LoginDto loginDto = new LoginDto("sebastiao.cad@algafood.com", "123", clientDto);
        return logarClienteViaCustomPasswordflow(loginDto, port);
    }

    @Transactional(readOnly = true)
    public String logarLoja(int port) throws Exception {
        ClientDto clientDto = new ClientDto("autorizationcode", "123");
        LoginDto loginDto = new LoginDto("manoel.loja@gmail.com", "123", clientDto);
        return logarClienteViaCustomPasswordflow(loginDto, port);
    }

    @Transactional(readOnly = true)
    public String logarClienteDebora(int port) throws Exception {
        ClientDto clientDto = new ClientDto("autorizationcode", "123");
        LoginDto loginDto = new LoginDto("inglezinho1+1@gmail.com", "123", clientDto);
        return logarClienteViaCustomPasswordflow(loginDto, port);
    }

    @Transactional(readOnly = true)
    public String logarClienteCarlos(int port) throws Exception {
        ClientDto clientDto = new ClientDto("autorizationcode", "123");
        LoginDto loginDto = new LoginDto("inglezinho1+2@gmail.com", "123", clientDto);
        return logarClienteViaCustomPasswordflow(loginDto, port);
    }


//    public static void main(String[] args) {
//        ClientDto clientDto = new ClientDto("autorizationcodea", "123");
//        LoginDto loginDto = new LoginDto("manoel.loja@gmail.coma", "123", clientDto);
//
//        System.out.println(logarClienteViaCustomPasswordflow(loginDto));
////        logarClienteViaCustomPasswordflow(loginDto);
//    }


//    @Autowired
//    private AlgafoodSecurityProperties algafoodSecurityProperties;
//
//
//    private static String codeVerifierGenerator() {
//        SecureRandom sr = new SecureRandom();
//        byte[] code = new byte[32];
//        sr.nextBytes(code);
//        return Base64.encodeBase64URLSafeString(code);
//    }
//
//    private static String challengeGenerator(String verifier) throws Exception {
//        byte[] bytes = verifier.getBytes("US-ASCII");
//        MessageDigest md = MessageDigest.getInstance("SHA-256");
//        md.update(bytes, 0, bytes.length);
//        byte[] digest = md.digest();
//        return Base64.encodeBase64URLSafeString(digest);
//    }
//
//    private static String encodeUrlParameter(String parameter) {
//        try {
//            return URLEncoder.encode(parameter, StandardCharsets.UTF_8.toString());
//        } catch (java.io.UnsupportedEncodingException ex) {
//            throw new RuntimeException(ex.getCause());
//        }
//    }
//
//    public static String urlAuthorization(String baseUrl, String codeChallenge, String codeChallengeMethod,
//                                          String clientId, String redirectUrl, String scope) {
//        StringBuilder url = new StringBuilder();
//        url.append(baseUrl).append("/oauth2/authorize");
//
//
//        url.append("?client_id=").append(encodeUrlParameter(clientId));
//        url.append("&redirect_uri=").append(encodeUrlParameter(redirectUrl));
//        url.append("&scope=").append(encodeUrlParameter(scope));
//        url.append("&response_type=code");
//        url.append("&code_challenge_method=S256");
//        url.append("&code_challenge=").append(encodeUrlParameter(codeChallenge));
//
//
//        //        url.append("?response_type=code");
//        return url.toString();
//    }
//
//
//    public static ResponseEntity<?> login(String url) {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<String> resquestEntity = new HttpEntity<>();
//
//        ResponseEntity<?> entity = restTemplate.getForEntity(url, Object.class);
//
//        return entity;
//    }
//
//    public static void main(String[] args) throws Exception {
////        @Autowired AlgafoodSecurityProperties properties;
////        verificador: 1lD_R1sX5Kc6FFuOogBjubxiRHrNQVQD8R6BNpYVS8E desafio: IdIepRZpyPN9TTKgmR_Vqyh_1OTk3D-YTu-zgbcoz7I
////        for (int i = 0; i < 10; i++) {
////            String verifier = codeVerifierGenerator();
////            String challenge = challengeGenerator(verifier);
////            System.out.println(String.format("verificador: %s desafio: %s", verifier, challenge));
////        }
////        System.out.println();
//
//        String verifier = codeVerifierGenerator();
//        String challenge = challengeGenerator(verifier);
//        String url = urlAuthorization("http://localhost:8080", challenge, "S256", "autorizationcode",
//                "https://oauthdebugger.com/debug", "READ");
////        System.out.println(url);
//
//        System.out.println(login(url));
//
//    }
}

//http://localhost:8080/oauth2/authorize?client_id=autorizationcode
// &redirect_uri=https%3A%2F%2Foauthdebugger.com%2Fdebug
// &scope=READ
// &response_type=code
// &response_mode=form_post
// &code_challenge_method=S256
// &code_challenge=IdIepRZpyPN9TTKgmR_Vqyh_1OTk3D-YTu-zgbcoz7I

//-[] efetuar login e receber token
//    -[x] concatenar login e senha do cliente e por em base64 no header como auth
//    -[x] mandar como form-data grant_type, scope, username, password
//    -[x] manda a solicitacao e recuperar token
//    -[x] fazer login com cada tipo de usuario


//-[ ] criar usuario com autenticacao
//    -[x] injetar grupo e permisaao
//    -[x] cadastrar grupos
//    -[x] cadastrar permissoes
//    -[x] cadastrar permissoes nos grupos
//    -[x] cadastrar usuarios
//    -[x] relaciona usuarios aos grupos

