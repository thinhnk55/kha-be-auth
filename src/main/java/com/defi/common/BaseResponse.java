package com.defi.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;


@JsonInclude(JsonInclude.Include.NON_NULL)
public record BaseResponse <T>(
        Integer code,
        String message,
        T data,
        Pagination pagination,
        Map<String, Object> errors
){
    public static <T> BaseResponse<T> of(int code, String message, T data,
                                         Pagination pagination, Map<String,
            Object> errors) {
        return new BaseResponse<>(code, message, data, pagination, errors);
    }

    public static <T> BaseResponse<T> of(int code, String message, Map<String, Object> errors) {
        return new BaseResponse<>(code, message, null, null, errors);
    }

    public static <T> BaseResponse<T> of(T data) {
        return new BaseResponse<>(null, null, data, null, null);
    }

    public static <T> BaseResponse<T> of(T data, Pagination pagination) {
        return new BaseResponse<>(null, null, data, pagination, null);
    }

    public static BaseResponse<?> of(int code, String message){
        return BaseResponse.of(code, message, null, null, null);
    }

    public static BaseResponse<?> of(Exception e) {
        if(e instanceof ResponseStatusException ex){
            return BaseResponse.of(ex.getStatusCode().value(), ex.getBody().getDetail());
        }else {
            return BaseResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "internal_server");
        }
    }
}
