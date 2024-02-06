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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MethodUtilsTest {

    @Test
    @DisplayName("Should return the number with decimals when the number is not an integer")
    void removeDecimalsZeroWhenNumberIsNotIntegerThenReturnNumberWithDecimals() {
        Float number = 1.23f;
        String result = MethodUtils.removeDecimalsZero(number);
        assertEquals("1.23", result);
    }

    @Test
    @DisplayName("Should return the number without decimals when the number is an integer")
    void removeDecimalsZeroWhenNumberIsIntegerThenReturnNumberWithoutDecimals() {
        Float number = 1.0f;

        String result = MethodUtils.removeDecimalsZero(number);

        assertEquals("1", result);
    }
}