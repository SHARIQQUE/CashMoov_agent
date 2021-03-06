package com.agent.cashmoovui.adapter;

public class TransferCommissionModal {

    public String code;
    public String Ccode;
    public String Ocode;
    public String currencyCode;
    public String currencyName;
    public String currencySymbol;
    public String commisionWalletValue;
    public String overdraftWalletValue;
    public String mainWalletValue;

    public TransferCommissionModal(String code, String ccode, String ocode, String currencyCode, String currencyName, String currencySymbol, String commisionWalletValue, String overdraftWalletValue, String mainWalletValue) {
        this.code = code;
        Ccode = ccode;
        Ocode = ocode;
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
                "code='" + code + '\'' +
                ", Ccode='" + Ccode + '\'' +
                ", Ocode='" + Ocode + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", currencyName='" + currencyName + '\'' +
                ", currencySymbol='" + currencySymbol + '\'' +
                ", commisionWalletValue='" + commisionWalletValue + '\'' +
                ", overdraftWalletValue='" + overdraftWalletValue + '\'' +
                ", mainWalletValue='" + mainWalletValue + '\'' +
                '}';
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
