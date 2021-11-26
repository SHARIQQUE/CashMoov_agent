package com.agent.cashmoovui.transactionhistory_walletscreen;

public class TransactionModel {
    public int id;
    public String code;
    public String transactionId;
    public String transTypeCode;
    public String transTypeName;
    public String srcWalletOwnerCode;
    public String srcWalletOwnerName;
    public String desWalletOwnerCode;
    public String desWalletOwnerName;
    public String srcWalletCode;
    public String desWalletCode;
    public String srcCurrencyCode;
    public String srcCurrencyName;
    public String srcCurrencySymbol;
    public String desCurrencyCode;
    public String desCurrencyName;
    public String desCurrencySymbol;
    public int transactionAmount;
    public String tax;
    public String resultCode;
    public String resultDescription;
    public String creationDate;
    public String createdBy;
    public String status;
    public boolean transactionReversed;
    public int srcMobileNumber;
    public int destMobileNumber;
    public boolean receiverBearer;
    public String rechargeNumber;
    public int fee;
    public String taxTypeName;
    public int value;
    public int srcPreBalance;
    public int destPreBalance;
    public int srcPostBalance;
    public int destPostBalance;

    public TransactionModel(int id, String code, String transactionId, String transTypeCode, String transTypeName, String srcWalletOwnerCode, String srcWalletOwnerName, String desWalletOwnerCode, String desWalletOwnerName, String srcWalletCode, String desWalletCode, String srcCurrencyCode, String srcCurrencyName, String srcCurrencySymbol, String desCurrencyCode, String desCurrencyName, String desCurrencySymbol, int transactionAmount, String tax, String resultCode, String resultDescription, String creationDate, String createdBy, String status, boolean transactionReversed, int srcMobileNumber, int destMobileNumber, boolean receiverBearer, String rechargeNumber, int fee, String taxTypeName, int value, int srcPreBalance, int destPreBalance, int srcPostBalance, int destPostBalance) {
        this.id = id;
        this.code = code;
        this.transactionId = transactionId;
        this.transTypeCode = transTypeCode;
        this.transTypeName = transTypeName;
        this.srcWalletOwnerCode = srcWalletOwnerCode;
        this.srcWalletOwnerName = srcWalletOwnerName;
        this.desWalletOwnerCode = desWalletOwnerCode;
        this.desWalletOwnerName = desWalletOwnerName;
        this.srcWalletCode = srcWalletCode;
        this.desWalletCode = desWalletCode;
        this.srcCurrencyCode = srcCurrencyCode;
        this.srcCurrencyName = srcCurrencyName;
        this.srcCurrencySymbol = srcCurrencySymbol;
        this.desCurrencyCode = desCurrencyCode;
        this.desCurrencyName = desCurrencyName;
        this.desCurrencySymbol = desCurrencySymbol;
        this.transactionAmount = transactionAmount;
        this.tax = tax;
        this.resultCode = resultCode;
        this.resultDescription = resultDescription;
        this.creationDate = creationDate;
        this.createdBy = createdBy;
        this.status = status;
        this.transactionReversed = transactionReversed;
        this.srcMobileNumber = srcMobileNumber;
        this.destMobileNumber = destMobileNumber;
        this.receiverBearer = receiverBearer;
        this.rechargeNumber = rechargeNumber;
        this.fee = fee;
        this.taxTypeName = taxTypeName;
        this.value = value;
        this.srcPreBalance = srcPreBalance;
        this.destPreBalance = destPreBalance;
        this.srcPostBalance = srcPostBalance;
        this.destPostBalance = destPostBalance;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransTypeCode() {
        return transTypeCode;
    }

    public void setTransTypeCode(String transTypeCode) {
        this.transTypeCode = transTypeCode;
    }

    public String getTransTypeName() {
        return transTypeName;
    }

    public void setTransTypeName(String transTypeName) {
        this.transTypeName = transTypeName;
    }

    public String getSrcWalletOwnerCode() {
        return srcWalletOwnerCode;
    }

    public void setSrcWalletOwnerCode(String srcWalletOwnerCode) {
        this.srcWalletOwnerCode = srcWalletOwnerCode;
    }

    public String getSrcWalletOwnerName() {
        return srcWalletOwnerName;
    }

    public void setSrcWalletOwnerName(String srcWalletOwnerName) {
        this.srcWalletOwnerName = srcWalletOwnerName;
    }

    public String getDesWalletOwnerCode() {
        return desWalletOwnerCode;
    }

    public void setDesWalletOwnerCode(String desWalletOwnerCode) {
        this.desWalletOwnerCode = desWalletOwnerCode;
    }

    public String getDesWalletOwnerName() {
        return desWalletOwnerName;
    }

    public void setDesWalletOwnerName(String desWalletOwnerName) {
        this.desWalletOwnerName = desWalletOwnerName;
    }

    public String getSrcWalletCode() {
        return srcWalletCode;
    }

    public void setSrcWalletCode(String srcWalletCode) {
        this.srcWalletCode = srcWalletCode;
    }

    public String getDesWalletCode() {
        return desWalletCode;
    }

    public void setDesWalletCode(String desWalletCode) {
        this.desWalletCode = desWalletCode;
    }

    public String getSrcCurrencyCode() {
        return srcCurrencyCode;
    }

    public void setSrcCurrencyCode(String srcCurrencyCode) {
        this.srcCurrencyCode = srcCurrencyCode;
    }

    public String getSrcCurrencyName() {
        return srcCurrencyName;
    }

    public void setSrcCurrencyName(String srcCurrencyName) {
        this.srcCurrencyName = srcCurrencyName;
    }

    public String getSrcCurrencySymbol() {
        return srcCurrencySymbol;
    }

    public void setSrcCurrencySymbol(String srcCurrencySymbol) {
        this.srcCurrencySymbol = srcCurrencySymbol;
    }

    public String getDesCurrencyCode() {
        return desCurrencyCode;
    }

    public void setDesCurrencyCode(String desCurrencyCode) {
        this.desCurrencyCode = desCurrencyCode;
    }

    public String getDesCurrencyName() {
        return desCurrencyName;
    }

    public void setDesCurrencyName(String desCurrencyName) {
        this.desCurrencyName = desCurrencyName;
    }

    public String getDesCurrencySymbol() {
        return desCurrencySymbol;
    }

    public void setDesCurrencySymbol(String desCurrencySymbol) {
        this.desCurrencySymbol = desCurrencySymbol;
    }

    public int getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(int transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isTransactionReversed() {
        return transactionReversed;
    }

    public void setTransactionReversed(boolean transactionReversed) {
        this.transactionReversed = transactionReversed;
    }

    public int getSrcMobileNumber() {
        return srcMobileNumber;
    }

    public void setSrcMobileNumber(int srcMobileNumber) {
        this.srcMobileNumber = srcMobileNumber;
    }

    public int getDestMobileNumber() {
        return destMobileNumber;
    }

    public void setDestMobileNumber(int destMobileNumber) {
        this.destMobileNumber = destMobileNumber;
    }

    public boolean isReceiverBearer() {
        return receiverBearer;
    }

    public void setReceiverBearer(boolean receiverBearer) {
        this.receiverBearer = receiverBearer;
    }

    public String getRechargeNumber() {
        return rechargeNumber;
    }

    public void setRechargeNumber(String rechargeNumber) {
        this.rechargeNumber = rechargeNumber;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getTaxTypeName() {
        return taxTypeName;
    }

    public void setTaxTypeName(String taxTypeName) {
        this.taxTypeName = taxTypeName;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


    public int getSrcPreBalance() {
        return srcPreBalance;
    }

    public void setSrcPreBalance(int srcPreBalance) {
        this.srcPreBalance = srcPreBalance;
    }

    public int getDestPreBalance() {
        return destPreBalance;
    }

    public void setDestPreBalance(int destPreBalance) {
        this.destPreBalance = destPreBalance;
    }

    public int getSrcPostBalance() {
        return srcPostBalance;
    }

    public void setSrcPostBalance(int srcPostBalance) {
        this.srcPostBalance = srcPostBalance;
    }

    public int getDestPostBalance() {
        return destPostBalance;
    }

    public void setDestPostBalance(int destPostBalance) {
        this.destPostBalance = destPostBalance;
    }
}
