package com.agent.cashmoovui.settings;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;

import java.util.Locale;


public class Confidentiality extends AppCompatActivity implements View.OnClickListener {
    public static Confidentiality confidentialityC;
    Button btnCancel;

    MyApplication applicationComponentClass;
    String languageToUse = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        applicationComponentClass = (MyApplication) getApplicationContext();

        try {

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

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_confidentiality);


            confidentialityC = this;

            // setBackMenu();
            getIds();

        } catch (Exception e) {
            Toast.makeText(Confidentiality.this, e.toString(), Toast.LENGTH_LONG).show();

        }

    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
//
//    private void setBackMenu() {
//        imgBack = findViewById(R.id.imgBack);
//        imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSupportNavigateUp();
//            }
//        });
//    }


    private void getIds() {
        btnCancel = findViewById(R.id.btnCancel);

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        btnCancel.setOnClickListener(confidentialityC);
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btnCancel:
                intent = new Intent(getApplicationContext(), Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

        }
    }

}