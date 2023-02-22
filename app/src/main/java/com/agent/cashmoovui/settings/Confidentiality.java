package com.agent.cashmoovui.settings;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.suke.widget.SwitchButton;

import java.util.Locale;


public class Confidentiality extends AppCompatActivity implements View.OnClickListener {
    public static Confidentiality confidentialityC;
    Button btnCancel;
    SwitchButton btnSwich;
    MyApplication applicationComponentClass;
    String languageToUse = "";
    private long mLastClickTime = 0;

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

        btnSwich = findViewById(R.id.btnSwich);

        MyApplication.setProtection = MyApplication.getSaveString("ACTIVATEPROTECTION", confidentialityC);
        if(MyApplication.setProtection!=null && !MyApplication.setProtection.isEmpty()) {
            if (MyApplication.setProtection.equalsIgnoreCase("Activate")) {
                btnSwich.setChecked(true);
            }else {
                btnSwich.setChecked(false);
            }
        }else{
            btnSwich.setChecked(true);
        }


        btnSwich.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    MyApplication.saveString("ACTIVATEPROTECTION", "Activate", confidentialityC);
                    MyApplication.setProtection = MyApplication.getSaveString("ACTIVATEPROTECTION", confidentialityC);
                } else {
                    MyApplication.saveString("ACTIVATEPROTECTION", "Deactivate", confidentialityC);
                    MyApplication.setProtection = MyApplication.getSaveString("ACTIVATEPROTECTION", confidentialityC);

                }
            }
        });

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
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                intent = new Intent(getApplicationContext(), Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

        }
    }

}