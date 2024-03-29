package com.apigaworks.algafood.api.exceptionhandler;

import com.apigaworks.algafood.common.validation.ValidacaoExcepiton;
import com.apigaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.apigaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.apigaworks.algafood.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

//a anotacao ControllerAdvice captura as excecoes de forma global
//estou herdadndo ResponseEntityExceptionHandler que é uma implementacao
// padrao que ja trata as excepections do spring
@ControllerAdvice
@Slf4j // lib para logar msg
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    //    serve para manipular a msg que chegam
    @Autowired
    private MessageSource messageSource;

    //    spring passa uma instancia da web request, o resto pode ser instanciar e modificado para passar a resposta
//    dessa forma toda e qualquer excessao na hora de estourar passa por  handleExceptionInternal, inclusive as do spring
    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex, WebRequest request) {

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        String details = ex.getMessage();

        log.error(ex.getMessage(), ex); // logar msg de erro, se precisar de log separado pesquisar por appender

        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
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

//    isso está deprecado, mas nao achei outra solucao, vou deixar ai por hora, é uma API de estudos, nao
//    merece tanta atencao assim
    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleValidationInternal(ex, ex.getBindingResult(), headers, HttpStatus.valueOf(status.value()), request);
    }


//isso para tratar corretamente, ja que internamente HttpMediaTypeNotAcceptableException, nao chama a nossa funcao sobreescrita handleExceptionInternal
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        return  ResponseEntity.status(status).headers(headers).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
        String detail = "Ocorreu um erro interno inesperado no sistema. " + "Tente novamente e se o problema persistir, entre em contato " + "com o administrador do sistema.";

        // Importante colocar o printStackTrace (pelo menos por enquanto, que não estamos
        // fazendo logging) para mostrar a stacktrace no console
        // Se não fizer isso, você não vai ver a stacktrace de exceptions que seriam importantes
        // para você durante, especialmente na fase de desenvolvimento
        ex.printStackTrace();

        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }


    //    sobreescrevi esse metodo pra testar com uma excessao que ja funciona, mas nesse cara o spring ja tava devolvendo
    //    no formato que eu configurei
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

//        é a causa raiz de uma exception, tem que passar a exception para ele pesquisar e ver
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
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

        String propriedade = ex.getPath().stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));

        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String details = String.format("A propriedade '%s' recebeu o valor '%s' que é do tipo invalido " + "passe uma propriedade do tipo '%s'", propriedade, ex.getValue(), ex.getTargetType().getSimpleName());

        Problem problem = createProblemBuilder(HttpStatus.valueOf(status.value()), problemType, details).build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatchException((MethodArgumentTypeMismatchException) ex, headers, status, request);
        }

        return super.handleTypeMismatch(ex, headers, status, request);
    }


    private ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;
        String details = String.format("o parametro da URL '%s' recebeu o valor '%s' que é do tipo invalido " + "informe um valor compativel com o tipo '%s'", ex.getPropertyName(), ex.getValue(), ex.getRequiredType().getSimpleName());
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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex, WebRequest request){
        HttpHeaders header = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        String details = ex.getMessage();
        Problem problem = createProblemBuilder(httpStatus, ProblemType.ACESSO_NEGADO, details).build();


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

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
        String detail = String.format("O recurso %s, que você tentou acessar, é inexistente.", ex.getRequestURL());

        Problem problem = createProblemBuilder(HttpStatus.valueOf(status.value()), problemType, detail).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleValidationInternal(ex, ex.getBindingResult(), headers, HttpStatus.valueOf(status.value()), request);

////        nao vou especilizar esse metodo pq na hierarquia de 'MethodArgumentNotValidException'
////        caso precise futuramente eu altero aqui
//
//
////        armazenas as constraint de violacao
//        BindingResult bindingResult = ex.getBindingResult();
//        List<Problem.Object> objectList = bindingResult.getAllErrors().stream()
//                .map(objectError -> {
//
//                    String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
//
////                    isso serve para passar nao só os campos com erro, mas todos o erros
////                    a validacao abaixo é pra passar o campo
//                    String name = objectError.getObjectName();
//                    if (objectError instanceof FieldError) {
//                        name = ((FieldError) objectError).getField();
//                    }
//
//                    return Problem.Object.builder()
//                            .name(name)
//                            .userMessage(message)
//                            .build();
//                })
//                .collect(Collectors.toList());
//
//
//        ProblemType problemType = ProblemType.DADOS_INVALIDOS;
//        String details = String.format("um ou mais campos esta invalidos, preencha os campos corretamente", "");
//        Problem problem = createProblemBuilder(HttpStatus.valueOf(status.value()), problemType, details).objectList(objectList).build();
//
//
//        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handleValidationInternal(Exception ex, BindingResult bindingResult, HttpHeaders headers,
                                                            HttpStatus status, WebRequest request) {

        ProblemType problemType = ProblemType.DADOS_INVALIDOS;
        String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";

        List<Problem.Object> problemObjects = bindingResult.getAllErrors().stream()
                .map(objectError -> {
                    String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

                    String name = objectError.getObjectName();

                    if (objectError instanceof FieldError) {
                        name = ((FieldError) objectError).getField();
                    }

                    return Problem.Object.builder()
                            .name(name)
                            .userMessage(message)
                            .build();
                })
                .collect(Collectors.toList());


        Problem problem = createProblemBuilder(HttpStatus.valueOf(status.value()), problemType, detail).objects(problemObjects).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
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

    @ExceptionHandler({ValidacaoExcepiton.class})
    public ResponseEntity<Object> handleValidacaoException(ValidacaoExcepiton ex, WebRequest request) {
        return handleValidationInternal(ex, ex.getBindResult(), new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);
    }


    //    Problem.ProblemBuilder essa é uma classe que o lombok cria dentro do Problem,
//    quando ce anota com Builder, nao dei um build pq se nao ele retornaria a instancia de Problem
    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String details) {
        return Problem.builder().status(status.value()).type(problemType.getUri()).title(problemType.getTitle()).detail(details);
    }
}