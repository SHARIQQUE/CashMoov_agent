package com.agent.cashmoovui.otp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.set_pin.SetPin;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import org.json.JSONObject;
import java.util.Locale;

public class OtpPage extends AppCompatActivity implements OnOtpCompletionListener {

    MyApplication applicationComponentClass;
    String languageToUse = "";
    OtpView otp_view;


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


        otp_view = findViewById(R.id.otp_view);

        otp_view.setOtpCompletionListener(this);
    }

    @Override
    public void onOtpCompleted(String otp) {
        if(otp.length()==6){
            callApiLoginPass(otp);
        }else{
            MyApplication.showToast(OtpPage.this,getString(R.string.please_enter_otp_code));
        }
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

    private void callApiLoginPass(String otp) {

        try{

            JSONObject loginJson=new JSONObject();
            
            loginJson.put("username",MyApplication.getSaveString("USERNAME",OtpPage.this));
            loginJson.put("password",otp);
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



}
