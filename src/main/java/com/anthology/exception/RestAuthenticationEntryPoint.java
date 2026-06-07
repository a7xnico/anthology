package com.anthology.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException{
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String errorMessage = switch (authenticationException){
        case BadCredentialsException badCredentialsException -> "Credenciales invalidas";
        case DisabledException disabledException -> "Cuenta desahabilitada";
        case LockedException lockedException -> "Cuenta bloqueada";
        case AccountExpiredException accountExpiredException -> "cuenta expirada";
        case CredentialsExpiredException credentialsExpiredException -> "Credenciales expiradas";
        case InsufficientAuthenticationException insufficientAuthenticationException -> "Autenticacion insuficiente";
        case AuthenticationServiceException authenticationServiceException -> "Error en el servicio de autenticacion";

            default ->  "Error de Autenticacion: " + authenticationException.getMessage();
        };

        String jsonResponse =String.format("{\"error\": \"%s\",\"status\": %d, \"path\": \"%s\"}",
                errorMessage,
                HttpServletResponse.SC_UNAUTHORIZED,
                request.getRequestURI());

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();

    }
}
