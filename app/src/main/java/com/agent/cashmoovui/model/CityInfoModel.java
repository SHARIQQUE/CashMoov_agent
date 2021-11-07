package com.agent.cashmoovui.model;

import java.util.ArrayList;

public class CityInfoModel {
    ArrayList<City> cityArrayList = new ArrayList<>();

    public CityInfoModel(ArrayList<City> cityArrayList) {
        this.cityArrayList = cityArrayList;
    }

    public ArrayList<City> getCityArrayList() {
        return cityArrayList;
    }

    public void setCityArrayList(ArrayList<City> cityArrayList) {
        this.cityArrayList = cityArrayList;
    }


    public static class City {
        public int id;
        public String code;
        public String creationDate;
        public String modificationDate;
        public String name;
        public String regionCode;
        public String regionName;
        public String state;
        public String status;

        public City(int id, String code, String creationDate, String modificationDate, String name, String regionCode, String regionName, String state, String status) {
            this.id = id;
            this.code = code;
            this.creationDate = creationDate;
            this.modificationDate = modificationDate;
            this.name = name;
            this.regionCode = regionCode;
            this.regionName = regionName;
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

        public String getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(String creationDate) {
            this.creationDate = creationDate;
        }

        public String getModificationDate() {
            return modificationDate;
        }

        public void setModificationDate(String modificationDate) {
            this.modificationDate = modificationDate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRegionCode() {
            return regionCode;
        }

        public void setRegionCode(String regionCode) {
            this.regionCode = regionCode;
        }

        public String getRegionName() {
            return regionName;
        }

        public void setRegionName(String regionName) {
            this.regionName = regionName;
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