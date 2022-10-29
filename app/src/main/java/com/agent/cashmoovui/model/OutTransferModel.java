package com.agent.cashmoovui.model;

import java.util.ArrayList;

public class OutTransferModel {

    ArrayList<OutModel> serviceProviderArrayList = new ArrayList<>();

    public OutTransferModel(ArrayList<OutModel> serviceProviderArrayList) {
        this.serviceProviderArrayList = serviceProviderArrayList;
    }

    public ArrayList<OutModel> getServiceProviderArrayList() {
        return serviceProviderArrayList;
    }

    public void setServiceProviderArrayList(ArrayList<OutModel> serviceProviderArrayList) {
        this.serviceProviderArrayList = serviceProviderArrayList;
    }


    public static class OutModel {
        public int id;
        public String code;
        public String serviceCode;
        public String serviceCategoryCode;
        public String serviceProviderCode;
        public String serviceItemId;
        public String nameFr;
        public String name;
        public String status;
        public String state;
        public String creationDate;

        public OutModel(int id, String code, String serviceCode, String serviceCategoryCode, String serviceProviderCode, String serviceItemId, String nameFr, String name, String status, String state, String creationDate) {
            this.id = id;
            this.code = code;
            this.serviceCode = serviceCode;
            this.serviceCategoryCode = serviceCategoryCode;
            this.serviceProviderCode = serviceProviderCode;
            this.serviceItemId = serviceItemId;
            this.nameFr = nameFr;
            this.name = name;
            this.status = status;
            this.state = state;
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

        public String getServiceCode() {
            return serviceCode;
        }

        public void setServiceCode(String serviceCode) {
            this.serviceCode = serviceCode;
        }

        public String getServiceCategoryCode() {
            return serviceCategoryCode;
        }

        public void setServiceCategoryCode(String serviceCategoryCode) {
            this.serviceCategoryCode = serviceCategoryCode;
        }

        public String getServiceProviderCode() {
            return serviceProviderCode;
        }

        public void setServiceProviderCode(String serviceProviderCode) {
            this.serviceProviderCode = serviceProviderCode;
        }

        public String getServiceItemId() {
            return serviceItemId;
        }

        public void setServiceItemId(String serviceItemId) {
            this.serviceItemId = serviceItemId;
        }

        public String getNameFr() {
            return nameFr;
        }

        public void setNameFr(String nameFr) {
            this.nameFr = nameFr;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(String creationDate) {
            this.creationDate = creationDate;
        }
    }

}
