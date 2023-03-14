package com.agent.cashmoovui.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.agent.cashmoovui.LogoutAppCompactActivity;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.google.android.material.radiobutton.MaterialRadioButton;

import java.util.Locale;

;

public class ChangeLanguage extends LogoutAppCompactActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    public static ChangeLanguage changelanguageC;
    MaterialRadioButton sbFrench, sbEnglish;
    TextView tvChange,languageText;


    MyApplication applicationComponentClass;
    String languageToUse = "";
    ImageView imgBack,imgHome;

    // ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
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

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_change_language);


            changelanguageC = this;
            setBackMenu();
            getIds();

    }

        catch (Exception e)
    {
        Toast.makeText(ChangeLanguage.this, e.toString(), Toast.LENGTH_LONG).show();

    }

}

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
//
   private void setBackMenu() {
        imgBack = findViewById(R.id.imgBack);
       imgHome = findViewById(R.id.imgHome);

       imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(ChangeLanguage.this);

                onSupportNavigateUp();
           }
       });

       imgHome.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               MyApplication.hideKeyboard(ChangeLanguage.this);
               Intent intent = new Intent(getApplicationContext(), MainActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent);
           }
       });

   }


    private void getIds() {
        sbFrench = findViewById(R.id.sbFrench);
        sbEnglish = findViewById(R.id.sbEnglish);
        tvChange = findViewById(R.id.tvChange);
        languageText=findViewById(R.id.languageText);
        MyApplication.setLang(changelanguageC);
        MyApplication.lang = MyApplication.getSaveString("Locale", changelanguageC);
        if (MyApplication.lang.equalsIgnoreCase("en")) {
            //sbFrench.setChecked(false);
            sbEnglish.setChecked(true);
        } else {
            sbFrench.setChecked(true);
           // languageText.setText("English");
            //sbFrench.setChecked(false);
        }

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        tvChange.setOnClickListener(changelanguageC);
        sbFrench.setOnCheckedChangeListener(changelanguageC);
        sbEnglish.setOnCheckedChangeListener(changelanguageC);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            if (compoundButton.getId() == R.id.sbFrench) {
                sbEnglish.setChecked(false);
            }
            if (compoundButton.getId() == R.id.sbEnglish) {
                sbFrench.setChecked(false); }
            }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvChange:
                langDialog();
                break;
        }

    }

    public void langDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.change_language))
                .setIcon(R.drawable.ic_baseline_translate_blue)
                .setMessage(getString(R.string.change_lang_conf_msg))
                .setCancelable(false)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        if(sbFrench.isChecked()){

                            applicationComponentClass.getmSharedPreferences().edit().putString("languageToUse", "fr").commit();

                            MyApplication.changeLocale(changelanguageC, "fr");
                            MyApplication.saveString("Locale", "fr", changelanguageC);

                        }

                        else if(sbEnglish.isChecked()){

                            applicationComponentClass.getmSharedPreferences().edit().putString("languageToUse", "en").commit();

                            MyApplication.changeLocale(changelanguageC, "en");
                            MyApplication.saveString("Locale", "en", changelanguageC);

                        }


                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }).create().show();
    }


}