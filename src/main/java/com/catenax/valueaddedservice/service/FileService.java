package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.domain.File;
import com.catenax.valueaddedservice.dto.FileDTO;
import com.catenax.valueaddedservice.repository.FileRepository;
import com.catenax.valueaddedservice.service.mapper.FileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service Implementation for managing {@link File}.
 */
@Service
public class FileService {

    private final Logger log = LoggerFactory.getLogger(FileService.class);

    private final FileRepository fileRepository;

    private final FileMapper fileMapper;

    public FileService(FileRepository fileRepository, FileMapper fileMapper) {
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
    }

    /**
     * Save a file.
     *
     * @param fileDTO the entity to save.
     * @return the persisted entity.
     */
    public FileDTO save(FileDTO fileDTO) {
        log.debug("Request to save File : {}", fileDTO);
        File file = fileMapper.toEntity(fileDTO);
        file = fileRepository.save(file);
        return fileMapper.toDto(file);
    }


    public Optional<FileDTO> findUpdatedDataSourceTemplate() {
        log.debug("Request to get File by UpdatedDataSourceTemplate");
        return fileRepository.findTopByOrderByVersionDesc().map(fileMapper::toDto);
    }

}
