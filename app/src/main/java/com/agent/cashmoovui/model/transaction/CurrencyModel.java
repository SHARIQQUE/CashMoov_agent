package com.agent.cashmoovui.model.transaction;

public class CurrencyModel {

    public String code;
    public String Ccode;
    public String Ocode;
    public String currencyCode;
    public String currencyName;
    public String currencySymbol;
    public String walletTypeCode;
    public String minValue;
    public String maxValue;
    public String commisionWalletValue;
    public String overdraftWalletValue;
    public String mainWalletValue;
    public String walletOwnerName;
    public String allocatedValue;
    public String alertValue;

    public String getWalletOwnerName() {
        return walletOwnerName;
    }

    public void setWalletOwnerName(String walletOwnerName) {
        this.walletOwnerName = walletOwnerName;
    }

    @Override
    public String toString() {
        return "CurrencyModel{" +
                "code='" + code + '\'' +
                ", Ccode='" + Ccode + '\'' +
                ", Ocode='" + Ocode + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", currencyName='" + currencyName + '\'' +
                ", currencySymbol='" + currencySymbol + '\'' +
                ", walletTypeCode='" + walletTypeCode + '\'' +
                ", minValue='" + minValue + '\'' +
                ", maxValue='" + maxValue + '\'' +
                ", commisionWalletValue='" + commisionWalletValue + '\'' +
                ", overdraftWalletValue='" + overdraftWalletValue + '\'' +
                ", mainWalletValue='" + mainWalletValue + '\'' +
                ", walletOwnerName='" + walletOwnerName + '\'' +
                ", allocatedValue='" + allocatedValue + '\'' +
                ", alertValue='" + alertValue + '\'' +

                '}';
    }

    public CurrencyModel(String code, String ccode, String ocode, String currencyCode, String currencyName, String currencySymbol, String walletTypeCode,String maxValue,String minValue,String commisionWalletValue, String overdraftWalletValue, String mainWalletValue, String walletOwnerName, String allocatedValue,String alertValue) {
        this.code = code;
        Ccode = ccode;
        Ocode = ocode;
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.currencySymbol = currencySymbol;
        this.walletTypeCode = walletTypeCode;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.commisionWalletValue = commisionWalletValue;
        this.overdraftWalletValue = overdraftWalletValue;
        this.mainWalletValue = mainWalletValue;
        this.walletOwnerName = walletOwnerName;
        this.allocatedValue = allocatedValue;
        this.alertValue = alertValue;

    }

    public String getWalletTypeCode() {
        return walletTypeCode;
    }

    public void setWalletTypeCode(String walletTypeCode) {
        this.walletTypeCode = walletTypeCode;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getAlertValue() {
        return alertValue;
    }

    public void setAlertValue(String alertValue) {
        this.alertValue = alertValue;
    }

    public String getAllocatedValue() {
        return allocatedValue;
    }

    public void setAllocatedValue(String allocatedValue) {
        this.allocatedValue = allocatedValue;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCcode() {
        return Ccode;
    }

    public void setCcode(String ccode) {
        Ccode = ccode;
    }

    public String getOcode() {
        return Ocode;
    }

    public void setOcode(String ocode) {
        Ocode = ocode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getCommisionWalletValue() {
        return commisionWalletValue;
    }

    public void setCommisionWalletValue(String commisionWalletValue) {
        this.commisionWalletValue = commisionWalletValue;
    }

    public String getOverdraftWalletValue() {
        return overdraftWalletValue;
    }

    public void setOverdraftWalletValue(String overdraftWalletValue) {
        this.overdraftWalletValue = overdraftWalletValue;
    }

    public String getMainWalletValue() {
        return mainWalletValue;
    }

    public void setMainWalletValue(String mainWalletValue) {
        this.mainWalletValue = mainWalletValue;
    }
}
