package com.apigaworks.algafood.common.security.authorizationserver;

import com.apigaworks.algafood.domain.model.Usuario;
import org.bouncycastle.crypto.Mac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2DeviceAuthorizationConsentAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2DeviceVerificationAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;


//essa classe faz o que a implementcao padrao do spring faz
//public final class OAuth2DeviceAuthorizationConsentAuthenticationProvider implements AuthenticationProvider
public class CustomPassordAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";
    private final OAuth2AuthorizationService auth2AuthorizationService;
    private final UserDetailsService userDetailsService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;


    private String username = "";
    private String password = "";

    private Set<String> authorizedScopes = new HashSet<>();

    public CustomPassordAuthenticationProvider(OAuth2AuthorizationService auth2AuthorizationService,
                                               OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                               UserDetailsService userDetailsService) {

        this.auth2AuthorizationService = auth2AuthorizationService;
        this.userDetailsService = userDetailsService;
        this.tokenGenerator = tokenGenerator;

    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        CustomPasswordAuthenticationToken customPasswordAuthenticationToken = (CustomPasswordAuthenticationToken) authentication;
//        TODO está com clientPrincipal e registeredClient trocados
        OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(customPasswordAuthenticationToken);
//        System.out.println("princnipal " + clientPrincipal);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        username = customPasswordAuthenticationToken.getUsername();
        password = customPasswordAuthenticationToken.getPassword();
        User user = null;


        try {
            user = (User) userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.ACCESS_DENIED);
        }
        if (!passwordEncoder.matches(password, user.getPassword()) || !user.getUsername().equals(username)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.ACCESS_DENIED);
        }

        authorizedScopes = registeredClient.getScopes().stream()
                .collect(Collectors.toSet());
//        authorizedScopes = user.getAuthorities().stream()
//                .map(scope -> scope.getAuthority())
////                .filter(scope -> registeredClient.getScopes().contains(scope)) // esse nao pode ter, pra adicionar todos os escopos
//                .collect(Collectors.toSet());

        //-----------Create a new Security Context Holder Context----------
        OAuth2ClientAuthenticationToken oAuth2ClientAuthenticationToken = (OAuth2ClientAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        CustomPasswordUser customPasswordUser = new CustomPasswordUser(username, user.getAuthorities());
        oAuth2ClientAuthenticationToken.setDetails(customPasswordUser);

        var newcontext = SecurityContextHolder.createEmptyContext();
        newcontext.setAuthentication(oAuth2ClientAuthenticationToken);
        SecurityContextHolder.setContext(newcontext);

        //-----------TOKEN BUILDERS----------
//        coisas que vão estar dentro do token
//        DefaultOAuth2TokenContext test = new DefaultOAuth2TokenContext();
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(clientPrincipal) // usuario tem que estar aqui

                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizedScopes(authorizedScopes)
                .authorizationGrantType(new AuthorizationGrantType("custom_password"))
                .authorizationGrant(customPasswordAuthenticationToken);


        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization
                .withRegisteredClient(registeredClient)
                .attribute(Principal.class.getName(), clientPrincipal)
                .principalName(clientPrincipal.getName())
                .authorizationGrantType(new AuthorizationGrantType("custom_password"))
                .authorizedScopes(authorizedScopes);
//                .attributes((Consumer<Map<String, Object>>) UserIdMap);

        //-----------ACCESS TOKEN----------
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the access token.", ERROR_URI);
            throw new OAuth2AuthenticationException(error);
        }


//        Map<String, String> UserIdMap = new HashMap<>();
//        UserIdMap.put("usuario_id", "1L");


//        geracao de token/dto que é devolvido, caso queira alterar algo, faz aqui
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt());
        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder.token(accessToken, (metadata) ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, ((ClaimAccessor) generatedAccessToken).getClaims()));
        } else {
            authorizationBuilder.accessToken(accessToken);
        }


        //-----------REFRESH TOKEN----------
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

            tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the refresh token.", ERROR_URI);
                throw new OAuth2AuthenticationException(error);
            }
            refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            authorizationBuilder.refreshToken(refreshToken);
        }

//        Authentication auth = (OAuth2Authorization) new OAuth2DeviceAuthorizationConsentAuthenticationToken();


        OAuth2Authorization authorization = authorizationBuilder.build();
        this.auth2AuthorizationService.save(authorization);


        var a = new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, refreshToken);


//        Authentication auth = (OAuth2Authorization) new OAuth2DeviceAuthorizationConsentAuthenticationToken("uriTest", registeredClient.getClientId().toString(), (Authentication) user, "teste", "teste");
//        OAuth2DeviceVerificationAuthenticationToken
//        var a = new OAuth2DeviceVerificationAuthenticationToken()
//        var a = (Authentication) user;
//        return new OAuth2DeviceVerificationAuthenticationToken(a, registeredClient.getId().toString(), "este");
        return a;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomPasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private static OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
//        System.out.println("sou o Authentication: " + authentication);
        OAuth2ClientAuthenticationToken principal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            principal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }
        if (principal != null && principal.isAuthenticated()) {
            return principal;
        }
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }

}
