package com.agent.cashmoovui.model;

public class ProductMasterModel {
    public int id;
    public String code;
    public String creationDate;
    public String operatorCode;
    public String operatorName;
    public String productName;
    public String serviceCategoryCode;
    public String serviceCategoryName;
    public String state;
    public String status;

    public ProductMasterModel(int id, String code, String creationDate, String operatorCode, String operatorName, String productName, String serviceCategoryCode, String serviceCategoryName, String state, String status) {
        this.id = id;
        this.code = code;
        this.creationDate = creationDate;
        this.operatorCode = operatorCode;
        this.operatorName = operatorName;
        this.productName = productName;
        this.serviceCategoryCode = serviceCategoryCode;
        this.serviceCategoryName = serviceCategoryName;
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

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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
