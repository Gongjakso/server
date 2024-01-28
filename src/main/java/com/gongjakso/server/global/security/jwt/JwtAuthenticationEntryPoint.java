package com.gongjakso.server.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongjakso.server.global.exception.ErrorCode;
import com.gongjakso.server.global.exception.ErrorResponse;
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
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        Object exception = request.getAttribute("exception");

        // exception에 할당된 속성이 ErrorCode일 경우, 관련된 응답 객체 정보를 삽입하도록 설정
        if (exception instanceof ErrorCode) {
            setResponse(response, (ErrorCode) exception);

            return;
        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }

    // Error 관련 응답 Response 생성 메소드
    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException{
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());

        ErrorResponse errorResponse = new ErrorResponse(errorCode);
        ObjectMapper objectMapper = new ObjectMapper();
        String errorJson = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(errorJson);
    }
}
