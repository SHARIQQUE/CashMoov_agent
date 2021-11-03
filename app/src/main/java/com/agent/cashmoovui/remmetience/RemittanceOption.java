package com.agent.cashmoovui.remmetience;

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
import com.agent.cashmoovui.remmetience.cash_to_wallet.CashToWallet;

import java.util.Locale;

public class RemittanceOption extends AppCompatActivity implements View.OnClickListener {
    ImageView imgBack,imgHome;
    MyApplication applicationComponentClass;
    String languageToUse = "";

    LinearLayout ll_receiveRemitance, ll_sendRemitance,ll_cashToWallet;

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

        setContentView(R.layout.activity_remittance_option);
        setBackMenu();

        ll_receiveRemitance = (LinearLayout) findViewById(R.id.ll_receiveRemitance);
        ll_sendRemitance = (LinearLayout) findViewById(R.id.ll_sendRemitance);
        ll_cashToWallet = (LinearLayout) findViewById(R.id.ll_cashToWallet);


        ll_receiveRemitance.setOnClickListener(this);
        ll_sendRemitance.setOnClickListener(this);
        ll_cashToWallet.setOnClickListener(this);

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
            case R.id.ll_receiveRemitance: {

                Intent i = new Intent(RemittanceOption.this, ReiceveRemittance.class);
                startActivity(i);
            }
            break;

            case R.id.ll_sendRemitance:
            {
                Intent i = new Intent(RemittanceOption.this, SendRemittanceOpt.class);
                startActivity(i);
            }
            break;

            case R.id.ll_cashToWallet: {

                Intent i = new Intent(RemittanceOption.this, CashToWallet.class);
                startActivity(i);
            }
            break;


        }
    }
}