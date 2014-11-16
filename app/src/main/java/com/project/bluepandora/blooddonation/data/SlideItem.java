package com.project.bluepandora.blooddonation.data;

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
public class SlideItem implements Item {
    private String slideItem;
    private int icons;
    private int background;

    public SlideItem() {
        // TODO Auto-generated constructor stub
    }

    public SlideItem(String slideItem) {
        // TODO Auto-generated constructor stub
        this.slideItem = slideItem;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    public String getSlideItem() {
        return slideItem;
    }

    public void setSlideItem(String slideItem) {
        this.slideItem = slideItem;
    }

    public int getIcons() {
        return icons;
    }

    public void setIcons(int icons) {
        this.icons = icons;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

}
