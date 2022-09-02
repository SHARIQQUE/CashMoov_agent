package com.agent.cashmoovui.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.agent.cashmoovui.HiddenPassTransformationMethod;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.model.ServiceList;
import com.agent.cashmoovui.otp.OtpPage;
import com.agent.cashmoovui.otp.RESETPINOtpPage;
import com.agent.cashmoovui.otp.VerifyLoginAccountScreen;
import com.agent.cashmoovui.otp.VerifyLoginOTPScreen;
import com.agent.cashmoovui.transfer_float.CommissionTransfer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executor;


public class LoginMsis extends AppCompatActivity implements View.OnClickListener {

    MyApplication applicationComponentClass;
    String languageToUse = "";
    String strPhoneNo = "",strPasword;

    EditText et_phoneNo,et_password;
    TextView tv_continue,tv_pin,tv_Or,tv_fingurePrint;
    LinearLayout ll_password;
    String selectButtonType="0";

    boolean  isPasswordVisible;

    private static final int PERMISSION_REQUEST_CODE = 200,READ_EXTERNAL_STORAGE=201,WRITE_EXTERNAL_STORAGE=202;

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

        setContentView(R.layout.login_msis);

        et_phoneNo = (EditText) findViewById(R.id.et_phoneNo);
        et_password = (EditText)findViewById(R.id.et_password);
        tv_continue = (TextView)findViewById(R.id.tv_continue);



        tv_pin = (TextView)findViewById(R.id.tv_pin);
        tv_Or = (TextView)findViewById(R.id.tv_Or);
        tv_fingurePrint = (TextView)findViewById(R.id.tv_fingurePrint);


        tv_continue.setOnClickListener(this);
        tv_pin.setOnClickListener(this);
        tv_fingurePrint.setOnClickListener(this);

        ll_password = (LinearLayout) findViewById(R.id.ll_password);

        et_password.addTextChangedListener(new TextWatcher() {

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
                    MyApplication.hideKeyboard(LoginMsis.this);            }
        });


        HiddenPassTransformationMethod hiddenPassTransformationMethod=new HiddenPassTransformationMethod();
        et_password.setTransformationMethod(hiddenPassTransformationMethod);

        et_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (et_password.getRight() - et_password.getCompoundDrawables()[RIGHT].getBounds().width())) {
                        int selection = et_password.getSelectionEnd();
                        if (isPasswordVisible) {
                            // set drawable image
                            et_password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            // hide Password
                            et_password.setTransformationMethod(hiddenPassTransformationMethod);
                            isPasswordVisible = false;
                        } else  {
                            // set drawable image
                            et_password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                            // show Password
                            et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isPasswordVisible = true;
                        }
                        et_password.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });




        tv_pin.setVisibility(View.GONE);
        tv_fingurePrint.setVisibility(View.GONE);
        tv_fingurePrint.setVisibility(View.GONE);

        String firstRunApp = applicationComponentClass.getmSharedPreferences().getString("isFirstRun", "");

        if (firstRunApp.trim().length() == 0) {
            tv_pin.setVisibility(View.GONE);
        }

        else if (firstRunApp.equalsIgnoreCase("YES")) {
            tv_pin.setVisibility(View.GONE);
        }



        else if (firstRunApp.equalsIgnoreCase("NO")) {

            tv_pin.setVisibility(View.VISIBLE);

            Intent i = new Intent(LoginMsis.this, LoginPin.class);
            startActivity(i);
            finish();

        }

        else if (firstRunApp.equalsIgnoreCase("NO_LOGINPIN")) {
            tv_pin.setVisibility(View.VISIBLE);

            Intent i = new Intent(LoginMsis.this, LoginPin.class);
            startActivity(i);
            finish();
        }



        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(LoginMsis.this);
        switch (biometricManager.canAuthenticate()) {

            // this means we can use biometric sensor
            case BiometricManager.BIOMETRIC_SUCCESS:

                // msgText.setText("You can use the fingerprint sensor to login");
                // msgText.setTextColor(Color.parseColor("#fafafa"));
                break;

            // this means that the device doesn't have fingerprint sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                MyApplication.showErrorToast(this,getString(R.string.no_fingerprint_senser));

                tv_fingurePrint.setVisibility(View.GONE);
                break;

            // this means that biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:

                MyApplication.showErrorToast(this,getString(R.string.no_biometric_senser));

                tv_fingurePrint.setVisibility(View.GONE);
                break;

            // this means that the device doesn't contain your fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:

                MyApplication.showErrorToast(this,getString(R.string.device_not_contain_fingerprint));

                tv_fingurePrint.setVisibility(View.GONE);
                break;
        }
        // creating a variable for our Executor
        Executor executor = ContextCompat.getMainExecutor(this);
        // this will give us result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt(LoginMsis.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                //  Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                // tvFinger.setText("Login Successful");
                Intent intent = new Intent(LoginMsis.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        // creating a variable for our promptInfo
        // BIOMETRIC DIALOG
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("CASHMOOV")
                .setDescription(getString(R.string.use_finger_to_login)).setNegativeButtonText(getString(R.string.cancel)).build();
        tv_fingurePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);

            }
        });





    }

    String FCM_TOKEN;
    @Override
    protected void onStart() {
        super.onStart();
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

    }


    boolean validation_mobile_Details()
    {

        strPhoneNo = et_phoneNo.getText().toString();

        if(strPhoneNo.isEmpty()) {
            MyApplication.showErrorToast(LoginMsis.this,getString(R.string.val_phone));

            return false;
        }

        else if(strPhoneNo.length() < 9) {

            MyApplication.showErrorToast(LoginMsis.this,getString(R.string.val_phone));

            return false;
        }



        return true;
    }


    boolean validation_password_Details()
    {

        strPasword = et_password.getText().toString();

        if(strPasword.isEmpty()) {

            MyApplication.showErrorToast(LoginMsis.this, getString(R.string.please_enter_4_digit_mpin));

            return false;
        }

        else if(strPasword.length() == 4) {


            return true;
        }
        else {
            MyApplication.showErrorToast(LoginMsis.this, getString(R.string.please_enter_4_digit_mpin));

            return false;
        }


    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tv_continue:
            {

                if(selectButtonType.equalsIgnoreCase("0")) {

                    if (validation_mobile_Details()) {
                        if (new InternetCheck().isConnected(LoginMsis.this)) {






                            if (checkPermission_camera()) {

                                if (checkPermission_read_external_storage()) {


                                    //  if (checkPermission_write_external_storage()) {

                                    MyApplication.showloader(LoginMsis.this, getString(R.string.getting_user_info));

                                    mssidn_api();


//                                    } else {
//                                        requestPermission_write_external_storage();
//                                    }
                                } else {
                                    requestPermission_read_external_storage();
                                }
                            } else {
                                requestPermission_camera();
                            }

                        } else {
                            Toast.makeText(LoginMsis.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else
                {
                    if (validation_password_Details()) {
                        if (new InternetCheck().isConnected(LoginMsis.this)) {
                            MyApplication.showloader(LoginMsis.this, getString(R.string.getting_user_info));

                            token_api();

                        }
                        else {
                            Toast.makeText(LoginMsis.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                        }
                    }
                }



            }
            break;

            case R.id.tv_pin:
            {
                Intent i = new Intent(LoginMsis.this, LoginPin.class);
                startActivity(i);
            }
            break;


        }
    }
    private void all_api() {
        try{


            API.GET_ALL("ewallet/public/notificationType/all/",languageToUse,new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {

                        // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1772020\",\"requestTime\":\"Mon Oct 18 18:19:09 IST 2021\",\"responseTime\":\"Mon Oct 18 18:19:09 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwnerUser\":{\"id\":2215,\"code\":\"101961\",\"firstName\":\"EstelPune\",\"userName\":\"EstelPune0370\",\"mobileNumber\":\"5555555556\",\"email\":\"sharique9718@gmail.com\",\"walletOwnerUserTypeCode\":\"100000\",\"walletOwnerUserTypeName\":\"Supervisor\",\"walletOwnerCategoryCode\":\"100000\",\"walletOwnerCategoryName\":\"Institute\",\"userCode\":\"1000002666\",\"status\":\"Active\",\"state\":\"Approved\",\"gender\":\"M\",\"idProofTypeCode\":\"100001\",\"idProofTypeName\":\"NATIONAL IDENTITY CARD\",\"idProofNumber\":\"AD12334\",\"creationDate\":\"2021-10-09T23:05:59.169+0530\",\"notificationName\":\"EMAIL\",\"notificationLanguage\":\"en\",\"createdBy\":\"100308\",\"modificationDate\":\"2021-10-18T18:15:10.865+0530\",\"modifiedBy\":\"100250\",\"macEnabled\":false,\"ipEnabled\":false,\"resetCredReqInit\":true,\"notificationTypeCode\":\"100000\"}}");


                        String resultCode =  jsonObject.getString("resultCode");
                        String resultDescription =  jsonObject.getString("resultDescription");

                        if(resultCode.equalsIgnoreCase("0"))
                        {


                        }

                        else
                        {
                            Toast.makeText(LoginMsis.this,resultDescription,Toast.LENGTH_LONG).show();

                        }



                    }
                    catch (Exception e)
                    {
                        Toast.makeText(LoginMsis.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }


                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();
                    MyApplication.showToast(LoginMsis.this,aFalse);

                }
            });

        }catch (Exception e){

            MyApplication.hideLoader();
            MyApplication.showToast(LoginMsis.this,e.toString());
        }

    }

    private void mssidn_api() {
        try{


            API.GET_MSDIN("ewallet/public/walletOwner/msisdn/"+strPhoneNo,languageToUse,new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {


                    try {

                        //  JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1725124\",\"requestTime\":\"Mon Oct 11 18:06:30 IST 2021\",\"responseTime\":\"Mon Oct 11 18:06:30 IST 2021\",\"firstLoginStatus\":\"N\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwnerUser\":{\"id\":1728,\"code\":\"101458\",\"firstName\":\"Mahendra\",\"userName\":\"Mahendra2355\",\"mobileNumber\":\"989898899\",\"email\":\"tarun.kumar@esteltelecom.com\",\"walletOwnerUserTypeCode\":\"100000\",\"walletOwnerUserTypeName\":\"Supervisor\",\"walletOwnerCategoryCode\":\"100000\",\"walletOwnerCategoryName\":\"Institute\",\"userCode\":\"1000002094\",\"status\":\"Active\",\"state\":\"Approved\",\"gender\":\"M\",\"dateOfBirth\":\"1960-01-15\",\"idProofTypeCode\":\"100005\",\"idProofTypeName\":\"COMPANY REGISTRATION NUMBER\",\"idProofNumber\":\"1111111111111111\",\"issuingCountryCode\":\"100102\",\"issuingCountryName\":\"India\",\"idExpiryDate\":\"2021-06-23\",\"creationDate\":\"2021-06-08T14:53:57.853+0530\",\"notificationName\":\"EMAIL\",\"notificationLanguage\":\"en\",\"createdBy\":\"100250\",\"modificationDate\":\"2021-10-11T16:18:02.389+0530\",\"modifiedBy\":\"100250\",\"macEnabled\":false,\"ipEnabled\":false,\"resetCredReqInit\":false,\"notificationTypeCode\":\"100000\"}}");


                        String resultCode =  jsonObject.getString("resultCode");
                        String resultDescription =  jsonObject.getString("resultDescription");

                        if(resultCode.equalsIgnoreCase("0"))
                        {


                            JSONObject walletOwnerUser =  jsonObject.getJSONObject("walletOwnerUser");

                            String EMAIL = walletOwnerUser.getString("email");
                            String USERCODE = walletOwnerUser.getString("userCode");
                            String CODE_AGENT = walletOwnerUser.getString("code");
                            String NTTYPECODE = walletOwnerUser.getString("notificationTypeCode");
                            String firstLoginStatus="Y";
                            if(walletOwnerUser.has("pinLoginStatus")) {
                                 firstLoginStatus = walletOwnerUser.getString("pinLoginStatus");
                            }else{
                                 firstLoginStatus = walletOwnerUser.getString("firstLoginStatus");
                            }

                            firstLoginStatus = walletOwnerUser.getString("firstLoginStatus");

                            //  String countryCode_agent = walletOwnerUser.getString("issuingCountryCode");
                            // String countryName_agent = walletOwnerUser.getString("issuingCountryName");

                            MyApplication.saveString("COUNTRYNAME_AGENT","",LoginMsis.this);
                            MyApplication.saveString("COUNTRYCODE_AGENT","",LoginMsis.this);
                           /* String countryCode_agent = walletOwnerUser.getString("issuingCountryCode");
                            String countryName_agent = walletOwnerUser.getString("issuingCountryName");*/
                            // MyApplication.getSaveString("COUNTRYCODE_AGENT"
                            MyApplication.saveString("COUNTRYNAME_AGENT","",LoginMsis.this);
                            MyApplication.saveString("COUNTRYCODE_AGENT","",LoginMsis.this);


                            MyApplication.saveString("USERNAME",strPhoneNo,LoginMsis.this);
                            MyApplication.saveString("PASSWORD",strPasword,LoginMsis.this);
                            MyApplication.saveString("CODE_AGENT",CODE_AGENT,LoginMsis.this);
                            MyApplication.saveString("EMAIL",EMAIL,LoginMsis.this);
                            MyApplication.saveString("USERCODE",USERCODE,LoginMsis.this);
                            MyApplication.saveString("NTTYPECODE",NTTYPECODE,LoginMsis.this);
                            MyApplication.hideLoader();

                            if(walletOwnerUser.optBoolean("reSetPinCredRequest")) {
                                Intent i = new Intent(LoginMsis.this, RESETPINOtpPage.class);
                                startActivity(i);
                                return;
                            }

                            if (firstLoginStatus.equalsIgnoreCase("Y"))
                            {
                                Intent i = new Intent(LoginMsis.this, OtpPage.class);
                                startActivity(i);
                                return;
                                // finish();
                            }

                             if (firstLoginStatus.equalsIgnoreCase("N")) {

                                ll_password.setVisibility(View.VISIBLE);
                                selectButtonType = "1";

                            }

                            else {
                                 MyApplication.hideLoader();

                                 Toast.makeText(LoginMsis.this, firstLoginStatus, Toast.LENGTH_LONG).show();
                                finish();
                            }




                        }

                        else
                        {
                          //  selectButtonType = "0";
                            MyApplication.hideLoader();

                            Toast.makeText(LoginMsis.this,resultDescription,Toast.LENGTH_LONG).show();


                        }



                    }
                    catch (Exception e)
                    {
                        MyApplication.hideLoader();

                        Toast.makeText(LoginMsis.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }


                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();
                    MyApplication.showToast(LoginMsis.this,aFalse);

                }
            });

        }catch (Exception e){

            MyApplication.hideLoader();
            MyApplication.showToast(LoginMsis.this,e.toString());
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
                                        walletOwner.optString("profileImageName"),LoginMsis.this);
                            }
                            if(walletOwner.has("registerCountryName")){
                                MyApplication.saveString("COUNTRY_NAME_USERINFO", walletOwner.getString("registerCountryName"), LoginMsis.this);
                                MyApplication.saveString("COUNTRYNAME_AGENT",walletOwner.getString("registerCountryName"),LoginMsis.this);
                            }else{
                                MyApplication.saveString("COUNTRY_NAME_USERINFO", "", LoginMsis.this);
                            }


                            if(walletOwner.has("registerCountryCode")){
                                MyApplication.saveString("COUNTRY_CODE_USERINFO", walletOwner.getString("registerCountryCode"), LoginMsis.this);
                                MyApplication.saveString("COUNTRYCODE_AGENT",walletOwner.getString("registerCountryCode"),LoginMsis.this);
                            }else{
                                MyApplication.saveString("COUNTRY_CODE_USERINFO", "", LoginMsis.this);
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
                                MyApplication.saveString("FIRSTNAME_USERINFO", walletOwner.getString("ownerName"), LoginMsis.this);
                            }else{
                                MyApplication.saveString("FIRSTNAME_USERINFO", "", LoginMsis.this);
                            }

                            if(walletOwner.has("lastName")){
                                MyApplication.saveString("LASTNAME_USERINFO", walletOwner.getString("lastName"), LoginMsis.this);
                            }else{
                                MyApplication.saveString("LASTNAME_USERINFO", "", LoginMsis.this);
                            }


                            sender_currency_api(walletOwner.getString("registerCountryCode"));


                        }




                        else
                        {
                            MyApplication.hideLoader();
                            selectButtonType = "0";

                            Toast.makeText(LoginMsis.this,resultDescription,Toast.LENGTH_LONG).show();


                        }



                    }
                    catch (Exception e)
                    {
                        MyApplication.hideLoader();
                        Toast.makeText(LoginMsis.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }


                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();
                    MyApplication.showToast(LoginMsis.this,aFalse);

                }
            });

        }catch (Exception e){

            MyApplication.hideLoader();
            MyApplication.showToast(LoginMsis.this,e.toString());
        }

    }

    private void sender_currency_api(String countryName) {
        try{


            API.GET("ewallet/api/v1/countryCurrency/country/"+countryName,new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {


                    try {


                        String resultCode =  jsonObject.getString("resultCode");
                        String resultDescription =  jsonObject.getString("resultDescription");

                        if(resultCode.equalsIgnoreCase("0")) {


                            JSONObject jsonObject_country = jsonObject.getJSONObject("country");


                            if(jsonObject_country.has("currencySymbol")){
                                MyApplication.saveString("currencySymbol_Loginpage", jsonObject_country.getString("currencySymbol"), LoginMsis.this);
                            }else{
                                MyApplication.saveString("currencySymbol_Loginpage", "", LoginMsis.this);
                            }


                         if(jsonObject_country.has("currencyCode")){
                                MyApplication.saveString("currencyCode_Loginpage", jsonObject_country.getString("currencyCode"), LoginMsis.this);
                            }else{
                                MyApplication.saveString("currencyCode_Loginpage", "", LoginMsis.this);
                            }

                            if(jsonObject_country.has("currencyRefCode")){
                                MyApplication.saveString("countryCode_Loginpage", jsonObject_country.getString("currencyRefCode"), LoginMsis.this);
                            }else{
                                MyApplication.saveString("countryCode_Loginpage", "", LoginMsis.this);
                            }


                           api_walletOwnerUser();

                        }




                        else
                        {
                            selectButtonType = "0";
                            MyApplication.hideLoader();

                            Toast.makeText(LoginMsis.this,resultDescription,Toast.LENGTH_LONG).show();


                        }



                    }
                    catch (Exception e)
                    {
                        MyApplication.hideLoader();

                        Toast.makeText(LoginMsis.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }


                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();
                    MyApplication.showToast(LoginMsis.this,aFalse);

                }
            });

        }catch (Exception e){

            MyApplication.hideLoader();
            MyApplication.showToast(LoginMsis.this,e.toString());
        }

    }


    private void token_api() {
        try{
            strPhoneNo = et_phoneNo.getText().toString();
            JSONObject jsonObject=new JSONObject();

            //  jsonObject.put("scope","read write");
            jsonObject.put("username",strPhoneNo);
            jsonObject.put("password",strPasword);
            jsonObject.put("grant_type","password");
            jsonObject.put("scope","read write");
            jsonObject.put("fcmToken",FCM_TOKEN);
            jsonObject.put("country",MyApplication.getSaveString("COUNTRY",LoginMsis.this));
            jsonObject.put("cc",MyApplication.getSaveString("CC",LoginMsis.this));


            MyApplication.saveString("USERNAME",strPhoneNo,LoginMsis.this);




            API.POST_REQEST_LOGIN_TOKEN("ewallet/oauth/token",jsonObject,new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    //MyApplication.hideLoader();

                    try {

                        //    JSONObject jsonObject = new JSONObject("{\"access_token\":\"7c666217-fe41-4b25-9e32-c27211c76f2f\",\"token_type\":\"bearer\",\"expires_in\":43189,\"scope\":\"read write\",\"lastName\":null,\"walletOwnerCategoryCode\":\"100000\",\"passwordExpiry\":\"2021-09-06T00:00:00.000+0530\",\"mobile\":\"989898899\",\"locale\":\"en\",\"userCountryCode\":\"100092\",\"userCode\":\"101458\",\"walletOwnerUserTypeCode\":\"100000\",\"idProffTypeName\":\"COMPANY REGISTRATION NUMBER\",\"idProffNumber\":\"1111111111111111\",\"firstName\":\"Mahendra\",\"firstLoginStatus\":\"N\",\"serviceList\":[{\"id\":1,\"code\":\"100000\",\"name\":\"Money Transfer\",\"status\":\"Active\",\"creationDate\":\"1598353606641\",\"serviceCategoryList\":[{\"id\":17,\"code\":\"100016\",\"serviceCode\":\"100000\",\"serviceName\":\"Money Transfer\",\"name\":\"Sell Float\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2020-09-18T11:31:46.841+0530\",\"modificationDate\":\"2021-06-20T12:43:35.764+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"walletOwnerCateList\":[\"100000\"],\"overdraftAllowed\":false},{\"id\":18,\"code\":\"100017\",\"serviceCode\":\"100000\",\"serviceName\":\"Money Transfer\",\"name\":\"Transfer Float\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2020-09-18T11:31:46.841+0530\",\"modificationDate\":\"2021-07-06T15:16:56.152+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":true,\"loyaltyAllowed\":false,\"walletOwnerCateList\":[\"100002\",\"100001\",\"100000\"],\"overdraftAllowed\":false}]},{\"id\":2,\"code\":\"100001\",\"name\":\"Recharge & Payment\",\"status\":\"Active\",\"creationDate\":\"1598361952197\",\"serviceCategoryList\":[{\"id\":29,\"code\":\"100028\",\"serviceCode\":\"100001\",\"serviceName\":\"Recharge & Payment\",\"name\":\"TV\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":true,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2020-11-27T13:36:44.602+0530\",\"modificationDate\":\"2021-08-24T20:26:20.708+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"walletOwnerCateList\":[\"100011\",\"100010\",\"100012\",\"100002\",\"100001\",\"100000\"],\"minTransValue\":1,\"maxTransValue\":100000000,\"overdraftAllowed\":false}]},{\"id\":3,\"code\":\"100002\",\"name\":\"Remittance\",\"status\":\"Active\",\"creationDate\":\"1598362002854\",\"serviceCategoryList\":[{\"id\":19,\"code\":\"100018\",\"serviceCode\":\"100002\",\"serviceName\":\"Remittance\",\"name\":\"Receive Remittance\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2020-09-18T11:31:46.841+0530\",\"modificationDate\":\"2021-06-18T19:08:06.024+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"walletOwnerCateList\":[\"100002\",\"100001\",\"100000\"],\"minTransValue\":100,\"maxTransValue\":200000,\"overdraftAllowed\":false},{\"id\":85,\"code\":\"100061\",\"serviceCode\":\"100002\",\"serviceName\":\"Remittance\",\"name\":\"Cash to Wallet\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2021-02-22T11:40:54.468+0530\",\"modificationDate\":\"2021-06-20T12:46:00.118+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"walletOwnerCateList\":[\"100002\",\"100001\",\"100000\"],\"overdraftAllowed\":false},{\"id\":2,\"code\":\"100001\",\"serviceCode\":\"100002\",\"serviceName\":\"Remittance\",\"name\":\"Send Remittance\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2020-08-25T23:54:07.573+0530\",\"modificationDate\":\"2021-06-18T19:02:13.088+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"walletOwnerCateList\":[\"100002\",\"100001\",\"100000\"],\"minTransValue\":1,\"maxTransValue\":100000,\"overdraftAllowed\":false}]},{\"id\":4,\"code\":\"100003\",\"name\":\"Cash\",\"status\":\"Active\",\"creationDate\":\"1598362025788\",\"serviceCategoryList\":[{\"id\":13,\"code\":\"100012\",\"serviceCode\":\"100003\",\"serviceName\":\"Cash\",\"name\":\"Cash Out\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2020-09-18T11:31:46.841+0530\",\"modificationDate\":\"2021-07-12T13:07:15.880+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"walletOwnerCateList\":[\"100002\",\"100001\",\"100000\",\"100010\"],\"overdraftAllowed\":false},{\"id\":12,\"code\":\"100011\",\"serviceCode\":\"100003\",\"serviceName\":\"Cash\",\"name\":\"Cash In\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2020-09-18T11:31:46.841+0530\",\"modificationDate\":\"2021-06-19T18:51:43.444+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"walletOwnerCateList\":[\"100002\",\"100001\",\"100000\"],\"minTransValue\":1000,\"maxTransValue\":10000000,\"overdraftAllowed\":false}]},{\"id\":10,\"code\":\"100009\",\"name\":\"Airtime Purchase\",\"status\":\"Active\",\"creationDate\":\"1605870076878\",\"serviceCategoryList\":[{\"id\":22,\"code\":\"100021\",\"serviceCode\":\"100009\",\"serviceName\":\"Airtime Purchase\",\"name\":\"Mobile Prepaid\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":true,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2020-11-20T16:36:10.202+0530\",\"modificationDate\":\"2021-09-19T19:33:15.127+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":true,\"loyaltyAllowed\":false,\"walletOwnerCateList\":[\"100010\",\"100011\",\"100002\",\"100000\",\"100001\"],\"overdraftAllowed\":false}]},{\"id\":14,\"code\":\"100013\",\"name\":\"Wallet Owner Management\",\"status\":\"Active\",\"creationDate\":\"1609321143274\",\"serviceCategoryList\":[{\"id\":74,\"code\":\"WLTOWR\",\"serviceCode\":\"100013\",\"serviceName\":\"Wallet Owner Management\",\"name\":\"Wallet Owner\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2021-01-05T18:54:35.157+0530\",\"modificationDate\":\"2021-01-07T13:29:53.195+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"overdraftAllowed\":false},{\"id\":75,\"code\":\"SUBS\",\"serviceCode\":\"100013\",\"serviceName\":\"Wallet Owner Management\",\"name\":\"Subscriber\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2021-01-05T18:55:09.344+0530\",\"modificationDate\":\"2021-09-19T19:31:51.568+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":true,\"loyaltyAllowed\":false,\"walletOwnerCateList\":[\"100000\",\"100002\",\"100001\"],\"overdraftAllowed\":false}]},{\"id\":17,\"code\":\"100016\",\"name\":\"Report\",\"status\":\"Active\",\"creationDate\":\"1609933314114\",\"serviceCategoryList\":[{\"id\":79,\"code\":\"TRNSRT\",\"serviceCode\":\"100016\",\"serviceName\":\"Report\",\"name\":\"Remittance Details\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":true,\"serviceCountryList\":[{\"code\":\"100092\",\"name\":\"Guinea\"}],\"creationDate\":\"2021-02-15T12:29:06.081+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"minTransValue\":1,\"maxTransValue\":50000,\"overdraftAllowed\":false},{\"id\":77,\"code\":\"TRNSDT\",\"serviceCode\":\"100016\",\"serviceName\":\"Report\",\"name\":\"Transaction Details\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2021-01-06T17:14:32.686+0530\",\"modificationDate\":\"2021-01-07T13:29:36.444+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"overdraftAllowed\":false}]},{\"id\":18,\"code\":\"100017\",\"name\":\"Overdraft\",\"status\":\"Active\",\"creationDate\":\"1614930496411\",\"serviceCategoryList\":[{\"id\":86,\"code\":\"ODLMT\",\"serviceCode\":\"100017\",\"serviceName\":\"Overdraft\",\"name\":\"Overdraft Limit\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2021-03-09T14:09:48.865+0530\",\"modificationDate\":\"2021-06-28T17:02:02.594+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"walletOwnerCateList\":[\"100002\",\"100001\",\"100000\"],\"minTransValue\":1,\"maxTransValue\":50000,\"overdraftAllowed\":false},{\"id\":92,\"code\":\"ODTRF\",\"serviceCode\":\"100017\",\"serviceName\":\"Overdraft\",\"name\":\"Overdraft Transfer\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2021-06-11T15:31:25.816+0530\",\"modificationDate\":\"2021-06-11T15:32:17.208+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"overdraftAllowed\":false}]},{\"id\":22,\"code\":\"100021\",\"name\":\"Commission Transfer\",\"status\":\"Active\",\"creationDate\":\"1629179547641\",\"serviceCategoryList\":[{\"id\":94,\"code\":\"COMTRF\",\"serviceCode\":\"100021\",\"serviceName\":\"Commission Transfer\",\"name\":\"Commission Transfer\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2021-08-17T11:22:31.268+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"minTransValue\":1,\"maxTransValue\":100000,\"overdraftAllowed\":false}]}],\"idProffTypeCode\":\"100005\",\"issuingCountryCode\":\"100102\",\"email\":\"tarun.kumar@esteltelecom.com\",\"walletOwnerCode\":\"1000002094\",\"username\":\"Mahendra2355\",\"issuingCountryName\":\"India\"}");

                        if (jsonObject.has("error")) {

                            MyApplication.hideLoader();
                            //  String error_message = jsonObject.getString("error_message");
                            //  Toast.makeText(LoginMsis.this, error_message, Toast.LENGTH_LONG).show();

                            if (jsonObject.getString("error").equalsIgnoreCase("1251")) {

                                Intent i = new Intent(LoginMsis.this, VerifyLoginOTPScreen.class);
                                i.putExtra("ERROR1251","1251");
                                startActivity(i);

                            }

                            else if (jsonObject.getString("error_message").equalsIgnoreCase("Invalid credentials")) {

                                Toast.makeText(LoginMsis.this, "Invalid pin", Toast.LENGTH_LONG).show();

                            }

                            else if (jsonObject.getString("error_message").equalsIgnoreCase("User Not Found")) {


                                Toast.makeText(LoginMsis.this, "User Not Found", Toast.LENGTH_LONG).show();

                            }else{
                                Toast.makeText(LoginMsis.this, jsonObject.getString("error_message"), Toast.LENGTH_LONG).show();
                            }
                        }

                        else {

                            ArrayList<ServiceList.serviceListMain> dataM=new ArrayList<>();

                                MyApplication.saveString("token", jsonObject.optString("access_token"), LoginMsis.this);


                                System.out.println("Login response=======" + jsonObject.toString());

                                MyApplication.saveString("pin", strPasword, LoginMsis.this);

                                MyApplication.saveString("token", jsonObject.optString("access_token"), LoginMsis.this);
                                MyApplication.saveString("firstName", jsonObject.optString("firstName"), LoginMsis.this);
                                MyApplication.saveString("lastName", jsonObject.optString("lastName"), LoginMsis.this);
                                MyApplication.saveString("email", jsonObject.optString("email"), LoginMsis.this);
                                MyApplication.saveString("mobile", jsonObject.optString("mobile"), LoginMsis.this);
                                MyApplication.saveString("walletOwnerCategoryCode", jsonObject.optString("walletOwnerCategoryCode"), LoginMsis.this);
                                MyApplication.saveString("walletOwnerCode", jsonObject.optString("walletOwnerCode"), LoginMsis.this);
                                MyApplication.saveString("userCountryCode", jsonObject.optString("userCountryCode"), LoginMsis.this);
                                MyApplication.saveString("userCode", jsonObject.optString("userCode"), LoginMsis.this);
                                MyApplication.saveString("username", jsonObject.optString("username"), LoginMsis.this);
                                MyApplication.saveString("userCountryCode", jsonObject.optString("userCountryCode"), LoginMsis.this);
                                MyApplication.saveString("issuingCountryName", jsonObject.optString("issuingCountryName"), LoginMsis.this);
                            MyApplication.saveString("userCode", jsonObject.optString("userCode"), LoginMsis.this);
                                // #################### serviceList Add  serviceCategoryList add  serviceCode

                                if (jsonObject.has("serviceList") &&jsonObject.optJSONArray("serviceList")!=null) {
                                    JSONArray jsonArray_serviceList = jsonObject.getJSONArray("serviceList");

                                    for (int i = 0; i < jsonArray_serviceList.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray_serviceList.getJSONObject(i);

                                        if (jsonObject1.has("serviceCategoryList")&&jsonObject1.optJSONArray("serviceCategoryList")!=null) {
                                            JSONArray jsonArray_serviceCategoryList = jsonObject1.getJSONArray("serviceCategoryList");
                                            for (int j = 0; j < jsonArray_serviceCategoryList.length(); j++) {
                                                JSONObject jsonObject2 = jsonArray_serviceCategoryList.getJSONObject(j);

                                                if (jsonObject2.has("serviceName")) {
                                                    String serviceName = jsonObject2.getString("serviceName");

                                                    if (serviceName.equalsIgnoreCase("Money Transfer")) {
                                                        if (jsonObject2.has("serviceCode")) {
                                                            String serviceCode = jsonObject2.getString("serviceCode");

                                                            MyApplication.saveString("serviceCode_LoginApi", serviceCode, LoginMsis.this);
                                                        } else {
                                                            MyApplication.saveString("serviceCode_LoginApi", "", LoginMsis.this); // not coming from server

                                                        }
                                                    }

                                                }
                                            }

                                        }

                                    }

                                } else {
                                    MyApplication.saveString("serviceCode_LoginApi", "", LoginMsis.this); // not coming from server

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

                            if (jsonObject.optBoolean("loginWithOtpRequired")) {
                                Intent i = new Intent(LoginMsis.this, VerifyLoginOTPScreen.class);
                                startActivity(i);
                                finish();
                            } else {
                                userInfo_api(jsonObject.optString("walletOwnerCode"));
                            }

                        }
                    }

                    catch (Exception e)
                    {
                        Toast.makeText(LoginMsis.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();
                    //  MyApplication.showToast(LoginMsis.this,aFalse);

                }
            });

        }catch (Exception e){

            MyApplication.hideLoader();
            MyApplication.showToast(LoginMsis.this,e.toString());
        }

    }


    private void token_api_old() {
        try{

            JSONObject jsonObject=new JSONObject();
            jsonObject.put("fcmToken",FCM_TOKEN);
            //  jsonObject.put("scope","read write");
            jsonObject.put("username",strPhoneNo);
            jsonObject.put("password",strPasword);
            jsonObject.put("grant_type","password");
            jsonObject.put("scope","read write");
            jsonObject.put("country",MyApplication.getSaveString("COUNTRY",LoginMsis.this));
            jsonObject.put("cc",MyApplication.getSaveString("CC",LoginMsis.this));

            //  applicationComponentClass.getmSharedPreferences().edit().putString("USERNAME", strPhoneNo).commit();
            //  applicationComponentClass.getmSharedPreferences().edit().putString("PASSWORD", strPasword).commit();




            API.POST_REQEST_LOGIN_TOKEN("ewallet/oauth/token",jsonObject,new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {

                        //    JSONObject jsonObject = new JSONObject("{\"access_token\":\"7c666217-fe41-4b25-9e32-c27211c76f2f\",\"token_type\":\"bearer\",\"expires_in\":43189,\"scope\":\"read write\",\"lastName\":null,\"walletOwnerCategoryCode\":\"100000\",\"passwordExpiry\":\"2021-09-06T00:00:00.000+0530\",\"mobile\":\"989898899\",\"locale\":\"en\",\"userCountryCode\":\"100092\",\"userCode\":\"101458\",\"walletOwnerUserTypeCode\":\"100000\",\"idProffTypeName\":\"COMPANY REGISTRATION NUMBER\",\"idProffNumber\":\"1111111111111111\",\"firstName\":\"Mahendra\",\"firstLoginStatus\":\"N\",\"serviceList\":[{\"id\":1,\"code\":\"100000\",\"name\":\"Money Transfer\",\"status\":\"Active\",\"creationDate\":\"1598353606641\",\"serviceCategoryList\":[{\"id\":17,\"code\":\"100016\",\"serviceCode\":\"100000\",\"serviceName\":\"Money Transfer\",\"name\":\"Sell Float\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2020-09-18T11:31:46.841+0530\",\"modificationDate\":\"2021-06-20T12:43:35.764+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"walletOwnerCateList\":[\"100000\"],\"overdraftAllowed\":false},{\"id\":18,\"code\":\"100017\",\"serviceCode\":\"100000\",\"serviceName\":\"Money Transfer\",\"name\":\"Transfer Float\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2020-09-18T11:31:46.841+0530\",\"modificationDate\":\"2021-07-06T15:16:56.152+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":true,\"loyaltyAllowed\":false,\"walletOwnerCateList\":[\"100002\",\"100001\",\"100000\"],\"overdraftAllowed\":false}]},{\"id\":2,\"code\":\"100001\",\"name\":\"Recharge & Payment\",\"status\":\"Active\",\"creationDate\":\"1598361952197\",\"serviceCategoryList\":[{\"id\":29,\"code\":\"100028\",\"serviceCode\":\"100001\",\"serviceName\":\"Recharge & Payment\",\"name\":\"TV\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":true,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2020-11-27T13:36:44.602+0530\",\"modificationDate\":\"2021-08-24T20:26:20.708+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"walletOwnerCateList\":[\"100011\",\"100010\",\"100012\",\"100002\",\"100001\",\"100000\"],\"minTransValue\":1,\"maxTransValue\":100000000,\"overdraftAllowed\":false}]},{\"id\":3,\"code\":\"100002\",\"name\":\"Remittance\",\"status\":\"Active\",\"creationDate\":\"1598362002854\",\"serviceCategoryList\":[{\"id\":19,\"code\":\"100018\",\"serviceCode\":\"100002\",\"serviceName\":\"Remittance\",\"name\":\"Receive Remittance\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2020-09-18T11:31:46.841+0530\",\"modificationDate\":\"2021-06-18T19:08:06.024+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"walletOwnerCateList\":[\"100002\",\"100001\",\"100000\"],\"minTransValue\":100,\"maxTransValue\":200000,\"overdraftAllowed\":false},{\"id\":85,\"code\":\"100061\",\"serviceCode\":\"100002\",\"serviceName\":\"Remittance\",\"name\":\"Cash to Wallet\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2021-02-22T11:40:54.468+0530\",\"modificationDate\":\"2021-06-20T12:46:00.118+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"walletOwnerCateList\":[\"100002\",\"100001\",\"100000\"],\"overdraftAllowed\":false},{\"id\":2,\"code\":\"100001\",\"serviceCode\":\"100002\",\"serviceName\":\"Remittance\",\"name\":\"Send Remittance\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2020-08-25T23:54:07.573+0530\",\"modificationDate\":\"2021-06-18T19:02:13.088+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"walletOwnerCateList\":[\"100002\",\"100001\",\"100000\"],\"minTransValue\":1,\"maxTransValue\":100000,\"overdraftAllowed\":false}]},{\"id\":4,\"code\":\"100003\",\"name\":\"Cash\",\"status\":\"Active\",\"creationDate\":\"1598362025788\",\"serviceCategoryList\":[{\"id\":13,\"code\":\"100012\",\"serviceCode\":\"100003\",\"serviceName\":\"Cash\",\"name\":\"Cash Out\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2020-09-18T11:31:46.841+0530\",\"modificationDate\":\"2021-07-12T13:07:15.880+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"walletOwnerCateList\":[\"100002\",\"100001\",\"100000\",\"100010\"],\"overdraftAllowed\":false},{\"id\":12,\"code\":\"100011\",\"serviceCode\":\"100003\",\"serviceName\":\"Cash\",\"name\":\"Cash In\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2020-09-18T11:31:46.841+0530\",\"modificationDate\":\"2021-06-19T18:51:43.444+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"walletOwnerCateList\":[\"100002\",\"100001\",\"100000\"],\"minTransValue\":1000,\"maxTransValue\":10000000,\"overdraftAllowed\":false}]},{\"id\":10,\"code\":\"100009\",\"name\":\"Airtime Purchase\",\"status\":\"Active\",\"creationDate\":\"1605870076878\",\"serviceCategoryList\":[{\"id\":22,\"code\":\"100021\",\"serviceCode\":\"100009\",\"serviceName\":\"Airtime Purchase\",\"name\":\"Mobile Prepaid\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":true,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2020-11-20T16:36:10.202+0530\",\"modificationDate\":\"2021-09-19T19:33:15.127+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":true,\"loyaltyAllowed\":false,\"walletOwnerCateList\":[\"100010\",\"100011\",\"100002\",\"100000\",\"100001\"],\"overdraftAllowed\":false}]},{\"id\":14,\"code\":\"100013\",\"name\":\"Wallet Owner Management\",\"status\":\"Active\",\"creationDate\":\"1609321143274\",\"serviceCategoryList\":[{\"id\":74,\"code\":\"WLTOWR\",\"serviceCode\":\"100013\",\"serviceName\":\"Wallet Owner Management\",\"name\":\"Wallet Owner\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2021-01-05T18:54:35.157+0530\",\"modificationDate\":\"2021-01-07T13:29:53.195+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"overdraftAllowed\":false},{\"id\":75,\"code\":\"SUBS\",\"serviceCode\":\"100013\",\"serviceName\":\"Wallet Owner Management\",\"name\":\"Subscriber\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2021-01-05T18:55:09.344+0530\",\"modificationDate\":\"2021-09-19T19:31:51.568+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":true,\"loyaltyAllowed\":false,\"walletOwnerCateList\":[\"100000\",\"100002\",\"100001\"],\"overdraftAllowed\":false}]},{\"id\":17,\"code\":\"100016\",\"name\":\"Report\",\"status\":\"Active\",\"creationDate\":\"1609933314114\",\"serviceCategoryList\":[{\"id\":79,\"code\":\"TRNSRT\",\"serviceCode\":\"100016\",\"serviceName\":\"Report\",\"name\":\"Remittance Details\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":true,\"serviceCountryList\":[{\"code\":\"100092\",\"name\":\"Guinea\"}],\"creationDate\":\"2021-02-15T12:29:06.081+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"minTransValue\":1,\"maxTransValue\":50000,\"overdraftAllowed\":false},{\"id\":77,\"code\":\"TRNSDT\",\"serviceCode\":\"100016\",\"serviceName\":\"Report\",\"name\":\"Transaction Details\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2021-01-06T17:14:32.686+0530\",\"modificationDate\":\"2021-01-07T13:29:36.444+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"overdraftAllowed\":false}]},{\"id\":18,\"code\":\"100017\",\"name\":\"Overdraft\",\"status\":\"Active\",\"creationDate\":\"1614930496411\",\"serviceCategoryList\":[{\"id\":86,\"code\":\"ODLMT\",\"serviceCode\":\"100017\",\"serviceName\":\"Overdraft\",\"name\":\"Overdraft Limit\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2021-03-09T14:09:48.865+0530\",\"modificationDate\":\"2021-06-28T17:02:02.594+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"walletOwnerCateList\":[\"100002\",\"100001\",\"100000\"],\"minTransValue\":1,\"maxTransValue\":50000,\"overdraftAllowed\":false},{\"id\":92,\"code\":\"ODTRF\",\"serviceCode\":\"100017\",\"serviceName\":\"Overdraft\",\"name\":\"Overdraft Transfer\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2021-06-11T15:31:25.816+0530\",\"modificationDate\":\"2021-06-11T15:32:17.208+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"overdraftAllowed\":false}]},{\"id\":22,\"code\":\"100021\",\"name\":\"Commission Transfer\",\"status\":\"Active\",\"creationDate\":\"1629179547641\",\"serviceCategoryList\":[{\"id\":94,\"code\":\"COMTRF\",\"serviceCode\":\"100021\",\"serviceName\":\"Commission Transfer\",\"name\":\"Commission Transfer\",\"status\":\"Active\",\"state\":\"Approved\",\"productAllowed\":false,\"serviceCountryList\":[{\"code\":\"ALL\",\"name\":\"All\"}],\"creationDate\":\"2021-08-17T11:22:31.268+0530\",\"incentiveAllowed\":false,\"promotionAllowed\":false,\"loyaltyAllowed\":false,\"minTransValue\":1,\"maxTransValue\":100000,\"overdraftAllowed\":false}]}],\"idProffTypeCode\":\"100005\",\"issuingCountryCode\":\"100102\",\"email\":\"tarun.kumar@esteltelecom.com\",\"walletOwnerCode\":\"1000002094\",\"username\":\"Mahendra2355\",\"issuingCountryName\":\"India\"}");

                        if (jsonObject.has("error")) {

                            //  String error_message = jsonObject.getString("error_message");
                            //  Toast.makeText(LoginMsis.this, error_message, Toast.LENGTH_LONG).show();

                            if (jsonObject.getString("error").equalsIgnoreCase("1251")) {

                                Intent i = new Intent(LoginMsis.this, VerifyLoginOTPScreen.class);
                                i.putExtra("ERROR1251","1251");
                                startActivity(i);
                            }

                            else if (jsonObject.getString("error_message").equalsIgnoreCase("Invalid credentials")) {

                                Toast.makeText(LoginMsis.this, "Invalid pin", Toast.LENGTH_LONG).show();

                            }
                            else{
                                Toast.makeText(LoginMsis.this, jsonObject.getString("error_message"), Toast.LENGTH_LONG).show();
                            }
                        }

                        else {

                            String firstLoginStatus = jsonObject.getString("firstLoginStatus");

                            MyApplication.saveString("token",jsonObject.optString("access_token"),LoginMsis.this);

                            if (firstLoginStatus.equalsIgnoreCase("Y"))
                            {
                                // otp_generate_api();
                            }

                            else if (firstLoginStatus.equalsIgnoreCase("N")) {

                                Toast.makeText(LoginMsis.this, getString(R.string.login_successful), Toast.LENGTH_LONG).show();

                                Intent i = new Intent(LoginMsis.this, MainActivity.class);
                                startActivity(i);
                                finish();

                            }

                            else {

                                Toast.makeText(LoginMsis.this, firstLoginStatus, Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    }

                    catch (Exception e)
                    {
                        Toast.makeText(LoginMsis.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();
                    //  MyApplication.showToast(LoginMsis.this,aFalse);

                }
            });

        }catch (Exception e){

            MyApplication.hideLoader();
            MyApplication.showToast(LoginMsis.this,e.toString());
        }

    }

    /*
    private void otp_generate_api() {
        try{

            JSONObject jsonObject=new JSONObject();

            String USERNAME =  MyApplication.getSaveString("USERNAME",LoginMsis.this);
            String PASSWORD = MyApplication.getSaveString("PASSWORD",LoginMsis.this);
            String EMAIL = MyApplication.getSaveString("EMAIL",LoginMsis.this);
            String NTTYPECODE = MyApplication.getSaveString("NTTYPECODE",LoginMsis.this);
            String USERCODE = MyApplication.getSaveString("USERCODE",LoginMsis.this);



            jsonObject.put("email",EMAIL);
            jsonObject.put("notificationTypeCode",NTTYPECODE);
            jsonObject.put("transTypeCode","101813");      // Temporary Hard Code acording to Praveen
            jsonObject.put("status","Active");
            jsonObject.put("walletOwnerUserCode","101961"); // Temporary Hard Code acording to Praveen


            API.POST_GET_OTP("ewallet/api/v1/otp",jsonObject,new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {

                        //  JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1771883\",\"requestTime\":\"Mon Oct 18 18:05:40 IST 2021\",\"responseTime\":\"Mon Oct 18 18:05:40 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\"}");

                        if (jsonObject.has("error")) {

                            String error = jsonObject.getString("error");
                            String error_message = jsonObject.getString("error_message");

                            Toast.makeText(LoginMsis.this, error_message, Toast.LENGTH_LONG).show();

                            if(error.equalsIgnoreCase("1251"))
                            {

                                Intent i = new Intent(LoginMsis.this, VerifyLoginAccountScreen.class);
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
                                Toast.makeText(LoginMsis.this, resultDescription, Toast.LENGTH_LONG).show();


                                Intent i = new Intent(LoginMsis.this, OtpPage.class);
                                startActivity(i);

                            }

                            else {
                                Toast.makeText(LoginMsis.this, resultDescription, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    catch (Exception e)
                    {
                        Toast.makeText(LoginMsis.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }


                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();
                    MyApplication.showToast(LoginMsis.this,aFalse);

                }
            });

        }catch (Exception e){

            MyApplication.hideLoader();
            MyApplication.showToast(LoginMsis.this,e.toString());
        }

    }
 */



    ////////////////////////////// Permission  /////////////////////////////////////

    private boolean checkPermission_camera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
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

    private boolean checkPermission_write_external_storage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }



    private void requestPermission_camera() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void requestPermission_write_external_storage() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
    }


    ///////////////////////////////////////////////////////////////////////////////

    int doubleBackToExitPressed = 1;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressed == 2) {
            finishAffinity();
            System.exit(0);
        }
        else {
            doubleBackToExitPressed++;
            Toast.makeText(this, "Please press Back again to exit", Toast.LENGTH_SHORT).show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressed=1;
            }
        }, 2000);
    }



    private void api_walletOwnerUser() {

        String USER_CODE_FROM_TOKEN_AGENTDETAILS = MyApplication.getSaveString("userCode", LoginMsis.this);


        API.GET_CASHOUT_DETAILS("ewallet/api/v1/loginSecurity" , languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {


                try {


                    // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1789408\",\"requestTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"responseTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwnerUser\":{\"id\":2171,\"code\":\"101917\",\"firstName\":\"TATASnegal\",\"userName\":\"TATASnegal5597\",\"mobileNumber\":\"8888888882\",\"email\":\"kundan.kumar@esteltelecom.com\",\"walletOwnerUserTypeCode\":\"100000\",\"walletOwnerUserTypeName\":\"Supervisor\",\"walletOwnerCategoryCode\":\"100000\",\"walletOwnerCategoryName\":\"Institute\",\"userCode\":\"1000002606\",\"status\":\"Active\",\"state\":\"Approved\",\"gender\":\"M\",\"idProofTypeCode\":\"100004\",\"idProofTypeName\":\"MILITARY ID CARD\",\"idProofNumber\":\"44444444444\",\"creationDate\":\"2021-10-01T09:04:07.330+0530\",\"notificationName\":\"EMAIL\",\"notificationLanguage\":\"en\",\"createdBy\":\"100308\",\"modificationDate\":\"2021-10-20T14:59:00.791+0530\",\"modifiedBy\":\"100308\",\"addressList\":[{\"id\":3569,\"walletOwnerUserCode\":\"101917\",\"addTypeCode\":\"100001\",\"addTypeName\":\"Commercial\",\"regionCode\":\"100068\",\"regionName\":\"Boke\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"city\":\"100022\",\"cityName\":\"Dubreka\",\"addressLine1\":\"delhi\",\"status\":\"Inactive\",\"creationDate\":\"2021-10-01T09:04:07.498+0530\",\"createdBy\":\"100250\",\"modificationDate\":\"2021-10-03T09:52:57.407+0530\",\"modifiedBy\":\"100308\"}],\"workingDaysList\":[{\"id\":3597,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100000\",\"weekdaysName\":\"Monday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.518+0530\"},{\"id\":3598,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100001\",\"weekdaysName\":\"Tuesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.528+0530\"},{\"id\":3599,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100002\",\"weekdaysName\":\"Wednesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.538+0530\"},{\"id\":3600,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100003\",\"weekdaysName\":\"Thursday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.547+0530\"},{\"id\":3601,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100004\",\"weekdaysName\":\"Friday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.556+0530\"},{\"id\":3602,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100005\",\"weekdaysName\":\"Saturday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.565+0530\"},{\"id\":3603,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100006\",\"weekdaysName\":\"Sunday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"2:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.573+0530\"}],\"macEnabled\":false,\"ipEnabled\":false,\"resetCredReqInit\":false,\"notificationTypeCode\":\"100000\"}}");


                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {
                        Toast.makeText(LoginMsis.this, getString(R.string.login_successful), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(LoginMsis.this, MainActivity.class);
                        startActivity(i);
                        finish();


                    } else {
                        MyApplication.hideLoader();

                        Toast.makeText(LoginMsis.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                } catch (Exception e) {
                    MyApplication.hideLoader();

                    Toast.makeText(LoginMsis.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(LoginMsis.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

}
