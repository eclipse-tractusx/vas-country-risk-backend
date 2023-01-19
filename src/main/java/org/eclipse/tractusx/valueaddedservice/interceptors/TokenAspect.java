/********************************************************************************
* Copyright (c) 2022,2023 BMW Group AG 
* Copyright (c) 2022,2023 Contributors to the Eclipse Foundation
*
* See the NOTICE file(s) distributed with this work for additional
* information regarding copyright ownership.
*
* This program and the accompanying materials are made available under the
* terms of the Apache License, Version 2.0 which is available at
* https://www.apache.org/licenses/LICENSE-2.0.
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
*
* SPDX-License-Identifier: Apache-2.0
********************************************************************************/
package org.eclipse.tractusx.valueaddedservice.interceptors;


import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.eclipse.tractusx.valueaddedservice.config.ApplicationVariables;
import org.eclipse.tractusx.valueaddedservice.constants.VasConstants;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyUserDTO;
import org.eclipse.tractusx.valueaddedservice.service.logic.CompanyUserLogicService;
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
    @Pointcut("within(org.eclipse.tractusx.valueaddedservice.web.rest.*)")
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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, VasConstants.VALIDATE_COMPANY_USER);
        }
        if (env.getProperty("security.enabled") != null && Boolean.valueOf(env.getProperty("security.enabled"))
                && !companyUserLogicService.validateUserAndTokenAreTheSame(companyUserDTO) && !companyUserLogicService.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

    }

}