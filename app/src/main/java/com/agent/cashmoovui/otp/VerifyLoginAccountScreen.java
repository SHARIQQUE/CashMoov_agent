package com.agent.cashmoovui.otp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.set_pin.SetPin;

import org.json.JSONObject;

import java.util.Locale;

public class VerifyLoginAccountScreen extends AppCompatActivity implements View.OnClickListener {
    public static VerifyLoginAccountScreen verifyaccountscreenC;
    EditText etOne,etTwo,etThree,etFour,etFive,etSix;
    TextView tvPhoneNoMsg,tvContinue;
    MyApplication applicationComponentClass;
    String  languageToUse;

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




        setContentView(R.layout.activity_verify_account_screen);
        verifyaccountscreenC = this;
        getIds();
    }

    private void getIds() {
        etOne = findViewById(R.id.etOne);
        etTwo = findViewById(R.id.etTwo);
        etThree = findViewById(R.id.etThree);
        etFour = findViewById(R.id.etFour);
        etFive = findViewById(R.id.etFive);
        etSix = findViewById(R.id.etSix);
        tvPhoneNoMsg = findViewById(R.id.tvPhoneNoMsg);
        tvContinue = findViewById(R.id.tvContinue);

        etSix.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() >= 1)
                    MyApplication.hideKeyboard(verifyaccountscreenC);            }
        });

        TextView[] otpTextViews = {etOne, etTwo, etThree, etFour,etFive,etSix};

        for (TextView currTextView : otpTextViews) {
            currTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    nextTextView().requestFocus();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }

                public TextView nextTextView() {

                    int i;
                    for (i = 0; i < otpTextViews.length - 1; i++) {
                        if (otpTextViews[i] == currTextView)
                            return otpTextViews[i + 1];
                    }
                    return otpTextViews[i];
                }
            });
        }


        setOnCLickListener();

    }

    public String getEditTextString(EditText editText){
        return editText.getText().toString().trim();
    }
    String pass;
    private void setOnCLickListener() {
        tvContinue.setOnClickListener(verifyaccountscreenC);
    }

    @Override
    public void onClick(View view) {

        pass=getEditTextString(etOne)+getEditTextString(etTwo)+getEditTextString(etThree)+
                getEditTextString(etFour)+getEditTextString(etFive)+getEditTextString(etSix);
        if(pass.length()==6){
            callApiLoginPass();
        }else{
            MyApplication.showToast(verifyaccountscreenC,"Please enter otp");
        }
      //  callApiLoginPass();
    }


    private void callApiLoginPass() {
        try{

            JSONObject loginJson=new JSONObject();
            
            loginJson.put("username",MyApplication.getSaveString("USERNAME",VerifyLoginAccountScreen.this));
            loginJson.put("password",pass);
            loginJson.put("grant_type","password");
            // loginJson.put("scope","read write");

            System.out.println("Login request"+loginJson.toString());
            MyApplication.showloader(VerifyLoginAccountScreen.this,"Verify OTP");
            API.POST_REQEST_GENERATEOTP("ewallet/oauth/token",loginJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    /*MyApplication.showloader(verifyaccountscreenC,"Verify OTP");
                    MyApplication.hideLoader();*/

                    //ArrayList<ServiceList.serviceListMain> dataM=new ArrayList<>();

                    System.out.println("Login response======="+jsonObject.toString());


                    MyApplication.saveString("token",jsonObject.optString("access_token"),verifyaccountscreenC);
                    MyApplication.saveString("firstName",jsonObject.optString("firstName"),verifyaccountscreenC);
                    MyApplication.saveString("lastName",jsonObject.optString("lastName"),verifyaccountscreenC);
                    MyApplication.saveString("email",jsonObject.optString("email"),verifyaccountscreenC);
                    MyApplication.saveString("mobile",jsonObject.optString("mobile"),verifyaccountscreenC);
                    MyApplication.saveString("walletOwnerCategoryCode",jsonObject.optString("walletOwnerCategoryCode"),verifyaccountscreenC);
                    MyApplication.saveString("walletOwnerCode",jsonObject.optString("walletOwnerCode"),verifyaccountscreenC);
                    MyApplication.saveString("userCountryCode",jsonObject.optString("userCountryCode"),verifyaccountscreenC);
                    MyApplication.saveString("userCode",jsonObject.optString("userCode"),verifyaccountscreenC);
                    MyApplication.saveString("username",jsonObject.optString("username"),verifyaccountscreenC);
                    MyApplication.saveString("userCountryCode",jsonObject.optString("userCountryCode"),verifyaccountscreenC);
                    MyApplication.saveString("issuingCountryName", jsonObject.optString("issuingCountryName"), verifyaccountscreenC);

                    Intent i = new Intent(verifyaccountscreenC, SetPin.class);
                    startActivity(i);
                    finish();
                    // Toast.makeText(verifyaccountscreenC,getString(R.string.login_successful),Toast.LENGTH_LONG).show();

                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();
                    MyApplication.showToast(verifyaccountscreenC,aFalse);

                }
            });

        }catch (Exception e){

        }

    }
}

