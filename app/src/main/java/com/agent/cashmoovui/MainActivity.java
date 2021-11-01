package com.agent.cashmoovui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.agent.cashmoovui.activity.FraisDeServices;
import com.agent.cashmoovui.activity.MobilePrepaid;
import com.agent.cashmoovui.activity.OverdraftLimit;
import com.agent.cashmoovui.activity.Paiements;
import com.agent.cashmoovui.activity.WalletScreen;
import com.agent.cashmoovui.cash_in.CashIn;
import com.agent.cashmoovui.cashout.CashOutOpt;
import com.agent.cashmoovui.remmetience.RemittanceOption;
import com.agent.cashmoovui.settings.Profile;
import com.agent.cashmoovui.transfer.TransferOption;
import com.agent.cashmoovui.wallet_owner.WalletOwnerMenu;

import java.util.Locale;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    LinearLayout ll_cashIn,ll_cashout,ll_remitence,ll_payment,ll_walletowner,ll_transfer,ll_credit,ll_overdraft,ll_serviceCharge;
    SmoothBottomBar bottomBar;

    MyApplication applicationComponentClass;
    String languageToUse = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

        setContentView(R.layout.activity_main);


        String firstRunApp = applicationComponentClass.getmSharedPreferences().getString("isFirstRun", "");

        if (firstRunApp.trim().length() == 0) {
            applicationComponentClass.getmSharedPreferences().edit().putString("isFirstRun", "NO").commit();
        }
        else if (firstRunApp.equalsIgnoreCase("YES"))
        {
            applicationComponentClass.getmSharedPreferences().edit().putString("isFirstRun", "NO").commit();
        }
        else if (firstRunApp.equalsIgnoreCase("NO_LOGINPIN"))
        {
            applicationComponentClass.getmSharedPreferences().edit().putString("isFirstRun", "NO_LOGINPIN").commit();
        }
        else {
            applicationComponentClass.getmSharedPreferences().edit().putString("isFirstRun", "NO").commit();
        }



        ll_cashIn = (LinearLayout) findViewById(R.id.ll_cashIn);
        ll_cashIn.setOnClickListener(this);


        ll_cashout = (LinearLayout) findViewById(R.id.ll_cashout);
        ll_cashout.setOnClickListener(this);

        ll_remitence= (LinearLayout) findViewById(R.id.ll_remitence);
        ll_remitence.setOnClickListener(this);

        ll_payment= (LinearLayout) findViewById(R.id.ll_payment);
        ll_payment.setOnClickListener(this);

        ll_transfer= (LinearLayout) findViewById(R.id.ll_transfer);
        ll_transfer.setOnClickListener(this);

        ll_credit= (LinearLayout) findViewById(R.id.ll_credit);
        ll_credit.setOnClickListener(this);

        ll_overdraft= (LinearLayout) findViewById(R.id.ll_overdraft);
        ll_overdraft.setOnClickListener(this);


        ll_serviceCharge= (LinearLayout) findViewById(R.id.ll_serviceCharge);
        ll_serviceCharge.setOnClickListener(this);

        ll_walletowner= (LinearLayout) findViewById(R.id.ll_walletowner);
        ll_walletowner.setOnClickListener(this);


        bottomBar = findViewById(R.id.bottomBar);

        bottomBar.setItemActiveIndex(0);
        bottomBar.setBarIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));


        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int bottomId) {
                Log.e("PositionMain--",""+bottomId);

                if (bottomId == 0) {
//                    Intent i = new Intent(mainC, MainActivity.class);
//                    startActivity(i);
//                    finish();
                }
                if (bottomId == 1) {

                    Intent i = new Intent(MainActivity.this, WalletScreen.class);
                    startActivity(i);
                    // finish();
                }
                if (bottomId == 2) {
                    Intent i = new Intent(MainActivity.this, Profile.class);
                    startActivity(i);
                    // finish();
                }
                return false;
            }
        });


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_cashIn: {

                Intent i = new Intent(MainActivity.this, CashIn.class);
                startActivity(i);

            }
            break;

            case R.id.ll_cashout: {

                Intent i = new Intent(MainActivity.this, CashOutOpt.class);
                startActivity(i);
                // finish();
                }
            break;

            case R.id.ll_remitence: {

                Intent i = new Intent(MainActivity.this, RemittanceOption.class);
                startActivity(i);

            }
            break;

            case R.id.ll_payment: {

                Intent i = new Intent(MainActivity.this, Paiements.class);
                startActivity(i);
            }
            break;

            case R.id.ll_walletowner:
            {

                Intent i = new Intent(MainActivity.this, WalletOwnerMenu.class);
                startActivity(i);

            }
            break;

            case R.id.ll_transfer:
                {

                    Intent i = new Intent(MainActivity.this, TransferOption.class);
                    startActivity(i);

                }
            break;

            case R.id.ll_credit:
            {

                Intent i = new Intent(MainActivity.this, MobilePrepaid.class);
                startActivity(i);

            }
            break;

            case R.id.ll_overdraft:
            {

                Intent i = new Intent(MainActivity.this, OverdraftLimit.class);
                startActivity(i);
            }

            break;

            case R.id.ll_serviceCharge:
            {

                Intent i = new Intent(MainActivity.this, FraisDeServices.class);
                startActivity(i);
            }
            break;






        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();


        finish();
    }

}
