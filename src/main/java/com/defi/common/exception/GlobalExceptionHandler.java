package com.defi.common.exception;

import com.defi.common.api.BaseResponse;
import com.defi.common.api.CommonMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<BaseResponse<?>> handleResponseStatusException(ResponseStatusException ex) {
        BaseResponse<?> response = BaseResponse.of(ex.getStatusCode().value(),
                ex.getBody().getDetail());
        return new ResponseEntity<>(response, ex.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex,
                                                                   WebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        BaseResponse<?> response = BaseResponse.of(ex.getStatusCode().value(),
                ex.getBody().getDetail(), errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<?>> handleAllExceptions(Exception ex,
            WebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("error", CommonMessage.INTERNAL_SERVER);
        return new ResponseEntity<>(BaseResponse.of(errors), HttpStatus.BAD_REQUEST);
    }
}
