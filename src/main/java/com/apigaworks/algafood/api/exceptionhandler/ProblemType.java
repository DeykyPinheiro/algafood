package com.apigaworks.algafood.api.exceptionhandler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ProblemType {

    ENTIDADE_NAO_ENCONTRADA("/entidade-nao-encontrada", "entidade nao encontrada"),
    ENTIDADE_EM_USO("/entidade-em-uso", "entidade em uso");

    private String title;

    private String uri;

    ProblemType(String path, String title) {
        this.uri = "https://algafood.com.br" + path;
        this.title = title;
    }





}
