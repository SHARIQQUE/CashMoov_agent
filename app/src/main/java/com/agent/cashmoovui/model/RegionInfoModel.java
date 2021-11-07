package com.agent.cashmoovui.model;

import java.util.ArrayList;

public class RegionInfoModel {
    ArrayList<Region> regionArrayList = new ArrayList<>();

    public RegionInfoModel(ArrayList<Region> regionArrayList) {
        this.regionArrayList = regionArrayList;
    }

    public ArrayList<Region> getRegionArrayList() {
        return regionArrayList;
    }

    public void setRegionArrayList(ArrayList<Region> regionArrayList) {
        this.regionArrayList = regionArrayList;
    }

    public static class Region {
        public int id;
        public String code;
        public String countryCode;
        public String countryName;
        public String creationDate;
        public String name;
        public String state;
        public String status;

        public Region(int id, String code, String countryCode, String countryName, String creationDate, String name,
                      String state, String status) {
            this.id = id;
            this.code = code;
            this.countryCode = countryCode;
            this.countryName = countryName;
            this.creationDate = creationDate;
            this.name = name;
            this.state = state;
            this.status = status;
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

        public String getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(String creationDate) {
            this.creationDate = creationDate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
    }
}
