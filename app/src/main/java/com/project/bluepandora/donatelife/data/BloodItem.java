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
public class BloodItem implements Item {

    private int bloodId;
    private String bloodName;
    private String banglaBloodName;

    public BloodItem() {

    }

    public BloodItem(String bloodName, String banglaBloodName, int bloodId) {
        this.bloodId = bloodId;
        this.bloodName = bloodName;
        this.banglaBloodName = banglaBloodName;
    }

    public int getBloodId() {
        return bloodId;
    }

    public void setBloodId(int bloodId) {
        this.bloodId = bloodId;
    }

    public String getBloodName() {
        return bloodName;
    }

    public void setBloodName(String bloodName) {
        this.bloodName = bloodName;
    }

    public String getBanglaBloodName() {
        return banglaBloodName;
    }

    public void setBanglaBloodName(String banglaBloodName) {
        this.banglaBloodName = banglaBloodName;
    }
}
