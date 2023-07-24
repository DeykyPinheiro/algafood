package com.apigaworks.algafood.common.security.authorizationserver;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Getter
@Setter
@Validated //o spring auto valida a classe
@ConfigurationProperties("algafood.auth")
public class AlgafoodSecurityProperties {

    @NotBlank
    private String providerUrl;
    // endereco do server de autorizacao, se for integrado com a
    // aplicacao, eu coloco endereco da api

}
