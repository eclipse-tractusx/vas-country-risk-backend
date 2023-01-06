package com.catenax.valueaddedservice.web.rest;

import com.catenax.valueaddedservice.ValueAddedServiceApplication;
import com.catenax.valueaddedservice.domain.Company;
import com.catenax.valueaddedservice.domain.CompanyGates;
import com.catenax.valueaddedservice.domain.CompanyGroup;
import com.catenax.valueaddedservice.domain.CompanyUser;
import com.catenax.valueaddedservice.dto.BusinessPartnerDTO;
import com.catenax.valueaddedservice.dto.CompanyGatesDTO;
import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.dto.CountryDTO;
import com.catenax.valueaddedservice.repository.CompanyGatesRepository;
import com.catenax.valueaddedservice.repository.CompanyGroupRepository;
import com.catenax.valueaddedservice.repository.CompanyRepository;
import com.catenax.valueaddedservice.repository.CompanyUserRepository;
import com.catenax.valueaddedservice.service.mapper.CompanyUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = ValueAddedServiceApplication.class)
class BPNIntegartionTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    CompanyGatesRepository companyGatesRepository;

    @Autowired
    CompanyGroupRepository companyGroupRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    CompanyUserRepository companyUserRepository;

    @AfterEach
    public void cleanGatesAndRelations(){
        companyUserRepository.deleteAll();
        companyGatesRepository.deleteAll();
        companyRepository.deleteAll();
        companyGroupRepository.deleteAll();

    }

    private Map<String,Object> getMap() throws IOException {
        Map<String,Object> map = new HashMap<>();
        map.put("companyName","TestCompany");
        map.put("name","John");
        map.put("email","John@email.com");

        return map;
    }

    @Test

    void getCountryByAssociatedBPtoUser() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getBpnCountrys?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<List<CountryDTO>> responseEntity = testRestTemplate.exchange(request, new ParameterizedTypeReference<>() {
        });
        List<CountryDTO> list = responseEntity.getBody();
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());
    }

    @Test

    void getCompanyBpns() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getCompanyBpns?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<List<BusinessPartnerDTO>> responseEntity = testRestTemplate.exchange(request,new ParameterizedTypeReference<>() {
        });
        List<BusinessPartnerDTO> list =responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());
    }

    @Test

    void getCountryFilterByISO2() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getCountryFilterByISO2?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<List<CountryDTO>> responseEntity = testRestTemplate.exchange(request,new ParameterizedTypeReference<>() {});
        List<CountryDTO> list = responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());
    }

    @Test
    void getGatesForUser() throws Exception {

        createGateForUser();
        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getAllUserBPDMGates?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<List<CompanyGatesDTO>> responseEntity = testRestTemplate.exchange(request,new ParameterizedTypeReference<>() {});
        List<CompanyGatesDTO> list = responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());
    }

    @Test
    void getGatesForNonUser() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getAllUserBPDMGates?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<List<CompanyGatesDTO>> responseEntity = testRestTemplate.exchange(request,new ParameterizedTypeReference<>() {});
        List<CompanyGatesDTO> list = responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(0,list.size());
    }


    public void createGateForUser() throws IOException {
        Map<String,Object> map = getMap();
        Company company = new Company();
        company.setCompanyName(String.valueOf(map.get("companyName")));
        CompanyUser companyUser = new CompanyUser();
        companyUser.setCompanyName(String.valueOf(map.get("companyName")));
        companyUser.setName(String.valueOf(map.get("name")));
        companyUser.setEmail(String.valueOf(map.get("email")));
        CompanyGroup companyGroup = new CompanyGroup();
        companyGroup.setCompanyGroup("TestGroup");
        companyGroup = companyGroupRepository.save(companyGroup);
        company.setCompanyGroup(companyGroup);
        company = companyRepository.save(company);
        CompanyGates companyGates = new CompanyGates();
        companyGates.setGateName("TestGate");
        companyGates.setCompanyGroup(companyGroup);
        companyGatesRepository.save(companyGates);
        companyUser.setCompany(company);
        companyUserRepository.save(companyUser);
    }

    @Test
    void getUserFromCompany() throws Exception {

        CompanyUser companyUser = new CompanyUser();
        companyUser.setName("NewUser");
        companyUser.setCompanyName("TestCompany");
        companyUser.setEmail("NewUser@email.com");

        companyUserRepository.save(companyUser);

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getUserFromCompany?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<List<CompanyUserDTO>> responseEntity = testRestTemplate.exchange(request,new ParameterizedTypeReference<>() {});
        List<CompanyUserDTO> list = responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());
    }


}


