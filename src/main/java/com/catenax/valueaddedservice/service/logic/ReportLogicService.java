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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReportLogicService {

    @Autowired
    ReportService reportService;

    @Autowired
    ReportValuesService reportValuesService;

    @Autowired
    CompanyUserLogicService companyUserLogicService;


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
        if(reportDTO.getId() != null){
            updateReport(reportDTO,companyUserDTO);
        }else{
            reportDTO.setCompany(companyUserDTO.getCompanyName());
            reportDTO.setCompanyUserName(companyUserDTO.getName());
            ReportDTO reportCreated = reportService.save(reportDTO);
            if(reportDTO.getReportValuesDTOList() != null && !reportDTO.getReportValuesDTOList().isEmpty()){
                reportDTO.getReportValuesDTOList().forEach(reportValuesDTO -> reportValuesService.save(reportValuesDTO,reportCreated));
            }
        }
    }

    public void updateReport(ReportDTO updatedReportDTO,CompanyUserDTO companyUserDTO){
        Optional<ReportDTO> optionalReportDTO = reportService.findOne(updatedReportDTO.getId());
        ReportDTO reportDTO = optionalReportDTO.orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        });
        if( validatePermissionToChangeReport(reportDTO,companyUserDTO)){
            List<ReportValuesDTO> reportValuesDTOList = reportValuesService.findByReport(reportDTO);
            reportValuesDTOList.forEach(reportValuesDTO -> reportValuesService.delete(reportValuesDTO.getId()));
            updatedReportDTO.getReportValuesDTOList().forEach(reportValuesDTO -> reportValuesService.save(reportValuesDTO,updatedReportDTO));
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

    }

    public void deleteReportById(Long reportId,CompanyUserDTO companyUserDTO){
        Optional<ReportDTO> optionalReportDTO = reportService.findOne(reportId);
        ReportDTO reportDTO = optionalReportDTO.orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        });
        if(validatePermissionToChangeReport(reportDTO,companyUserDTO)){
            List<ReportValuesDTO> reportValuesDTOList = reportValuesService.findByReport(reportDTO);
            reportValuesDTOList.forEach(reportValuesDTO -> reportValuesService.delete(reportValuesDTO.getId()));
            reportService.delete(reportDTO.getId());
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
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



    public boolean validatePermissionToChangeReport(ReportDTO reportDTO,CompanyUserDTO companyUserDTO){
        if((reportDTO.getCompany().equalsIgnoreCase(companyUserDTO.getCompanyName())
                && reportDTO.getCompanyUserName().equalsIgnoreCase(companyUserDTO.getName())) || companyUserLogicService.isAdmin()){
           return true;
        }
        return false;
    }
}
