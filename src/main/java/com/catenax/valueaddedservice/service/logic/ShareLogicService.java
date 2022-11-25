package com.catenax.valueaddedservice.service.logic;

import com.catenax.valueaddedservice.dto.*;
import com.catenax.valueaddedservice.dto.ShareDTOs.ShareDTO;
import com.catenax.valueaddedservice.dto.ShareDTOs.ShareRatingDTO;
import com.catenax.valueaddedservice.service.DataSourceValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ShareLogicService {

    @Autowired
    DataSourceValueService dataSourceValueService;

    @Autowired
    ExternalBusinessPartnersLogicService externalBusinessPartnersLogicService;

    public List<ShareDTO> getMappedRatings(List<DataSourceDTO> datasource, List<BusinessPartnerDTO> businessPartner, CompanyUserDTO companyUser) {

        List<ShareDTO> shareDTOSList = new ArrayList<>();
        
        List<BusinessPartnerDTO> businessPartnerDTOS;
        businessPartnerDTOS = externalBusinessPartnersLogicService.getExternalBusinessPartners(companyUser);

        businessPartner.forEach(bp -> {
            if(bp.getCountry() == null){
                businessPartnerDTOS.forEach(bpDTO -> {
                   if(bp.getBpn().equals(bpDTO.getBpn())){
                       bp.setCountry(bpDTO.getCountry());
                   }
                });
            }
        });

        List<String> countryList = new ArrayList<>();;
        countryList.addAll(businessPartner.stream().map(BusinessPartnerDTO::getCountry).collect(Collectors.toSet()));

        List<String> dataSources = datasource.stream().map(DataSourceDTO::getDataSourceName).collect(Collectors.toList());
        List<DataDTO> dataDTOS = new ArrayList<>();

        datasource.forEach(ds -> {
            if (!dataSources.isEmpty() && ds.getYearPublished() != null && ds.getYearPublished() > 0) {
                dataDTOS.addAll(dataSourceValueService.findByRatingAndCountryAndScoreGreaterThanAndYear(Float.valueOf(-1), countryList, dataSources, ds.getYearPublished()));
            }
        });

        final ShareDTO[] shareDTOS = {new ShareDTO()};
        final int[] id = {1};
        businessPartner.forEach(bp -> {
            shareDTOS[0] = setShareDTO(bp,dataDTOS,id[0]);
            id[0]++;
            shareDTOSList.add(shareDTOS[0]);
        });
        
        return shareDTOSList;
    }

    private ShareDTO setShareDTO(BusinessPartnerDTO bp, List<DataDTO> dataDTOS, Integer id ) {
        List<ShareRatingDTO> shareRatingDTOList = new ArrayList<>();
        ShareDTO shareDTO = new ShareDTO();
        shareDTO.setId(Long.valueOf(id));
        shareDTO.setBpn(bp.getBpn());
        shareDTO.setCountry(bp.getCountry());
        dataDTOS.forEach(dataDTO -> {
            if(Objects.equals(dataDTO.getCountry(), bp.getCountry())){
                ShareRatingDTO shareRatingDTO = new ShareRatingDTO();
                shareRatingDTO.setDataSourceName(dataDTO.getDataSourceName());
                shareRatingDTO.setScore(dataDTO.getScore());
                shareRatingDTOList.add(shareRatingDTO);
                shareDTO.setIso2(dataDTO.getIso2());
            }
        });
        shareDTO.setRating(shareRatingDTOList);
        return shareDTO;
    }
}
