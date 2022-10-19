package com.catenax.valueaddedservice.service.logic;

import com.catenax.valueaddedservice.dto.FileDTO;
import com.catenax.valueaddedservice.service.DataSourceService;
import com.catenax.valueaddedservice.service.DataSourceValueService;
import com.catenax.valueaddedservice.service.FileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UploadAndDownloadLogicService")
class UploadAndDownloadLogicServiceTest {

    @Mock
    DataSourceService dataSourceService;

    @Mock
    DataSourceValueService dataSourceValueService;

    @Mock
    FileService fileService;

    @InjectMocks
    UploadAndDownloadLogicService uploadAndDownloadLogicService;




    @Test
    @DisplayName("Should return the template when the template exists")
    void getDataSourceTemplateWhenTemplateExistsThenReturnTheTemplate() {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(1L);
        fileDTO.setFileName("TestFile");
        fileDTO.setContent("TestContent");
        fileDTO.setContentContentType("TestContentType");
        fileDTO.setVersion(1);
        when(fileService.findUpdatedDataSourceTemplate()).thenReturn(Optional.of(fileDTO));

        FileDTO result = uploadAndDownloadLogicService.getDataSourceTemplate();

        assertEquals(fileDTO, result);
    }
}