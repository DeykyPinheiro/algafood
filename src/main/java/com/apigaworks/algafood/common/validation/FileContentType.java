package com.apigaworks.algafood.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
//aqui vinculamos quem faz a validacao
@Constraint(validatedBy = {FileContentTypeValidator.class})
public @interface FileContentType {


    String message() default "Formato Invalido, envie PNG ou JPG";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    String[] allowed();
}
