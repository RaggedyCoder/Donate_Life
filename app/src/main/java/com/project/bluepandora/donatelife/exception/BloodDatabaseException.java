package com.project.bluepandora.donatelife.exception;

public class BloodDatabaseException extends SQliteDataBaseException {
    /**
     *
     */
    private static final long serialVersionUID = -2968403787125932603L;
    public static final String BLOODDATABASEEXCEPTION_TAG = "BloodDatabaseException";

    @Override
    public String getMessage() {
        return super.getMessage() + "Problem Occured During Blood Table";
    }
}
