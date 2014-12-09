package com.project.bluepandora.donatelife.helpers;

import android.app.Activity;

import com.project.bluepandora.donatelife.datasource.BloodDataSource;
import com.project.bluepandora.donatelife.datasource.DRDataSource;
import com.project.bluepandora.donatelife.datasource.DistrictDataSource;
import com.project.bluepandora.donatelife.datasource.HospitalDataSource;
import com.project.bluepandora.donatelife.datasource.UserDataSource;
import com.project.bluepandora.donatelife.exception.BloodDatabaseException;
import com.project.bluepandora.donatelife.exception.DRDatabseException;
import com.project.bluepandora.donatelife.exception.DataSourceHelperException;
import com.project.bluepandora.donatelife.exception.DistrictDatabaseException;

public class DataSourceHelper {

    public static final int ALL = 0;
    public static final int USER_PROFILE = 1;
    public static final int DISTRICT_LIST = 2;
    public static final int HOSPITAL_LIST = 3;
    public static final int BLOOD_GROUP = 4;
    public static final int DONATION_RECORD = 5;
    private Activity activity;
    private UserDataSource userDataSource;
    private DistrictDataSource districtDataSource;
    private HospitalDataSource hospitalDataSource;
    private BloodDataSource bloodDataSource;
    private DRDataSource drDataSource;

    public DataSourceHelper(Activity activity, final int TAG) throws DataSourceHelperException {
        this.activity = activity;
        switch (TAG) {
            case ALL:
                userDataSource = new UserDataSource(activity);
                districtDataSource = new DistrictDataSource(activity);
                hospitalDataSource = new HospitalDataSource(activity);
                bloodDataSource = new BloodDataSource(activity);
                drDataSource = new DRDataSource(activity);
                break;
            case USER_PROFILE:
                userDataSource = new UserDataSource(activity);
                break;
            case DISTRICT_LIST:
                districtDataSource = new DistrictDataSource(activity);
                break;
            case HOSPITAL_LIST:
                hospitalDataSource = new HospitalDataSource(activity);
                break;
            case BLOOD_GROUP:
                bloodDataSource = new BloodDataSource(activity);
                break;
            case DONATION_RECORD:
                drDataSource = new DRDataSource(activity);
                break;
            default:
                throw new DataSourceHelperException();
        }
    }

    public Activity getActivity() {
        return activity;
    }

    public DistrictDataSource getDistrictDataSource() throws DistrictDatabaseException {
        if (districtDataSource != null)
            return districtDataSource;
        else
            throw new DistrictDatabaseException("District List Database is null");
    }

    public BloodDataSource getBloodDataSource() throws BloodDatabaseException {
        if (bloodDataSource != null)
            return bloodDataSource;
        else
            throw new BloodDatabaseException("Blood Group Database is null");
    }

    public DRDataSource getDrDataSource() throws DRDatabseException {
        if (drDataSource != null)
            return drDataSource;
        else
            throw new DRDatabseException("Donation Record Database is null");
    }
}
