package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.domain.DataSource;
import com.catenax.valueaddedservice.domain.enumeration.RangeType;
import com.catenax.valueaddedservice.domain.enumeration.Type;
import com.catenax.valueaddedservice.dto.*;
import com.catenax.valueaddedservice.repository.RangeRepository;
import com.catenax.valueaddedservice.service.csv.CSVFileReader;
import com.catenax.valueaddedservice.service.csv.ResponseMessage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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



    public void saveCsv(MultipartFile file, String Filename, String DataSourceName) {
        try {
            List<DataSourceValueDTO> csvData = CSVFileReader.getCsvData(file.getInputStream());

            for (DataSourceValueDTO csvDatum : csvData) {

                List<CountryDTO> countryDTOS = new ArrayList<>();

                countryDTOS = countryService.findCountryByName(csvDatum.getCountry());
                if(countryDTOS.get(0).getIso2() == null || countryDTOS.get(0).getIso3() == null){
                    //Need to throw error message for incorrect values in CSV!
                    System.out.println("Valores Incorretos");
                }else {
                    csvDatum.setIso2(countryDTOS.get(0).getIso2());
                    csvDatum.setIso3(countryDTOS.get(0).getIso3());
                    csvDatum.setContinent(countryDTOS.get(0).getContinent());
                }

                // TODO para alem de criares o data source value tens de criar um DataSourceDTO que vai ser a entidade Pai na relação csvDatum.setDataSource();

                dataSourceService.findRatingByUser(null,DataSourceName);

                if(dataSourceService.findRatingByUser(null,DataSourceName).size() > 0) {
                    System.out.println("Já existe");
                }else{
                    DataSourceDTO dsDto = new DataSourceDTO();
                    dsDto.setDataSourceName(DataSourceName);
                    dsDto.setCompanyUser(null);
                    dsDto.setFileName(null);
                    dsDto.setType(Type.Global);
                    dsDto.setYearPublished(2021);

                    dataSourceService.save(dsDto);
                    //csvDatum.setDataSource(dsDto);
                    dataSourceValueService.save(csvDatum);
                }

                System.out.println("VALORES:" + csvDatum);

            }
        } catch (IOException e) {
            throw new RuntimeException("Fail to store csv data: " + e.getMessage());
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
    public void saveRanges(Integer rangeHigh, Integer rangeMid, Integer rangeLow) throws Exception {
        List<RangeDTO> RangeDto;
        RangeDto = rangeService.getAllRangesList(null);

        RangeDTO rangeHighDTO = new RangeDTO();
        RangeDTO rangeMidDTO = new RangeDTO();
        RangeDTO rangeLowDTO = new RangeDTO();

        if(rangeLow >= rangeMid || rangeMid >= rangeHigh){
            //Needs to return error message!
            throw new Exception("Ranges Overlap!");
        }

        if(RangeDto.size() == 0){

            //High Value Range
            rangeHighDTO.setRange(RangeType.Max);
            rangeHighDTO.setDescription("HighValue");
            rangeHighDTO.setValue(rangeHigh);
            rangeHighDTO.setCompanyUser(null);

            //Medium Value Range
            rangeMidDTO.setRange(RangeType.Between);
            rangeMidDTO.setDescription("BetweenValue");
            rangeMidDTO.setValue(rangeMid);
            rangeMidDTO.setCompanyUser(null);

            //Low Value Range
            rangeLowDTO.setRange(RangeType.Min);
            rangeLowDTO.setDescription("LowValue");
            rangeLowDTO.setValue(rangeLow);
            rangeLowDTO.setCompanyUser(null);

            rangeService.save(rangeHighDTO);
            rangeService.save(rangeMidDTO);
            rangeService.save(rangeLowDTO);
        }

        for (RangeDTO rangeDTO : RangeDto) {
            if (rangeDTO.getCompanyUser() == null) { //Change null for the User Variable
                if (rangeDTO.getRange().equals(RangeType.Max)) {

                    //High Value Range
                    rangeHighDTO.setValue(rangeHigh);

                    rangeService.updateRange(rangeHigh, rangeDTO.getId());
                }

                else if (rangeDTO.getRange().equals(RangeType.Between)){

                    //Medium Value Range
                    rangeMidDTO.setRange(RangeType.Between);
                    rangeMidDTO.setDescription("BetweenValue");
                    rangeMidDTO.setValue(rangeMid);
                    rangeMidDTO.setCompanyUser(null);

                    rangeService.partialUpdate(rangeMidDTO);
                }

                else if (rangeDTO.getRange().equals(RangeType.Min)){

                    //Low Value Range
                    rangeLowDTO.setRange(RangeType.Min);
                    rangeLowDTO.setDescription("LowValue");
                    rangeLowDTO.setValue(rangeLow);
                    rangeLowDTO.setCompanyUser(null);

                    rangeService.partialUpdate(rangeLowDTO);
                }
            }
        }
    }



}
