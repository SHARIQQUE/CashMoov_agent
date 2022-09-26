package com.agent.cashmoovui.model;

public class MiniStatementTrans {
    private int id;
    private String code;
    private String transactionId;
    private String fromWalletOwnerCode;
    private String toWalletOwnerCode;
    private String fromWalletOwnerName;
    private String toWalletOwnerName;
    private String fromWalletOwnerMsisdn;
    private String toWalletOwnerMsisdn;
    private String fromWalletCode;
    private String fromWalletName;
    private String fromCurrencyCode;
    private String toCurrencyCode;
    private String fromCurrencyName;
    private String toCurrencyName;
    private String fromCurrencySymbol;
    private String toCurrencySymbol;
    private String transactionTypeCode;
    private String transactionTypeName;
    private String creationDate;
    private String comReceiveWalletCode;
    private String taxAsJson;
    private String holdingAccountCode;
    private String status;
    private double fee;
    private double fromAmount;
    private double toAmount;
    private double comReceiveAmount;
    private double srcPostBalance;
    private double srcPreviousBalance;
    private double destPreviousBalance;
    private double destPostBalance;
    private double commissionAmountForInstitute;
    private double commissionAmountForAgent;
    private double commissionAmountForBranch;
    private double commissionAmountForMerchant;
    private double commissionAmountForOutlet;
    private double transactionAmount;
    private double principalAmount;
    private String fromWalletOwnerSurname;
    private String fromWalletTypeCode;
    private boolean isReverse;
    private boolean bearerSender;


    public MiniStatementTrans(int id, String code, String transactionId, String fromWalletOwnerCode, String toWalletOwnerCode, String fromWalletOwnerName, String toWalletOwnerName, String fromWalletOwnerMsisdn, String toWalletOwnerMsisdn, String fromWalletCode, String fromWalletName, String fromCurrencyCode, String toCurrencyCode, String fromCurrencyName, String toCurrencyName, String fromCurrencySymbol, String toCurrencySymbol, String transactionTypeCode, String transactionTypeName, String creationDate, String comReceiveWalletCode, String taxAsJson, String holdingAccountCode, String status, double fromAmount, double toAmount, double comReceiveAmount, double srcPostBalance, double srcPreviousBalance, double destPreviousBalance, double destPostBalance, double commissionAmountForInstitute, double commissionAmountForAgent, double commissionAmountForBranch, double commissionAmountForMerchant, double commissionAmountForOutlet, double transactionAmount, double principalAmount, String fromWalletOwnerSurname, String fromWalletTypeCode,
                              boolean isReverse,double fee,boolean bearerSender) {
        this.id = id;
        this.code = code;
        this.transactionId = transactionId;
        this.fromWalletOwnerCode = fromWalletOwnerCode;
        this.toWalletOwnerCode = toWalletOwnerCode;
        this.fromWalletOwnerName = fromWalletOwnerName;
        this.toWalletOwnerName = toWalletOwnerName;
        this.fromWalletOwnerMsisdn = fromWalletOwnerMsisdn;
        this.toWalletOwnerMsisdn = toWalletOwnerMsisdn;
        this.fromWalletCode = fromWalletCode;
        this.fromWalletName = fromWalletName;
        this.fromCurrencyCode = fromCurrencyCode;
        this.toCurrencyCode = toCurrencyCode;
        this.fromCurrencyName = fromCurrencyName;
        this.toCurrencyName = toCurrencyName;
        this.fromCurrencySymbol = fromCurrencySymbol;
        this.toCurrencySymbol = toCurrencySymbol;
        this.transactionTypeCode = transactionTypeCode;
        this.transactionTypeName = transactionTypeName;
        this.creationDate = creationDate;
        this.comReceiveWalletCode = comReceiveWalletCode;
        this.taxAsJson = taxAsJson;
        this.holdingAccountCode = holdingAccountCode;
        this.status = status;
        this.fromAmount = fromAmount;
        this.toAmount = toAmount;
        this.comReceiveAmount = comReceiveAmount;
        this.srcPostBalance = srcPostBalance;
        this.srcPreviousBalance = srcPreviousBalance;
        this.destPreviousBalance = destPreviousBalance;
        this.destPostBalance = destPostBalance;
        this.commissionAmountForInstitute = commissionAmountForInstitute;
        this.commissionAmountForAgent = commissionAmountForAgent;
        this.commissionAmountForBranch = commissionAmountForBranch;
        this.commissionAmountForMerchant = commissionAmountForMerchant;
        this.commissionAmountForOutlet = commissionAmountForOutlet;
        this.transactionAmount = transactionAmount;
        this.principalAmount = principalAmount;
        this.fromWalletOwnerSurname = fromWalletOwnerSurname;
        this.fromWalletTypeCode = fromWalletTypeCode;
        this.isReverse = isReverse;
        this.fee=fee;
        this.bearerSender=bearerSender;

    }

    public boolean isBearerSender() {
        return bearerSender;
    }

    public void setBearerSender(boolean bearerSender) {
        this.bearerSender = bearerSender;
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

    public String getFromWalletOwnerCode() {
        return fromWalletOwnerCode;
    }

    public void setFromWalletOwnerCode(String fromWalletOwnerCode) {
        this.fromWalletOwnerCode = fromWalletOwnerCode;
    }

    public String getToWalletOwnerCode() {
        return toWalletOwnerCode;
    }

    public void setToWalletOwnerCode(String toWalletOwnerCode) {
        this.toWalletOwnerCode = toWalletOwnerCode;
    }

    public String getFromWalletOwnerName() {
        return fromWalletOwnerName;
    }

    public void setFromWalletOwnerName(String fromWalletOwnerName) {
        this.fromWalletOwnerName = fromWalletOwnerName;
    }

    public String getToWalletOwnerName() {
        return toWalletOwnerName;
    }

    public void setToWalletOwnerName(String toWalletOwnerName) {
        this.toWalletOwnerName = toWalletOwnerName;
    }

    public String getFromWalletOwnerMsisdn() {
        return fromWalletOwnerMsisdn;
    }

    public void setFromWalletOwnerMsisdn(String fromWalletOwnerMsisdn) {
        this.fromWalletOwnerMsisdn = fromWalletOwnerMsisdn;
    }

    public String getToWalletOwnerMsisdn() {
        return toWalletOwnerMsisdn;
    }

    public void setToWalletOwnerMsisdn(String toWalletOwnerMsisdn) {
        this.toWalletOwnerMsisdn = toWalletOwnerMsisdn;
    }

    public String getFromWalletCode() {
        return fromWalletCode;
    }

    public void setFromWalletCode(String fromWalletCode) {
        this.fromWalletCode = fromWalletCode;
    }

    public String getFromWalletName() {
        return fromWalletName;
    }

    public void setFromWalletName(String fromWalletName) {
        this.fromWalletName = fromWalletName;
    }

    public String getFromCurrencyCode() {
        return fromCurrencyCode;
    }

    public void setFromCurrencyCode(String fromCurrencyCode) {
        this.fromCurrencyCode = fromCurrencyCode;
    }

    public String getToCurrencyCode() {
        return toCurrencyCode;
    }

    public void setToCurrencyCode(String toCurrencyCode) {
        this.toCurrencyCode = toCurrencyCode;
    }

    public String getFromCurrencyName() {
        return fromCurrencyName;
    }

    public void setFromCurrencyName(String fromCurrencyName) {
        this.fromCurrencyName = fromCurrencyName;
    }

    public String getToCurrencyName() {
        return toCurrencyName;
    }

    public void setToCurrencyName(String toCurrencyName) {
        this.toCurrencyName = toCurrencyName;
    }

    public String getFromCurrencySymbol() {
        return fromCurrencySymbol;
    }

    public void setFromCurrencySymbol(String fromCurrencySymbol) {
        this.fromCurrencySymbol = fromCurrencySymbol;
    }

    public String getToCurrencySymbol() {
        return toCurrencySymbol;
    }

    public void setToCurrencySymbol(String toCurrencySymbol) {
        this.toCurrencySymbol = toCurrencySymbol;
    }

    public String getTransactionTypeCode() {
        return transactionTypeCode;
    }

    public void setTransactionTypeCode(String transactionTypeCode) {
        this.transactionTypeCode = transactionTypeCode;
    }

    public String getTransactionTypeName() {
        return transactionTypeName;
    }

    public void setTransactionTypeName(String transactionTypeName) {
        this.transactionTypeName = transactionTypeName;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getComReceiveWalletCode() {
        return comReceiveWalletCode;
    }

    public void setComReceiveWalletCode(String comReceiveWalletCode) {
        this.comReceiveWalletCode = comReceiveWalletCode;
    }

    public String getTaxAsJson() {
        return taxAsJson;
    }

    public void setTaxAsJson(String taxAsJson) {
        this.taxAsJson = taxAsJson;
    }

    public String getHoldingAccountCode() {
        return holdingAccountCode;
    }

    public void setHoldingAccountCode(String holdingAccountCode) {
        this.holdingAccountCode = holdingAccountCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(double fromAmount) {
        this.fromAmount = fromAmount;
    }

    public double getToAmount() {
        return toAmount;
    }

    public void setToAmount(double toAmount) {
        this.toAmount = toAmount;
    }

    public double getComReceiveAmount() {
        return comReceiveAmount;
    }

    public void setComReceiveAmount(double comReceiveAmount) {
        this.comReceiveAmount = comReceiveAmount;
    }

    public double getSrcPostBalance() {
        return srcPostBalance;
    }

    public void setSrcPostBalance(double srcPostBalance) {
        this.srcPostBalance = srcPostBalance;
    }

    public double getSrcPreviousBalance() {
        return srcPreviousBalance;
    }

    public void setSrcPreviousBalance(double srcPreviousBalance) {
        this.srcPreviousBalance = srcPreviousBalance;
    }

    public double getDestPreviousBalance() {
        return destPreviousBalance;
    }

    public void setDestPreviousBalance(double destPreviousBalance) {
        this.destPreviousBalance = destPreviousBalance;
    }

    public double getDestPostBalance() {
        return destPostBalance;
    }

    public void setDestPostBalance(double destPostBalance) {
        this.destPostBalance = destPostBalance;
    }

    public double getCommissionAmountForInstitute() {
        return commissionAmountForInstitute;
    }

    public void setCommissionAmountForInstitute(double commissionAmountForInstitute) {
        this.commissionAmountForInstitute = commissionAmountForInstitute;
    }

    public double getCommissionAmountForAgent() {
        return commissionAmountForAgent;
    }

    public void setCommissionAmountForAgent(double commissionAmountForAgent) {
        this.commissionAmountForAgent = commissionAmountForAgent;
    }

    public double getCommissionAmountForBranch() {
        return commissionAmountForBranch;
    }

    public void setCommissionAmountForBranch(double commissionAmountForBranch) {
        this.commissionAmountForBranch = commissionAmountForBranch;
    }

    public double getCommissionAmountForMerchant() {
        return commissionAmountForMerchant;
    }

    public void setCommissionAmountForMerchant(double commissionAmountForMerchant) {
        this.commissionAmountForMerchant = commissionAmountForMerchant;
    }

    public double getCommissionAmountForOutlet() {
        return commissionAmountForOutlet;
    }

    public void setCommissionAmountForOutlet(double commissionAmountForOutlet) {
        this.commissionAmountForOutlet = commissionAmountForOutlet;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public double getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(double principalAmount) {
        this.principalAmount = principalAmount;
    }

    public String getFromWalletOwnerSurname() {
        return fromWalletOwnerSurname;
    }

    public void setFromWalletOwnerSurname(String fromWalletOwnerSurname) {
        this.fromWalletOwnerSurname = fromWalletOwnerSurname;
    }

    public String getFromWalletTypeCode() {
        return fromWalletTypeCode;
    }

    public void setFromWalletTypeCode(String fromWalletTypeCode) {
        this.fromWalletTypeCode = fromWalletTypeCode;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public boolean isReverse() {
        return isReverse;
    }

    public void setReverse(boolean reverse) {
        isReverse = reverse;
    }
}
