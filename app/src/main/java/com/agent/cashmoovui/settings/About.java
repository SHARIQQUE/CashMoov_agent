package com.agent.cashmoovui.settings;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;

import java.util.Locale;


public class About extends AppCompatActivity implements View.OnClickListener {

    Button buttonOk;

    @Override
    public void onClick(View view) {


        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cashmoov.net/"));
        startActivity(browserIntent);

    }

    public static About aboutC;

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
            setContentView(R.layout.activity_about);

            buttonOk = (Button)findViewById(R.id.buttonOk);
            buttonOk.setOnClickListener(this);





            aboutC = this;
            //setBackMenu();
            getIds();
        }

        catch (Exception e)
    {
        Toast.makeText(About.this, e.toString(), Toast.LENGTH_LONG).show();

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
        // tvSend = findViewById(R.id.tvSend);


    }

}