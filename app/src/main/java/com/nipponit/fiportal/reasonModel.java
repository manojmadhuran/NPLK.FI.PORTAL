package com.nipponit.fiportal;

public class reasonModel {
    private String reasonID;
    private String reasonText;

    public reasonModel(String reasonID_,String reasonText_){
        this.setReasonID(reasonID_);
        this.setReasonText(reasonText_);
    }

    public String getReasonID() {
        return reasonID;
    }

    public void setReasonID(String reasonID) {
        this.reasonID = reasonID;
    }

    public String getReasonText() {
        return reasonText;
    }

    public void setReasonText(String reasonText) {
        this.reasonText = reasonText;
    }
}
