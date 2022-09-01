package com.agent.cashmoovui.model;

public class RegistrationModel {
    public int id;
    public String code;
    public String walletOwnerParentCode;
    public String walletOwnerCategoryCode;
    public String ownerName;
    public String mobileNumber;
    public String businessTypeCode;
    public String businessTypeName;
    public String idProofNumber;
    public String email;
    public String status;
    public String state;
    public String stage;
    public String idProofTypeCode;
    public String idProofTypeName;
    public String notificationLanguage;
    public String notificationTypeCode;
    public String notificationName;
    public String gender;
    public String dateOfBirth;
    public String lastName;
    public String issuingCountryCode;
    public String issuingCountryName;
    public String registerCountryCode;
    public String registerCountryName;
    public String createdBy;
    public boolean walletExists;
    public String profileTypeCode;
    public String profileTypeName;
    public String currencyCode;
    public String walletOwnerCatName;
    public String requestedSource;
    public String regesterCountryDialCode;
    public String issuingCountryDialCode;
    public String walletOwnerCode;
    public boolean hasChild;
    public String currencyName;
    public boolean loginWithOtpRequired;
    public String timeZone;

    public RegistrationModel(int id, String code, String walletOwnerParentCode, String walletOwnerCategoryCode, String ownerName, String mobileNumber, String businessTypeCode,String businessTypeName, String idProofNumber, String email, String status, String state, String stage, String idProofTypeCode, String idProofTypeName, String notificationLanguage, String notificationTypeCode, String notificationName, String gender, String dateOfBirth, String lastName, String issuingCountryCode, String issuingCountryName, String registerCountryCode, String registerCountryName, String createdBy, boolean walletExists, String profileTypeCode, String profileTypeName, String currencyCode, String walletOwnerCatName, String requestedSource, String regesterCountryDialCode, String issuingCountryDialCode, String walletOwnerCode, boolean hasChild, String currencyName, boolean loginWithOtpRequired, String timeZone) {
        this.id = id;
        this.code = code;
        this.walletOwnerParentCode = walletOwnerParentCode;
        this.walletOwnerCategoryCode = walletOwnerCategoryCode;
        this.ownerName = ownerName;
        this.mobileNumber = mobileNumber;
        this.businessTypeCode = businessTypeCode;
        this.businessTypeName = businessTypeName;

        this.idProofNumber = idProofNumber;
        this.email = email;
        this.status = status;
        this.state = state;
        this.stage = stage;
        this.idProofTypeCode = idProofTypeCode;
        this.idProofTypeName = idProofTypeName;
        this.notificationLanguage = notificationLanguage;
        this.notificationTypeCode = notificationTypeCode;
        this.notificationName = notificationName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.lastName = lastName;
        this.issuingCountryCode = issuingCountryCode;
        this.issuingCountryName = issuingCountryName;
        this.registerCountryCode = registerCountryCode;
        this.registerCountryName = registerCountryName;
        this.createdBy = createdBy;
        this.walletExists = walletExists;
        this.profileTypeCode = profileTypeCode;
        this.profileTypeName = profileTypeName;
        this.currencyCode = currencyCode;
        this.walletOwnerCatName = walletOwnerCatName;
        this.requestedSource = requestedSource;
        this.regesterCountryDialCode = regesterCountryDialCode;
        this.issuingCountryDialCode = issuingCountryDialCode;
        this.walletOwnerCode = walletOwnerCode;
        this.hasChild = hasChild;
        this.currencyName = currencyName;
        this.loginWithOtpRequired = loginWithOtpRequired;
        this.timeZone = timeZone;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getWalletOwnerParentCode() {
        return walletOwnerParentCode;
    }

    public void setWalletOwnerParentCode(String walletOwnerParentCode) {
        this.walletOwnerParentCode = walletOwnerParentCode;
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

    public String getBusinessTypeCode() {
        return businessTypeCode;
    }

    public void setBusinessTypeCode(String businessTypeCode) {
        this.businessTypeCode = businessTypeCode;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }
    public String getIdProofNumber() {
        return idProofNumber;
    }

    public void setIdProofNumber(String idProofNumber) {
        this.idProofNumber = idProofNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getIdProofTypeCode() {
        return idProofTypeCode;
    }

    public void setIdProofTypeCode(String idProofTypeCode) {
        this.idProofTypeCode = idProofTypeCode;
    }

    public String getIdProofTypeName() {
        return idProofTypeName;
    }

    public void setIdProofTypeName(String idProofTypeName) {
        this.idProofTypeName = idProofTypeName;
    }

    public String getNotificationLanguage() {
        return notificationLanguage;
    }

    public void setNotificationLanguage(String notificationLanguage) {
        this.notificationLanguage = notificationLanguage;
    }

    public String getNotificationTypeCode() {
        return notificationTypeCode;
    }

    public void setNotificationTypeCode(String notificationTypeCode) {
        this.notificationTypeCode = notificationTypeCode;
    }

    public String getNotificationName() {
        return notificationName;
    }

    public void setNotificationName(String notificationName) {
        this.notificationName = notificationName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIssuingCountryCode() {
        return issuingCountryCode;
    }

    public void setIssuingCountryCode(String issuingCountryCode) {
        this.issuingCountryCode = issuingCountryCode;
    }

    public String getIssuingCountryName() {
        return issuingCountryName;
    }

    public void setIssuingCountryName(String issuingCountryName) {
        this.issuingCountryName = issuingCountryName;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isWalletExists() {
        return walletExists;
    }

    public void setWalletExists(boolean walletExists) {
        this.walletExists = walletExists;
    }

    public String getProfileTypeCode() {
        return profileTypeCode;
    }

    public void setProfileTypeCode(String profileTypeCode) {
        this.profileTypeCode = profileTypeCode;
    }

    public String getProfileTypeName() {
        return profileTypeName;
    }

    public void setProfileTypeName(String profileTypeName) {
        this.profileTypeName = profileTypeName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getWalletOwnerCatName() {
        return walletOwnerCatName;
    }

    public void setWalletOwnerCatName(String walletOwnerCatName) {
        this.walletOwnerCatName = walletOwnerCatName;
    }

    public String getRequestedSource() {
        return requestedSource;
    }

    public void setRequestedSource(String requestedSource) {
        this.requestedSource = requestedSource;
    }

    public String getRegesterCountryDialCode() {
        return regesterCountryDialCode;
    }

    public void setRegesterCountryDialCode(String regesterCountryDialCode) {
        this.regesterCountryDialCode = regesterCountryDialCode;
    }

    public String getIssuingCountryDialCode() {
        return issuingCountryDialCode;
    }

    public void setIssuingCountryDialCode(String issuingCountryDialCode) {
        this.issuingCountryDialCode = issuingCountryDialCode;
    }

    public String getWalletOwnerCode() {
        return walletOwnerCode;
    }

    public void setWalletOwnerCode(String walletOwnerCode) {
        this.walletOwnerCode = walletOwnerCode;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public boolean isLoginWithOtpRequired() {
        return loginWithOtpRequired;
    }

    public void setLoginWithOtpRequired(boolean loginWithOtpRequired) {
        this.loginWithOtpRequired = loginWithOtpRequired;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
