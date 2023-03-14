package com.agent.cashmoovui.remmetience;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.agent.cashmoovui.LogoutAppCompactActivity;

import com.agent.cashmoovui.HiddenPassTransformationMethod;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.activity.OtherOption;
import com.agent.cashmoovui.adapter.CustomeBaseAdapterGender;
import com.agent.cashmoovui.adapter.CustomeBaseAdapterProvided;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.login.LoginPin;
import com.agent.cashmoovui.otp.VerifyLoginAccountScreen;
import com.agent.cashmoovui.set_pin.AESEncryption;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CashToWallet extends LogoutAppCompactActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    public static LoginPin loginpinC;
    View rootView;
    ImageView imgBack,imgHome;
    Spinner spinner_gender,spinner_provider;
    ArrayList<String> arrayList_genderName;
    ArrayList<String> arrayList_genderCode;

    boolean  isPasswordVisible;

    String  currencyCode_agent="",countryCode_agent="",currencyName_agent="",countryName_agent;
    String  currencyCode_subscriber="",countryCode_subscriber="",currencyName_subscriber="",countryName_subscriber="",agentCode_subscriber="";


    String  firstName_sender_from_walletOwnerUser="",lastName_sender_from_walletOwnerUser="",
            email_sender_from_walletOwnerUser="",idProofTypeCode_sender_from_walletOwnerUser="",idProofNumber_sender_from_walletOwnerUser=""
            ,idExpiryDate_sender_from_walletOwnerUser="",dateOfBirth_sender_from_walletOwnerUser="",
            regionCode_sender_from_walletOwnerUser="",city_sender_from_walletOwnerUser="",address_sender_from_walletOwnerUser,mobileNumber_sender_from_walletOwnerUser="",gender_sender_from_allByCriteria="";






    TextView fees_first_page,amountTobeCharged_first_page,tax_first_page,convertionRate_first_page,receiptPage_tv_senderCurrency,receiptPage_tv_senderCode,receiptPage_tv_benificiary_no,receiptPage_tv_BenificiaryCurrency,receiptPage_tv_confirmation_code,exportReceipt_textview, rp_tv_comment, rp_tv_convertionFees, tv_nextClick, rp_tv_benificicaryCode, rp_tv_sender_id, rp_tv_agentCode, rp_tv_senderDocument, rp_tv_sending_currency,rp_tv_ReceiverMSISDN, rp_tv_beneficiaryCurrency, rp_tv_transactionAmount, rp_tv_fees_reveiewPage, receiptPage_tv_stransactionType, receiptPage_tv_dateOfTransaction, receiptPage_tv_transactionAmount,
            receiptPage_tv_amount_to_be_charge, receiptPage_tv_fee, receiptPage_tv_financialtax, receiptPage_tv_transaction_receiptNo, receiptPage_tv_sender_name,
            receiptPage_tv_sender_phoneNo,
            receiptPage_tv_receiver_name, receiptPage_tv_receiver_phoneNo, close_receiptPage_textview, rp_tv_financialTax, rp_tv_amount_to_be_charge, rp_tv_amount_to_be_paid, previous_reviewClick_textview, confirm_reviewClick_textview;
    LinearLayout ll_page_1, ll_reviewpage, ll_receiptPage, main_layout;

    MyApplication applicationComponentClass;
    String languageToUse = "";

    EditText et_subscriberNo, edittext_amount, et_mpin,et_fp_deviceSender,edittext_amount_pay,et_fp_desinationName,et_fp_firstNameDestination;



    String mobileNoStr = "", amountstr = "",currecnyname_from_walletOwnerCountryCurrency="",countryName_from_walletOwnerCountryCurrency="",genderSelect_name = "",provider_select_name = "", genderSelect_code = "",walletOwnerCode_mssis_agent = "", walletOwnerCode_subs, agentCodeStr = "", senderIdStr = "", benificicaryCodeStr;
    String firstname_destinationStr="",deviceSenderStr="",amountToPayStr="",tax_financial = "", fees_amount, totalAmount_str, receivernameStr = "";
    Double tax_financial_double = 0.0, amountstr_double = 0.0, fees_amount_double = 0.0, totalAmount_double = 0.0;

    String mpinStr = "", convertionFeesStr="",senderCode_from_senderApi="",receivercode_from_receiverAPi="";


    String name_destinationStr ="",serviceCode_from_serviceCategory = "", serviceCategoryCode_from_serviceCategory = "", serviceProviderCode_from_serviceCategory;


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


            setContentView(R.layout.cash_to_wallet);
            setBackMenu();




            rootView = getWindow().getDecorView().findViewById(R.id.main_layout);

            //     First page

            ll_page_1 = (LinearLayout) findViewById(R.id.ll_page_1);

            tv_nextClick = (TextView) findViewById(R.id.tv_nextClick);
            et_subscriberNo = (EditText) findViewById(R.id.et_subscriberNo);
            edittext_amount = (EditText) findViewById(R.id.edittext_amount);

            et_fp_deviceSender = (EditText) findViewById(R.id.et_fp_deviceSender);
            edittext_amount_pay = (EditText) findViewById(R.id.edittext_amount_pay);

            spinner_gender = (Spinner) findViewById(R.id.spinner_gender);
            spinner_provider = (Spinner) findViewById(R.id.spinner_provider);

            spinner_gender.setOnItemSelectedListener(this);
            spinner_provider.setOnItemSelectedListener(this);


            //    Reveiw page

            ll_reviewpage = (LinearLayout) findViewById(R.id.ll_reviewpage);

            rp_tv_agentCode = (TextView) findViewById(R.id.rp_tv_agentCode);
            rp_tv_ReceiverMSISDN = (TextView) findViewById(R.id.rp_tv_ReceiverMSISDN);
            rp_tv_sender_id = (TextView) findViewById(R.id.rp_tv_sender_id);
            rp_tv_benificicaryCode = (TextView) findViewById(R.id.rp_tv_benificicaryCode);
            rp_tv_senderDocument = (TextView) findViewById(R.id.rp_tv_senderDocument);
            rp_tv_sending_currency = (TextView) findViewById(R.id.rp_tv_sending_currency);
            rp_tv_beneficiaryCurrency = (TextView) findViewById(R.id.rp_tv_beneficiaryCurrency);
            rp_tv_transactionAmount = (TextView) findViewById(R.id.rp_tv_transactionAmount);
            rp_tv_convertionFees = (TextView) findViewById(R.id.rp_tv_convertionFees);
            rp_tv_fees_reveiewPage = (TextView) findViewById(R.id.rp_tv_fees_reveiewPage);
            rp_tv_financialTax = (TextView) findViewById(R.id.rp_tv_financialTax);
            rp_tv_amount_to_be_charge = (TextView) findViewById(R.id.rp_tv_amount_to_be_charge);
            rp_tv_amount_to_be_paid = (TextView) findViewById(R.id.rp_tv_amount_to_be_paid);
            rp_tv_comment = (TextView) findViewById(R.id.rp_tv_comment);

            et_fp_desinationName = (EditText) findViewById(R.id.et_fp_desinationName);
            et_fp_firstNameDestination = (EditText) findViewById(R.id.et_fp_firstNameDestination);


            et_mpin = (EditText) findViewById(R.id.et_mpin);
            previous_reviewClick_textview = (TextView) findViewById(R.id.previous_reviewClick_textview);
            confirm_reviewClick_textview = (TextView) findViewById(R.id.confirm_reviewClick_textview);

            //    Receipt page

            ll_receiptPage = (LinearLayout) findViewById(R.id.ll_receiptPage);
            main_layout = (LinearLayout) findViewById(R.id.main_layout);
            exportReceipt_textview = (TextView) findViewById(R.id.exportReceipt_textview);
            exportReceipt_textview.setOnClickListener(this);


            fees_first_page = (TextView) findViewById(R.id.fees_first_page);
            amountTobeCharged_first_page = (TextView) findViewById(R.id.amountTobeCharged_first_page);
            tax_first_page = (TextView) findViewById(R.id.tax_first_page);

            convertionRate_first_page = (TextView) findViewById(R.id.convertionRate_first_page);
            receiptPage_tv_senderCurrency = (TextView) findViewById(R.id.receiptPage_tv_senderCurrency);
            receiptPage_tv_BenificiaryCurrency = (TextView) findViewById(R.id.receiptPage_tv_BenificiaryCurrency);

            receiptPage_tv_senderCode = (TextView) findViewById(R.id.receiptPage_tv_senderCode);
            receiptPage_tv_benificiary_no = (TextView) findViewById(R.id.receiptPage_tv_benificiary_no);



            receiptPage_tv_transaction_receiptNo = (TextView) findViewById(R.id.receiptPage_tv_transaction_receiptNo);
            receiptPage_tv_stransactionType = (TextView) findViewById(R.id.receiptPage_tv_stransactionType);
            receiptPage_tv_confirmation_code = (TextView) findViewById(R.id.receiptPage_tv_confirmation_code);
            receiptPage_tv_dateOfTransaction = (TextView) findViewById(R.id.receiptPage_tv_dateOfTransaction);
            receiptPage_tv_transactionAmount = (TextView) findViewById(R.id.receiptPage_tv_transactionAmount);
            receiptPage_tv_amount_to_be_charge = (TextView) findViewById(R.id.receiptPage_tv_amount_to_be_charge);
            receiptPage_tv_fee = (TextView) findViewById(R.id.receiptPage_tv_fee);
            receiptPage_tv_financialtax = (TextView) findViewById(R.id.receiptPage_tv_financialtax);
            receiptPage_tv_sender_name = (TextView) findViewById(R.id.receiptPage_tv_sender_name);
            receiptPage_tv_sender_phoneNo = (TextView) findViewById(R.id.receiptPage_tv_sender_phoneNo);
            receiptPage_tv_receiver_name = (TextView) findViewById(R.id.receiptPage_tv_receiver_name);
            receiptPage_tv_receiver_phoneNo = (TextView) findViewById(R.id.receiptPage_tv_receiver_phoneNo);
            close_receiptPage_textview = (TextView) findViewById(R.id.close_receiptPage_textview);

            tv_nextClick.setOnClickListener(this);
            previous_reviewClick_textview.setOnClickListener(this);
            confirm_reviewClick_textview.setOnClickListener(this);
            close_receiptPage_textview.setOnClickListener(this);

            et_subscriberNo.setEnabled(true);
            edittext_amount.setEnabled(false);


            walletOwnerCode_mssis_agent = MyApplication.getSaveString("walletOwnerCode", CashToWallet.this);

            if (new InternetCheck().isConnected(CashToWallet.this)) {

                MyApplication.showloader(CashToWallet.this, getString(R.string.please_wait));

                api_country_all();


            } else {
                Toast.makeText(CashToWallet.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
            }



            et_subscriberNo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (new InternetCheck().isConnected(CashToWallet.this)) {

                        mobileNoStr = et_subscriberNo.getText().toString().trim();

                        if (mobileNoStr.length()>9) {

                            edittext_amount.setEnabled(true);


                            api_subscriberDetails();
                        }

                        else {
                            et_subscriberNo.setHint(getString(R.string.plz_enter_subscriber_no));
                        }

                    } else {
                        Toast.makeText(CashToWallet.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                    }
                }
            });

            edittext_amount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (new InternetCheck().isConnected(CashToWallet.this)) {

                        amountstr = edittext_amount.getText().toString().trim();

                        if (amountstr.length()>1) {

                            api_exchangeRate();

                        }

                        else {
                            edittext_amount_pay.setText(getString(R.string.amount_to_pay));;
                        }

                    } else {
                        Toast.makeText(CashToWallet.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                    }
                }
            });

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
                        MyApplication.hideKeyboard(CashToWallet.this);            }
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




        } catch (Exception e) {
            Toast.makeText(CashToWallet.this, e.toString(), Toast.LENGTH_LONG).show();

        }

    }


    public static JSONArray taxConfigurationList;



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
                MyApplication.hideKeyboard(CashToWallet.this);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(CashToWallet.this);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private void api_country_all() {

        API.GET_REMMITANCE_DETAILS("ewallet/api/v1/country/all", languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {


                    //  JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1840087\",\"requestTime\":\"Tue Oct 26 00:01:09 IST 2021\",\"responseTime\":\"Tue Oct 26 00:01:09 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"countryList\":[{\"id\":1,\"code\":\"100000\",\"isoCode\":\"AFG\",\"name\":\"Afghanistan\",\"countryCode\":\"AF\",\"status\":\"Active\",\"dialCode\":\"+93\",\"mobileLength\":\"9\",\"currencyCode\":\"AFN\",\"currencySymbol\":\"؋\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:16.312+0530\"},{\"id\":249,\"code\":\"100248\",\"isoCode\":\"ALA\",\"name\":\"Åland Islands\",\"countryCode\":\"AX\",\"status\":\"Active\",\"dialCode\":\"+358\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:26.875+0530\"},{\"id\":2,\"code\":\"100001\",\"isoCode\":\"ALB\",\"name\":\"Albania\",\"countryCode\":\"AL\",\"status\":\"Active\",\"dialCode\":\"+355\",\"mobileLength\":\"9\",\"currencyCode\":\"ALL\",\"currencySymbol\":\"L\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:16.630+0530\"},{\"id\":3,\"code\":\"100002\",\"isoCode\":\"DZA\",\"name\":\"Algeria\",\"countryCode\":\"DZ\",\"status\":\"Active\",\"dialCode\":\"+213\",\"mobileLength\":\"9\",\"currencyCode\":\"DZD\",\"currencySymbol\":\"د.ج\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:16.915+0530\"},{\"id\":4,\"code\":\"100003\",\"isoCode\":\"ASM\",\"name\":\"American Samoa\",\"countryCode\":\"AS\",\"status\":\"Active\",\"dialCode\":\"+1684\",\"mobileLength\":\"10\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:17.218+0530\"},{\"id\":5,\"code\":\"100004\",\"isoCode\":\"AND\",\"name\":\"Andorra\",\"countryCode\":\"AD\",\"status\":\"Active\",\"dialCode\":\"+376\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:17.508+0530\"},{\"id\":6,\"code\":\"100005\",\"isoCode\":\"AGO\",\"name\":\"Angola\",\"countryCode\":\"AO\",\"status\":\"Active\",\"dialCode\":\"+244\",\"currencyCode\":\"AOA\",\"currencySymbol\":\"Kz\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:17.790+0530\"},{\"id\":7,\"code\":\"100006\",\"isoCode\":\"AIA\",\"name\":\"Anguilla\",\"countryCode\":\"AI\",\"status\":\"Active\",\"dialCode\":\"+1264\",\"mobileLength\":\"7\",\"currencyCode\":\"XCD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:18.078+0530\"},{\"id\":8,\"code\":\"100007\",\"isoCode\":\"ATA\",\"name\":\"Antarctica\",\"countryCode\":\"AQ\",\"status\":\"Active\",\"dialCode\":\"+672\",\"currencyCode\":\"AUD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:18.362+0530\"},{\"id\":9,\"code\":\"100008\",\"isoCode\":\"ATG\",\"name\":\"Antigua and Barbuda\",\"countryCode\":\"AG\",\"status\":\"Active\",\"dialCode\":\"+1268\",\"mobileLength\":\"7\",\"currencyCode\":\"XCD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:18.638+0530\"},{\"id\":10,\"code\":\"100009\",\"isoCode\":\"ARG\",\"name\":\"Argentina\",\"countryCode\":\"AR\",\"status\":\"Active\",\"dialCode\":\"+54\",\"mobileLength\":\"11\",\"currencyCode\":\"ARS\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:18.916+0530\"},{\"id\":11,\"code\":\"100010\",\"isoCode\":\"ARM\",\"name\":\"Armenia\",\"countryCode\":\"AM\",\"status\":\"Active\",\"dialCode\":\"+374\",\"mobileLength\":\"8\",\"currencyCode\":\"AMD\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:19.215+0530\"},{\"id\":12,\"code\":\"100011\",\"isoCode\":\"ABW\",\"name\":\"Aruba\",\"countryCode\":\"AW\",\"status\":\"Active\",\"dialCode\":\"+297\",\"mobileLength\":\"7\",\"currencyCode\":\"AWG\",\"currencySymbol\":\"ƒ\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:19.506+0530\"},{\"id\":13,\"code\":\"100012\",\"isoCode\":\"AUS\",\"name\":\"Australia\",\"countryCode\":\"AU\",\"status\":\"Active\",\"dialCode\":\"+61\",\"mobileLength\":\"9\",\"currencyCode\":\"AUD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:19.782+0530\"},{\"id\":14,\"code\":\"100013\",\"isoCode\":\"AUT\",\"name\":\"Austria\",\"countryCode\":\"AT\",\"status\":\"Active\",\"dialCode\":\"+43\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:20.067+0530\"},{\"id\":15,\"code\":\"100014\",\"isoCode\":\"AZE\",\"name\":\"Azerbaijan\",\"countryCode\":\"AZ\",\"status\":\"Active\",\"dialCode\":\"+994\",\"currencyCode\":\"AZN\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:20.358+0530\"},{\"id\":16,\"code\":\"100015\",\"isoCode\":\"BHS\",\"name\":\"Bahamas\",\"countryCode\":\"BS\",\"status\":\"Active\",\"dialCode\":\"+1242\",\"mobileLength\":\"7\",\"currencyCode\":\"BSD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:20.645+0530\"},{\"id\":17,\"code\":\"100016\",\"isoCode\":\"BHR\",\"name\":\"Bahrain\",\"countryCode\":\"BH\",\"status\":\"Active\",\"dialCode\":\"+973\",\"mobileLength\":\"8\",\"currencyCode\":\"BHD\",\"currencySymbol\":\".د.ب\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:20.923+0530\"},{\"id\":18,\"code\":\"100017\",\"isoCode\":\"BGD\",\"name\":\"Bangladesh\",\"countryCode\":\"BD\",\"status\":\"Active\",\"dialCode\":\"+880\",\"mobileLength\":\"10\",\"currencyCode\":\"BDT\",\"currencySymbol\":\"৳\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:21.206+0530\"},{\"id\":19,\"code\":\"100018\",\"isoCode\":\"BRB\",\"name\":\"Barbados\",\"countryCode\":\"BB\",\"status\":\"Active\",\"dialCode\":\"+1246\",\"mobileLength\":\"7\",\"currencyCode\":\"BBD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:21.499+0530\"},{\"id\":20,\"code\":\"100019\",\"isoCode\":\"BLR\",\"name\":\"Belarus\",\"countryCode\":\"BY\",\"status\":\"Active\",\"dialCode\":\"+375\",\"mobileLength\":\"9\",\"currencyCode\":\"BYN\",\"currencySymbol\":\"Br\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:21.784+0530\"},{\"id\":21,\"code\":\"100020\",\"isoCode\":\"BEL\",\"name\":\"Belgium\",\"countryCode\":\"BE\",\"status\":\"Active\",\"dialCode\":\"+32\",\"mobileLength\":\"9\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:22.068+0530\"},{\"id\":22,\"code\":\"100021\",\"isoCode\":\"BLZ\",\"name\":\"Belize\",\"countryCode\":\"BZ\",\"status\":\"Active\",\"dialCode\":\"+501\",\"currencyCode\":\"BZD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:22.351+0530\"},{\"id\":23,\"code\":\"100022\",\"isoCode\":\"BEN\",\"name\":\"Benin\",\"countryCode\":\"BJ\",\"status\":\"Active\",\"dialCode\":\"+229\",\"mobileLength\":\"8\",\"currencyCode\":\"XOF\",\"currencySymbol\":\"CFA\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:22.629+0530\"},{\"id\":24,\"code\":\"100023\",\"isoCode\":\"BMU\",\"name\":\"Bermuda\",\"countryCode\":\"BM\",\"status\":\"Active\",\"dialCode\":\"+1441\",\"mobileLength\":\"7\",\"currencyCode\":\"BMD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:22.944+0530\"},{\"id\":25,\"code\":\"100024\",\"isoCode\":\"BTN\",\"name\":\"Bhutan\",\"countryCode\":\"BT\",\"status\":\"Active\",\"dialCode\":\"+975\",\"currencyCode\":\"BTN\",\"currencySymbol\":\"Nu.\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:23.222+0530\"},{\"id\":26,\"code\":\"100025\",\"isoCode\":\"BOL\",\"name\":\"Bolivia (Plurinational State of)\",\"countryCode\":\"BO\",\"status\":\"Active\",\"dialCode\":\"+591\",\"mobileLength\":\"8\",\"currencyCode\":\"BOB\",\"currencySymbol\":\"Bs.\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:23.500+0530\"},{\"id\":27,\"code\":\"100026\",\"isoCode\":\"BES\",\"name\":\"Bonaire, Sint Eustatius and Saba\",\"countryCode\":\"BQ\",\"status\":\"Active\",\"dialCode\":\"+5997\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:23.790+0530\"},{\"id\":28,\"code\":\"100027\",\"isoCode\":\"BIH\",\"name\":\"Bosnia and Herzegovina\",\"countryCode\":\"BA\",\"status\":\"Active\",\"dialCode\":\"+387\",\"mobileLength\":\"8\",\"currencyCode\":\"BAM\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:24.076+0530\"},{\"id\":29,\"code\":\"100028\",\"isoCode\":\"BWA\",\"name\":\"Botswana\",\"countryCode\":\"BW\",\"status\":\"Active\",\"dialCode\":\"+267\",\"mobileLength\":\"8\",\"currencyCode\":\"BWP\",\"currencySymbol\":\"P\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:24.362+0530\"},{\"id\":30,\"code\":\"100029\",\"isoCode\":\"BVT\",\"name\":\"Bouvet Island\",\"countryCode\":\"BV\",\"status\":\"Active\",\"currencyCode\":\"NOK\",\"currencySymbol\":\"kr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:24.652+0530\"},{\"id\":31,\"code\":\"100030\",\"isoCode\":\"BRA\",\"name\":\"Brazil\",\"countryCode\":\"BR\",\"status\":\"Active\",\"dialCode\":\"+55\",\"mobileLength\":\"11\",\"currencyCode\":\"BRL\",\"currencySymbol\":\"R$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:24.947+0530\"},{\"id\":32,\"code\":\"100031\",\"isoCode\":\"IOT\",\"name\":\"British Indian Ocean Territory\",\"countryCode\":\"IO\",\"status\":\"Active\",\"dialCode\":\"+246\",\"mobileLength\":\"7\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:25.239+0530\"},{\"id\":33,\"code\":\"100032\",\"isoCode\":\"BRN\",\"name\":\"Brunei Darussalam\",\"countryCode\":\"BN\",\"status\":\"Active\",\"dialCode\":\"+673\",\"currencyCode\":\"BND\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:25.525+0530\"},{\"id\":34,\"code\":\"100033\",\"isoCode\":\"BGR\",\"name\":\"Bulgaria\",\"countryCode\":\"BG\",\"status\":\"Active\",\"dialCode\":\"+359\",\"currencyCode\":\"BGN\",\"currencySymbol\":\"лв\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:25.807+0530\"},{\"id\":35,\"code\":\"100034\",\"isoCode\":\"BFA\",\"name\":\"Burkina Faso\",\"countryCode\":\"BF\",\"status\":\"Active\",\"dialCode\":\"+226\",\"mobileLength\":\"8\",\"currencyCode\":\"XOF\",\"currencySymbol\":\"CFA\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:26.080+0530\"},{\"id\":36,\"code\":\"100035\",\"isoCode\":\"BDI\",\"name\":\"Burundi\",\"countryCode\":\"BI\",\"status\":\"Active\",\"dialCode\":\"+257\",\"mobileLength\":\"8\",\"currencyCode\":\"BIF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:26.372+0530\"},{\"id\":37,\"code\":\"100036\",\"isoCode\":\"CPV\",\"name\":\"Cabo Verde\",\"countryCode\":\"CV\",\"status\":\"Active\",\"dialCode\":\"+238\",\"mobileLength\":\"7\",\"currencyCode\":\"CVE\",\"currencySymbol\":\"Esc\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:26.650+0530\"},{\"id\":38,\"code\":\"100037\",\"isoCode\":\"KHM\",\"name\":\"Cambodia\",\"countryCode\":\"KH\",\"status\":\"Active\",\"dialCode\":\"+855\",\"mobileLength\":\"9\",\"currencyCode\":\"KHR\",\"currencySymbol\":\"៛\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:26.940+0530\"},{\"id\":39,\"code\":\"100038\",\"isoCode\":\"CMR\",\"name\":\"Cameroon\",\"countryCode\":\"CM\",\"status\":\"Active\",\"dialCode\":\"+237\",\"mobileLength\":\"10\",\"currencyCode\":\"XAF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:27.225+0530\"},{\"id\":40,\"code\":\"100039\",\"isoCode\":\"CAN\",\"name\":\"Canada\",\"countryCode\":\"CA\",\"status\":\"Active\",\"dialCode\":\"+1\",\"mobileLength\":\"10\",\"currencyCode\":\"CAD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:27.517+0530\"},{\"id\":41,\"code\":\"100040\",\"isoCode\":\"CYM\",\"name\":\"Cayman Islands\",\"countryCode\":\"KY\",\"status\":\"Active\",\"dialCode\":\"+1345\",\"mobileLength\":\"7\",\"currencyCode\":\"KYD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:27.817+0530\"},{\"id\":42,\"code\":\"100041\",\"isoCode\":\"CAF\",\"name\":\"Central African Republic\",\"countryCode\":\"CF\",\"status\":\"Active\",\"dialCode\":\"+236\",\"currencyCode\":\"XAF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:28.100+0530\"},{\"id\":43,\"code\":\"100042\",\"isoCode\":\"TCD\",\"name\":\"Chad\",\"countryCode\":\"TD\",\"status\":\"Active\",\"dialCode\":\"+235\",\"mobileLength\":\"8\",\"currencyCode\":\"XAF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:28.391+0530\"},{\"id\":44,\"code\":\"100043\",\"isoCode\":\"CHL\",\"name\":\"Chile\",\"countryCode\":\"CL\",\"status\":\"Active\",\"dialCode\":\"+56\",\"mobileLength\":\"9\",\"currencyCode\":\"CLP\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:28.682+0530\"},{\"id\":45,\"code\":\"100044\",\"isoCode\":\"CHN\",\"name\":\"China\",\"countryCode\":\"CN\",\"status\":\"Active\",\"dialCode\":\"+86\",\"mobileLength\":\"11\",\"currencyCode\":\"CNY\",\"currencySymbol\":\"¥\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:28.957+0530\"},{\"id\":46,\"code\":\"100045\",\"isoCode\":\"CXR\",\"name\":\"Christmas Island\",\"countryCode\":\"CX\",\"status\":\"Active\",\"dialCode\":\"+61\",\"currencyCode\":\"AUD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:29.230+0530\"},{\"id\":47,\"code\":\"100046\",\"isoCode\":\"CCK\",\"name\":\"Cocos (Keeling) Islands\",\"countryCode\":\"CC\",\"status\":\"Active\",\"dialCode\":\"+61\",\"currencyCode\":\"AUD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:29.508+0530\"},{\"id\":48,\"code\":\"100047\",\"isoCode\":\"COL\",\"name\":\"Colombia\",\"countryCode\":\"CO\",\"status\":\"Active\",\"dialCode\":\"+57\",\"mobileLength\":\"10\",\"currencyCode\":\"COP\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:29.779+0530\"},{\"id\":49,\"code\":\"100048\",\"isoCode\":\"COM\",\"name\":\"Comoros\",\"countryCode\":\"KM\",\"status\":\"Active\",\"dialCode\":\"+269\",\"currencyCode\":\"KMF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:30.059+0530\"},{\"id\":51,\"code\":\"100050\",\"isoCode\":\"COG\",\"name\":\"Congo\",\"countryCode\":\"CG\",\"status\":\"Active\",\"dialCode\":\"+242\",\"currencyCode\":\"XAF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:30.615+0530\"},{\"id\":50,\"code\":\"100049\",\"isoCode\":\"COD\",\"name\":\"Congo (Democratic Republic of the)\",\"countryCode\":\"CD\",\"status\":\"Active\",\"dialCode\":\"+243\",\"mobileLength\":\"9\",\"currencyCode\":\"CDF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:30.330+0530\"},{\"id\":52,\"code\":\"100051\",\"isoCode\":\"COK\",\"name\":\"Cook Islands\",\"countryCode\":\"CK\",\"status\":\"Active\",\"dialCode\":\"+682\",\"mobileLength\":\"5\",\"currencyCode\":\"NZD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:30.905+0530\"},{\"id\":53,\"code\":\"100052\",\"isoCode\":\"CRI\",\"name\":\"Costa Rica\",\"countryCode\":\"CR\",\"status\":\"Active\",\"dialCode\":\"+506\",\"mobileLength\":\"8\",\"currencyCode\":\"CRC\",\"currencySymbol\":\"₡\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:31.181+0530\"},{\"id\":59,\"code\":\"100058\",\"isoCode\":\"CIV\",\"name\":\"Côte d'Ivoire\",\"countryCode\":\"CI\",\"status\":\"Active\",\"dialCode\":\"+225\",\"mobileLength\":\"8\",\"currencyCode\":\"XOF\",\"currencySymbol\":\"CFA\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:32.879+0530\"},{\"id\":54,\"code\":\"100053\",\"isoCode\":\"HRV\",\"name\":\"Croatia\",\"countryCode\":\"HR\",\"status\":\"Active\",\"dialCode\":\"+385\",\"mobileLength\":\"9\",\"currencyCode\":\"HRK\",\"currencySymbol\":\"kn\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:31.463+0530\"},{\"id\":55,\"code\":\"100054\",\"isoCode\":\"CUB\",\"name\":\"Cuba\",\"countryCode\":\"CU\",\"status\":\"Active\",\"dialCode\":\"+53\",\"currencyCode\":\"CUC\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:31.742+0530\"},{\"id\":56,\"code\":\"100055\",\"isoCode\":\"CUW\",\"name\":\"Curaçao\",\"countryCode\":\"CW\",\"status\":\"Active\",\"dialCode\":\"+599\",\"currencyCode\":\"ANG\",\"currencySymbol\":\"ƒ\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:32.032+0530\"},{\"id\":57,\"code\":\"100056\",\"isoCode\":\"CYP\",\"name\":\"Cyprus\",\"countryCode\":\"CY\",\"status\":\"Active\",\"dialCode\":\"+357\",\"mobileLength\":\"8\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:32.311+0530\"},{\"id\":58,\"code\":\"100057\",\"isoCode\":\"CZE\",\"name\":\"Czech Republic\",\"countryCode\":\"CZ\",\"status\":\"Active\",\"dialCode\":\"+420\",\"mobileLength\":\"9\",\"currencyCode\":\"CZK\",\"currencySymbol\":\"Kč\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:32.597+0530\"},{\"id\":60,\"code\":\"100059\",\"isoCode\":\"DNK\",\"name\":\"Denmark\",\"countryCode\":\"DK\",\"status\":\"Active\",\"dialCode\":\"+45\",\"mobileLength\":\"8\",\"currencyCode\":\"DKK\",\"currencySymbol\":\"kr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:33.160+0530\"},{\"id\":61,\"code\":\"100060\",\"isoCode\":\"DJI\",\"name\":\"Djibouti\",\"countryCode\":\"DJ\",\"status\":\"Active\",\"dialCode\":\"+253\",\"currencyCode\":\"DJF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:33.451+0530\"},{\"id\":62,\"code\":\"100061\",\"isoCode\":\"DMA\",\"name\":\"Dominica\",\"countryCode\":\"DM\",\"status\":\"Active\",\"dialCode\":\"+1767\",\"mobileLength\":\"7\",\"currencyCode\":\"XCD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:33.727+0530\"},{\"id\":63,\"code\":\"100062\",\"isoCode\":\"DOM\",\"name\":\"Dominican Republic\",\"countryCode\":\"DO\",\"status\":\"Active\",\"dialCode\":\"+1809\",\"mobileLength\":\"7\",\"currencyCode\":\"DOP\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:34.011+0530\"},{\"id\":64,\"code\":\"100063\",\"isoCode\":\"ECU\",\"name\":\"Ecuador\",\"countryCode\":\"EC\",\"status\":\"Active\",\"dialCode\":\"+593\",\"mobileLength\":\"9\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:34.291+0530\"},{\"id\":65,\"code\":\"100064\",\"isoCode\":\"EGY\",\"name\":\"Egypt\",\"countryCode\":\"EG\",\"status\":\"Active\",\"dialCode\":\"+20\",\"mobileLength\":\"10\",\"currencyCode\":\"EGP\",\"currencySymbol\":\"£\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:34.571+0530\"},{\"id\":66,\"code\":\"100065\",\"isoCode\":\"SLV\",\"name\":\"El Salvador\",\"countryCode\":\"SV\",\"status\":\"Active\",\"dialCode\":\"+503\",\"mobileLength\":\"8\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:34.851+0530\"},{\"id\":67,\"code\":\"100066\",\"isoCode\":\"GNQ\",\"name\":\"Equatorial Guinea\",\"countryCode\":\"GQ\",\"status\":\"Active\",\"dialCode\":\"+240\",\"currencyCode\":\"XAF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:35.123+0530\"},{\"id\":68,\"code\":\"100067\",\"isoCode\":\"ERI\",\"name\":\"Eritrea\",\"countryCode\":\"ER\",\"status\":\"Active\",\"dialCode\":\"+291\",\"currencyCode\":\"ERN\",\"currencySymbol\":\"Nfk\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:35.396+0530\"},{\"id\":69,\"code\":\"100068\",\"isoCode\":\"EST\",\"name\":\"Estonia\",\"countryCode\":\"EE\",\"status\":\"Active\",\"dialCode\":\"+372\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:35.691+0530\"},{\"id\":71,\"code\":\"100070\",\"isoCode\":\"ETH\",\"name\":\"Ethiopia\",\"countryCode\":\"ET\",\"status\":\"Active\",\"dialCode\":\"+251\",\"currencyCode\":\"ETB\",\"currencySymbol\":\"Br\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:36.248+0530\"},{\"id\":72,\"code\":\"100071\",\"isoCode\":\"FLK\",\"name\":\"Falkland Islands (Malvinas)\",\"countryCode\":\"FK\",\"status\":\"Active\",\"dialCode\":\"+500\",\"mobileLength\":\"5\",\"currencyCode\":\"FKP\",\"currencySymbol\":\"£\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:36.543+0530\"},{\"id\":73,\"code\":\"100072\",\"isoCode\":\"FRO\",\"name\":\"Faroe Islands\",\"countryCode\":\"FO\",\"status\":\"Active\",\"dialCode\":\"+298\",\"mobileLength\":\"5\",\"currencyCode\":\"DKK\",\"currencySymbol\":\"kr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:36.822+0530\"},{\"id\":74,\"code\":\"100073\",\"isoCode\":\"FJI\",\"name\":\"Fiji\",\"countryCode\":\"FJ\",\"status\":\"Active\",\"dialCode\":\"+679\",\"mobileLength\":\"7\",\"currencyCode\":\"FJD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:37.105+0530\"},{\"id\":75,\"code\":\"100074\",\"isoCode\":\"FIN\",\"name\":\"Finland\",\"countryCode\":\"FI\",\"status\":\"Active\",\"dialCode\":\"+358\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:37.386+0530\"},{\"id\":76,\"code\":\"100075\",\"isoCode\":\"FRA\",\"name\":\"France\",\"countryCode\":\"FR\",\"status\":\"Active\",\"dialCode\":\"+33\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:37.665+0530\"},{\"id\":77,\"code\":\"100076\",\"isoCode\":\"GUF\",\"name\":\"French Guiana\",\"countryCode\":\"GF\",\"status\":\"Active\",\"dialCode\":\"+594\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:37.946+0530\"},{\"id\":78,\"code\":\"100077\",\"isoCode\":\"PYF\",\"name\":\"French Polynesia\",\"countryCode\":\"PF\",\"status\":\"Active\",\"dialCode\":\"+689\",\"currencyCode\":\"XPF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:38.226+0530\"},{\"id\":79,\"code\":\"100078\",\"isoCode\":\"ATF\",\"name\":\"French Southern Territories\",\"countryCode\":\"TF\",\"status\":\"Active\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:38.499+0530\"},{\"id\":80,\"code\":\"100079\",\"isoCode\":\"GAB\",\"name\":\"Gabon\",\"countryCode\":\"GA\",\"status\":\"Active\",\"dialCode\":\"+241\",\"currencyCode\":\"XAF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:38.771+0530\"},{\"id\":81,\"code\":\"100080\",\"isoCode\":\"GMB\",\"name\":\"Gambia\",\"countryCode\":\"GM\",\"status\":\"Active\",\"dialCode\":\"+220\",\"mobileLength\":\"7\",\"currencyCode\":\"GMD\",\"currencySymbol\":\"D\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:39.055+0530\"},{\"id\":82,\"code\":\"100081\",\"isoCode\":\"GEO\",\"name\":\"Georgia\",\"countryCode\":\"GE\",\"status\":\"Active\",\"dialCode\":\"+995\",\"currencyCode\":\"GEL\",\"currencySymbol\":\"ლ\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:39.349+0530\"},{\"id\":83,\"code\":\"100082\",\"isoCode\":\"DEU\",\"name\":\"Germany\",\"countryCode\":\"DE\",\"status\":\"Active\",\"dialCode\":\"+49\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:39.623+0530\"},{\"id\":84,\"code\":\"100083\",\"isoCode\":\"GHA\",\"name\":\"Ghana\",\"countryCode\":\"GH\",\"status\":\"Active\",\"dialCode\":\"+233\",\"mobileLength\":\"9\",\"currencyCode\":\"GHS\",\"currencySymbol\":\"₵\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:39.907+0530\"},{\"id\":85,\"code\":\"100084\",\"isoCode\":\"GIB\",\"name\":\"Gibraltar\",\"countryCode\":\"GI\",\"status\":\"Active\",\"dialCode\":\"+350\",\"currencyCode\":\"GIP\",\"currencySymbol\":\"£\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:40.187+0530\"},{\"id\":86,\"code\":\"100085\",\"isoCode\":\"GRC\",\"name\":\"Greece\",\"countryCode\":\"GR\",\"status\":\"Active\",\"dialCode\":\"+30\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:40.467+0530\"},{\"id\":87,\"code\":\"100086\",\"isoCode\":\"GRL\",\"name\":\"Greenland\",\"countryCode\":\"GL\",\"status\":\"Active\",\"dialCode\":\"+299\",\"currencyCode\":\"DKK\",\"currencySymbol\":\"kr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:40.763+0530\"},{\"id\":88,\"code\":\"100087\",\"isoCode\":\"GRD\",\"name\":\"Grenada\",\"countryCode\":\"GD\",\"status\":\"Active\",\"dialCode\":\"+1473\",\"mobileLength\":\"7\",\"currencyCode\":\"XCD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:41.044+0530\"},{\"id\":89,\"code\":\"100088\",\"isoCode\":\"GLP\",\"name\":\"Guadeloupe\",\"countryCode\":\"GP\",\"status\":\"Active\",\"dialCode\":\"+590\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:41.323+0530\"},{\"id\":90,\"code\":\"100089\",\"isoCode\":\"GUM\",\"name\":\"Guam\",\"countryCode\":\"GU\",\"status\":\"Active\",\"dialCode\":\"+1671\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:41.597+0530\"},{\"id\":91,\"code\":\"100090\",\"isoCode\":\"GTM\",\"name\":\"Guatemala\",\"countryCode\":\"GT\",\"status\":\"Active\",\"dialCode\":\"+502\",\"mobileLength\":\"8\",\"currencyCode\":\"GTQ\",\"currencySymbol\":\"Q\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:41.904+0530\"},{\"id\":92,\"code\":\"100091\",\"isoCode\":\"GGY\",\"name\":\"Guernsey\",\"countryCode\":\"GG\",\"status\":\"Active\",\"dialCode\":\"+44\",\"currencyCode\":\"GBP\",\"currencySymbol\":\"£\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:42.179+0530\"},{\"id\":93,\"code\":\"100092\",\"isoCode\":\"GIN\",\"name\":\"Guinea\",\"countryCode\":\"GN\",\"status\":\"Active\",\"dialCode\":\"+224\",\"mobileLength\":\"9\",\"currencyCode\":\"GNF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":true,\"creationDate\":\"2020-08-11T15:04:42.459+0530\",\"nameFr\":\"Guinée\"},{\"id\":94,\"code\":\"100093\",\"isoCode\":\"GNB\",\"name\":\"Guinea-Bissau\",\"countryCode\":\"GW\",\"status\":\"Active\",\"dialCode\":\"+245\",\"mobileLength\":\"9\",\"currencyCode\":\"XOF\",\"currencySymbol\":\"CFA\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:42.729+0530\"},{\"id\":95,\"code\":\"100094\",\"isoCode\":\"GUY\",\"name\":\"Guyana\",\"countryCode\":\"GY\",\"status\":\"Active\",\"dialCode\":\"+592\",\"mobileLength\":\"7\",\"currencyCode\":\"GYD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:43.033+0530\"},{\"id\":96,\"code\":\"100095\",\"isoCode\":\"HTI\",\"name\":\"Haiti\",\"countryCode\":\"HT\",\"status\":\"Active\",\"dialCode\":\"+509\",\"mobileLength\":\"8\",\"currencyCode\":\"HTG\",\"currencySymbol\":\"G\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:43.316+0530\"},{\"id\":97,\"code\":\"100096\",\"isoCode\":\"HMD\",\"name\":\"Heard Island and McDonald Islands\",\"countryCode\":\"HM\",\"status\":\"Active\",\"currencyCode\":\"AUD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:43.598+0530\"},{\"id\":98,\"code\":\"100097\",\"isoCode\":\"VAT\",\"name\":\"Holy See\",\"countryCode\":\"VA\",\"status\":\"Active\",\"dialCode\":\"+379\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:43.879+0530\"},{\"id\":99,\"code\":\"100098\",\"isoCode\":\"HND\",\"name\":\"Honduras\",\"countryCode\":\"HN\",\"status\":\"Active\",\"dialCode\":\"+504\",\"mobileLength\":\"8\",\"currencyCode\":\"HNL\",\"currencySymbol\":\"L\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:44.160+0530\"},{\"id\":100,\"code\":\"100099\",\"isoCode\":\"HKG\",\"name\":\"Hong Kong\",\"countryCode\":\"HK\",\"status\":\"Active\",\"dialCode\":\"+852\",\"currencyCode\":\"HKD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:44.442+0530\"},{\"id\":101,\"code\":\"100100\",\"isoCode\":\"HUN\",\"name\":\"Hungary\",\"countryCode\":\"HU\",\"status\":\"Active\",\"dialCode\":\"+36\",\"currencyCode\":\"HUF\",\"currencySymbol\":\"Ft\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:44.714+0530\"},{\"id\":102,\"code\":\"100101\",\"isoCode\":\"ISL\",\"name\":\"Iceland\",\"countryCode\":\"IS\",\"status\":\"Active\",\"dialCode\":\"+354\",\"currencyCode\":\"ISK\",\"currencySymbol\":\"kr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:44.988+0530\"},{\"id\":103,\"code\":\"100102\",\"isoCode\":\"IND\",\"name\":\"India\",\"countryCode\":\"IN\",\"status\":\"Active\",\"dialCode\":\"+91\",\"mobileLength\":\"10\",\"currencyCode\":\"INR\",\"currencySymbol\":\"₹\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:45.281+0530\",\"nameFr\":\"Inde\"},{\"id\":104,\"code\":\"100103\",\"isoCode\":\"IDN\",\"name\":\"Indonesia\",\"countryCode\":\"ID\",\"status\":\"Active\",\"dialCode\":\"+62\",\"mobileLength\":\"9\",\"currencyCode\":\"IDR\",\"currencySymbol\":\"Rp\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:45.630+0530\"},{\"id\":105,\"code\":\"100104\",\"isoCode\":\"IRN\",\"name\":\"Iran (Islamic Republic of)\",\"countryCode\":\"IR\",\"status\":\"Active\",\"dialCode\":\"+98\",\"currencyCode\":\"IRR\",\"currencySymbol\":\"﷼\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:45.918+0530\"},{\"id\":106,\"code\":\"100105\",\"isoCode\":\"IRQ\",\"name\":\"Iraq\",\"countryCode\":\"IQ\",\"status\":\"Active\",\"dialCode\":\"+964\",\"mobileLength\":\"10\",\"currencyCode\":\"IQD\",\"currencySymbol\":\"ع.د\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:46.198+0530\"},{\"id\":107,\"code\":\"100106\",\"isoCode\":\"IRL\",\"name\":\"Ireland\",\"countryCode\":\"IE\",\"status\":\"Active\",\"dialCode\":\"+353\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:46.498+0530\"},{\"id\":108,\"code\":\"100107\",\"isoCode\":\"IMN\",\"name\":\"Isle of Man\",\"countryCode\":\"IM\",\"status\":\"Active\",\"dialCode\":\"+44\",\"currencyCode\":\"GBP\",\"currencySymbol\":\"£\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:46.790+0530\"},{\"id\":109,\"code\":\"100108\",\"isoCode\":\"ISR\",\"name\":\"Israel\",\"countryCode\":\"IL\",\"status\":\"Active\",\"dialCode\":\"+972\",\"mobileLength\":\"9\",\"currencyCode\":\"ILS\",\"currencySymbol\":\"₪\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:47.087+0530\"},{\"id\":110,\"code\":\"100109\",\"isoCode\":\"ITA\",\"name\":\"Italy\",\"countryCode\":\"IT\",\"status\":\"Active\",\"dialCode\":\"+39\",\"mobileLength\":\"10\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:47.381+0530\"},{\"id\":111,\"code\":\"100110\",\"isoCode\":\"JAM\",\"name\":\"Jamaica\",\"countryCode\":\"JM\",\"status\":\"Active\",\"dialCode\":\"+1876\",\"mobileLength\":\"7\",\"currencyCode\":\"JMD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:47.666+0530\"},{\"id\":112,\"code\":\"100111\",\"isoCode\":\"JPN\",\"name\":\"Japan\",\"countryCode\":\"JP\",\"status\":\"Active\",\"dialCode\":\"+81\",\"currencyCode\":\"JPY\",\"currencySymbol\":\"¥\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:47.938+0530\"},{\"id\":113,\"code\":\"100112\",\"isoCode\":\"JEY\",\"name\":\"Jersey\",\"countryCode\":\"JE\",\"status\":\"Active\",\"dialCode\":\"+44\",\"currencyCode\":\"GBP\",\"currencySymbol\":\"£\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:48.218+0530\"},{\"id\":114,\"code\":\"100113\",\"isoCode\":\"JOR\",\"name\":\"Jordan\",\"countryCode\":\"JO\",\"status\":\"Active\",\"dialCode\":\"+962\",\"mobileLength\":\"9\",\"currencyCode\":\"JOD\",\"currencySymbol\":\"د.ا\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:48.489+0530\"},{\"id\":115,\"code\":\"100114\",\"isoCode\":\"KAZ\",\"name\":\"Kazakhstan\",\"countryCode\":\"KZ\",\"status\":\"Active\",\"dialCode\":\"+76\",\"mobileLength\":\"10\",\"currencyCode\":\"KZT\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:48.779+0530\"},{\"id\":116,\"code\":\"100115\",\"isoCode\":\"KEN\",\"name\":\"Kenya\",\"countryCode\":\"KE\",\"status\":\"Active\",\"dialCode\":\"+254\",\"mobileLength\":\"9\",\"currencyCode\":\"KES\",\"currencySymbol\":\"Sh\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:49.060+0530\"},{\"id\":117,\"code\":\"100116\",\"isoCode\":\"KIR\",\"name\":\"Kiribati\",\"countryCode\":\"KI\",\"status\":\"Active\",\"dialCode\":\"+686\",\"currencyCode\":\"AUD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:49.340+0530\"},{\"id\":118,\"code\":\"100117\",\"isoCode\":\"PRK\",\"name\":\"Korea (Democratic People's Republic of)\",\"countryCode\":\"KP\",\"status\":\"Active\",\"dialCode\":\"+850\",\"currencyCode\":\"KPW\",\"currencySymbol\":\"₩\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:49.630+0530\"},{\"id\":119,\"code\":\"100118\",\"isoCode\":\"KOR\",\"name\":\"Korea (Republic of)\",\"countryCode\":\"KR\",\"status\":\"Active\",\"dialCode\":\"+82\",\"currencyCode\":\"KRW\",\"currencySymbol\":\"₩\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:49.916+0530\"},{\"id\":120,\"code\":\"100119\",\"isoCode\":\"KWT\",\"name\":\"Kuwait\",\"countryCode\":\"KW\",\"status\":\"Active\",\"dialCode\":\"+965\",\"mobileLength\":\"8\",\"currencyCode\":\"KWD\",\"currencySymbol\":\"د.ك\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:50.198+0530\"},{\"id\":121,\"code\":\"100120\",\"isoCode\":\"KGZ\",\"name\":\"Kyrgyzstan\",\"countryCode\":\"KG\",\"status\":\"Active\",\"dialCode\":\"+996\",\"mobileLength\":\"9\",\"currencyCode\":\"KGS\",\"currencySymbol\":\"с\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:50.485+0530\"},{\"id\":122,\"code\":\"100121\",\"isoCode\":\"LAO\",\"name\":\"Lao People's Democratic Republic\",\"countryCode\":\"LA\",\"status\":\"Active\",\"dialCode\":\"+856\",\"mobileLength\":\"10\",\"currencyCode\":\"LAK\",\"currencySymbol\":\"₭\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:50.767+0530\"},{\"id\":123,\"code\":\"100122\",\"isoCode\":\"LVA\",\"name\":\"Latvia\",\"countryCode\":\"LV\",\"status\":\"Active\",\"dialCode\":\"+371\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:51.057+0530\"},{\"id\":124,\"code\":\"100123\",\"isoCode\":\"LBN\",\"name\":\"Lebanon\",\"countryCode\":\"LB\",\"status\":\"Active\",\"dialCode\":\"+961\",\"currencyCode\":\"LBP\",\"currencySymbol\":\"ل.ل\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:51.344+0530\"},{\"id\":125,\"code\":\"100124\",\"isoCode\":\"LSO\",\"name\":\"Lesotho\",\"countryCode\":\"LS\",\"status\":\"Active\",\"dialCode\":\"+266\",\"currencyCode\":\"LSL\",\"currencySymbol\":\"L\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:51.616+0530\"},{\"id\":126,\"code\":\"100125\",\"isoCode\":\"LBR\",\"name\":\"Liberia\",\"countryCode\":\"LR\",\"status\":\"Active\",\"dialCode\":\"+231\",\"mobileLength\":\"9\",\"currencyCode\":\"LRD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:51.890+0530\"},{\"id\":127,\"code\":\"100126\",\"isoCode\":\"LBY\",\"name\":\"Libya\",\"countryCode\":\"LY\",\"status\":\"Active\",\"dialCode\":\"+218\",\"currencyCode\":\"LYD\",\"currencySymbol\":\"ل.د\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:52.167+0530\"},{\"id\":128,\"code\":\"100127\",\"isoCode\":\"LIE\",\"name\":\"Liechtenstein\",\"countryCode\":\"LI\",\"status\":\"Active\",\"dialCode\":\"+423\",\"currencyCode\":\"CHF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:52.445+0530\"},{\"id\":129,\"code\":\"100128\",\"isoCode\":\"LTU\",\"name\":\"Lithuania\",\"countryCode\":\"LT\",\"status\":\"Active\",\"dialCode\":\"+370\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:52.726+0530\"},{\"id\":130,\"code\":\"100129\",\"isoCode\":\"LUX\",\"name\":\"Luxembourg\",\"countryCode\":\"LU\",\"status\":\"Active\",\"dialCode\":\"+352\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:53.004+0530\"},{\"id\":131,\"code\":\"100130\",\"isoCode\":\"MAC\",\"name\":\"Macao\",\"countryCode\":\"MO\",\"status\":\"Active\",\"dialCode\":\"+853\",\"currencyCode\":\"MOP\",\"currencySymbol\":\"P\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:53.286+0530\"},{\"id\":180,\"code\":\"100179\",\"isoCode\":\"MKD\",\"name\":\"Macedonia (the former Yugoslav Republic of)\",\"countryCode\":\"MK\",\"status\":\"Active\",\"dialCode\":\"+389\",\"currencyCode\":\"MKD\",\"currencySymbol\":\"ден\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:07.168+0530\"},{\"id\":132,\"code\":\"100131\",\"isoCode\":\"MDG\",\"name\":\"Madagascar\",\"countryCode\":\"MG\",\"status\":\"Active\",\"dialCode\":\"+261\",\"mobileLength\":\"9\",\"currencyCode\":\"MGA\",\"currencySymbol\":\"Ar\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:53.567+0530\"},{\"id\":133,\"code\":\"100132\",\"isoCode\":\"MWI\",\"name\":\"Malawi\",\"countryCode\":\"MW\",\"status\":\"Active\",\"dialCode\":\"+265\",\"currencyCode\":\"MWK\",\"currencySymbol\":\"MK\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:53.838+0530\"},{\"id\":134,\"code\":\"100133\",\"isoCode\":\"MYS\",\"name\":\"Malaysia\",\"countryCode\":\"MY\",\"status\":\"Active\",\"dialCode\":\"+60\",\"mobileLength\":\"11\",\"currencyCode\":\"MYR\",\"currencySymbol\":\"RM\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:54.129+0530\"},{\"id\":135,\"code\":\"100134\",\"isoCode\":\"MDV\",\"name\":\"Maldives\",\"countryCode\":\"MV\",\"status\":\"Active\",\"dialCode\":\"+960\",\"currencyCode\":\"MVR\",\"currencySymbol\":\".ރ\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:54.428+0530\"},{\"id\":136,\"code\":\"100135\",\"isoCode\":\"MLI\",\"name\":\"Mali\",\"countryCode\":\"ML\",\"status\":\"Active\",\"dialCode\":\"+223\",\"mobileLength\":\"8\",\"currencyCode\":\"XOF\",\"currencySymbol\":\"CFA\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:54.701+0530\"},{\"id\":137,\"code\":\"100136\",\"isoCode\":\"MLT\",\"name\":\"Malta\",\"countryCode\":\"MT\",\"status\":\"Active\",\"dialCode\":\"+356\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:54.975+0530\"},{\"id\":138,\"code\":\"100137\",\"isoCode\":\"MHL\",\"name\":\"Marshall Islands\",\"countryCode\":\"MH\",\"status\":\"Active\",\"dialCode\":\"+692\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:55.256+0530\"},{\"id\":139,\"code\":\"100138\",\"isoCode\":\"MTQ\",\"name\":\"Martinique\",\"countryCode\":\"MQ\",\"status\":\"Active\",\"dialCode\":\"+596\",\"mobileLength\":\"9\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:55.550+0530\"},{\"id\":140,\"code\":\"100139\",\"isoCode\":\"MRT\",\"name\":\"Mauritania\",\"countryCode\":\"MR\",\"status\":\"Active\",\"dialCode\":\"+222\",\"currencyCode\":\"MRO\",\"currencySymbol\":\"UM\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:55.832+0530\"},{\"id\":141,\"code\":\"100140\",\"isoCode\":\"MUS\",\"name\":\"Mauritius\",\"countryCode\":\"MU\",\"status\":\"Active\",\"dialCode\":\"+230\",\"currencyCode\":\"MUR\",\"currencySymbol\":\"₨\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:56.122+0530\"},{\"id\":142,\"code\":\"100141\",\"isoCode\":\"MYT\",\"name\":\"Mayotte\",\"countryCode\":\"YT\",\"status\":\"Active\",\"dialCode\":\"+262\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:56.403+0530\"},{\"id\":143,\"code\":\"100142\",\"isoCode\":\"MEX\",\"name\":\"Mexico\",\"countryCode\":\"MX\",\"status\":\"Active\",\"dialCode\":\"+52\",\"mobileLength\":\"10\",\"currencyCode\":\"MXN\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:56.690+0530\"},{\"id\":144,\"code\":\"100143\",\"isoCode\":\"FSM\",\"name\":\"Micronesia (Federated States of)\",\"countryCode\":\"FM\",\"status\":\"Active\",\"dialCode\":\"+691\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:56.965+0530\"},{\"id\":145,\"code\":\"100144\",\"isoCode\":\"MDA\",\"name\":\"Moldova (Republic of)\",\"countryCode\":\"MD\",\"status\":\"Active\",\"dialCode\":\"+373\",\"mobileLength\":\"8\",\"currencyCode\":\"MDL\",\"currencySymbol\":\"L\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:57.252+0530\"},{\"id\":146,\"code\":\"100145\",\"isoCode\":\"MCO\",\"name\":\"Monaco\",\"countryCode\":\"MC\",\"status\":\"Active\",\"dialCode\":\"+377\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:57.534+0530\"},{\"id\":147,\"code\":\"100146\",\"isoCode\":\"MNG\",\"name\":\"Mongolia\",\"countryCode\":\"MN\",\"status\":\"Active\",\"dialCode\":\"+976\",\"currencyCode\":\"MNT\",\"currencySymbol\":\"₮\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:57.816+0530\"},{\"id\":148,\"code\":\"100147\",\"isoCode\":\"MNE\",\"name\":\"Montenegro\",\"countryCode\":\"ME\",\"status\":\"Active\",\"dialCode\":\"+382\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:58.109+0530\"},{\"id\":149,\"code\":\"100148\",\"isoCode\":\"MSR\",\"name\":\"Montserrat\",\"countryCode\":\"MS\",\"status\":\"Active\",\"dialCode\":\"+1664\",\"mobileLength\":\"7\",\"currencyCode\":\"XCD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:58.387+0530\"},{\"id\":150,\"code\":\"100149\",\"isoCode\":\"MAR\",\"name\":\"Morocco\",\"countryCode\":\"MA\",\"status\":\"Active\",\"dialCode\":\"+212\",\"mobileLength\":\"9\",\"currencyCode\":\"MAD\",\"currencySymbol\":\"د.م.\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:58.674+0530\"},{\"id\":151,\"code\":\"100150\",\"isoCode\":\"MOZ\",\"name\":\"Mozambique\",\"countryCode\":\"MZ\",\"status\":\"Active\",\"dialCode\":\"+258\",\"mobileLength\":\"9\",\"currencyCode\":\"MZN\",\"currencySymbol\":\"MT\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:58.956+0530\"},{\"id\":152,\"code\":\"100151\",\"isoCode\":\"MMR\",\"name\":\"Myanmar\",\"countryCode\":\"MM\",\"status\":\"Active\",\"dialCode\":\"+95\",\"mobileLength\":\"10\",\"currencyCode\":\"MMK\",\"currencySymbol\":\"Ks\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:59.231+0530\"},{\"id\":153,\"code\":\"100152\",\"isoCode\":\"NAM\",\"name\":\"Namibia\",\"countryCode\":\"NA\",\"status\":\"Active\",\"dialCode\":\"+264\",\"currencyCode\":\"NAD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:59.523+0530\"},{\"id\":154,\"code\":\"100153\",\"isoCode\":\"NRU\",\"name\":\"Nauru\",\"countryCode\":\"NR\",\"status\":\"Active\",\"dialCode\":\"+674\",\"mobileLength\":\"7\",\"currencyCode\":\"AUD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:59.804+0530\"},{\"id\":155,\"code\":\"100154\",\"isoCode\":\"NPL\",\"name\":\"Nepal\",\"countryCode\":\"NP\",\"status\":\"Active\",\"dialCode\":\"+977\",\"mobileLength\":\"10\",\"currencyCode\":\"NPR\",\"currencySymbol\":\"₨\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:00.085+0530\"},{\"id\":156,\"code\":\"100155\",\"isoCode\":\"NLD\",\"name\":\"Netherlands\",\"countryCode\":\"NL\",\"status\":\"Active\",\"dialCode\":\"+31\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:00.368+0530\"},{\"id\":157,\"code\":\"100156\",\"isoCode\":\"NCL\",\"name\":\"New Caledonia\",\"countryCode\":\"NC\",\"status\":\"Active\",\"dialCode\":\"+687\",\"currencyCode\":\"XPF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:00.640+0530\"},{\"id\":158,\"code\":\"100157\",\"isoCode\":\"NZL\",\"name\":\"New Zealand\",\"countryCode\":\"NZ\",\"status\":\"Active\",\"dialCode\":\"+64\",\"currencyCode\":\"NZD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:00.917+0530\"},{\"id\":159,\"code\":\"100158\",\"isoCode\":\"NIC\",\"name\":\"Nicaragua\",\"countryCode\":\"NI\",\"status\":\"Active\",\"dialCode\":\"+505\",\"mobileLength\":\"8\",\"currencyCode\":\"NIO\",\"currencySymbol\":\"C$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:01.212+0530\"},{\"id\":160,\"code\":\"100159\",\"isoCode\":\"NER\",\"name\":\"Niger\",\"countryCode\":\"NE\",\"status\":\"Active\",\"dialCode\":\"+227\",\"mobileLength\":\"8\",\"currencyCode\":\"XOF\",\"currencySymbol\":\"CFA\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:01.508+0530\"},{\"id\":161,\"code\":\"100160\",\"isoCode\":\"NGA\",\"name\":\"Nigeria\",\"countryCode\":\"NG\",\"status\":\"Active\",\"dialCode\":\"+234\",\"mobileLength\":\"10\",\"currencyCode\":\"NGN\",\"currencySymbol\":\"₦\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:01.794+0530\"},{\"id\":162,\"code\":\"100161\",\"isoCode\":\"NIU\",\"name\":\"Niue\",\"countryCode\":\"NU\",\"status\":\"Active\",\"dialCode\":\"+683\",\"currencyCode\":\"NZD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:02.072+0530\"},{\"id\":163,\"code\":\"100162\",\"isoCode\":\"NFK\",\"name\":\"Norfolk Island\",\"countryCode\":\"NF\",\"status\":\"Active\",\"dialCode\":\"+672\",\"currencyCode\":\"AUD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:02.366+0530\"},{\"id\":164,\"code\":\"100163\",\"isoCode\":\"MNP\",\"name\":\"Northern Mariana Islands\",\"countryCode\":\"MP\",\"status\":\"Active\",\"dialCode\":\"+1670\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:02.640+0530\"},{\"id\":165,\"code\":\"100164\",\"isoCode\":\"NOR\",\"name\":\"Norway\",\"countryCode\":\"NO\",\"status\":\"Active\",\"dialCode\":\"+47\",\"currencyCode\":\"NOK\",\"currencySymbol\":\"kr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:02.915+0530\"},{\"id\":166,\"code\":\"100165\",\"isoCode\":\"OMN\",\"name\":\"Oman\",\"countryCode\":\"OM\",\"status\":\"Active\",\"dialCode\":\"+968\",\"currencyCode\":\"OMR\",\"currencySymbol\":\"ر.ع.\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:03.207+0530\"},{\"id\":167,\"code\":\"100166\",\"isoCode\":\"PAK\",\"name\":\"Pakistan\",\"countryCode\":\"PK\",\"status\":\"Active\",\"dialCode\":\"+92\",\"mobileLength\":\"10\",\"currencyCode\":\"PKR\",\"currencySymbol\":\"₨\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:03.481+0530\"},{\"id\":168,\"code\":\"100167\",\"isoCode\":\"PLW\",\"name\":\"Palau\",\"countryCode\":\"PW\",\"status\":\"Active\",\"dialCode\":\"+680\",\"currencyCode\":\"(none)\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:03.763+0530\"},{\"id\":169,\"code\":\"100168\",\"isoCode\":\"PSE\",\"name\":\"Palestine, State of\",\"countryCode\":\"PS\",\"status\":\"Active\",\"dialCode\":\"+970\",\"currencyCode\":\"ILS\",\"currencySymbol\":\"₪\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:04.033+0530\"},{\"id\":170,\"code\":\"100169\",\"isoCode\":\"PAN\",\"name\":\"Panama\",\"countryCode\":\"PA\",\"status\":\"Active\",\"dialCode\":\"+507\",\"mobileLength\":\"8\",\"currencyCode\":\"PAB\",\"currencySymbol\":\"B/.\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:04.306+0530\"},{\"id\":171,\"code\":\"100170\",\"isoCode\":\"PNG\",\"name\":\"Papua New Guinea\",\"countryCode\":\"PG\",\"status\":\"Active\",\"dialCode\":\"+675\",\"mobileLength\":\"8\",\"currencyCode\":\"PGK\",\"currencySymbol\":\"K\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:04.594+0530\"},{\"id\":172,\"code\":\"100171\",\"isoCode\":\"PRY\",\"name\":\"Paraguay\",\"countryCode\":\"PY\",\"status\":\"Active\",\"dialCode\":\"+595\",\"mobileLength\":\"9\",\"currencyCode\":\"PYG\",\"currencySymbol\":\"₲\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:04.883+0530\"},{\"id\":173,\"code\":\"100172\",\"isoCode\":\"PER\",\"name\":\"Peru\",\"countryCode\":\"PE\",\"status\":\"Active\",\"dialCode\":\"+51\",\"mobileLength\":\"9\",\"currencyCode\":\"PEN\",\"currencySymbol\":\"S/.\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:05.176+0530\"},{\"id\":174,\"code\":\"100173\",\"isoCode\":\"PHL\",\"name\":\"Philippines\",\"countryCode\":\"PH\",\"status\":\"Active\",\"dialCode\":\"+63\",\"mobileLength\":\"10\",\"currencyCode\":\"PHP\",\"currencySymbol\":\"₱\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:05.488+0530\"},{\"id\":175,\"code\":\"100174\",\"isoCode\":\"PCN\",\"name\":\"Pitcairn\",\"countryCode\":\"PN\",\"status\":\"Active\",\"dialCode\":\"+64\",\"currencyCode\":\"NZD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:05.769+0530\"},{\"id\":176,\"code\":\"100175\",\"isoCode\":\"POL\",\"name\":\"Poland\",\"countryCode\":\"PL\",\"status\":\"Active\",\"dialCode\":\"+48\",\"mobileLength\":\"9\",\"currencyCode\":\"PLN\",\"currencySymbol\":\"zł\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:06.048+0530\"},{\"id\":177,\"code\":\"100176\",\"isoCode\":\"PRT\",\"name\":\"Portugal\",\"countryCode\":\"PT\",\"status\":\"Active\",\"dialCode\":\"+351\",\"mobileLength\":\"9\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:06.332+0530\"},{\"id\":178,\"code\":\"100177\",\"isoCode\":\"PRI\",\"name\":\"Puerto Rico\",\"countryCode\":\"PR\",\"status\":\"Active\",\"dialCode\":\"+1787\",\"mobileLength\":\"7\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:06.611+0530\"},{\"id\":179,\"code\":\"100178\",\"isoCode\":\"QAT\",\"name\":\"Qatar\",\"countryCode\":\"QA\",\"status\":\"Active\",\"dialCode\":\"+974\",\"currencyCode\":\"QAR\",\"currencySymbol\":\"ر.ق\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:06.891+0530\"},{\"id\":184,\"code\":\"100183\",\"isoCode\":\"REU\",\"name\":\"Réunion\",\"countryCode\":\"RE\",\"status\":\"Active\",\"dialCode\":\"+262\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:08.291+0530\"},{\"id\":181,\"code\":\"100180\",\"isoCode\":\"ROU\",\"name\":\"Romania\",\"countryCode\":\"RO\",\"status\":\"Active\",\"dialCode\":\"+40\",\"mobileLength\":\"9\",\"currencyCode\":\"RON\",\"currencySymbol\":\"lei\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:07.452+0530\"},{\"id\":182,\"code\":\"100181\",\"isoCode\":\"RUS\",\"name\":\"Russian Federation\",\"countryCode\":\"RU\",\"status\":\"Active\",\"dialCode\":\"+7\",\"mobileLength\":\"10\",\"currencyCode\":\"RUB\",\"currencySymbol\":\"₽\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:07.724+0530\"},{\"id\":183,\"code\":\"100182\",\"isoCode\":\"RWA\",\"name\":\"Rwanda\",\"countryCode\":\"RW\",\"status\":\"Active\",\"dialCode\":\"+250\",\"mobileLength\":\"9\",\"currencyCode\":\"RWF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:08.006+0530\"},{\"id\":185,\"code\":\"100184\",\"isoCode\":\"BLM\",\"name\":\"Saint Barthélemy\",\"countryCode\":\"BL\",\"status\":\"Active\",\"dialCode\":\"+590\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:08.570+0530\"},{\"id\":186,\"code\":\"100185\",\"isoCode\":\"SHN\",\"name\":\"Saint Helena, Ascension and Tristan da Cunha\",\"countryCode\":\"SH\",\"status\":\"Active\",\"dialCode\":\"+290\",\"currencyCode\":\"SHP\",\"currencySymbol\":\"£\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:08.852+0530\"},{\"id\":187,\"code\":\"100186\",\"isoCode\":\"KNA\",\"name\":\"Saint Kitts and Nevis\",\"countryCode\":\"KN\",\"status\":\"Active\",\"dialCode\":\"+1869\",\"mobileLength\":\"7\",\"currencyCode\":\"XCD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:09.150+0530\"},{\"id\":188,\"code\":\"100187\",\"isoCode\":\"LCA\",\"name\":\"Saint Lucia\",\"countryCode\":\"LC\",\"status\":\"Active\",\"dialCode\":\"+1758\",\"mobileLength\":\"7\",\"currencyCode\":\"XCD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:09.423+0530\"},{\"id\":189,\"code\":\"100188\",\"isoCode\":\"MAF\",\"name\":\"Saint Martin (French part)\",\"countryCode\":\"MF\",\"status\":\"Active\",\"dialCode\":\"+590\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:09.701+0530\"},{\"id\":190,\"code\":\"100189\",\"isoCode\":\"SPM\",\"name\":\"Saint Pierre and Miquelon\",\"countryCode\":\"PM\",\"status\":\"Active\",\"dialCode\":\"+508\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:09.977+0530\"},{\"id\":191,\"code\":\"100190\",\"isoCode\":\"VCT\",\"name\":\"Saint Vincent and the Grenadines\",\"countryCode\":\"VC\",\"status\":\"Active\",\"dialCode\":\"+1784\",\"mobileLength\":\"7\",\"currencyCode\":\"XCD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:10.254+0530\"},{\"id\":192,\"code\":\"100191\",\"isoCode\":\"WSM\",\"name\":\"Samoa\",\"countryCode\":\"WS\",\"status\":\"Active\",\"dialCode\":\"+685\",\"mobileLength\":\"7\",\"currencyCode\":\"WST\",\"currencySymbol\":\"T\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:10.539+0530\"},{\"id\":193,\"code\":\"100192\",\"isoCode\":\"SMR\",\"name\":\"San Marino\",\"countryCode\":\"SM\",\"status\":\"Active\",\"dialCode\":\"+378\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:10.823+0530\"},{\"id\":194,\"code\":\"100193\",\"isoCode\":\"STP\",\"name\":\"Sao Tome and Principe\",\"countryCode\":\"ST\",\"status\":\"Active\",\"dialCode\":\"+239\",\"currencyCode\":\"STD\",\"currencySymbol\":\"Db\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:11.108+0530\"},{\"id\":195,\"code\":\"100194\",\"isoCode\":\"SAU\",\"name\":\"Saudi Arabia\",\"countryCode\":\"SA\",\"status\":\"Active\",\"dialCode\":\"+966\",\"currencyCode\":\"SAR\",\"currencySymbol\":\"ر.س\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:11.424+0530\"},{\"id\":196,\"code\":\"100195\",\"isoCode\":\"SEN\",\"name\":\"Senegal\",\"countryCode\":\"SN\",\"status\":\"Active\",\"dialCode\":\"+221\",\"mobileLength\":\"9\",\"currencyCode\":\"XOF\",\"currencySymbol\":\"CFA\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:11.705+0530\"},{\"id\":197,\"code\":\"100196\",\"isoCode\":\"SRB\",\"name\":\"Serbia\",\"countryCode\":\"RS\",\"status\":\"Active\",\"dialCode\":\"+381\",\"currencyCode\":\"RSD\",\"currencySymbol\":\"дин.\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:11.975+0530\"},{\"id\":198,\"code\":\"100197\",\"isoCode\":\"SYC\",\"name\":\"Seychelles\",\"countryCode\":\"SC\",\"status\":\"Active\",\"dialCode\":\"+248\",\"currencyCode\":\"SCR\",\"currencySymbol\":\"₨\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:12.250+0530\"},{\"id\":199,\"code\":\"100198\",\"isoCode\":\"SLE\",\"name\":\"Sierra Leone\",\"countryCode\":\"SL\",\"status\":\"Active\",\"dialCode\":\"+232\",\"mobileLength\":\"8\",\"currencyCode\":\"SLL\",\"currencySymbol\":\"Le\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:12.526+0530\"},{\"id\":200,\"code\":\"100199\",\"isoCode\":\"SGP\",\"name\":\"Singapore\",\"countryCode\":\"SG\",\"status\":\"Active\",\"dialCode\":\"+65\",\"mobileLength\":\"8\",\"currencyCode\":\"BND\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:12.809+0530\"},{\"id\":201,\"code\":\"100200\",\"isoCode\":\"SXM\",\"name\":\"Sint Maarten (Dutch part)\",\"countryCode\":\"SX\",\"status\":\"Active\",\"dialCode\":\"+1721\",\"currencyCode\":\"ANG\",\"currencySymbol\":\"ƒ\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:13.090+0530\"},{\"id\":202,\"code\":\"100201\",\"isoCode\":\"SVK\",\"name\":\"Slovakia\",\"countryCode\":\"SK\",\"status\":\"Active\",\"dialCode\":\"+421\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:13.377+0530\"},{\"id\":203,\"code\":\"100202\",\"isoCode\":\"SVN\",\"name\":\"Slovenia\",\"countryCode\":\"SI\",\"status\":\"Active\",\"dialCode\":\"+386\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:13.657+0530\"},{\"id\":204,\"code\":\"100203\",\"isoCode\":\"SLB\",\"name\":\"Solomon Islands\",\"countryCode\":\"SB\",\"status\":\"Active\",\"dialCode\":\"+677\",\"currencyCode\":\"SBD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:13.939+0530\"},{\"id\":205,\"code\":\"100204\",\"isoCode\":\"SOM\",\"name\":\"Somalia\",\"countryCode\":\"SO\",\"status\":\"Active\",\"dialCode\":\"+252\",\"currencyCode\":\"SOS\",\"currencySymbol\":\"Sh\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:14.216+0530\"},{\"id\":206,\"code\":\"100205\",\"isoCode\":\"ZAF\",\"name\":\"South Africa\",\"countryCode\":\"ZA\",\"status\":\"Active\",\"dialCode\":\"+27\",\"mobileLength\":\"9\",\"currencyCode\":\"ZAR\",\"currencySymbol\":\"R\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:14.512+0530\"},{\"id\":207,\"code\":\"100206\",\"isoCode\":\"SGS\",\"name\":\"South Georgia and the South Sandwich Islands\",\"countryCode\":\"GS\",\"status\":\"Active\",\"dialCode\":\"+500\",\"currencyCode\":\"GBP\",\"currencySymbol\":\"£\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:14.788+0530\"},{\"id\":208,\"code\":\"100207\",\"isoCode\":\"SSD\",\"name\":\"South Sudan\",\"countryCode\":\"SS\",\"status\":\"Active\",\"dialCode\":\"+211\",\"currencyCode\":\"SSP\",\"currencySymbol\":\"£\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:15.067+0530\"},{\"id\":209,\"code\":\"100208\",\"isoCode\":\"ESP\",\"name\":\"Spain\",\"countryCode\":\"ES\",\"status\":\"Active\",\"dialCode\":\"+34\",\"mobileLength\":\"9\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:15.346+0530\"},{\"id\":210,\"code\":\"100209\",\"isoCode\":\"LKA\",\"name\":\"Sri Lanka\",\"countryCode\":\"LK\",\"status\":\"Active\",\"dialCode\":\"+94\",\"mobileLength\":\"9\",\"currencyCode\":\"LKR\",\"currencySymbol\":\"Rs\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:15.630+0530\"},{\"id\":211,\"code\":\"100210\",\"isoCode\":\"SDN\",\"name\":\"Sudan\",\"countryCode\":\"SD\",\"status\":\"Active\",\"dialCode\":\"+249\",\"currencyCode\":\"SDG\",\"currencySymbol\":\"ج.س.\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:15.899+0530\"},{\"id\":212,\"code\":\"100211\",\"isoCode\":\"SUR\",\"name\":\"Suriname\",\"countryCode\":\"SR\",\"status\":\"Active\",\"dialCode\":\"+597\",\"mobileLength\":\"7\",\"currencyCode\":\"SRD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:16.194+0530\"},{\"id\":213,\"code\":\"100212\",\"isoCode\":\"SJM\",\"name\":\"Svalbard and Jan Mayen\",\"countryCode\":\"SJ\",\"status\":\"Active\",\"dialCode\":\"+4779\",\"currencyCode\":\"NOK\",\"currencySymbol\":\"kr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:16.488+0530\"},{\"id\":70,\"code\":\"100069\",\"isoCode\":\"SWZ\",\"name\":\"Swaziland\",\"countryCode\":\"SZ\",\"status\":\"Active\",\"dialCode\":\"+268\",\"mobileLength\":\"8\",\"currencyCode\":\"SZL\",\"currencySymbol\":\"L\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:35.974+0530\"},{\"id\":214,\"code\":\"100213\",\"isoCode\":\"SWE\",\"name\":\"Sweden\",\"countryCode\":\"SE\",\"status\":\"Active\",\"dialCode\":\"+46\",\"currencyCode\":\"SEK\",\"currencySymbol\":\"kr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:16.772+0530\"},{\"id\":215,\"code\":\"100214\",\"isoCode\":\"CHE\",\"name\":\"Switzerland\",\"countryCode\":\"CH\",\"status\":\"Active\",\"dialCode\":\"+41\",\"currencyCode\":\"CHF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:17.044+0530\"},{\"id\":216,\"code\":\"100215\",\"isoCode\":\"SYR\",\"name\":\"Syrian Arab Republic\",\"countryCode\":\"SY\",\"status\":\"Active\",\"dialCode\":\"+963\",\"currencyCode\":\"SYP\",\"currencySymbol\":\"£\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:17.315+0530\"},{\"id\":217,\"code\":\"100216\",\"isoCode\":\"TWN\",\"name\":\"Taiwan\",\"countryCode\":\"TW\",\"status\":\"Active\",\"dialCode\":\"+886\",\"currencyCode\":\"TWD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:17.583+0530\"},{\"id\":218,\"code\":\"100217\",\"isoCode\":\"TJK\",\"name\":\"Tajikistan\",\"countryCode\":\"TJ\",\"status\":\"Active\",\"dialCode\":\"+992\",\"mobileLength\":\"9\",\"currencyCode\":\"TJS\",\"currencySymbol\":\"ЅМ\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:17.863+0530\"},{\"id\":219,\"code\":\"100218\",\"isoCode\":\"TZA\",\"name\":\"Tanzania, United Republic of\",\"countryCode\":\"TZ\",\"status\":\"Active\",\"dialCode\":\"+255\",\"mobileLength\":\"9\",\"currencyCode\":\"TZS\",\"currencySymbol\":\"Sh\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:18.137+0530\"},{\"id\":220,\"code\":\"100219\",\"isoCode\":\"THA\",\"name\":\"Thailand\",\"countryCode\":\"TH\",\"status\":\"Active\",\"dialCode\":\"+66\",\"mobileLength\":\"9\",\"currencyCode\":\"THB\",\"currencySymbol\":\"฿\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:18.412+0530\"},{\"id\":221,\"code\":\"100220\",\"isoCode\":\"TLS\",\"name\":\"Timor-Leste\",\"countryCode\":\"TL\",\"status\":\"Active\",\"dialCode\":\"+670\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:18.688+0530\"},{\"id\":222,\"code\":\"100221\",\"isoCode\":\"TGO\",\"name\":\"Togo\",\"countryCode\":\"TG\",\"status\":\"Active\",\"dialCode\":\"+228\",\"mobileLength\":\"8\",\"currencyCode\":\"XOF\",\"currencySymbol\":\"CFA\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:18.963+0530\"},{\"id\":223,\"code\":\"100222\",\"isoCode\":\"TKL\",\"name\":\"Tokelau\",\"countryCode\":\"TK\",\"status\":\"Active\",\"dialCode\":\"+690\",\"currencyCode\":\"NZD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:19.238+0530\"},{\"id\":224,\"code\":\"100223\",\"isoCode\":\"TON\",\"name\":\"Tonga\",\"countryCode\":\"TO\",\"status\":\"Active\",\"dialCode\":\"+676\",\"mobileLength\":\"7\",\"currencyCode\":\"TOP\",\"currencySymbol\":\"T$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:19.516+0530\"},{\"id\":225,\"code\":\"100224\",\"isoCode\":\"TTO\",\"name\":\"Trinidad and Tobago\",\"countryCode\":\"TT\",\"status\":\"Active\",\"dialCode\":\"+1868\",\"mobileLength\":\"7\",\"currencyCode\":\"TTD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:19.799+0530\"},{\"id\":226,\"code\":\"100225\",\"isoCode\":\"TUN\",\"name\":\"Tunisia\",\"countryCode\":\"TN\",\"status\":\"Active\",\"dialCode\":\"+216\",\"mobileLength\":\"8\",\"currencyCode\":\"TND\",\"currencySymbol\":\"د.ت\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:20.089+0530\"},{\"id\":227,\"code\":\"100226\",\"isoCode\":\"TUR\",\"name\":\"Turkey\",\"countryCode\":\"TR\",\"status\":\"Active\",\"dialCode\":\"+90\",\"mobileLength\":\"10\",\"currencyCode\":\"TRY\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:20.430+0530\"},{\"id\":228,\"code\":\"100227\",\"isoCode\":\"TKM\",\"name\":\"Turkmenistan\",\"countryCode\":\"TM\",\"status\":\"Active\",\"dialCode\":\"+993\",\"currencyCode\":\"TMT\",\"currencySymbol\":\"m\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:20.705+0530\"},{\"id\":229,\"code\":\"100228\",\"isoCode\":\"TCA\",\"name\":\"Turks and Caicos Islands\",\"countryCode\":\"TC\",\"status\":\"Active\",\"dialCode\":\"+1649\",\"mobileLength\":\"7\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:20.991+0530\"},{\"id\":230,\"code\":\"100229\",\"isoCode\":\"TUV\",\"name\":\"Tuvalu\",\"countryCode\":\"TV\",\"status\":\"Active\",\"dialCode\":\"+688\",\"currencyCode\":\"AUD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:21.269+0530\"},{\"id\":231,\"code\":\"100230\",\"isoCode\":\"UGA\",\"name\":\"Uganda\",\"countryCode\":\"UG\",\"status\":\"Active\",\"dialCode\":\"+256\",\"mobileLength\":\"9\",\"currencyCode\":\"UGX\",\"currencySymbol\":\"Sh\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:21.557+0530\"},{\"id\":232,\"code\":\"100231\",\"isoCode\":\"UKR\",\"name\":\"Ukraine\",\"countryCode\":\"UA\",\"status\":\"Active\",\"dialCode\":\"+380\",\"mobileLength\":\"9\",\"currencyCode\":\"UAH\",\"currencySymbol\":\"₴\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:21.835+0530\"},{\"id\":233,\"code\":\"100232\",\"isoCode\":\"ARE\",\"name\":\"United Arab Emirates\",\"countryCode\":\"AE\",\"status\":\"Active\",\"dialCode\":\"+971\",\"mobileLength\":\"9\",\"currencyCode\":\"AED\",\"currencySymbol\":\"د.إ\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:22.109+0530\"},{\"id\":234,\"code\":\"100233\",\"isoCode\":\"GBR\",\"name\":\"United Kingdom of Great Britain and Northern Ireland\",\"countryCode\":\"GB\",\"status\":\"Active\",\"dialCode\":\"+44\",\"currencyCode\":\"GBP\",\"currencySymbol\":\"£\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:22.388+0530\"},{\"id\":235,\"code\":\"100234\",\"isoCode\":\"UMI\",\"name\":\"United States Minor Outlying Islands\",\"countryCode\":\"UM\",\"status\":\"Active\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:22.675+0530\"},{\"id\":236,\"code\":\"100235\",\"isoCode\":\"USA\",\"name\":\"United States of America\",\"countryCode\":\"US\",\"status\":\"Active\",\"dialCode\":\"+1\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:22.957+0530\"},{\"id\":237,\"code\":\"100236\",\"isoCode\":\"URY\",\"name\":\"Uruguay\",\"countryCode\":\"UY\",\"status\":\"Active\",\"dialCode\":\"+598\",\"currencyCode\":\"UYU\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:23.234+0530\"},{\"id\":238,\"code\":\"100237\",\"isoCode\":\"UZB\",\"name\":\"Uzbekistan\",\"countryCode\":\"UZ\",\"status\":\"Active\",\"dialCode\":\"+998\",\"mobileLength\":\"9\",\"currencyCode\":\"UZS\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:23.515+0530\"},{\"id\":239,\"code\":\"100238\",\"isoCode\":\"VUT\",\"name\":\"Vanuatu\",\"countryCode\":\"VU\",\"status\":\"Active\",\"dialCode\":\"+678\",\"mobileLength\":\"7\",\"currencyCode\":\"VUV\",\"currencySymbol\":\"Vt\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:23.801+0530\"},{\"id\":240,\"code\":\"100239\",\"isoCode\":\"VEN\",\"name\":\"Venezuela (Bolivarian Republic of)\",\"countryCode\":\"VE\",\"status\":\"Active\",\"dialCode\":\"+58\",\"mobileLength\":\"10\",\"currencyCode\":\"VEF\",\"currencySymbol\":\"Bs F\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:24.080+0530\"},{\"id\":241,\"code\":\"100240\",\"isoCode\":\"VNM\",\"name\":\"Viet Nam\",\"countryCode\":\"VN\",\"status\":\"Active\",\"dialCode\":\"+84\",\"mobileLength\":\"10\",\"currencyCode\":\"VND\",\"currencySymbol\":\"₫\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:24.358+0530\"},{\"id\":242,\"code\":\"100241\",\"isoCode\":\"VGB\",\"name\":\"Virgin Islands (British)\",\"countryCode\":\"VG\",\"status\":\"Active\",\"dialCode\":\"+1284\",\"mobileLength\":\"7\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:24.640+0530\"},{\"id\":243,\"code\":\"100242\",\"isoCode\":\"VIR\",\"name\":\"Virgin Islands (U.S.)\",\"countryCode\":\"VI\",\"status\":\"Active\",\"dialCode\":\"+1340\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:24.925+0530\"},{\"id\":244,\"code\":\"100243\",\"isoCode\":\"WLF\",\"name\":\"Wallis and Futuna\",\"countryCode\":\"WF\",\"status\":\"Active\",\"dialCode\":\"+681\",\"currencyCode\":\"XPF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:25.210+0530\"},{\"id\":245,\"code\":\"100244\",\"isoCode\":\"ESH\",\"name\":\"Western Sahara\",\"countryCode\":\"EH\",\"status\":\"Active\",\"dialCode\":\"+212\",\"currencyCode\":\"MAD\",\"currencySymbol\":\"د.م.\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:25.488+0530\"},{\"id\":246,\"code\":\"100245\",\"isoCode\":\"YEM\",\"name\":\"Yemen\",\"countryCode\":\"YE\",\"status\":\"Active\",\"dialCode\":\"+967\",\"mobileLength\":\"10\",\"currencyCode\":\"YER\",\"currencySymbol\":\"﷼\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:25.788+0530\"},{\"id\":247,\"code\":\"100246\",\"isoCode\":\"ZMB\",\"name\":\"Zambia\",\"countryCode\":\"ZM\",\"status\":\"Active\",\"dialCode\":\"+260\",\"mobileLength\":\"9\",\"currencyCode\":\"ZMW\",\"currencySymbol\":\"ZK\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:26.061+0530\"},{\"id\":248,\"code\":\"100247\",\"isoCode\":\"ZWE\",\"name\":\"Zimbabwe\",\"countryCode\":\"ZW\",\"status\":\"Active\",\"dialCode\":\"+263\",\"mobileLength\":\"9\",\"currencyCode\":\"BWP\",\"currencySymbol\":\"P\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:26.590+0530\"}]}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");


                    if (resultCode.equalsIgnoreCase("0")) {

                        //Toast.makeText(LocalRemittance.this, resultDescription, Toast.LENGTH_LONG).show();

                        JSONArray jsonArray_countryList = jsonObject.getJSONArray("countryList");
                        for (int i = 0; i < jsonArray_countryList.length(); i++) {

                            JSONObject jsonObject2 = jsonArray_countryList.getJSONObject(i);

                            int country_id = jsonObject2.getInt("id");
                            String country_code = jsonObject2.getString("code");
                            String country_isoCode = jsonObject2.getString("isoCode");
                            String countryCode_from_countryList_str = jsonObject2.getString("name");
                            String country_status = jsonObject2.getString("status");

                          //  String country_mobileLength = jsonObject2.getString("mobileLength");
                          //  String country_currencyCode = jsonObject2.getString("currencyCode");
                         //   String country_currency_symbol = jsonObject2.getString("currencySymbol");





                        }


                        api_gender_list();

                    } else {
                        Toast.makeText(CashToWallet.this, resultDescription, Toast.LENGTH_LONG).show();
                        //  finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(CashToWallet.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashToWallet.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    private void api_gender_list() {

        MyApplication.showloader(CashToWallet.this, getString(R.string.please_wait));


        API.GET_REMMITANCE_DETAILS("ewallet/api/v1/master/GENDERTYPE", languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {


                   // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"6050\",\"requestedBy\":\"101917\",\"requestTime\":\"Tue Oct 26 00:13:31 IST 2021\",\"responseTime\":\"Tue Oct 26 00:13:31 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"genderTypeList\":[{\"id\":2,\"code\":\"F\",\"type\":\"Female\",\"status\":\"Active\",\"creationDate\":\"1597748977410\"},{\"id\":1,\"code\":\"M\",\"type\":\"Male\",\"status\":\"Active\",\"creationDate\":\"1597748959156\"}]}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {

                       // Toast.makeText(LocalRemittance.this, resultDescription, Toast.LENGTH_LONG).show();

                        JSONArray jsonArray_genderTypeList = jsonObject.getJSONArray("genderTypeList");

                        arrayList_genderName = new ArrayList<>();
                        arrayList_genderCode = new ArrayList<>();

                        arrayList_genderName.add(0,getString(R.string.genderSelect_fixed));
                        arrayList_genderCode.add(0,getString(R.string.genderSelect_fixed));




                        for (int i = 0; i < jsonArray_genderTypeList.length(); i++) {

                            JSONObject jsonObject2 = jsonArray_genderTypeList.getJSONObject(i);

                            int gender_id = jsonObject2.getInt("id");
                            String gender_name = jsonObject2.getString("type");


                             genderSelect_code = String.valueOf(gender_id);

                            arrayList_genderCode.add(genderSelect_code);
                            arrayList_genderName.add(gender_name);
                        }


                        CustomeBaseAdapterGender recordAdapter = new CustomeBaseAdapterGender(CashToWallet.this, arrayList_genderName);
                        spinner_gender.setAdapter(recordAdapter);


                        String provided_array[] = getResources().getStringArray(R.array.Spinner_provider);
                        CustomeBaseAdapterProvided recordAdapter2 = new CustomeBaseAdapterProvided(CashToWallet.this, provided_array);
                        spinner_provider.setAdapter(recordAdapter2);


                        api_serviceProvider();


                    } else {
                        Toast.makeText(CashToWallet.this, resultDescription, Toast.LENGTH_LONG).show();
                        //  finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(CashToWallet.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashToWallet.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void api_idProof() {

        API.GET_REMMITANCE_DETAILS("ewallet/api/v1/master/IDPROOFTYPE", languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"6050\",\"requestedBy\":\"101917\",\"requestTime\":\"Tue Oct 26 00:13:31 IST 2021\",\"responseTime\":\"Tue Oct 26 00:13:31 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"idProffTypeList\":[{\"id\":6,\"code\":\"100005\",\"type\":\"COMPANY REGISTRATION NUMBER\",\"status\":\"Active\",\"creationDate\":\"1609835141414\"},{\"id\":5,\"code\":\"100004\",\"type\":\"MILITARY ID CARD\",\"status\":\"Active\",\"creationDate\":\"1609835141414\"},{\"id\":2,\"code\":\"100001\",\"type\":\"NATIONAL IDENTITY CARD\",\"status\":\"Active\",\"creationDate\":\"1609835141414\"},{\"id\":7,\"code\":\"100006\",\"type\":\"OTHER\",\"status\":\"Active\",\"creationDate\":\"1609835141414\"},{\"id\":1,\"code\":\"100000\",\"type\":\"PASSPORT\",\"status\":\"Active\",\"creationDate\":\"1597749171481\"},{\"id\":4,\"code\":\"100003\",\"type\":\"RESIDENCE CARD\",\"status\":\"Active\",\"creationDate\":\"1609835141414\"},{\"id\":3,\"code\":\"100002\",\"type\":\"VOTER CARD\",\"status\":\"Active\",\"creationDate\":\"1609835141414\"}]}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {

                      //  Toast.makeText(LocalRemittance.this,"-----api_idProof--------" +resultDescription, Toast.LENGTH_LONG).show();

                        JSONArray jsonArray_idProffTypeList = jsonObject.getJSONArray("idProffTypeList");
                        for (int i = 0; i < jsonArray_idProffTypeList.length(); i++) {
                            JSONObject jsonObject2 = jsonArray_idProffTypeList.getJSONObject(i);

                            int idproof_id = jsonObject2.getInt("id");
                            String idproof_code = jsonObject2.getString("code");
                            String idproof_type = jsonObject2.getString("type");
                            String idproof_status = jsonObject2.getString("status");
                            String idproof_creationDate = jsonObject2.getString("creationDate");
                        }


                       api_region();

                    } else {
                        Toast.makeText(CashToWallet.this, resultDescription, Toast.LENGTH_LONG).show();
                        //  finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(CashToWallet.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashToWallet.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }



    boolean validation_mobile_Details() {


        amountstr = edittext_amount.getText().toString().trim();
        mobileNoStr = et_subscriberNo.getText().toString().trim();

        deviceSenderStr = et_fp_deviceSender.getText().toString().trim();
        amountToPayStr = edittext_amount_pay.getText().toString().trim();

        name_destinationStr = et_fp_desinationName.getText().toString().trim();
        firstname_destinationStr = et_fp_firstNameDestination.getText().toString().trim();




       if (spinner_provider.getSelectedItemPosition()==0)
        {

            MyApplication.showErrorToast(this, getString(R.string.plz_select_provider));

            return false;
        }
       else if(mobileNoStr.isEmpty()) {

           MyApplication.showErrorToast(this,getString(R.string.plz_enter_subscriber_no));

           return false;
       }

       else if(mobileNoStr.length() < 9) {

           MyApplication.showErrorToast(this,getString(R.string.plz_enter_subscriber_no));

           return false;
       }

        else if (amountstr.isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.plz_enter_amount));

            return false;
        }

        else if (amountstr.trim().length() < 2) {

            MyApplication.showErrorToast(this, getString(R.string.device_to_send));

            return false;
        }


        else if (deviceSenderStr.isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.device_to_send));

            return false;
        }

        else if (amountToPayStr.isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.plz_enter_amount_to_pay));

            return false;
        }



        else if (amountToPayStr.trim().length() < 2) {

            MyApplication.showErrorToast(this, getString(R.string.plz_enter_amount_to_pay));

            return false;
        }





        else if (name_destinationStr.trim().length() < 2) {

            MyApplication.showErrorToast(this, getString(R.string.plz_enter_name));

            return false;
        }


        else if (firstname_destinationStr.trim().length() < 2) {

            MyApplication.showErrorToast(this, getString(R.string.plz_enter_firstName));

            return false;
        }

        else if (spinner_gender.getSelectedItemPosition()==0)
        {
            MyApplication.showErrorToast(this, getString(R.string.genderSelect_fixed));

            return false;
        }

        return true;


    }


    private void api_subscriberDetails() {

        String walletOwnerCategoryCode = MyApplication.getSaveString("walletOwnerCategoryCode", CashToWallet.this);

        walletOwnerCategoryCode = "100010"; // HARD CODE FINAL ACORDING TO PARVEEN


        API.GET_REMMITANCE_DETAILS("ewallet/api/v1/walletOwner/all?walletOwnerCategoryCode=" + walletOwnerCategoryCode + "&mobileNumber=" + mobileNoStr  + "&status=Y&offset=0&limit=500", languageToUse, new Api_Responce_Handler() {
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

                            if(jsonObject2.has("walletOwnerCode"))
                            {

                                walletOwnerCode_subs = jsonObject2.getString("walletOwnerCode");

                                countryCode_subscriber = jsonObject2.getString("registerCountryCode");
                                agentCode_subscriber = jsonObject2.getString("code");


                            }


                            if(jsonObject2.has("email"))
                            {
                                rp_tv_senderDocument.setText(jsonObject2.getString("email"));
                            }

                            if(jsonObject2.has("issuingCountryName"))
                            {
                                rp_tv_sending_currency.setText(jsonObject2.getString("issuingCountryName"));
                            }

                            if(jsonObject2.has("ownerName"))
                            {
                                receivernameStr = jsonObject2.getString("ownerName");
                                rp_tv_beneficiaryCurrency.setText(receivernameStr);

                            }

                        }

                        api_currency_sender();



                    } else {

                        Toast.makeText(CashToWallet.this, resultDescription, Toast.LENGTH_LONG).show();
                        edittext_amount.setEnabled(false);

                        mobileNoStr="";

                    }


                } catch (Exception e) {
                    Toast.makeText(CashToWallet.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashToWallet.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }



    private void api_currency_subscriber() {

        System.out.println(walletOwnerCode_subs);

        API.GET_REMMITANCE_DETAILS("ewallet/api/v1/walletOwnerCountryCurrency/" + walletOwnerCode_subs, languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {


                 //   JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1835348\",\"requestTime\":\"Mon Oct 25 20:16:40 IST 2021\",\"responseTime\":\"Mon Oct 25 20:16:40 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwnerCountryCurrencyList\":[{\"id\":8266,\"code\":\"108265\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100014\",\"currencyName\":\"BDT\",\"currencySymbol\":\"৳\",\"countryCurrencyCode\":\"100103\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"},{\"id\":8267,\"code\":\"108266\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100013\",\"currencyName\":\"BHD\",\"currencySymbol\":\".د.ب\",\"countryCurrencyCode\":\"100104\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"},{\"id\":8268,\"code\":\"108267\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100012\",\"currencyName\":\"BSD\",\"currencySymbol\":\"$\",\"countryCurrencyCode\":\"100105\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"},{\"id\":8269,\"code\":\"108268\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100007\",\"currencyName\":\"AUD\",\"currencySymbol\":\"$\",\"countryCurrencyCode\":\"100106\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"},{\"id\":8270,\"code\":\"108269\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100010\",\"currencyName\":\"AWG\",\"currencySymbol\":\"ƒ\",\"countryCurrencyCode\":\"100107\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"},{\"id\":8271,\"code\":\"108270\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100008\",\"currencyName\":\"ARS\",\"currencySymbol\":\"$\",\"countryCurrencyCode\":\"100108\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"},{\"id\":8272,\"code\":\"108271\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100005\",\"currencyName\":\"AOA\",\"currencySymbol\":\"Kz\",\"countryCurrencyCode\":\"100109\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"},{\"id\":8273,\"code\":\"108272\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100002\",\"currencyName\":\"DZD\",\"currencySymbol\":\"د.ج\",\"countryCurrencyCode\":\"100110\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"},{\"id\":8274,\"code\":\"108273\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100001\",\"currencyName\":\"ALL\",\"currencySymbol\":\"L\",\"countryCurrencyCode\":\"100111\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"},{\"id\":8275,\"code\":\"108274\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100003\",\"currencyName\":\"USD\",\"currencySymbol\":\"$\",\"countryCurrencyCode\":\"100074\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"},{\"id\":8276,\"code\":\"108275\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100069\",\"currencyName\":\"INR\",\"currencySymbol\":\"₹\",\"countryCurrencyCode\":\"100075\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"},{\"id\":8277,\"code\":\"108276\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100062\",\"currencyName\":\"GNF\",\"currencySymbol\":\"Fr\",\"countryCurrencyCode\":\"100076\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"},{\"id\":8278,\"code\":\"108277\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100004\",\"currencyName\":\"EUR\",\"currencySymbol\":\"€\",\"countryCurrencyCode\":\"100090\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"},{\"id\":8279,\"code\":\"108278\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100000\",\"currencyName\":\"AFN\",\"currencySymbol\":\"؋\",\"countryCurrencyCode\":\"100093\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"},{\"id\":8280,\"code\":\"108279\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100028\",\"currencyName\":\"BGN\",\"currencySymbol\":\"лв\",\"countryCurrencyCode\":\"100094\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"},{\"id\":8281,\"code\":\"108280\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100027\",\"currencyName\":\"BND\",\"currencySymbol\":\"$\",\"countryCurrencyCode\":\"100095\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"},{\"id\":8282,\"code\":\"108281\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100026\",\"currencyName\":\"BRL\",\"currencySymbol\":\"R$\",\"countryCurrencyCode\":\"100096\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"},{\"id\":8283,\"code\":\"108282\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100024\",\"currencyName\":\"BWP\",\"currencySymbol\":\"P\",\"countryCurrencyCode\":\"100097\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"},{\"id\":8284,\"code\":\"108283\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100021\",\"currencyName\":\"BOB\",\"currencySymbol\":\"Bs.\",\"countryCurrencyCode\":\"100098\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"},{\"id\":8285,\"code\":\"108284\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100020\",\"currencyName\":\"BTN\",\"currencySymbol\":\"Nu.\",\"countryCurrencyCode\":\"100099\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"},{\"id\":8286,\"code\":\"108285\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100019\",\"currencyName\":\"BMD\",\"currencySymbol\":\"$\",\"countryCurrencyCode\":\"100100\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"},{\"id\":8287,\"code\":\"108286\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100017\",\"currencyName\":\"BZD\",\"currencySymbol\":\"$\",\"countryCurrencyCode\":\"100101\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"},{\"id\":8288,\"code\":\"108287\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100015\",\"currencyName\":\"BBD\",\"currencySymbol\":\"$\",\"countryCurrencyCode\":\"100102\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"},{\"id\":8289,\"code\":\"108288\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100018\",\"currencyName\":\"XOF\",\"currencySymbol\":\"CFA\",\"countryCurrencyCode\":\"100114\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"},{\"id\":8290,\"code\":\"108289\",\"walletOwnerCode\":\"1000002606\",\"currencyCode\":\"100058\",\"currencyName\":\"GHS\",\"currencySymbol\":\"₵\",\"countryCurrencyCode\":\"100132\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"}]}");

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

                                } else {

                                }
                            }

                            Toast.makeText(CashToWallet.this, currencyName_subscriber+"======"+currencyCode_subscriber, Toast.LENGTH_LONG).show();


                        }

                    } else {
                        Toast.makeText(CashToWallet.this, resultDescription, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    Toast.makeText(CashToWallet.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashToWallet.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    private void api_serviceProvider() {

        // Hard Code Final Deepak

        API.GET_CASHIN_DETAILS("ewallet/api/v1/serviceProvider/serviceCategory?serviceCode=100002&serviceCategoryCode=100001&status=Y", languageToUse, new Api_Responce_Handler() {
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

                        api_walletOwnerUser();



                    } else {
                        Toast.makeText(CashToWallet.this, resultDescription, Toast.LENGTH_LONG).show();
                    }





                } catch (Exception e) {
                    Toast.makeText(CashToWallet.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashToWallet.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


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

                                } else {

                                }
                            }

                        }

                        api_currency_subscriber();

                    }

                    else {
                        Toast.makeText(CashToWallet.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(CashToWallet.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashToWallet.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    private void api_walletOwnerUser() {

        String USER_CODE_FROM_TOKEN_AGENTDETAILS = MyApplication.getSaveString("userCode", CashToWallet.this);


        API.GET_REMMITANCE_DETAILS("ewallet/api/v1/walletOwnerUser/" + USER_CODE_FROM_TOKEN_AGENTDETAILS, languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1789408\",\"requestTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"responseTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwnerUser\":{\"id\":2171,\"code\":\"101917\",\"firstName\":\"TATASnegal\",\"userName\":\"TATASnegal5597\",\"mobileNumber\":\"8888888882\",\"email\":\"kundan.kumar@esteltelecom.com\",\"walletOwnerUserTypeCode\":\"100000\",\"walletOwnerUserTypeName\":\"Supervisor\",\"walletOwnerCategoryCode\":\"100000\",\"walletOwnerCategoryName\":\"Institute\",\"userCode\":\"1000002606\",\"status\":\"Active\",\"state\":\"Approved\",\"gender\":\"M\",\"idProofTypeCode\":\"100004\",\"idProofTypeName\":\"MILITARY ID CARD\",\"idProofNumber\":\"44444444444\",\"creationDate\":\"2021-10-01T09:04:07.330+0530\",\"notificationName\":\"EMAIL\",\"notificationLanguage\":\"en\",\"createdBy\":\"100308\",\"modificationDate\":\"2021-10-20T14:59:00.791+0530\",\"modifiedBy\":\"100308\",\"addressList\":[{\"id\":3569,\"walletOwnerUserCode\":\"101917\",\"addTypeCode\":\"100001\",\"addTypeName\":\"Commercial\",\"regionCode\":\"100068\",\"regionName\":\"Boke\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"city\":\"100022\",\"cityName\":\"Dubreka\",\"addressLine1\":\"delhi\",\"status\":\"Inactive\",\"creationDate\":\"2021-10-01T09:04:07.498+0530\",\"createdBy\":\"100250\",\"modificationDate\":\"2021-10-03T09:52:57.407+0530\",\"modifiedBy\":\"100308\"}],\"workingDaysList\":[{\"id\":3597,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100000\",\"weekdaysName\":\"Monday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.518+0530\"},{\"id\":3598,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100001\",\"weekdaysName\":\"Tuesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.528+0530\"},{\"id\":3599,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100002\",\"weekdaysName\":\"Wednesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.538+0530\"},{\"id\":3600,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100003\",\"weekdaysName\":\"Thursday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.547+0530\"},{\"id\":3601,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100004\",\"weekdaysName\":\"Friday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.556+0530\"},{\"id\":3602,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100005\",\"weekdaysName\":\"Saturday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.565+0530\"},{\"id\":3603,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100006\",\"weekdaysName\":\"Sunday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"2:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.573+0530\"}],\"macEnabled\":false,\"ipEnabled\":false,\"resetCredReqInit\":false,\"notificationTypeCode\":\"100000\"}}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {

                        if (resultCode.equalsIgnoreCase("0")) {

                            //  Toast.makeText(LocalRemittance.this,"----- api_walletOwnerUser ----"+ resultDescription, Toast.LENGTH_LONG).show();

                            if (jsonObject.has("walletOwnerUser")) {

                                JSONObject walletOwnerUser = jsonObject.getJSONObject("walletOwnerUser");



                                countryCode_agent = walletOwnerUser.getString("issuingCountryCode");
                                countryName_agent = walletOwnerUser.getString("issuingCountryName");


                                if (walletOwnerUser.has("mobileNumber")) {
                                    mobileNumber_sender_from_walletOwnerUser = walletOwnerUser.getString("mobileNumber");
                                }


                                if (walletOwnerUser.has("firstName")) {
                                    firstName_sender_from_walletOwnerUser = walletOwnerUser.getString("firstName");

                                    if(firstName_sender_from_walletOwnerUser.contains(" "))
                                    {
                                        String[] lastName_temp=firstName_sender_from_walletOwnerUser.split("\\ ");
                                        lastName_sender_from_walletOwnerUser = lastName_temp[1];
                                    }
                                    else
                                    {

                                    }

                                }

                                if (walletOwnerUser.has("email")) {
                                    email_sender_from_walletOwnerUser = walletOwnerUser.getString("email");
                                }

                                if (walletOwnerUser.has("idProofTypeCode")) {
                                    idProofTypeCode_sender_from_walletOwnerUser = walletOwnerUser.getString("idProofTypeCode");
                                }



                                if (walletOwnerUser.has("idProofNumber")) {
                                    idProofNumber_sender_from_walletOwnerUser = walletOwnerUser.getString("idProofNumber");
                                }

                                if (walletOwnerUser.has("creationDate")) {
                                    idExpiryDate_sender_from_walletOwnerUser = walletOwnerUser.getString("creationDate");
                                }

                                if (walletOwnerUser.has("creationDate")) {
                                    dateOfBirth_sender_from_walletOwnerUser = walletOwnerUser.getString("creationDate");
                                }


                                if (walletOwnerUser.has("addressList")) {

                                    JSONArray jsonArray = walletOwnerUser.getJSONArray("addressList");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                                        if (jsonObject2.has("regionCode")) {
                                            regionCode_sender_from_walletOwnerUser = jsonObject2.getString("regionCode");
                                        }
                                        if (jsonObject2.has("cityName")) {
                                            city_sender_from_walletOwnerUser = jsonObject2.getString("cityName");
                                        }
                                        if (jsonObject2.has("addressLine1")) {
                                            address_sender_from_walletOwnerUser = jsonObject2.getString("addressLine1");
                                        }
                                    }
                                }
                            }





                        } else {
                            Toast.makeText(CashToWallet.this, resultDescription, Toast.LENGTH_LONG).show();
                        }





                    } else {
                        Toast.makeText(CashToWallet.this, resultDescription, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    Toast.makeText(CashToWallet.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashToWallet.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void api_region() {

        API.GET_REMMITANCE_DETAILS("ewallet/api/v1/region/country/" + "100092", languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {


                   // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1840636\",\"requestTime\":\"Tue Oct 26 00:17:34 IST 2021\",\"responseTime\":\"Tue Oct 26 00:17:34 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"country\":{\"id\":93,\"code\":\"100092\",\"isoCode\":\"GIN\",\"name\":\"Guinea\",\"countryCode\":\"GN\",\"status\":\"Active\",\"dialCode\":\"+224\",\"mobileLength\":\"9\",\"currencyCode\":\"GNF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":true,\"creationDate\":\"2020-08-11T15:04:42.459+0530\",\"regionList\":[{\"id\":75,\"code\":\"100068\",\"name\":\"Boke\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"status\":\"Active\",\"state\":\"Created\",\"creationDate\":\"2021-01-15T21:29:26.094+0530\",\"modificationDate\":\"2021-10-25T22:39:12.974+0530\"},{\"id\":4,\"code\":\"100003\",\"name\":\"Conakry\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"status\":\"Active\",\"state\":\"Created\",\"creationDate\":\"2020-06-30T18:39:22.000+0530\"},{\"id\":9,\"code\":\"100004\",\"name\":\"Faranah\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"status\":\"Active\",\"state\":\"Created\",\"creationDate\":\"2020-06-30T18:39:22.000+0530\"},{\"id\":10,\"code\":\"100005\",\"name\":\"Kankan\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"status\":\"Active\",\"state\":\"Created\",\"creationDate\":\"2020-06-30T18:39:22.000+0530\"},{\"id\":11,\"code\":\"100006\",\"name\":\"Kindia\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"status\":\"Active\",\"state\":\"Created\",\"creationDate\":\"2020-06-30T18:39:22.000+0530\"},{\"id\":13,\"code\":\"100007\",\"name\":\"Mamou\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"status\":\"Active\",\"state\":\"Created\",\"creationDate\":\"2020-06-30T18:39:22.000+0530\"}],\"nameFr\":\"Guinée\"}}");


                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");


                    if (resultCode.equalsIgnoreCase("0")) {



                        api_sender();




                    } else {
                        Toast.makeText(CashToWallet.this, resultDescription, Toast.LENGTH_LONG).show();
                    }



                } catch (Exception e) {
                    Toast.makeText(CashToWallet.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashToWallet.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    private void api_allByCriteria() {

        API.GET_REMMITANCE_DETAILS("ewallet/api/v1/customer/allByCriteria?firstName="+firstname_destinationStr+"%20&countryCode=" + "100092", languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                  //  JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1853328\",\"requestTime\":\"Tue Oct 26 23:03:18 IST 2021\",\"responseTime\":\"Tue Oct 26 23:03:18 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"customerList\":[{\"id\":1702,\"code\":\"1000001705\",\"firstName\":\"sharique \",\"lastName\":\"anwar\",\"mobileNumber\":\"9878787878\",\"gender\":\"M\",\"idProofTypeCode\":\"100000\",\"idProofTypeName\":\"PASSPORT\",\"idProofNumber\":\"id12345\",\"idExpiryDate\":\"2021-10-30\",\"dateOfBirth\":\"2003-10-24\",\"email\":\"sharique@gmail.com\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"regionCode\":\"100068\",\"regionName\":\"Boke\",\"city\":\"new delhi\",\"address\":\"delhi okhla\",\"issuingCountryCode\":\"100092\",\"issuingCountryName\":\"Guinea\",\"status\":\"Active\",\"creationDate\":\"2021-10-26T22:59:39.323+0530\",\"createdBy\":\"102068\"},{\"id\":1681,\"code\":\"1000001684\",\"firstName\":\"sharique \",\"lastName\":\"anwar\",\"mobileNumber\":\"9878787878\",\"gender\":\"M\",\"idProofTypeCode\":\"100000\",\"idProofTypeName\":\"PASSPORT\",\"idProofNumber\":\"id12345\",\"idExpiryDate\":\"2021-10-29\",\"dateOfBirth\":\"2003-10-16\",\"email\":\"sharique@gmail.com\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"regionCode\":\"100068\",\"regionName\":\"Boke\",\"city\":\"new delhi\",\"address\":\"delhi okhla\",\"issuingCountryCode\":\"100092\",\"issuingCountryName\":\"Guinea\",\"idProofUrl\":\"Paspost_size_demo.jpg\",\"status\":\"Active\",\"creationDate\":\"2021-10-26T00:20:05.401+0530\",\"createdBy\":\"101917\",\"modificationDate\":\"2021-10-26T12:21:16.418+0530\",\"modifiedBy\":\"102068\"}]}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");


                    if(jsonObject.has("customerList")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("customerList");
                        {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                if(jsonObject1.has("gender"))
                                {
                                    gender_sender_from_allByCriteria=jsonObject1.getString("gender");
                                }

                            }
                        }
                    }

                    rp_tv_agentCode.setText(MyApplication.getSaveString("USERCODE", CashToWallet.this));
                    rp_tv_sender_id.setText(senderCode_from_senderApi);
                    rp_tv_benificicaryCode.setText(receivercode_from_receiverAPi);
                    rp_tv_senderDocument.setText("on image available"); // Temporary hard code no Option on First Page

                    rp_tv_sending_currency.setText(currencyName_agent); // annu
                    rp_tv_beneficiaryCurrency.setText(currencyCode_subscriber);
                    rp_tv_ReceiverMSISDN.setText(mobileNoStr);

                    if (resultCode.equalsIgnoreCase("0")) {



                        ll_page_1.setVisibility(View.GONE);
                        ll_reviewpage.setVisibility(View.VISIBLE);
                        ll_receiptPage.setVisibility(View.GONE);






                    } else {

                        Toast.makeText(CashToWallet.this, resultDescription, Toast.LENGTH_LONG).show();


                    }


                } catch (Exception e) {
                    Toast.makeText(CashToWallet.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    finish();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashToWallet.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    boolean validation_mpin_detail() {

        mpinStr = et_mpin.getText().toString();

        if (mpinStr.trim().isEmpty()) {
            MyApplication.showErrorToast(this, getString(R.string.please_enter_4_digit_mpin));
            return false;
        }

        else if (mpinStr.trim().length() == 4) {
            return true;
        } else {

            MyApplication.showErrorToast(this, getString(R.string.please_enter_4_digit_mpin));

            return false;
        }


    }

    private void api_receiver() {

        try {

            JSONObject jsonObject = new JSONObject();


//            {
//                "firstName":"saasasas",
//                    "lastName":"asasassasa",
//                    "email":"",
//                    "mobileNumber":"455445455445",
//                    "idProofTypeCode":"",
//                    "idProofNumber":"",
//                    "idExpiryDate":null,
//                    "dateOfBirth":null,
//                    "countryCode":"100092",
//                    "regionCode":"",
//                    "city":"",
//                    "address":"",
//                    "issuingCountryCode":"100092",
//                    "gender":"M
//            }


            jsonObject.put("firstName", firstname_destinationStr);
            jsonObject.put("lastName", ""); // No in UI
            jsonObject.put("email", "");    // No in UI
            jsonObject.put("mobileNumber", mobileNoStr);    // No in UI
            jsonObject.put("idProofTypeCode", "");    // No in UI
            jsonObject.put("idProofNumber", "");    // No in UI By Defualt
            jsonObject.put("idExpiryDate", "");    // No in UI
            jsonObject.put("dateOfBirth", "");    // No in UI
            // jsonObject.put("countryCode","100092");  // Hard Code according  to Deepak
            jsonObject.put("countryCode","100092");  // Hard Code according  to Deepak
            jsonObject.put("regionCode",""); // No in UI
            jsonObject.put("city","");   // No in UI
            jsonObject.put("address","");   // No in UI
            jsonObject.put("issuingCountryCode","100092");  // Hard Code according  to Deepak
            jsonObject.put("gender",genderSelect_name);



            API.POST_REMMIT_LOCAL("ewallet/api/v1/customer/receiver", jsonObject, languageToUse, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {


                        String resultCode = jsonObject.getString("resultCode");
                        String resultDescription = jsonObject.getString("resultDescription");

                        if (resultCode.equalsIgnoreCase("0")) {

                            JSONObject jsonObject1 = jsonObject.getJSONObject("customer");

                            receivercode_from_receiverAPi = jsonObject1.getString("code");


                            api_allByCriteria();

                        } else {
                            Toast.makeText(CashToWallet.this, resultDescription, Toast.LENGTH_LONG).show();
                            //  finish();
                        }


                    } catch (Exception e) {
                        Toast.makeText(CashToWallet.this, e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }


                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(CashToWallet.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    } else

                        Toast.makeText(CashToWallet.this, aFalse, Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(CashToWallet.this, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }



    private void api_sender() {

        try {

            JSONObject jsonObject = new JSONObject();


//            {
//                "firstName":"saasasas",
//                    "lastName":"asasassasa",
//                    "email":"",
//                    "mobileNumber":"455445455445",
//                    "idProofTypeCode":"",
//                    "idProofNumber":"",
//                    "idExpiryDate":null,
//                    "dateOfBirth":null,
//                    "countryCode":"100092",
//                    "regionCode":"",
//                    "city":"",
//                    "address":"",
//                    "issuingCountryCode":"100092",
//                    "gender":"M
//            }


            jsonObject.put("firstName", firstName_sender_from_walletOwnerUser);
            jsonObject.put("lastName", lastName_sender_from_walletOwnerUser);
            jsonObject.put("email", email_sender_from_walletOwnerUser);
            jsonObject.put("mobileNumber", mobileNumber_sender_from_walletOwnerUser);
            jsonObject.put("idProofTypeCode", idProofTypeCode_sender_from_walletOwnerUser);
            jsonObject.put("idProofNumber", idProofTypeCode_sender_from_walletOwnerUser);
            jsonObject.put("idExpiryDate", idExpiryDate_sender_from_walletOwnerUser);
            jsonObject.put("dateOfBirth", dateOfBirth_sender_from_walletOwnerUser);
            jsonObject.put("countryCode","100092");
            jsonObject.put("regionCode",regionCode_sender_from_walletOwnerUser);
            jsonObject.put("city",city_sender_from_walletOwnerUser);
            jsonObject.put("address",address_sender_from_walletOwnerUser);
            jsonObject.put("issuingCountryCode","100092");

            if(gender_sender_from_allByCriteria.equalsIgnoreCase("")) // If Not Coming From Server Then Send M (It mandatory )
            {
                jsonObject.put("gender","M");
            }
            else {
                jsonObject.put("gender",gender_sender_from_allByCriteria);

            }



            API.POST_REMIT_SENDER_RECEIVER("ewallet/api/v1/customer/sender", jsonObject, languageToUse, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {


                        String resultCode = jsonObject.getString("resultCode");
                        String resultDescription = jsonObject.getString("resultDescription");

                        if (resultCode.equalsIgnoreCase("0")) {

                            JSONObject jsonObject1 = jsonObject.getJSONObject("customer");

                            senderCode_from_senderApi = jsonObject1.getString("code");


                            api_receiver();

                        } else {
                            Toast.makeText(CashToWallet.this, resultDescription, Toast.LENGTH_LONG).show();
                            //  finish();
                        }


                    } catch (Exception e) {
                        Toast.makeText(CashToWallet.this, e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }


                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(CashToWallet.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    } else

                        Toast.makeText(CashToWallet.this, aFalse, Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(CashToWallet.this, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }




    private void api_mpin_final() {


        try {

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("walletOwnerCode", walletOwnerCode_mssis_agent);
            jsonObject.put("senderCode",senderCode_from_senderApi);
            jsonObject.put("receiverCode",receivercode_from_receiverAPi);

            jsonObject.put("fromCurrencyCode", currencyCode_agent);
            jsonObject.put("toCurrencyCode", currencyCode_subscriber);
            jsonObject.put("receiveCountryCode",countryCode_subscriber);

            jsonObject.put("amount", amountstr);
            jsonObject.put("receiveMode","WALLET");
            jsonObject.put("conversionRate", convertionFeesStr);
            String encryptionDatanew = AESEncryption.getAESEncryption(mpinStr);
            jsonObject.put("pin", encryptionDatanew);
            jsonObject.put("comments", "");  // Hard Code according  to Deepak
           // jsonObject.put("channelTypeCode", "100000");  // Hard Code according  to Deepak
            jsonObject.put("serviceCode", serviceCode_from_serviceCategory);
            jsonObject.put("serviceCategoryCode", serviceCategoryCode_from_serviceCategory);
            jsonObject.put("serviceProviderCode", serviceProviderCode_from_serviceCategory);
            jsonObject.put("firstName",firstname_destinationStr);
            jsonObject.put("mobileNumber",mobileNoStr);

            String requestNo=AESEncryption.getAESEncryption(jsonObject.toString());
            JSONObject jsonObjectA=null;
            try{
                jsonObjectA=new JSONObject();
                jsonObjectA.put("request",requestNo);
                System.out.println("get json "+jsonObjectA);
            }catch (Exception e){




            }

            API.POST_REMMIT_CASHTOWALLET("ewallet/api/v1/remittance/cashToWallet", jsonObjectA, languageToUse, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {


                       // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"117343\",\"requestTime\":\"Wed Oct 27 09:23:56 IST 2021\",
                        // \"responseTime\":\"Wed Oct 27 09:23:57 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\"
                        // ,\"walletTransfer\":{\"code\":\"117343\",\"srcWalletCode\":\"1000028522\",\"desWalletCode\":\"1000028533\",\"srcWalletOwnerCode\":\"1000002785\",\"desWalletOwnerCode\":\"1000002794\",\"srcWalletTypeCode\":\"100008\",\"desWalletTypeCode\":\"100008\",\"srcCurrencyCode\":\"100062\",\"desCurrencyCode\":\"100062\",\"srcCurrencyName\":\"GNF\",\"desCurrencyName\":\"GNF\",\"srcCurrencySymbol\":\"Fr\",\"desCurrencySymbol\":\"Fr\",\"value\":11695,\"createdBy\":\"102068\",\"creationDate\":\"2021-10-27 09:23:57\",\"fee\":1500,\"finalAmount\":10000,\"srcWalletOwner\":{\"id\":110679,\"code\":\"1000002785\",\"walletOwnerCategoryCode\":\"100000\",\"ownerName\":\"sharique agent\",\"mobileNumber\":\"9990063618\",\"businessTypeCode\":\"100008\",\"businessTypeName\":\"Goldsmith\",\"lineOfBusiness\":\"gffg\",\"idProofNumber\":\"trt465656\",\"email\":\"sharique9718@gmail.com\",\"status\":\"Active\",\"state\":\"Enrolled\",\"stage\":\"Document\",\"idProofTypeCode\":\"100005\",\"idProofTypeName\":\"COMPANY REGISTRATION NUMBER\",\"idExpiryDate\":\"2021-10-22\",\"notificationLanguage\":\"en\",\"notificationTypeCode\":\"100000\",\"notificationName\":\"EMAIL\",\"issuingCountryCode\":\"100102\",\"issuingCountryName\":\"India\",\"registerCountryCode\":\"100102\",\"registerCountryName\":\"India\",\"createdBy\":\"100250\",\"creationDate\":\"2021-10-19T22:38:48.969+0530\",\"modificationDate\":\"2021-10-21T21:27:53.250+0530\",\"walletExists\":true,\"profileTypeCode\":\"100000\",\"profileTypeName\":\"tier1\",\"walletCurrencyList\":[\"100018\",\"100017\",\"100069\",\"100020\",\"100004\",\"100029\",\"100062\",\"100003\"],\"walletOwnerCatName\":\"Institute\",\"requestedSource\":\"ADMIN\",\"regesterCountryDialCode\":\"+91\",\"issuingCountryDialCode\":\"+91\",\"walletOwnerCode\":\"1000002785\",\"senderName\":\"sharique\",\"senderMobileNumber\":\"971819684998\"},\"desWalletOwner\":{\"id\":110688,\"code\":\"1000002794\",\"walletOwnerCategoryCode\":\"100010\",\"ownerName\":\"Sharique subs\",\"mobileNumber\":\"9015931368\",\"idProofNumber\":\"passportno123\",\"email\":\"sharique9718@gmail.com\",\"status\":\"Active\",\"state\":\"Approved\",\"stage\":\"Document\",\"idProofTypeCode\":\"100000\",\"idProofTypeName\":\"PASSPORT\",\"idExpiryDate\":\"2021-10-28\",\"notificationLanguage\":\"en\",\"notificationTypeCode\":\"100000\",\"notificationName\":\"EMAIL\",\"gender\":\"M\",\"dateOfBirth\":\"1959-09-09\",\"lastName\":\"sharique anwar\",\"issuingCountryCode\":\"100102\",\"issuingCountryName\":\"India\",\"registerCountryCode\":\"100092\",\"registerCountryName\":\"Guinea\",\"createdBy\":\"100250\",\"modifiedBy\":\"100308\",\"creationDate\":\"2021-10-22T12:05:58.055+0530\",\"modificationDate\":\"2021-10-22T12:16:13.398+0530\",\"walletExists\":true,\"profileTypeCode\":\"100001\",\"profileTypeName\":\"tier2\",\"walletOwnerCatName\":\"Subscriber\",\"occupationTypeCode\":\"100000\",\"occupationTypeName\":\"Self employed\",\"requestedSource\":\"ADMIN\",\"regesterCountryDialCode\":\"+224\",\"issuingCountryDialCode\":\"+91\",\"walletOwnerCode\":\"1000002794\"},\"taxConfigurationList\":[{\"taxTypeCode\":\"100133\",\"taxTypeName\":\"Financial Tax\",\"value\":195,\"taxAvailBy\":\"Fee Amount\"}],\"transactionType\":\"Cash to Wallet\"}}");

                        String resultCode = jsonObject.getString("resultCode");
                        String resultDescription = jsonObject.getString("resultDescription");

                        if (resultCode.equalsIgnoreCase("0")) {

                            ll_page_1.setVisibility(View.GONE);
                            ll_reviewpage.setVisibility(View.GONE);

                            ll_receiptPage.setVisibility(View.VISIBLE);


                            receiptPage_tv_stransactionType.setText(getString(R.string.cash_to_wallet));
                            receiptPage_tv_benificiary_no.setText(mobileNoStr);

                            receiptPage_tv_senderCurrency.setText("GNF");
                            receiptPage_tv_BenificiaryCurrency.setText("GNF");
                            receiptPage_tv_transactionAmount.setText("Fr "+amountstr);
                            receiptPage_tv_fee.setText("Fr "+fees_amount);
                            receiptPage_tv_financialtax.setText("Fr "+tax_financial);
                            receiptPage_tv_transaction_receiptNo.setText(jsonObject.getString("transactionId"));
                            receiptPage_tv_dateOfTransaction.setText(jsonObject.optJSONObject("walletTransfer").getString("creationDate"));

                            receiptPage_tv_amount_to_be_charge.setText("Fr "+totalAmount_str);


                            receiptPage_tv_sender_phoneNo.setText(MyApplication.getSaveString("USERNAME", CashToWallet.this));
                            receiptPage_tv_sender_name.setText(deviceSenderStr);
                            receiptPage_tv_sender_phoneNo.setText(MyApplication.getSaveString("USERNAME", CashToWallet.this));
                            receiptPage_tv_receiver_name.setText(receivernameStr);
                            receiptPage_tv_receiver_phoneNo.setText(mobileNoStr);


                        } else {
                            Toast.makeText(CashToWallet.this, resultDescription, Toast.LENGTH_LONG).show();
                            //  finish();
                        }


                    } catch (Exception e) {
                        Toast.makeText(CashToWallet.this, e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }


                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(CashToWallet.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    } else

                        Toast.makeText(CashToWallet.this, aFalse, Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(CashToWallet.this, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }

    private void api_exchangeRate() {




           API.GET_REMMITANCE_DETAILS("ewallet/api/v1/exchangeRate/getAmountDetails?sendCurrencyCode=" + currencyCode_agent +
                                "&receiveCurrencyCode="+currencyCode_subscriber+"&sendCountryCode=" +
                                countryCode_agent + "&receiveCountryCode="+countryCode_subscriber+
                                "&currencyValue=" + amountstr + "&channelTypeCode=100002&serviceCode=" +
                                serviceCode_from_serviceCategory + "&serviceCategoryCode=" +
                                serviceCategoryCode_from_serviceCategory + "&serviceProviderCode=" +
                                serviceProviderCode_from_serviceCategory + "&walletOwnerCode=" + walletOwnerCode_mssis_agent,


                        languageToUse, new Api_Responce_Handler() {


            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                   // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1853221\",\"requestTime\":\"Tue Oct 26 22:34:47 IST 2021\",\"responseTime\":\"Tue Oct 26 22:34:47 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"exchangeRate\":{\"value\":\"0.0\",\"currencyValue\":\"1500\",\"fee\":1500,\"taxConfigurationList\":[{\"taxTypeCode\":\"100133\",\"taxTypeName\":\"Financial Tax\",\"value\":195,\"taxAvailBy\":\"Fee Amount\"}]}}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {

                     //   Toast.makeText(LocalRemittance.this, resultDescription, Toast.LENGTH_LONG).show();




                        JSONObject exchangeRate = jsonObject.getJSONObject("exchangeRate");

                        fees_amount = exchangeRate.getString("fee");
                        rp_tv_fees_reveiewPage.setText("Fr "+fees_amount);


                        edittext_amount_pay.setEnabled(false);
                        edittext_amount_pay.setText("Fr "+MyApplication.addDecimal(amountstr));
                        convertionRate_first_page.setText("Fr "+MyApplication.addDecimalthreenew(exchangeRate.getString("value")));
                        if(!exchangeRate.has("receiverTax")) {
                            if (exchangeRate.has("taxConfigurationList")) {
                                JSONArray jsonArray = exchangeRate.getJSONArray("taxConfigurationList");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    tax_financial = jsonObject2.getString("value");
                                }
                            } else {
                                tax_financial = exchangeRate.getString("value");
                            }
                        }


                        tax_first_page.setText("Fr "+tax_financial);
                        fees_first_page.setText("Fr "+fees_amount);


                        rp_tv_financialTax.setText("Fr "+tax_financial);

                        tax_financial_double = Double.parseDouble(tax_financial);
                        //  credit_amount_double = Double.parseDouble(credit_amount);
                        fees_amount_double = Double.parseDouble(fees_amount);
                        amountstr_double = Double.parseDouble(amountstr);

                         convertionFeesStr = String.valueOf(fees_amount_double);
                        rp_tv_convertionFees.setText("Fr "+convertionFeesStr);


                        totalAmount_double = tax_financial_double + amountstr_double + fees_amount_double;
                        totalAmount_str = String.valueOf(totalAmount_double);
                        rp_tv_amount_to_be_charge.setText("Fr "+totalAmount_str);

                        amountstr = String.valueOf(amountstr_double);
                        rp_tv_transactionAmount.setText("Fr "+amountstr);
                        rp_tv_amount_to_be_paid.setText("Fr "+amountstr);

                        amountTobeCharged_first_page.setText("Fr "+totalAmount_str);

//
//                        ll_page_1.setVisibility(View.GONE);
//                        ll_reviewpage.setVisibility(View.VISIBLE);
//                        ll_receiptPage.setVisibility(View.GONE);
//



                    } else {
                        Toast.makeText(CashToWallet.this, resultDescription, Toast.LENGTH_LONG).show();
                       // finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(CashToWallet.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashToWallet.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }
    public static long mLastClickTime=0;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_nextClick: {


                if (validation_mobile_Details()) {


                    if (new InternetCheck().isConnected(CashToWallet.this)) {

                        MyApplication.showloader(CashToWallet.this, getString(R.string.please_wait));


                        api_idProof();

                    } else {
                        Toast.makeText(CashToWallet.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                    }
                }
            }

            break;

            case R.id.confirm_reviewClick_textview: {

                if (validation_mpin_detail()) {

                    if (new InternetCheck().isConnected(CashToWallet.this)) {

                        MyApplication.showloader(CashToWallet.this, getString(R.string.please_wait));

                       api_mpin_final();

                    } else {
                        Toast.makeText(CashToWallet.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                    }
                }

            }
            break;

            case R.id.exportReceipt_textview: {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) { // 1000 = 1second
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                close_receiptPage_textview.setVisibility(View.VISIBLE);
                exportReceipt_textview.setVisibility(View.VISIBLE);
                Bitmap bitmap = getScreenShot(rootView);
                createImageFile(bitmap);
                //store(bitmap, "test.jpg");
            }

            break;

            case R.id.previous_reviewClick_textview: {

                ll_page_1.setVisibility(View.VISIBLE);
                ll_reviewpage.setVisibility(View.GONE);
                ll_receiptPage.setVisibility(View.GONE);
            }

            break;

            case R.id.close_receiptPage_textview: {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) { // 1000 = 1second
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
//                    ll_page_1.setVisibility(View.VISIBLE);
//                    ll_reviewpage.setVisibility(View.GONE);
//                    ll_receiptPage.setVisibility(View.GONE);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }

            break;

        }
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
                        et_subscriberNo.setEnabled(true);

                    } else {


                        String[] qrData = str.split("\\:");
                        mobileNoStr = qrData[0];
                        et_subscriberNo.setText(mobileNoStr);
                        et_subscriberNo.setEnabled(false);

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

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
               // bm.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            shareImage(image);
        }catch (Exception e){

        }
    }

//    public void store(Bitmap bm, String fileName) {
//        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
//        File dir = new File(dirPath);
//        if (!dir.exists())
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

    private void shareImage(File file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.share_screenshot)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_app_available), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinner_gender:
                {

                     genderSelect_name = arrayList_genderName.get(i);
                     genderSelect_code = arrayList_genderCode.get(i);

                 //  Toast.makeText(LocalRemittance.this, genderSelect_name, Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.spinner_provider:
                {

                    String provided_array[] = getResources().getStringArray(R.array.Spinner_provider);
                    provider_select_name = provided_array[i];

                  //  Toast.makeText(this,provider_select_name, Toast.LENGTH_SHORT).show();

                }

                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}