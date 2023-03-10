package com.agent.cashmoovui.pin_change;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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
import com.agent.cashmoovui.set_pin.AESEncryption;
import com.agent.cashmoovui.settings.Profile;

import org.json.JSONObject;

import java.util.Locale;

public class ChangePin extends AppCompatActivity implements View.OnClickListener {

    MyApplication applicationComponentClass;
    String languageToUse = "";
    String oldPinStr = "", newPinStr = "",confirmPinStr;

    EditText et_oldPin, et_newPin,et_confirmPin;
    TextView tv_continue;
    Button btnCancel;


    boolean isPasswordVisible,isPasswordVisible2,isPasswordVisible3;
    private long mLastClickTime = 0;


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

        setContentView(R.layout.change_pin);

        et_oldPin = (EditText) findViewById(R.id.et_oldPin);
        et_newPin = (EditText) findViewById(R.id.et_newPin);
        et_confirmPin = (EditText) findViewById(R.id.et_confirmPin);
        btnCancel = findViewById(R.id.btnCancel);


        tv_continue = (TextView) findViewById(R.id.tv_continue);

        btnCancel.setOnClickListener(this);

        tv_continue.setOnClickListener(this);
        HiddenPassTransformationMethod hiddenPassTransformationMethod=new HiddenPassTransformationMethod();
        et_oldPin.setTransformationMethod(hiddenPassTransformationMethod);
        et_oldPin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (et_oldPin.getRight() - et_oldPin.getCompoundDrawables()[RIGHT].getBounds().width())) {
                        int selection = et_oldPin.getSelectionEnd();
                        if (isPasswordVisible) {
                            // set drawable image
                            et_oldPin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            // hide Password
                            et_oldPin.setTransformationMethod(hiddenPassTransformationMethod);
                            isPasswordVisible = false;
                        } else  {
                            // set drawable image
                            et_oldPin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                            // show Password
                            et_oldPin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isPasswordVisible = true;
                        }
                        et_oldPin.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        et_newPin.setTransformationMethod(hiddenPassTransformationMethod);
        et_newPin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (et_newPin.getRight() - et_newPin.getCompoundDrawables()[RIGHT].getBounds().width())) {
                        int selection = et_newPin.getSelectionEnd();
                        if (isPasswordVisible2) {
                            // set drawable image
                            et_newPin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            // hide Password
                            et_newPin.setTransformationMethod(hiddenPassTransformationMethod);
                            isPasswordVisible2 = false;
                        } else  {
                            // set drawable image
                            et_newPin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                            // show Password
                            et_newPin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isPasswordVisible2 = true;
                        }
                        et_newPin.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        et_confirmPin.setTransformationMethod(hiddenPassTransformationMethod);
        et_confirmPin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (et_confirmPin.getRight() - et_confirmPin.getCompoundDrawables()[RIGHT].getBounds().width())) {
                        int selection = et_confirmPin.getSelectionEnd();
                        if (isPasswordVisible3) {
                            // set drawable image
                            et_confirmPin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            // hide Password
                            et_confirmPin.setTransformationMethod(hiddenPassTransformationMethod);
                            isPasswordVisible3 = false;
                        } else  {
                            // set drawable image
                            et_confirmPin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                            // show Password
                            et_confirmPin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isPasswordVisible3 = true;
                        }
                        et_confirmPin.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });


    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {

            case R.id.tv_continue: {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (validation_Details()) {

                    if (new InternetCheck().isConnected(ChangePin.this)) {

                        setpin_api();
                    }

                    else {
                        Toast.makeText(ChangePin.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                    }
                }

                break;

            }
            case R.id.btnCancel:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                intent = new Intent(getApplicationContext(), Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }

    }

    boolean validation_Details()
    {

        oldPinStr = et_oldPin.getText().toString().trim();
        newPinStr = et_newPin.getText().toString().trim();
        confirmPinStr = et_confirmPin.getText().toString().trim();

        if(oldPinStr.isEmpty()) {

            MyApplication.showErrorToast(this,getString(R.string.plz_enter_old_pin));

            return false;
        }

        else if(oldPinStr.length() < 3) {

            MyApplication.showErrorToast(this,getString(R.string.plz_enter_old_pin));

            return false;
        }

       else if(newPinStr.isEmpty()) {

            MyApplication.showErrorToast(this,getString(R.string.plz_enter_new_pin));

            return false;
        }

        else if(newPinStr.length() < 3) {

            MyApplication.showErrorToast(this,getString(R.string.val_valid_new_pin));

            return false;
        }

        else if(confirmPinStr.isEmpty()) {

            MyApplication.showErrorToast(this,getString(R.string.plz_confirm_new_pin));

            return false;
        }

       /* else if(confirmPinStr.length() < 3) {

            MyApplication.showErrorToast(this,getString(R.string.plz_confirm_new_pin));

            return false;
        }*/

        else if(!newPinStr.equalsIgnoreCase(confirmPinStr)) {

            MyApplication.showErrorToast(this,getString(R.string.new_coinfirm_should_be_same));

            return false;
        }

            return true;
    }



    private void setpin_api() {
        try{

            String encryption_old = AESEncryption.getAESEncryption(oldPinStr);
            String encryption_new = AESEncryption.getAESEncryption(newPinStr);

           String walletOwnerUserCode =   MyApplication.getSaveString("CODE_AGENT",ChangePin.this);

           // MyApplication.showToast(ChangePin.this,walletOwnerUserCode);


            JSONObject jsonObject=new JSONObject();


            jsonObject.put("walletOwnerUserCode",walletOwnerUserCode);
            jsonObject.put("oldPin",encryption_old);
            jsonObject.put("pin",encryption_new);

          /*  String requestNo=AESEncryption.getAESEncryption(jsonObject.toString());
            JSONObject jsonObjectA=null;
            try{
                jsonObjectA=new JSONObject();
                jsonObjectA.put("request",requestNo);
            }catch (Exception e){

            }*/
            MyApplication.showloader(ChangePin.this,"Please wait!");

            API.PUT("ewallet/api/v1/walletOwnerUser/changePin", jsonObject, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();


                  //  JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1890125\",\"requestTime\":\"Thu Oct 28 12:15:22 IST 2021\",\"responseTime\":\"Thu Oct 28 12:15:23 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\"}");

                    if (jsonObject != null) {
                        if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                            //MyApplication.showToast(changepinC,getString(R.string.pin_changed_msg));
                            MyApplication.showToast(ChangePin.this,jsonObject.optString("resultDescription", "N/A"));
                            Intent intent = new Intent(ChangePin.this, LoginMsis.class);
                            startActivity(intent);
                            finish();


                        }else if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("2001")){
                            MyApplication.showToast(ChangePin.this,getString(R.string.technical_failure));
                            MyApplication.hideLoader();

                        } else {
                            MyApplication.hideLoader();

                            MyApplication.showToast(ChangePin.this,jsonObject.optString("resultDescription", "N/A"));
                        }
                    }
                }

                @Override
                public void failure(String aFalse) {
                    MyApplication.hideLoader();

                }
            });

        }catch (Exception e){
            MyApplication.hideLoader();

        }


    }





}