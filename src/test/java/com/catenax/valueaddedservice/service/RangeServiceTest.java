package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.domain.Range;
import com.catenax.valueaddedservice.domain.enumeration.RangeType;
import com.catenax.valueaddedservice.dto.RangeDTO;
import com.catenax.valueaddedservice.repository.RangeRepository;
import com.catenax.valueaddedservice.service.mapper.RangeMapper;
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
class RangeServiceTest {

    @Mock
    private RangeRepository rangeRepository;

    @Mock
    private RangeMapper rangeMapper;

    @InjectMocks
    private RangeService rangeService;

    @Test
    @DisplayName("Should return all ranges")
    void findAllShouldReturnAllRanges() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Range> rangeList = new ArrayList<>();
        rangeList.add(new Range());
        rangeList.add(new Range());
        Page<Range> rangePage = new PageImpl<>(rangeList);

        when(rangeRepository.findAll(any(Pageable.class))).thenReturn(rangePage);

        Page<RangeDTO> result = rangeService.findAll(pageable);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    @DisplayName("Should return empty when the range does not exist")
    void partialUpdateWhenRangeDoesNotExistThenReturnEmpty() {
        RangeDTO rangeDTO = new RangeDTO();
        rangeDTO.setId(1L);
        when(rangeRepository.findById(any())).thenReturn(Optional.empty());

        Optional<RangeDTO> result = rangeService.partialUpdate(rangeDTO);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return the updated range when the range exists")
    void partialUpdateWhenRangeExistsThenReturnUpdatedRange() {
        RangeDTO rangeDTO = new RangeDTO();
        rangeDTO.setId(1L);
        rangeDTO.setValue(80);
        rangeDTO.setDescription("Max value");
        rangeDTO.setRange(RangeType.Max);

        Range range = new Range();
        range.setId(1L);
        range.setValue(80);
        range.setDescription("Max value");
        range.setRange(RangeType.Max);

        when(rangeRepository.findById(anyLong())).thenReturn(Optional.of(range));
        when(rangeRepository.save(any())).thenReturn(range);
        when(rangeMapper.toDto(range)).thenReturn(rangeDTO);

        Optional<RangeDTO> result = rangeService.partialUpdate(rangeDTO);

        assertTrue(result.isPresent());
        assertEquals(result.get().getId(), rangeDTO.getId());
    }

    @Test
    @DisplayName("Should return the Range when the id is valid")
    void findOneWhenIdIsValid() {
        Long id = 1L;
        Range range = new Range();
        range.setId(id);
        RangeDTO rangeDTO = new RangeDTO();
        rangeDTO.setId(id);

        when(rangeRepository.findById(id)).thenReturn(Optional.of(range));
        when(rangeMapper.toDto(range)).thenReturn(rangeDTO);

        Optional<RangeDTO> result = rangeService.findOne(id);

        assertTrue(result.isPresent());
        assertEquals(result.get().getId(), id);
    }

    @Test
    @DisplayName("Should delete the Range when the id is valid")
    void deleteWhenIdIsValid() {
        Long id = 1L;
        rangeService.delete(id);
        verify(rangeRepository, times(1)).deleteById(id);
    }
}