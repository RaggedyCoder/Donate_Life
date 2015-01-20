package com.project.bluepandora.donatelife.data;

/**
 * Created by tuman on 11/1/2015.
 */
public class SMItem implements Item {

    private int buttonImage;

    public SMItem() {
        buttonImage = 0;
    }

    public SMItem(int buttonImage) {
        this.buttonImage = buttonImage;
    }

    public int getButtonImage() {
        return buttonImage;
    }

    public void setButtonImage(int buttonImage) {
        this.buttonImage = buttonImage;
    }
}
