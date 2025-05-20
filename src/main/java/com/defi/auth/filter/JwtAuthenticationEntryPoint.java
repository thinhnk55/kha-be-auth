package com.defi.auth.filter;

import com.defi.common.BaseResponse;
import com.defi.common.CommonMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint  implements AuthenticationEntryPoint {
    private ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        BaseResponse<?> baseResponse = BaseResponse.of(HttpStatus.UNAUTHORIZED.value()
                , CommonMessage.UNAUTHORIZED);
        response.getWriter().write(mapper.writeValueAsString(baseResponse));
    }
}