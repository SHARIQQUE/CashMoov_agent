package com.agent.cashmoovui.otp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.login.LoginPin;
import com.agent.cashmoovui.model.ServiceList;
import com.agent.cashmoovui.set_pin.SetPin;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class VerifyLoginAccountScreen extends AppCompatActivity implements OnOtpCompletionListener {
    public static VerifyLoginAccountScreen verifyaccountscreenC;
    OtpView otp_view;
    TextView tvPhoneNoMsg;
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
        otp_view = findViewById(R.id.otp_view);
        tvPhoneNoMsg = findViewById(R.id.tvPhoneNoMsg);

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        otp_view.setOtpCompletionListener(verifyaccountscreenC);
    }

    @Override
    public void onOtpCompleted(String otp) {
        if(otp.length()==6){
            callApiLoginPass(otp);
        }else{
            MyApplication.showToast(verifyaccountscreenC,getString(R.string.please_enter_otp_code));
        }
    }


    private void callApiLoginPass(String otp) {
        try{

            JSONObject loginJson=new JSONObject();
            
            loginJson.put("username",MyApplication.getSaveString("USERNAME",VerifyLoginAccountScreen.this));
            loginJson.put("password",otp);
            loginJson.put("grant_type","password");
            // loginJson.put("scope","read write");

            System.out.println("Login request"+loginJson.toString());
            MyApplication.showloader(VerifyLoginAccountScreen.this,"Verify OTP");
            API.POST_REQEST_GENERATEOTP("ewallet/oauth/token",loginJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    /*MyApplication.showloader(verifyaccountscreenC,"Verify OTP");
                    MyApplication.hideLoader();*/

                    ArrayList<ServiceList.serviceListMain> dataM=new ArrayList<>();

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

