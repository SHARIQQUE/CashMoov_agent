package com.agent.cashmoovui.settings;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import java.util.Locale;


public class About extends AppCompatActivity {
    public static About aboutC;
    Button btnVisitSite;
    TextView tvUserId;
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
        tvUserId = findViewById(R.id.tvUserId);
        btnVisitSite = findViewById(R.id.btnVisitSite);

        tvUserId.setText(getString(R.string.user_id)+" : "+ MyApplication.getSaveString("userCode", aboutC));

        btnVisitSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openURL();
            }
        });

    }
    public void openURL()  {
        String url="https://www.cashmoov.net";

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        this.startActivity(intent);
    }


    }


