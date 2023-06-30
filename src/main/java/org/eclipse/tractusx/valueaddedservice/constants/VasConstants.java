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
package org.eclipse.tractusx.valueaddedservice.constants;

/**
 * Defines constants for the MDC key names
 */
public final class VasConstants {

    private VasConstants() {
    }

    public static final String HEADERS_BEARER_TOKEN = "authorization";
    public static final String REMOVE_BEARER_TAG = "Bearer ";

    public static final String ERROR_LOG = "Error ";
    public static final String UPLOAD_SUCCESS_MESSAGE= "Uploaded the file successfully: ";
    public static final String UPLOAD_ERROR_MESSAGE= "Rating name already exists. Please chose a different name ";
    public static final String UPLOAD_ERROR_MESSAGE_ON_SCORES = "Invalid scoring for ";

    public static final String VALIDATE_COMPANY_USER= "Request parameter companyUser not valid";
    public static final Integer MIN_DEFAULT_USER_RANGE = 25;
    public static final Integer BETWEEN_DEFAULT_USER_RANGE = 50;
    public static final Integer MAX_DEFAULT_USER_RANGE = 100;

    public static final String HEADER_CSV_NAME = "testeRatingINTtest";
    public static final String HEADER_CSV_NAME_ERROR = "testeRatingError";
    public static final String HEADER_CSV_YEAR = "2022";
    public static final String HEADER_FAKE_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyZXNvdXJjZV9hY2Nlc3MiOnsiZmFrZUNsaWVudCI6eyJyb2xlcyI6WyJmYWtlUm9sZSJdfX0sIm5hbWUiOiJmYWtlVXNlciIsIm9yZ2FuaXNhdGlvbiI6ImZha2VDb21wYW55IiwiZW1haWwiOiJmYWtlZW1haWwifQ.FxFjhpnRMZbHF-f-zOs-cXDIx_rQasUgU96X0KxqVZA";

    public static final String CSV_TYPE = "form-data";
    public static final String CSV_NAME = "file";
    public static final String CSV_FILENAME = "testFile.csv";
    public static final String CSV_ROLE_TYPE_GLOBAL = "Global";

    public static final String CSV_ROLE_TYPE_CUSTOM = "Custom";

    public static final String CSV_ROLE_TYPE_COMPANY = "Company";

    public static final String CSV_ROLE_COMPANY_ADMIN = "Company Admin";
    public static final String CSV_ROLE_READ_SUPPLIER = "read_suppliers";
    public static final String CSV_ROLE_READ_CUSTOMER = "read_customers";


    public static final String REQUEST_COMPANY_NAME = "companyName";

    public static final String REQUEST_USER_EMAIL = "email";

    public static final String REQUEST_USER_NAME = "name";

    public static final String CSV_FILEPATH = "src/test/resources/config/liquibase/test-data/file_test_upload.csv";
    public static final String CSV_FILEPATH_ERROR = "src/test/resources/config/liquibase/test-data/file_test_upload_with_error.csv";

}
