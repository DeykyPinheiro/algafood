package com.apigaworks.algafood.common.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@PreAuthorize("isAuthenticated()")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PodeConsultarCozinhas {
}
