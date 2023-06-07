package org.example.custom_exception;

import lombok.extern.slf4j.Slf4j;
import org.example.util.api_response.ApiResponseUtil;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String HTTP_MESSAGE_NOT_READABLE = "http_message_not_readable";

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpHeaders httpHeaders,
                                                                  HttpStatus httpStatus, WebRequest webRequest) {
        e.printStackTrace();
        return ApiResponseUtil.build(HttpStatus.BAD_REQUEST, HTTP_MESSAGE_NOT_READABLE);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders httpHeaders,
                                                               HttpStatus httpStatus, WebRequest webRequest) {
        List<Object> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return ApiResponseUtil.build(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), errors);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<Object> handleInternalServerError(Exception e, WebRequest request) {
        e.printStackTrace();
        return ApiResponseUtil.build(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }


    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<Object> handleFileNotFound(Exception e, WebRequest webRequest) {
        e.printStackTrace();
        return ApiResponseUtil.build(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
