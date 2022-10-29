package com.agent.cashmoovui.model;

public class AmountDetailsInfoModel {
    public String transactionId;
    public String requestTime;
    public String responseTime;
    public String resultCode;
    public String resultDescription;
    public AmountDetails amountDetails;

    public AmountDetailsInfoModel(String transactionId, String requestTime, String responseTime, String resultCode, String resultDescription, AmountDetails amountDetails) {
        this.transactionId = transactionId;
        this.requestTime = requestTime;
        this.responseTime = responseTime;
        this.resultCode = resultCode;
        this.resultDescription = resultDescription;
        this.amountDetails = amountDetails;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    public AmountDetails getAmountDetails() {
        return amountDetails;
    }

    public void setAmountDetails(AmountDetails amountDetails) {
        this.amountDetails = amountDetails;
    }

    public static class AmountDetails {
        public int fee;
        public int receiverFee;
        public int receiverTax;
        public String value;
        public String currencyValue;

        public AmountDetails(int fee, int receiverFee, int receiverTax, String value, String currencyValue) {
            this.fee = fee;
            this.receiverFee = receiverFee;
            this.receiverTax = receiverTax;
            this.value = value;
            this.currencyValue = currencyValue;
        }

        public int getFee() {
            return fee;
        }

        public void setFee(int fee) {
            this.fee = fee;
        }

        public int getReceiverFee() {
            return receiverFee;
        }

        public void setReceiverFee(int receiverFee) {
            this.receiverFee = receiverFee;
        }

        public int getReceiverTax() {
            return receiverTax;
        }

        public void setReceiverTax(int receiverTax) {
            this.receiverTax = receiverTax;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCurrencyValue() {
            return currencyValue;
        }

        public void setCurrencyValue(String currencyValue) {
            this.currencyValue = currencyValue;
        }
    }
}