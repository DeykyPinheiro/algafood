package com.apigaworks.algafood.api.exceptionhandler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ProblemType {

    MENSAGEM_INCOMPREENSIVEL("/mensagem-imcompreensivel", "mensagem imcompreensivel"),

    ENTIDADE_NAO_ENCONTRADA("/entidade-nao-encontrada", "entidade nao encontrada"),

    ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio"),

    ENTIDADE_EM_USO("/entidade-em-uso", "entidade em uso");

    private String title;

    private String uri;

    ProblemType(String path, String title) {
        this.uri = "https://algafood.com.br" + path;
        this.title = title;
    }

}
