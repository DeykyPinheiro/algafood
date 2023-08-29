package com.apigaworks.algafood.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.w3c.dom.html.HTMLTableColElement;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


//public class ResourceServerConfig {}

@Configuration
// isso está deprecado EnableGlobalMethodSecurity  esse substitui EnableMethodSecurity
@EnableMethodSecurity(securedEnabled = true) // padrao é true, eu nao precisava colocar, mas pra deixar didatico ta ai
@EnableWebSecurity
public class ResourceServerConfig {
    @Bean
    public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/host").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/oauth2/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.formLogin(Customizer.withDefaults()).build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

//        converte as autorizacoes que eu criei
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> authorityList = jwt.getClaimAsStringList("authorities");
            if (authorityList == null) {
                return Collections.emptyList();
            }

//            converte os escopos em granted authority
            JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
            Collection<GrantedAuthority> grantedAuthorityList = authoritiesConverter.convert(jwt);

//            concatenando as duas lista de autorizacoes
            grantedAuthorityList.addAll(
                    authorityList.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList()));

            return grantedAuthorityList;
        });

        return converter;
    }
}
