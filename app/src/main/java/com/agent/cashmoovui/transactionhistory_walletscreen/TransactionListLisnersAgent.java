package com.agent.cashmoovui.transactionhistory_walletscreen;

public interface TransactionListLisnersAgent {

    void onTransactionViewItemClick(String walletOwnerCode,String registerCountryCode,String transId, String transType, String transDate, String source, String destination, int sourceMsisdn, int destMsisdn, String symbol, int amount, int fee, String taxType, String tax, int postBalance, String status);

}

