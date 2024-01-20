package com.gongjakso.server.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // 인증 관련 에러 처리, 401
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        Object exception = request.getAttribute("exception");

//        if (exception instanceof ErrorCode) {
//            ErrorCode errorCode = (ErrorCode) exception;
//            setResponse(response,errorCode);
//
//            return;
//        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }

//    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException{
//        response.setContentType("application/json;charset=UTF-8");
//        response.setStatus(errorCode.getHttpStatus().value());
//
//        ApplicationErrorResponse errorResponse = new ApplicationErrorResponse(errorCode);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String errorJson = objectMapper.writeValueAsString(errorResponse);
//
//        response.getWriter().write(errorJson);
//    }
}
