package com.agent.cashmoovui.listeners;

public interface AgentMiniStatemetListners {
    void onAgentMiniStatementListItemClick(String transactionTypeName, String fromWalletOwnerName, String walletOwnerMssdn, String currencySymbol, double fromAmount, String transactionId, String creationDate, String status);

}
