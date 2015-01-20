package com.project.bluepandora.donatelife.data;

/*
 * Copyright (C) 2014 The Blue Pandora Project Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class FeedItem implements Item {
    private int id, bloodAmount;
    private String name;
    private String timeStamp;
    private String emergency;
    private String bloodGroup;
    private String hospital;
    private String area;
    private String contact;

    public FeedItem() {
    }

    public FeedItem(int id, int bloodAmount, String name, String timeStamp,
                    String emergency, String bloodGroup, String hospital, String area,
                    String contact) {
        this.id = id;
        this.bloodAmount = bloodAmount;
        this.name = name;
        this.timeStamp = timeStamp;
        this.emergency = emergency;
        this.bloodGroup = bloodGroup;
        this.hospital = hospital;
        this.area = area;
        this.contact = contact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getEmergency() {
        return emergency;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getBloodAmount() {
        return bloodAmount;
    }

    public void setBloodAmount(int bloodAmount) {
        this.bloodAmount = bloodAmount;
    }

}
