package com.agent.cashmoovui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.agent.cashmoovui.LogoutAppCompactActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.login.LoginMsis;
import com.agent.cashmoovui.otp.OtpPage;
import com.suke.widget.SwitchButton;

import org.w3c.dom.Text;

import java.util.Locale;


public class LanguageChoose extends LogoutAppCompactActivity implements View.OnClickListener {


    MyApplication applicationComponentClass;
    String languageToUse = "";

    SwitchButton  switch_button_french,switch_button_english;
    TextView tv_change_language;



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

        setContentView(R.layout.language_choose);



            switch_button_french = (SwitchButton) findViewById(R.id.switch_button_french);
            switch_button_english = (SwitchButton) findViewById(R.id.switch_button_english);
            tv_change_language = (TextView) findViewById(R.id.tv_change_language);


            switch_button_french.setChecked(false);
            switch_button_english.setChecked(false);

            tv_change_language.setOnClickListener(this);




            switch_button_french.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SwitchButton view, boolean isChecked) {

                    try {

                  //  switch_button_french.setChecked(true);
                  //  switch_button_english.setChecked(false);

                    applicationComponentClass.getmSharedPreferences().edit().putString("languageToUse", "fr").commit();
                    applicationComponentClass.getmSharedPreferences().edit().putString("isFirstRun", "YES").commit();

                        Toast.makeText(LanguageChoose.this, "Vous avez choisir la langue française", Toast.LENGTH_LONG).show();

                        Intent i = new Intent(LanguageChoose.this, LoginMsis.class);
                        startActivity(i);
                        finish();

                   }
                catch (Exception e)
                        {
                            e.printStackTrace();
                            Toast.makeText(LanguageChoose.this, e.toString(), Toast.LENGTH_LONG).show();
                        }


                }

            });


            switch_button_english.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SwitchButton view, boolean isChecked) {

                    try {

                       // switch_button_french.setChecked(false);
                       // switch_button_english.setChecked(true);

                        applicationComponentClass.getmSharedPreferences().edit().putString("languageToUse", "en").commit();
                        applicationComponentClass.getmSharedPreferences().edit().putString("isFirstRun", "YES").commit();

                        Toast.makeText(LanguageChoose.this, "You have choose english language", Toast.LENGTH_LONG).show();

                        Intent i = new Intent(LanguageChoose.this, LoginMsis.class);
                        startActivity(i);
                        finish();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        Toast.makeText(LanguageChoose.this, e.toString(), Toast.LENGTH_LONG).show();

                    }


                }

            });


    }


    @Override
    public void onClick(View view) {

      switch (view.getId())
      {
          case R.id.tv_change_language:
          {
              Intent i = new Intent(LanguageChoose.this, LoginMsis.class);
              startActivity(i);
              LanguageChoose.this.finish();
          }
          break;

      }


    }

}
