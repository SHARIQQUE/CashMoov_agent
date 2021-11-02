package com.agent.cashmoovui.transfer;

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

import java.util.Locale;


public class TransferOption extends AppCompatActivity implements View.OnClickListener {
        ImageView imgBack,imgHome;
        MyApplication applicationComponentClass;
        String languageToUse = "";

        LinearLayout ll_sellFloat,ll_transferFloat,ll_commissionTransfer;

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

            setContentView(R.layout.activity_transfer_option);
            setBackMenu();

            ll_sellFloat = (LinearLayout) findViewById(R.id.ll_sellFloat);
            ll_transferFloat = (LinearLayout) findViewById(R.id.ll_transferFloat);
            ll_commissionTransfer = (LinearLayout) findViewById(R.id.ll_commissionTransfer);


            ll_sellFloat.setOnClickListener(this);
            ll_transferFloat.setOnClickListener(this);
            ll_commissionTransfer.setOnClickListener(this);

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
            case R.id.ll_sellFloat: {

                Intent i = new Intent(TransferOption.this, SellFloat.class);
                startActivity(i);
            }
            break;

            case R.id.ll_transferFloat:
            {
                Intent i = new Intent(TransferOption.this, TransferFloat.class);
                startActivity(i);
            }
            break;

            case R.id.ll_commissionTransfer:
            {
                Intent i = new Intent(TransferOption.this, CommissionTransfert.class);
                startActivity(i);
            }
            break;


        }
    }
}