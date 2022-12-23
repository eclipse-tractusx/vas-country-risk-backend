package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.domain.File;
import com.catenax.valueaddedservice.dto.FileDTO;
import com.catenax.valueaddedservice.repository.FileRepository;
import com.catenax.valueaddedservice.service.mapper.FileMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private FileMapper fileMapper;

    @InjectMocks
    private FileService fileService;

    @Test
    @DisplayName("Should save the file")
    void saveShouldSaveFile() {
        FileDTO fileDTO = new FileDTO();
        File file = new File();
        when(fileMapper.toEntity(fileDTO)).thenReturn(file);
        when(fileRepository.save(file)).thenReturn(file);
        when(fileMapper.toDto(file)).thenReturn(fileDTO);

        FileDTO result = fileService.save(fileDTO);

        assertEquals(result, fileDTO);
    }

    @Test
    @DisplayName("Should return the saved file")
    void saveShouldReturnSavedFile() {
        FileDTO fileDTO = new FileDTO();
        File file = new File();
        when(fileMapper.toEntity(fileDTO)).thenReturn(file);
        when(fileRepository.save(file)).thenReturn(file);
        when(fileMapper.toDto(file)).thenReturn(fileDTO);

        FileDTO savedFile = fileService.save(fileDTO);

        assertEquals(savedFile, fileDTO);
    }
}
