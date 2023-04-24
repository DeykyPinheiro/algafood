package com.apigaworks.algafood.api.exceptionhandler;

import com.apigaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.apigaworks.algafood.domain.exception.NegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

//a anotacao ControllerAdvice captura as excecoes de forma global
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> tratarEntidadeNaoEncontradaException(EntidadeNaoEncontradaException e) {

        Problema problema = Problema.builder()
                .dataHora(LocalDateTime.now())
                .mensagem(e.getMessage()).build();


        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(problema);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> tratarNegocioException(NegocioException e) {

        Problema problema = Problema.builder()
                .dataHora(LocalDateTime.now())
                .mensagem(e.getMessage()).build();


        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(problema);
    }

//    midias nao suportadas
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> tratarHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e){

        Problema problema = Problema.builder()
                .dataHora(LocalDateTime.now())
                .mensagem("tipo de midia nao é aceito, tente json").build();


        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(problema);
    }
}
