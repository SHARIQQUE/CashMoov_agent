package com.agent.cashmoovui.listeners;

public interface MiniStatemetListners {
    void onMiniStatementListItemClick(String transactionTypeName, String fromWalletOwnerName,String toWalletOwnerName, String walletOwnerMssdn, String currencySymbol, double fromAmount, String transactionId, String creationDate, String status, double commissionAmount);

}
