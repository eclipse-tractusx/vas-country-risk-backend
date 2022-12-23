package com.catenax.valueaddedservice.interceptors;

import com.catenax.valueaddedservice.config.ApplicationVariables;
import com.catenax.valueaddedservice.constants.VasConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenAspectTest {

    @Mock
    ApplicationVariables applicationVariables;

    @InjectMocks
    TokenAspect tokenAspect;

    @Test
    @DisplayName("Should not set the token in applicationvariables when the token is not present")
    void validateTokenWhenTokenIsNotPresentThenNotSetTheTokenInApplicationVariables() throws JsonProcessingException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        ServletRequestAttributes servletRequestAttributes = mock(ServletRequestAttributes.class);
        when(servletRequestAttributes.getRequest()).thenReturn(request);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
        Enumeration<String> headerValues = mock(Enumeration.class);
        when(request.getHeaders(VasConstants.HEADERS_BEARER_TOKEN)).thenReturn(headerValues);
        when(headerValues.hasMoreElements()).thenReturn(false);

        tokenAspect.validateToken();

        verify(applicationVariables, never()).setToken(anyString());
    }

    @Test
    @DisplayName("Should set the token in applicationvariables when the token is present")
    void validateTokenWhenTokenIsPresentThenSetTheTokenInApplicationVariables() throws JsonProcessingException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Enumeration<String> headerValues = mock(Enumeration.class);
        when(headerValues.hasMoreElements()).thenReturn(true);
        when(headerValues.nextElement())
                .thenReturn(
                        "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyZXNvdXJjZV9hY2Nlc3MiOnsiZmFrZUNsaWVudCI6eyJyb2xlcyI6WyJmYWtlUm9sZSJdfX0sIm5hbWUiOiJmYWtlVXNlciIsIm9yZ2FuaXNhdGlvbiI6ImZha2VDb21wYW55IiwiZW1haWwiOiJmYWtlZW1haWwifQ.FxFjhpnRMZbHF-f-zOs-cXDIx_rQasUgU96X0KxqVZA");
        when(request.getHeaders(VasConstants.HEADERS_BEARER_TOKEN)).thenReturn(headerValues);
        ServletRequestAttributes servletRequestAttributes = mock(ServletRequestAttributes.class);
        when(servletRequestAttributes.getRequest()).thenReturn(request);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);

        tokenAspect.validateToken();

        verify(applicationVariables, times(1))
                .setToken(
                        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyZXNvdXJjZV9hY2Nlc3MiOnsiZmFrZUNsaWVudCI6eyJyb2xlcyI6WyJmYWtlUm9sZSJdfX0sIm5hbWUiOiJmYWtlVXNlciIsIm9yZ2FuaXNhdGlvbiI6ImZha2VDb21wYW55IiwiZW1haWwiOiJmYWtlZW1haWwifQ.FxFjhpnRMZbHF-f-zOs-cXDIx_rQasUgU96X0KxqVZA");
    }
}