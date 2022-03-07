package com.agent.cashmoovui.cashout;

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
import com.agent.cashmoovui.activity.OtherOption;

import java.util.Locale;

public class CashOutOpt extends AppCompatActivity implements View.OnClickListener {

    MyApplication applicationComponentClass;
    String languageToUse = "";
    ImageView imgBack,imgHome;
    LinearLayout ll_cashout, ll_cashout_code;

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

        setContentView(R.layout.activity_cash_out_opt);
        setBackMenu();

        ll_cashout = (LinearLayout) findViewById(R.id.ll_cashout);
        ll_cashout_code = (LinearLayout) findViewById(R.id.ll_cashout_code);


        ll_cashout.setOnClickListener(this);
        ll_cashout_code.setOnClickListener(this);

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
                MyApplication.hideKeyboard(CashOutOpt.this);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(CashOutOpt.this);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_cashout:
                Intent i = new Intent(CashOutOpt.this, CashOutAgent.class);
                startActivity(i);
            break;

            case R.id.ll_cashout_code:
                Intent in = new Intent(CashOutOpt.this, CashOutCodeSubscriber.class);
                startActivity(in);
            break;


        }
    }

}


