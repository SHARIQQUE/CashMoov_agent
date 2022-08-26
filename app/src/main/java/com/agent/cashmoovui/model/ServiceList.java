package com.agent.cashmoovui.model;

import java.util.ArrayList;

public  class ServiceList {


    public ArrayList<serviceListMain> serviceListMains=new ArrayList<serviceListMain>();

    public ArrayList<serviceListMain> getServiceListMains() {
        return serviceListMains;
    }

    public void setServiceListMains(ArrayList<serviceListMain> serviceListMains) {
        this.serviceListMains = serviceListMains;
    }

    public ServiceList(ArrayList<serviceListMain> serviceListMains) {
        this.serviceListMains = serviceListMains;
    }

    public static class serviceListMain {
        public int id;
        public String code;
        public String name;
        public String status;
        public String creationDate;
        public boolean isOverdraftTrans;
        public ArrayList<serviceCategoryList> serviceCategoryListArrayList=new ArrayList<serviceCategoryList>();


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

        public String getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(String creationDate) {
            this.creationDate = creationDate;
        }

        public boolean isOverdraftTrans() {
            return isOverdraftTrans;
        }

        public void setOverdraftTrans(boolean overdraftTrans) {
            isOverdraftTrans = overdraftTrans;
        }

        public ArrayList<serviceCategoryList> getServiceCategoryListArrayList() {
            return serviceCategoryListArrayList;
        }

        public void setServiceCategoryListArrayList(ArrayList<serviceCategoryList> serviceCategoryListArrayList) {
            this.serviceCategoryListArrayList = serviceCategoryListArrayList;
        }

        public serviceListMain(int id, String code, String name, String status, String creationDate, boolean isOverdraftTrans, ArrayList<serviceCategoryList> serviceCategoryListArrayList) {
            this.id = id;
            this.code = code;
            this.name = name;
            this.status = status;
            this.creationDate = creationDate;
            this.isOverdraftTrans = isOverdraftTrans;
            this.serviceCategoryListArrayList = serviceCategoryListArrayList;
        }
    }

    public static class serviceCategoryList {
        public int id;
        public String code;
        public String serviceCode;
        public String serviceName;
        public String name;
        public String status;
        public String creationDate;
        public boolean productAllowed;
        public int minTransValue;
        public int maxTransValue;


        public int getMinTransValue() {
            return minTransValue;
        }

        public void setMinTransValue(int minTransValue) {
            this.minTransValue = minTransValue;
        }

        public int getMaxTransValue() {
            return maxTransValue;
        }

        public void setMaxTransValue(int maxTransValue) {
            this.maxTransValue = maxTransValue;
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

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
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

        public String getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(String creationDate) {
            this.creationDate = creationDate;
        }

        public boolean isProductAllowed() {
            return productAllowed;
        }

        public void setProductAllowed(boolean productAllowed) {
            this.productAllowed = productAllowed;
        }

        public serviceCategoryList(int id, String code, String serviceCode, String serviceName, String name, String status, String creationDate, boolean productAllowed, int minTransValue, int maxTransValue) {
            this.id = id;
            this.code = code;
            this.serviceCode = serviceCode;
            this.serviceName = serviceName;
            this.name = name;
            this.status = status;
            this.creationDate = creationDate;
            this.productAllowed = productAllowed;
            this.minTransValue = minTransValue;
            this.maxTransValue = maxTransValue;
        }
    }


}
