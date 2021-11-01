package com.agent.cashmoovui.model;

import java.util.ArrayList;

public class OccupationTypeModel {

    ArrayList<OccupationType> occupationTypeArrayList = new ArrayList<>();

    public OccupationTypeModel(ArrayList<OccupationType> occupationTypeArrayList) {
        this.occupationTypeArrayList = occupationTypeArrayList;
    }

    public ArrayList<OccupationType> getOccupationTypeArrayList() {
        return occupationTypeArrayList;
    }

    public void setOccupationTypeArrayList(ArrayList<OccupationType> occupationTypeArrayList) {
        this.occupationTypeArrayList = occupationTypeArrayList;
    }

    public static class OccupationType {
        public int id;
        public String code;
        public String type;
        public String status;
        public String creationDate;

        public OccupationType(int id, String code, String type, String status, String creationDate) {
            this.id = id;
            this.code = code;
            this.type = type;
            this.status = status;
            this.creationDate = creationDate;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(String creationDate) {
            this.creationDate = creationDate;
        }
    }
}