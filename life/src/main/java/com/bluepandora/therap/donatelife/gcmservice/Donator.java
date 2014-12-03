/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.gcmservice;

/**
 *
 * @author Biswajit Debnath
 */
public class Donator {

    private String donatorMobileNumber;
    private String donatorGcmId;

    Donator(String donatorMobileNumber, String donatorGcmId) {
        setDonator(donatorMobileNumber, donatorGcmId);
    }

    public void setDonator(String donatorMobileNumber, String donatorGcmId) {
        setDonatorMobileNumber(donatorMobileNumber);
        setDonatorGcmId(donatorGcmId);
    }

    public String getDonatorMobileNumber() {
        return donatorMobileNumber;
    }

    public void setDonatorMobileNumber(String donatorMobileNumber) {
        this.donatorMobileNumber = donatorMobileNumber;
    }

    public String getDonatorGcmId() {
        return donatorGcmId;
    }

    public void setDonatorGcmId(String donatorGcmId) {
        this.donatorGcmId = donatorGcmId;
    }

    @Override
    public String toString() {
        return "Donator{" + "donatorMobileNumber=" + donatorMobileNumber + ", donatorGcmId=" + donatorGcmId + '}';
    }
    

}
