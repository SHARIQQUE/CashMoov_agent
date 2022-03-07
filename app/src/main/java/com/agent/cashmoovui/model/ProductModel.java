package com.agent.cashmoovui.model;

public class ProductModel {
    public int id;
    public String code;
    public String creationDate;
    public String description;
    public int maxValue;
    public int minValue;
    public int value;
    public String modificationDate;
    public String name;
    public String operatorCode;
    public String operatorName;
    public String productMasterCode;
    public String productTypeCode;
    public String productTypeName;
    public String serviceCategoryCode;
    public String serviceCategoryName;
    public String state;
    public String status;
    public String vendorProductCode;

    public ProductModel(int id, String code, String creationDate, String description, int maxValue, int minValue, int value, String modificationDate, String name, String operatorCode, String operatorName, String productMasterCode, String productTypeCode, String productTypeName, String serviceCategoryCode, String serviceCategoryName, String state, String status, String vendorProductCode) {
        this.id = id;
        this.code = code;
        this.creationDate = creationDate;
        this.description = description;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.value = value;
        this.modificationDate = modificationDate;
        this.name = name;
        this.operatorCode = operatorCode;
        this.operatorName = operatorName;
        this.productMasterCode = productMasterCode;
        this.productTypeCode = productTypeCode;
        this.productTypeName = productTypeName;
        this.serviceCategoryCode = serviceCategoryCode;
        this.serviceCategoryName = serviceCategoryName;
        this.state = state;
        this.status = status;
        this.vendorProductCode = vendorProductCode;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
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

    public String getProductMasterCode() {
        return productMasterCode;
    }

    public void setProductMasterCode(String productMasterCode) {
        this.productMasterCode = productMasterCode;
    }

    public String getProductTypeCode() {
        return productTypeCode;
    }

    public void setProductTypeCode(String productTypeCode) {
        this.productTypeCode = productTypeCode;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
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

    public String getVendorProductCode() {
        return vendorProductCode;
    }

    public void setVendorProductCode(String vendorProductCode) {
        this.vendorProductCode = vendorProductCode;
    }
}
