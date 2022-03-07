package com.agent.cashmoovui.cashout;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.agent.cashmoovui.AddContact;
import com.agent.cashmoovui.HiddenPassTransformationMethod;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.activity.OtherOption;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.apiCalls.BioMetric_Responce_Handler;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.login.LoginPin;
import com.agent.cashmoovui.otp.VerifyLoginAccountScreen;
import com.agent.cashmoovui.set_pin.AESEncryption;
import com.blikoon.qrcodescanner.QrCodeActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CashOutAgent extends AppCompatActivity implements View.OnClickListener {

    boolean  isPasswordVisible,isPasswordVisible2;

    private static final int REQUEST_CODE_QR_SCAN = 101;


    public static LoginPin loginpinC;
    ImageButton qrCode_imageButton;
    private static final int PERMISSION_REQUEST_CODE = 200;
    TextView exportReceipt_textview;
    ImageView imgBack,imgHome;
    View rootView;

    String currencySymbol_sender="";
    String currencySymbol_receiver="";



    TextView tvContinue,tv_nextClick, rp_tv_senderName, rp_tv_mobileNumber, rp_tv_businessType, rp_tv_email, rp_tv_country, rp_tv_receiverName, rp_tv_transactionAmount, rp_tv_fees_reveiewPage, receiptPage_tv_stransactionType, receiptPage_tv_dateOfTransaction, receiptPage_tv_transactionAmount,
            receiptPage_tv_amount_to_be_paid, receiptPage_tv_fee, receiptPage_tv_financialtax,receipt_tv_amount_to_be_charge,
            receiptPage_tv_transaction_receiptNo, receiptPage_tv_sender_name,
            receiptPage_tv_sender_phoneNo,
            receiptPage_tv_receiver_name, receiptPage_tv_receiver_phoneNo, close_receiptPage_textview, rp_tv_financialTax, rp_tv_amount_to_be_charge, rp_tv_amount_to_be_paid, previous_reviewClick_textview, confirm_reviewClick_textview;
    LinearLayout ll_page_1, ll_reviewPage, ll_receiptPage, ll_pin, ll_otp, ll_resendOtp,ll_successPage;

    String selectClickType="",desWalletOwnerCode_from_currency="";

    MyApplication applicationComponentClass;
    String languageToUse = "";

    EditText edittext_mobileNuber, edittext_amount, et_mpin, et_otp;

    String mobileNoStr = "", amountstr = "", otpStr = "";

    String walletOwnerCode_mssis_agent = "", walletOwnerCode_subs, senderNameAgent = "";

    String  currencyCode_agent="",countryCode_agent="",currencyName_agent="",countryName_agent;
    String  currencyCode_subscriber="",countryCode_subscriber="",currencyName_subscriber="",agentCode_subscriber;

    String tax_financial = "", fees_amount, totalAmount_str, receivernameStr = "";
    Double tax_financial_double = 0.0, amountstr_double = 0.0, fees_amount_double = 0.0, totalAmount_double = 0.0;

    String mpinStr = "";


    String serviceCode_from_serviceCategory = "", serviceCategoryCode_from_serviceCategory = "", serviceProviderCode_from_serviceCategory;
    TextView tvFinger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        applicationComponentClass = (MyApplication) getApplicationContext();

        try {

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
            setContentView(R.layout.cashout_agent);

            setBackMenu();

            rootView = getWindow().getDecorView().findViewById(R.id.main_layout);
            exportReceipt_textview = (TextView) findViewById(R.id.exportReceipt_textview);
            exportReceipt_textview.setOnClickListener(this);


            //     First page

            ll_page_1 = (LinearLayout) findViewById(R.id.ll_page_1);

            tv_nextClick = (TextView) findViewById(R.id.tv_nextClick);
            tvContinue = (TextView) findViewById(R.id.tvContinue);
            tvContinue.setOnClickListener(this);
            edittext_mobileNuber = (EditText) findViewById(R.id.edittext_mobileNuber);
            edittext_amount = (EditText) findViewById(R.id.edittext_amount);

            edittext_mobileNuber.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;

                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        if(event.getRawX() >= (edittext_mobileNuber.getRight() - edittext_mobileNuber.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here


                            Intent intent = new Intent(CashOutAgent.this,
                                    AddContact.class);
                            startActivityForResult(intent , REQUEST_CODE);

                            return true;
                        }
                    }
                    return false;
                }
            });

            //    Reveiw page

            ll_reviewPage = (LinearLayout) findViewById(R.id.ll_reviewPage);
            ll_successPage = (LinearLayout) findViewById(R.id.ll_successPage);

            rp_tv_senderName = (TextView) findViewById(R.id.rp_tv_senderName);
            rp_tv_mobileNumber = (TextView) findViewById(R.id.rp_tv_mobileNumber);
            rp_tv_businessType = (TextView) findViewById(R.id.rp_tv_businessType);
            rp_tv_email = (TextView) findViewById(R.id.rp_tv_email);
            rp_tv_country = (TextView) findViewById(R.id.rp_tv_country);
            rp_tv_country.setVisibility(View.GONE);
            rp_tv_receiverName = (TextView) findViewById(R.id.rp_tv_receiverName);
            rp_tv_transactionAmount = (TextView) findViewById(R.id.rp_tv_transactionAmount);
            rp_tv_fees_reveiewPage = (TextView) findViewById(R.id.rp_tv_fees_reveiewPage);
            rp_tv_financialTax = (TextView) findViewById(R.id.rp_tv_financialTax);
            rp_tv_amount_to_be_charge = (TextView) findViewById(R.id.rp_tv_amount_to_be_charge);
            rp_tv_amount_to_be_paid = (TextView) findViewById(R.id.rp_tv_amount_to_be_paid);


            et_mpin = (EditText) findViewById(R.id.et_mpin);
            previous_reviewClick_textview = (TextView) findViewById(R.id.previous_reviewClick_textview);
            confirm_reviewClick_textview = (TextView) findViewById(R.id.confirm_reviewClick_textview);

             tvFinger =findViewById(R.id.tvFinger);
            if(MyApplication.setProtection!=null && !MyApplication.setProtection.isEmpty()) {
                if (MyApplication.setProtection.equalsIgnoreCase("Activate")) {
                    tvFinger.setVisibility(View.VISIBLE);
                } else {
                    tvFinger.setVisibility(View.GONE);
                }
            }else{
                tvFinger.setVisibility(View.VISIBLE);
            }
            tvFinger.setVisibility(View.GONE);
            tvFinger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.biometricAuth(CashOutAgent.this, new BioMetric_Responce_Handler() {
                        @Override
                        public void success(String success) {
                            try {

                                //  String encryptionDatanew = AESEncryption.getAESEncryption(MyApplication.getSaveString("pin",MyApplication.appInstance).toString().trim());
                                mpinStr=MyApplication.getSaveString("pin",MyApplication.appInstance);

                                if (new InternetCheck().isConnected(CashOutAgent.this)) {

                                    MyApplication.showloader(CashOutAgent.this, getString(R.string.getting_user_info));


                                    mpin_final_api();


                                } else {
                                    Toast.makeText(CashOutAgent.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failure(String failure) {
                            MyApplication.showToast(CashOutAgent.this,failure);
                        }
                    });
                }
            });


            //    Receipt page

            ll_receiptPage = (LinearLayout) findViewById(R.id.ll_receiptPage);


            receiptPage_tv_transaction_receiptNo = (TextView) findViewById(R.id.receiptPage_tv_transaction_receiptNo);
            receiptPage_tv_stransactionType = (TextView) findViewById(R.id.receiptPage_tv_stransactionType);
            receiptPage_tv_dateOfTransaction = (TextView) findViewById(R.id.receiptPage_tv_dateOfTransaction);
            receiptPage_tv_transactionAmount = (TextView) findViewById(R.id.receiptPage_tv_transactionAmount);
            receiptPage_tv_amount_to_be_paid = (TextView) findViewById(R.id.receiptPage_tv_amount_to_be_paid);
            receiptPage_tv_fee = (TextView) findViewById(R.id.receiptPage_tv_fee);
            receiptPage_tv_financialtax = (TextView) findViewById(R.id.receiptPage_tv_financialtax);
            receipt_tv_amount_to_be_charge = findViewById(R.id.receipt_tv_amount_to_be_charge);
            receiptPage_tv_sender_name = (TextView) findViewById(R.id.receiptPage_tv_sender_name);
            receiptPage_tv_sender_phoneNo = (TextView) findViewById(R.id.receiptPage_tv_sender_phoneNo);
            receiptPage_tv_receiver_name = (TextView) findViewById(R.id.receiptPage_tv_receiver_name);
            receiptPage_tv_receiver_phoneNo = (TextView) findViewById(R.id.receiptPage_tv_receiver_phoneNo);
            close_receiptPage_textview = (TextView) findViewById(R.id.close_receiptPage_textview);
            qrCode_imageButton = (ImageButton) findViewById(R.id.qrCode_imageButton);

            qrCode_imageButton.setOnClickListener(this);
            tv_nextClick.setOnClickListener(this);
            previous_reviewClick_textview.setOnClickListener(this);
            confirm_reviewClick_textview.setOnClickListener(this);
            close_receiptPage_textview.setOnClickListener(this);

            edittext_mobileNuber.setEnabled(true);


            walletOwnerCode_mssis_agent = MyApplication.getSaveString("USERCODE", CashOutAgent.this);


            et_otp = (EditText) findViewById(R.id.et_otp);
            ll_pin = (LinearLayout) findViewById(R.id.ll_pin);
            ll_otp = (LinearLayout) findViewById(R.id.ll_otp);
            ll_resendOtp = (LinearLayout) findViewById(R.id.ll_resendOtp);
            ll_resendOtp.setOnClickListener(this);

            confirm_reviewClick_textview.setText(getString(R.string.otp_verification));
             selectClickType="select_otp";

            et_mpin.addTextChangedListener(new TextWatcher() {

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
                        MyApplication.hideKeyboard(CashOutAgent.this);            }
            });

            HiddenPassTransformationMethod hiddenPassTransformationMethod=new HiddenPassTransformationMethod();
            et_mpin.setTransformationMethod(hiddenPassTransformationMethod);
            et_mpin.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int RIGHT = 2;
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (et_mpin.getRight() - et_mpin.getCompoundDrawables()[RIGHT].getBounds().width())) {
                            int selection = et_mpin.getSelectionEnd();
                            if (isPasswordVisible) {
                                // set drawable image
                                et_mpin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                                // hide Password
                                et_mpin.setTransformationMethod(hiddenPassTransformationMethod);
                                isPasswordVisible = false;
                            } else  {
                                // set drawable image
                                et_mpin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                                // show Password
                                et_mpin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                isPasswordVisible = true;
                            }
                            et_mpin.setSelection(selection);
                            return true;
                        }
                    }
                    return false;
                }
            });


            et_otp.setTransformationMethod(hiddenPassTransformationMethod);
            et_otp.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int RIGHT = 2;
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (et_otp.getRight() - et_otp.getCompoundDrawables()[RIGHT].getBounds().width())) {
                            int selection = et_otp.getSelectionEnd();
                            if (isPasswordVisible2) {
                                // set drawable image
                                et_otp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                                // hide Password
                                et_otp.setTransformationMethod(hiddenPassTransformationMethod);
                                isPasswordVisible2 = false;
                            } else  {
                                // set drawable image
                                et_otp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                                // show Password
                                et_otp.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                isPasswordVisible2 = true;
                            }
                            et_otp.setSelection(selection);
                            return true;
                        }
                    }
                    return false;
                }
            });

            if (new InternetCheck().isConnected(CashOutAgent.this)) {

                MyApplication.showloader(CashOutAgent.this, getString(R.string.getting_user_info));

                api_walletOwner_agent();

            } else {
                Toast.makeText(CashOutAgent.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
            }


        } catch (Exception e) {
            Toast.makeText(CashOutAgent.this, e.toString(), Toast.LENGTH_LONG).show();

        }

    }

    private void api_walletOwner_agent() {


        API.GET_TRANSFER_DETAILS("ewallet/api/v1/walletOwner/"+walletOwnerCode_mssis_agent,languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {


                    //    JSONObject jsonObject1 = new JSONObject("{\"transactionId\":\"1927802\",\"requestTime\":\"Tue Nov 02 13:03:30 IST 2021\",\"responseTime\":\"Tue Nov 02 13:03:30 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwner\":{\"id\":110679,\"code\":\"1000002785\",\"walletOwnerCategoryCode\":\"100000\",\"ownerName\":\"sharique agent\",\"mobileNumber\":\"9990063618\",\"businessTypeCode\":\"100008\",\"businessTypeName\":\"Goldsmith\",\"lineOfBusiness\":\"gffg\",\"idProofNumber\":\"trt465656\",\"email\":\"sharique9718@gmail.com\",\"status\":\"Active\",\"state\":\"Approved\",\"stage\":\"Document\",\"idProofTypeCode\":\"100005\",\"idProofTypeName\":\"COMPANY REGISTRATION NUMBER\",\"idExpiryDate\":\"2021-10-22\",\"notificationLanguage\":\"en\",\"notificationTypeCode\":\"100000\",\"notificationName\":\"EMAIL\",\"issuingCountryCode\":\"100102\",\"issuingCountryName\":\"India\",\"registerCountryCode\":\"100102\",\"registerCountryName\":\"India\",\"createdBy\":\"100250\",\"modifiedBy\":\"100308\",\"creationDate\":\"2021-10-19T22:38:48.969+0530\",\"modificationDate\":\"2021-11-01T13:49:14.892+0530\",\"walletExists\":true,\"profileTypeCode\":\"100000\",\"profileTypeName\":\"tier1\",\"walletCurrencyList\":[\"100018\",\"100017\",\"100069\",\"100020\",\"100004\",\"100029\",\"100062\",\"100003\"],\"walletOwnerCatName\":\"Institute\",\"requestedSource\":\"ADMIN\",\"regesterCountryDialCode\":\"+91\",\"issuingCountryDialCode\":\"+91\",\"walletOwnerCode\":\"1000002785\"}}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {

                        JSONObject jsonObject_walletOwner = jsonObject.getJSONObject("walletOwner");
                        rp_tv_businessType.setText(jsonObject_walletOwner.getString("businessTypeName"));

                    } else {
                        Toast.makeText(CashOutAgent.this, resultDescription, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(CashOutAgent.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashOutAgent.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setBackMenu() {
        imgBack = findViewById(R.id.imgBack);
        imgHome = findViewById(R.id.imgHome);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(CashOutAgent.this);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(CashOutAgent.this);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    boolean validation_mobile_Details() {

        mobileNoStr = edittext_mobileNuber.getText().toString().trim();
        amountstr = edittext_amount.getText().toString().trim();


        if (mobileNoStr.isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.subscriber_number_new));

            return false;
        } else if (mobileNoStr.length() < 9) {

            MyApplication.showErrorToast(this, getString(R.string.enter_phone_no_val));

            return false;
        }

        if (amountstr.isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.plz_enter_amount));

            return false;
        }

//        else if (amountstr.trim().length() < 4) {
//
//            MyApplication.showErrorToast(this, getString(R.string.minimum_amount_1000));
//
//            return false;
//        }

        if (amountstr.trim().length() > 5) {

            MyApplication.showErrorToast(this, getString(R.string.maximum_amount_10000));

            return false;
        }



        return true;
    }


    private void subscriber_details_api_walletownerUser() {

        String walletOwnerCategoryCode = MyApplication.getSaveString("walletOwnerCategoryCode", CashOutAgent.this);

        walletOwnerCategoryCode = "100010"; // HARD CODE FINAL ACORDING TO PARVEEN


        API.GET_CASHOUT_DETAILS("ewallet/api/v1/walletOwner/all?walletOwnerCategoryCode=" + walletOwnerCategoryCode + "&mobileNumber=" + mobileNoStr + "&offset=0&limit=500", languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    //JSONObject jsonObject = new JSONObject("{"transactionId":"1789327","requestTime":"Wed Oct 20 15:55:16 IST 2021","responseTime":"Wed Oct 20 15:55:16 IST 2021","resultCode":"0","resultDescription":"Transaction Successful","pageable":{"limit":500,"offset":0,"totalRecords":1},"walletOwnerList":[{"id":110382,"code":"1000002488","walletOwnerCategoryCode":"100010","ownerName":"Kundan","mobileNumber":"118110111","idProofNumber":"vc12345","email":"kundan.kumar@esteltelecom.com","status":"Active","state":"Approved","stage":"Document","idProofTypeCode":"100006","idProofTypeName":"OTHER","idExpiryDate":"2021-09-29","notificationLanguage":"en","notificationTypeCode":"100000","notificationName":"EMAIL","gender":"M","dateOfBirth":"1960-01-26","lastName":"New","issuingCountryCode":"100092","issuingCountryName":"Guinea","registerCountryCode":"100092","registerCountryName":"Guinea","createdBy":"100375","modifiedBy":"100322","creationDate":"2021-09-16T17:08:49.796+0530","modificationDate":"2021-09-16T17:10:17.009+0530","walletExists":true,"profileTypeCode":"100001","profileTypeName":"tier2","walletOwnerCatName":"Subscriber","occupationTypeCode":"100002","occupationTypeName":"Others","requestedSource":"ADMIN","regesterCountryDialCode":"+224","issuingCountryDialCode":"+224","walletOwnerCode":"1000002488"}]}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {


                        //  Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();


                        JSONArray jsonArray = jsonObject.getJSONArray("walletOwnerList");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            walletOwnerCode_subs = jsonObject2.getString("walletOwnerCode");

                            countryCode_subscriber = jsonObject2.getString("registerCountryCode");
                            agentCode_subscriber = jsonObject2.getString("code");

                            rp_tv_mobileNumber.setText(MyApplication.getSaveString("USERNAME", CashOutAgent.this));
                            rp_tv_email.setText(jsonObject2.getString("email"));

                            rp_tv_country.setText(countryName_agent);


                            receivernameStr = jsonObject2.getString("ownerName");
                            rp_tv_receiverName.setText(receivernameStr);


//                                JSONObject walletTransfer = jsonObject.getJSONObject("walletTransfer");
//                                JSONObject srcWalletOwner = walletTransfer.getJSONObject("srcWalletOwner");
//                                rp_tv_businessType.setText(srcWalletOwner.getString("businessTypeName"));


                        }

                      api_currency_sender();

                    } else {
                        Toast.makeText(CashOutAgent.this, resultDescription, Toast.LENGTH_LONG).show();
                        //  finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(CashOutAgent.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashOutAgent.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void api_currency_subscriber() {

        API.GET_CASHOUT_DETAILS("ewallet/api/v1/walletOwnerCountryCurrency/" + walletOwnerCode_subs, languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    //JSONObject jsonObject = new JSONObject("{"transactionId":"1789322","requestTime":"Wed Oct 20 15:53:33 IST 2021","responseTime":"Wed Oct 20 15:53:33 IST 2021","resultCode":"0","resultDescription":"Transaction Successful","walletOwnerCountryCurrencyList":[{"id":7452,"code":"107451","walletOwnerCode":"1000002488","currencyCode":"100062","currencyName":"GNF","currencySymbol":"Fr","countryCurrencyCode":"100076","inBound":true,"outBound":true,"status":"Active"}]}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("walletOwnerCountryCurrencyList");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            if(jsonObject2.has("currencyName")) {

                                String  currencyName_subscriber_temp = jsonObject2.getString("currencyName");
                                if (currencyName_subscriber_temp.equalsIgnoreCase("GNF")) {
                                    currencyName_subscriber = jsonObject2.getString("currencyName");
                                    currencyCode_subscriber = jsonObject2.getString("currencyCode");
                                    currencySymbol_receiver = jsonObject2.getString("currencySymbol");

                                } else {

                                }
                            }
                        }



                        //  Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();


                        api_exchange();


                    } else {
                        Toast.makeText(CashOutAgent.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(CashOutAgent.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashOutAgent.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void service_Provider_api() {

        // Hard Code Final Deepak

        API.GET_CASHOUT_DETAILS("ewallet/api/v1/serviceProvider/serviceCategory?serviceCode=100003&serviceCategoryCode=100012&status=Y", languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1789408\",\"requestTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"responseTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwnerUser\":{\"id\":2171,\"code\":\"101917\",\"firstName\":\"TATASnegal\",\"userName\":\"TATASnegal5597\",\"mobileNumber\":\"8888888882\",\"email\":\"kundan.kumar@esteltelecom.com\",\"walletOwnerUserTypeCode\":\"100000\",\"walletOwnerUserTypeName\":\"Supervisor\",\"walletOwnerCategoryCode\":\"100000\",\"walletOwnerCategoryName\":\"Institute\",\"userCode\":\"1000002606\",\"status\":\"Active\",\"state\":\"Approved\",\"gender\":\"M\",\"idProofTypeCode\":\"100004\",\"idProofTypeName\":\"MILITARY ID CARD\",\"idProofNumber\":\"44444444444\",\"creationDate\":\"2021-10-01T09:04:07.330+0530\",\"notificationName\":\"EMAIL\",\"notificationLanguage\":\"en\",\"createdBy\":\"100308\",\"modificationDate\":\"2021-10-20T14:59:00.791+0530\",\"modifiedBy\":\"100308\",\"addressList\":[{\"id\":3569,\"walletOwnerUserCode\":\"101917\",\"addTypeCode\":\"100001\",\"addTypeName\":\"Commercial\",\"regionCode\":\"100068\",\"regionName\":\"Boke\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"city\":\"100022\",\"cityName\":\"Dubreka\",\"addressLine1\":\"delhi\",\"status\":\"Inactive\",\"creationDate\":\"2021-10-01T09:04:07.498+0530\",\"createdBy\":\"100250\",\"modificationDate\":\"2021-10-03T09:52:57.407+0530\",\"modifiedBy\":\"100308\"}],\"workingDaysList\":[{\"id\":3597,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100000\",\"weekdaysName\":\"Monday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.518+0530\"},{\"id\":3598,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100001\",\"weekdaysName\":\"Tuesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.528+0530\"},{\"id\":3599,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100002\",\"weekdaysName\":\"Wednesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.538+0530\"},{\"id\":3600,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100003\",\"weekdaysName\":\"Thursday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.547+0530\"},{\"id\":3601,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100004\",\"weekdaysName\":\"Friday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.556+0530\"},{\"id\":3602,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100005\",\"weekdaysName\":\"Saturday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.565+0530\"},{\"id\":3603,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100006\",\"weekdaysName\":\"Sunday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"2:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.573+0530\"}],\"macEnabled\":false,\"ipEnabled\":false,\"resetCredReqInit\":false,\"notificationTypeCode\":\"100000\"}}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("serviceProviderList");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            serviceCode_from_serviceCategory = jsonObject2.getString("serviceCode");
                            serviceCategoryCode_from_serviceCategory = jsonObject2.getString("serviceCategoryCode");
                            serviceProviderCode_from_serviceCategory = jsonObject2.getString("code");

                        }

                        agent_details_api_walletownerUser();


                    } else {
                        Toast.makeText(CashOutAgent.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(CashOutAgent.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashOutAgent.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    private void agent_details_api_walletownerUser() {

        String USER_CODE_FROM_TOKEN_AGENTDETAILS = MyApplication.getSaveString("userCode", CashOutAgent.this);


        API.GET_CASHOUT_DETAILS("ewallet/api/v1/walletOwnerUser/" + USER_CODE_FROM_TOKEN_AGENTDETAILS, languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1789408\",\"requestTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"responseTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwnerUser\":{\"id\":2171,\"code\":\"101917\",\"firstName\":\"TATASnegal\",\"userName\":\"TATASnegal5597\",\"mobileNumber\":\"8888888882\",\"email\":\"kundan.kumar@esteltelecom.com\",\"walletOwnerUserTypeCode\":\"100000\",\"walletOwnerUserTypeName\":\"Supervisor\",\"walletOwnerCategoryCode\":\"100000\",\"walletOwnerCategoryName\":\"Institute\",\"userCode\":\"1000002606\",\"status\":\"Active\",\"state\":\"Approved\",\"gender\":\"M\",\"idProofTypeCode\":\"100004\",\"idProofTypeName\":\"MILITARY ID CARD\",\"idProofNumber\":\"44444444444\",\"creationDate\":\"2021-10-01T09:04:07.330+0530\",\"notificationName\":\"EMAIL\",\"notificationLanguage\":\"en\",\"createdBy\":\"100308\",\"modificationDate\":\"2021-10-20T14:59:00.791+0530\",\"modifiedBy\":\"100308\",\"addressList\":[{\"id\":3569,\"walletOwnerUserCode\":\"101917\",\"addTypeCode\":\"100001\",\"addTypeName\":\"Commercial\",\"regionCode\":\"100068\",\"regionName\":\"Boke\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"city\":\"100022\",\"cityName\":\"Dubreka\",\"addressLine1\":\"delhi\",\"status\":\"Inactive\",\"creationDate\":\"2021-10-01T09:04:07.498+0530\",\"createdBy\":\"100250\",\"modificationDate\":\"2021-10-03T09:52:57.407+0530\",\"modifiedBy\":\"100308\"}],\"workingDaysList\":[{\"id\":3597,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100000\",\"weekdaysName\":\"Monday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.518+0530\"},{\"id\":3598,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100001\",\"weekdaysName\":\"Tuesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.528+0530\"},{\"id\":3599,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100002\",\"weekdaysName\":\"Wednesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.538+0530\"},{\"id\":3600,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100003\",\"weekdaysName\":\"Thursday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.547+0530\"},{\"id\":3601,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100004\",\"weekdaysName\":\"Friday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.556+0530\"},{\"id\":3602,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100005\",\"weekdaysName\":\"Saturday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.565+0530\"},{\"id\":3603,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100006\",\"weekdaysName\":\"Sunday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"2:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.573+0530\"}],\"macEnabled\":false,\"ipEnabled\":false,\"resetCredReqInit\":false,\"notificationTypeCode\":\"100000\"}}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {

                        JSONObject walletOwnerUser = jsonObject.getJSONObject("walletOwnerUser");

                        if(walletOwnerUser.has("issuingCountryCode")){
                            countryCode_agent = walletOwnerUser.getString("issuingCountryCode");
                        }else{
                            countryCode_agent = "";
                        }
                        if(walletOwnerUser.has("issuingCountryName")){
                            countryName_agent = walletOwnerUser.getString("issuingCountryName");
                        }else{
                            countryName_agent = "";
                        }
                        if(walletOwnerUser.has("firstName")){
                            senderNameAgent = walletOwnerUser.getString("firstName");
                        }else{
                            senderNameAgent = "";
                        }
                        rp_tv_senderName.setText(senderNameAgent);

                        subscriber_details_api_walletownerUser();


                    } else {
                        Toast.makeText(CashOutAgent.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(CashOutAgent.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashOutAgent.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void allByCriteria_walletOwnerCode_api() {


        API.GET_CASHOUT_DETAILS("ewallet/api/v1/channel/allByCriteria?walletOwnerCode=" + walletOwnerCode_mssis_agent, languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    //JSONObject jsonObject = new JSONObject("{"transactionId":"1789322","requestTime":"Wed Oct 20 15:53:33 IST 2021","responseTime":"Wed Oct 20 15:53:33 IST 2021","resultCode":"0","resultDescription":"Transaction Successful","walletOwnerCountryCurrencyList":[{"id":7452,"code":"107451","walletOwnerCode":"1000002488","currencyCode":"100062","currencyName":"GNF","currencySymbol":"Fr","countryCurrencyCode":"100076","inBound":true,"outBound":true,"status":"Active"}]}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {




                        otp_generate_api();


                    } else {
                        Toast.makeText(CashOutAgent.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(CashOutAgent.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashOutAgent.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    boolean validation_otp_detail() {

        otpStr = et_otp.getText().toString();
        if (otpStr.trim().isEmpty()) {
            MyApplication.showErrorToast(this, getString(R.string.plz_enter_6_digit_otp_code));

            return false;
        } else if (otpStr.trim().length() == 6) {

            return true;
        } else {

            MyApplication.showErrorToast(this, getString(R.string.plz_enter_6_digit_otp_code));

            return false;
        }
    }

    boolean validation_mpin_detail() {

        mpinStr = et_mpin.getText().toString();

        if (mpinStr.trim().isEmpty()) {
            MyApplication.showErrorToast(this, getString(R.string.please_enter_4_digit_mpin));

            return false;
        } else if (mpinStr.trim().length() == 4) {


            return true;
        } else {

            MyApplication.showErrorToast(this, getString(R.string.please_enter_4_digit_mpin));

            return false;
        }
    }


    private void mpin_final_api() {

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("transactionType", "100001");         // Hard Code according  to Deepak

            jsonObject.put("srcWalletOwnerCode",agentCode_subscriber);
            jsonObject.put("desWalletOwnerCode",walletOwnerCode_mssis_agent);


            jsonObject.put("srcCurrencyCode",currencyCode_agent);
            jsonObject.put("desCurrencyCode",currencyCode_subscriber);

            jsonObject.put("value", amountstr);
            String encryptionDatanew = AESEncryption.getAESEncryption(mpinStr);
            jsonObject.put("pin", encryptionDatanew);
            jsonObject.put("channelTypeCode", "100002");           // Hard Code according  to Deepak
            jsonObject.put("serviceCode", serviceCode_from_serviceCategory);
            jsonObject.put("serviceCategoryCode", serviceCategoryCode_from_serviceCategory);  // Hard Code according  to Deepak
            jsonObject.put("serviceProviderCode", serviceProviderCode_from_serviceCategory);  // Hard Code according  to Deepak


            API.POST_CASHOUT_MPIN("ewallet/api/v1/walletTransfer/cashOut", jsonObject, languageToUse, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {


                       // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"116510\",\"requestTime\":\"Thu Oct 21 23:44:26 IST 2021\",\"responseTime\":\"Thu Oct 21 23:44:27 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletTransfer\":{\"code\":\"116510\",\"srcWalletCode\":\"1000022471\",\"desWalletCode\":\"1000024941\",\"srcWalletOwnerCode\":\"1000002488\",\"desWalletOwnerCode\":\"1000002606\",\"srcWalletTypeCode\":\"100008\",\"desWalletTypeCode\":\"100008\",\"srcCurrencyCode\":\"100062\",\"desCurrencyCode\":\"100062\",\"srcCurrencyName\":\"GNF\",\"desCurrencyName\":\"GNF\",\"srcCurrencySymbol\":\"Fr\",\"desCurrencySymbol\":\"Fr\",\"value\":11469,\"createdBy\":\"101917\",\"creationDate\":\"2021-10-21 23:44:27\",\"fee\":1300,\"finalAmount\":10000,\"srcWalletOwner\":{\"id\":110382,\"code\":\"1000002488\",\"walletOwnerCategoryCode\":\"100010\",\"ownerName\":\"Kundan\",\"mobileNumber\":\"118110111\",\"idProofNumber\":\"vc12345\",\"email\":\"kundan.kumar@esteltelecom.com\",\"status\":\"Active\",\"state\":\"Approved\",\"stage\":\"Document\",\"idProofTypeCode\":\"100006\",\"idProofTypeName\":\"OTHER\",\"idExpiryDate\":\"2021-09-29\",\"notificationLanguage\":\"en\",\"notificationTypeCode\":\"100000\",\"notificationName\":\"EMAIL\",\"gender\":\"M\",\"dateOfBirth\":\"1960-01-26\",\"lastName\":\"New\",\"issuingCountryCode\":\"100092\",\"issuingCountryName\":\"Guinea\",\"registerCountryCode\":\"100092\",\"registerCountryName\":\"Guinea\",\"createdBy\":\"100375\",\"modifiedBy\":\"100322\",\"creationDate\":\"2021-09-16T17:08:49.796+0530\",\"modificationDate\":\"2021-10-21T23:22:25.514+0530\",\"walletExists\":true,\"profileTypeCode\":\"100001\",\"profileTypeName\":\"tier2\",\"walletOwnerCatName\":\"Subscriber\",\"occupationTypeCode\":\"100002\",\"occupationTypeName\":\"Others\",\"requestedSource\":\"ADMIN\",\"regesterCountryDialCode\":\"+224\",\"issuingCountryDialCode\":\"+224\",\"walletOwnerCode\":\"1000002488\"},\"desWalletOwner\":{\"id\":110500,\"code\":\"1000002606\",\"walletOwnerCategoryCode\":\"100000\",\"ownerName\":\"TATASnegal\",\"mobileNumber\":\"8888888882\",\"businessTypeCode\":\"100001\",\"businessTypeName\":\"Telecom\",\"idProofNumber\":\"44444444444\",\"email\":\"kundan.kumar@esteltelecom.com\",\"status\":\"Active\",\"state\":\"Approved\",\"stage\":\"Document\",\"idProofTypeCode\":\"100004\",\"idProofTypeName\":\"MILITARY ID CARD\",\"notificationLanguage\":\"en\",\"notificationTypeCode\":\"100000\",\"notificationName\":\"EMAIL\",\"registerCountryCode\":\"100092\",\"registerCountryName\":\"Guinea\",\"createdBy\":\"100250\",\"modifiedBy\":\"100308\",\"creationDate\":\"2021-10-01T09:01:15.968+0530\",\"modificationDate\":\"2021-10-01T09:10:25.037+0530\",\"walletExists\":true,\"profileTypeCode\":\"100000\",\"profileTypeName\":\"tier1\",\"walletCurrencyList\":[\"100014\",\"100013\",\"100012\",\"100007\",\"100010\",\"100008\",\"100005\",\"100002\",\"100001\",\"100003\",\"100069\",\"100062\",\"100004\",\"100000\",\"100028\",\"100027\",\"100026\",\"100024\",\"100021\",\"100020\",\"100019\",\"100017\",\"100015\",\"100018\",\"100058\"],\"walletOwnerCatName\":\"Institute\",\"requestedSource\":\"ADMIN\",\"regesterCountryDialCode\":\"+224\",\"walletOwnerCode\":\"1000002606\"},\"taxConfigurationList\":[{\"taxTypeCode\":\"100133\",\"taxTypeName\":\"Financial Tax\",\"value\":169,\"taxAvailBy\":\"Fee Amount\"}],\"transactionType\":\"CASH-OUT\"}}");

                        String resultCode = jsonObject.getString("resultCode");
                        String resultDescription = jsonObject.getString("resultDescription");

                        if (resultCode.equalsIgnoreCase("0")) {


                            ll_page_1.setVisibility(View.GONE);
                            ll_reviewPage.setVisibility(View.GONE);
                            ll_receiptPage.setVisibility(View.GONE);
                            ll_receiptPage.setVisibility(View.GONE);
                            ll_successPage.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ll_page_1.setVisibility(View.GONE);
                                    ll_reviewPage.setVisibility(View.GONE);
                                    ll_successPage.setVisibility(View.GONE);
                                    ll_receiptPage.setVisibility(View.VISIBLE);
                                }
                            }, 2000);


                            receiptPage_tv_stransactionType.setText("CASH-OUT");
                            receiptPage_tv_transactionAmount.setText(currencySymbol_sender +" "+ amountstr);
                            receiptPage_tv_fee.setText(currencySymbol_sender +" " + fees_amount);
                            receiptPage_tv_financialtax.setText(currencySymbol_sender +" "+ tax_financial);
                            receipt_tv_amount_to_be_charge.setText(currencySymbol_sender+" "+ totalAmount_str);
                            receiptPage_tv_transaction_receiptNo.setText(jsonObject.getString("transactionId"));
                            receiptPage_tv_dateOfTransaction.setText(jsonObject.getString("responseTime"));


                            receiptPage_tv_amount_to_be_paid.setText(currencySymbol_receiver +" " + amountstr);



                            receiptPage_tv_sender_name.setText(senderNameAgent);
                            receiptPage_tv_sender_phoneNo.setText(MyApplication.getSaveString("USERNAME", CashOutAgent.this));
                            receiptPage_tv_sender_name.setText(senderNameAgent);
                            receiptPage_tv_sender_phoneNo.setText(MyApplication.getSaveString("USERNAME", CashOutAgent.this));
                            receiptPage_tv_receiver_name.setText(receivernameStr);
                            receiptPage_tv_receiver_phoneNo.setText(mobileNoStr);



                        } else {
                            Toast.makeText(CashOutAgent.this, resultDescription, Toast.LENGTH_LONG).show();
                            //  finish();
                        }


                    } catch (Exception e) {
                        Toast.makeText(CashOutAgent.this, e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }


                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(CashOutAgent.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    } else {

                        Toast.makeText(CashOutAgent.this, aFalse, Toast.LENGTH_SHORT).show();
                    }
                    }
            });

        } catch (Exception e) {
            Toast.makeText(CashOutAgent.this, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }





    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_nextClick: {


                if (validation_mobile_Details()) {

                    if (new InternetCheck().isConnected(CashOutAgent.this)) {

                        MyApplication.showloader(CashOutAgent.this, getString(R.string.getting_user_info));


                        service_Provider_api();

                    } else {
                        Toast.makeText(CashOutAgent.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                    }
                }
            }

            break;

            case R.id.confirm_reviewClick_textview: {


                if (selectClickType.equalsIgnoreCase("select_otp")) {

                    if (validation_otp_detail()) {
                        if (new InternetCheck().isConnected(CashOutAgent.this)) {
                            MyApplication.showloader(CashOutAgent.this, getString(R.string.getting_user_info));

                           otp_verify_api();

                        } else {
                            Toast.makeText(CashOutAgent.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                else if (selectClickType.equalsIgnoreCase("select_subscriber_mpin")) {
                    if (validation_mpin_detail()) {
                        if (new InternetCheck().isConnected(CashOutAgent.this)) {
                            MyApplication.showloader(CashOutAgent.this, getString(R.string.getting_user_info));


                           api_mpin_subscriber();

                        } else {
                            Toast.makeText(CashOutAgent.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                else if (selectClickType.equalsIgnoreCase("select_mpin")) {
                    if (validation_mpin_detail()) {
                        if (new InternetCheck().isConnected(CashOutAgent.this)) {
                            MyApplication.showloader(CashOutAgent.this, getString(R.string.getting_user_info));


                            mpin_final_api();

                        } else {
                            Toast.makeText(CashOutAgent.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                        }
                    }
                }


            }
            break;

            case R.id.previous_reviewClick_textview: {

                ll_page_1.setVisibility(View.VISIBLE);
                ll_reviewPage.setVisibility(View.GONE);
                ll_receiptPage.setVisibility(View.GONE);
            }
            break;

            case R.id.tvContinue: {

                ll_page_1.setVisibility(View.GONE);
                ll_reviewPage.setVisibility(View.GONE);
                ll_successPage.setVisibility(View.GONE);
                ll_receiptPage.setVisibility(View.GONE);
                ll_receiptPage.setVisibility(View.VISIBLE);


            }
            break;

            case R.id.qrCode_imageButton: {

                if (checkPermission()) {

                    qrScan();

                } else {
                    requestPermission();
                }

            }

            break;

            case R.id.close_receiptPage_textview: {

//
//                ll_page_1.setVisibility(View.VISIBLE);
//                ll_reviewPage.setVisibility(View.GONE);
//                ll_receiptPage.setVisibility(View.GONE);

                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            break;


            case R.id.ll_resendOtp: {


                if (new InternetCheck().isConnected(CashOutAgent.this)) {

                    MyApplication.showloader(CashOutAgent.this, getString(R.string.getting_user_info));

                    otp_generate_api();

                } else {
                    Toast.makeText(CashOutAgent.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                }
            }
            break;

            case R.id.exportReceipt_textview: {
                close_receiptPage_textview.setVisibility(View.GONE);
                exportReceipt_textview.setVisibility(View.GONE);
                Bitmap bitmap = getScreenShot(rootView);
                createImageFile(bitmap);
                //store(bitmap, "test.jpg");
            }

            break;



        }
    }
    private void api_currency_sender() {

        API.GET_CASHIN_DETAILS("ewallet/api/v1/walletOwnerCountryCurrency/"+walletOwnerCode_mssis_agent,languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    //JSONObject jsonObject = new JSONObject("{"transactionId":"1789322","requestTime":"Wed Oct 20 15:53:33 IST 2021","responseTime":"Wed Oct 20 15:53:33 IST 2021","resultCode":"0","resultDescription":"Transaction Successful","walletOwnerCountryCurrencyList":[{"id":7452,"code":"107451","walletOwnerCode":"1000002488","currencyCode":"100062","currencyName":"GNF","currencySymbol":"Fr","countryCurrencyCode":"100076","inBound":true,"outBound":true,"status":"Active"}]}");

                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");

                    if(resultCode.equalsIgnoreCase("0")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("walletOwnerCountryCurrencyList");
                        for(int i=0;i<jsonArray.length();i++) {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);


                            if(jsonObject2.has("currencyName")) {

                                String  currencyName_agent_temp = jsonObject2.getString("currencyName");
                                if (currencyName_agent_temp.equalsIgnoreCase("GNF")) {
                                    currencyCode_agent = jsonObject2.getString("currencyCode");
                                    currencyName_agent = jsonObject2.getString("currencyName");
                                    currencySymbol_sender = jsonObject2.getString("currencySymbol");

                                } else {

                                }
                            }

                        }


                        api_currency_subscriber();

                    }

                    else {
                        Toast.makeText(CashOutAgent.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(CashOutAgent.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashOutAgent.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void api_exchange() {


             API.GET_CASHOUT_DETAILS("ewallet/api/v1/exchangeRate/getAmountDetails?sendCurrencyCode=" + currencyCode_agent +
                    "&receiveCurrencyCode="+currencyCode_subscriber+"&sendCountryCode=" + countryCode_agent + "&receiveCountryCode="+countryCode_subscriber+
                    "&currencyValue=" + amountstr + "&channelTypeCode=100002&serviceCode=" + serviceCode_from_serviceCategory + "&serviceCategoryCode=" + serviceCategoryCode_from_serviceCategory + "&serviceProviderCode=" +
            serviceProviderCode_from_serviceCategory + "&walletOwnerCode=" + walletOwnerCode_subs + "&remitAgentCode=" +
                     walletOwnerCode_mssis_agent + "&payAgentCode="+agentCode_subscriber,languageToUse, new Api_Responce_Handler() {


                @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    //JSONObject jsonObject = new JSONObject("{"transactionId":"1789322","requestTime":"Wed Oct 20 15:53:33 IST 2021","responseTime":"Wed Oct 20 15:53:33 IST 2021","resultCode":"0","resultDescription":"Transaction Successful","walletOwnerCountryCurrencyList":[{"id":7452,"code":"107451","walletOwnerCode":"1000002488","currencyCode":"100062","currencyName":"GNF","currencySymbol":"Fr","countryCurrencyCode":"100076","inBound":true,"outBound":true,"status":"Active"}]}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {

                        //Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();

                        JSONObject exchangeRate = jsonObject.getJSONObject("exchangeRate");

                        fees_amount = exchangeRate.getString("fee");
                        rp_tv_fees_reveiewPage.setText(currencySymbol_sender+" "  + fees_amount);

                        //  credit_amount=exchangeRate.getString("currencyValue");

                        if(exchangeRate.has("taxConfigurationList"))
                        {
                            JSONArray jsonArray = exchangeRate.getJSONArray("taxConfigurationList");
                            for(int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                tax_financial = jsonObject2.getString("value");
                            }
                        }
                        else {
                            tax_financial = exchangeRate.getString("value");
                        }




                        rp_tv_financialTax.setText(currencySymbol_sender+" "  + tax_financial);

                        tax_financial_double = Double.parseDouble(tax_financial);
                        fees_amount_double = Double.parseDouble(fees_amount);
                        amountstr_double = Double.parseDouble(amountstr);

                        totalAmount_double = tax_financial_double + amountstr_double + fees_amount_double;
                        totalAmount_str = String.valueOf(totalAmount_double);
                        rp_tv_amount_to_be_charge.setText(currencySymbol_sender+" "+ totalAmount_str);

                        amountstr = String.valueOf(amountstr_double);
                        rp_tv_transactionAmount.setText(currencySymbol_sender+" " + amountstr);
                        rp_tv_amount_to_be_paid.setText(currencySymbol_receiver+" "  +amountstr);

                        allByCriteria_walletOwnerCode_api();

                    } else {
                        Toast.makeText(CashOutAgent.this, resultDescription, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    Toast.makeText(CashOutAgent.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    finish();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashOutAgent.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }



    public void qrScan(){

        Intent i = new Intent(CashOutAgent.this, QrCodeActivity.class);
        startActivityForResult( i,REQUEST_CODE_QR_SCAN);
    }

    public static final int REQUEST_CODE = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {


            String requiredValue = data.getStringExtra("PHONE");
            edittext_mobileNuber.setText(requiredValue);
            edittext_amount.requestFocus();

        }
        if (resultCode != Activity.RESULT_OK) {


            Log.d("LOGTAG", "COULD NOT GET A GOOD RESULT.");
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (result != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(CashOutAgent.this).create();
                alertDialog.setTitle(getString(R.string.scan_error));
                alertDialog.setMessage(getString(R.string.val_scan_error_content));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;

        }


        if (requestCode == REQUEST_CODE_QR_SCAN) {

            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.d("LOGTAG", "Have scan result in your app activity :" + result);

            String[] date=result.split(":");

            callwalletOwnerDetailsQR(date[0]);



        }
    }




    public void callwalletOwnerDetailsQR(String Code){

        API.GET("ewallet/api/v1/walletOwner/"+Code, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){

                    mobileNoStr = jsonObject.optJSONObject("walletOwner").optString("mobileNumber","N/A");

                    edittext_mobileNuber.setText(mobileNoStr);
                    edittext_mobileNuber.setEnabled(false);

                    rp_tv_businessType.setText(jsonObject.optJSONObject("walletOwner").optString("businessTypeName"));

                    //  callwalletOwnerCountryCurrency();
                }else{

                    MyApplication.showToast(CashOutAgent.this,jsonObject.optString("resultDescription"));
                    mobileNoStr="";
                    edittext_mobileNuber.setText("");

                }

            }

            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(CashOutAgent.this,aFalse);
            }
        });
    }



    private void otp_generate_api() {
        try{

            JSONObject jsonObject=new JSONObject();

            String USERNAME =  MyApplication.getSaveString("USERNAME",CashOutAgent.this);
            String PASSWORD = MyApplication.getSaveString("PASSWORD",CashOutAgent.this);
            String EMAIL = MyApplication.getSaveString("EMAIL",CashOutAgent.this);
            String NTTYPECODE = MyApplication.getSaveString("NTTYPECODE",CashOutAgent.this);
            String USERCODE = MyApplication.getSaveString("USERCODE",CashOutAgent.this);

            /*
            jsonObject.put("email",EMAIL);
            jsonObject.put("notificationTypeCode",NTTYPECODE);
            jsonObject.put("transTypeCode","101813");      // Temporary Hard Code acording to Praveen
            jsonObject.put("status","Active");
            String USER_CODE_FROM_TOKEN_AGENTDETAILS =  MyApplication.getSaveString("userCode", CashOutAgent.this);
            jsonObject.put("walletOwnerUserCode",USER_CODE_FROM_TOKEN_AGENTDETAILS);

             */

            jsonObject.put("transTypeCode","100001");
           // jsonObject.put("transTypeCode","101813");
            jsonObject.put("subscriberWalletOwnerCode",USERCODE);


            API.POST_GET_OTP("ewallet/api/v1/otp",jsonObject,new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {

                        //  JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1771883\",\"requestTime\":\"Mon Oct 18 18:05:40 IST 2021\",\"responseTime\":\"Mon Oct 18 18:05:40 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\"}");

                        if (jsonObject.has("error")) {

                            String error = jsonObject.getString("error");
                            String error_message = jsonObject.getString("error_message");

                            Toast.makeText(CashOutAgent.this, error_message, Toast.LENGTH_LONG).show();

                            if(error.equalsIgnoreCase("1251"))
                            {

                                Intent i = new Intent(CashOutAgent.this, VerifyLoginAccountScreen.class);
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


                                Toast.makeText(CashOutAgent.this, getString(R.string.otp_has_send_sucessfully_subscriber_register), Toast.LENGTH_LONG).show();

                                confirm_reviewClick_textview.setText(getString(R.string.otp_verify));
                                selectClickType="select_otp";

                                ll_page_1.setVisibility(View.GONE);
                                ll_receiptPage.setVisibility(View.GONE);
                                ll_pin.setVisibility(View.GONE);

                                et_otp.setText("");
                                et_mpin.setText("");

                                ll_reviewPage.setVisibility(View.VISIBLE);
                                ll_otp.setVisibility(View.VISIBLE);
                                ll_resendOtp.setVisibility(View.VISIBLE);


                            }

                            else {
                                Toast.makeText(CashOutAgent.this, resultDescription, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    catch (Exception e)
                    {
                        Toast.makeText(CashOutAgent.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }


                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(CashOutAgent.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    }

                    else {

                        Toast.makeText(CashOutAgent.this, aFalse, Toast.LENGTH_SHORT).show();
                    }

                    //  MyApplication.showToast(LoginPin.this,aFalse);

                }
            });

        }catch (Exception e){

            MyApplication.hideLoader();

            MyApplication.showToast(CashOutAgent.this,e.toString());

        }

    }


    private void otp_verify_api() {
        try{

            JSONObject jsonObject=new JSONObject();

            jsonObject.put("transTypeCode","100001");      // Temporary Hard Code acording to Praveen
            jsonObject.put("otp",otpStr);


            API.POST_REQUEST_VERIFY_OTP("ewallet/api/v1/otp/verify",jsonObject,new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {

                        //JSONObject jsonObject = new JSONObject("");

                        if (jsonObject.has("error")) {

                            String error = jsonObject.getString("error");
                            String error_message = jsonObject.getString("error_message");

                            Toast.makeText(CashOutAgent.this, error_message, Toast.LENGTH_LONG).show();

                            if(error.equalsIgnoreCase("1251")) {

                                Intent i = new Intent(CashOutAgent.this, VerifyLoginAccountScreen.class);
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

                                selectClickType="select_subscriber_mpin";
                                confirm_reviewClick_textview.setText(getString(R.string.verify_subscriber_pin));
                                ll_otp.setVisibility(View.GONE);
                                ll_otp.setVisibility(View.GONE);
                                ll_resendOtp.setVisibility(View.GONE);
                                ll_pin.setVisibility(View.VISIBLE);


                            }

                            else {
                                Toast.makeText(CashOutAgent.this, resultDescription, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    catch (Exception e)
                    {
                        Toast.makeText(CashOutAgent.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(CashOutAgent.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(CashOutAgent.this, aFalse, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (Exception e){

            MyApplication.hideLoader();
            MyApplication.showToast(CashOutAgent.this,e.toString());
        }

    }
    private void api_mpin_subscriber() {
        try{

            JSONObject jsonObject=new JSONObject();

            String encryptionDatanew = AESEncryption.getAESEncryption(mpinStr);
            jsonObject.put("pin", encryptionDatanew.toLowerCase(Locale.ROOT));
            jsonObject.put("mobileNumber",mobileNoStr);


            API.POST_CASHOUT_MPIN("ewallet/api/v1/walletOwnerUser/verifyMPin",jsonObject,languageToUse,new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {

                        //JSONObject jsonObject = new JSONObject("");

                        if (jsonObject.has("error")) {

                            String error = jsonObject.getString("error");
                            String error_message = jsonObject.getString("error_message");

                            Toast.makeText(CashOutAgent.this, error_message, Toast.LENGTH_LONG).show();

                            if(error.equalsIgnoreCase("1251")) {

                                Intent i = new Intent(CashOutAgent.this, VerifyLoginAccountScreen.class);
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

                                et_mpin.setText("");
                                mpinStr="";

                                selectClickType="select_mpin";
                                confirm_reviewClick_textview.setText(getString(R.string.pin_capital));
                                ll_otp.setVisibility(View.GONE);
                                ll_otp.setVisibility(View.GONE);
                                ll_resendOtp.setVisibility(View.GONE);
                                ll_pin.setVisibility(View.VISIBLE);
                                tvFinger.setVisibility(View.VISIBLE);

                            }

                            else {
                                Toast.makeText(CashOutAgent.this, resultDescription, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    catch (Exception e)
                    {
                        Toast.makeText(CashOutAgent.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(CashOutAgent.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(CashOutAgent.this, aFalse, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (Exception e){

            MyApplication.hideLoader();
            MyApplication.showToast(CashOutAgent.this,e.toString());
        }

    }


//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
//    }


    public static Bitmap getScreenShot(View view) {
        View screenView = view;
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public  void createImageFile(Bitmap bm)  {
        try {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(System.currentTimeMillis());
            File storageDir = new File(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/");
            if (!storageDir.exists())
                storageDir.mkdirs();
            File image = File.createTempFile(
                    timeStamp,
                    ".jpeg",
                    storageDir
            );

            System.out.println(image.getAbsolutePath());
            if (image.exists()) image.delete();
            //   Log.i("LOAD", root + fname);
            try {
                FileOutputStream out = new FileOutputStream(image);
                bm.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            shareImage(image);
        }catch (Exception e){

        }
    }

//    public  void store(Bitmap bm, String fileName){
//        final  String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
//        File dir = new File(dirPath);
//        if(!dir.exists())
//            dir.mkdirs();
//        File file = new File(dirPath, fileName);
//        try {
//            FileOutputStream fOut = new FileOutputStream(file);
//            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
//            fOut.flush();
//            fOut.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        shareImage(file);
//    }

    private void shareImage(File file){
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.share_screenshot)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_app_available), Toast.LENGTH_SHORT).show();
        }
    }


}