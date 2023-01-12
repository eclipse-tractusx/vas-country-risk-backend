package com.catenax.valueaddedservice.interceptors;


import com.catenax.valueaddedservice.config.ApplicationVariables;
import com.catenax.valueaddedservice.constants.VasConstants;
import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.service.logic.CompanyUserLogicService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.util.Enumeration;
import java.util.Objects;


/**
 * {@link Aspect} to validate environment
 */
@Aspect
public class TokenAspect {

    @Autowired
    ApplicationVariables applicationVariables;

    @Autowired
    CompanyUserLogicService companyUserLogicService;

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
    public void validateToken() throws JsonProcessingException {

        // Get the specific header attribute
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Enumeration<String> headerValues = request.getHeaders(VasConstants.HEADERS_BEARER_TOKEN);

        // If exists the Header attribute, validate if it is correct
        if (headerValues.hasMoreElements()) {
            String token = headerValues.nextElement();
            applicationVariables.setToken(token);

        }


    }

    @Before("restController()")
    public void validateUserAndTokenAreTheSame() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setName(request.getParameter(VasConstants.REQUEST_USER_NAME) != null ? request.getParameter(VasConstants.REQUEST_USER_NAME) : "");
        companyUserDTO.setCompanyName(request.getParameter(VasConstants.REQUEST_COMPANY_NAME) != null ? request.getParameter(VasConstants.REQUEST_COMPANY_NAME) : "");
        companyUserDTO.setEmail(request.getParameter(VasConstants.REQUEST_USER_EMAIL) != null ? request.getParameter(VasConstants.REQUEST_USER_EMAIL) : "");
        if (companyUserDTO.getName() == null || companyUserDTO.getName().isEmpty()
                || companyUserDTO.getEmail() == null || companyUserDTO.getEmail().isEmpty()
                || companyUserDTO.getCompanyName() == null || companyUserDTO.getCompanyName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,VasConstants.VALIDATE_COMPANY_USER);
        }
        if (env.getProperty("security.enabled") != null && Boolean.valueOf(env.getProperty("security.enabled"))
                && !companyUserLogicService.validateUserAndTokenAreTheSame(companyUserDTO) && !companyUserLogicService.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

    }

}