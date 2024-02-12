/********************************************************************************
* Copyright (c) 2022,2024 BMW Group AG 
* Copyright (c) 2022,2024 Contributors to the Eclipse Foundation
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
package org.eclipse.tractusx.valueaddedservice.service.logic;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.valueaddedservice.constants.VasConstants;
import org.eclipse.tractusx.valueaddedservice.domain.DataSource;
import org.eclipse.tractusx.valueaddedservice.domain.enumeration.Type;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyUserDTO;
import org.eclipse.tractusx.valueaddedservice.dto.DataSourceDTO;
import org.eclipse.tractusx.valueaddedservice.dto.DataSourceValueDTO;
import org.eclipse.tractusx.valueaddedservice.dto.FileDTO;
import org.eclipse.tractusx.valueaddedservice.service.DataSourceService;
import org.eclipse.tractusx.valueaddedservice.service.DataSourceValueService;
import org.eclipse.tractusx.valueaddedservice.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Service Implementation for managing {@link DataSource}.
 */
@Service
@Slf4j
public class UploadAndDownloadLogicService {

    @Autowired
    DataSourceService dataSourceService;

    @Autowired
    DataSourceValueService dataSourceValueService;

    @Autowired
    FileService fileService;

    public FileDTO getDataSourceTemplate(){
        log.debug("getDataSourceTemplate get Template");
        Optional<FileDTO> optionalFileDTO = fileService.findUpdatedDataSourceTemplate();
        return optionalFileDTO.orElseGet(optionalFileDTO::orElseThrow);
    }



    public void saveCsv(MultipartFile file, String dataSourceName,CompanyUserDTO companyUserDTO, Integer year, String type) throws IOException {
        Type typePermission = getStringType(type);
        log.debug("save new CSV Rating");
        BufferedReader br = new BufferedReader(new InputStreamReader(file.getResource().getInputStream(), StandardCharsets.UTF_8));
        String line = "";
        DataSourceDTO dataSource = new DataSourceDTO();
        dataSource.setType(typePermission);
        dataSource.setCompanyUser(companyUserDTO);
        dataSource.setFileName(dataSourceName);
        dataSource.setYearPublished(year);
        dataSource.setDataSourceName(dataSourceName);
        dataSource = dataSourceService.save(dataSource);
        DataSourceValueDTO dataSourceValueDTO = new DataSourceValueDTO();
        line = br.readLine();
        while ((line = br.readLine()) != null) {
            String[] countryAndValue = line.split(";");
            dataSourceValueDTO.setCountry(countryAndValue[1]);
            dataSourceValueDTO.setContinent(countryAndValue[0]);
            dataSourceValueDTO.setIso2(countryAndValue[3]);
            dataSourceValueDTO.setIso3(countryAndValue[2]);
            dataSourceValueDTO.setScore(-1F);
            dataSourceValueDTO.setDataSource(dataSource);
            if(countryAndValue.length > 4){
                if(Float.valueOf(countryAndValue[4]) < 0 || Float.valueOf(countryAndValue[4]) > 100){
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, VasConstants.UPLOAD_ERROR_MESSAGE_ON_SCORES + countryAndValue[1]);
                }
                dataSourceValueDTO.setScore(Float.valueOf(countryAndValue[4]));
            }
            dataSourceValueService.save(dataSourceValueDTO);
            dataSourceValueDTO = new DataSourceValueDTO();
        }
        br.close();

    }

    public Type getStringType(String type){
        if(type.equals("Company")){
            return Type.Company;
        }
        else if(type.equals("Custom")){
            return Type.Custom;
        }
        else {
            return Type.Global;
        }
    }



}
