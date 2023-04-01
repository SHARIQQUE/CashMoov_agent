package com.agent.cashmoovui.settings;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.agent.cashmoovui.LogoutAppCompactActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.login.LoginMsis;

import org.json.JSONObject;

import java.util.Locale;

public class Logout extends LogoutAppCompactActivity implements View.OnClickListener {
    public static Logout LogoutC;
    Button btnCancel, btnConfirm;
    // ImageView imgBack;

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
            setContentView(R.layout.logout);


            LogoutC = this;
            //setBackMenu();
            getIds();


        } catch (Exception e) {
            Toast.makeText(Logout.this, e.toString(), Toast.LENGTH_LONG).show();

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
        btnConfirm = findViewById(R.id.btnConfirm);

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        btnCancel.setOnClickListener(LogoutC);
        btnConfirm.setOnClickListener(LogoutC);
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
            case R.id.btnConfirm:
                MyApplication.saveBool("FirstLoginCounter",true,MyApplication.getInstance());
                API.POST_REQEST_WH_NEW("ewallet/oauth/logout", null, new Api_Responce_Handler() {
                    @Override
                    public void success(JSONObject jsonObject) {
                        MyApplication.hideLoader();
                        if (jsonObject != null) {

                            if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                // MyApplication.saveString("Locale","",myprofileC);
                                MyApplication.saveBool("FirstLogin",false,LogoutC);
                                MyApplication.saveBool("FirstLoginCounter",true,LogoutC);
                               // MyApplication.saveString("ImageName", "1", LogoutC);



                                applicationComponentClass.getmSharedPreferences().edit().putString("isFirstRun", "YES").commit();

                                Intent i = new Intent(LogoutC, LoginMsis.class);
                                startActivity(i);
                                finish();

                            } else {
                                MyApplication.saveBool("FirstLogin",false,LogoutC);
                                // MyApplication.saveString("ImageName", "1", LogoutC);



                                applicationComponentClass.getmSharedPreferences().edit().putString("isFirstRun", "YES").commit();
                                MyApplication.saveBool("FirstLoginCounter",true,LogoutC);
                                Intent i = new Intent(LogoutC, LoginMsis.class);
                                startActivity(i);
                                finish();
                               // MyApplication.showToast(LogoutC,jsonObject.optString("resultDescription", "N/A"));
                            }
                        }
                    }

                    @Override
                    public void failure(String aFalse) {
                        MyApplication.hideLoader();
                        MyApplication.saveBool("FirstLoginCounter",true,LogoutC);
                        MyApplication.saveBool("FirstLogin",false,LogoutC);
                        // MyApplication.saveString("ImageName", "1", LogoutC);



                        applicationComponentClass.getmSharedPreferences().edit().putString("isFirstRun", "YES").commit();

                        Intent i = new Intent(LogoutC, LoginMsis.class);
                        startActivity(i);
                        finish();
                    }
                });

                break;

        }
    }


}