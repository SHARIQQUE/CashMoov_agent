package com.agent.cashmoovui.remittancebyabhay;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.activity.OtherOption;
import com.agent.cashmoovui.apiCalls.CommonData;
import com.agent.cashmoovui.internationaltransfer.InTransfer;
import com.agent.cashmoovui.internationaltransfer.OutTransfer;
import com.agent.cashmoovui.remittancebyabhay.international.InternationalRemittanceActivity;
import com.agent.cashmoovui.remittancebyabhay.local.LocalRemittanceActivity;

import java.util.Locale;

public class SendRemittanceOpt extends AppCompatActivity implements View.OnClickListener {
    ImageView imgBack,imgHome;
    MyApplication applicationComponentClass;
    String languageToUse = "";

    LinearLayout ll_remitence_international,ll_remitence_local;
    CardView cardToreceiveinternational,mCardToPartners;
    private long mLastClickTime = 0;

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

        setContentView(R.layout.activity_send_remittance_opt);
        setBackMenu();

        ll_remitence_international = (LinearLayout) findViewById(R.id.ll_remitence_international);
        ll_remitence_local = (LinearLayout) findViewById(R.id.ll_remitence_local);
        cardToreceiveinternational=findViewById(R.id.cardToreceiveinternational);
        mCardToPartners=findViewById(R.id.cardToPartners);
        ll_remitence_international.setOnClickListener(this);
        ll_remitence_local.setOnClickListener(this);
        mCardToPartners.setOnClickListener(this);
        cardToreceiveinternational.setOnClickListener(this);

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
                MyApplication.hideKeyboard(SendRemittanceOpt.this);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(SendRemittanceOpt.this);
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
            case R.id.ll_remitence_international: {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                Intent i = new Intent(SendRemittanceOpt.this, InternationalRemittanceActivity.class);
                MyApplication.saveOrUpdateValueInSharedPreferences(SendRemittanceOpt.this, "International","international");

                startActivity(i);
            }
            break;

            case R.id.ll_remitence_local:
            {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent i = new Intent(SendRemittanceOpt.this, LocalRemittanceActivity.class);
                MyApplication.saveOrUpdateValueInSharedPreferences(SendRemittanceOpt.this, "Local","local");

                startActivity(i);
            }
            break;

            case R.id.cardToreceiveinternational:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                    Intent intent = new Intent(SendRemittanceOpt.this, InTransfer.class);
                    startActivity(intent);

                break;

            case R.id.cardToPartners:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                    Intent intento = new Intent(SendRemittanceOpt.this, OutTransfer.class);
                    startActivity(intento);

                break;
        }
    }
}