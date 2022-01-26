package com.vedubox.commonlibraries.exception.management;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedubox.commonlibraries.model.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.vedubox.commonlibraries.model.enums.HeaderName.X_CORRELATION_ID;


@RequiredArgsConstructor
@Component
public class AuthenticationFailureHandler implements AuthenticationEntryPoint {

    @Value("${spring.application.name:Application name not defined.}")
    private String serviceName;

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e)
            throws IOException, ServletException {

        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponse errorMessage = null;

        if(e instanceof InsufficientAuthenticationException) {
            errorMessage = ErrorResponse.builder()
                    .code(serviceName.toUpperCase() +  "_INSUFFICIENT_AUTHENTICATION")
                    .message("Invalid token.")
                    .xCorrelationId(httpServletRequest.getHeader(X_CORRELATION_ID.toString()))
                    .build();
        } else if(e instanceof AuthenticationCredentialsNotFoundException) {
            errorMessage = ErrorResponse.builder()
                    .code(serviceName.toUpperCase() +  "_MISSING_AUTHENTICATION_CREDENTIALS")
                    .message("Missing Authorization Header.")
                    .xCorrelationId(httpServletRequest.getHeader(X_CORRELATION_ID.toString()))
                    .build();
        }

        httpServletResponse.getOutputStream()
                .println(objectMapper.writeValueAsString(errorMessage));
    }
}
