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
package org.eclipse.tractusx.valueaddedservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

public class DataBaseJsonConverterTest {

    @InjectMocks
    private DataBaseJsonConverter dataBaseJsonConverter;

    @Mock
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    /**
     * Should return null when a null JSON string is provided
     */
    @Test
    public void convertToEntityAttributeWithNullJsonString() {
        String customerInfoJSON = null;

        Map<String, Object> result = dataBaseJsonConverter.convertToEntityAttribute(customerInfoJSON);

        assertNull(result);
    }


    @Test
    public void testConvertToDatabaseColumn() throws JsonProcessingException {
        Map<String, Object> customerInfo = new HashMap<>();
        customerInfo.put("key", "value");

        when(objectMapper.writeValueAsString(customerInfo)).thenReturn("{\"key\":\"value\"}");

        String result = dataBaseJsonConverter.convertToDatabaseColumn(customerInfo);

        assertEquals("{\"key\":\"value\"}", result);
    }


    @Test
    public void testConvertToDatabaseColumnException() throws JsonProcessingException {
        Map<String, Object> customerInfo = new HashMap<>();
        customerInfo.put("key", "value");

        when(objectMapper.writeValueAsString(customerInfo)).thenThrow(new JsonProcessingException("Exception") {
        });

        String result = dataBaseJsonConverter.convertToDatabaseColumn(customerInfo);

        assertNull(result);
    }
}
