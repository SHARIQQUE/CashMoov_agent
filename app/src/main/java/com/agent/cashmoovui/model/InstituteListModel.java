package com.agent.cashmoovui.model;

public class InstituteListModel {
    public String code;
    public String walletOwnerCategoryCode;
    public String ownerName;
    public String mobileNumber;
    public String email;
    public String dateOfBirth;
    public String registerCountryCode;
    public String registerCountryName;
    public String walletOwnerCode;

    public InstituteListModel(String code, String walletOwnerCategoryCode, String ownerName, String mobileNumber, String email, String dateOfBirth, String registerCountryCode, String registerCountryName, String walletOwnerCode) {
        this.code = code;
        this.walletOwnerCategoryCode = walletOwnerCategoryCode;
        this.ownerName = ownerName;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.registerCountryCode = registerCountryCode;
        this.registerCountryName = registerCountryName;
        this.walletOwnerCode = walletOwnerCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getWalletOwnerCategoryCode() {
        return walletOwnerCategoryCode;
    }

    public void setWalletOwnerCategoryCode(String walletOwnerCategoryCode) {
        this.walletOwnerCategoryCode = walletOwnerCategoryCode;
    }

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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRegisterCountryCode() {
        return registerCountryCode;
    }

    public void setRegisterCountryCode(String registerCountryCode) {
        this.registerCountryCode = registerCountryCode;
    }

    public String getRegisterCountryName() {
        return registerCountryName;
    }

    public void setRegisterCountryName(String registerCountryName) {
        this.registerCountryName = registerCountryName;
    }

    public String getWalletOwnerCode() {
        return walletOwnerCode;
    }

    public void setWalletOwnerCode(String walletOwnerCode) {
        this.walletOwnerCode = walletOwnerCode;
    }
}
