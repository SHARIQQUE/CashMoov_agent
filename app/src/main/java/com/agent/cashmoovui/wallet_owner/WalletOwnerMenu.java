package com.agent.cashmoovui.wallet_owner;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.transfer.CommissionTransfert;
import com.agent.cashmoovui.transfer.SellFloat;
import com.agent.cashmoovui.transfer.TransferFloat;
import com.agent.cashmoovui.transfer.TransferOption;
import com.agent.cashmoovui.wallet_owner.add_agent_branch.AgentKYCBranch;
import com.agent.cashmoovui.wallet_owner.subscriber.SubscriberKYC;
import com.agent.cashmoovui.wallet_owner.wallet_owner.WalletOwner;

import java.util.Locale;

// activity_owner_option

public class WalletOwnerMenu extends  AppCompatActivity implements View.OnClickListener {
    ImageView imgBack,imgHome;
    MyApplication applicationComponentClass;
    String languageToUse = "";

    LinearLayout ll_walletowner,ll_subscriber,ll_addAgentBranch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        applicationComponentClass = (MyApplication) getApplicationContext();


        languageToUse = applicationComponentClass.getmSharedPreferences().getString("languageToUse", "");

        if (languageToUse.trim().length() == 0) {
            languageToUse = "fr";
        }

        Locale locale = new Locale(languageToUse);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.activity_owner_option);

        setBackMenu();

        ll_walletowner = (LinearLayout) findViewById(R.id.ll_walletowner);
        ll_subscriber = (LinearLayout) findViewById(R.id.ll_subscriber);
        ll_addAgentBranch = (LinearLayout) findViewById(R.id.ll_addAgentBranch);


        ll_walletowner.setOnClickListener(this);
        ll_subscriber.setOnClickListener(this);
        ll_addAgentBranch.setOnClickListener(this);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setBackMenu() {
        imgBack = findViewById(R.id.imgBack);
        imgHome = findViewById(R.id.imgHome);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ll_walletowner: {

                Intent i = new Intent(WalletOwnerMenu.this, WalletOwner.class);
                startActivity(i);
            }
            break;

            case R.id.ll_subscriber:
            {
                Intent i = new Intent(WalletOwnerMenu.this, SubscriberKYC.class);
                startActivity(i);
            }
            break;

            case R.id.ll_addAgentBranch:
            {
                Intent i = new Intent(WalletOwnerMenu.this, AgentKYCBranch.class);
                startActivity(i);
            }
            break;


        }
    }
}