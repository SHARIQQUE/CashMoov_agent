package com.agent.cashmoovui.splash;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.activity.LanguageChoose;
import com.agent.cashmoovui.login.LoginMsis;
import com.agent.cashmoovui.login.LoginPin;

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
}
