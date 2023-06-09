package com.apigaworks.algafood.common.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;


@AllArgsConstructor
@Getter
public class ValidacaoExcepiton extends RuntimeException {

    private BindingResult bindResult;

}
