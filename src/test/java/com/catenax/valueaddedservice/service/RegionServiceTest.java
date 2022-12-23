package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.domain.Region;
import com.catenax.valueaddedservice.domain.enumeration.Type;
import com.catenax.valueaddedservice.dto.RegionDTO;
import com.catenax.valueaddedservice.repository.RegionRepository;
import com.catenax.valueaddedservice.service.mapper.RegionMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegionServiceTest {

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private RegionMapper regionMapper;

    @InjectMocks
    private RegionService regionService;

    @Test
    @DisplayName("Should return all Regions")
    void findAllShouldReturnAllRegions() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Region> regionList = new ArrayList<>();
        regionList.add(new Region());
        regionList.add(new Region());
        Page<Region> RegionPage = new PageImpl<>(regionList);

        when(regionRepository.findAll(any(Pageable.class))).thenReturn(RegionPage);

        Page<RegionDTO> result = regionService.findAll(pageable);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    @DisplayName("Should return empty when the Region does not exist")
    void partialUpdateWhenRegionDoesNotExistThenReturnEmpty() {
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setId(1L);
        when(regionRepository.findById(any())).thenReturn(Optional.empty());

        Optional<RegionDTO> result = regionService.partialUpdate(regionDTO);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return the updated Region when the Region exists")
    void partialUpdateWhenRegionExistsThenReturnUpdatedRegion() {
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setId(1L);
        regionDTO.setName("Name");
        regionDTO.setType(Type.Global);
        regionDTO.setDescription("Max value");


        Region region = new Region();
        region.setId(1L);
        region.setName("Name");
        region.setType(Type.Global);
        region.setDescription("Max value");

        when(regionRepository.findById(anyLong())).thenReturn(Optional.of(region));
        when(regionRepository.save(any())).thenReturn(region);
        when(regionMapper.toDto(region)).thenReturn(regionDTO);

        Optional<RegionDTO> result = regionService.partialUpdate(regionDTO);

        assertTrue(result.isPresent());
        assertEquals(result.get().getId(), regionDTO.getId());
    }

    @Test
    @DisplayName("Should return the Region when the id is valid")
    void findOneWhenIdIsValid() {
        Long id = 1L;
        Region region = new Region();
        region.setId(id);
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setId(id);

        when(regionRepository.findById(id)).thenReturn(Optional.of(region));
        when(regionMapper.toDto(region)).thenReturn(regionDTO);

        Optional<RegionDTO> result = regionService.findOne(id);

        assertTrue(result.isPresent());
        assertEquals(result.get().getId(), id);
    }

    @Test
    @DisplayName("Should delete the Region when the id is valid")
    void deleteWhenIdIsValid() {
        Long id = 1L;
        regionService.delete(id);
        verify(regionRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should save the region")
    void saveShouldSaveFile() {
        RegionDTO regionDTO = new RegionDTO();
        Region region = new Region();
        when(regionMapper.toEntity(regionDTO)).thenReturn(region);
        when(regionRepository.save(region)).thenReturn(region);
        when(regionMapper.toDto(region)).thenReturn(regionDTO);

        RegionDTO result = regionService.save(regionDTO);

        assertEquals(result, regionDTO);
    }

}