package com.agent.cashmoovui.otp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.login.LoginPin;
import com.agent.cashmoovui.model.ServiceList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class VerifyLoginOTPScreen extends AppCompatActivity implements OnOtpCompletionListener {
    public static VerifyLoginOTPScreen verifyloginotpscreenC;
    OtpView otp_view;
    TextView tvPhoneNoMsg;
    String FCM_TOKEN;
    MyApplication applicationComponentClass;
    String languageToUse = "";

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
        setContentView(R.layout.activity_verify_login_otp_screen);
        verifyloginotpscreenC = this;
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    FCM_TOKEN = task.getException().getMessage();
                    Log.w("FCM TOKEN Failed", task.getException());
                } else {
                    FCM_TOKEN = task.getResult().getToken();
                    Log.i("FCM TOKEN", FCM_TOKEN);
                }
            }
        });

        getIds();

    }



    private void getIds() {
        otp_view = findViewById(R.id.otp_view);
        tvPhoneNoMsg = findViewById(R.id.tvPhoneNoMsg);


        Intent intent = getIntent();

        if (intent.hasExtra("ERROR1251")) {
            tvPhoneNoMsg.setText(getString(R.string.verification_register_otpback));
        } else {
            tvPhoneNoMsg.setText(getString(R.string.verification_register_otp));
        }


        setOnCLickListener();


    }

    private void setOnCLickListener() {
        otp_view.setOtpCompletionListener(verifyloginotpscreenC);
    }

    @Override
    public void onOtpCompleted(String otp) {
        if(otp.length()==6){
            callApiLoginPass(otp);
        }else{
            MyApplication.showToast(verifyloginotpscreenC,getString(R.string.please_enter_otp_code));
        }
    }


    private void callApiLoginPass(String otp) {
        try{

            JSONObject loginJson=new JSONObject();

            loginJson.put("username",MyApplication.getSaveString("USERNAME",VerifyLoginOTPScreen.this));
            loginJson.put("password",MyApplication.getEncript(otp));
            loginJson.put("grant_type","password");
            loginJson.put("fcmToken",FCM_TOKEN);
//            loginJson.put("country",MyApplication.getSaveString("COUNTRY", VerifyLoginOTPScreen.this));
//            loginJson.put("cc",MyApplication.getSaveString("CC",VerifyLoginOTPScreen.this));
            // loginJson.put("scope","read write");

            System.out.println("Login request"+loginJson.toString());
            MyApplication.showloader(VerifyLoginOTPScreen.this,"Verify OTP");
            API.POST_REQEST_GENERATEOTP("ewallet/oauth/token",loginJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {


                    System.out.println("Login response======="+jsonObject.toString());

                    try {
                        ArrayList<ServiceList.serviceListMain> dataM=new ArrayList<>();
                        MyApplication.saveString("token", jsonObject.optString("access_token"), verifyloginotpscreenC);
                        MyApplication.saveString("firstName", jsonObject.optString("firstName"), verifyloginotpscreenC);
                        MyApplication.saveString("lastName", jsonObject.optString("lastName"), verifyloginotpscreenC);
                        MyApplication.saveString("email", jsonObject.optString("email"), verifyloginotpscreenC);
                        MyApplication.saveString("mobile", jsonObject.optString("mobile"), verifyloginotpscreenC);
                        MyApplication.saveString("walletOwnerCategoryCode", jsonObject.optString("walletOwnerCategoryCode"), verifyloginotpscreenC);
                        MyApplication.saveString("walletOwnerCode", jsonObject.optString("walletOwnerCode"), verifyloginotpscreenC);
                        MyApplication.saveString("userCountryCode", jsonObject.optString("userCountryCode"), verifyloginotpscreenC);
                        MyApplication.saveString("userCode", jsonObject.optString("userCode"), verifyloginotpscreenC);
                        MyApplication.saveString("username", jsonObject.optString("username"), verifyloginotpscreenC);
                        MyApplication.saveString("userCountryCode", jsonObject.optString("userCountryCode"), verifyloginotpscreenC);
                        MyApplication.saveString("issuingCountryName", jsonObject.optString("issuingCountryName"), verifyloginotpscreenC);

                        if (jsonObject.has("serviceList")) {
                            JSONArray jsonArray_serviceList = jsonObject.getJSONArray("serviceList");
                            if(jsonArray_serviceList!=null&&jsonArray_serviceList.length()>0) {

                                for (int i = 0; i < jsonArray_serviceList.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray_serviceList.getJSONObject(i);

                                    if (jsonObject1.has("serviceCategoryList")) {
                                        JSONArray jsonArray_serviceCategoryList = jsonObject1.getJSONArray("serviceCategoryList");
                                        for (int j = 0; j < jsonArray_serviceCategoryList.length(); j++) {
                                            JSONObject jsonObject2 = jsonArray_serviceCategoryList.getJSONObject(j);

                                            if (jsonObject2.has("serviceName")) {
                                                String serviceName = jsonObject2.getString("serviceName");

                                                if (serviceName.equalsIgnoreCase("Money Transfer")) {
                                                    if (jsonObject2.has("serviceCode")) {
                                                        String serviceCode = jsonObject2.getString("serviceCode");

                                                        MyApplication.saveString("serviceCode_LoginApi", serviceCode, verifyloginotpscreenC);
                                                    } else {
                                                        MyApplication.saveString("serviceCode_LoginApi", "", verifyloginotpscreenC); // not coming from server

                                                    }
                                                }

                                            }
                                        }

                                    }

                                }
                            }

                        } else {
                            MyApplication.saveString("serviceCode_LoginApi", "", verifyloginotpscreenC); // not coming from server

                        }

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
                                                    ,
                                                    jsonObjectServiceListResponceArray.optInt("minTransValue"),
                                                    jsonObjectServiceListResponceArray.optInt("maxTransValue")));
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


                        userInfo_api(jsonObject.optString("walletOwnerCode"));
                        // Toast.makeText(VerifyRegisterOTP.this,getString(R.string.login_successful),Toast.LENGTH_LONG).show();
                    }catch (Exception e) {
                            Toast.makeText(verifyloginotpscreenC,e.toString(),Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.showToast(VerifyLoginOTPScreen.this,aFalse);

                }
            });

        }catch (Exception e){

        }

    }


    private void userInfo_api(String userCode) {
        try{

            MyApplication.currencyModelArrayList.clear();
            API.GET("ewallet/api/v1/walletOwner/"+userCode,new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    try {

                        //  JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1725124\",\"requestTime\":\"Mon Oct 11 18:06:30 IST 2021\",\"responseTime\":\"Mon Oct 11 18:06:30 IST 2021\",\"firstLoginStatus\":\"N\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwnerUser\":{\"id\":1728,\"code\":\"101458\",\"firstName\":\"Mahendra\",\"userName\":\"Mahendra2355\",\"mobileNumber\":\"989898899\",\"email\":\"tarun.kumar@esteltelecom.com\",\"walletOwnerUserTypeCode\":\"100000\",\"walletOwnerUserTypeName\":\"Supervisor\",\"walletOwnerCategoryCode\":\"100000\",\"walletOwnerCategoryName\":\"Institute\",\"userCode\":\"1000002094\",\"status\":\"Active\",\"state\":\"Approved\",\"gender\":\"M\",\"dateOfBirth\":\"1960-01-15\",\"idProofTypeCode\":\"100005\",\"idProofTypeName\":\"COMPANY REGISTRATION NUMBER\",\"idProofNumber\":\"1111111111111111\",\"issuingCountryCode\":\"100102\",\"issuingCountryName\":\"India\",\"idExpiryDate\":\"2021-06-23\",\"creationDate\":\"2021-06-08T14:53:57.853+0530\",\"notificationName\":\"EMAIL\",\"notificationLanguage\":\"en\",\"createdBy\":\"100250\",\"modificationDate\":\"2021-10-11T16:18:02.389+0530\",\"modifiedBy\":\"100250\",\"macEnabled\":false,\"ipEnabled\":false,\"resetCredReqInit\":false,\"notificationTypeCode\":\"100000\"}}");


                        String resultCode =  jsonObject.getString("resultCode");
                        String resultDescription =  jsonObject.getString("resultDescription");

                        if(resultCode.equalsIgnoreCase("0")) {


                            JSONObject walletOwner = jsonObject.getJSONObject("walletOwner");
//
//                            String EMAIL = walletOwnerUser.getString("email");
//                            String USERCODE = walletOwnerUser.getString("userCode");
//                            String CODE_AGENT = walletOwnerUser.getString("code");
//                            String NTTYPECODE = walletOwnerUser.getString("notificationTypeCode");
//
//
//

                            if (walletOwner.has("profileImageName")){
                                MyApplication.saveString("ImageName", API.BASEURL+"ewallet/api/v1/fileUpload/download/" +
                                        userCode+"/"+
                                        walletOwner.optString("profileImageName"),verifyloginotpscreenC);
                            }
                            if(walletOwner.has("registerCountryName")){
                                MyApplication.saveString("COUNTRY_NAME_USERINFO", walletOwner.getString("registerCountryName"), verifyloginotpscreenC);
                                MyApplication.saveString("COUNTRYNAME_AGENT",walletOwner.getString("registerCountryName"),verifyloginotpscreenC);
                            }else{
                                MyApplication.saveString("COUNTRY_NAME_USERINFO", "", verifyloginotpscreenC);
                            }


                            if(walletOwner.has("registerCountryCode")){
                                MyApplication.saveString("COUNTRY_CODE_USERINFO", walletOwner.getString("registerCountryCode"), verifyloginotpscreenC);
                                MyApplication.saveString("COUNTRYCODE_AGENT",walletOwner.getString("registerCountryCode"),verifyloginotpscreenC);
                            }else{
                                MyApplication.saveString("COUNTRY_CODE_USERINFO", "", verifyloginotpscreenC);
                            }


//
//                            MyApplication.saveString("USERNAME", strPhoneNo, LoginMsis.this);
//                            MyApplication.saveString("PASSWORD", strPasword, LoginMsis.this);
//                            MyApplication.saveString("CODE_AGENT", CODE_AGENT, LoginMsis.this);
//                            MyApplication.saveString("EMAIL", EMAIL, LoginMsis.this);
//                            MyApplication.saveString("USERCODE", USERCODE, LoginMsis.this);
//                            MyApplication.saveString("NTTYPECODE", NTTYPECODE, LoginMsis.this);
//
//
//

                            if(walletOwner.has("ownerName")){
                                MyApplication.saveString("FIRSTNAME_USERINFO", walletOwner.getString("ownerName"), verifyloginotpscreenC);
                            }else{
                                MyApplication.saveString("FIRSTNAME_USERINFO", "", verifyloginotpscreenC);
                            }

                            if(walletOwner.has("lastName")){
                                MyApplication.saveString("LASTNAME_USERINFO", walletOwner.getString("lastName"), verifyloginotpscreenC);
                            }else{
                                MyApplication.saveString("LASTNAME_USERINFO", "", verifyloginotpscreenC);
                            }


                            sender_currency_api(walletOwner.getString("registerCountryCode"));


                        }




                        else
                        {
                            MyApplication.hideLoader();

                            Toast.makeText(verifyloginotpscreenC,resultDescription,Toast.LENGTH_LONG).show();


                        }



                    }
                    catch (Exception e)
                    {
                        MyApplication.hideLoader();
                        Toast.makeText(verifyloginotpscreenC,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }


                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();
                    MyApplication.showToast(verifyloginotpscreenC,aFalse);

                }
            });

        }catch (Exception e){

            MyApplication.hideLoader();
            MyApplication.showToast(verifyloginotpscreenC,e.toString());
        }

    }

    private void sender_currency_api(String countryName) {
        try{


            API.GET("ewallet/api/v1/countryCurrency/country/"+countryName,new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {


                        String resultCode =  jsonObject.getString("resultCode");
                        String resultDescription =  jsonObject.getString("resultDescription");

                        if(resultCode.equalsIgnoreCase("0")) {


                            JSONObject jsonObject_country = jsonObject.getJSONObject("country");


                            if(jsonObject_country.has("currencySymbol")){
                                MyApplication.saveString("currencySymbol_Loginpage", jsonObject_country.getString("currencySymbol"), verifyloginotpscreenC);
                            }else{
                                MyApplication.saveString("currencySymbol_Loginpage", "", verifyloginotpscreenC);
                            }


                            if(jsonObject_country.has("currencyCode")){
                                MyApplication.saveString("currencyCode_Loginpage", jsonObject_country.getString("currencyCode"), verifyloginotpscreenC);
                            }else{
                                MyApplication.saveString("currencyCode_Loginpage", "", verifyloginotpscreenC);
                            }

                            if(jsonObject_country.has("currencyRefCode")){
                                MyApplication.saveString("countryCode_Loginpage", jsonObject_country.getString("currencyRefCode"), verifyloginotpscreenC);
                            }else{
                                MyApplication.saveString("countryCode_Loginpage", "", verifyloginotpscreenC);
                            }


                            apiCallSecurity();

                        }




                        else
                        {

                            Toast.makeText(verifyloginotpscreenC,resultDescription,Toast.LENGTH_LONG).show();


                        }



                    }
                    catch (Exception e)
                    {
                        Toast.makeText(verifyloginotpscreenC,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }


                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();
                    MyApplication.showToast(verifyloginotpscreenC,aFalse);

                }
            });

        }catch (Exception e){

            MyApplication.hideLoader();
            MyApplication.showToast(verifyloginotpscreenC,e.toString());
        }

    }

    private void apiCallSecurity() {


        API.GET_CASHOUT_DETAILS("ewallet/api/v1/loginSecurity" , languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {


                    // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1789408\",\"requestTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"responseTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwnerUser\":{\"id\":2171,\"code\":\"101917\",\"firstName\":\"TATASnegal\",\"userName\":\"TATASnegal5597\",\"mobileNumber\":\"8888888882\",\"email\":\"kundan.kumar@esteltelecom.com\",\"walletOwnerUserTypeCode\":\"100000\",\"walletOwnerUserTypeName\":\"Supervisor\",\"walletOwnerCategoryCode\":\"100000\",\"walletOwnerCategoryName\":\"Institute\",\"userCode\":\"1000002606\",\"status\":\"Active\",\"state\":\"Approved\",\"gender\":\"M\",\"idProofTypeCode\":\"100004\",\"idProofTypeName\":\"MILITARY ID CARD\",\"idProofNumber\":\"44444444444\",\"creationDate\":\"2021-10-01T09:04:07.330+0530\",\"notificationName\":\"EMAIL\",\"notificationLanguage\":\"en\",\"createdBy\":\"100308\",\"modificationDate\":\"2021-10-20T14:59:00.791+0530\",\"modifiedBy\":\"100308\",\"addressList\":[{\"id\":3569,\"walletOwnerUserCode\":\"101917\",\"addTypeCode\":\"100001\",\"addTypeName\":\"Commercial\",\"regionCode\":\"100068\",\"regionName\":\"Boke\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"city\":\"100022\",\"cityName\":\"Dubreka\",\"addressLine1\":\"delhi\",\"status\":\"Inactive\",\"creationDate\":\"2021-10-01T09:04:07.498+0530\",\"createdBy\":\"100250\",\"modificationDate\":\"2021-10-03T09:52:57.407+0530\",\"modifiedBy\":\"100308\"}],\"workingDaysList\":[{\"id\":3597,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100000\",\"weekdaysName\":\"Monday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.518+0530\"},{\"id\":3598,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100001\",\"weekdaysName\":\"Tuesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.528+0530\"},{\"id\":3599,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100002\",\"weekdaysName\":\"Wednesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.538+0530\"},{\"id\":3600,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100003\",\"weekdaysName\":\"Thursday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.547+0530\"},{\"id\":3601,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100004\",\"weekdaysName\":\"Friday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.556+0530\"},{\"id\":3602,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100005\",\"weekdaysName\":\"Saturday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.565+0530\"},{\"id\":3603,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100006\",\"weekdaysName\":\"Sunday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"2:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.573+0530\"}],\"macEnabled\":false,\"ipEnabled\":false,\"resetCredReqInit\":false,\"notificationTypeCode\":\"100000\"}}");


                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {
                        Intent i = new Intent(verifyloginotpscreenC, MainActivity.class);
                        startActivity(i);
                        finish();


                    } else {
                        Toast.makeText(verifyloginotpscreenC, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(verifyloginotpscreenC, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(verifyloginotpscreenC, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


}

