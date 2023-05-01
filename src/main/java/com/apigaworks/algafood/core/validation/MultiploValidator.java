package com.apigaworks.algafood.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class MultiploValidator implements ConstraintValidator<Multiplo, Number> {


    //    numero colocado na anotacao, para multiplicar
    private int numeroMultiplo;

    //    esse nao é obrigatorio, mas posso usar para buscar o valor de variaveis principalmente
    @Override
    public void initialize(Multiplo constraintAnnotation) {
        this.numeroMultiplo = constraintAnnotation.numero();
    }

    //    esse sou obrigado a implementar, pq ele é quem faz a validacao das propriedade
//    value é o valor que eu recebo pela chamada do endpoint
    @Override
    public boolean isValid(Number value, ConstraintValidatorContext constraintValidatorContext) {
        Boolean ehValido = true;

        if (value != null) {
//            transformando o valor para bigdecimal
//            remainder -> retorna o resto da divisao de a/b
            var valorDecimal = BigDecimal.valueOf(value.doubleValue());
            var multiploDecimal = BigDecimal.valueOf(this.numeroMultiplo);
            var resto = valorDecimal.remainder(multiploDecimal);

            ehValido = BigDecimal.ZERO.compareTo(resto) == 0;
        }

        return ehValido;
    }
}
