package com.apigaworks.algafood.core.validation;

import com.apigaworks.algafood.domain.exception.NegocioException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;


@AllArgsConstructor
@Getter
public class ValidacaoExcepiton extends RuntimeException {

    private BindingResult bindResult;

}
