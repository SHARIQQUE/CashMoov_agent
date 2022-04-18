package com.agent.cashmoovui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;


import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.model.CountryInfoModel;
import com.agent.cashmoovui.model.ServiceList;
import com.agent.cashmoovui.otp.VerifyLoginOTPScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class PhoneNumberRegistrationScreen extends AppCompatActivity {
    public static PhoneNumberRegistrationScreen phnoregistrationccreenC;
    TextView spCountry,tvContinue,tvPin,tvOr,tvFinger,msgText;
    SpinnerDialog spinnerDialogCountry;
    EditText etPhoneNo,etPass;
    private ArrayList<String> countryList = new ArrayList<>();
    private ArrayList<CountryInfoModel.Country> countryModelList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_registration_screen);
        phnoregistrationccreenC = this;
        getIds();
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

    private void getIds() {
        spCountry = findViewById(R.id.spCountry);
        etPhoneNo = findViewById(R.id.etPhoneNo);
        etPass = findViewById(R.id.etPass);
        tvPin = findViewById(R.id.tvPin);
        tvOr = findViewById(R.id.tvOr);
        tvFinger = findViewById(R.id.tvFinger);
        msgText = findViewById(R.id.msgText);
        tvContinue = findViewById(R.id.tvContinue);

        etPass.addTextChangedListener(new TextWatcher() {

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
                    MyApplication.hideKeyboard(phnoregistrationccreenC);            }
        });



//        spCountry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (spinnerDialogCountry!=null)
//                    spinnerDialogCountry.showSpinerDialog();
//            }
//        });

        setOnCLickListener();

        callApiCountry();

    }

    private void setOnCLickListener() {
        // creating a variable for our BiometricManager
        // and lets check if our user can use biometric sensor or not
        BiometricManager biometricManager = BiometricManager.from(phnoregistrationccreenC);
        switch (biometricManager.canAuthenticate()) {

            // this means we can use biometric sensor
            case BiometricManager.BIOMETRIC_SUCCESS:

                // msgText.setText("You can use the fingerprint sensor to login");
                // msgText.setTextColor(Color.parseColor("#fafafa"));
                break;

            // this means that the device doesn't have fingerprint sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                msgText.setText(getString(R.string.no_fingerprint_senser));
                tvFinger.setVisibility(View.GONE);
                break;

            // this means that biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msgText.setText(getString(R.string.no_biometric_senser));
                tvFinger.setVisibility(View.GONE);
                break;

            // this means that the device doesn't contain your fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                msgText.setText(getString(R.string.device_not_contain_fingerprint));
                tvFinger.setVisibility(View.GONE);
                break;
        }
        // creating a variable for our Executor
        Executor executor = ContextCompat.getMainExecutor(this);
        // this will give us result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt(phnoregistrationccreenC, executor, new BiometricPrompt.AuthenticationCallback() {
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
                Intent intent = new Intent(phnoregistrationccreenC, MainActivity.class);
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
        tvFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);

            }
        });

        tvPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(phnoregistrationccreenC, LoginMsis.class);
                startActivity(intent);

            }
        });

        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etPhoneNo.getText().toString().trim().isEmpty()) {
                    MyApplication.showTipError(phnoregistrationccreenC,getString(R.string.val_phone),etPhoneNo);
                    MyApplication.hideKeyboard(phnoregistrationccreenC);
                    return;
                }
                if(etPass.isShown()&&etPass.getText().toString().trim().isEmpty()) {
                    MyApplication.showTipError(phnoregistrationccreenC, getString(R.string.val_pass),etPass);
                    MyApplication.hideKeyboard(phnoregistrationccreenC);
                    return;
                } else {
                    if(etPass.isShown()){
                        callApiLoginPass();
                    }else{
                        callApiLoginOtp();
                    }

                }

            }
        });
    }

    private void callApiCountry() {
        try {
            API.GET_PUBLIC("ewallet/public/country/all",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                countryList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("countryList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                         if(data.optBoolean("subscriberAllowed")) {
                                        if (data.optString("code").equalsIgnoreCase("100092")) {
                                            countryModelList.add(new CountryInfoModel.Country(
                                                    data.optInt("id"),
                                                    data.optString("code"),
                                                    data.optString("isoCode"),
                                                    data.optString("name"),
                                                    data.optString("countryCode"),
                                                    data.optString("status"),
                                                    data.optString("dialCode"),
                                                    data.optString("currencyCode"),
                                                    data.optString("currencySymbol"),
                                                    data.optString("creationDate"),
                                                    data.optBoolean("subscriberAllowed")

                                            ));

                                            countryList.add(data.optString("name").trim());
                                            spCountry.setText(countryList.get(0));

                                            }
                                        }

                                    }

                                    spinnerDialogCountry = new SpinnerDialog(phnoregistrationccreenC, countryList, "Select Country", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
                                    spinnerDialogCountry.setCancellable(true); // for cancellable
                                    spinnerDialogCountry.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogCountry.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spCountry.setText(item);
                                            spCountry.setTag(position);
                                        }
                                    });

                                } else {
                                    MyApplication.showToast(phnoregistrationccreenC,jsonObject.optString("resultDescription", "N/A"));
                                }
                            }
                        }

                        @Override
                        public void failure(String aFalse) {
                            MyApplication.hideLoader();

                        }
                    });


        } catch (Exception e) {

        }

    }

    private void callApiLoginOtp() {
        try{

            JSONObject loginOtpJson=new JSONObject();
            loginOtpJson.put("mobileNumber",etPhoneNo.getText().toString());
            loginOtpJson.put("walletOwnerCategoryCode","100010");

            MyApplication.showloader(phnoregistrationccreenC,"Please wait!");
            API.POST_REQEST_CHECK("ewallet/public/login-otp", loginOtpJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {
                    System.out.println("PhoneNoRegister response======="+jsonObject.toString());

                    if (jsonObject != null) {
                        if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                            // MyApplication.showToast(jsonObject.optString("resultDescription", "N/A"));
                                    etPass.setVisibility(View.VISIBLE);
                                    tvPin.setVisibility(View.VISIBLE);
                                    tvOr.setVisibility(View.VISIBLE);
                            MyApplication.saveString("token",jsonObject.optString("access_token"),phnoregistrationccreenC);
                        }else if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("1000")){
                            MyApplication.showToast(phnoregistrationccreenC,getString(R.string.technical_failure));
                        } else {
                            //MyApplication.showToast(jsonObject.optString("resultDescription", "N/A"));

                        }
                    }
                }

                @Override
                public void failure(String aFalse) {
                    //MyApplication.showToast(phnoregistrationccreenC,aFalse);
                    MyApplication.showToast(phnoregistrationccreenC,getString(R.string.register_first_time_message));

                  //  Intent intent = new Intent(phnoregistrationccreenC, RegisterStepOne.class);
                  //  startActivity(intent);


                }
            });

        }catch (Exception e){

        }

    }


    private void callApiLoginPass() {
        try{

            JSONObject loginJson=new JSONObject();

            loginJson.put("username",etPhoneNo.getText().toString().trim());
            loginJson.put("password",etPass.getText().toString().trim());
            loginJson.put("grant_type","password");
            loginJson.put("country",MyApplication.getSaveString("COUNTRY",PhoneNumberRegistrationScreen.this));
            loginJson.put("cc",MyApplication.getSaveString("CC",PhoneNumberRegistrationScreen.this));
           // loginJson.put("scope","read write");

            System.out.println("Login request"+loginJson.toString());
            MyApplication.showloader(phnoregistrationccreenC,getString(R.string.getting_user_info));
            API.POST_REQEST_LOGIN_TOKEN("ewallet/oauth/token", loginJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    ArrayList<ServiceList.serviceListMain> dataM=new ArrayList<>();

                    System.out.println("Login response======="+jsonObject.toString());

                    MyApplication.saveString("token",jsonObject.optString("access_token"),phnoregistrationccreenC);
                    MyApplication.saveString("firstName",jsonObject.optString("firstName"),phnoregistrationccreenC);
                    MyApplication.saveString("lastName",jsonObject.optString("lastName"),phnoregistrationccreenC);
                    MyApplication.saveString("email",jsonObject.optString("email"),phnoregistrationccreenC);
                    MyApplication.saveString("mobile",jsonObject.optString("mobile"),phnoregistrationccreenC);
                    MyApplication.saveString("walletOwnerCategoryCode",jsonObject.optString("walletOwnerCategoryCode"),phnoregistrationccreenC);
                    MyApplication.saveString("walletOwnerCode",jsonObject.optString("walletOwnerCode"),phnoregistrationccreenC);
                    MyApplication.saveString("userCountryCode",jsonObject.optString("userCountryCode"),phnoregistrationccreenC);
                    MyApplication.saveString("userCode",jsonObject.optString("userCode"),phnoregistrationccreenC);
                    MyApplication.saveString("username",jsonObject.optString("username"),phnoregistrationccreenC);
                    MyApplication.saveString("userCountryCode",jsonObject.optString("userCountryCode"),phnoregistrationccreenC);
                    MyApplication.saveString("issuingCountryName", jsonObject.optString("issuingCountryName"), phnoregistrationccreenC);

                    // MyApplication.saveString("locale", jsonObject.optString("locale"), LoginActivity.this);
//
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

                    if(jsonObject.optString("firstLoginStatus").equalsIgnoreCase("Y")){
                        // MyApplication.showloader(LoginActivity.this,"Change Password Screen");
                        MyApplication.saveBool("FirstLogin",true,phnoregistrationccreenC);
                        MyApplication.saveString("loginUsername", etPhoneNo.getText().toString().trim(), phnoregistrationccreenC);
                        MyApplication.saveString("loginPassword",etPass.getText().toString().trim(), phnoregistrationccreenC);

                        if (jsonObject.optBoolean("loginWithOtpRequired")) {
                            Intent i = new Intent(phnoregistrationccreenC, VerifyLoginOTPScreen.class);
                            startActivity(i);
                            finish();
                        } else {

                            Intent i = new Intent(phnoregistrationccreenC, MainActivity.class);
                            startActivity(i);
                            finish();
                            Toast.makeText(phnoregistrationccreenC, getString(R.string.login_successful), Toast.LENGTH_LONG).show();
                        }
                    }else{
                        MyApplication.saveBool("FirstLogin",true,phnoregistrationccreenC);
                        MyApplication.saveString("loginUsername", etPhoneNo.getText().toString().trim(), phnoregistrationccreenC);
                        MyApplication.saveString("loginPassword", etPass.getText().toString().trim(), phnoregistrationccreenC);

                        if (jsonObject.optBoolean("loginWithOtpRequired")) {
                            Intent i = new Intent(phnoregistrationccreenC, VerifyLoginOTPScreen.class);
                            startActivity(i);
                            finish();
                        } else {
                            Intent i = new Intent(phnoregistrationccreenC, MainActivity.class);
                            startActivity(i);
                            finish();
                            Toast.makeText(phnoregistrationccreenC, getString(R.string.login_successful), Toast.LENGTH_LONG).show();
                            }
                        }

                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.showToast(phnoregistrationccreenC,aFalse);

                }
            });

        }catch (Exception e){

        }

    }

}
