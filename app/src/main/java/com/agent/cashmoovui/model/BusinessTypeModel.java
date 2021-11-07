package com.agent.cashmoovui.model;

import java.util.ArrayList;

public class BusinessTypeModel {

        ArrayList<BusinessType> businessTypeArrayList = new ArrayList<>();

        public BusinessTypeModel(ArrayList<BusinessType> businessTypeArrayList) {
            this.businessTypeArrayList = businessTypeArrayList;
        }

    public ArrayList<BusinessType> getBusinessTypeArrayList() {
        return businessTypeArrayList;
    }

    public void setBusinessTypeArrayList(ArrayList<BusinessType> businessTypeArrayList) {
        this.businessTypeArrayList = businessTypeArrayList;
    }


    public static class BusinessType {
        public int id;
        public String code;
        public String creationDate;
        public String type;

        public BusinessType(int id, String code, String creationDate, String type) {
            this.id = id;
            this.code = code;
            this.creationDate = creationDate;
            this.type = type;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
