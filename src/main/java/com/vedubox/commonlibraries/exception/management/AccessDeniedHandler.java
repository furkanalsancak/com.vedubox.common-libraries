package com.vedubox.commonlibraries.exception.management;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedubox.commonlibraries.model.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.vedubox.commonlibraries.model.enums.HeaderName.X_CORRELATION_ID;


@RequiredArgsConstructor
@Component
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

    @Value("${spring.application.name:Application name not defined.}")
    private String serviceName;

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e)
            throws IOException, ServletException {

        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);

        var errorMessage = ErrorResponse.builder()
                .code(serviceName.toUpperCase() +  "_FORBIDDEN")
                .message("You do not have access to this resource.")
                .xCorrelationId(httpServletRequest.getHeader(X_CORRELATION_ID.toString()))
                .build();

        httpServletResponse.getOutputStream()
                .println(objectMapper.writeValueAsString(errorMessage));
    }
}
