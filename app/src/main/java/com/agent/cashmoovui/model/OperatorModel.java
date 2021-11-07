package com.agent.cashmoovui.model;

public class OperatorModel {
    public int id;
    public String code;
    public String creationDate;
    public String modificationDate;
    public String name;
    public String serviceCategoryCode;
    public String serviceCategoryName;
    public String serviceCode;
    public String serviceName;
    public String serviceProviderCode;
    public String serviceProviderName;
    public String state;
    public String status;

    public OperatorModel(int id, String code, String creationDate, String modificationDate, String name, String serviceCategoryCode, String serviceCategoryName, String serviceCode, String serviceName, String serviceProviderCode, String serviceProviderName, String state, String status) {
        this.id = id;
        this.code = code;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.name = name;
        this.serviceCategoryCode = serviceCategoryCode;
        this.serviceCategoryName = serviceCategoryName;
        this.serviceCode = serviceCode;
        this.serviceName = serviceName;
        this.serviceProviderCode = serviceProviderCode;
        this.serviceProviderName = serviceProviderName;
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

    public String getServiceProviderCode() {
        return serviceProviderCode;
    }

    public void setServiceProviderCode(String serviceProviderCode) {
        this.serviceProviderCode = serviceProviderCode;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
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