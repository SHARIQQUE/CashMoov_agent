package com.agent.cashmoovui.model;

import java.util.ArrayList;

public class CountryInfoModel {
    ArrayList<Country> countryArrayList = new ArrayList<>();

    public CountryInfoModel(ArrayList<Country> countryArrayList) {
        this.countryArrayList = countryArrayList;
    }

    public ArrayList<Country> getCountryArrayList() {
        return countryArrayList;
    }

    public void setCountryArrayList(ArrayList<Country> countryArrayList) {
        this.countryArrayList = countryArrayList;
    }

    public static class Country {
        public int id;
        public String code;
        public String isoCode;
        public String name;
        public String countryCode;
        public String status;
        public String dialCode;
        public String currencyCode;
        public String currencySymbol;
        public String creationDate;
        public boolean subscriberAllowed;



        public Country(int i, int id, String code, String isoCode, String name, String countryCode, String status,
                       String dialCode, String currencyCode, String currencySymbol, String creationDate,
                       boolean subscriberAllowed) {
            this.id = id;
            this.code = code;
            this.isoCode = isoCode;
            this.name = name;
            this.countryCode = countryCode;
            this.status = status;
            this.dialCode = dialCode;
            this.currencyCode = currencyCode;
            this.currencySymbol = currencySymbol;
            this.creationDate = creationDate;
            this.subscriberAllowed = subscriberAllowed;
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

        public String getIsoCode() {
            return isoCode;
        }

        public void setIsoCode(String isoCode) {
            this.isoCode = isoCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDialCode() {
            return dialCode;
        }

        public void setDialCode(String dialCode) {
            this.dialCode = dialCode;
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

        public String getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(String creationDate) {
            this.creationDate = creationDate;
        }

        public boolean isSubscriberAllowed() {
            return subscriberAllowed;
        }

        public void setSubscriberAllowed(boolean subscriberAllowed) {
            this.subscriberAllowed = subscriberAllowed;
        }
    }
}
