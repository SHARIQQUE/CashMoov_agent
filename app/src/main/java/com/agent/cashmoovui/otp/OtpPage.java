package com.agent.cashmoovui.otp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.agent.cashmoovui.MainActivity;
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

public class OtpPage extends AppCompatActivity implements OnOtpCompletionListener {

    MyApplication applicationComponentClass;
    String languageToUse = "";
    OtpView otp_view;
    TextView otp_text;


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
        otp_text=findViewById(R.id.otp_text);
        otp_text.setText(getString(R.string.verification_login_otp));

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
                    ArrayList<ServiceList.serviceListMain> dataM=new ArrayList<>();

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


                    Intent i = new Intent(OtpPage.this, SetPin.class);
                    i.putExtra("CHECKINTENTSETPIN","VerifyLoginAccountScreen");
                    startActivity(i);
                    finish();
                   /* if( jsonObject.optString("pinLoginStatus").equalsIgnoreCase("Y")){
                        Intent i = new Intent(OtpPage.this, SetPin.class);
                        i.putExtra("CHECKINTENTSETPIN","VerifyLoginAccountScreen");
                        startActivity(i);
                        finish();
                    }else{
                        userInfo_api(jsonObject.optString("walletOwnerCode"));
                    }
*/

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
                                        walletOwner.optString("profileImageName"),OtpPage.this);
                            }
                            if(walletOwner.has("registerCountryName")){
                                MyApplication.saveString("COUNTRY_NAME_USERINFO", walletOwner.getString("registerCountryName"), OtpPage.this);
                                MyApplication.saveString("COUNTRYNAME_AGENT",walletOwner.getString("registerCountryName"),OtpPage.this);
                            }else{
                                MyApplication.saveString("COUNTRY_NAME_USERINFO", "", OtpPage.this);
                            }


                            if(walletOwner.has("registerCountryCode")){
                                MyApplication.saveString("COUNTRY_CODE_USERINFO", walletOwner.getString("registerCountryCode"), OtpPage.this);
                                MyApplication.saveString("COUNTRYCODE_AGENT",walletOwner.getString("registerCountryCode"),OtpPage.this);
                            }else{
                                MyApplication.saveString("COUNTRY_CODE_USERINFO", "", OtpPage.this);
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
                                MyApplication.saveString("FIRSTNAME_USERINFO", walletOwner.getString("ownerName"), OtpPage.this);
                            }else{
                                MyApplication.saveString("FIRSTNAME_USERINFO", "", OtpPage.this);
                            }

                            if(walletOwner.has("lastName")){
                                MyApplication.saveString("LASTNAME_USERINFO", walletOwner.getString("lastName"), OtpPage.this);
                            }else{
                                MyApplication.saveString("LASTNAME_USERINFO", "", OtpPage.this);
                            }


                            sender_currency_api(walletOwner.getString("registerCountryCode"));


                        }




                        else
                        {
                            MyApplication.hideLoader();

                            Toast.makeText(OtpPage.this,resultDescription,Toast.LENGTH_LONG).show();


                        }



                    }
                    catch (Exception e)
                    {
                        MyApplication.hideLoader();
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
                                MyApplication.saveString("currencySymbol_Loginpage", jsonObject_country.getString("currencySymbol"), OtpPage.this);
                            }else{
                                MyApplication.saveString("currencySymbol_Loginpage", "", OtpPage.this);
                            }


                            if(jsonObject_country.has("currencyCode")){
                                MyApplication.saveString("currencyCode_Loginpage", jsonObject_country.getString("currencyCode"), OtpPage.this);
                            }else{
                                MyApplication.saveString("currencyCode_Loginpage", "", OtpPage.this);
                            }

                            if(jsonObject_country.has("currencyRefCode")){
                                MyApplication.saveString("countryCode_Loginpage", jsonObject_country.getString("currencyRefCode"), OtpPage.this);
                            }else{
                                MyApplication.saveString("countryCode_Loginpage", "", OtpPage.this);
                            }


                            apiCallSecurity();

                        }




                        else
                        {

                            Toast.makeText(OtpPage.this,resultDescription,Toast.LENGTH_LONG).show();


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
                        Intent i = new Intent(OtpPage.this, MainActivity.class);
                        startActivity(i);
                        finish();


                    } else {
                        Toast.makeText(OtpPage.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(OtpPage.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(OtpPage.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


}
