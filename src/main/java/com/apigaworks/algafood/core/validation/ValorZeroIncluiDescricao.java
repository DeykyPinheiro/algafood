package com.apigaworks.algafood.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//essa anotacao ela funciona a nivel de classe e nao um atributo como
// a Multiplo que eu criei no commit anterior
// pq TYPE? pq posso usar em classe e interface apenas, nao posso usar em propriedade
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValorZeroIncluiDescricaoValidator.class})
public @interface ValorZeroIncluiDescricao {

    String message() default "teste krai";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String valorField();

    String descricaoField();

    String descricaoObrigatoria();






}
