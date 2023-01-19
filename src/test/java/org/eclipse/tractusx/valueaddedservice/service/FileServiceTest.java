/********************************************************************************
* Copyright (c) 2022,2023 BMW Group AG 
* Copyright (c) 2022,2023 Contributors to the Eclipse Foundation
*
* See the NOTICE file(s) distributed with this work for additional
* information regarding copyright ownership.
*
* This program and the accompanying materials are made available under the
* terms of the Apache License, Version 2.0 which is available at
* https://www.apache.org/licenses/LICENSE-2.0.
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
*
* SPDX-License-Identifier: Apache-2.0
********************************************************************************/
package org.eclipse.tractusx.valueaddedservice.service;

import org.eclipse.tractusx.valueaddedservice.domain.File;
import org.eclipse.tractusx.valueaddedservice.dto.FileDTO;
import org.eclipse.tractusx.valueaddedservice.repository.FileRepository;
import org.eclipse.tractusx.valueaddedservice.service.mapper.FileMapper;
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
