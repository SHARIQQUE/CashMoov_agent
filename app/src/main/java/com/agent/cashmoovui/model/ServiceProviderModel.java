package com.agent.cashmoovui.model;

import java.util.ArrayList;

public class ServiceProviderModel {

    ArrayList<ServiceProvider> serviceProviderArrayList = new ArrayList<>();

    public ServiceProviderModel(ArrayList<ServiceProvider> serviceProviderArrayList) {
        this.serviceProviderArrayList = serviceProviderArrayList;
    }

    public ArrayList<ServiceProvider> getServiceProviderArrayList() {
        return serviceProviderArrayList;
    }

    public void setServiceProviderArrayList(ArrayList<ServiceProvider> serviceProviderArrayList) {
        this.serviceProviderArrayList = serviceProviderArrayList;
    }


    public static class ServiceProvider {
        public int id;
        public String code;
        public String creationDate;
        public String name;
        public String serviceCategoryCode;
        public String serviceCategoryName;
        public String serviceCode;
        public String serviceName;
        public String serviceProviderMasterCode;
        public String status;

        public ServiceProvider(int id, String code, String creationDate, String name, String serviceCategoryCode, String serviceCategoryName, String serviceCode, String serviceName, String serviceProviderMasterCode, String status) {
            this.id = id;
            this.code = code;
            this.creationDate = creationDate;
            this.name = name;
            this.serviceCategoryCode = serviceCategoryCode;
            this.serviceCategoryName = serviceCategoryName;
            this.serviceCode = serviceCode;
            this.serviceName = serviceName;
            this.serviceProviderMasterCode = serviceProviderMasterCode;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getServiceCategoryCode() {
            return serviceCategoryCode;
        }

        public void setServiceCategoryCode(String serviceCategoryCode) {
            this.serviceCategoryCode = serviceCategoryCode;
        }

        public String getServiceCategoryName() {
            return serviceCategoryName;
        }

        public void setServiceCategoryName(String serviceCategoryName) {
            this.serviceCategoryName = serviceCategoryName;
        }

        public String getServiceCode() {
            return serviceCode;
        }

        public void setServiceCode(String serviceCode) {
            this.serviceCode = serviceCode;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getServiceProviderMasterCode() {
            return serviceProviderMasterCode;
        }

        public void setServiceProviderMasterCode(String serviceProviderMasterCode) {
            this.serviceProviderMasterCode = serviceProviderMasterCode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

}
