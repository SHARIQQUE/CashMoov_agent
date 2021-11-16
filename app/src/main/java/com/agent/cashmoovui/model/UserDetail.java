package com.agent.cashmoovui.model;

public class UserDetail {

    private String transTypeName;
    private String destMobileNumber;
    private String transactionAmount;
    private String desCurrencyName;
    private String creationDate;
    private String desCurrencySymbol;

    public String getTransTypeName() {
        return transTypeName;
    }

    public void setTransTypeName(String transTypeName) {
        this.transTypeName = transTypeName;
    }

    public String getDestMobileNumber() {
        return destMobileNumber;
    }

    public void setDestMobileNumber(String destMobileNumber) {
        this.destMobileNumber = destMobileNumber;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getDesCurrencyName() {
        return desCurrencyName;
    }

    public void setDesCurrencyName(String desCurrencyName) {
        this.desCurrencyName = desCurrencyName;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getDesCurrencySymbol() {
        return desCurrencySymbol;
    }

    public void setDesCurrencySymbol(String desCurrencySymbol) {
        this.desCurrencySymbol = desCurrencySymbol;
    }
}
