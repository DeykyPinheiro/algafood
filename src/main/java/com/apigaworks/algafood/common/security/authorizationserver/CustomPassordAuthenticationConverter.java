package com.apigaworks.algafood.common.security.authorizationserver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.*;

//public class CustomPassordAuthenticationConverter implements AuthenticationConverter {
//    @Nullable
//    @Override
//    public Authentication convert(HttpServletRequest http) {
//
//        System.out.println("entrei na CustomPassordAuthenticationConverter - convert");
//
//        String grantType = http.getParameter(OAuth2ParameterNames.GRANT_TYPE);
//
////        se o fluxo que vc recebeu nao é igual ao que implementei ja sai aqui
//        if ("custom_password".equals(grantType)) {
//            return null;
//        }
//
////        lista de parametros recebida
//        MultiValueMap<String, String> parameter = getParameters(http);
//
////         scope (OPTIONAL)
//        String scope = parameter.getFirst(OAuth2ParameterNames.SCOPE);
//        if (StringUtils.hasText(scope) && parameter.get(OAuth2ParameterNames.SCOPE).size() != 1) {
//            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
//        }
//
//
//        return null;
//    }
//
//    private static MultiValueMap<String, String> getParameters(HttpServletRequest http) {
//        Map<String, String[]> parameterMap = http.getParameterMap();
//        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
//        parameterMap.forEach((key, values) -> {
//            if (values.length > 0) {
//                for (String value : values) {
//                    parameters.add(key, value);
//                }
//            }
//        });
//        return parameters;
//    }
//}

public class CustomPassordAuthenticationConverter implements AuthenticationConverter {

    @Nullable
    @Override
    public Authentication convert(HttpServletRequest request) {

        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);

        if (!"custom_password".equals(grantType)) {
            return null;
        }

            MultiValueMap<String, String> parameters = getParameters(request);

        // scope (OPTIONAL)
        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
        if (StringUtils.hasText(scope) &&
                parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
        }

        // username (REQUIRED)
        String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
        if (!StringUtils.hasText(username) ||
                parameters.get(OAuth2ParameterNames.USERNAME).size() != 1) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
        }

        // password (REQUIRED)
        String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
        if (!StringUtils.hasText(password) ||
                parameters.get(OAuth2ParameterNames.PASSWORD).size() != 1) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
        }




        Set<String> requestedScopes = null;
        if (StringUtils.hasText(scope)) {
            requestedScopes = new HashSet<>(
                    Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
        }

        Map<String, Object> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(OAuth2ParameterNames.SCOPE)) {
                additionalParameters.put(key, value.get(0));
            }
        });

        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
//        Authentication clientPrincipal = new Authentication();

        return new CustomPasswordAuthenticationToken(clientPrincipal, requestedScopes, additionalParameters);
    }

    private static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            if (values.length > 0) {
                for (String value : values) {
                    parameters.add(key, value);
                }
            }
        });
        return parameters;
    }
}
