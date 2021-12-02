package com.agent.cashmoovui.model;

public class FeeDetailModel {
    public String range;
    public String value;

    @Override
    public String toString() {
        return "FeeDetailModel{" +
                "range='" + range + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public FeeDetailModel(String range, String value) {
        this.range = range;
        this.value = value;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
