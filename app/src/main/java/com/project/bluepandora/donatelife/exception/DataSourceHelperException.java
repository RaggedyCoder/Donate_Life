package com.project.bluepandora.donatelife.exception;


public class DataSourceHelperException extends Exception {
    public static final String DATA_SOURCE_HELPER_EXCEPTION_TAG = "BloodDatabaseException";
    public String message = "Invalid TAG";

    @Override
    public String getMessage() {
        return DATA_SOURCE_HELPER_EXCEPTION_TAG + " " + message;
    }

}
