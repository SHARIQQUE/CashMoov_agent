package com.agent.cashmoovui.model;

import java.util.ArrayList;

public class CountryCurrencyInfoModel {

    ArrayList<CountryCurrency> countryCurrencyArrayList=new ArrayList<>();

    public CountryCurrencyInfoModel(ArrayList<CountryCurrency> countryCurrencyArrayList) {
        this.countryCurrencyArrayList = countryCurrencyArrayList;
    }

    public ArrayList<CountryCurrency> getCountryCurrencyArrayList() {
        return countryCurrencyArrayList;
    }

    public void setCountryCurrencyArrayList(ArrayList<CountryCurrency> countryCurrencyArrayList) {
        this.countryCurrencyArrayList = countryCurrencyArrayList;
    }

    public static class CountryCurrency {
        public int id;
        public String code;
        public String countryCode;
        public String countryName;
        public String createdBy;
        public String creationDate;
        public String currCode;
        public String currencyCode;
        public String currencyName;
        public String currencySymbol;
        public String dialCode;
        public int mobileLength;
        public String modificationDate;
        public String modifiedBy;
        public String state;
        public String status;
        public boolean inBound;
        public boolean outBound;

        public CountryCurrency(int id, String code, String countryCode, String countryName, String createdBy, String creationDate, String currCode, String currencyCode, String currencyName, String currencySymbol, String dialCode, int mobileLength, String modificationDate, String modifiedBy, String state, String status, boolean inBound, boolean outBound) {
            this.id = id;
            this.code = code;
            this.countryCode = countryCode;
            this.countryName = countryName;
            this.createdBy = createdBy;
            this.creationDate = creationDate;
            this.currCode = currCode;
            this.currencyCode = currencyCode;
            this.currencyName = currencyName;
            this.currencySymbol = currencySymbol;
            this.dialCode = dialCode;
            this.mobileLength = mobileLength;
            this.modificationDate = modificationDate;
            this.modifiedBy = modifiedBy;
            this.state = state;
            this.status = status;
            this.inBound = inBound;
            this.outBound = outBound;
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

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(String creationDate) {
            this.creationDate = creationDate;
        }

        public String getCurrCode() {
            return currCode;
        }

        public void setCurrCode(String currCode) {
            this.currCode = currCode;
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

        public String getDialCode() {
            return dialCode;
        }

        public void setDialCode(String dialCode) {
            this.dialCode = dialCode;
        }

        public int getMobileLength() {
            return mobileLength;
        }

        public void setMobileLength(int mobileLength) {
            this.mobileLength = mobileLength;
        }

        public String getModificationDate() {
            return modificationDate;
        }

        public void setModificationDate(String modificationDate) {
            this.modificationDate = modificationDate;
        }

        public String getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public boolean isInBound() {
            return inBound;
        }

        public void setInBound(boolean inBound) {
            this.inBound = inBound;
        }

        public boolean isOutBound() {
            return outBound;
        }

        public void setOutBound(boolean outBound) {
            this.outBound = outBound;
        }
    }
}