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
package org.eclipse.tractusx.valueaddedservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;



public class MethodUtils {

    private static final Logger log = ESAPI.getLogger(MethodUtils.class);

    @Autowired
    ObjectMapper objectMapper;

    public static Float formatFloatTwoDecimals(Float number) {
        DecimalFormat df = new DecimalFormat("#.##");
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
        sym.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(sym);
        return Float.valueOf(df.format(number));
    }

    public static String removeDecimalsZero(Float number) {
        if (number == number.longValue()) {
            return new DecimalFormat("#").format(number);
        }
        return String.valueOf(number);
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

}
