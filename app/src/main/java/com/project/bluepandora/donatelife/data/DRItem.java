package com.project.bluepandora.donatelife.data;

/**
 * Created by tuman on 25/11/2014.
 */
public class DRItem implements Item {

    private String donationTime;
    private String donationDetails;


    public DRItem() {

    }

    public DRItem(String donationTime, String donationDetails) {
        this.donationTime = donationTime;
        this.donationDetails = donationDetails;
    }

    public String getDonationTime() {
        return donationTime;
    }

    public void setDonationTime(String donationTime) {
        this.donationTime = donationTime;
    }

    public String getDonationDetails() {
        return donationDetails;
    }

    public void setDonationDetails(String donationDetails) {
        this.donationDetails = donationDetails;
    }
}
