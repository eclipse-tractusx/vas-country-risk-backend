package com.catenax.valueaddedservice.service.logic;

import com.catenax.valueaddedservice.domain.DataSource;
import com.catenax.valueaddedservice.domain.enumeration.Type;
import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.dto.DataSourceDTO;
import com.catenax.valueaddedservice.dto.DataSourceValueDTO;
import com.catenax.valueaddedservice.dto.FileDTO;
import com.catenax.valueaddedservice.service.DataSourceService;
import com.catenax.valueaddedservice.service.DataSourceValueService;
import com.catenax.valueaddedservice.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Optional;

/**
 * Service Implementation for managing {@link DataSource}.
 */
@Service
@Transactional
@Slf4j
public class UploadAndDownloadLogicService {

    @Autowired
    DataSourceService dataSourceService;

    @Autowired
    DataSourceValueService dataSourceValueService;

    @Autowired
    FileService fileService;

    public FileDTO getDataSourceTemplate(){
        Optional<FileDTO> optionalFileDTO = fileService.findUpdatedDataSourceTemplate();
        return optionalFileDTO.orElseGet(optionalFileDTO::orElseThrow);
    }



    public void saveCsv(MultipartFile file, String dataSourceName,CompanyUserDTO companyUserDTO) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader((file.getResource().getInputStream())));
        String line = "";
        DataSourceDTO dataSource = new DataSourceDTO();
        dataSource.setType(Type.Custom);
        dataSource.setCompanyUser(companyUserDTO);
        dataSource.setFileName(dataSourceName);
        dataSource.setYearPublished(Calendar.getInstance().get(Calendar.YEAR));
        dataSource.setDataSourceName(dataSourceName);
        dataSource = dataSourceService.save(dataSource);
        DataSourceValueDTO dataSourceValueDTO = new DataSourceValueDTO();
        line = br.readLine();
        while ((line = br.readLine()) != null) {
            String[] countryAndValue = line.split(";");
            dataSourceValueDTO.setCountry(countryAndValue[0]);
            dataSourceValueDTO.setContinent("World");
            dataSourceValueDTO.setScore(-1F);
            dataSourceValueDTO.setDataSource(dataSource);
            if(countryAndValue.length > 1){
                dataSourceValueDTO.setScore(Float.valueOf(countryAndValue[1]));
            }
            dataSourceValueService.save(dataSourceValueDTO);
            dataSourceValueDTO = new DataSourceValueDTO();
        }


    }



}
