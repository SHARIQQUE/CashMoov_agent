package com.agent.cashmoovui.model;

public class UserDetailAgent {

    private String ownerName="";
    private String mobileNumber="";
    private String email="";
    private String issuingCountryName="";

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIssuingCountryName() {
        return issuingCountryName;
    }

    public void setIssuingCountryName(String issuingCountryName) {
        this.issuingCountryName = issuingCountryName;
    }
}
