package com.shgxzybaba.testsolution.httpcomponents;


import com.shgxzybaba.testsolution.exceptions.InvalidDataException;
import com.shgxzybaba.testsolution.exceptions.UserNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@ControllerAdvice
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    @ExceptionHandler({UserNotFoundException.class, InvalidDataException.class})
    public ResponseEntity<Object> handleControllerException(Exception exception, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        int httpCode = status.value();
        ApiResponseBody responseBody = ResponseBuilder.buildResponse(exception, httpCode);
        return new ResponseEntity<>(responseBody, status);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiResponseBody responseBody = ResponseBuilder.buildResponse(ex, status.value());
        responseBody.getErrors().clear();
        BindingResult result = ex.getBindingResult();
        List<ObjectError> errors = result.getAllErrors();
        List<String> errorMessages = errors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        errorMessages.forEach(error -> responseBody.getErrors().add(error));
        return new ResponseEntity<>(responseBody, status);
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        try {
            Class<?>[] classes = Objects.requireNonNull(methodParameter.getMethod()).getParameterTypes();
            for (Class<?> clazz : classes) {
                if (clazz.isInstance(Model.class)) {
                    return false;
                }
            }
            return true;
        } catch (NullPointerException e) {
            return true;
        }
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        if (o instanceof ApiResponseBody) {
            return o;
        } else {

            ApiResponseBody output = new ApiResponseBody();
            output.setStatusCode(200);
            output.setSuccess(true);
            output.setBody(o);
            return output;
        }
    }

}
