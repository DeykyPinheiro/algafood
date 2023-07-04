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
import org.springframework.security.web.SecurityFilterChain;

@Configuration
// isso está deprecado EnableGlobalMethodSecurity  esse substitui EnableMethodSecurity
@EnableMethodSecurity(securedEnabled = true) // padrao é true, eu nao precisava colocar, mas pra deixar didatico ta ai
@EnableWebSecurity
public class ResourceServerConfig {
    @Bean
    public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // desabilita csrf para evitar ataques de path ja autenticado
//                politica de autorizacoes

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/oauth2/**").permitAll() //precisa esta autenticado
                        .anyRequest().authenticated()
                )
//                .cors(cors -> cors.) VOU PRECISAR, MAS DEIXA COMENTADO POR HORA
//                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::opaqueToken); // ativando opaque token
                .oauth2ResourceServer().jwt();
        return http.build();

    }
}
