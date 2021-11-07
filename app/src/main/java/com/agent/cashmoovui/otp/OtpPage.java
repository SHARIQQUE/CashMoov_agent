package com.agent.cashmoovui.otp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.login.LoginMsis;
import com.agent.cashmoovui.set_pin.SetPin;

import org.json.JSONObject;

import java.util.Locale;


public class OtpPage extends AppCompatActivity implements View.OnClickListener {

    MyApplication applicationComponentClass;
    String languageToUse = "";

    EditText et_One, et_Two, et_Three, et_Four,et_Five,et_six;
    String str_one, str_two, str_three, str_four,str_five,str_six;

    TextView tv_Continue;

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

        setContentView(R.layout.otp_page);


        et_One = (EditText) findViewById(R.id.et_One);
        et_Two = (EditText) findViewById(R.id.et_Two);
        et_Three = (EditText) findViewById(R.id.et_Three);
        et_Four = (EditText) findViewById(R.id.et_Four);
        et_Five = (EditText) findViewById(R.id.et_Five);
        et_six = (EditText) findViewById(R.id.et_six);

        tv_Continue = (TextView) findViewById(R.id.tv_Continue);


        et_One.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (et_One.getText().toString().length() == 1)     //size as per your requirement
                {
                    et_Two.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        et_Two.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (et_Two.getText().toString().length() == 1)     //size as per your requirement
                {
                    et_Three.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        et_Three.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (et_Three.getText().toString().length() == 1)     //size as per your requirement
                {

                    et_Four.requestFocus();

                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });


        et_Four.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (et_Four.getText().toString().length() == 1)     //size as per your requirement
                {
                    et_Five.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        et_Five.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (et_Five.getText().toString().length() == 1)     //size as per your requirement
                {

                    et_six.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });




        et_six.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (et_six.getText().toString().length() == 1)     //size as per your requirement
                {

//                    if (validation_Details()) {
//                        if (new InternetCheck().isConnected(OtpPage.this)) {
//
//                            otp_verify_api();
//
//                        } else {
//                            Toast.makeText(OtpPage.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
//                        }
//                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        tv_Continue.setOnClickListener(this);


    }

 /*
    private void otp_verify_api() {
        try{

            JSONObject jsonObject=new JSONObject();


            jsonObject.put("transTypeCode","101813");      // Temporary Hard Code acording to Praveen
            jsonObject.put("otp",str_one+str_two+str_three+str_four+str_five+str_six);



            API.POST_REQUEST_VERIFY_OTP("ewallet/api/v1/otp/verify",jsonObject,new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {

                        //JSONObject jsonObject = new JSONObject("");

                        if (jsonObject.has("error")) {

                            String error = jsonObject.getString("error");
                            String error_message = jsonObject.getString("error_message");

                            Toast.makeText(OtpPage.this, error_message, Toast.LENGTH_LONG).show();

                            if(error.equalsIgnoreCase("1251")) {

                                Intent i = new Intent(OtpPage.this, VerifyLoginAccountScreen.class);
                                startActivity(i);

                                // finish();
                            }
                        }

                        else
                        {

                            String resultDescription = jsonObject.getString("resultDescription");
                            String resultCode = jsonObject.getString("resultCode");

                            if (resultCode.equalsIgnoreCase("0"))
                            {
                                Intent i = new Intent(OtpPage.this, SetPin.class);
                                startActivity(i);
                            }

                            else {
                                Toast.makeText(OtpPage.this, resultDescription, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    catch (Exception e)
                    {
                        Toast.makeText(OtpPage.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }


                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();
                    MyApplication.showToast(OtpPage.this,aFalse);

                }
            });

        }catch (Exception e){

            MyApplication.hideLoader();
            MyApplication.showToast(OtpPage.this,e.toString());
        }

    }
    
    */



    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_Continue: {

                if (validation_Details()) {

                    if (new InternetCheck().isConnected(OtpPage.this)) {


                        callApiLoginPass();


                        // otp_verify_api();

                    } else {
                        Toast.makeText(OtpPage.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                    }
                }

            }
            break;

        }
    }

    private void callApiLoginPass() {

        try{

            JSONObject loginJson=new JSONObject();
            
            loginJson.put("username",MyApplication.getSaveString("USERNAME",OtpPage.this));
            loginJson.put("password",str_one+str_two+str_three+str_four+str_five+str_six);
            loginJson.put("grant_type","password");

            System.out.println("Login request"+loginJson.toString());
            MyApplication.showloader(OtpPage.this,"Verify OTP");
            API.POST_REQEST_LoginOTP("ewallet/oauth/token",loginJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();


                    System.out.println("Login response======="+jsonObject.toString());


                    MyApplication.saveString("token",jsonObject.optString("access_token"),OtpPage.this);
                    MyApplication.saveString("firstName",jsonObject.optString("firstName"),OtpPage.this);
                    MyApplication.saveString("lastName",jsonObject.optString("lastName"),OtpPage.this);
                    MyApplication.saveString("email",jsonObject.optString("email"),OtpPage.this);
                    MyApplication.saveString("mobile",jsonObject.optString("mobile"),OtpPage.this);
                    MyApplication.saveString("walletOwnerCategoryCode",jsonObject.optString("walletOwnerCategoryCode"),OtpPage.this);
                    MyApplication.saveString("walletOwnerCode",jsonObject.optString("walletOwnerCode"),OtpPage.this);
                    MyApplication.saveString("userCountryCode",jsonObject.optString("userCountryCode"),OtpPage.this);
                    MyApplication.saveString("userCode",jsonObject.optString("userCode"),OtpPage.this);
                    MyApplication.saveString("username",jsonObject.optString("username"),OtpPage.this);
                    MyApplication.saveString("userCountryCode",jsonObject.optString("userCountryCode"),OtpPage.this);
                    MyApplication.saveString("issuingCountryName", jsonObject.optString("issuingCountryName"), OtpPage.this);

                    Intent i = new Intent(OtpPage.this, SetPin.class);
                    i.putExtra("CHECKINTENTSETPIN","VerifyLoginAccountScreen");
                    startActivity(i);
                    finish();
                    // Toast.makeText(OtpPage.this,getString(R.string.login_successful),Toast.LENGTH_LONG).show();

                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    Toast.makeText(OtpPage.this, aFalse, Toast.LENGTH_LONG).show();


                }
            });

        }catch (Exception e){

        }

    }


    boolean validation_Details() {


        str_one = et_One.getText().toString();
        str_two = et_Two.getText().toString();
        str_three = et_Three.getText().toString();
        str_four = et_Four.getText().toString();
        str_five = et_Five.getText().toString();
        str_six = et_six.getText().toString();

        if (str_one.isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.please_enter_otp_code));

            return false;
        }
        else if (str_one.length() < 0) {

            MyApplication.showErrorToast(this, getString(R.string.please_enter_otp_code));

            return false;
        } else if (str_two.isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.please_enter_otp_code));

            return false;
        } else if (str_two.length() < 0) {

            MyApplication.showErrorToast(this, getString(R.string.please_enter_otp_code));

            return false;
        } else if (str_three.isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.please_enter_otp_code));

            return false;
        } else if (str_three.length() < 0) {

            MyApplication.showErrorToast(this, getString(R.string.please_enter_otp_code));

            return false;
        }

        else if (str_four.isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.please_enter_otp_code));

            return false;
        } else if (str_four.length() < 0) {

            MyApplication.showErrorToast(this, getString(R.string.please_enter_otp_code));

            return false;
        }

        else if (str_five.isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.please_enter_otp_code));

            return false;
        } else if (str_five.length() < 0) {

            MyApplication.showErrorToast(this, getString(R.string.please_enter_otp_code));

            return false;
        }

        else if (str_six.isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.please_enter_otp_code));

            return false;
        } else if (str_six.length() < 0) {

            MyApplication.showErrorToast(this, getString(R.string.please_enter_otp_code));

            return false;
        }


        return true;
    }


}
