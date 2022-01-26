package com.vedubox.commonlibraries.exception.management;

import com.vedubox.commonlibraries.model.ErrorResponse;
import com.vedubox.commonlibraries.exception.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

import static com.vedubox.commonlibraries.model.enums.HeaderName.X_CORRELATION_ID;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ControllerAdvice
@Slf4j
public class ApiResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${spring.application.name:Application name not defined.}")
    private String serviceName;

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(
                String.format("Missing Servlet Request Parameter Exception: %s", request.getHeader(X_CORRELATION_ID.toString())),
                ex.getMessage());

        var errorResponse = ErrorResponse.builder()
                .xCorrelationId(request.getHeader(X_CORRELATION_ID.toString()))
                .code(serviceName.toUpperCase() +  "_MISSING_REQUEST_PARAMETER")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.badRequest().contentType(APPLICATION_JSON).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(
                String.format("Missing Path Variable Exception: %s", request.getHeader(X_CORRELATION_ID.toString())), ex.getMessage(),
                ex.getMessage());

        var errorResponse = ErrorResponse.builder()
                .xCorrelationId(request.getHeader(X_CORRELATION_ID.toString()))
                .code(serviceName.toUpperCase() +  "_MISSING_PATH_VARIABLE")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.badRequest().contentType(APPLICATION_JSON).body(errorResponse);
    }

    @ExceptionHandler(value = { MissingRequestHeaderException.class })
    public ResponseEntity<ErrorResponse> handleMissingHeaderException(MissingRequestHeaderException ex) {
        log.error(String.format("Missing Header Exception: %s", ex.getHeaderName()));

        var errorResponse = ErrorResponse.builder()
                .code(serviceName.toUpperCase() +  "_MISSING_REQUEST_HEADER")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.badRequest().contentType(APPLICATION_JSON).body(errorResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        log.error(
                String.format("Entity Not Found Exception: %s", request.getHeader(X_CORRELATION_ID.toString())),
                ex.getMessage());

        var errorResponse = ErrorResponse.builder()
                .xCorrelationId(request.getHeader(X_CORRELATION_ID.toString()))
                .code(serviceName.toUpperCase() +  "_ENTITY_NOT_FOUND")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.badRequest().contentType(APPLICATION_JSON).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleServerException(Exception ex, WebRequest request) {
        log.error(String.format("Unhandled Exception: %s", request.getHeader(X_CORRELATION_ID.toString())), ex);

        var errorResponse = ErrorResponse.builder()
                .xCorrelationId(request.getHeader(X_CORRELATION_ID.toString()))
                .code(serviceName.toUpperCase() +  "_SERVER_ERROR")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.internalServerError().contentType(APPLICATION_JSON).body(errorResponse);
    }



    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(
                String.format("Method Argument Not Valid Exception: %s", request.getHeader(X_CORRELATION_ID.toString())),
                ex.getMessage());

        var errorResponse = ErrorResponse.builder()
                .xCorrelationId(request.getHeader(X_CORRELATION_ID.toString()))
                .code(serviceName.toUpperCase() +  "_METHOD_ARGUMENT_NOT_VALID")
                .message(ex.getMessage())
                .details(ex.getBindingResult()
                        .getFieldErrors().stream()
                        // .map(e -> new ResponseError(e.getDefaultMessage(), e.getField()))
                        .map(e -> e.getField() + ":" + e.getDefaultMessage())
                        .collect(Collectors.toList()))
                .build();

        return ResponseEntity.badRequest().contentType(APPLICATION_JSON).body(errorResponse);
    }


    // @ExceptionHandler(ConstraintViolationException.class)
    // public final ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, WebRequest request)
    // {
    //     log.error(
    //             String.format("Constraint Violation Exception: %s", request.getHeader(X_CORRELATION_ID_PARAM_NAME)),
    //             ex.getMessage());
    //
    //     var errorResponse = ErrorResponse.builder()
    //             // .xCorrelationId(request.getHeader(X_CORRELATION_ID_PARAM_NAME))
    //             .code(serviceName.toUpperCase() +  "_METHOD_ARGUMENT_NOT_VALID")
    //             .message(ex.getMessage())
    //             .details(ex.getConstraintViolations().stream()
    //                     .map(ConstraintViolation::getMessage)
    //                     // .getFieldErrors().stream()
    //                     // .map(e -> new ResponseError(e.getDefaultMessage(), e.getField()))
    //                     // .map(e -> e.getField() + ":" + e.getDefaultMessage())
    //                     .collect(Collectors.toList()))
    //             .build();
    //
    //     return ResponseEntity.badRequest().contentType(APPLICATION_JSON).body(errorResponse);
    // }

    // @Override
    // protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    //     log.error("handleBindException", ex.getMessage());
    //     return super.handleBindException(ex, headers, status, request);
    // }
    //
    // @Override
    // protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    //     log.error("handleHttpRequestMethodNotSupported", ex.getMessage());
    //     return super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
    // }
    //
    // @Override
    // protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    //     log.error("handleNoHandlerFoundException", ex.getMessage());
    //     return super.handleNoHandlerFoundException(ex, headers, status, request);
    // }



    // @Override
    // protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    //     return super.handleTypeMismatch(ex, headers, status, request);
    // }
    //
    // @Override
    // protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    //     log.error("handleServletRequestBindingException");
    //     return super.handleServletRequestBindingException(ex, headers, status, request);
    // }
    //
    // @Override
    // protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    //     log.error("handleMissingServletRequestPart");
    //     return super.handleMissingServletRequestPart(ex, headers, status, request);
    // }
}
