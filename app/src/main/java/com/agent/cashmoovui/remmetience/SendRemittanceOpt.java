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
import com.agent.cashmoovui.remittancebyabhay.InternationalRemittanceActivity;
import com.agent.cashmoovui.remittancebyabhay.LocalRemittanceActivity;

import java.util.Locale;

public class SendRemittanceOpt extends AppCompatActivity implements View.OnClickListener {
    ImageView imgBack,imgHome;
    MyApplication applicationComponentClass;
    String languageToUse = "";

    LinearLayout ll_remitence_international,ll_remitence_local;

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

        ll_remitence_international.setOnClickListener(this);
        ll_remitence_local.setOnClickListener(this);

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
            case R.id.ll_remitence_international: {

                Intent i = new Intent(SendRemittanceOpt.this, InternationalRemittanceActivity.class);
                startActivity(i);
            }
            break;

            case R.id.ll_remitence_local:
            {
                Intent i = new Intent(SendRemittanceOpt.this, LocalRemittanceActivity.class);
                startActivity(i);
            }
            break;




        }
    }
}