package com.agent.cashmoovui.model;

import java.util.ArrayList;

public class GenderInfoModel {

    ArrayList<Gender> genderArrayList = new ArrayList<>();

    public GenderInfoModel(ArrayList<Gender> genderArrayList) {
        this.genderArrayList = genderArrayList;
    }

    public ArrayList<Gender> getGenderArrayList() {
        return genderArrayList;
    }

    public void setGenderArrayList(ArrayList<Gender> genderArrayList) {
        this.genderArrayList = genderArrayList;
    }

    public static class Gender {
        public int id;
        public String code;
        public String creationDate;
        public String status;
        public String type;

        public Gender(int id, String code, String creationDate, String status, String type) {
            this.id = id;
            this.code = code;
            this.creationDate = creationDate;
            this.status = status;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }
}