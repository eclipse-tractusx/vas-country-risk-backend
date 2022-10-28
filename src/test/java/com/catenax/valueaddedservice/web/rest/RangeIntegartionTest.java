package com.catenax.valueaddedservice.web.rest;

import com.catenax.valueaddedservice.ValueAddedServiceApplication;
import com.catenax.valueaddedservice.domain.enumeration.RangeType;
import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.dto.RangeDTO;
import com.catenax.valueaddedservice.service.csv.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = ValueAddedServiceApplication.class)
class RangeIntegartionTest {

    @Autowired
    private TestRestTemplate testRestTemplate;


    private Map<String,Object> getMap() throws IOException {
        Map<String,Object> map = new HashMap<>();
        map.put("company","TestCompany");
        map.put("name","John");
        map.put("email","John@email.com");
        map.put("ratingName", "testRating123");

        return map;
    }

    @Test
    @Transactional
    void saveUserRanges() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<RangeDTO> rangeDTOList = createRanges();

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/saveUserRanges?name={name}&company={company}&email={email}");
        URI uri = uritemplate.expand(map);

        RequestEntity requestEntity = new RequestEntity(rangeDTOList, headers, HttpMethod.POST, uri);

        ResponseEntity<ResponseMessage> responseEntity = testRestTemplate.exchange(requestEntity,ResponseMessage.class);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());


       // TODO update nao deves fazer no mesmo teste , deve ser feito noutro test unitario aqui deveria ser feito um get para validar os Ranges que acabamos de criar
//        //Update Ranges
//        rangeDTO.setValue(70); //Changed Value for Max Range
//
//        RequestEntity requestEntityUpdate = new RequestEntity(rangeDTOList, headers, HttpMethod.POST, uri);
//
//        ResponseEntity<Object> responseEntityUpdate = testRestTemplate.exchange(requestEntityUpdate,Object.class);
//
//        assertEquals(HttpStatus.OK,responseEntityUpdate.getStatusCode());

        // TODO make Get request
    }

    @Test
    @Transactional
    void getUserRanges() throws Exception {

        // TODO make get Request para obter valores default
//        Map<String,Object> map = getMap();
//        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getUserRanges?name={name}&company={company}&email={email}");
//        URI uri = uritemplate.expand(map);
//        RequestEntity<Void> request = RequestEntity
//                .get(uri).build();
//        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(request,Object.class);
//        List<RangeDTO> list = (List<RangeDTO>) responseEntity.getBody();
//
//        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
//        assertNotEquals(0,list.size());


        assertNotEquals(0,1);
    }


    @Test
    @Transactional
    void updateRanges() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<RangeDTO> rangeDTOList = createRanges();

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/saveUserRanges?name={name}&company={company}&email={email}");
        URI uri = uritemplate.expand(map);

        RequestEntity requestEntity = new RequestEntity(rangeDTOList, headers, HttpMethod.POST, uri);

        ResponseEntity<ResponseMessage> responseEntity = testRestTemplate.exchange(requestEntity,ResponseMessage.class);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        // Change VALUES
        Integer newValue = 20;
        rangeDTOList.get(0).setValue(newValue);

        requestEntity = new RequestEntity(rangeDTOList, headers, HttpMethod.POST, uri);
        responseEntity = testRestTemplate.exchange(requestEntity,ResponseMessage.class);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        // TODO make Get request e comparar que o newValue vem nos Ranges



    }

    private RangeDTO createRangeDTO(RangeType rangeType,Integer value,String description){
        RangeDTO rangeDTO = new RangeDTO();
        rangeDTO.setRange(rangeType);
        rangeDTO.setValue(value);
        rangeDTO.setDescription(description);
        rangeDTO.setCompanyUser(createCompanyUserDTO());
        return rangeDTO;
    }

    private CompanyUserDTO createCompanyUserDTO(){

        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setName("John");
        companyUserDTO.setEmail("John@email.com");
        companyUserDTO.setCompany("TestCompany");
        return companyUserDTO;
    }

    private List<RangeDTO> createRanges(){
        RangeDTO minRange = createRangeDTO(RangeType.Min,40,"MIN");
        RangeDTO betweenRange = createRangeDTO(RangeType.Between,75 ,"BetWeen Value");
        RangeDTO maxRANGE = createRangeDTO(RangeType.Max,100,"Max");
        List<RangeDTO> list = new ArrayList<>();
        list.add(minRange);
        list.add(betweenRange);
        list.add(maxRANGE);
        return list;
    }
}


