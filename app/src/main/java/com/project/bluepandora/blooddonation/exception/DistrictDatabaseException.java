package com.project.bluepandora.blooddonation.exception;

public class DistrictDatabaseException extends SQliteDataBaseException {
	public static final String DISTRICTDATABASE_EXCEPTION_TAG = "DistrictDatabaseException";
	private static final long serialVersionUID = 8021847091188779832L;

	@Override
	public String getMessage() {
		return super.getMessage()+"-Problem Occured During District Table";
	}
}
