package com.catenax.valueaddedservice.constants;

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
    public static final String UPLOAD_ERROR_MESSAGE= "Could not upload the file duplicate name ";

    public static final Integer MIN_DEFAULT_USER_RANGE = 25;
    public static final Integer BETWEEN_DEFAULT_USER_RANGE = 50;
    public static final Integer MAX_DEFAULT_USER_RANGE = 100;

    public static final String HEADER_CSV_NAME = "testeRatingINTtest";
    public static final String HEADER_CSV_NAME_ERROR = "testeRatingError";
    public static final String HEADER_TOKEN = "adb4a97ffb9a12fb29094267852f06b3d3d1f93076425d7c61cf1bac3b557648cb4119aff5a57b810e0ae32";

    public static final String CSV_TYPE = "form-data";
    public static final String CSV_NAME = "file";
    public static final String CSV_FILENAME = "testeFile.csv";

    public static final String CSV_FILEPATH = "src/test/resources/config/liquibase/test-data/file_test_upload.csv";

}
