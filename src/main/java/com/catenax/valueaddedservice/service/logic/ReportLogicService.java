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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class ReportLogicService {

    @Autowired
    ReportService reportService;

    @Autowired
    ReportValuesService reportValuesService;

    @Transactional
    @Cacheable(value = "vas-reports", key = "{#root.methodName , {#companyUserDTO.name,#companyUserDTO.email,#companyUserDTO.company}}", unless = "#result == null")
    public List<ReportDTO> getReportsForCompanyUser(CompanyUserDTO companyUserDTO)  {
        return reportService.findByCompanyUserNameAndCompanyAndType(companyUserDTO.getName(),companyUserDTO.getCompany(), Type.Custom);
    }

    @Transactional
    @Cacheable(value = "vas-reports", key = "{#root.methodName }", unless = "#result == null")
    public List<ReportDTO> getGlobalReports()  {
        return reportService.findByGlobalType( Type.Global);
    }

    @Transactional
    @Cacheable(value = "vas-reports", key = "{#root.methodName , {#companyUserDTO.company}}", unless = "#result == null")
    public List<ReportDTO> getCompanyReports(CompanyUserDTO companyUserDTO)  {
        return reportService.findByCompanyAndType(companyUserDTO.getCompany(),Type.Company);
    }

    public void saveReport(ReportDTO reportDTO,CompanyUserDTO companyUserDTO)  {
        reportDTO.setCompany(companyUserDTO.getCompany());
        reportDTO.setCompanyUserName(companyUserDTO.getName());
        ReportDTO reportCreated = reportService.save(reportDTO);
        if(reportDTO.getReportValuesDTOList() != null && !reportDTO.getReportValuesDTOList().isEmpty()){
            reportDTO.getReportValuesDTOList().forEach(reportValuesDTO -> reportValuesService.save(reportValuesDTO,reportCreated));
        }
    }

    @Transactional
    @Cacheable(value = "vas-reports", key = "{#root.methodName , {#reportDTO}}", unless = "#result == null")
    public List<ReportValuesDTO> getReportValues(ReportDTO reportDTO){
        return reportValuesService.findByReport(reportDTO);
    }

    @CacheEvict(value = "vas-reports", allEntries = true)
    public void invalidateAllCache() {
        log.debug("invalidateAllCache|vas-Reports -  invalidated cache - allEntries");
    }
}
