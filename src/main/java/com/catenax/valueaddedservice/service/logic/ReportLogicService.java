package com.catenax.valueaddedservice.service.logic;

import com.catenax.valueaddedservice.domain.enumeration.Type;
import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.dto.ReportDTO;
import com.catenax.valueaddedservice.dto.ReportValuesDTO;
import com.catenax.valueaddedservice.service.ReportService;
import com.catenax.valueaddedservice.service.ReportValuesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReportLogicService {

    @Autowired
    ReportService reportService;

    @Autowired
    ReportValuesService reportValuesService;


    @Cacheable(value = "vas-reports", key = "{#root.methodName , {#companyUserDTO.name,#companyUserDTO.email,#companyUserDTO.companyName}}", unless = "#result == null")
    public List<ReportDTO> getReportsForCompanyUser(CompanyUserDTO companyUserDTO)  {
        return reportService.findByCompanyUserNameAndCompanyAndType(companyUserDTO.getName(),companyUserDTO.getCompanyName(), Type.Custom);
    }


    @Cacheable(value = "vas-reports", key = "{#root.methodName, #type}", unless = "#result == null")
    public List<ReportDTO> getGlobalReports()  {
        return reportService.findByGlobalType( Type.Global);
    }


    @Cacheable(value = "vas-reports", key = "{#root.methodName , #companyUserDTO.companyName}", unless = "#result == null")
    public List<ReportDTO> getCompanyReports(CompanyUserDTO companyUserDTO)  {
        return reportService.findByCompanyAndType(companyUserDTO.getCompanyName(),Type.Company);
    }

    public void saveReport(ReportDTO reportDTO,CompanyUserDTO companyUserDTO)  {
        reportDTO.setCompany(companyUserDTO.getCompanyName());
        reportDTO.setCompanyUserName(companyUserDTO.getName());
        ReportDTO reportCreated = reportService.save(reportDTO);
        if(reportDTO.getReportValuesDTOList() != null && !reportDTO.getReportValuesDTOList().isEmpty()){
            reportDTO.getReportValuesDTOList().forEach(reportValuesDTO -> reportValuesService.save(reportValuesDTO,reportCreated));
        }
    }


    @Cacheable(value = "vas-reports", key = "{#root.methodName , #reportDTO}", unless = "#result == null")
    public List<ReportValuesDTO> getReportValues(ReportDTO reportDTO){
        if(reportDTO == null || reportDTO.getId() == null){
            return new ArrayList<>();
        }
        return reportValuesService.findByReport(reportDTO);
    }

    @CacheEvict(value = "vas-reports", allEntries = true)
    public void invalidateAllCache() {
        log.debug("invalidateAllCache|vas-Reports -  invalidated cache - allEntries");
    }
}
