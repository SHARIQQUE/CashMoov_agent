package com.agent.cashmoovui.model;

public class UserDetailBranch {

    private String ownerName="";
    private String mobileNumber="";
    private String email="";
    private String issuingCountryName="";
    private String walletOwnerCode="";
    private String registerCountryCode="";

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

    public String getWalletOwnerCode() {
        return walletOwnerCode;
    }

    public void setWalletOwnerCode(String walletOwnerCode) {
        this.walletOwnerCode = walletOwnerCode;
    }

    public String getRegisterCountryCode() {
        return registerCountryCode;
    }

    public void setRegisterCountryCode(String registerCountryCode) {
        this.registerCountryCode = registerCountryCode;
    }
}
