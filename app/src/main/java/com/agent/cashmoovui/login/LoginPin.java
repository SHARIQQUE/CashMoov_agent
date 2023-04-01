package com.agent.cashmoovui.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.agent.cashmoovui.HiddenPassTransformationMethod;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.model.ServiceList;
import com.agent.cashmoovui.otp.OtpPage;
import com.agent.cashmoovui.otp.RESETPINOtpPage;
import com.agent.cashmoovui.otp.VerifyLoginAccountScreen;
import com.agent.cashmoovui.otp.VerifyLoginOTPScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executor;

public class LoginPin extends AppCompatActivity {
    public static LoginPin loginpinC;
    EditText etPin,etmobile;
    TextView tvContinue,tvFinger,msgText,tvregister,tvFinger1,nameText;
    private static final int PERMISSION_REQUEST_CODE = 200,READ_EXTERNAL_STORAGE=201,WRITE_EXTERNAL_STORAGE=202;

    boolean  isPasswordVisible;

    MyApplication applicationComponentClass;
    String languageToUse = "",mName,mMobile,mLastName;
    CardView pin_login_lay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_pin);
        loginpinC = this;
        getIds();

        etPin.addTextChangedListener(new TextWatcher() {

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
                    MyApplication.hideKeyboard(loginpinC);
            }
        });


        HiddenPassTransformationMethod hiddenPassTransformationMethod=new HiddenPassTransformationMethod();
        etPin.setTransformationMethod(hiddenPassTransformationMethod);
        etPin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etPin.getRight() - etPin.getCompoundDrawables()[RIGHT].getBounds().width())) {
                        int selection = etPin.getSelectionEnd();
                        if (isPasswordVisible) {
                            // set drawable image
                            etPin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            // hide Password
                            etPin.setTransformationMethod(hiddenPassTransformationMethod);
                            isPasswordVisible = false;
                        } else  {
                            // set drawable image
                            etPin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                            // show Password
                            etPin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isPasswordVisible = true;
                        }
                        etPin.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });


    }



    String pin;
    String FCM_TOKEN;
    @Override
    protected void onStart() {
        super.onStart();
        MyApplication.saveString("qrcode","", LoginPin.this);
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

    private void getIds() {
        etPin = findViewById(R.id.etPin);
        tvContinue = findViewById(R.id.tvContinue);
        tvFinger = findViewById(R.id.tvFinger);
        tvFinger1= findViewById(R.id.tvFinger1);
        msgText = findViewById(R.id.msgText);
        tvregister = findViewById(R.id.tvregister);
        nameText=findViewById(R.id.nameText);
        etmobile=findViewById(R.id.etmobile);
        SpannableString content = new SpannableString(getString(R.string.reset_pin));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tvFinger1.setText(content);

        pin_login_lay=findViewById(R.id.pin_login_lay);
        mName=MyApplication.getSaveString("firstName", LoginPin.this);
        mMobile=MyApplication.getSaveString("mobile", LoginPin.this);
        mLastName=MyApplication.getSaveString("lastName", LoginPin.this);


        if(mLastName.equalsIgnoreCase("null")){
            nameText.setText("Hi "+ mName);

        }else{
            nameText.setText("Hi "+ mName+" "+mLastName);

        }
        etmobile.setText(mMobile);
        etmobile.setEnabled(false);

        tvFinger1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mssidn_api();

            }
        });

        etPin.addTextChangedListener(new TextWatcher() {

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
                    MyApplication.hideKeyboard(loginpinC);            }
        });


        tvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - MyApplication.mLastClickTime < 1000) { // 1000 = 1second
                    return;
                }
                MyApplication.mLastClickTime = SystemClock.elapsedRealtime();
                applicationComponentClass.getmSharedPreferences().edit().putString("isFirstRun", "YES").commit();


                Intent intent=new Intent(loginpinC, LoginMsis.class);
                startActivity(intent);

            }
        });

        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPin.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(loginpinC,getString(R.string.val_pin));
                    return;
                }
                if(etPin.getText().toString().trim().length()<4) {
                    MyApplication.showErrorToast(loginpinC, getString(R.string.val_valid_pin));
                    return;
                }


                if (checkPermission_camera()) {
                    if (checkPermission_read_external_storage()) {
                        if(!MyApplication.isConnectingToInternet(LoginPin.this)){
                            Toast.makeText(applicationComponentClass, getString(R.string.please_check_internet), Toast.LENGTH_SHORT).show();
                        }else{
                            callApiLoginPass();

                        }

                       // if (checkPermission_write_external_storage()) {



//                        } else {
//                            requestPermission_write_external_storage();
//                        }
                    }

                    else {
                        requestPermission_read_external_storage();
                    }
                } else {
                    requestPermission_camera();
                }

            }

        });


        pin =MyApplication.getSaveString("pin",loginpinC);
        if(pin!=null && pin.length()==4) {
            tvFinger.setVisibility(View.VISIBLE);
        }else{
            tvFinger.setVisibility(View.GONE);
        }

        pin_login_lay.setVisibility(View.GONE);
            MyApplication.setProtection = MyApplication.getSaveString("ACTIVATEPROTECTION", LoginPin.this);
        if(MyApplication.setProtection!=null && !MyApplication.setProtection.isEmpty()) {
            if (MyApplication.setProtection.equalsIgnoreCase("Activate")) {
                setOnClickListener();
                pin_login_lay.setVisibility(View.GONE);
            }else {
                pin_login_lay.setVisibility(View.VISIBLE);
            }
        }else{
            pin_login_lay.setVisibility(View.VISIBLE);
        }
    }

     BiometricPrompt biometricPrompt=null;
    private void setOnClickListener() {




        BiometricManager biometricManager = BiometricManager.from(loginpinC);
        switch (biometricManager.canAuthenticate()) {

            // this means we can use biometric sensor
            case BiometricManager.BIOMETRIC_SUCCESS:

                // msgText.setText("You can use the fingerprint sensor to login");
                // msgText.setTextColor(Color.parseColor("#fafafa"));
                break;

            // this means that the device doesn't have fingerprint sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                //msgText.setText(getString(R.string.no_fingerprint_senser));
                pin_login_lay.setVisibility(View.VISIBLE);
                tvFinger.setVisibility(View.GONE);
                break;

            // this means that biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                //msgText.setText(getString(R.string.no_biometric_senser));
                tvFinger.setVisibility(View.GONE);
                System.out.println("get sensor");
                pin_login_lay.setVisibility(View.VISIBLE);
                break;

            // this means that the device doesn't contain your fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                //msgText.setText(getString(R.string.device_not_contain_fingerprint));
                pin_login_lay.setVisibility(View.VISIBLE);
                tvFinger.setVisibility(View.GONE);
                break;
        }
        // creating a variable for our Executor
        Executor executor = ContextCompat.getMainExecutor(this);
        // this will give us result of AUTHENTICATION
         biometricPrompt = new BiometricPrompt(loginpinC, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                System.out.println("get failde"+errString);
                pin_login_lay.setVisibility(View.VISIBLE);
                biometricPrompt.cancelAuthentication();

            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                //  Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                // tvFinger.setText("Login Successful");

                System.out.println("Biomatric   =>"+result.toString());


                if (checkPermission_camera()) {

                    if (checkPermission_read_external_storage()) {

                       // if (checkPermission_write_external_storage()) {

                            callApiLoginPassF();

//                        } else {
//                            requestPermission_write_external_storage();
//                        }
                    } else {
                        requestPermission_read_external_storage();
                    }
                } else {
                    requestPermission_camera();
                }

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                pin_login_lay.setVisibility(View.VISIBLE);
                biometricPrompt.cancelAuthentication();
            }
        });
        // creating a variable for our promptInfo
        // BIOMETRIC DIALOG
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("CASHMOOV")
                .setDescription(getString(R.string.use_finger_to_login)).setNegativeButtonText(getString(R.string.cancel)).setConfirmationRequired(false).build();
        tvFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);

            }
        });

        biometricPrompt.authenticate(promptInfo);

    }


    private void callApiLoginPass() {
        try{

            pin_login_lay.setVisibility(View.VISIBLE);
            JSONObject loginJson=new JSONObject();

           
            loginJson.put("username",MyApplication.getSaveString("USERNAME",loginpinC));
            loginJson.put("password",MyApplication.getEncript(etPin.getText().toString().trim()));
            loginJson.put("grant_type","password");
            loginJson.put("fcmToken",FCM_TOKEN);
            loginJson.put("country",MyApplication.getSaveString("COUNTRY",LoginPin.this));
            loginJson.put("cc",MyApplication.getSaveString("CC",LoginPin.this));

            // loginJson.put("scope","read write");

            System.out.println("Login request"+loginJson.toString());
            MyApplication.saveBool("FirstLoginCounter",true,MyApplication.getInstance());
            MyApplication.showloader(loginpinC,getString(R.string.getting_user_info));
            API.POST_REQEST_LOGIN_TOKEN("ewallet/oauth/token", loginJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                  //  generateNoteOnSD(LoginPin.this,ewallet/oauth/token", loginJson);


                    try {


                        if (jsonObject.has("error")) {
                            MyApplication.hideLoader();


                            //  String error_message = jsonObject.getString("error_message");
                            //  Toast.makeText(LoginMsis.this, error_message, Toast.LENGTH_LONG).show();

                            if (jsonObject.getString("error").equalsIgnoreCase("1251")) {
                                MyApplication.saveString("pin", etPin.getText().toString().trim(), loginpinC);
                                Intent i = new Intent(LoginPin.this, VerifyLoginOTPScreen.class);
                                i.putExtra("ERROR1251","1251");
                                startActivity(i);
                            }

                            else if (jsonObject.getString("error_message").equalsIgnoreCase("Invalid credentials")) {
                                MyApplication.hideLoader();

                                Toast.makeText(LoginPin.this, R.string.invalid_pin_toast, Toast.LENGTH_LONG).show();
                                MyApplication.saveString("pin", pin, loginpinC);

                            }
                            else{
                                MyApplication.hideLoader();

                                Toast.makeText(LoginPin.this, jsonObject.getString("error_message"), Toast.LENGTH_LONG).show();
                            }
                        }

                        else {

                            System.out.println("get response"+jsonObject.toString());
                            applicationComponentClass.getmSharedPreferences().edit().putString("isFirstRun", "NO_LOGINPIN").commit();

                            ArrayList<ServiceList.serviceListMain> dataM=new ArrayList<>();

                            MyApplication.saveString("pin", etPin.getText().toString().trim(), loginpinC);

                            MyApplication.saveString("token", jsonObject.optString("access_token"), loginpinC);
                            MyApplication.saveString("firstName", jsonObject.optString("firstName"), loginpinC);
                            MyApplication.saveString("lastName", jsonObject.optString("lastName"), loginpinC);
                            MyApplication.saveString("email", jsonObject.optString("email"), loginpinC);
                            MyApplication.saveString("mobile", jsonObject.optString("mobile"), loginpinC);
                            MyApplication.saveString("walletOwnerCategoryCode", jsonObject.optString("walletOwnerCategoryCode"), loginpinC);
                            MyApplication.saveString("walletOwnerCode", jsonObject.optString("walletOwnerCode"), loginpinC);
                            MyApplication.saveString("userCountryCode", jsonObject.optString("userCountryCode"), loginpinC);
                            MyApplication.saveString("userCode", jsonObject.optString("userCode"), loginpinC);
                            MyApplication.saveString("username", jsonObject.optString("username"), loginpinC);
                            MyApplication.saveString("userCountryCode", jsonObject.optString("userCountryCode"), loginpinC);
                            MyApplication.saveString("issuingCountryName", jsonObject.optString("issuingCountryName"), loginpinC);
                            MyApplication.setService(jsonObject.optJSONArray("serviceList"));

                            // #################### serviceList Add  serviceCategoryList add  serviceCode

                            if(jsonObject.has("serviceList"))
                            {
                                JSONArray jsonArray_serviceList = jsonObject.getJSONArray("serviceList");

                                for(int i=0;i<jsonArray_serviceList.length();i++)
                                {
                                    JSONObject jsonObject1 = jsonArray_serviceList.getJSONObject(i);

                                    if(jsonObject1.has("serviceCategoryList"))
                                    {
                                        JSONArray jsonArray_serviceCategoryList = jsonObject1.getJSONArray("serviceCategoryList");
                                        for(int j=0;j<jsonArray_serviceCategoryList.length();j++)
                                        {
                                            JSONObject jsonObject2 = jsonArray_serviceCategoryList.getJSONObject(j);

                                            if(jsonObject2.has("serviceName"))
                                            {
                                                String serviceName =jsonObject2.getString("serviceName");

                                                if(serviceName.equalsIgnoreCase("Money Transfer"))
                                                {
                                                    if(jsonObject2.has("serviceCode"))
                                                    {
                                                        String serviceCode =jsonObject2.getString("serviceCode");


                                                        MyApplication.saveString("serviceCode_LoginApi", serviceCode, loginpinC);

                                                    }
                                                    else {
                                                        MyApplication.saveString("serviceCode_LoginApi", "", loginpinC); // not coming from server

                                                    }
                                                }

                                            }
                                        }

                                    }

                                }

                            }
                            else {
                                MyApplication.saveString("serviceCode_LoginApi", "", loginpinC); // not coming from server

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
                                MyApplication.hideLoader();

                            }

                            if (jsonObject.optBoolean("loginWithOtpRequired")) {
                                Intent i = new Intent(loginpinC, VerifyLoginOTPScreen.class);
                                startActivity(i);
                                MyApplication.hideLoader();

                                // finish();
                            } else {
                                userInfo_api(jsonObject.optString("walletOwnerCode"));
                            }

                        }
                    }

                    catch (Exception e)
                    {
                        MyApplication.hideLoader();

                        //Toast.makeText(loginpinC, e.toString(), Toast.LENGTH_LONG).show();
                    }
                }


                @Override
                public void failure(String aFalse) {
                    MyApplication.hideLoader();


                    if(aFalse.equalsIgnoreCase("1251")){

                        Intent i = new Intent(loginpinC, VerifyLoginOTPScreen.class);
                        i.putExtra("ERROR1251","1251");
                        startActivity(i);
                        // callPostGetLoginOTP();
                       /* Intent i = new Intent(loginpinC, ChangeIpLoginActivity.class);
                        startActivity(i);*/
                    }

                   else if(aFalse.contains("Failed to connect")){
                         Toast.makeText(loginpinC, getString(R.string.please_try_again_later), Toast.LENGTH_LONG).show();

                    }
                    else{
                       // Toast.makeText(LoginPin.this,aFalse, Toast.LENGTH_LONG).show();
                    }

                        // com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ConnectException: Failed to connect to /202.131.144.130:8081

                   // Toast.makeText(loginpinC, aFalse, Toast.LENGTH_LONG).show();


                }
            });

        }catch (Exception e){

        }

    }

    private void callApiLoginPassF() {
        try{
            pin_login_lay.setVisibility(View.VISIBLE);
            JSONObject loginJson=new JSONObject();


            loginJson.put("username",MyApplication.getSaveString("USERNAME",loginpinC));
            loginJson.put("password",MyApplication.getEncript(pin));
            loginJson.put("grant_type","password");
            loginJson.put("fcmToken",FCM_TOKEN);
            loginJson.put("country",MyApplication.getSaveString("COUNTRY",LoginPin.this));
            loginJson.put("cc",MyApplication.getSaveString("CC",LoginPin.this));

            // loginJson.put("scope","read write");

            MyApplication.saveBool("FirstLoginCounter",true,MyApplication.getInstance());
            System.out.println("Login request"+loginJson.toString());
            MyApplication.showloader(loginpinC,getString(R.string.getting_user_info));
            API.POST_REQEST_LOGIN_TOKEN("ewallet/oauth/token", loginJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {



                    try {

                        if (jsonObject.has("error")) {
                            MyApplication.hideLoader();
                           // pin_login_lay.setVisibility(View.VISIBLE);

                            //  String error_message = jsonObject.getString("error_message");
                            //  Toast.makeText(LoginMsis.this, error_message, Toast.LENGTH_LONG).show();

                            if (jsonObject.getString("error").equalsIgnoreCase("1251")) {

                                Intent i = new Intent(LoginPin.this, VerifyLoginOTPScreen.class);
                                i.putExtra("ERROR1251","1251");
                                startActivity(i);

                            } else if (jsonObject.getString("error_message").equalsIgnoreCase("Invalid credentials")) {

                                Toast.makeText(LoginPin.this, getString(R.string.invalid_pin_toast), Toast.LENGTH_LONG).show();

                               // MyApplication.saveString("pin", pin, loginpinC);

                            }else{
                                Toast.makeText(LoginPin.this, jsonObject.getString("error_message"), Toast.LENGTH_LONG).show();
                            }


                        }

                        else {

                            applicationComponentClass.getmSharedPreferences().edit().putString("isFirstRun", "NO_LOGINPIN").commit();


                            ArrayList<ServiceList.serviceListMain> dataM=new ArrayList<>();

                            System.out.println("Login response=======" + jsonObject.toString());
                            MyApplication.saveString("pin", pin, loginpinC);

                            MyApplication.saveString("token", jsonObject.optString("access_token"), loginpinC);
                            MyApplication.saveString("firstName", jsonObject.optString("firstName"), loginpinC);
                            MyApplication.saveString("lastName", jsonObject.optString("lastName"), loginpinC);
                            MyApplication.saveString("email", jsonObject.optString("email"), loginpinC);
                            MyApplication.saveString("mobile", jsonObject.optString("mobile"), loginpinC);
                            MyApplication.saveString("walletOwnerCategoryCode", jsonObject.optString("walletOwnerCategoryCode"), loginpinC);
                            MyApplication.saveString("walletOwnerCode", jsonObject.optString("walletOwnerCode"), loginpinC);
                            MyApplication.saveString("userCountryCode", jsonObject.optString("userCountryCode"), loginpinC);
                            MyApplication.saveString("userCode", jsonObject.optString("userCode"), loginpinC);
                            MyApplication.saveString("username", jsonObject.optString("username"), loginpinC);
                            MyApplication.saveString("userCountryCode", jsonObject.optString("userCountryCode"), loginpinC);
                            MyApplication.saveString("issuingCountryName", jsonObject.optString("issuingCountryName"), loginpinC);

                            MyApplication.setService(jsonObject.optJSONArray("serviceList"));

                            // #################### serviceList Add  serviceCategoryList add  serviceCode

                            if(jsonObject.has("serviceList"))
                            {
                                JSONArray jsonArray_serviceList = jsonObject.getJSONArray("serviceList");

                                for(int i=0;i<jsonArray_serviceList.length();i++)
                                {
                                    JSONObject jsonObject1 = jsonArray_serviceList.getJSONObject(i);

                                    if(jsonObject1.has("serviceCategoryList"))
                                        {
                                            JSONArray jsonArray_serviceCategoryList = jsonObject1.getJSONArray("serviceCategoryList");
                                            for(int j=0;j<jsonArray_serviceCategoryList.length();j++)
                                            {
                                                JSONObject jsonObject2 = jsonArray_serviceCategoryList.getJSONObject(j);

                                                if(jsonObject2.has("serviceName"))
                                                {
                                                    String serviceName =jsonObject2.getString("serviceName");

                                                    if(serviceName.equalsIgnoreCase("Money Transfer"))
                                                    {
                                                        if(jsonObject2.has("serviceCode"))
                                                        {
                                                            String serviceCode =jsonObject2.getString("serviceCode");


                                                            MyApplication.saveString("serviceCode_LoginApi", serviceCode, loginpinC);

                                                        }
                                                        else {
                                                            MyApplication.saveString("serviceCode_LoginApi", "", loginpinC); // not coming from server

                                                        }
                                                    }

                                                }
                                            }

                                        }

                                }

                            }
                            else {
                                 MyApplication.saveString("serviceCode_LoginApi", "", loginpinC); // not coming from server

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
                                MyApplication.hideLoader();
                            }

                            if (jsonObject.optBoolean("loginWithOtpRequired")) {
                                MyApplication.hideLoader();
                                Intent i = new Intent(loginpinC, VerifyLoginOTPScreen.class);
                                startActivity(i);
                                MyApplication.hideLoader();

                                //finish();
                            } else {
                                userInfo_api(jsonObject.optString("walletOwnerCode"));
                            }
                                // ###################################################################################




                        }

                    } catch (Exception e) {
                        Toast.makeText(loginpinC, e.toString(), Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void failure(String aFalse) {

                    if(aFalse.equalsIgnoreCase("1251")){
                        Intent i = new Intent(loginpinC, VerifyLoginOTPScreen.class);
                        i.putExtra("ERROR1251","1251");
                        startActivity(i);

                    }


                    else if(aFalse.contains("Failed to connect")){
                        Toast.makeText(loginpinC, getString(R.string.please_try_again_later), Toast.LENGTH_LONG).show();

                    }

                   // Toast.makeText(loginpinC, aFalse, Toast.LENGTH_LONG).show();

                }
            });

        }catch (Exception e){

        }

    }

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
                                        walletOwner.optString("profileImageName"),LoginPin.this);
                            }
                            if(walletOwner.has("registerCountryName")){
                                MyApplication.saveString("COUNTRY_NAME_USERINFO", walletOwner.getString("registerCountryName"), LoginPin.this);
                                MyApplication.saveString("COUNTRYNAME_AGENT",walletOwner.getString("registerCountryName"),LoginPin.this);
                            }else{
                                MyApplication.saveString("COUNTRY_NAME_USERINFO", "", LoginPin.this);
                            }


                            if(walletOwner.has("registerCountryCode")){
                                MyApplication.saveString("COUNTRY_CODE_USERINFO", walletOwner.getString("registerCountryCode"), LoginPin.this);
                                MyApplication.saveString("COUNTRYCODE_AGENT",walletOwner.getString("registerCountryCode"),LoginPin.this);
                            }else{
                                MyApplication.saveString("COUNTRY_CODE_USERINFO", "", LoginPin.this);
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
                                MyApplication.saveString("FIRSTNAME_USERINFO", walletOwner.getString("ownerName"), LoginPin.this);
                            }else{
                                MyApplication.saveString("FIRSTNAME_USERINFO", "", LoginPin.this);
                            }

                            if(walletOwner.has("lastName")){
                                MyApplication.saveString("LASTNAME_USERINFO", walletOwner.getString("lastName"), LoginPin.this);
                            }else{
                                MyApplication.saveString("LASTNAME_USERINFO", "", LoginPin.this);
                            }

                            MyApplication.saveBool("FirstLoginCounter",true,LoginPin.this);
                            MyApplication.hideLoader();
                            Intent i = new Intent(loginpinC, MainActivity.class);
                            startActivity(i);
                            finish();
                            Toast.makeText(loginpinC, loginpinC.getString(R.string.login_successful), Toast.LENGTH_LONG).show();


                        }





                        else
                        {

                            MyApplication.hideLoader();

                            Toast.makeText(LoginPin.this,resultDescription,Toast.LENGTH_LONG).show();


                        }



                    }
                    catch (Exception e)
                    {
                        MyApplication.hideLoader();

                        Toast.makeText(LoginPin.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }


                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();
                    MyApplication.showToast(LoginPin.this,aFalse);

                }
            });

        }catch (Exception e){

            MyApplication.hideLoader();
            MyApplication.showToast(LoginPin.this,e.toString());
        }

    }


    private void mssidn_api() {
        try{


            API.GET_MSDIN("ewallet/public/walletOwner/msisdn/"+MyApplication.getSaveString("USERNAME",loginpinC),languageToUse,new Api_Responce_Handler() {
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

                            if (!walletOwnerUser.optBoolean("isPinAlreadySet") && walletOwnerUser.getString("firstLoginStatus").equalsIgnoreCase("y")) {
                                firstLoginStatus = "y";
                            } else {
                                firstLoginStatus = "n";
                            }
                            //  String countryCode_agent = walletOwnerUser.getString("issuingCountryCode");
                            // String countryName_agent = walletOwnerUser.getString("issuingCountryName");

                            MyApplication.saveString("COUNTRYNAME_AGENT","",LoginPin.this);
                            MyApplication.saveString("COUNTRYCODE_AGENT","",LoginPin.this);
                           /* String countryCode_agent = walletOwnerUser.getString("issuingCountryCode");
                            String countryName_agent = walletOwnerUser.getString("issuingCountryName");*/
                            // MyApplication.getSaveString("COUNTRYCODE_AGENT"
                            MyApplication.saveString("COUNTRYNAME_AGENT","",LoginPin.this);
                            MyApplication.saveString("COUNTRYCODE_AGENT","",LoginPin.this);


                            MyApplication.saveString("USERNAME",MyApplication.getSaveString("USERNAME",loginpinC),LoginPin.this);
                            MyApplication.saveString("PASSWORD",pin,LoginPin.this);
                            MyApplication.saveString("CODE_AGENT",CODE_AGENT,LoginPin.this);
                            MyApplication.saveString("EMAIL",EMAIL,LoginPin.this);
                            MyApplication.saveString("USERCODE",USERCODE,LoginPin.this);
                            MyApplication.saveString("NTTYPECODE",NTTYPECODE,LoginPin.this);

                            if(walletOwnerUser.optBoolean("reSetPinCredRequest")) {
                                Intent i = new Intent(LoginPin.this, RESETPINOtpPage.class);
                                startActivity(i);
                                return;
                            } else {
                                Toast.makeText(LoginPin.this, getString(R.string.contactadmin), Toast.LENGTH_LONG).show();

                            }

                            if (firstLoginStatus.equalsIgnoreCase("Y"))
                            {
                                Intent i = new Intent(LoginPin.this, OtpPage.class);
                                startActivity(i);
                                // finish();
                            }








                        }

                        else
                        {

                            MyApplication.hideLoader();

                            Toast.makeText(LoginPin.this,resultDescription,Toast.LENGTH_LONG).show();


                        }



                    }
                    catch (Exception e)
                    {
                        MyApplication.hideLoader();

                        Toast.makeText(LoginPin.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }


                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();
                    MyApplication.showToast(LoginPin.this,aFalse);

                }
            });

        }catch (Exception e){

            MyApplication.hideLoader();
            MyApplication.showToast(LoginPin.this,e.toString());
        }

    }





}
