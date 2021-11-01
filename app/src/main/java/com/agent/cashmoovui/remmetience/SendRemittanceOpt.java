package com.agent.cashmoovui.remmetience;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.remmetience.international.InternationalRemittance;
import com.agent.cashmoovui.remmetience.local.LocalRemittance;

import java.util.Locale;

public class SendRemittanceOpt extends AppCompatActivity implements View.OnClickListener {



    MyApplication applicationComponentClass;
    String languageToUse = "";

    LinearLayout ll_remitence_international,ll_remitence_local;

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

        setContentView(R.layout.activity_send_remittance_opt);


        ll_remitence_international = (LinearLayout) findViewById(R.id.ll_remitence_international);
        ll_remitence_local = (LinearLayout) findViewById(R.id.ll_remitence_local);

        ll_remitence_international.setOnClickListener(this);
        ll_remitence_local.setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ll_remitence_international: {

                Intent i = new Intent(SendRemittanceOpt.this, InternationalRemittance.class);
                startActivity(i);
            }
            break;

            case R.id.ll_remitence_local:
            {
                Intent i = new Intent(SendRemittanceOpt.this, LocalRemittance.class);
                startActivity(i);
            }
            break;




        }
    }
}