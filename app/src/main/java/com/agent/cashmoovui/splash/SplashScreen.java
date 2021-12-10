package com.agent.cashmoovui.splash;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.activity.LanguageChoose;
import com.agent.cashmoovui.login.LoginMsis;
import com.agent.cashmoovui.login.LoginPin;
import com.agent.cashmoovui.model.CurrencyModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SplashScreen extends AppCompatActivity implements View.OnClickListener  {

    MyApplication applicationComponentClass;
    String languageToUse = "";
    TextView startnow_textview;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        applicationComponentClass = (MyApplication) getApplicationContext();

        languageToUse = applicationComponentClass.getmSharedPreferences().getString("languageToUse", "");

        String languageToUse = applicationComponentClass.getmSharedPreferences().getString("languageToUse", "");
        if (languageToUse.trim().length() == 0) {
            languageToUse = "en";
        }


        Locale locale = new Locale(languageToUse);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());




        setContentView(R.layout.activity_spash_screen);




        startnow_textview = (TextView)findViewById(R.id.startnow_textview);
        startnow_textview.setOnClickListener(this);


        String firstRunApp = applicationComponentClass.getmSharedPreferences().getString("isFirstRun", "");



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {


                        String firstRunApp = applicationComponentClass.getmSharedPreferences().getString("isFirstRun", "");

                        if (firstRunApp.trim().length() == 0) {
                            startnow_textview.setVisibility(View.GONE);
                            loginPage();
                        }

                        else if (firstRunApp.equalsIgnoreCase("YES")) {
                            startnow_textview.setVisibility(View.GONE);
                            loginPage();
                        }

                        else if (firstRunApp.equalsIgnoreCase("NO")) {
                            startnow_textview.setVisibility(View.GONE);
                            loginPage();
                        }

                        else if (firstRunApp.equalsIgnoreCase("NO_LOGINPIN"))
                        {
                            startnow_textview.setVisibility(View.GONE);
                            loginPage();
                        }
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(SplashScreen.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT)
                                .show();
                    }


                };

                TedPermission.with(SplashScreen.this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission, you can not use this service.\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setGotoSettingButtonText("Go to settings")
                        .setPermissions(CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE,ACCESS_FINE_LOCATION)
                        .check();

                // This method will be executed once the timer is over

            }
        }, 2000);



    }



    void mainActivity()
    {
        Handler handler;

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {



                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                SplashScreen.this.finish();
            }
        },1000);

    }

    void loginPage()
    {
        Handler handler;

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(SplashScreen.this, LoginMsis.class);
                startActivity(i);

               SplashScreen.this.finish();
            }
        },1000);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {

            case R.id.startnow_textview:
            {

                applicationComponentClass.getmSharedPreferences().edit().putString("languageToUse", "en").commit();
                applicationComponentClass.getmSharedPreferences().edit().putString("isFirstRun", "YES").commit();

                Intent i = new Intent(SplashScreen.this, LoginMsis.class);
                startActivity(i);
                finish();


            }
            break;

        }
    }


    private boolean checkPermission_camera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission_read_external_storage() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
    }

    private boolean checkPermission_read_external_storage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }
    private void requestPermission_camera() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
    }



}
