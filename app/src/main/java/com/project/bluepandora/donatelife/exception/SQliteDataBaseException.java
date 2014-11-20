package com.project.bluepandora.donatelife.exception;

import android.database.SQLException;

public class SQliteDataBaseException extends SQLException {

    /**
     *
     */
    public static final String DatabaseException_TAG = "DatabaseException";
    private static final long serialVersionUID = 4266329753277016743L;

    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        return "Problem Occured During BloodDonate Database";
    }
}
