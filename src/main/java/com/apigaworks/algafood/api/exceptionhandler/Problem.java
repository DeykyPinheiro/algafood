package com.apigaworks.algafood.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

//a anotacao @JsonInclude(JsonInclude.Include.NON_NULL), só inclui na hora de exibir se nao tiver null
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class Problem {

    private Integer status;

    private String type;

    private String title;

    private String detail;

    List<Object> objects;

    @Getter
    @Builder
    static class Object {

        String name;

        String userMessage;

    }

}
