package com.apigaworks.algafood.api.exceptionhandler;

import com.apigaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.apigaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.apigaworks.algafood.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

//a anotacao ControllerAdvice captura as excecoes de forma global
//estou herdadndo ResponseEntityExceptionHandler que é uma implementacao
// padrao que ja trata as excepections do spring
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    //    spring passa uma instancia da web request, o resto pode ser instanciar e modificado para passar a resposta
//    dessa forma toda e qualquer excessao na hora de estourar passa por  handleExceptionInternal, inclusive as do spring
    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex, WebRequest request) {

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        String details = ex.getMessage();

        ProblemType problemType = ProblemType.ENTIDADE_NAO_ENCONTRADA;
        Problem problem = createProblemBuilder(httpStatus, problemType, details).build();

//        Problem problem = Problem.builder()
//                .status(httpStatus.value())
//                .type("https://algafood.com.br/entidade-nao-encontrada")
//                .title("entidade nao encontrada")
//                .detail(details)
//                .build();

        HttpHeaders header = new HttpHeaders();
        return handleExceptionInternal(ex, problem, header, httpStatus, request);
    }

    //    sobreescrevi esse metodo pra testar com uma excessao que ja funciona, mas nesse cara o spring ja tava devolvendo
//    no formato que eu configurei
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

//        é a causa raiz de uma exception, tem que passar a exception para ele pesquisar e ver
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
        } else if (rootCause instanceof  PropertyBindingException){
            return  handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
        }


        HttpHeaders header = new HttpHeaders();
        String details = "corpo da requisicao está invalido, verifique a sintaxe";
        Problem problem = createProblemBuilder(HttpStatus.valueOf(status.value()), ProblemType.MENSAGEM_INCOMPREENSIVEL, details).build();


        return handleExceptionInternal(ex, problem, header, status, request);
    }

    private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String details = String.format("o campo '%s' nao é reconhecido, tente novamente com campos validos", ex.getPropertyName());
        Problem problem = createProblemBuilder(HttpStatus.valueOf(status.value()), problemType, details).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String propriedade = ex.getPath().stream()
                .map(ref -> ref.getFieldName()).collect(Collectors.joining("."));

        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String details = String.format("A propriedade '%s' recebeu o valor '%s' que é do tipo invalido " +
                "passe uma propriedade do tipo '%s'", propriedade, ex.getValue(), ex.getTargetType().getSimpleName());

        Problem problem = createProblemBuilder(HttpStatus.valueOf(status.value()), problemType, details).build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> handleNegocioException(NegocioException ex, WebRequest request) {

        HttpHeaders header = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        String details = ex.getMessage();
        Problem problem = createProblemBuilder(httpStatus, ProblemType.ERRO_NEGOCIO, details).build();


        return handleExceptionInternal(ex, problem, header, HttpStatus.NOT_FOUND, request);
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
    public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException ex, WebRequest request) {

        HttpHeaders header = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        String details = ex.getMessage();
        Problem problem = createProblemBuilder(httpStatus, ProblemType.ENTIDADE_EM_USO, details).build();

        return handleExceptionInternal(ex, problem, header, httpStatus, request);
    }

    //    especializando metodo, colocando um corpo padrao para a resposta de erro ja definidas pelo spring
    //    usado no erro application/xml quando a midia nao é suportada, e em outros erros
    //    todas a exceptions do spring chamam ResponseEntityExceptionHandler, tal como eu faco com a negocioException
    //    entao todas as excessoes estao cobertas
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

        if (body == null) {
            body = Problem.builder().title(statusCode.toString()).status(statusCode.value()).build();
        } else if (body instanceof String) {
            body = Problem.builder().title(ex.getMessage()).status(statusCode.value()).build();
        }

        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }


    //    Problem.ProblemBuilder essa é uma classe que o lombok cria dentro do Problem,
//    quando ce anota com Builder, nao dei um build pq se nao ele retornaria a instancia de Problem
    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String details) {
        return Problem.builder().status(status.value()).type(problemType.getUri()).title(problemType.getTitle()).detail(details);
    }
}