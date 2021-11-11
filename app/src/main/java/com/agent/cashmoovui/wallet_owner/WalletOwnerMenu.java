package com.agent.cashmoovui.wallet_owner;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.wallet_owner.agent.AgentKYC;
import com.agent.cashmoovui.wallet_owner.branch.BranchKYC;
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
            languageToUse = "en";
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
                showDialog();
            }
            break;


        }
    }

    public void showDialog() {
        Dialog operationDialog = new Dialog(WalletOwnerMenu.this);
        operationDialog.setContentView(R.layout.dialog_agent_branch);

        Button btnAddAgent,btnAddBranch;
        btnAddAgent = operationDialog.findViewById(R.id.btnAddAgent);
        btnAddBranch = operationDialog.findViewById(R.id.btnAddBranch);

        if(MyApplication.getSaveString("walletOwnerCategoryCode",WalletOwnerMenu.this).equalsIgnoreCase(MyApplication.InstituteCode)){
            btnAddAgent.setVisibility(View.VISIBLE);
            btnAddBranch.setVisibility(View.VISIBLE);
        }
        if(MyApplication.getSaveString("walletOwnerCategoryCode",WalletOwnerMenu.this).equalsIgnoreCase(MyApplication.AgentCode)){
            btnAddAgent.setVisibility(View.GONE);
            btnAddBranch.setVisibility(View.VISIBLE);
        }
        if(MyApplication.getSaveString("walletOwnerCategoryCode",WalletOwnerMenu.this).equalsIgnoreCase(MyApplication.BranchCode)){
            ll_addAgentBranch.setVisibility(View.GONE);
            btnAddAgent.setVisibility(View.GONE);
            btnAddBranch.setVisibility(View.GONE);
        }

        btnAddAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WalletOwnerMenu.this, AgentKYC.class);
                startActivity(i);
                operationDialog.dismiss();
            }
        });
        btnAddBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WalletOwnerMenu.this, BranchKYC.class);
                startActivity(i);
                operationDialog.dismiss();
            }
        });
        //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        operationDialog.show();
    }

}