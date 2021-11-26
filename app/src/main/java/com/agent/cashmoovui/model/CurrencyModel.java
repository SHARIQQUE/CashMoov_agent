package com.agent.cashmoovui.model;

public class CurrencyModel {

    public String currencyCode;
    public String currencyName;
    public String currencySymbol;
    public String commisionWalletValue;
    public String overdraftWalletValue;
    public String mainWalletValue;

    public CurrencyModel(String currencyCode, String currencyName, String currencySymbol, String commisionWalletValue, String overdraftWalletValue, String mainWalletValue) {
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.currencySymbol = currencySymbol;
        this.commisionWalletValue = commisionWalletValue;
        this.overdraftWalletValue = overdraftWalletValue;
        this.mainWalletValue = mainWalletValue;
    }

    @Override
    public String toString() {
        return "CurrencyModel{" +
                "currencyCode='" + currencyCode + '\'' +
                ", currencyName='" + currencyName + '\'' +
                ", currencySymbol='" + currencySymbol + '\'' +
                ", commisionWalletValue='" + commisionWalletValue + '\'' +
                ", overdraftWalletValue='" + overdraftWalletValue + '\'' +
                ", mainWalletValue='" + mainWalletValue + '\'' +
                '}';
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
