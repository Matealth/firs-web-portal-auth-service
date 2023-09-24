package com.firs.risk.mgt.auth.svc.jwt;

import com.firs.risk.mgt.auth.svc.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    private Locale locale = new Locale("en", "US");
    private ResourceBundle messages = ResourceBundle.getBundle("messages", locale);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws UnauthorizedException {

        resolver.resolveException(request, response, null, new UnauthorizedException(messages.getString("invalid-credentials")));

    }
}
