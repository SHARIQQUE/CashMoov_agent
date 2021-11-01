package com.agent.cashmoovui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.cash_in.CashIn;
import com.agent.cashmoovui.cashout.CashOutAgent;
import com.agent.cashmoovui.cashout.CashOutCodeSubscriber;
import com.agent.cashmoovui.cashout.CashOutOpt;
import com.agent.cashmoovui.login.LoginMsis;
import com.agent.cashmoovui.otp.OtpPage;
import com.agent.cashmoovui.remmetience.ReiceveRemittance;
import com.agent.cashmoovui.remmetience.RemittanceOption;
import com.agent.cashmoovui.remmetience.SendRemittanceInternaional;
import com.agent.cashmoovui.remmetience.SendRemittanceLocal;
import com.agent.cashmoovui.remmetience.SendRemittanceOpt;
import com.agent.cashmoovui.remmetience.cash_to_wallet.CashToWallet;
import com.agent.cashmoovui.set_pin.SetPin;
import com.agent.cashmoovui.splash.SplashScreen;
import com.agent.cashmoovui.transfer.CommissionTransfert;
import com.agent.cashmoovui.transfer.SellFloat;
import com.agent.cashmoovui.transfer.TransferFloat;
import com.agent.cashmoovui.transfer.TransferOption;
import com.agent.cashmoovui.wallet_owner.WalletOwnerMenu;
import com.agent.cashmoovui.wallet_owner.add_agent_branch.AgentKYCAtatched;
import com.agent.cashmoovui.wallet_owner.add_agent_branch.AgentKYCBranch;
import com.agent.cashmoovui.wallet_owner.subscriber.SubscriberKYC;
import com.agent.cashmoovui.wallet_owner.subscriber.SubscriberKYCAtached;
import com.agent.cashmoovui.wallet_owner.wallet_owner.WalletOwner;


public class ListOfActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of);
    }

    public void SplashScreen(View view) {

        startActivity(new Intent(ListOfActivity.this, SplashScreen.class));
    }



    public void PhoneNumberRegistrationScreen(View view) {

        startActivity(new Intent(ListOfActivity.this, LoginMsis.class));
    }

    public void AccountCreatedScreen(View view) {

        startActivity(new Intent(ListOfActivity.this, AccountCreatedScreen.class));
    }


    public void HomeScreen(View view) {

        startActivity(new Intent(ListOfActivity.this, MainActivity.class));
    }



    public void VarifyAccountScreen(View view) {

        startActivity(new Intent(ListOfActivity.this, OtpPage.class));
    }

    public void WalletScreen(View view) {

        startActivity(new Intent(ListOfActivity.this, WalletScreen.class));
    }

    public void About(View view) {

        startActivity(new Intent(ListOfActivity.this, About.class));
    }

    public void Add_contact(View view) {

        startActivity(new Intent(ListOfActivity.this, AddContact.class));
    }


//    public void Change_language(View view) {
//
//        startActivity(new Intent(ListOfActivity.this, Change_language.class));
//    }
//
//    public void Change_pin(View view) {
//
//        startActivity(new Intent(ListOfActivity.this, Change_pin.class));
//    }
//
//    public void Change_profil_img(View view) {
//
//        startActivity(new Intent(ListOfActivity.this, Change_profil_img.class));
//    }

    public void Confidentialite(View view) {

        startActivity(new Intent(ListOfActivity.this, Confidentialite.class));
    }

    public void Notification_screen(View view) {

        startActivity(new Intent(ListOfActivity.this, Notification_screen.class));
    }


    public void Profil(View view) {

        startActivity(new Intent(ListOfActivity.this, Profil.class));
    }

    public void registerstepone(View view) {

        startActivity(new Intent(ListOfActivity.this, SubscriberKYC.class));
    }

    public void registersteptwo(View view) {

        startActivity(new Intent(ListOfActivity.this, SubscriberKYCAtached.class));
    }

    public void Reset(View view) {

        startActivity(new Intent(ListOfActivity.this, Reset.class));
    }

    public void Show_profil_qr(View view) {

        startActivity(new Intent(ListOfActivity.this, ShowProfileQr.class));
    }

    public void Showpopup(View view) {

        startActivity(new Intent(ListOfActivity.this, Showpopup.class));
    }

    public void DetailAbonne(View view) {

        startActivity(new Intent(ListOfActivity.this, WalletOwner.class));
    }

    public void SendRemittance(View view) {

        startActivity(new Intent(ListOfActivity.this, SendRemittanceLocal.class));
    }

    public void TransferFloat(View view) {

        startActivity(new Intent(ListOfActivity.this, TransferFloat.class));
    }
    public void TransferOption(View view) {

        startActivity(new Intent(ListOfActivity.this, TransferOption.class));
    }

    public void RemittanceOption(View view) {

        startActivity(new Intent(ListOfActivity.this, RemittanceOption.class));
    }

    public void SellFloat(View view) {

        startActivity(new Intent(ListOfActivity.this, SellFloat.class));
    }

    public void OverdraftLimit(View view) {

        startActivity(new Intent(ListOfActivity.this, OverdraftLimit.class));
    }
    public void HistoriqueTransaction(View view) {

        startActivity(new Intent(ListOfActivity.this, HistoriqueTransaction.class));
    }


    public void cashin(View view) {

        startActivity(new Intent(ListOfActivity.this, CashIn.class));
    }

    public void Elements(View view) {

        startActivity(new Intent(ListOfActivity.this, Elements.class));
    }
    public void OwnerOption(View view) {

        startActivity(new Intent(ListOfActivity.this, WalletOwnerMenu.class));
    }

    public void SettingCurrency(View view) {

        startActivity(new Intent(ListOfActivity.this, SettingCurrency.class));
    }

    public void OtpScreen(View view) {

        startActivity(new Intent(ListOfActivity.this, OtpPage.class));
    }

    public void DetailTransaction(View view) {

        startActivity(new Intent(ListOfActivity.this, DetailTransaction.class));
    }

    public void HistoriqueAgent(View view) {

        startActivity(new Intent(ListOfActivity.this, HistoriqueAgent.class));
    }

    public void HistoriqueBranch(View view) {

        startActivity(new Intent(ListOfActivity.this, HistoriqueBranch.class));
    }

    public void CashOutOpt(View view) {

        startActivity(new Intent(ListOfActivity.this, CashOutOpt.class));
    }

    public void cashOutCode(View view) {

        startActivity(new Intent(ListOfActivity.this, CashOutCodeSubscriber.class));
    }

    public void SendRemittanceOpt(View view) {

        startActivity(new Intent(ListOfActivity.this, SendRemittanceOpt.class));
    }

    public void RemittanceInternational(View view) {

        startActivity(new Intent(ListOfActivity.this, SendRemittanceInternaional.class));
    }

    public void CashOut(View view) {

        startActivity(new Intent(ListOfActivity.this, CashOutAgent.class));
    }

    public void ReiceveRemittance(View view) {

        startActivity(new Intent(ListOfActivity.this, ReiceveRemittance.class));
    }

    public void RegisterAgentOrBranch(View view) {

        startActivity(new Intent(ListOfActivity.this, AgentKYCBranch.class));
    }

    public void RegisterAgentOrBranchDoc(View view) {

        startActivity(new Intent(ListOfActivity.this, AgentKYCAtatched.class));
    }
    public void CommissionTransfert(View view) {

        startActivity(new Intent(ListOfActivity.this, CommissionTransfert.class));
    }
    public void Paiements(View view) {

        startActivity(new Intent(ListOfActivity.this, Paiements.class));
    }
    public void PaiementTvStarTime(View view) {

        startActivity(new Intent(ListOfActivity.this, PaiementTvStarTime.class));
    }

    public void MobilePrepaid(View view) {

        startActivity(new Intent(ListOfActivity.this, MobilePrepaid.class));
    }

    public void FraisDeServices(View view) {

        startActivity(new Intent(ListOfActivity.this, FraisDeServices.class));
    }
    public void SetPin(View view) {

        startActivity(new Intent(ListOfActivity.this, SetPin.class));
    }

    public void ListUsers(View view) {

        startActivity(new Intent(ListOfActivity.this, ListUsers.class));
    }

    public void CashToWallet(View view) {

        startActivity(new Intent(ListOfActivity.this, CashToWallet.class));
    }


}