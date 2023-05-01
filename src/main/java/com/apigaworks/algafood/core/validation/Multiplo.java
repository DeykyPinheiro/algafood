package com.apigaworks.algafood.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.OverridesAttribute;
import jakarta.validation.Payload;
import jakarta.validation.constraints.PositiveOrZero;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
//aqui vinculamos quem faz a validacao
@Constraint(validatedBy = {MultiploValidator.class})
public @interface Multiplo {


    String message() default "{Multiplo.taxaFrete}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int numero();
}
