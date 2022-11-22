package com.catenax.valueaddedservice.service.logic;

import com.catenax.valueaddedservice.dto.*;
import com.catenax.valueaddedservice.service.DataSourceValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ShareLogicService {

    @Autowired
    DataSourceValueService dataSourceValueService;

    @Autowired
    CountryLogicService countryLogicService;

    @Autowired
    ExternalBusinessPartnersLogicService externalBusinessPartnersLogicService;

    /*public List<ShareDTO> getMappedRatings(Integer year, List<RatingDTO> ratingDTOList, CompanyUserDTO companyUser) {

        List<String> dataSources = ratingDTOList.stream().map(RatingDTO::getDataSourceName).collect(Collectors.toList());
        List<DataDTO> dataDTOS = new ArrayList<>();

        List<BusinessPartnerDTO> businessPartnerDTOS;
        businessPartnerDTOS = externalBusinessPartnersLogicService.getExternalBusinessPartners(companyUser);
        List<String> countryList = externalBusinessPartnersLogicService.getExternalPartnersCountry(companyUser);

        if (!dataSources.isEmpty() && year != null && year > 0) {
            dataDTOS = dataSourceValueService.findByRatingAndCountryAndScoreGreaterThanAndYear(Float.valueOf(-1), countryList, dataSources, year);
        }

        dataDTOS.forEach(each-> ratingDTOList.forEach(eachData->{
            if(each.getDataSourceName().equalsIgnoreCase(eachData.getDataSourceName())){
                each.setWeight(eachData.getWeight());
            }
        }));
        return mapBusinessPartnerToRatings(businessPartnerDTOS,dataDTOS,ratingDTOList);
    }

    private List<ShareDTO> mapBusinessPartnerToRatings(List<BusinessPartnerDTO> businessPartnerDTOS,  List<DataDTO> dataDTOS, List<RatingDTO> ratingDTOS) {
        List<ShareDTO> shareDTOS = new ArrayList<>();
        final ShareDTO[] shareDTO = {new ShareDTO()};
        final int[] id = {1};
        businessPartnerDTOS.forEach(businessPartnerDTO -> {
            shareDTO[0] = setBusinessPartnerProps(businessPartnerDTO,id[0]);
            id[0]++;
            shareDTOS.add(shareDTO[0]);
        });

        for(ShareDTO d: shareDTOS){
            mapScoreForEachBpn(d,dataDTOS,ratingDTOS);
        }

        return shareDTOS;
    }

    private ShareDTO setBusinessPartnerProps(BusinessPartnerDTO businessPartnerDTO,Integer id) {
        ShareDTO shareDTO = new ShareDTO();
        shareDTO.setBpn(businessPartnerDTO.getBpn());
        shareDTO.setCountry(businessPartnerDTO.getCountry());;
        shareDTO.setId(Long.valueOf(id));
        return shareDTO;
    }

    private ShareDTO mapScoreForEachBpn(ShareDTO d, List<DataDTO> dataDTOS,List<RatingDTO> ratingDTOS){
        List<DataDTO> dataSourceForCountry = dataDTOS.stream().filter(each -> each.getCountry().equalsIgnoreCase(d.getCountry())).collect(Collectors.toList());
        //List<RatingDTO> ratingDTOList = null;
        dataSourceForCountry.stream().forEach(eachDataSource -> {
            //RatingDTO ratingDTO = new RatingDTO();
            //eachDataSource.setDataSourceName(eachDataSource.getDataSourceName());
            //eachDataSource.setWeight(eachDataSource.getScore());
            //ratingDTOList.add(ratingDTO);
            d.setName(eachDataSource.getDataSourceName());
            d.setScore(eachDataSource.getScore());
            d.setIso2(eachDataSource.getIso2());
        });

        //d.setRating(ratingDTOList);
        return d;
    }*/

}
