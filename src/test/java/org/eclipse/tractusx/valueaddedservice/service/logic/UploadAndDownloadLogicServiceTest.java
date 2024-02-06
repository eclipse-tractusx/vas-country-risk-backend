/********************************************************************************
* Copyright (c) 2022,2024 BMW Group AG
* Copyright (c) 2022,2024 Contributors to the Eclipse Foundation
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
package org.eclipse.tractusx.valueaddedservice.service.logic;

import org.eclipse.tractusx.valueaddedservice.dto.FileDTO;
import org.eclipse.tractusx.valueaddedservice.service.FileService;
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