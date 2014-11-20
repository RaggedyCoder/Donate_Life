package com.project.bluepandora.donatelife.exception;

public class HospitalDatabaseException extends SQliteDataBaseException {
    /**
     *
     */
    private static final long serialVersionUID = -2968403787125932603L;
    public static final String HOSPITALDATABASE_EXCEPTION_TAG = "HospitalDatabaseException";

    @Override
    public String getMessage() {
        return super.getMessage() + "Problem Occured During Hospital Table";
    }
}
