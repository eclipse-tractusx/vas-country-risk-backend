package com.catenax.valueaddedservice.web.rest;

import com.catenax.valueaddedservice.dto.*;
import com.catenax.valueaddedservice.repository.ReportRepository;
import com.catenax.valueaddedservice.repository.ReportValuesRepository;
import com.catenax.valueaddedservice.service.DashboardService;
import com.catenax.valueaddedservice.service.csv.ResponseMessage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.catenax.valueaddedservice.constants.VasConstants.RATINGS_TAG;


@RestController
@RequestMapping("/api")
@Tag(name = "Dashboard Controller")
@SecurityRequirements({@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "open_id_scheme")})
public class DashBoardResource {

    private final Logger log = LoggerFactory.getLogger(DashBoardResource.class);


    @Autowired
    DashboardService dashboardService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ReportValuesRepository reportValuesRepository;

    @Autowired
    ReportRepository reportRepository;

    @Operation(summary = "Retrieves Business partners based on selected ratings, year and current user")
    @ApiResponses(value = {@ApiResponse (responseCode = "200", description = "Business partners request with success based on selected variables "),
            @ApiResponse (responseCode = "401", description = "Authentication Required", content = @Content)})
    @GetMapping("/dashboard/getTableInfo")
    public ResponseEntity<List<DashBoardTableDTO>> getAllDashBoardTable(@RequestHeader HttpHeaders headers ,@RequestParam Map<String, Object> ratings,
                                                                        @RequestParam(value = "year", defaultValue = "0", required = false) Integer year,
                                                                        CompanyUserDTO companyUser) throws IOException {
        log.debug("REST request to get a page of Dashboard");
        List<DashBoardTableDTO> dashBoardTableDTOs;
        List<RatingDTO> ratingDTOS = new ArrayList<>();
        if (ratings.get(RATINGS_TAG) != null && !String.valueOf(ratings.get(RATINGS_TAG)).isEmpty()) {
            ratingDTOS = objectMapper.readValue(String.valueOf(ratings.get(RATINGS_TAG)), new TypeReference<>() {
            });
        }

        dashBoardTableDTOs = dashboardService.getTableInfo(year, ratingDTOS, companyUser);
        return ResponseEntity.ok().body(dashBoardTableDTOs);
    }

    @Operation(summary = "Retrieves a score based on selected ratings, year and current user")
    @ApiResponses(value = {@ApiResponse (responseCode = "200", description = "World map information requested with success"),
                        @ApiResponse (responseCode = "401", description = "Authentication Required", content = @Content)})
    @GetMapping("/dashboard/getWorldMap")
    public ResponseEntity<List<DashBoardWorldMapDTO>> getDashBoardWorldMap(@RequestParam Map<String, Object> ratings,
                                                                           @RequestParam(value = "year", defaultValue = "0", required = false) Integer year,
                                                                           CompanyUserDTO companyUser) throws IOException {
        log.debug("REST request to get a page of Dashboard");
        List<DashBoardWorldMapDTO> dashBoardWorldMapDTOS;
        List<RatingDTO> ratingDTOS = new ArrayList<>();
        if (ratings.get(RATINGS_TAG) != null && !String.valueOf(ratings.get(RATINGS_TAG)).isEmpty()) {
            ratingDTOS = objectMapper.readValue(String.valueOf(ratings.get(RATINGS_TAG)), new TypeReference<>() {
            });
        }

        dashBoardWorldMapDTOS = dashboardService.getWorldMapInfo(year, ratingDTOS, companyUser);
        return ResponseEntity.ok().body(dashBoardWorldMapDTOS);
    }

    @Operation(summary = "Retrieves all years saved on the database")
    @ApiResponses(value = {@ApiResponse (responseCode = "200", description = "All years requested with success"),
            @ApiResponse (responseCode = "401", description = "Authentication Required", content = @Content)})
    @GetMapping("/dashboard/allYears")
    public ResponseEntity<List<Integer>> getYears(CompanyUserDTO companyUser) {
        log.debug("REST request to get a allYears");
        List<Integer> years;
        years = dashboardService.getYearsOfUserRatings(companyUser);
        return ResponseEntity.ok().body(years);
    }

    @Operation(summary = "Retrieves ratings based on inserted year")
    @ApiResponses(value = {@ApiResponse (responseCode = "200", description = "Ratings of inserted custom year retrieved with success"),
                           @ApiResponse (responseCode = "401", description = "Authentication Required", content = @Content)})
    @GetMapping("/dashboard/ratingsByYear")
    public ResponseEntity<List<DataSourceDTO>> ratingsByYear(@RequestParam(value = "year", defaultValue = "0", required = false) Integer year, CompanyUserDTO companyUserDTO) {
        log.debug("REST request to get ratingsByYear");
        List<DataSourceDTO> dataSourceDto;
        dataSourceDto = dashboardService.findRatingsByYearAndCompanyUser(year,companyUserDTO);
        return ResponseEntity.ok().body(dataSourceDto);
    }

    @Operation(summary = "Retrieves an CSV template")
    @ApiResponses(value = {@ApiResponse (responseCode = "200", description = "CSV file retrieved with success"),
            @ApiResponse (responseCode = "401", description = "Authentication Required", content = @Content)})
    @GetMapping("/dashboard/getTemplate")
    public ResponseEntity<byte[]> getTemplate() {
        log.debug("REST request to get Template");
        FileDTO fileDTO = dashboardService.getDataSourceTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileDTO.getFileName() + ".csv");
        httpHeaders.set("filename", fileDTO.getFileName() + ".csv");
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ByteArrayResource byteArrayResource = new ByteArrayResource(fileDTO.getContent().getBytes());
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(byteArrayResource.getByteArray());

    }

    @Operation(summary = "Inserts information from an CSV file into the database, with the associated user")
    @ApiResponses(value = {@ApiResponse (responseCode = "200", description = "CSV file uploaded with success"),
                           @ApiResponse (responseCode = "400", description = "Bad Request", content = @Content),
                           @ApiResponse (responseCode = "500", description = "CSV file is missing", content = @Content),
            @ApiResponse (responseCode = "401", description = "Authentication Required", content = @Content)})
    @PostMapping("/dashboard/uploadCsv")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file,
                                                      @RequestHeader("ratingName") String dataSourceName, CompanyUserDTO companyUser) {
        log.debug("REST request to uploadCsv");
        String message = "";
        message = "Uploaded the file successfully: " + file.getOriginalFilename();
        try {
            dashboardService.saveCsv(file, dataSourceName, companyUser);
        } catch (DataIntegrityViolationException e) {
            message = "Could not upload the file duplicate name: " + dataSourceName + "!";
            log.error(message);
            log.error("Error {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        } catch (Exception e) {
            log.error("Error {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage( e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));


    }

    @Operation(summary = "Retrieves current user ranges")
    @ApiResponses(value = {@ApiResponse (responseCode = "200", description = "User Ranges requested with success"),
            @ApiResponse (responseCode = "401", description = "Authentication Required", content = @Content)})
    @GetMapping("/dashboard/getUserRanges")
    public ResponseEntity<List<RangeDTO>> userRanges(CompanyUserDTO companyUser) {
        log.debug("REST request to get User Ranges");
        List<RangeDTO> rangeDTOS;
        rangeDTOS = dashboardService.getUserRangesOrDefault(companyUser);
        return ResponseEntity.ok().body(rangeDTOS);
    }

    @Operation(summary = "Retrieves all countries in the database filter by ISO CODE 2")
    @ApiResponses(value = {@ApiResponse (responseCode = "200", description = "Countries requested with success"),
            @ApiResponse (responseCode = "401", description = "Authentication Required", content = @Content)})
    @GetMapping("/dashboard/getCountryFilterByISO2")
    public ResponseEntity<List<CountryDTO>> getCountrys(CompanyUserDTO companyUserDTO) {
        log.debug("REST request to get CountryFilterByISO2");
        return ResponseEntity.ok().body(dashboardService.getCountryFilterByISO2(companyUserDTO));
    }


    @Operation(summary = "Retrieves all Business Partners of a Company")
    @ApiResponses(value = {@ApiResponse (responseCode = "200", description = "Bpn requested with success"),
            @ApiResponse (responseCode = "401", description = "Authentication Required", content = @Content)})
    @GetMapping("/dashboard/getCompanyBpns")
    public ResponseEntity<List<BusinessPartnerDTO>> getCompanyBpns(CompanyUserDTO companyUserDTO) {
        log.debug("REST request to get CompanyBpns");
        return ResponseEntity.ok().body(dashboardService.getExternalBusinessPartners(companyUserDTO));
    }

    @Operation(summary = "Retrieves all countries in the database OF THE Bpns")
    @ApiResponses(value = {@ApiResponse (responseCode = "200", description = "Countries requested with success"),
            @ApiResponse (responseCode = "401", description = "Authentication Required", content = @Content)})
    @GetMapping("/dashboard/getBpnCountrys")
    public ResponseEntity<List<CountryDTO>> getBpnCountrys(CompanyUserDTO companyUserDTO) {
        log.debug("REST request to get BpnCountrys");
        List<CountryDTO> countryDTOS;
        countryDTOS = dashboardService.getCountryByAssociatedBPtoUser(companyUserDTO);

        return ResponseEntity.ok().body(countryDTOS);
    }
    @Operation(summary = "Saves the current user ranges")
    @ApiResponses(value = {@ApiResponse (responseCode = "200", description = "Ranges saved with success"),
                           @ApiResponse (responseCode = "400", description = "Bad Request", content = @Content),
                           @ApiResponse (responseCode = "401", description = "Authentication Required", content = @Content)})
    @PostMapping("/dashboard/saveUserRanges")
    public ResponseEntity<ResponseMessage> saveRanges(@Valid @RequestBody List<RangeDTO> rangeDTOS, CompanyUserDTO companyUserDTO) {
        String message = "";
        log.debug("REST request to saveUserRanges");
        try {
            dashboardService.saveRanges(rangeDTOS, companyUserDTO);
            message = "Range successfully saved!";
            log.info(message);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }catch (Exception e) {
            message = "Could not save the ranges values!";
            log.error(message);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ResponseMessage(message));
        }
    }

}
