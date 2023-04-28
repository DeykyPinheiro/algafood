package com.apigaworks.algafood.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {

    MENSAGEM_INCOMPREENSIVEL("/mensagem-imcompreensivel", "mensagem imcompreensivel"),

    RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "recurso nao encontrado"),

    ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio"),

    ENTIDADE_EM_USO("/entidade-em-uso", "entidade em uso"),

    PARAMETRO_INVALIDO("/parametro-invalido", "parametro invalido"),

    ERRO_DE_SISTEMA("/erro-de-sistema", "erro de sistema"),

    DADOS_INVALIDOS("/dados-invalidos", "dados da invalidos");

    private String title;

    private String uri;

    ProblemType(String path, String title) {
        this.uri = "https://algafood.com.br" + path;
        this.title = title;
    }

}
