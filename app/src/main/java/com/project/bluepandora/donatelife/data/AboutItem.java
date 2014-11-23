package com.project.bluepandora.donatelife.data;

/**
 * Created by tuman on 23/11/2014.
 */
public class AboutItem implements Item {

    private String header;
    private String body;

    public AboutItem() {

    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
