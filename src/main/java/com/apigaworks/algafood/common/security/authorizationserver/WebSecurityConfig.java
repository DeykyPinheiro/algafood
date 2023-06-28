package com.apigaworks.algafood.common.security.authorizationserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //dentro dela tem o configuration, mas to deixando ai pra ficar mais explicito
public class WebSecurityConfig {

    //salvar dois usuario em memoria e codificar com Bcript
//    para usar isso em requisicao é sou por basic auth e por user e senha
    @Bean
    public UserDetailsService users() {
        // O construtor garantirá que as senhas sejam codificadas antes de salvar na memória em bcript
        User.UserBuilder users = User.withDefaultPasswordEncoder();
//        System.out.println("impressao da senha: " + passwordEncoder().encode("123"));


        UserDetails user = users
                .username("user")
                .password("123")
                .roles("USER")
                .build();

        UserDetails admin = users
                .username("deyky")
                .password("123")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    //    configurarndo protecao contra csrf, que é quando o usuario ta logado e é guardado coockies que idenficam o user
//    assim quando o user receber uma pagina e acessa ele nao precisa autenticar, isso deixa o sistema vulneravel a
//    ataques
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // desabilita csrf para evitar ataques de path ja autenticado
//                politica de autorizacoes
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/cozinhas/**").permitAll() //permite tudo
                        .anyRequest().authenticated() // fora outras resquest tem que ser autenticada
                )
                .httpBasic(Customizer.withDefaults()) // autoriza apenas autenticacoes via httpbasic
                .sessionManagement((session) -> session //politica de sessao
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // cookies é bom ninguem quer dar, configura para nao salvar cookies
    //salvar user em memoria
        return http.build();
    }

//    se eu usar User.withDefaultPasswordEncoder() e instanciar esse bean, da erro
//    por causa do encode
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}