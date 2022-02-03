package com.agent.cashmoovui.set_pin;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.agent.cashmoovui.HiddenPassTransformationMethod;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.login.LoginMsis;
import com.agent.cashmoovui.wallet_owner.agent.AgentKYCAttached;
import com.agent.cashmoovui.wallet_owner.agent.AgentSignature;
import com.agent.cashmoovui.wallet_owner.branch.BranchSignature;
import com.agent.cashmoovui.wallet_owner.subscriber.SubscriberSignature;

import org.json.JSONObject;
import java.util.Locale;

public class SetPin extends AppCompatActivity implements View.OnClickListener {

    MyApplication applicationComponentClass;
    String languageToUse = "";
    String strPin = "", strRePin;
    EditText et_Pin, et_reEnterPin;
    TextView tv_continue;
    String checkIntent;

    boolean  isPasswordVisible,isPasswordVisible2;

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

        setContentView(R.layout.activity_set_pin);

        et_Pin = (EditText) findViewById(R.id.et_Pin);
        et_reEnterPin = (EditText) findViewById(R.id.et_reEnterPin);
        tv_continue = (TextView) findViewById(R.id.tv_continue);


        if (getIntent().getExtras() != null) {
            checkIntent = (getIntent().getStringExtra("CHECKINTENTSETPIN"));

        }

        tv_continue.setOnClickListener(this);

        et_reEnterPin.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() >= 4)
                    MyApplication.hideKeyboard(SetPin.this);            }
        });


        HiddenPassTransformationMethod hiddenPassTransformationMethod=new HiddenPassTransformationMethod();
        et_Pin.setTransformationMethod(hiddenPassTransformationMethod);
        et_Pin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (et_Pin.getRight() - et_Pin.getCompoundDrawables()[RIGHT].getBounds().width())) {
                        int selection = et_Pin.getSelectionEnd();
                        if (isPasswordVisible) {
                            // set drawable image
                            et_Pin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            // hide Password
                            et_Pin.setTransformationMethod(hiddenPassTransformationMethod);
                            isPasswordVisible = false;
                        } else  {
                            // set drawable image
                            et_Pin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                            // show Password
                            et_Pin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isPasswordVisible = true;
                        }
                        et_Pin.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        et_reEnterPin.setTransformationMethod(hiddenPassTransformationMethod);
        et_reEnterPin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (et_reEnterPin.getRight() - et_reEnterPin.getCompoundDrawables()[RIGHT].getBounds().width())) {
                        int selection = et_reEnterPin.getSelectionEnd();
                        if (isPasswordVisible2) {
                            // set drawable image
                            et_reEnterPin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            // hide Password
                            et_reEnterPin.setTransformationMethod(hiddenPassTransformationMethod);
                            isPasswordVisible2 = false;
                        } else  {
                            // set drawable image
                            et_reEnterPin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                            // show Password
                            et_reEnterPin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isPasswordVisible2 = true;
                        }
                        et_reEnterPin.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_continue: {

                if (validation_Details()) {
                    if (new InternetCheck().isConnected(SetPin.this)) {


                        setpin_api();
                    }

                    else {
                        Toast.makeText(SetPin.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                    }
                }

                break;

            }
        }

    }

    boolean validation_Details()
    {

        strPin = et_Pin.getText().toString().trim();
        strRePin = et_reEnterPin.getText().toString().trim();

        if(strPin.isEmpty()) {

            MyApplication.showErrorToast(this,getString(R.string.enter_pin));

            return false;
        }

        else if(strPin.length() < 4) {

            MyApplication.showErrorToast(this,getString(R.string.enter_pin));

            return false;
        }

       else if(strRePin.isEmpty()) {

            MyApplication.showErrorToast(this,getString(R.string.re_enter_pin));

            return false;
        }

        else if(strRePin.length() < 4) {

            MyApplication.showErrorToast(this,getString(R.string.re_enter_pin));

            return false;
        }

        else if(!strPin.equalsIgnoreCase(strRePin)) {

            MyApplication.showErrorToast(this,getString(R.string.mpin_renter_shoud_be_same));

            return false;
        }

            return true;
    }



    private void setpin_api() {
        try{

            String encryptionDatanew = AESEncryption.getAESEncryption(strRePin);

            JSONObject setPinJson=new JSONObject();
            setPinJson.put("pin",encryptionDatanew);

            MyApplication.showloader(SetPin.this,"Please wait!");
            API.POST_REQEST_SETPIN("ewallet/api/v1/walletOwnerUser/setPin", setPinJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();


                    if (jsonObject != null) {
                        if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){

                            MyApplication.showToast(SetPin.this,"Your Pin generate Successfully!");



                            Intent intent = new Intent(SetPin.this, LoginMsis.class);
                            startActivity(intent);
                            finish();

//                            if(checkIntent.equalsIgnoreCase("SubscriberKYC")){
//                                Intent intent = new Intent(SetPin.this, SubscriberSignature.class);
//                                startActivity(intent);
//                                finish();
//                                return;
//                            }
//                            if(checkIntent.equalsIgnoreCase("AgentKYC")) {
//                                Intent intent = new Intent(SetPin.this, AgentSignature.class);
//                                startActivity(intent);
//                                finish();
//                                return;
//                            }
//                            if(checkIntent.equalsIgnoreCase("BranchKYC")) {
//                                Intent intent = new Intent(SetPin.this, BranchSignature.class);
//                                startActivity(intent);
//                                finish();
//                                return;
//                            }
//                            else {
//                                Intent intent = new Intent(SetPin.this, LoginMsis.class);
//                                startActivity(intent);
//                                finish();
//                            }


                        }

                        else if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("2001"))
                        {
                            MyApplication.showToast(SetPin.this,getString(R.string.technical_failure));
                        }

                        else {
                            MyApplication.showToast(SetPin.this,jsonObject.optString("resultDescription", "N/A"));
                        }
                    }
                }

                @Override
                public void failure(String aFalse) {

                }
            });

        }catch (Exception e){

        }


    }





}