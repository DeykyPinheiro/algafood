package com.apigaworks.algafood.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

//a anotacao @JsonInclude(JsonInclude.Include.NON_NULL), só inclui na hora de exibir se nao tiver null
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
@Schema(name = "Problema")
public class
Problem {

    @Schema(example = "400")
    private Integer status;

    @Schema(example = "https://algafood.com.br/dados-invalidos")
    private String type;

    @Schema(example = "dados invalidos")
    private String title;

    @Schema(example = "um ou mais campos invalidos")
    private String detail;

    List<Object> objects;

    @Getter
    @Builder
    @Schema(example = "detalhes dos problemas")
    static class Object {

        @Schema(example = "preco")
        String name;

        @Schema(example = "preco invalido")
        String userMessage;

    }

}
