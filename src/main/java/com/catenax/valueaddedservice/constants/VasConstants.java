package com.catenax.valueaddedservice.constants;

/**
 * Defines constants for the MDC key names
 */
public final class VasConstants {

    private VasConstants() {
    }

    public static final String HEADERS_BEARER_TOKEN = "authorization";
    public static final String REMOVE_BEARER_TAG = "Bearer ";

    public static final String ERROR_LOG = "Error {}";
    public static final String UPLOAD_SUCCESS_MESSAGE= "Uploaded the file successfully: ";
    public static final String UPLOAD_ERROR_MESSAGE= "Could not upload the file duplicate name: ";

    public static final Integer MIN_DEFAULT_USER_RANGE = 25;


}
