package com.agent.cashmoovui.overdraft;

public class OverDraftModal {


    String currencyName="";
    String amount="";
    String currencySymbol="";
    String status="";
    String creationDate="";
    String maximumLimit="";
    String validityMaxDays="";
    String currencyCode="";

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getMaximumLimit() {
        return maximumLimit;
    }

    public void setMaximumLimit(String maximumLimit) {
        this.maximumLimit = maximumLimit;
    }


    public String getValidityMaxDays() {
        return validityMaxDays;
    }

    public void setValidityMaxDays(String validityMaxDays) {
        this.validityMaxDays = validityMaxDays;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
