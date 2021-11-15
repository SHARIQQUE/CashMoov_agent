package com.agent.cashmoovui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.cash_in.CashIn;
import com.agent.cashmoovui.cashout.CashOutCodeSubscriber;
import com.agent.cashmoovui.cashout.CashOutOpt;
import com.agent.cashmoovui.login.LoginMsis;
import com.agent.cashmoovui.otp.OtpPage;
import com.agent.cashmoovui.overdraft.OverdraftLimit;
import com.agent.cashmoovui.payments.PaymentDetails;
import com.agent.cashmoovui.payments.Payments;
import com.agent.cashmoovui.remmetience.RemittanceOption;
import com.agent.cashmoovui.remmetience.RemittanceReceive;
import com.agent.cashmoovui.remmetience.SendRemittanceInternaional;
import com.agent.cashmoovui.remmetience.SendRemittanceLocal;
import com.agent.cashmoovui.remmetience.SendRemittanceOpt;
import com.agent.cashmoovui.remmetience.cash_to_wallet.CashToWallet;
import com.agent.cashmoovui.set_pin.SetPin;
import com.agent.cashmoovui.splash.SplashScreen;
import com.agent.cashmoovui.transactionhistory_walletscreen.WalletScreen;
import com.agent.cashmoovui.transfer_float.CommissionTransfer;
import com.agent.cashmoovui.transfer_float.SellFloat;
import com.agent.cashmoovui.transfer_float.TransferFloats;
import com.agent.cashmoovui.transfer_float.TransferOption;
import com.agent.cashmoovui.wallet_owner.WalletOwnerMenu;
import com.agent.cashmoovui.wallet_owner.agent.AgentKYCAttached;
import com.agent.cashmoovui.wallet_owner.agent.AgentKYC;
import com.agent.cashmoovui.wallet_owner.subscriber.SubscriberKYC;
import com.agent.cashmoovui.wallet_owner.subscriber.SubscriberKYCAttached;
import com.agent.cashmoovui.wallet_owner.wallet_owner.WalletOwner;


public class ListOfActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of);
    }



}