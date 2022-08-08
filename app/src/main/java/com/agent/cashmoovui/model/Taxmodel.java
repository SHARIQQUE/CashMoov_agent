package com.agent.cashmoovui.model;

public class Taxmodel {

    private int taxTypeCode;
    private String taxTypeName;
    private String value;
    private String taxAvailBy;
    private String feeAmountTaxValue;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTaxAvaiBy() {
        return taxAvailBy;
    }

    public void setTaxAvaiBy(String taxAvaiBy) {
        this.taxAvailBy = taxAvaiBy;
    }

    public String getFeeAmountTaxValue() {
        return feeAmountTaxValue;
    }

    public void setFeeAmountTaxValue(String feeAmountTaxValue) {
        this.feeAmountTaxValue = feeAmountTaxValue;
    }

    public int getTaxTypeCode() {
        return taxTypeCode;
    }

    public void setTaxTypeCode(int taxTypeCode) {
        this.taxTypeCode = taxTypeCode;
    }

    public String getTaxTypeName() {
        return taxTypeName;
    }

    public void setTaxTypeName(String taxTypeName) {
        this.taxTypeName = taxTypeName;
    }
}
