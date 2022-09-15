package com.agent.cashmoovui.cash_in;

import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.util.TypedValue;
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
import androidx.biometric.BiometricManager;
import androidx.core.app.ActivityCompat;

import com.agent.cashmoovui.AddContact;
import com.agent.cashmoovui.HiddenPassTransformationMethod;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.activity.OtherOption;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.apiCalls.BioMetric_Responce_Handler;
import com.agent.cashmoovui.cashout.CashOutAgent;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.login.LoginMsis;
import com.agent.cashmoovui.login.LoginPin;
import com.agent.cashmoovui.otp.VerifyLoginAccountScreen;
import com.agent.cashmoovui.set_pin.AESEncryption;
import com.aldoapps.autoformatedittext.AutoFormatUtil;
import com.blikoon.qrcodescanner.QrCodeActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

public class CashIn  extends AppCompatActivity implements View.OnClickListener {
    ImageButton qrCode_imageButton;
    ImageView imgBack,imgHome;
    View rootView;
    boolean  isPasswordVisible;

    String agentCode_subscriber="";
    private static final int REQUEST_CODE_QR_SCAN = 101;
    String currencySymbol_sender="";
    String currencySymbol_receiver="";

    public static EditText etName;

    String walletOwnerCode="";

    EditText etPin;
    LinearLayout financialTax_receiptPageLinear,linear_layout_businessType,taxcashinLinear,receipt_Linear,pinLinear;
    TextView financialTax_receiptPage,receiptPage_tv_financialtaxvaluecashin,taxvalueText,tvAmtCurr,tvContinue,exportReceipt_textview,tv_nextClick,rp_tv_senderName,rp_tv_mobileNumber,rp_tv_businessType,rp_tv_email,rp_tv_country,rp_tv_receiverName,rp_tv_transactionAmount
            ,rp_tv_fees_reveiewPage,receiptPage_tv_stransactionType, receiptPage_tv_dateOfTransaction, receiptPage_tv_transactionAmount,
            receiptPage_tv_amount_to_be_credit, receiptPage_tv_fee, receiptPage_tv_financialtax, receiptPage_tv_transaction_receiptNo,receiptPage_tv_sender_name,
            receiptPage_tv_sender_phoneNo,
            receiptPage_tv_receiver_name, receiptPage_tv_receiver_phoneNo, close_receiptPage_textview, receipt_tv_amount_to_be_charge,rp_tv_financialTax,rp_tv_amount_to_be_charge,rp_tv_amount_to_be_credit,previous_reviewClick_textview,confirm_reviewClick_textview;
    LinearLayout ll_page_1,ll_reviewPage,ll_receiptPage,main_layout,ll_successPage;

    MyApplication applicationComponentClass;
    String languageToUse = "";

    EditText edittext_mobileNuber,edittext_amount,et_mpin;

    String mobileNoStr="",amountstr="";

    String walletOwnerCode_mssis_agent="",walletOwnerCode_subs, senderlastNameAgent,senderNameAgent="";

    String  currencyCode_agent="",countryCode_agent="",currencyName_agent="",countryName_agent;
    String  currencyCode_subscriber="",countryCode_subscriber="",currencyName_subscriber="",countryName_subscriber;

    String tax_financialnew,tax_financialtypename,tax_financial="",fees_amount,receivermobileNumberStr,totalAmount_str,receivernameStr="",receiverlastnameStr="";
    Double tax_financial_double=0.0,amountstr_double=0.0,tax_financialnewDouble=0.0,fees_amount_double=0.0,totalAmount_double=0.0;

    String mpinStr="";
    public Double feeDouble;


    String  serviceCode_from_serviceCategory="",serviceCategoryCode_from_serviceCategory="",serviceProviderCode_from_serviceCategory;




    public static final int REQUEST_CODE = 1;


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


            setContentView(R.layout.activity_cashin);
            setBackMenu();

            rootView = getWindow().getDecorView().findViewById(R.id.main_layout);
            //     First page

            ll_page_1 = (LinearLayout) findViewById(R.id.ll_page_1);
            linear_layout_businessType = (LinearLayout) findViewById(R.id.linear_layout_businessType);
            ll_successPage = (LinearLayout) findViewById(R.id.ll_successPage);
            tvContinue = (TextView) findViewById(R.id.tvContinue);
            tvContinue.setOnClickListener(this);
            financialTax_receiptPageLinear=findViewById(R.id.financialTax_receiptPageLinear);
            financialTax_receiptPage=findViewById(R.id.financialTax_receiptPage);

            etName = findViewById(R.id.etName);

            etName.setEnabled(false);

            taxcashinLinear=findViewById(R.id.taxcashinLinear);
            tv_nextClick = (TextView) findViewById(R.id.tv_nextClick);
            edittext_mobileNuber = (EditText) findViewById(R.id.edittext_mobileNuber);
            tvAmtCurr = findViewById(R.id.tvAmtCurr);
            edittext_amount = (EditText) findViewById(R.id.edittext_amount);
            pinLinear=findViewById(R.id.pinLinear);
            et_mpin = (EditText)findViewById(R.id.et_mpin);

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
                        MyApplication.hideKeyboard(CashIn.this);            }
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


                            Intent intent = new Intent(CashIn.this,
                                    AddContact.class);
                            startActivityForResult(intent , REQUEST_CODE);

                            return true;
                        }
                    }
                    return false;
                }
            });

            edittext_mobileNuber.addTextChangedListener(new TextWatcher() {
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

                    if (s.length() >=9) {
                        subscriber_details_api_walletownerUserNew();


                    }
                    if(s.length()<=9){
                        etName.setText("");



                    }

                    isFormatting = false;



                }
            });

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
            receipt_tv_amount_to_be_charge = findViewById(R.id.receipt_tv_amount_to_be_charge);





            previous_reviewClick_textview = (TextView) findViewById(R.id.previous_reviewClick_textview);
            confirm_reviewClick_textview = (TextView) findViewById(R.id.confirm_reviewClick_textview);

            TextView tvFinger =findViewById(R.id.tvFinger);
            if(MyApplication.setProtection!=null && !MyApplication.setProtection.isEmpty()) {
                if (MyApplication.setProtection.equalsIgnoreCase("Activate")) {
                   // tvFinger.setVisibility(View.VISIBLE);
                } else {
                  //  tvFinger.setVisibility(View.GONE);
                }
            }else{
               // tvFinger.setVisibility(View.VISIBLE);
            }
            tvFinger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.biometricAuth(CashIn.this, new BioMetric_Responce_Handler() {
                        @Override
                        public void success(String success) {
                            try {

                                //  String encryptionDatanew = AESEncryption.getAESEncryption(MyApplication.getSaveString("pin",MyApplication.appInstance).toString().trim());
                                mpinStr=MyApplication.getSaveString("pin",MyApplication.appInstance);

                                if (new InternetCheck().isConnected(CashIn.this)) {

                                    MyApplication.showloader(CashIn.this, getString(R.string.please_wait));

                                    mpin_final_api();

                                } else {
                                    Toast.makeText(CashIn.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failure(String failure) {
                            MyApplication.showToast(CashIn.this,failure);
                        }
                    });
                }
            });



            //    Receipt page

            ll_receiptPage = (LinearLayout) findViewById(R.id.ll_receiptPage);
            main_layout = (LinearLayout) findViewById(R.id.main_layout);
            exportReceipt_textview = (TextView) findViewById(R.id.exportReceipt_textview);
            exportReceipt_textview.setOnClickListener(this);

            receiptPage_tv_transaction_receiptNo = (TextView) findViewById(R.id.receiptPage_tv_transaction_receiptNo);
            receiptPage_tv_stransactionType = (TextView) findViewById(R.id.receiptPage_tv_stransactionType);
            receiptPage_tv_dateOfTransaction = (TextView) findViewById(R.id.receiptPage_tv_dateOfTransaction);
            receiptPage_tv_transactionAmount = (TextView) findViewById(R.id.receiptPage_tv_transactionAmount);
            receiptPage_tv_amount_to_be_credit = (TextView) findViewById(R.id.receiptPage_tv_amount_to_be_credit);
            receiptPage_tv_fee = (TextView) findViewById(R.id.receiptPage_tv_fee);
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


            walletOwnerCode_mssis_agent = MyApplication.getSaveString("walletOwnerCode", CashIn.this);


            if (new InternetCheck().isConnected(CashIn.this)) {

                MyApplication.showloader(CashIn.this, getString(R.string.please_wait));

                api_walletOwner_agent();

            } else {
                Toast.makeText(CashIn.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
            }

        }
        catch (Exception e)
        {
            Toast.makeText(CashIn.this, e.toString(), Toast.LENGTH_LONG).show();

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
                MyApplication.hideKeyboard(CashIn.this);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(CashIn.this);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    boolean validation_mobile_Details()
    {

        mobileNoStr = edittext_mobileNuber.getText().toString().trim();
        amountstr = edittext_amount.getText().toString().trim().replace(",","");


        if(mobileNoStr.isEmpty()) {

            MyApplication.showErrorToast(this,getString(R.string.val_phone));

            return false;
        }

        else if(mobileNoStr.length() < 9) {

            MyApplication.showErrorToast(this,getString(R.string.enter_phone_no_val));

            return false;
        }

        else if(amountstr.isEmpty()) {

            MyApplication.showErrorToast(this,getString(R.string.plz_enter_amount));

            return false;
        }


        else  if(Double.parseDouble(edittext_amount.getText().toString().trim().replace(",",""))<MyApplication.ToCashInMinAmount) {
            MyApplication.showErrorToast(CashIn.this,getString(R.string.val_amount_min)+" "+MyApplication.ToCashInMinAmount);
            return false;
        }

        else   if(Double.parseDouble(edittext_amount.getText().toString().trim().replace(",",""))>MyApplication.ToCashInMaxAmount) {
            MyApplication.showErrorToast(CashIn.this,getString(R.string.val_amount_max)+" "+MyApplication.ToCashInMaxAmount);
            return false;


    }


        return true;
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

                        if(jsonObject_walletOwner.has("businessTypeName"))
                        {
                            if(jsonObject_walletOwner.getString("businessTypeName").equalsIgnoreCase(""))
                            {
                                rp_tv_businessType.setText(jsonObject_walletOwner.getString("businessTypeName"));
                                linear_layout_businessType.setVisibility(View.VISIBLE);
                            }
                            else {
                                linear_layout_businessType.setVisibility(View.GONE);
                            }
                        }

                    } else {
                        Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    Toast.makeText(CashIn.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    finish();

                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashIn.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void subscriber_details_api_walletownerUserNew() {

        String walletOwnerCategoryCode =  MyApplication.getSaveString("walletOwnerCategoryCode",CashIn.this);

        walletOwnerCategoryCode ="100010"; // HARD CODE FINAL ACORDING TO PARVEEN


        API.GET_CASHIN_DETAILS("ewallet/api/v1/walletOwner/all?walletOwnerCategoryCode="+walletOwnerCategoryCode+"&mobileNumber="+(edittext_mobileNuber.getText().toString()) + "&offset=0&limit=500",languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    //JSONObject jsonObject = new JSONObject("{"transactionId":"1789327","requestTime":"Wed Oct 20 15:55:16 IST 2021","responseTime":"Wed Oct 20 15:55:16 IST 2021","resultCode":"0","resultDescription":"Transaction Successful","pageable":{"limit":500,"offset":0,"totalRecords":1},"walletOwnerList":[{"id":110382,"code":"1000002488","walletOwnerCategoryCode":"100010","ownerName":"Kundan","mobileNumber":"118110111","idProofNumber":"vc12345","email":"kundan.kumar@esteltelecom.com","status":"Active","state":"Approved","stage":"Document","idProofTypeCode":"100006","idProofTypeName":"OTHER","idExpiryDate":"2021-09-29","notificationLanguage":"en","notificationTypeCode":"100000","notificationName":"EMAIL","gender":"M","dateOfBirth":"1960-01-26","lastName":"New","issuingCountryCode":"100092","issuingCountryName":"Guinea","registerCountryCode":"100092","registerCountryName":"Guinea","createdBy":"100375","modifiedBy":"100322","creationDate":"2021-09-16T17:08:49.796+0530","modificationDate":"2021-09-16T17:10:17.009+0530","walletExists":true,"profileTypeCode":"100001","profileTypeName":"tier2","walletOwnerCatName":"Subscriber","occupationTypeCode":"100002","occupationTypeName":"Others","requestedSource":"ADMIN","regesterCountryDialCode":"+224","issuingCountryDialCode":"+224","walletOwnerCode":"1000002488"}]}");

                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");

                    if(resultCode.equalsIgnoreCase("0")) {

                        //  Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();

                        JSONArray jsonArray = jsonObject.getJSONArray("walletOwnerList");
                        for(int i=0;i<jsonArray.length();i++)
                        {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            receivernameStr=jsonObject2.getString("ownerName");
                            etName.setText(receivernameStr);

                        }

                       // api_currency_sender();

                    }

                    else {
                        etName.setText("");
                        Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();
                        //  finish();
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(CashIn.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashIn.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    private void subscriber_details_api_walletownerUser() {

        String walletOwnerCategoryCode =  MyApplication.getSaveString("walletOwnerCategoryCode",CashIn.this);

        walletOwnerCategoryCode ="100010"; // HARD CODE FINAL ACORDING TO PARVEEN


        API.GET_CASHIN_DETAILS("ewallet/api/v1/walletOwner/all?walletOwnerCategoryCode="+walletOwnerCategoryCode+"&mobileNumber="+(edittext_mobileNuber.getText().toString()) + "&offset=0&limit=500",languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {


                try {

                    //JSONObject jsonObject = new JSONObject("{"transactionId":"1789327","requestTime":"Wed Oct 20 15:55:16 IST 2021","responseTime":"Wed Oct 20 15:55:16 IST 2021","resultCode":"0","resultDescription":"Transaction Successful","pageable":{"limit":500,"offset":0,"totalRecords":1},"walletOwnerList":[{"id":110382,"code":"1000002488","walletOwnerCategoryCode":"100010","ownerName":"Kundan","mobileNumber":"118110111","idProofNumber":"vc12345","email":"kundan.kumar@esteltelecom.com","status":"Active","state":"Approved","stage":"Document","idProofTypeCode":"100006","idProofTypeName":"OTHER","idExpiryDate":"2021-09-29","notificationLanguage":"en","notificationTypeCode":"100000","notificationName":"EMAIL","gender":"M","dateOfBirth":"1960-01-26","lastName":"New","issuingCountryCode":"100092","issuingCountryName":"Guinea","registerCountryCode":"100092","registerCountryName":"Guinea","createdBy":"100375","modifiedBy":"100322","creationDate":"2021-09-16T17:08:49.796+0530","modificationDate":"2021-09-16T17:10:17.009+0530","walletExists":true,"profileTypeCode":"100001","profileTypeName":"tier2","walletOwnerCatName":"Subscriber","occupationTypeCode":"100002","occupationTypeName":"Others","requestedSource":"ADMIN","regesterCountryDialCode":"+224","issuingCountryDialCode":"+224","walletOwnerCode":"1000002488"}]}");

                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");

                    if(resultCode.equalsIgnoreCase("0")) {

                        //  Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();

                        JSONArray jsonArray = jsonObject.getJSONArray("walletOwnerList");
                        for(int i=0;i<jsonArray.length();i++)
                        {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            walletOwnerCode_subs = jsonObject2.getString("walletOwnerCode");

                            countryCode_subscriber = jsonObject2.getString("registerCountryCode");
                            agentCode_subscriber = jsonObject2.getString("code");

                           // rp_tv_mobileNumber.setText(MyApplication.getSaveString("USERNAME",CashIn.this));
                            rp_tv_email.setText(jsonObject2.getString("email"));
                            rp_tv_country.setText(countryName_agent);

                            receivernameStr=jsonObject2.getString("ownerName");
                            receiverlastnameStr=jsonObject2.getString("lastName");
                            receivermobileNumberStr=jsonObject2.getString("mobileNumber");

                            rp_tv_receiverName.setText(receivernameStr +" " + receiverlastnameStr);
                            rp_tv_mobileNumber.setText(receivermobileNumberStr);
                        }

                        api_currency_sender();

                    }

                    else {
                        MyApplication.hideLoader();

                        Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();
                        //  finish();
                    }


                }
                catch (Exception e)
                {
                    MyApplication.hideLoader();

                    Toast.makeText(CashIn.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashIn.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }



    private void api_currency_subscriber() {

        API.GET_CASHIN_DETAILS("ewallet/api/v1/walletOwnerCountryCurrency/"+walletOwnerCode_subs,languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {


                try {

                    //JSONObject jsonObject = new JSONObject("{"transactionId":"1789322","requestTime":"Wed Oct 20 15:53:33 IST 2021","responseTime":"Wed Oct 20 15:53:33 IST 2021","resultCode":"0","resultDescription":"Transaction Successful","walletOwnerCountryCurrencyList":[{"id":7452,"code":"107451","walletOwnerCode":"1000002488","currencyCode":"100062","currencyName":"GNF","currencySymbol":"Fr","countryCurrencyCode":"100076","inBound":true,"outBound":true,"status":"Active"}]}");

                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");

                    if(resultCode.equalsIgnoreCase("0")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("walletOwnerCountryCurrencyList");
                        for(int i=0;i<jsonArray.length();i++) {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            if(jsonObject2.has("currencyName")) {

                                String  currencyName_subscriber_temp = jsonObject2.getString("currencyName");
                                if (currencyName_subscriber_temp.equalsIgnoreCase("GNF")) {
                                    currencyName_subscriber = jsonObject2.getString("currencyName");
                                    currencyCode_subscriber = jsonObject2.getString("currencyCode");
                                    currencySymbol_receiver = jsonObject2.getString("currencySymbol");
                                    tvAmtCurr.setText(jsonObject2.getString("currencySymbol"));

                                } else {

                                }
                            }


                        }

                        api_exchange_rate();

                    }

                    else {
                        MyApplication.hideLoader();

                        Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
                catch (Exception e)
                {
                    MyApplication.hideLoader();

                    Toast.makeText(CashIn.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashIn.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void api_currency_sender() {

        API.GET_CASHIN_DETAILS("ewallet/api/v1/walletOwnerCountryCurrency/"+walletOwnerCode_mssis_agent,languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {


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


                        // Toast.makeText(CashIn.this, currencyCode_subscriber, Toast.LENGTH_LONG).show();

                        api_currency_subscriber();

                    }

                    else {
                        MyApplication.hideLoader();

                        Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
                catch (Exception e)
                {
                    MyApplication.hideLoader();

                    Toast.makeText(CashIn.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashIn.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void service_Provider_api() {

        // Hard Code Final Deepak

        API.GET_CASHIN_DETAILS("ewallet/api/v1/serviceProvider/serviceCategory?serviceCode=100003&serviceCategoryCode=100011&status=Y",languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {


                try {

                    // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1789408\",\"requestTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"responseTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwnerUser\":{\"id\":2171,\"code\":\"101917\",\"firstName\":\"TATASnegal\",\"userName\":\"TATASnegal5597\",\"mobileNumber\":\"8888888882\",\"email\":\"kundan.kumar@esteltelecom.com\",\"walletOwnerUserTypeCode\":\"100000\",\"walletOwnerUserTypeName\":\"Supervisor\",\"walletOwnerCategoryCode\":\"100000\",\"walletOwnerCategoryName\":\"Institute\",\"userCode\":\"1000002606\",\"status\":\"Active\",\"state\":\"Approved\",\"gender\":\"M\",\"idProofTypeCode\":\"100004\",\"idProofTypeName\":\"MILITARY ID CARD\",\"idProofNumber\":\"44444444444\",\"creationDate\":\"2021-10-01T09:04:07.330+0530\",\"notificationName\":\"EMAIL\",\"notificationLanguage\":\"en\",\"createdBy\":\"100308\",\"modificationDate\":\"2021-10-20T14:59:00.791+0530\",\"modifiedBy\":\"100308\",\"addressList\":[{\"id\":3569,\"walletOwnerUserCode\":\"101917\",\"addTypeCode\":\"100001\",\"addTypeName\":\"Commercial\",\"regionCode\":\"100068\",\"regionName\":\"Boke\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"city\":\"100022\",\"cityName\":\"Dubreka\",\"addressLine1\":\"delhi\",\"status\":\"Inactive\",\"creationDate\":\"2021-10-01T09:04:07.498+0530\",\"createdBy\":\"100250\",\"modificationDate\":\"2021-10-03T09:52:57.407+0530\",\"modifiedBy\":\"100308\"}],\"workingDaysList\":[{\"id\":3597,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100000\",\"weekdaysName\":\"Monday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.518+0530\"},{\"id\":3598,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100001\",\"weekdaysName\":\"Tuesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.528+0530\"},{\"id\":3599,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100002\",\"weekdaysName\":\"Wednesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.538+0530\"},{\"id\":3600,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100003\",\"weekdaysName\":\"Thursday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.547+0530\"},{\"id\":3601,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100004\",\"weekdaysName\":\"Friday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.556+0530\"},{\"id\":3602,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100005\",\"weekdaysName\":\"Saturday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.565+0530\"},{\"id\":3603,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100006\",\"weekdaysName\":\"Sunday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"2:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.573+0530\"}],\"macEnabled\":false,\"ipEnabled\":false,\"resetCredReqInit\":false,\"notificationTypeCode\":\"100000\"}}");

                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");

                    if(resultCode.equalsIgnoreCase("0")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("serviceProviderList");
                        for(int i=0;i<jsonArray.length();i++) {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            serviceCode_from_serviceCategory = jsonObject2.getString("serviceCode");
                            serviceCategoryCode_from_serviceCategory = jsonObject2.getString("serviceCategoryCode");
                            serviceProviderCode_from_serviceCategory = jsonObject2.getString("code");
                        }

                        agent_details_api_walletownerUser();

                    }

                    else {
                        MyApplication.hideLoader();

                        Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
                catch (Exception e)
                {
                    MyApplication.hideLoader();

                    Toast.makeText(CashIn.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashIn.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    private void agent_details_api_walletownerUser() {

        String USER_CODE_FROM_TOKEN_AGENTDETAILS =  MyApplication.getSaveString("userCode",CashIn.this);


        API.GET_CASHIN_DETAILS("ewallet/api/v1/walletOwnerUser/"+USER_CODE_FROM_TOKEN_AGENTDETAILS,languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {


                try {

                    // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1789408\",\"requestTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"responseTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwnerUser\":{\"id\":2171,\"code\":\"101917\",\"firstName\":\"TATASnegal\",\"userName\":\"TATASnegal5597\",\"mobileNumber\":\"8888888882\",\"email\":\"kundan.kumar@esteltelecom.com\",\"walletOwnerUserTypeCode\":\"100000\",\"walletOwnerUserTypeName\":\"Supervisor\",\"walletOwnerCategoryCode\":\"100000\",\"walletOwnerCategoryName\":\"Institute\",\"userCode\":\"1000002606\",\"status\":\"Active\",\"state\":\"Approved\",\"gender\":\"M\",\"idProofTypeCode\":\"100004\",\"idProofTypeName\":\"MILITARY ID CARD\",\"idProofNumber\":\"44444444444\",\"creationDate\":\"2021-10-01T09:04:07.330+0530\",\"notificationName\":\"EMAIL\",\"notificationLanguage\":\"en\",\"createdBy\":\"100308\",\"modificationDate\":\"2021-10-20T14:59:00.791+0530\",\"modifiedBy\":\"100308\",\"addressList\":[{\"id\":3569,\"walletOwnerUserCode\":\"101917\",\"addTypeCode\":\"100001\",\"addTypeName\":\"Commercial\",\"regionCode\":\"100068\",\"regionName\":\"Boke\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"city\":\"100022\",\"cityName\":\"Dubreka\",\"addressLine1\":\"delhi\",\"status\":\"Inactive\",\"creationDate\":\"2021-10-01T09:04:07.498+0530\",\"createdBy\":\"100250\",\"modificationDate\":\"2021-10-03T09:52:57.407+0530\",\"modifiedBy\":\"100308\"}],\"workingDaysList\":[{\"id\":3597,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100000\",\"weekdaysName\":\"Monday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.518+0530\"},{\"id\":3598,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100001\",\"weekdaysName\":\"Tuesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.528+0530\"},{\"id\":3599,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100002\",\"weekdaysName\":\"Wednesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.538+0530\"},{\"id\":3600,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100003\",\"weekdaysName\":\"Thursday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.547+0530\"},{\"id\":3601,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100004\",\"weekdaysName\":\"Friday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.556+0530\"},{\"id\":3602,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100005\",\"weekdaysName\":\"Saturday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.565+0530\"},{\"id\":3603,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100006\",\"weekdaysName\":\"Sunday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"2:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.573+0530\"}],\"macEnabled\":false,\"ipEnabled\":false,\"resetCredReqInit\":false,\"notificationTypeCode\":\"100000\"}}");

                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");

                    if(resultCode.equalsIgnoreCase("0")) {

                        JSONObject walletOwnerUser = jsonObject.getJSONObject("walletOwnerUser");
                        if(walletOwnerUser.has("issuingCountryCode")){
                            countryCode_agent = walletOwnerUser.getString("issuingCountryCode");
                        }else{
                            countryCode_agent=MyApplication.getSaveString("COUNTRYCODE_AGENT", CashIn.this);
                        }
                        if(walletOwnerUser.has("issuingCountryName")){
                            countryName_agent = walletOwnerUser.getString("issuingCountryName");
                        }else{
                            countryName_agent=MyApplication.getSaveString("COUNTRYNAME_AGENT", CashIn.this);

                        }
                        if(walletOwnerUser.has("firstName")){
                            senderNameAgent = walletOwnerUser.getString("firstName");

                        }else{
                            senderNameAgent = "";
                        }

                        rp_tv_senderName.setText(senderNameAgent);

                        subscriber_details_api_walletownerUser();

                    }

                    else {
                        MyApplication.hideLoader();

                        Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
                catch (Exception e)
                {
                    MyApplication.hideLoader();

                    Toast.makeText(CashIn.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashIn.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void allByCriteria_walletOwnerCode_api() {


        API.GET_CASHIN_DETAILS("ewallet/api/v1/channel/allByCriteria?walletOwnerCode="+walletOwnerCode_mssis_agent,languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {


                try {

                    //JSONObject jsonObject = new JSONObject("{"transactionId":"1789322","requestTime":"Wed Oct 20 15:53:33 IST 2021","responseTime":"Wed Oct 20 15:53:33 IST 2021","resultCode":"0","resultDescription":"Transaction Successful","walletOwnerCountryCurrencyList":[{"id":7452,"code":"107451","walletOwnerCode":"1000002488","currencyCode":"100062","currencyName":"GNF","currencySymbol":"Fr","countryCurrencyCode":"100076","inBound":true,"outBound":true,"status":"Active"}]}");

                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");

                    if(resultCode.equalsIgnoreCase("0")) {



                        ll_page_1.setVisibility(View.GONE);
                        ll_successPage.setVisibility(View.GONE);
                        ll_reviewPage.setVisibility(View.VISIBLE);
                        ll_receiptPage.setVisibility(View.GONE);
                        MyApplication.hideLoader();



                    }

                    else {
                        MyApplication.hideLoader();

                        Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();
                        //   finish();
                    }


                }
                catch (Exception e)
                {
                    MyApplication.hideLoader();

                    Toast.makeText(CashIn.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashIn.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }



    boolean validation_mpin_detail()
    {

        mpinStr = et_mpin.getText().toString();

        if(mpinStr.trim().isEmpty()) {
            MyApplication.showErrorToast(this, getString(R.string.please_enter_4_digit_mpin));

            return false;
        }

        else if(mpinStr.trim().length() == 4) {


            return true;
        }
        else {

            MyApplication.showErrorToast(this, getString(R.string.please_enter_4_digit_mpin));

            return false;
        }


    }


    private void mpin_final_api() {

        try {

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("srcWalletOwnerCode", MyApplication.getSaveString("walletOwnerCode", CashIn.this));
            jsonObject.put("desWalletOwnerCode",agentCode_subscriber);
            jsonObject.put("srcCurrencyCode",currencyCode_agent);
            jsonObject.put("desCurrencyCode",currencyCode_subscriber);
            jsonObject.put("value",amountstr);
            String encryptionDatanew = AESEncryption.getAESEncryption(mpinStr);
            jsonObject.put("pin",encryptionDatanew);
            jsonObject.put("transactionType","100000");         // Hard Code according  to Deepak
            jsonObject.put("channelTypeCode","2");           // Hard Code according  to Deepak
            jsonObject.put("serviceCode",serviceCode_from_serviceCategory);
            jsonObject.put("serviceCategoryCode",serviceCategoryCode_from_serviceCategory);  // Hard Code according  to Deepak
            jsonObject.put("serviceProviderCode",serviceProviderCode_from_serviceCategory);  // Hard Code according  to Deepak


            System.out.println("CASHIN REQUEST================"+jsonObject.toString());
            String requestNo=AESEncryption.getAESEncryption(jsonObject.toString());
            JSONObject jsonObjectA=null;
            try{
                jsonObjectA=new JSONObject();
                jsonObjectA.put("request",requestNo);
            }catch (Exception e){

            }

            API.POST_CASHIN_MPIN("ewallet/api/v1/walletTransfer/cashIn", jsonObjectA, languageToUse, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {


                    try {

                        // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"116204\",\"requestTime\":\"Wed Oct 20 19:51:47 IST 2021\",\"responseTime\":\"Wed Oct 20 19:51:47 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletTransfer\":{\"code\":\"116204\",\"srcWalletCode\":\"1000024941\",\"desWalletCode\":\"1000022471\",\"srcWalletOwnerCode\":\"1000002606\",\"desWalletOwnerCode\":\"1000002488\",\"srcWalletTypeCode\":\"100008\",\"desWalletTypeCode\":\"100008\",\"srcCurrencyCode\":\"100062\",\"desCurrencyCode\":\"100062\",\"srcCurrencyName\":\"GNF\",\"desCurrencyName\":\"GNF\",\"srcCurrencySymbol\":\"Fr\",\"desCurrencySymbol\":\"Fr\",\"value\":2130,\"createdBy\":\"101917\",\"creationDate\":\"2021-10-20 19:51:47\",\"fee\":1000,\"finalAmount\":1000,\"srcWalletOwner\":{\"id\":110500,\"code\":\"1000002606\",\"walletOwnerCategoryCode\":\"100000\",\"ownerName\":\"TATASnegal\",\"mobileNumber\":\"8888888882\",\"businessTypeCode\":\"100001\",\"businessTypeName\":\"Telecom\",\"idProofNumber\":\"44444444444\",\"email\":\"kundan.kumar@esteltelecom.com\",\"status\":\"Active\",\"state\":\"Approved\",\"stage\":\"Document\",\"idProofTypeCode\":\"100004\",\"idProofTypeName\":\"MILITARY ID CARD\",\"notificationLanguage\":\"en\",\"notificationTypeCode\":\"100000\",\"notificationName\":\"EMAIL\",\"registerCountryCode\":\"100092\",\"registerCountryName\":\"Guinea\",\"createdBy\":\"100250\",\"modifiedBy\":\"100308\",\"creationDate\":\"2021-10-01T09:01:15.968+0530\",\"modificationDate\":\"2021-10-01T09:10:25.037+0530\",\"walletExists\":true,\"profileTypeCode\":\"100000\",\"profileTypeName\":\"tier1\",\"walletCurrencyList\":[\"100014\",\"100013\",\"100012\",\"100007\",\"100010\",\"100008\",\"100005\",\"100002\",\"100001\",\"100003\",\"100069\",\"100062\",\"100004\",\"100000\",\"100028\",\"100027\",\"100026\",\"100024\",\"100021\",\"100020\",\"100019\",\"100017\",\"100015\",\"100018\",\"100058\"],\"walletOwnerCatName\":\"Institute\",\"requestedSource\":\"ADMIN\",\"regesterCountryDialCode\":\"+224\",\"walletOwnerCode\":\"1000002606\"},\"desWalletOwner\":{\"id\":110382,\"code\":\"1000002488\",\"walletOwnerCategoryCode\":\"100010\",\"ownerName\":\"Kundan\",\"mobileNumber\":\"118110111\",\"idProofNumber\":\"vc12345\",\"email\":\"kundan.kumar@esteltelecom.com\",\"status\":\"Active\",\"state\":\"Approved\",\"stage\":\"Document\",\"idProofTypeCode\":\"100006\",\"idProofTypeName\":\"OTHER\",\"idExpiryDate\":\"2021-09-29\",\"notificationLanguage\":\"en\",\"notificationTypeCode\":\"100000\",\"notificationName\":\"EMAIL\",\"gender\":\"M\",\"dateOfBirth\":\"1960-01-26\",\"lastName\":\"New\",\"issuingCountryCode\":\"100092\",\"issuingCountryName\":\"Guinea\",\"registerCountryCode\":\"100092\",\"registerCountryName\":\"Guinea\",\"createdBy\":\"100375\",\"modifiedBy\":\"100322\",\"creationDate\":\"2021-09-16T17:08:49.796+0530\",\"modificationDate\":\"2021-09-16T17:10:17.009+0530\",\"walletExists\":true,\"profileTypeCode\":\"100001\",\"profileTypeName\":\"tier2\",\"walletOwnerCatName\":\"Subscriber\",\"occupationTypeCode\":\"100002\",\"occupationTypeName\":\"Others\",\"requestedSource\":\"ADMIN\",\"regesterCountryDialCode\":\"+224\",\"issuingCountryDialCode\":\"+224\",\"walletOwnerCode\":\"1000002488\"},\"taxConfigurationList\":[{\"taxTypeCode\":\"100133\",\"taxTypeName\":\"Financial Tax\",\"value\":130,\"taxAvailBy\":\"Fee Amount\"}],\"transactionType\":\"CASH-IN\"}}");

                        String resultCode = jsonObject.getString("resultCode");
                        String resultDescription = jsonObject.getString("resultDescription");

                        if (resultCode.equalsIgnoreCase("0")) {

                            String currencySymbolsender="";
                            String currencySymbolreceiver="";

                            if(jsonObject.has("walletTransfer")) {


                                JSONObject jsonObject_walletTransfer = jsonObject.getJSONObject("walletTransfer");

                                currencySymbolsender = jsonObject_walletTransfer.getString("srcCurrencySymbol");
                                currencySymbolreceiver = jsonObject_walletTransfer.getString("desCurrencySymbol");
                                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
                                DecimalFormat df = new DecimalFormat("0.00",symbols);

                                receiptPage_tv_stransactionType.setText(getString(R.string.cash_In));
                                receiptPage_tv_transactionAmount.setText(currencySymbolsender+" "+MyApplication.addDecimal(amountstr));
                                receiptPage_tv_fee.setText(currencySymbolsender+" "+df.format(feeDouble));

                                receipt_tv_amount_to_be_charge.setText(currencySymbol_sender+" "+MyApplication.addDecimal(totalAmount_str));

                                receiptPage_tv_transaction_receiptNo.setText(jsonObject.getString("transactionId"));

                                receiptPage_tv_dateOfTransaction.setText((MyApplication.convertUTCToLocaldate(jsonObject.getString("responseTime"))));

                                System.out.println("get date"+jsonObject.getString("responseTime"));

                                receiptPage_tv_amount_to_be_credit.setText(currencySymbolreceiver+" "+MyApplication.addDecimal(amountstr));

                                receiptPage_tv_sender_name.setText(senderNameAgent+ " "+receiverlastnameStr);
                                receiptPage_tv_sender_phoneNo.setText(MyApplication.getSaveString("USERNAME", CashIn.this));

                                receiptPage_tv_sender_name.setText(senderNameAgent);
                                receiptPage_tv_sender_phoneNo.setText(MyApplication.getSaveString("USERNAME", CashIn.this));

                                receiptPage_tv_receiver_name.setText(receivernameStr+" "+receiverlastnameStr);
                                receiptPage_tv_receiver_phoneNo.setText(mobileNoStr);

                                ll_page_1.setVisibility(View.GONE);
                                ll_reviewPage.setVisibility(View.GONE);
                                ll_successPage.setVisibility(View.VISIBLE);
                                ll_receiptPage.setVisibility(View.GONE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ll_page_1.setVisibility(View.GONE);
                                        ll_reviewPage.setVisibility(View.GONE);
                                        ll_successPage.setVisibility(View.GONE);
                                        ll_receiptPage.setVisibility(View.VISIBLE);
                                    }
                                }, 2000);
                                MyApplication.hideLoader();

                            }


                        } else {
                            MyApplication.hideLoader();

                            Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();
                            //  finish();
                        }


                    } catch (Exception e) {
                        MyApplication.hideLoader();

                        Toast.makeText(CashIn.this, e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }


                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(CashIn.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    }
                    else

                        Toast.makeText(CashIn.this, aFalse, Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (Exception e)
        {
            Toast.makeText(CashIn.this,e.toString(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }

    private void api_exchange_rate() {

       /* if(amountstr.matches(".*\\s.*")){
            amountstr=edittext_amount.getText().toString().trim().replaceAll("\\s","");
        }else{
            amountstr=edittext_amount.getText().toString().trim().replaceAll(",","");
        }*/

        // API.GET_CASHIN_DETAILS("ewallet/api/v1/exchangeRate/getAmountDetails?sendCurrencyCode=100062&receiveCurrencyCode=100062&sendCountryCode=100092&receiveCountryCode=&currencyValue=1000&channelTypeCode=100000&serviceCode=100003&serviceCategoryCode=100011&serviceProviderCode=100106&walletOwnerCode=1000002606&remitAgentCode=1000002606&payAgentCode=1000002488",languageToUse,new Api_Responce_Handler() {

        http://202.131.144.130:8081/ewallet/api/v1/serviceProvider/serviceCategory?serviceCode=100003&serviceCategoryCode=100011&status=Y
        API.GET_CASHOUT_DETAILS("ewallet/api/v1/exchangeRate/getAmountDetails?sendCurrencyCode=" + currencyCode_agent + "&receiveCurrencyCode="+currencyCode_subscriber+"&sendCountryCode=" + countryCode_agent + "&receiveCountryCode="+ countryCode_subscriber+// countryCode_subscriber Blanck Acording to Web
                        countryCode_subscriber+ "&currencyValue=" + amountstr + "&channelTypeCode=100002&serviceCode=" + serviceCode_from_serviceCategory + "&serviceCategoryCode=" + serviceCategoryCode_from_serviceCategory + "&serviceProviderCode=" +
                serviceProviderCode_from_serviceCategory + "&walletOwnerCode=" + walletOwnerCode_mssis_agent + "&remitAgentCode=" +
                        walletOwnerCode_subs + "&payAgentCode="+agentCode_subscriber,

                languageToUse, new Api_Responce_Handler() {

            @Override
            public void success(JSONObject jsonObject) {


                try {

                    System.out.println("get response"+jsonObject.toString());

                    //JSONObject jsonObject = new JSONObject("{"transactionId":"1789322","requestTime":"Wed Oct 20 15:53:33 IST 2021","responseTime":"Wed Oct 20 15:53:33 IST 2021","resultCode":"0","resultDescription":"Transaction Successful","walletOwnerCountryCurrencyList":[{"id":7452,"code":"107451","walletOwnerCode":"1000002488","currencyCode":"100062","currencyName":"GNF","currencySymbol":"Fr","countryCurrencyCode":"100076","inBound":true,"outBound":true,"status":"Active"}]}");

                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");

                    if(resultCode.equalsIgnoreCase("0")) {

                        //Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();

                        JSONObject exchangeRate = jsonObject.getJSONObject("exchangeRate");


                        System.out.println("get exchangeRate"+exchangeRate.toString());

                        fees_amount = exchangeRate.getString("fee");
                         feeDouble= Double.parseDouble(fees_amount);
                        tax_financial = exchangeRate.getString("value");
                        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
                        DecimalFormat df = new DecimalFormat("0.00",symbols);

                        rp_tv_fees_reveiewPage.setText(currencySymbol_sender+" "  + df.format(feeDouble));

                        //  credit_amount=exchangeRate.getString("currencyValue");




                        if(exchangeRate.has("taxConfigurationList"))
                        {
                            JSONArray jsonArray = exchangeRate.getJSONArray("taxConfigurationList");
                            for(int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                 tax_financialnew = jsonObject2.getString("value");


                                tax_financialtypename = jsonObject2.getString("taxTypeName");

                                //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                               // TextView textView = new TextView(CashIn.this);

                                System.out.println("get tax"+tax_financialnew);
                                taxcashinLinear.setVisibility(View.VISIBLE);
                                financialTax_receiptPageLinear.setVisibility(View.VISIBLE);
                              //  taxcashinLinear.addView(textView, lp);

                                rp_tv_financialTax.setText((tax_financialtypename+":"  + " "+currencySymbol_receiver+" "+ MyApplication.addDecimal(tax_financialnew)));

                                financialTax_receiptPage.setText(rp_tv_financialTax.getText().toString());





                            }
                        }
                        else {
                            tax_financial = exchangeRate.getString("value");
                            tax_financialnew = exchangeRate.getString("value");

                        }




                    //    rp_tv_financialTax.setText(currencySymbol_sender+" "  +MyApplication.addDecimal(tax_financial));

//                        tax_financial_double = Double.parseDouble(tax_financial);
                        fees_amount_double = Double.parseDouble((fees_amount));
                        amountstr_double = Double.parseDouble(amountstr);
                        tax_financialnewDouble=Double.parseDouble(tax_financialnew);
                        totalAmount_double = tax_financialnewDouble+amountstr_double+fees_amount_double;
                        totalAmount_str = String.valueOf(totalAmount_double);
                        double dtotalAmount_str= Double.parseDouble(totalAmount_str);
                        System.out.println("get sonu"+amountstr_double);
                        System.out.println("get value2"+amountstr_double);
                        System.out.println("get value3"+fees_amount_double);

                        System.out.println("get value4"+totalAmount_double);


                        rp_tv_amount_to_be_charge.setText(currencySymbol_sender+" "+ df.format(dtotalAmount_str));

                        amountstr = String.valueOf(amountstr_double);
                        rp_tv_transactionAmount.setText(currencySymbol_sender+" "+MyApplication.addDecimal(amountstr));
                        rp_tv_amount_to_be_credit.setText(currencySymbol_receiver+" "+df.format(amountstr_double));

                        allByCriteria_walletOwnerCode_api();

                    }

                    else {
                        MyApplication.hideLoader();

                        Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
                catch (Exception e)
                {

                    MyApplication.hideLoader();

                    Toast.makeText(CashIn.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashIn.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    public static int clickedCount=0;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_nextClick: {


                if (validation_mobile_Details()) {


                    if (new InternetCheck().isConnected(CashIn.this)) {

                        MyApplication.showloader(CashIn.this, getString(R.string.please_wait));
                        service_Provider_api();

                    } else {
                        Toast.makeText(CashIn.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                    }
                }
            }

            break;

            case R.id.confirm_reviewClick_textview: {

                if(pinLinear.getVisibility()==View.VISIBLE){
                    if (validation_mpin_detail()) {

                        if (new InternetCheck().isConnected(CashIn.this)) {

                            MyApplication.showloader(CashIn.this, getString(R.string.please_wait));

                            mpin_final_api();

                        } else {
                            Toast.makeText(CashIn.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                        }
                    }
                }else {
                    MyApplication.biometricAuth(CashIn.this, new BioMetric_Responce_Handler() {
                        @Override
                        public void success(String success) {
                            try {

                                //  String encryptionDatanew = AESEncryption.getAESEncryption(MyApplication.getSaveString("pin",MyApplication.appInstance).toString().trim());
                                mpinStr = MyApplication.getSaveString("pin", MyApplication.appInstance);

                                if (new InternetCheck().isConnected(CashIn.this)) {

                                    MyApplication.showloader(CashIn.this, getString(R.string.please_wait));

                                    mpin_final_api();

                                } else {
                                    Toast.makeText(CashIn.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failure(String failure) {

                            MyApplication.showToast(CashIn.this, failure);

                            pinLinear.setVisibility(View.VISIBLE);


                        }
                    });
                }
//new update
              /*  BiometricManager biometricManager = androidx.biometric.BiometricManager.from(CashIn.this);
                switch (biometricManager.canAuthenticate()) {

                    // this means we can use biometric sensor
                    case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:

                        Toast.makeText(CashIn.this, getString(R.string.device_not_contain_fingerprint), Toast.LENGTH_SHORT).show();
                        pinLinear.setVisibility(View.VISIBLE);
                        if (validation_mpin_detail()) {

                            if (new InternetCheck().isConnected(CashIn.this)) {

                                MyApplication.showloader(CashIn.this, getString(R.string.please_wait));

                                mpin_final_api();

                            } else {
                                Toast.makeText(CashIn.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                            }
                            // msgText.setText("You can use the fingerprint sensor to login");
                            // msgText.setTextColor(Color.parseColor("#fafafa"));
                            return;
                        }
                    case BiometricManager.BIOMETRIC_SUCCESS:

                        // msgText.setText("You can use the fingerprint sensor to login");
                        // msgText.setTextColor(Color.parseColor("#fafafa"));
                            MyApplication.biometricAuth(CashIn.this, new BioMetric_Responce_Handler() {
                                @Override
                                public void success(String success) {
                                    try {

                                        //  String encryptionDatanew = AESEncryption.getAESEncryption(MyApplication.getSaveString("pin",MyApplication.appInstance).toString().trim());
                                        mpinStr = MyApplication.getSaveString("pin", MyApplication.appInstance);

                                        if (new InternetCheck().isConnected(CashIn.this)) {

                                            MyApplication.showloader(CashIn.this, getString(R.string.please_wait));

                                            mpin_final_api();

                                        } else {
                                            Toast.makeText(CashIn.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void failure(String failure) {
                                    MyApplication.showToast(CashIn.this, failure);

                                }

                            });
                        }
*/






               /* if (validation_mpin_detail()) {

                    if (new InternetCheck().isConnected(CashIn.this)) {

                        MyApplication.showloader(CashIn.this, getString(R.string.please_wait));

                        mpin_final_api();

                    } else {
                        Toast.makeText(CashIn.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                    }
                }*/

            }
            break;



            case R.id.tvContinue: {

                ll_page_1.setVisibility(View.GONE);
                ll_reviewPage.setVisibility(View.GONE);
                ll_successPage.setVisibility(View.GONE);
                ll_receiptPage.setVisibility(View.VISIBLE);

            }
            break;


            case R.id.exportReceipt_textview: {
                close_receiptPage_textview.setVisibility(View.VISIBLE);
                exportReceipt_textview.setVisibility(View.VISIBLE);
                Bitmap bitmap = getScreenShot(rootView);
                createImageFile(bitmap);
                //store(bitmap, "test.jpg");
            }

            break;

            case R.id.previous_reviewClick_textview: {

                ll_page_1.setVisibility(View.VISIBLE);
                ll_reviewPage.setVisibility(View.GONE);
                ll_successPage.setVisibility(View.GONE);
                ll_receiptPage.setVisibility(View.GONE);
            }
            break;

            case R.id.qrCode_imageButton: {


                qrScan();

            }

            break;

            case R.id.close_receiptPage_textview:
            {
//                    ll_page_1.setVisibility(View.VISIBLE);
//                    ll_reviewPage.setVisibility(View.GONE);
//                    ll_receiptPage.setVisibility(View.GONE);

                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }

            break;

        }
    }


    public void qrScan(){

        Intent i = new Intent(CashIn.this, QrCodeActivity.class);
        startActivityForResult( i,REQUEST_CODE_QR_SCAN);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String requiredValue = data.getStringExtra("PHONE");
            edittext_mobileNuber.setText(requiredValue);
            edittext_amount.requestFocus();
           // MyApplication.showKeyboard(CashIn.this,edittext_amount);
        }

        if (resultCode != Activity.RESULT_OK) {
            Log.d("LOGTAG", "COULD NOT GET A GOOD RESULT.");
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (result != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(CashIn.this).create();
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

                    MyApplication.showToast(CashIn.this,jsonObject.optString("resultDescription"));
                    mobileNoStr="";
                    edittext_mobileNuber.setText("");

                }

            }

            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(CashIn.this,aFalse);
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(ll_reviewPage.getVisibility()==View.VISIBLE) {
            ll_page_1.setVisibility(View.VISIBLE);
            ll_reviewPage.setVisibility(View.GONE);
            ll_successPage.setVisibility(View.GONE);
            ll_receiptPage.setVisibility(View.GONE);
            return;
        }
       super.onBackPressed();
    }


    //
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



        if(MyApplication.checkMinMax(CashIn.this,s,editText
                ,MyApplication.ToCashInMinAmount,MyApplication.ToCashInMaxAmount)){
            return;
        }
        isFormatting = true;
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