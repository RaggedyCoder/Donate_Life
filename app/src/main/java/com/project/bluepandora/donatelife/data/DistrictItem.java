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
public class DistrictItem implements Item {

    private int distId;
    private String distName;
    private String banglaDistName;

    public DistrictItem() {

    }

    public DistrictItem(int distId, String distName, String banglaDistName) {
        this.distId = distId;
        this.distName = distName;
        this.banglaDistName = banglaDistName;
    }

    public int getDistId() {
        return distId;
    }

    public void setDistId(int distId) {
        this.distId = distId;
    }

    public String getDistName() {
        return distName;
    }

    public void setDistName(String distName) {
        this.distName = distName;
    }

    public String toString() {
        return distName;
    }

    public String getBanglaDistName() {
        return banglaDistName;
    }

    public void setBanglaDistName(String banglaDistName) {
        this.banglaDistName = banglaDistName;
    }
}
