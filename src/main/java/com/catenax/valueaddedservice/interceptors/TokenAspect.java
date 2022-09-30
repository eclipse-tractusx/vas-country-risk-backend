package com.catenax.valueaddedservice.interceptors;


import com.catenax.valueaddedservice.config.ApplicationVariables;
import com.catenax.valueaddedservice.constants.VasConstants;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Objects;


/**
 * {@link Aspect} to validate environment
 */
@Aspect
public class TokenAspect {

    @Autowired
    ApplicationVariables applicationVariables;

    /**
     * Constant <code>X_ENVIRONMENT="x-environment"</code>
     */
    public static final String X_ENVIRONMENT = "x-environment";

    private final Environment env;

    /**
     * <p>Constructor for EnvironmentAspect.</p>
     *
     * @param env a {@link Environment} object.
     */
    public TokenAspect(Environment env) {
        this.env = env;
    }

    /**
     * Pointcut that matches all Web REST endpoints.
     */
    @Pointcut("within(com.catenax.valueaddedservice.web.rest.*)")
    public void restController() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }


    @Before("restController()")
    public void validateToken() {

        // Get the specific header attribute
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Enumeration<String> headerValues = request.getHeaders(VasConstants.HEADERS_BEARER_TOKEN);

        // If exists the Header attribute, validate if it is correct
        if (headerValues.hasMoreElements()) {
            String token = headerValues.nextElement();
            applicationVariables.setToken(token);
        }
    }
}