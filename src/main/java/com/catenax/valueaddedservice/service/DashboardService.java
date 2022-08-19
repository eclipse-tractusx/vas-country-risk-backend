package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.domain.DataSource;
import com.catenax.valueaddedservice.domain.enumeration.Type;
import com.catenax.valueaddedservice.dto.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link DataSource}.
 */
@Service
@Transactional
@Slf4j
public class DashboardService {

    @Autowired
    DataSourceService dataSourceService;

    @Autowired
    DataSourceValueService dataSourceValueService;

    @Autowired
    RangeService rangeService;

    @Autowired
    CompanyUserService companyUserService;

    @Autowired
    CountryService countryService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    FileService fileService;


    @Value(value = "classpath:config/liquibase/fake-data/dashboard.json")
    private Resource json;

    public List<DashBoardTableDTO> getTableInfo(Integer year, List<RatingDTO> ratingDTOList, CompanyUserDTO companyUser) {
        log.debug("Request to get Table Info");
        List<String> dataSources = ratingDTOList.stream().map(RatingDTO::getDataSourceName).collect(Collectors.toList());
        List<DataDTO> dataDTOS = new ArrayList<>();
        List<BusinessPartnerDTO> businessPartnerDTOS;
        businessPartnerDTOS = getExternalBusinessPartners(companyUser);
        List<String> countryList = new ArrayList<>();
        countryList.addAll(businessPartnerDTOS.stream().map(BusinessPartnerDTO::getCountry)
                .collect(Collectors.toSet()));

        if (!dataSources.isEmpty()) {
            if (year != null && year > 0) {
                dataDTOS = dataSourceValueService.findByRatingAndCountryAndScoreGreaterThanAndYear(Float.valueOf(-1), countryList, dataSources, year);
            } else {
                dataDTOS = dataSourceValueService.findByRatingAndCountryAndScoreGreaterThan(Float.valueOf(-1), countryList, dataSources);
            }
        }

        dataDTOS.forEach(each-> ratingDTOList.forEach(eachData->{
            if(each.getDataSourceName().equalsIgnoreCase(eachData.getDataSourceName())){
                each.setWeight(eachData.getWeight());
            }
        }));
        return mapBusinessPartnerToDashboard(businessPartnerDTOS,dataDTOS,ratingDTOList);
    }

    public List<DashBoardWorldMapDTO> getWorldMapInfo(Integer year, List<RatingDTO> ratingDTOList, CompanyUserDTO companyUser) {
        log.debug("Request to get WorldMap Info");
        List<String> dataSources = ratingDTOList.stream().map(RatingDTO::getDataSourceName).collect(Collectors.toList());
        List<DataDTO> dataDTOS = new ArrayList<>();

        if (!dataSources.isEmpty()) {
            if (year != null && year > 0) {
                dataDTOS = dataSourceValueService.findByRatingAndScoreGreaterThanAndYear(Float.valueOf(-1), dataSources, year);
            } else {
                dataDTOS = dataSourceValueService.findByRatingAndScoreGreaterThan(Float.valueOf(-1), dataSources);
            }
        }

        dataDTOS.forEach(each-> ratingDTOList.forEach(eachData->{
            if(each.getDataSourceName().equalsIgnoreCase(eachData.getDataSourceName())){
                each.setWeight(eachData.getWeight());
            }
        }));
        return mapDataSourcesToWorldMap(dataDTOS,ratingDTOList,companyUser);
    }

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
            dataSourceValueDTO.setScore(0F);
            dataSourceValueDTO.setDataSource(dataSource);
            if(countryAndValue.length > 1){
                dataSourceValueDTO.setScore(Float.valueOf(countryAndValue[1]));
            }
            dataSourceValueService.save(dataSourceValueDTO);
            dataSourceValueDTO = new DataSourceValueDTO();
        }


    }

    private List<DashBoardTableDTO> mapBusinessPartnerToDashboard(List<BusinessPartnerDTO> businessPartnerDTOS,  List<DataDTO> dataDTOS,List<RatingDTO> ratingDTOS) {
        List<DashBoardTableDTO> dashBoardTableDTOS = new ArrayList<>();
        final DashBoardTableDTO[] dashBoardTableDTO = {new DashBoardTableDTO()};
        final int[] id = {1};
        businessPartnerDTOS.forEach(businessPartnerDTO -> {
            dashBoardTableDTO[0] = setBusinessPartnerProps(businessPartnerDTO,id[0]);
            id[0]++;
            dashBoardTableDTOS.add(dashBoardTableDTO[0]);
        });

        for(DashBoardTableDTO d: dashBoardTableDTOS){
            mapScoreForEachBpn(d,dataDTOS,ratingDTOS);
        }

        return dashBoardTableDTOS;
    }

    private List<DashBoardWorldMapDTO> mapDataSourcesToWorldMap(List<DataDTO> dataDTOS,List<RatingDTO> ratingDTOS,CompanyUserDTO companyUser){
        List<String> countryList = new ArrayList<>();
        countryList.addAll(dataDTOS.stream().map(DataDTO::getCountry)
                .collect(Collectors.toSet()));
        List<DashBoardWorldMapDTO> dashBoardWorldMapDTOS = new ArrayList<>();
        final DashBoardWorldMapDTO[] dashBoardWorldMapDTO = {new DashBoardWorldMapDTO()};
        List<BusinessPartnerDTO> businessPartnerDTOS;
        businessPartnerDTOS = getExternalBusinessPartners(companyUser);
        countryList.forEach(country->{
            final float[] generalFormulaTotal = {0F};
            float total = 100F;
            List<DataDTO> dataSources = dataDTOS.stream().filter(dataDTO -> dataDTO.getCountry().equalsIgnoreCase(country)).collect(Collectors.toList());
            float eachWeight = total/dataSources.size();
            dataSources.forEach(dataDTO -> generalFormulaTotal[0] = generalFormulaTotal[0] + calculateFinalScore(ratingDTOS.size(),dataSources.size(),dataDTO,eachWeight));
            dashBoardWorldMapDTO[0] = new DashBoardWorldMapDTO();
            dashBoardWorldMapDTO[0].setCountry(country);
            dashBoardWorldMapDTO[0].setScore(generalFormulaTotal[0]);
            dashBoardWorldMapDTO[0].setBusinessPartnerDTOList(
                    businessPartnerDTOS.stream().filter(businessPartnerDTO -> businessPartnerDTO.getCountry().equalsIgnoreCase(country)).collect(Collectors.toList()));

            dashBoardWorldMapDTOS.add(dashBoardWorldMapDTO[0]);
        });
        return dashBoardWorldMapDTOS;
    }
    private DashBoardTableDTO mapScoreForEachBpn(DashBoardTableDTO d, List<DataDTO> dataDTOS,List<RatingDTO> ratingDTOS){
        List<DataDTO> dataSourceForCountry = dataDTOS.stream().filter(each -> each.getCountry().equalsIgnoreCase(d.getCountry())).collect(Collectors.toList());
        float total = 100F;
        float eachWeight = total/dataSourceForCountry.size();
        final float[] generalFormulaTotal = {0F};
        final String[] ratingsList = {""};
        dataSourceForCountry.stream().forEach(eachDataSource -> {
            generalFormulaTotal[0] = generalFormulaTotal[0] + calculateFinalScore(ratingDTOS.size(),dataSourceForCountry.size(),eachDataSource,eachWeight);
            ratingsList[0] = concatenateRatings(ratingsList[0], eachDataSource);
        });
        d.setScore(generalFormulaTotal[0]);
        d.setRating(ratingsList[0]);
        return d;
    }

    private DashBoardTableDTO setBusinessPartnerProps(BusinessPartnerDTO businessPartnerDTO,Integer id) {
        DashBoardTableDTO dashBoardTableDTO = new DashBoardTableDTO();
        dashBoardTableDTO.setBpn(businessPartnerDTO.getBpn());
        dashBoardTableDTO.setCity(businessPartnerDTO.getCity());
        dashBoardTableDTO.setCountry(businessPartnerDTO.getCountry());
        dashBoardTableDTO.setAddress(businessPartnerDTO.getAddress());
        dashBoardTableDTO.setLegalName(businessPartnerDTO.getLegalName());
        dashBoardTableDTO.setId(Long.valueOf(id));
        return dashBoardTableDTO;
    }

    private String concatenateRatings(String ratingsList, DataDTO element) {
        if (ratingsList.isEmpty()) {
            ratingsList = element.getDataSourceName();
        } else {
            ratingsList = ratingsList + "," + element.getDataSourceName();
        }

        return ratingsList;
    }

    private Float calculateFinalScore(Integer ratingDTOSize, Integer dataSourceCountrySize,DataDTO eachDataSource,Float eachWeight) {
        Float generalFormulaTotal = 0F;
        if(ratingDTOSize == dataSourceCountrySize){
            generalFormulaTotal= generalFormulaTotal + (eachDataSource.getScore() * (eachDataSource.getWeight() * 0.01F));
        }else{
            generalFormulaTotal = generalFormulaTotal+ (eachDataSource.getScore() * (eachWeight * 0.01F));
        }

        return generalFormulaTotal;
    }

    private List<BusinessPartnerDTO> getExternalBusinessPartners(CompanyUserDTO companyUser) {
        try {

            return objectMapper.readValue(json.getInputStream(), new TypeReference<List<BusinessPartnerDTO>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Ranges
    public void saveRanges(List<RangeDTO> rangeDTOS,CompanyUserDTO companyUserDTO) throws Exception {
        List<RangeDTO> list = rangeService.getAllRangesList(companyUserDTO);
        if (list.isEmpty()) {
            rangeDTOS.forEach(rangeDTO -> rangeService.save(rangeDTO));
        } else {
            rangeDTOS.forEach(rangeDTO -> rangeService.updateRanges(rangeDTO));
        }
    }



}
