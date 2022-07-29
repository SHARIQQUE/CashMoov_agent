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
import com.agent.cashmoovui.model.ServiceList;
import com.agent.cashmoovui.set_pin.SetPin;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class RESETPINOtpPage extends AppCompatActivity implements OnOtpCompletionListener {

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

        TextView otp_text=findViewById(R.id.otp_text);
        otp_text.setText(getString(R.string.verification_reset_pin_otp));

        otp_view = findViewById(R.id.otp_view);

        otp_view.setOtpCompletionListener(this);
    }

    @Override
    public void onOtpCompleted(String otp) {
        if(otp.length()==6){
            callApiLoginPass(otp);
        }else{
            MyApplication.showToast(RESETPINOtpPage.this,getString(R.string.please_enter_otp_code));
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
            
            loginJson.put("username",MyApplication.getSaveString("USERNAME", RESETPINOtpPage.this));
            loginJson.put("password",otp);
            loginJson.put("grant_type","password");

            System.out.println("Login request"+loginJson.toString());
            MyApplication.showloader(RESETPINOtpPage.this,"Verify OTP");
            API.POST_REQEST_RESETPIN("ewallet/oauth/token",loginJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();
                    ArrayList<ServiceList.serviceListMain> dataM=new ArrayList<>();

                    System.out.println("Login response======="+jsonObject.toString());


                    MyApplication.saveString("token",jsonObject.optString("access_token"), RESETPINOtpPage.this);
                    MyApplication.saveString("firstName",jsonObject.optString("firstName"), RESETPINOtpPage.this);
                    MyApplication.saveString("lastName",jsonObject.optString("lastName"), RESETPINOtpPage.this);
                    MyApplication.saveString("email",jsonObject.optString("email"), RESETPINOtpPage.this);
                    MyApplication.saveString("mobile",jsonObject.optString("mobile"), RESETPINOtpPage.this);
                    MyApplication.saveString("walletOwnerCategoryCode",jsonObject.optString("walletOwnerCategoryCode"), RESETPINOtpPage.this);
                    MyApplication.saveString("walletOwnerCode",jsonObject.optString("walletOwnerCode"), RESETPINOtpPage.this);
                    MyApplication.saveString("userCountryCode",jsonObject.optString("userCountryCode"), RESETPINOtpPage.this);
                    MyApplication.saveString("userCode",jsonObject.optString("userCode"), RESETPINOtpPage.this);
                    MyApplication.saveString("username",jsonObject.optString("username"), RESETPINOtpPage.this);
                    MyApplication.saveString("userCountryCode",jsonObject.optString("userCountryCode"), RESETPINOtpPage.this);
                    MyApplication.saveString("issuingCountryName", jsonObject.optString("issuingCountryName"), RESETPINOtpPage.this);

                    try {
                        JSONArray serviceListResponceArray=jsonObject.optJSONArray("serviceList");
                        if(serviceListResponceArray!=null&&serviceListResponceArray.length()>0){

                            dataM.clear();
                            for (int i=0;i<serviceListResponceArray.length();i++){

                                JSONObject jsonObjectServiceList=serviceListResponceArray.optJSONObject(i);
                                JSONArray serviceCategoryListResArray=jsonObjectServiceList.optJSONArray("serviceCategoryList");
                                if(serviceCategoryListResArray!=null&&serviceCategoryListResArray.length()>0){
                                    ArrayList<ServiceList.serviceCategoryList> data=new ArrayList<>();
                                    data.clear();
                                    for (int j=0;j<serviceCategoryListResArray.length();j++){
                                        JSONObject jsonObjectServiceListResponceArray=serviceCategoryListResArray.optJSONObject(j);

                                        data.add(new ServiceList.serviceCategoryList(
                                                jsonObjectServiceListResponceArray.optInt("id"),
                                                jsonObjectServiceListResponceArray.optString("code"),
                                                jsonObjectServiceListResponceArray.optString("serviceCode"),
                                                jsonObjectServiceListResponceArray.optString("serviceName"),
                                                jsonObjectServiceListResponceArray.optString("name"),
                                                jsonObjectServiceListResponceArray.optString("status"),
                                                jsonObjectServiceListResponceArray.optString("creationDate"),
                                                jsonObjectServiceListResponceArray.optBoolean("productAllowed")
                                        ));

                                    }

                                    dataM.add(new ServiceList.serviceListMain(
                                            jsonObjectServiceList.optInt("id"),
                                            jsonObjectServiceList.optString("code"),
                                            jsonObjectServiceList.optString("name"),
                                            jsonObjectServiceList.optString("status"),
                                            jsonObjectServiceList.optString("creationDate"),
                                            jsonObjectServiceList.optBoolean("isOverdraftTrans"),
                                            data
                                    ));

                                }
                            }

                            ServiceList serviceList=new ServiceList(dataM);
                            MyApplication.tinyDB.putObject("ServiceList",serviceList);
                        }
                    }catch (Exception e){

                    }

                    Intent i = new Intent(RESETPINOtpPage.this, SetPin.class);
                    i.putExtra("CHECKINTENTSETPIN","VerifyLoginAccountScreen");
                    startActivity(i);
                    finish();
                    // Toast.makeText(OtpPage.this,getString(R.string.login_successful),Toast.LENGTH_LONG).show();

                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    Toast.makeText(RESETPINOtpPage.this, aFalse, Toast.LENGTH_LONG).show();


                }
            });

        }catch (Exception e){

        }

    }



}
