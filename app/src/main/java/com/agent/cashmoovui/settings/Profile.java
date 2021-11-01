package com.agent.cashmoovui.settings;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.activity.WalletScreen;
import com.agent.cashmoovui.otp.OtpPage;
import com.agent.cashmoovui.otp.VerifyLoginAccountScreen;
import com.agent.cashmoovui.pin_change.ChangePin;
import com.agent.cashmoovui.set_pin.SetPin;
import com.bumptech.glide.BuildConfig;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class Profile extends AppCompatActivity implements View.OnClickListener {
    public static Profile profileC;
    ImageView imgBack,imgHome;
    ImageView imgQR;
    SmoothBottomBar bottomBar;
    LinearLayout linBeneficiary,linChangeLang,linConfidentiality,linShareApp,
            linTermCondition,linAbout,linChangePin,linEditProfile,linReset;

    TextView currency,number,etAddress,name;
    CircleImageView profile_img;

    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int READ_EXTERNAL_STORAGE = 201;

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
            setContentView(R.layout.activity_profile);


            profileC = this;
            //  setBackMenu();


            getIds();

        } catch (Exception e) {
            Toast.makeText(Profile.this, e.toString(), Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.profil)
                .error(R.drawable.profil);
        String ImageName=MyApplication.getSaveString("ImageName", profileC);
        if(ImageName!=null&&ImageName.length()>1) {
            String image_url = MyApplication.ImageURL + ImageName;
            Glide.with(this).load(image_url).apply(options).into(profile_img);
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
//        imgHome = findViewById(R.id.imgHome);
//
//        imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSupportNavigateUp();
//            }
//        });
//        imgHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });
//
//    }


    @Override
    protected void onRestart() {
        super.onRestart();
        bottomBar.setItemActiveIndex(2);
        bottomBar.setBarIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
    }


    private void getIds() {

      //  imgQR = findViewById(R.id.imgQR);


        bottomBar = findViewById(R.id.bottomBar);
        linBeneficiary = findViewById(R.id.linBeneficiary);
        linChangeLang = findViewById(R.id.linChangeLang);
        linConfidentiality = findViewById(R.id.linConfidentiality);
        linShareApp = findViewById(R.id.linShareApp);
        linTermCondition = findViewById(R.id.linTermCondition);
        linAbout = findViewById(R.id.linAbout);
        linChangePin = findViewById(R.id.linChangePin);
        linEditProfile = findViewById(R.id.linEditProfile);
        linReset = findViewById(R.id.linReset);

        currency = findViewById(R.id.currency);
        number = findViewById(R.id.number);
        etAddress = findViewById(R.id.etAddress);
        name = findViewById(R.id.name);
        profile_img = findViewById(R.id.profile_img);



      //  String lastName= MyApplication.getSaveString("firstName",profileC)+" "+MyApplication.getSaveString("lastName",profileC);
        String add= MyApplication.getSaveString("issuingCountryName",profileC);
        String num= MyApplication.getSaveString("mobile",profileC);
        String firstName= MyApplication.getSaveString("firstName",profileC);


        number.setText(num);


        if(firstName.equalsIgnoreCase(""))
        {
            name.setText(firstName+"");
        }
        else if(firstName.equalsIgnoreCase("null"))
        {
            name.setText(firstName+"");
        }
        else if(firstName==null)
        {
            name.setText(firstName+"");
        }

        else {
            name.setText(firstName);

        }


        etAddress.setText(add);


        bottomBar.setItemActiveIndex(2);
        bottomBar.setBarIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));

        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int bottomId) {
                if (bottomId == 0) {
                    Intent i = new Intent(profileC, MainActivity.class);
                    startActivity(i);
                    //  finish();
                }
                if (bottomId == 1) {
                    Intent i = new Intent(profileC, WalletScreen.class);
                    startActivity(i);
                    //finish();
                }
                if (bottomId == 2) {
//                    Intent i = new Intent(profileC, Profile.class);
//                    startActivity(i);
                    //  finish();
                }
                return true;
            }
        });

        setOnCLickListener();

    }

    private void setOnCLickListener() {

      //  imgQR.setOnClickListener(profileC);

        linBeneficiary.setOnClickListener(profileC);
        linChangeLang.setOnClickListener(profileC);
        linConfidentiality.setOnClickListener(profileC);
        linShareApp.setOnClickListener(profileC);
        linTermCondition.setOnClickListener(profileC);
        linAbout.setOnClickListener(profileC);
        linChangePin.setOnClickListener(profileC);
        linEditProfile.setOnClickListener(profileC);
        linReset.setOnClickListener(profileC);
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {

//            case R.id.imgQR:
//                intent = new Intent(profileC, MyQrCode.class);
//                startActivity(intent);
//                break;

            case R.id.linBeneficiary:
//                intent = new Intent(profileC, AddBeneficiary.class);
//                startActivity(intent);
                Toast.makeText(profileC,"Coming Soon.....", Toast.LENGTH_SHORT).show();

                break;
            case R.id.linChangeLang:


                intent = new Intent(profileC, ChangeLanguage.class);
                startActivity(intent);

                break;
            case R.id.linConfidentiality:
                intent = new Intent(profileC, Confidentiality.class);
                startActivity(intent);
                break;
            case R.id.linShareApp:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;

            case R.id.linTermCondition:
                Toast.makeText(profileC,"Coming Soon.....", Toast.LENGTH_SHORT).show();

                break;

            case R.id.linAbout:

                intent = new Intent(profileC, About.class);
                startActivity(intent);
                break;


            case R.id.linChangePin:



                intent = new Intent(profileC, ChangePin.class);
                startActivity(intent);


                break;

            case R.id.linEditProfile:


                if(checkPermission_camera())
                {

                    if(checkPermission_read_external_storage())
                    {

                        intent = new Intent(profileC, EditProfile.class);
                        startActivity(intent);
                    }
                    else {
                        requestPermission_read_external_storage();
                    }



                }
                else {
                    requestPermission();
                }




                break;

            case R.id.linReset:
                intent = new Intent(profileC, Logout.class);
                startActivity(intent);
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

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void requestPermission_read_external_storage() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
    }

    private boolean checkPermission_read_external_storage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }






}