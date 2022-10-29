package com.agent.cashmoovui.cashout;

import android.Manifest;
import android.content.ActivityNotFoundException;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.aldoapps.autoformatedittext.AutoFormatUtil;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.StringTokenizer;

public class CashOutCodeSubscriber extends AppCompatActivity implements View.OnClickListener {


    String currencySymbol_sender="";
    String currencySymbol_receiver="";

    boolean  isPasswordVisible,isPasswordVisible2;

    public static LoginPin loginpinC;
    ImageButton qrCode_imageButton;
    private static final int PERMISSION_REQUEST_CODE = 200;
    ImageView imgBack,imgHome;
    TextView exportReceipt_textview;
    View rootView;

    TextView tvAmtCurr,tvContinue,tv_nextClick, rp_tv_senderName,receiptPage_sender_mssidn,receiptPage_sbenificairay_mssidn,receiptPage_conversion_rate, receiptPage_confirmationCode,rp_tv_mobileNumber, rp_tv_businessType, rp_tv_email, rp_tv_country, rp_tv_receiverName, rp_tv_transactionAmount, rp_tv_fees_reveiewPage, receiptPage_tv_stransactionType, receiptPage_tv_dateOfTransaction, receiptPage_tv_transactionAmount,
            receiptPage_tv_amount_to_be_charged,receiptPage_amount_to_paid_receiptpage, receiptPage_tv_fee, receiptPage_tv_financialtax, receiptPage_tv_transaction_receiptNo, receiptPage_tv_sender_name,
            receiptPage_tv_sender_phoneNo,
            receiptPage_tv_receiver_name, receiptPage_tv_receiver_phoneNo, close_receiptPage_textview, rp_tv_financialTax, rp_tv_amount_to_be_charge, rp_tv_amount_to_be_credit, previous_reviewClick_textview, confirm_reviewClick_textview;
    LinearLayout ll_page_1, ll_reviewPage, ll_receiptPage, ll_pin, ll_otp, ll_resendOtp,ll_successPage;

    String selectClickType="";
    String senderName_str="",receiver_name_str;


    MyApplication applicationComponentClass;
    String languageToUse = "";

    EditText edittext_mobileNuber, edittext_amount, et_mpin, et_otp,edittext_confirmationCode;

    String mobileNoStr = "", amountstr = "", otpStr = "",confirmationCodeStr;

    String walletOwnerCode_mssis_agent = "", walletOwnerCode_subs, senderNameAgent = "";

    String currencyCode_agent = "", countryCode_agent = "", currencyName_agent = "";

    String tax_financial = "", fees_amount, totalAmount_str, senderName_susbcriber = "";
    Double tax_financial_double = 0.0, amountstr_double = 0.0, fees_amount_double = 0.0, totalAmount_double = 0.0;

    String mpinStr = "";


    String serviceCode_from_serviceCategory = "", serviceCategoryCode_from_serviceCategory = "", serviceProviderCode_from_serviceCategory;


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

            setContentView(R.layout.cash_out_code_subscriber);
            setBackMenu();

            rootView = getWindow().getDecorView().findViewById(R.id.main_layout);
            exportReceipt_textview = (TextView) findViewById(R.id.exportReceipt_textview);
            exportReceipt_textview.setOnClickListener(this);


            edittext_confirmationCode = (EditText) findViewById(R.id.edittext_confirmationCode);

            //     First page

            ll_page_1 = (LinearLayout) findViewById(R.id.ll_page_1);

            tv_nextClick = (TextView) findViewById(R.id.tv_nextClick);
            edittext_mobileNuber = (EditText) findViewById(R.id.edittext_mobileNuber);
            tvAmtCurr = findViewById(R.id.tvAmtCurr);
            edittext_amount = (EditText) findViewById(R.id.edittext_amount);
            ll_successPage = (LinearLayout) findViewById(R.id.ll_successPage);

            //    Reveiw page

            ll_reviewPage = (LinearLayout) findViewById(R.id.ll_reviewPage);

            rp_tv_senderName = (TextView) findViewById(R.id.rp_tv_senderName);
            rp_tv_mobileNumber = (TextView) findViewById(R.id.rp_tv_mobileNumber);
            rp_tv_businessType = (TextView) findViewById(R.id.rp_tv_businessType);
            rp_tv_email = (TextView) findViewById(R.id.rp_tv_email);
            rp_tv_country = (TextView) findViewById(R.id.rp_tv_country);
            rp_tv_receiverName = (TextView) findViewById(R.id.rp_tv_receiverName);
            rp_tv_transactionAmount = (TextView) findViewById(R.id.rp_tv_transactionAmount);
            rp_tv_fees_reveiewPage = (TextView) findViewById(R.id.rp_tv_fees_reveiewPage);
            rp_tv_financialTax = (TextView) findViewById(R.id.rp_tv_financialTax);
            rp_tv_amount_to_be_charge = (TextView) findViewById(R.id.rp_tv_amount_to_be_charge);
            rp_tv_amount_to_be_credit = (TextView) findViewById(R.id.rp_tv_amount_to_be_credit);


            et_mpin = (EditText) findViewById(R.id.et_mpin);
            previous_reviewClick_textview = (TextView) findViewById(R.id.previous_reviewClick_textview);
            confirm_reviewClick_textview = (TextView) findViewById(R.id.confirm_reviewClick_textview);

            tvContinue = (TextView) findViewById(R.id.tvContinue);
            tvContinue.setOnClickListener(this);

            TextView tvFinger =findViewById(R.id.tvFinger);
            if(MyApplication.setProtection!=null && !MyApplication.setProtection.isEmpty()) {
                if (MyApplication.setProtection.equalsIgnoreCase("Activate")) {
                    tvFinger.setVisibility(View.VISIBLE);
                } else {
                    tvFinger.setVisibility(View.GONE);
                }
            }else{
                tvFinger.setVisibility(View.VISIBLE);
            }
            tvFinger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.biometricAuth(CashOutCodeSubscriber.this, new BioMetric_Responce_Handler() {
                        @Override
                        public void success(String success) {
                            try {

                                //  String encryptionDatanew = AESEncryption.getAESEncryption(MyApplication.getSaveString("pin",MyApplication.appInstance).toString().trim());
                                mpinStr=MyApplication.getSaveString("pin",MyApplication.appInstance);

                                if (new InternetCheck().isConnected(CashOutCodeSubscriber.this)) {

                                    MyApplication.showloader(CashOutCodeSubscriber.this, getString(R.string.please_wait));


                                    mpin_final_api();


                                } else {
                                    Toast.makeText(CashOutCodeSubscriber.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failure(String failure) {
                            MyApplication.showToast(CashOutCodeSubscriber.this,failure);
                        }
                    });
                }
            });

            //    Receipt page

            ll_receiptPage = (LinearLayout) findViewById(R.id.ll_receiptPage);
            ll_successPage = (LinearLayout) findViewById(R.id.ll_successPage);


            receiptPage_conversion_rate = (TextView) findViewById(R.id.receiptPage_conversion_rate);
            receiptPage_sbenificairay_mssidn = (TextView) findViewById(R.id.receiptPage_sbenificairay_mssidn);
            receiptPage_confirmationCode = (TextView) findViewById(R.id.receiptPage_confirmationCode);
            receiptPage_sender_mssidn = (TextView) findViewById(R.id.receiptPage_sender_mssidn);
            receiptPage_tv_transaction_receiptNo = (TextView) findViewById(R.id.receiptPage_tv_transaction_receiptNo);
            receiptPage_tv_stransactionType = (TextView) findViewById(R.id.receiptPage_tv_stransactionType);
            receiptPage_tv_dateOfTransaction = (TextView) findViewById(R.id.receiptPage_tv_dateOfTransaction);
            receiptPage_tv_transactionAmount = (TextView) findViewById(R.id.receiptPage_tv_transactionAmount);
            receiptPage_tv_amount_to_be_charged = (TextView) findViewById(R.id.receiptPage_tv_amount_to_be_charged);
            receiptPage_amount_to_paid_receiptpage = (TextView) findViewById(R.id.receiptPage_amount_to_paid_receiptpage);
            receiptPage_tv_fee = (TextView) findViewById(R.id.receiptPage_tv_fee);
            receiptPage_tv_financialtax = (TextView) findViewById(R.id.receiptPage_tv_financialtax);
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



            walletOwnerCode_mssis_agent = MyApplication.getSaveString("walletOwnerCode", CashOutCodeSubscriber.this);


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
                        MyApplication.hideKeyboard(CashOutCodeSubscriber.this);            }
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


            edittext_amount.setEnabled(false);
          //  edittext_mobileNuber.setEnabled(false);
            edittext_amount.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (isFormatting) {
                        return;
                    }

                    if (s.length() > 0) {
                        formatInput(edittext_amount,s, s.length(), s.length());

                    }

                    isFormatting = false;



                }
            });

            edittext_confirmationCode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (new InternetCheck().isConnected(CashOutCodeSubscriber.this)) {

                        confirmationCodeStr = edittext_confirmationCode.getText().toString().trim();

                        //edittext_confirmationCode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


                        if (confirmationCodeStr.length()==11) {

                            api_confirmaCode();
                        }

                    } else {
                        Toast.makeText(CashOutCodeSubscriber.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                    }
                }
            });



        } catch (Exception e) {
            Toast.makeText(CashOutCodeSubscriber.this, e.toString(), Toast.LENGTH_LONG).show();

        }

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
                MyApplication.hideKeyboard(CashOutCodeSubscriber.this);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(CashOutCodeSubscriber.this);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    boolean validation_mobile_Details() {

        mobileNoStr = edittext_mobileNuber.getText().toString().trim();
        amountstr = edittext_amount.getText().toString().trim().replace(",","");
        confirmationCodeStr = edittext_confirmationCode.getText().toString().trim();
        mpinStr = et_mpin.getText().toString();




        if (confirmationCodeStr.isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.plz_enter_confirmation_code));

            return false;
        } else if (confirmationCodeStr.trim().length() < 4) {

            MyApplication.showErrorToast(this, getString(R.string.plz_enter_confirmation_code));

            return false;
        }


        if (mobileNoStr.isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.mobileNumber_star));

            return false;
        } else if (mobileNoStr.length() < 9) {

            MyApplication.showErrorToast(this, getString(R.string.mobileNumber_star));

            return false;
        }

        if (amountstr.isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.plz_enter_amount));

            return false;
        } else if (amountstr.trim().length() < 2) {

            MyApplication.showErrorToast(this, getString(R.string.plz_enter_amount));

            return false;
        }



        if (mpinStr.trim().isEmpty()) {
            MyApplication.showErrorToast(this, getString(R.string.please_enter_4_digit_mpin));

            return false;
        }

         if (mpinStr.trim().length() == 4) {

            return true;
        }
        else {

            MyApplication.showErrorToast(this, getString(R.string.please_enter_4_digit_mpin));

            return false;
        }


    }


    private void subscriber_details_api_walletownerUser() {

        String walletOwnerCategoryCode = MyApplication.getSaveString("walletOwnerCategoryCode", CashOutCodeSubscriber.this);

        walletOwnerCategoryCode = "100010"; // HARD CODE FINAL ACORDING TO PARVEEN



        API.GET_CASHOUT_CONFCODE_DETAILS("ewallet/api/v1/walletOwner/all?walletOwnerCategoryCode=" + walletOwnerCategoryCode + "&mobileNumber=" + mobileNoStr + "&offset=0&limit=500", languageToUse, new Api_Responce_Handler() {
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

                            rp_tv_mobileNumber.setText(MyApplication.getSaveString("USERNAME", CashOutCodeSubscriber.this));
                            rp_tv_email.setText(jsonObject2.getString("email"));
                            rp_tv_country.setText(jsonObject2.getString("issuingCountryName"));

                            senderName_susbcriber = jsonObject2.getString("ownerName");
                            rp_tv_receiverName.setText(senderName_susbcriber);
                            senderName_str = jsonObject2.getString("lastName");





//                                JSONObject walletTransfer = jsonObject.getJSONObject("walletTransfer");
//                                JSONObject srcWalletOwner = walletTransfer.getJSONObject("srcWalletOwner");
//                                rp_tv_businessType.setText(srcWalletOwner.getString("businessTypeName"));


                        }

                        currency_api();

                    } else {
                        Toast.makeText(CashOutCodeSubscriber.this, resultDescription, Toast.LENGTH_LONG).show();
                        //  finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(CashOutCodeSubscriber.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashOutCodeSubscriber.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void currency_api() {

        API.GET_CASHOUT_CONFCODE_DETAILS("ewallet/api/v1/walletOwnerCountryCurrency/" + walletOwnerCode_subs, languageToUse, new Api_Responce_Handler() {
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

                            currencyName_agent = jsonObject2.getString("currencyName");
                            countryCode_agent = jsonObject2.getString("countryCurrencyCode");
                            currencyCode_agent = jsonObject2.getString("currencyCode");

                        }

                        walletOwner_all_parent();

                        //exchange_rate_api();


                    } else {
                        Toast.makeText(CashOutCodeSubscriber.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(CashOutCodeSubscriber.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashOutCodeSubscriber.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void service_Provider_api() {

        // Hard Code Final Deepak

        API.GET_CASHOUT_CONFCODE_DETAILS("ewallet/api/v1/serviceProvider/serviceCategory?serviceCode=100003&serviceCategoryCode=100012&status=Y\n", languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    //JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1812943\",\"requestTime\":\"Fri Oct 22 15:28:44 IST 2021\",\"responseTime\":\"Fri Oct 22 15:28:44 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"serviceProviderList\":[{\"id\":108,\"code\":\"100107\",\"serviceCode\":\"100003\",\"serviceName\":\"Cash\",\"serviceCategoryCode\":\"100012\",\"serviceCategoryName\":\"Cash Out\",\"name\":\"Cashmoov\",\"status\":\"Active\",\"serviceProviderMasterCode\":\"100004\",\"creationDate\":\"2021-06-07T19:11:29.944+0530\"}]}");

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
                        Toast.makeText(CashOutCodeSubscriber.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(CashOutCodeSubscriber.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashOutCodeSubscriber.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    private void agent_details_api_walletownerUser() {

        String USER_CODE_FROM_TOKEN_AGENTDETAILS = MyApplication.getSaveString("userCode", CashOutCodeSubscriber.this);


        API.GET_CASHOUT_CONFCODE_DETAILS("ewallet/api/v1/walletOwnerUser/" + USER_CODE_FROM_TOKEN_AGENTDETAILS, languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1789408\",\"requestTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"responseTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwnerUser\":{\"id\":2171,\"code\":\"101917\",\"firstName\":\"TATASnegal\",\"userName\":\"TATASnegal5597\",\"mobileNumber\":\"8888888882\",\"email\":\"kundan.kumar@esteltelecom.com\",\"walletOwnerUserTypeCode\":\"100000\",\"walletOwnerUserTypeName\":\"Supervisor\",\"walletOwnerCategoryCode\":\"100000\",\"walletOwnerCategoryName\":\"Institute\",\"userCode\":\"1000002606\",\"status\":\"Active\",\"state\":\"Approved\",\"gender\":\"M\",\"idProofTypeCode\":\"100004\",\"idProofTypeName\":\"MILITARY ID CARD\",\"idProofNumber\":\"44444444444\",\"creationDate\":\"2021-10-01T09:04:07.330+0530\",\"notificationName\":\"EMAIL\",\"notificationLanguage\":\"en\",\"createdBy\":\"100308\",\"modificationDate\":\"2021-10-20T14:59:00.791+0530\",\"modifiedBy\":\"100308\",\"addressList\":[{\"id\":3569,\"walletOwnerUserCode\":\"101917\",\"addTypeCode\":\"100001\",\"addTypeName\":\"Commercial\",\"regionCode\":\"100068\",\"regionName\":\"Boke\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"city\":\"100022\",\"cityName\":\"Dubreka\",\"addressLine1\":\"delhi\",\"status\":\"Inactive\",\"creationDate\":\"2021-10-01T09:04:07.498+0530\",\"createdBy\":\"100250\",\"modificationDate\":\"2021-10-03T09:52:57.407+0530\",\"modifiedBy\":\"100308\"}],\"workingDaysList\":[{\"id\":3597,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100000\",\"weekdaysName\":\"Monday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.518+0530\"},{\"id\":3598,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100001\",\"weekdaysName\":\"Tuesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.528+0530\"},{\"id\":3599,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100002\",\"weekdaysName\":\"Wednesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.538+0530\"},{\"id\":3600,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100003\",\"weekdaysName\":\"Thursday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.547+0530\"},{\"id\":3601,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100004\",\"weekdaysName\":\"Friday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.556+0530\"},{\"id\":3602,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100005\",\"weekdaysName\":\"Saturday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.565+0530\"},{\"id\":3603,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100006\",\"weekdaysName\":\"Sunday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"2:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.573+0530\"}],\"macEnabled\":false,\"ipEnabled\":false,\"resetCredReqInit\":false,\"notificationTypeCode\":\"100000\"}}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {

                        JSONObject walletOwnerUser = jsonObject.getJSONObject("walletOwnerUser");


                        if(walletOwnerUser.has("firstName")){
                            receiver_name_str = walletOwnerUser.getString("firstName");
                        }else{
                            receiver_name_str = "";
                        }


                        subscriber_details_api_walletownerUser();


                    } else {
                        Toast.makeText(CashOutCodeSubscriber.this, resultDescription, Toast.LENGTH_LONG).show();
                      //  finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(CashOutCodeSubscriber.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashOutCodeSubscriber.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void walletOwner_all_parent() {

        API.GET_CASHOUT_CONFCODE_DETAILS("ewallet/api/v1/walletOwner/all/parent/" +walletOwnerCode_subs, languageToUse, new Api_Responce_Handler() {@Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1813316\",\"requestTime\":\"Fri Oct 22 15:45:30 IST 2021\",\"responseTime\":\"Fri Oct 22 15:45:30 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"pageable\":{\"totalRecords\":0},\"walletOwner\":{\"id\":110679,\"code\":\"1000002785\",\"walletOwnerCategoryCode\":\"100000\",\"ownerName\":\"sharique agent\",\"mobileNumber\":\"9990063618\",\"businessTypeCode\":\"100008\",\"businessTypeName\":\"Goldsmith\",\"lineOfBusiness\":\"gffg\",\"idProofNumber\":\"trt465656\",\"email\":\"sharique9718@gmail.com\",\"status\":\"Active\",\"state\":\"Approved\",\"stage\":\"Document\",\"idProofTypeCode\":\"100005\",\"idProofTypeName\":\"COMPANY REGISTRATION NUMBER\",\"idExpiryDate\":\"2021-10-22\",\"notificationLanguage\":\"en\",\"notificationTypeCode\":\"100000\",\"notificationName\":\"EMAIL\",\"issuingCountryCode\":\"100102\",\"issuingCountryName\":\"India\",\"registerCountryCode\":\"100102\",\"registerCountryName\":\"India\",\"createdBy\":\"100250\",\"creationDate\":\"2021-10-19T22:38:48.969+0530\",\"modificationDate\":\"2021-10-21T21:27:53.250+0530\",\"walletExists\":true,\"profileTypeCode\":\"100000\",\"profileTypeName\":\"tier1\",\"walletCurrencyList\":[\"100018\",\"100017\",\"100069\",\"100020\",\"100004\",\"100029\",\"100062\",\"100003\"],\"walletOwnerCatName\":\"Institute\",\"requestedSource\":\"ADMIN\",\"regesterCountryDialCode\":\"+91\",\"issuingCountryDialCode\":\"+91\",\"walletOwnerCode\":\"1000002785\"}}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {

                        JSONObject walletOwner = jsonObject.getJSONObject("walletOwner");

                        senderNameAgent = walletOwner.getString("ownerName");
                        rp_tv_senderName.setText(senderNameAgent);

                        mpin_final_api();


                    } else {
                        Toast.makeText(CashOutCodeSubscriber.this, resultDescription, Toast.LENGTH_LONG).show();
                      //  finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(CashOutCodeSubscriber.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashOutCodeSubscriber.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void api_confirmaCode() {

        API.GET_CASHOUT_CONFCODE_DETAILS("ewallet/api/v1/holdingAccount/confirmationCode/" + confirmationCodeStr, languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                  //  JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1813407\",\"requestTime\":\"Fri Oct 22 15:57:35 IST 2021\",\"responseTime\":\"Fri Oct 22 15:57:35 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"accountHolding\":{\"id\":1937,\"code\":\"1000002014\",\"walletOwnerCode\":\"1000002794\",\"transactionId\":116607,\"openingBalance\":47593,\"closingBalance\":45124,\"sendingAmount\":1000,\"beneficiaryAmount\":1000,\"fee\":1300,\"exchangeRateValue\":0,\"tax\":169,\"fromCurrencyCode\":\"100062\",\"toCurrencyCode\":\"100062\",\"confirmationCode\":\"MM*********\",\"dueDate\":\"2021-10-29T00:00:00.000+0530\",\"status\":\"In Transit\",\"creationDate\":\"2021-10-22 15:08:56\",\"createdBy\":\"102073\",\"sendingWalletOwnerCode\":\"1000002794\",\"beneficiaryWalletOwnerCode\":\"1000002785\",\"holdingAccountTypeCode\":\"100000\",\"holdingAccountTypeName\":\"R2R\",\"taxConfigurationList\":[{\"taxTypeCode\":\"100133\",\"taxTypeName\":\"Financial Tax\",\"value\":169,\"taxAvailBy\":\"Fee Amount\"}],\"srcProviderWalletCode\":\"1000015411\",\"sendCountryCode\":\"100092\",\"receiveCountryCode\":\"100092\",\"walletToCash\":true}}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {


                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("accountHolding"))   ;

                        String sendingAmount_fromApi = jsonObject1.getString("sendingAmount");
                        edittext_amount.setText(sendingAmount_fromApi);
                       // tvAmtCurr.setText("");

                        edittext_amount.setEnabled(true);



                    } else {
                        Toast.makeText(CashOutCodeSubscriber.this, resultDescription, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    Toast.makeText(CashOutCodeSubscriber.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashOutCodeSubscriber.this, aFalse, Toast.LENGTH_SHORT).show();
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

//
//            {
//                    "walletOwnerCode": "1000002785",
//                    "transactionType": "RECEIVEREMITTANCE",
//                    "confirmationCode": "MM686087063",
//                    "firstName": "Sharique subs",
//                    "lastName": "sharique anwar",
//                    "phoneNumber": "9015931368",
//
//                    "toCurrencyCode": "100062",
//                    "amount": 1000,
//                    "pin": "6bdbfd10be9cf67a0d6b0de45db0ce0a",
//                    "channelTypeCode": "100000",
//                    "serviceCode": "100002",
//                    "serviceCategoryCode": "100018",
//                    "serviceProviderCode": "100105"
//            }

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("walletOwnerCode", walletOwnerCode_mssis_agent);
            jsonObject.put("transactionType", "CASHOUTSUBSCRIBER"); // Hard Code according  to Deepak
            jsonObject.put("confirmationCode", confirmationCodeStr);
            jsonObject.put("firstName",senderName_susbcriber);
            jsonObject.put("lastName", senderName_str);
            jsonObject.put("phoneNumber", mobileNoStr);
            jsonObject.put("toCurrencyCode", currencyCode_agent);         // source  srcWalletOwnerCode
            jsonObject.put("amount", amountstr);
            String encryptionDatanew = AESEncryption.getAESEncryption(mpinStr);
            jsonObject.put("pin", encryptionDatanew);
            jsonObject.put("channelTypeCode", "2");           // Hard Code according  to Deepak
            jsonObject.put("serviceCode", serviceCode_from_serviceCategory);
            jsonObject.put("serviceCategoryCode", serviceCategoryCode_from_serviceCategory);  // Hard Code according  to Deepak
            jsonObject.put("serviceProviderCode", serviceProviderCode_from_serviceCategory);  // Hard Code according  to Deepak

            String requestNo=AESEncryption.getAESEncryption(jsonObject.toString());
            JSONObject jsonObjectA=null;
            try{
                jsonObjectA=new JSONObject();
                jsonObjectA.put("request",requestNo);
            }catch (Exception e){

            }

            API.POST_CASHOUT_CONFCODE_MPIN("ewallet/api/v1/remittance/receive", jsonObjectA, languageToUse, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {

                       // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"116595\",\"requestTime\":\"Fri Oct 22 14:40:15 IST 2021\",
                        // \"responseTime\":\"Fri Oct 22 14:40:15 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",
                        // \"remittance\":{\"id\":1936,\"code\":\"1000002013\",\"walletOwnerCode\":\"1000002794\",\"transactionType\":\"RECEIVE REMITTANCE\",\"fromCurrencyCode\":\"100062\",\"fromCurrencyName\":\"GNF\",\"fromCurrencySymbol\":\"Fr\",\"toCurrencyCode\":\"100062\",\"toCurrencyName\":\"GNF\",\"toCurrencySymbol\":\"Fr\",\"amount\":1169,\"amountToPaid\":1000,\"fee\":0,\"tax\":\"0.0\",\"conversionRate\":0,\"confirmationCode\":\"MM*********\",\"transactionReferenceNo\":\"116595\",\"transactionDateTime\":\"2021-10-22 14:40:15\",\"sender\":{\"id\":110688,\"code\":\"1000002794\",\"firstName\":\"Sharique subs\",\"lastName\":\"sharique anwar\",\"mobileNumber\":\"9015931368\",\"gender\":\"M\",\"idProofTypeCode\":\"100000\",\"idProofTypeName\":\"PASSPORT\",\"idProofNumber\":\"passportno123\",\"idExpiryDate\":\"2021-10-28\",\"dateOfBirth\":\"1959-09-09\",\"email\":\"sharique9718@gmail.com\",\"issuingCountryCode\":\"100102\",\"issuingCountryName\":\"India\",\"status\":\"Active\",\"creationDate\":\"2021-10-22 12:05:58\",\"createdBy\":\"100250\",\"modificationDate\":\"2021-10-22 12:16:13\",\"modifiedBy\":\"100308\",\"registerCountryCode\":\"100092\",\"registerCountryName\":\"Guinea\",\"ownerName\":\"Sharique subs\"},\"receiver\":{\"id\":110679,\"code\":\"1000002785\",\"firstName\":\"sharique agent\",\"mobileNumber\":\"9990063618\",\"idProofTypeCode\":\"100005\",\"idProofTypeName\":\"COMPANY REGISTRATION NUMBER\",\"idProofNumber\":\"trt465656\",\"idExpiryDate\":\"2021-10-22\",\"email\":\"sharique9718@gmail.com\",\"issuingCountryCode\":\"100102\",\"issuingCountryName\":\"India\",\"status\":\"Active\",\"creationDate\":\"2021-10-19 22:38:48\",\"createdBy\":\"100250\",\"modificationDate\":\"2021-10-21 21:27:53\",\"registerCountryCode\":\"100102\",\"registerCountryName\":\"India\",\"ownerName\":\"sharique agent\"},\"taxConfigurationList\":[{\"taxTypeCode\":\"100133\",\"taxTypeName\":\"Financial Tax\",\"value\":169,\"taxAvailBy\":\"Fee Amount\"}],\"sendCountryCode\":\"100092\",\"receiveCountryCode\":\"100092\",\"sendCountryName\":\"Guinea\",\"receiveCountryName\":\"Guinea\",\"walletToCash\":true}}");

                        String resultCode = jsonObject.getString("resultCode");
                        String resultDescription = jsonObject.getString("resultDescription");

                        if (resultCode.equalsIgnoreCase("0")) {




                            ll_page_1.setVisibility(View.GONE);
                            ll_reviewPage.setVisibility(View.GONE);
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

                            receiptPage_tv_stransactionType.setText(getString(R.string.cash_Out));

                            receiptPage_sender_mssidn.setText(MyApplication.getSaveString("USERNAME", CashOutCodeSubscriber.this));



                            JSONObject remittance_object = jsonObject.getJSONObject("remittance");
                            receiptPage_confirmationCode.setText(remittance_object.getString("confirmationCode"));




                            if(remittance_object.has("fromCurrencySymbol"))
                            {
                                currencySymbol_sender = remittance_object.getString("fromCurrencySymbol");
                            }

                            if(remittance_object.has("toCurrencySymbol"))
                            {
                                currencySymbol_receiver = remittance_object.getString("toCurrencySymbol");

                            }




                            receiptPage_sbenificairay_mssidn.setText(mobileNoStr);


                            receiptPage_tv_transactionAmount.setText(currencySymbol_sender+ " " +amountstr);

                            receiptPage_tv_amount_to_be_charged.setText(currencySymbol_sender+ " " +MyApplication.addDecimal(remittance_object.getInt("amount")+""));
                            receiptPage_amount_to_paid_receiptpage.setText(currencySymbol_receiver+ " " +MyApplication.addDecimal(remittance_object.getInt("amountToPaid")+""));



                            receiptPage_tv_fee.setText(currencySymbol_sender+ " " +MyApplication.addDecimal(remittance_object.getInt("fee")+""));
                            receiptPage_conversion_rate.setText(currencySymbol_sender+ " " +remittance_object.getString("conversionRate"));
                            receiptPage_tv_financialtax.setText(currencySymbol_sender+ " " +remittance_object.getInt("amount"));


                            receiptPage_tv_transaction_receiptNo.setText(jsonObject.getString("transactionId"));
                            receiptPage_tv_dateOfTransaction.setText(remittance_object.getString("creationDate"));


                            receiptPage_tv_sender_name.setText(senderNameAgent);
                            receiptPage_tv_sender_phoneNo.setText(mobileNoStr);

                            receiptPage_tv_receiver_name.setText(receiver_name_str);
                            receiptPage_tv_receiver_phoneNo.setText(MyApplication.getSaveString("USERNAME", CashOutCodeSubscriber.this));


                        } else {
                            Toast.makeText(CashOutCodeSubscriber.this, resultDescription, Toast.LENGTH_LONG).show();
                            //  finish();
                        }


                    } catch (Exception e) {
                        Toast.makeText(CashOutCodeSubscriber.this, e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }


                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(CashOutCodeSubscriber.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    } else {

                        Toast.makeText(CashOutCodeSubscriber.this, aFalse, Toast.LENGTH_SHORT).show();
                    }
                    }
            });

        } catch (Exception e) {
            Toast.makeText(CashOutCodeSubscriber.this, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }





    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_nextClick: {


                if (validation_mobile_Details()) {

                    if (new InternetCheck().isConnected(CashOutCodeSubscriber.this)) {

                        MyApplication.showloader(CashOutCodeSubscriber.this, getString(R.string.please_wait));


                        service_Provider_api();

                    } else {
                        Toast.makeText(CashOutCodeSubscriber.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                    }
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


            case R.id.confirm_reviewClick_textview: {


                if (selectClickType.equalsIgnoreCase("select_otp")) {

                    if (validation_otp_detail()) {
                        if (new InternetCheck().isConnected(CashOutCodeSubscriber.this)) {
                            MyApplication.showloader(CashOutCodeSubscriber.this, getString(R.string.please_wait));

                           otp_verify_api();

                        } else {
                            Toast.makeText(CashOutCodeSubscriber.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                else if (selectClickType.equalsIgnoreCase("select_mpin")) {
                    if (validation_mpin_detail()) {
                        if (new InternetCheck().isConnected(CashOutCodeSubscriber.this)) {
                            MyApplication.showloader(CashOutCodeSubscriber.this, getString(R.string.please_wait));


                            mpin_final_api();

                        } else {
                            Toast.makeText(CashOutCodeSubscriber.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
            break;

            case R.id.previous_reviewClick_textview: {

                ll_page_1.setVisibility(View.VISIBLE);
                ll_reviewPage.setVisibility(View.GONE);
                ll_receiptPage.setVisibility(View.GONE);
                ll_successPage.setVisibility(View.GONE);
            }
            break;

            case R.id.tvContinue: {

                ll_page_1.setVisibility(View.GONE);
                ll_reviewPage.setVisibility(View.GONE);
                ll_successPage.setVisibility(View.GONE);
                ll_receiptPage.setVisibility(View.VISIBLE);

            }
            break;

            case R.id.qrCode_imageButton: {

                if (checkPermission()) {


                    IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                    intentIntegrator.setPrompt("Scan a barcode or QR Code");
                    intentIntegrator.setOrientationLocked(true);
                    intentIntegrator.initiateScan();


                } else {
                    requestPermission();
                }


            }

            break;

            case R.id.close_receiptPage_textview: {


//                ll_page_1.setVisibility(View.VISIBLE);
//                ll_reviewPage.setVisibility(View.GONE);
//                ll_receiptPage.setVisibility(View.GONE);

                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();


            }

            break;


            case R.id.ll_resendOtp: {


                if (new InternetCheck().isConnected(CashOutCodeSubscriber.this)) {

                    MyApplication.showloader(CashOutCodeSubscriber.this, getString(R.string.please_wait));

                    otp_generate_api();

                } else {
                    Toast.makeText(CashOutCodeSubscriber.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                }
            }
            break;


        }
    }

//    private void exchange_rate_api() {
//
//        // API.GET_CASHOUT_CONFCODE_DETAILS("ewallet/api/v1/exchangeRate/getAmountDetails?sendCurrencyCode=100062&receiveCurrencyCode=100062&sendCountryCode=100092&receiveCountryCode=&currencyValue=1000&channelTypeCode=100000&serviceCode=100003&serviceCategoryCode=100011&serviceProviderCode=100106&walletOwnerCode=1000002606&remitAgentCode=1000002606&payAgentCode=1000002488",languageToUse,new Api_Responce_Handler() {
//
//
//        API.GET_CASHOUT_CONFCODE_DETAILS("ewallet/api/v1/exchangeRate/getAmountDetails?sendCurrencyCode=" + currencyCode_agent + "&receiveCurrencyCode=100062&sendCountryCode=" + countryCode_agent + "&receiveCountryCode=" +
//                "&currencyValue=" + amountstr + "&channelTypeCode=2&serviceCode=" + serviceCode_from_serviceCategory + "&serviceCategoryCode=" + serviceCategoryCode_from_serviceCategory + "&serviceProviderCode=" + serviceProviderCode_from_serviceCategory + "&walletOwnerCode=" + walletOwnerCode_mssis_agent + "&remitAgentCode=" + walletOwnerCode_mssis_agent + "&payAgentCode=1000002488", languageToUse, new Api_Responce_Handler() {
//            @Override
//            public void success(JSONObject jsonObject) {
//
//                MyApplication.hideLoader();
//
//                try {
//
//                    //JSONObject jsonObject = new JSONObject("{"transactionId":"1789322","requestTime":"Wed Oct 20 15:53:33 IST 2021","responseTime":"Wed Oct 20 15:53:33 IST 2021","resultCode":"0","resultDescription":"Transaction Successful","walletOwnerCountryCurrencyList":[{"id":7452,"code":"107451","walletOwnerCode":"1000002488","currencyCode":"100062","currencyName":"GNF","currencySymbol":"Fr","countryCurrencyCode":"100076","inBound":true,"outBound":true,"status":"Active"}]}");
//
//                    String resultCode = jsonObject.getString("resultCode");
//                    String resultDescription = jsonObject.getString("resultDescription");
//
//                    if (resultCode.equalsIgnoreCase("0")) {
//
//                        //Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();
//
//                        JSONObject exchangeRate = jsonObject.getJSONObject("exchangeRate");
//
//                        fees_amount = exchangeRate.getString("fee");
//                        rp_tv_fees_reveiewPage.setText("Fr " + fees_amount);
//
//                        //  credit_amount=exchangeRate.getString("currencyValue");
//
//                        JSONArray jsonArray = exchangeRate.getJSONArray("taxConfigurationList");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//
//                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
//                            tax_financial = jsonObject2.getString("value");
//                        }
//
//                        rp_tv_financialTax.setText("Fr " + tax_financial);
//
//                        tax_financial_double = Double.parseDouble(tax_financial);
//                        fees_amount_double = Double.parseDouble(fees_amount);
//                        amountstr_double = Double.parseDouble(amountstr);
//
//                        totalAmount_double = tax_financial_double + amountstr_double + fees_amount_double;
//                        totalAmount_str = String.valueOf(totalAmount_double);
//                        rp_tv_amount_to_be_charge.setText("Fr " + totalAmount_str);
//
//                        amountstr = String.valueOf(amountstr_double);
//                        rp_tv_transactionAmount.setText("Fr " + amountstr);
//                        rp_tv_amount_to_be_credit.setText("Fr " + amountstr);
//
//                        allByCriteria_walletOwnerCode_api();
//
//                    } else {
//                        Toast.makeText(CashOutCodeSubscriber.this, resultDescription, Toast.LENGTH_LONG).show();
//                        finish();
//                    }
//
//
//                } catch (Exception e) {
//                    Toast.makeText(CashOutCodeSubscriber.this, e.toString(), Toast.LENGTH_LONG).show();
//                    e.printStackTrace();
//                }
//
//            }
//
//
//            @Override
//            public void failure(String aFalse) {
//
//                MyApplication.hideLoader();
//                Toast.makeText(CashOutCodeSubscriber.this, aFalse, Toast.LENGTH_SHORT).show();
//                finish();
//
//            }
//        });
//
//
//    }


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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {

            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if (result != null) {

                System.out.println(resultCode);

                if (result.getContents() == null) {

                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {


                    String str = result.getContents();

                    if (str.equalsIgnoreCase("")) {
                        // 1000002786:TarunMwTest

                        Toast.makeText(this, "QR Code Not Valid", Toast.LENGTH_LONG).show();
                        edittext_mobileNuber.setEnabled(true);

                    } else {


                        String[] qrData = str.split("\\:");
                        mobileNoStr = qrData[0];
                        edittext_mobileNuber.setText(mobileNoStr);
                        edittext_mobileNuber.setEnabled(false);


                        // Toast.makeText(this, "QR Code  Valid", Toast.LENGTH_LONG).show();
                    }

                }

            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }


        } catch (Exception e) {

            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }

    }

    private void otp_generate_api() {
        try{

            JSONObject jsonObject=new JSONObject();

            String USERNAME =  MyApplication.getSaveString("USERNAME", CashOutCodeSubscriber.this);
            String PASSWORD = MyApplication.getSaveString("PASSWORD", CashOutCodeSubscriber.this);
            String EMAIL = MyApplication.getSaveString("EMAIL", CashOutCodeSubscriber.this);
            String NTTYPECODE = MyApplication.getSaveString("NTTYPECODE", CashOutCodeSubscriber.this);
            String USERCODE = MyApplication.getSaveString("USERCODE", CashOutCodeSubscriber.this);

            jsonObject.put("email",EMAIL);
            jsonObject.put("notificationTypeCode",NTTYPECODE);
            jsonObject.put("transTypeCode","101813");      // Temporary Hard Code acording to Praveen
            jsonObject.put("status","Active");

            String USER_CODE_FROM_TOKEN_AGENTDETAILS =  MyApplication.getSaveString("userCode", CashOutCodeSubscriber.this);

           // jsonObject.put("walletOwnerUserCode","101961"); // Temporary Hard Code acording to Praveen
            jsonObject.put("walletOwnerUserCode",USER_CODE_FROM_TOKEN_AGENTDETAILS);


            API.POST_GET_OTP("ewallet/api/v1/otp",jsonObject,new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {

                        //  JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1771883\",\"requestTime\":\"Mon Oct 18 18:05:40 IST 2021\",\"responseTime\":\"Mon Oct 18 18:05:40 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\"}");

                        if (jsonObject.has("error")) {

                            String error = jsonObject.getString("error");
                            String error_message = jsonObject.getString("error_message");

                            Toast.makeText(CashOutCodeSubscriber.this, error_message, Toast.LENGTH_LONG).show();

                            if(error.equalsIgnoreCase("1251"))
                            {

                                Intent i = new Intent(CashOutCodeSubscriber.this, VerifyLoginAccountScreen.class);
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


                                Toast.makeText(CashOutCodeSubscriber.this, getString(R.string.otp_has_send_sucessfully_subscriber_register), Toast.LENGTH_LONG).show();

                                confirm_reviewClick_textview.setText(getString(R.string.otp_verify));
                                selectClickType="select_otp";

                                ll_page_1.setVisibility(View.GONE);
                                ll_receiptPage.setVisibility(View.GONE);
                                ll_pin.setVisibility(View.GONE);
                                ll_successPage.setVisibility(View.GONE);

                                et_otp.setText("");
                                et_mpin.setText("");

                                ll_reviewPage.setVisibility(View.VISIBLE);
                                ll_otp.setVisibility(View.VISIBLE);
                                ll_resendOtp.setVisibility(View.VISIBLE);


                            }

                            else {
                                Toast.makeText(CashOutCodeSubscriber.this, resultDescription, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    catch (Exception e)
                    {
                        Toast.makeText(CashOutCodeSubscriber.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }


                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(CashOutCodeSubscriber.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    }

                    else {

                        Toast.makeText(CashOutCodeSubscriber.this, aFalse, Toast.LENGTH_SHORT).show();
                    }

                    //  MyApplication.showToast(LoginPin.this,aFalse);

                }
            });

        }catch (Exception e){

            MyApplication.hideLoader();

            MyApplication.showToast(CashOutCodeSubscriber.this,e.toString());

        }

    }


    private void otp_verify_api() {
        try{

            JSONObject jsonObject=new JSONObject();

            jsonObject.put("transTypeCode","101813");      // Temporary Hard Code acording to Praveen
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

                            Toast.makeText(CashOutCodeSubscriber.this, error_message, Toast.LENGTH_LONG).show();

                            if(error.equalsIgnoreCase("1251")) {

                                Intent i = new Intent(CashOutCodeSubscriber.this, VerifyLoginAccountScreen.class);
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
                                selectClickType="select_mpin";
                                confirm_reviewClick_textview.setText(getString(R.string.verify_subscriber_pin));
                                ll_otp.setVisibility(View.GONE);
                                ll_otp.setVisibility(View.GONE);
                                ll_resendOtp.setVisibility(View.GONE);

                                ll_pin.setVisibility(View.VISIBLE);



                            }

                            else {
                                Toast.makeText(CashOutCodeSubscriber.this, resultDescription, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    catch (Exception e)
                    {
                        Toast.makeText(CashOutCodeSubscriber.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(CashOutCodeSubscriber.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(CashOutCodeSubscriber.this, aFalse, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (Exception e){

            MyApplication.hideLoader();
            MyApplication.showToast(CashOutCodeSubscriber.this,e.toString());
        }

    }


//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
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

    private boolean isFormatting;
    private int prevCommaAmount;
    private void formatInput(EditText editText,CharSequence s, int start, int count) {
        isFormatting = true;

        if( MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")){
            return;
        }
        StringBuilder sbResult = new StringBuilder();
        String result;
        int newStart = start;

        try {
            // Extract value without its comma
            String digitAndDotText = s.toString().replace(",", "");
            int commaAmount = 0;

            // if user press . turn it into 0.
            if (s.toString().startsWith(".") && s.length() == 1) {
                editText.setText("0.");
                editText.setSelection(editText.getText().toString().length());
                return;
            }

            // if user press . when number already exist turns it into comma
            if (s.toString().startsWith(".") && s.length() > 1) {
                StringTokenizer st = new StringTokenizer(s.toString());
                String afterDot = st.nextToken(".");
                editText.setText("0." + AutoFormatUtil.extractDigits(afterDot));
                editText.setSelection(2);
                return;
            }

            if (digitAndDotText.contains(".")) {
                // escape sequence for .
                String[] wholeText = digitAndDotText.split("\\.");

                if (wholeText.length == 0) {
                    return;
                }

                // in 150,000.45 non decimal is 150,000 and decimal is 45
                String nonDecimal = wholeText[0];
                if (nonDecimal.length() == 0) {
                    return;
                }

                // only format the non-decimal value
                result = AutoFormatUtil.formatToStringWithoutDecimal(nonDecimal);

                sbResult
                        .append(result)
                        .append(".");

                if (wholeText.length > 1) {
                    sbResult.append(wholeText[1]);
                }

            } else {
                result = AutoFormatUtil.formatWithDecimal(digitAndDotText);
                sbResult.append(result);
            }

            // count == 0 indicates users is deleting a text
            // count == 1 indicates users is entering a text
            newStart += ((count == 0) ? 0 : 1);

            // calculate comma amount in edit text
            commaAmount += AutoFormatUtil.getCharOccurance(result, ',');

            // flag to mark whether new comma is added / removed
            if (commaAmount >= 1 && prevCommaAmount != commaAmount) {
                newStart += ((count == 0) ? -1 : 1);
                prevCommaAmount = commaAmount;
            }

            // case when deleting without comma
            if (commaAmount == 0 && count == 0 && prevCommaAmount != commaAmount) {
                newStart -= 1;
                prevCommaAmount = commaAmount;
            }

            // case when deleting without dots
            if (count == 0 && !sbResult.toString()
                    .contains(".") && prevCommaAmount != commaAmount) {
                newStart = start;
                prevCommaAmount = commaAmount;
            }

            editText.setText(sbResult.toString());

            // ensure newStart is within result length
            if (newStart > sbResult.toString().length()) {
                newStart = sbResult.toString().length();
            } else if (newStart < 0) {
                newStart = 0;
            }

            editText.setSelection(newStart);

        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }



}