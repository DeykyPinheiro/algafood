package com.apigaworks.algafood.api.exceptionhandler;

import com.apigaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.apigaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.apigaworks.algafood.domain.exception.NegocioException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

//a anotacao ControllerAdvice captura as excecoes de forma global
//estou herdadndo ResponseEntityExceptionHandler que é uma implementacao
// padrao que ja trata as excepections do spring
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

//    spring passa uma instancia da web request, o resto pode ser instanciar e modificado para passar a resposta
//    dessa forma toda e qualquer excessao na hora de estourar passa por  handleExceptionInternal, inclusive as do spring
    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> tratarEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex,  WebRequest request) {

        HttpHeaders header  = new HttpHeaders();
        return handleExceptionInternal(ex, ex.getMessage(), header, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> tratarNegocioException(NegocioException ex, WebRequest request){

        HttpHeaders header  = new HttpHeaders();
        return handleExceptionInternal(ex, ex.getMessage(), header, HttpStatus.NOT_FOUND, request);
    }

////    midias nao suportadas
//    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
//    public ResponseEntity<?> tratarHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e){
//
//        Problema problema = Problema.builder()
//                .dataHora(LocalDateTime.now())
//                .mensagem("tipo de midia nao é aceito, tente json").build();
//
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(problema);
//    }

    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<?> tratarEntidadeEmUsoException(EntidadeEmUsoException ex, WebRequest request) {

        HttpHeaders header  = new HttpHeaders();
        return handleExceptionInternal(ex, ex.getMessage(), header, HttpStatus.CONFLICT, request);
    }

    //    especializando metodo, colocando um corpo padrao para a resposta de erro ja definidas pelo spring
    //    usado no erro application/xml quando a midia nao é suportada, e em outros erros
    //    todas a exceptions do spring chamam ResponseEntityExceptionHandler, tal como eu faco com a negocioException
    //    entao todas as excessoes estao cobertas
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

        if (body == null) {
            body = Problema.builder()
                    .dataHora(LocalDateTime.now())
                    .mensagem(ex.getMessage()).build();
        } else if (body instanceof String) {
            body = Problema.builder()
                    .dataHora(LocalDateTime.now())
                    .mensagem(ex.getMessage()).build();
        }

        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }
}
