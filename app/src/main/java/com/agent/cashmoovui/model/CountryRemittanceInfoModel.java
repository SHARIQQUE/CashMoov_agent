package com.agent.cashmoovui.model;

import java.util.ArrayList;

public class CountryRemittanceInfoModel {
    ArrayList<CountryRemittanceInfoModel.RemitCountry> countryRemitArrayList = new ArrayList<>();

    public CountryRemittanceInfoModel(ArrayList<CountryRemittanceInfoModel.RemitCountry> countryRemitArrayList) {
        this.countryRemitArrayList = countryRemitArrayList;
    }

    public ArrayList<RemitCountry> getCountryRemitArrayList() {
        return countryRemitArrayList;
    }

    public void setCountryRemitArrayList(ArrayList<RemitCountry> countryRemitArrayList) {
        this.countryRemitArrayList = countryRemitArrayList;
    }


    public static class RemitCountry {
        public int id;
        public String code;
        public String countryCode;
        public String countryIsoCode;
        public String countryName;
        public String createdBy;
        public String creationDate;
        public String currencyCode;
        public String currencySymbol;
        public String dialCode;
        public String mobileLength;
        public String modificationDate;
        public String modifiedBy;
        public String state;
        public String status;
        public boolean remitReceiving;
        public boolean remitSending;

        public RemitCountry(int id, String code, String countryCode, String countryIsoCode, String countryName, String createdBy, String creationDate, String currencyCode, String currencySymbol, String dialCode, String mobileLength, String modificationDate, String modifiedBy, String state, String status, boolean remitReceiving, boolean remitSending) {
            this.id = id;
            this.code = code;
            this.countryCode = countryCode;
            this.countryIsoCode = countryIsoCode;
            this.countryName = countryName;
            this.createdBy = createdBy;
            this.creationDate = creationDate;
            this.currencyCode = currencyCode;
            this.currencySymbol = currencySymbol;
            this.dialCode = dialCode;
            this.mobileLength = mobileLength;
            this.modificationDate = modificationDate;
            this.modifiedBy = modifiedBy;
            this.state = state;
            this.status = status;
            this.remitReceiving = remitReceiving;
            this.remitSending = remitSending;
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

        public String getCountryIsoCode() {
            return countryIsoCode;
        }

        public void setCountryIsoCode(String countryIsoCode) {
            this.countryIsoCode = countryIsoCode;
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

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
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

        public String getMobileLength() {
            return mobileLength;
        }

        public void setMobileLength(String mobileLength) {
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

        public boolean isRemitReceiving() {
            return remitReceiving;
        }

        public void setRemitReceiving(boolean remitReceiving) {
            this.remitReceiving = remitReceiving;
        }

        public boolean isRemitSending() {
            return remitSending;
        }

        public void setRemitSending(boolean remitSending) {
            this.remitSending = remitSending;
        }
    }
    }
