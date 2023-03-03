package com.agent.cashmoovui.receive_money;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.cardview.widget.CardView;


import com.agent.cashmoovui.HiddenPassTransformationMethod;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.activity.TransactionSuccessScreen;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.apiCalls.BioMetric_Responce_Handler;


import com.agent.cashmoovui.cash_in.CashIn;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.otp.VerifyLoginAccountScreen;
import com.agent.cashmoovui.remittancebyabhay.cashtowallet.CashtoWalletReceiverKYC;
import com.agent.cashmoovui.set_pin.AESEncryption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ReceiveMoneyConfirmScreen extends AppCompatActivity implements View.OnClickListener {
    public static ReceiveMoneyConfirmScreen receivemoneyconfirmationscreenC;
    // ImageView imgBack;
    TextView pinText,btnConfirm,btnCancel,tvFinger;
    public static TextView tvrecCurrency,tvrate,tvProvider,tvMobile,tvName,tvConfCode,tvCurrency,tvTransAmounts,tvAmountPaid,tvAmountCharged,tvFee,tax_label,tax_r,vat_label,vat_r;
    EditText etPin;
    double finalamount;
    LinearLayout tax_label_layout,vat_label_layout,pinLinear,ll_resendOtp,ll_successPage,linera_all;
    CardView cardBearFee;
    ImageView icPin;
    private LinearLayout ll_otp;
    public static  String emailreceiver,mobilenumberreceiver,lastnamereceiver,ownernamereceiver,email,mobilenumber,lastname,ownername,descurrencycode,srccurrencycode,descurrencyname,srccurrencyname,currencySymbolreceiver,selectClickType="",desWalletOwnerCode_from_currency="",transactionType,currencySymbolsender;
    private EditText et_otp,et_mpin;
    private String otpStr="",mpinStr;
    String languageToUse = "";
    boolean  isPasswordVisible;

    MyApplication applicationComponentClass;
    Double tax_financial_double = 0.0, amountstr_double = 0.0,tax_financialnewDouble=0.0, fees_amount_double = 0.0, totalAmount_double = 0.0;
    String tax_financialnew,tax_financial = "",tax_financialtypename, fees_amount, totalAmount_str, receivermobileNumberStr,receiverlastnameStr,receivernameStr = "";
    public static JSONObject receiptJson=new JSONObject();

    private String serviceCode_from_serviceCategory,serviceCategoryCode_from_serviceCategory,serviceProviderCode_from_serviceCategory;
    private long mLastClickTime = 0;

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
            setContentView(R.layout.activity_receive_money_confirmation_screen);
            receivemoneyconfirmationscreenC = this;
            //setBackMenu();
            getIds();
        } finally {

        }
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
//
//    private void setBackMenu() {
//        imgBack = findViewById(R.id.imgBack);
//        imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSupportNavigateUp();
//            }
//        });
//    }


    private void getIds() {
        tvProvider = findViewById(R.id.tvProvider);
        tvrate=findViewById(R.id.tvrate);
        tvMobile = findViewById(R.id.tvMobile);
        tvName = findViewById(R.id.tvName);
        et_otp = (EditText) findViewById(R.id.et_otp);
        et_mpin = (EditText) findViewById(R.id.et_mpin);
        ll_successPage=findViewById(R.id.ll_successPage);
        linera_all=findViewById(R.id.linera_all);
        ll_resendOtp = (LinearLayout) findViewById(R.id.ll_resendOtp);
        ll_resendOtp.setOnClickListener(this);
        //  tvConfCode = findViewById(R.id.tvConfCode);
        tvCurrency = findViewById(R.id.tvCurrency);
        tvTransAmounts = findViewById(R.id.tvTransAmount);
        tvAmountPaid = findViewById(R.id.tvAmountPaid);
        tvAmountCharged = findViewById(R.id.tvAmountCharged);
        tvFee = findViewById(R.id.tvFee);
        //icPin = findViewById(R.id.icPin);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);
        tvFinger=findViewById(R.id.tvFinger);
        cardBearFee = findViewById(R.id.cardBearFee);
        cardBearFee.setVisibility(View.GONE);
        pinLinear=findViewById(R.id.pinLinear);
        tax_r=findViewById(R.id.tax_r);
        vat_r=findViewById(R.id.vat_r);
        tax_label=findViewById(R.id.tax_label);
        vat_label=findViewById(R.id.vat_label);
        tax_label_layout=findViewById(R.id.tax_label_layout);
        vat_label_layout=findViewById(R.id.vat_label_layout);
        btnConfirm.setOnClickListener(this);
        ll_otp=findViewById(R.id.ll_otp);
        pinText=findViewById(R.id.pinText);
        tvrecCurrency=findViewById(R.id.tvrecCurrency);
        tvrecCurrency.setText(ReceiveMoneyDetailScreen.spinner_receiverCurrency.getText().toString());
       // tvProvider.setText(ReceiveMoney.serviceProv;ider);
       tvMobile.setText(ReceiveMoneyDetailScreen.et_destination_mobileNumber.getText().toString());
        tvName.setText(ReceiveMoneyDetailScreen.et_destination_firstName.getText().toString()+" "+ReceiveMoneyDetailScreen.et_destination_lastName.getText().toString());
        //  tvConfCode.setText(ReceiveMoney.mobileNo);
        tvCurrency.setText(ReceiveMoneyDetailScreen.fromCurrency);
        tvTransAmounts.setText(ReceiveMoneyDetailScreen.fromCurrencySymbol+" "+MyApplication.addDecimal(ReceiveMoneyDetailScreen.amount));
        tvAmountPaid.setText(ReceiveMoneyDetailScreen.toCurrencySymbol+" "+MyApplication.addDecimal(ReceiveMoneyDetailScreen.currencyValue));
        tvFee.setText(ReceiveMoneyDetailScreen.fromCurrencySymbol+" "+MyApplication.addDecimal(ReceiveMoneyDetailScreen.fee));
        tvrate.setText(MyApplication.addDecimalfive(ReceiveMoneyDetailScreen.rate));
//        finalamount=Double.parseDouble(ReceiveMoney.fee)+Double.parseDouble(MyApplication.getSaveString("AMOUNTReceiveMoney",receivemoneyconfirmationscreenC));

        btnConfirm.setText(getString(R.string.otp_verification));
        selectClickType="select_otp";
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DecimalFormat df = new DecimalFormat("0.00",symbols);
        if(ReceiveMoneyDetailScreen.taxConfigurationList!=null){
            if(ReceiveMoneyDetailScreen.taxConfigurationList.length()==1){
                tax_label_layout.setVisibility(View.VISIBLE);
                tax_label.setText(MyApplication.getTaxString(ReceiveMoneyDetailScreen.taxConfigurationList.optJSONObject(0).optString("taxTypeName"))+" ");
                tax_r.setText(ReceiveMoneyDetailScreen.fromCurrencySymbol+" "+MyApplication.addDecimal(String.valueOf(ReceiveMoneyDetailScreen.taxConfigurationList.optJSONObject(0).optDouble("value"))));
               /*finalamount=Double.parseDouble(ReceiveMoneyDetailScreen.fee)+Double.parseDouble(MyApplication.getSaveString("AMOUNTReceiveMoney",receivemoneyconfirmationscreenC))+
                       Double.parseDouble(ReceiveMoneyDetailScreen.taxConfigurationList.optJSONObject(0).optString("value"));
*/            }
            if(ReceiveMoneyDetailScreen.taxConfigurationList.length()==2){
                tax_label_layout.setVisibility(View.VISIBLE);
                tax_label.setText(MyApplication.getTaxString(ReceiveMoneyDetailScreen.taxConfigurationList.optJSONObject(0).optString("taxTypeName"))+" ");
                tax_r.setText(ReceiveMoneyDetailScreen.fromCurrencySymbol+" "+MyApplication.addDecimal(String.valueOf(ReceiveMoneyDetailScreen.taxConfigurationList.optJSONObject(0).optDouble("value"))));

                vat_label_layout.setVisibility(View.VISIBLE);
                vat_label.setText(MyApplication.getTaxString(ReceiveMoneyDetailScreen.taxConfigurationList.optJSONObject(1).optString("taxTypeName"))+" ");
                vat_r.setText(ReceiveMoneyDetailScreen.fromCurrencySymbol+" "+MyApplication.addDecimal(String.valueOf(ReceiveMoneyDetailScreen.taxConfigurationList.optJSONObject(1).optDouble("value"))));
              //  finalamount=Double.parseDouble(ReceiveMoneyDetailScreen.fee)+Double.parseDouble(MyApplication.getSaveString("AMOUNTReceiveMoney",receivemoneyconfirmationscreenC))+Double.parseDouble(ReceiveMoneyDetailScreen.taxConfigurationList.optJSONObject(0).optString("value"))+Double.parseDouble(ReceiveMoneyDetailScreen.taxConfigurationList.optJSONObject(1).optString("value"));
            }
        }
        fees_amount_double = Double.parseDouble(ReceiveMoneyDetailScreen.fee);
        amountstr_double = Double.parseDouble(ReceiveMoneyDetailScreen.amount);
        tax_financialnewDouble = Double.parseDouble(String.valueOf(ReceiveMoneyDetailScreen.taxtest));



            totalAmount_double = tax_financialnewDouble + amountstr_double + fees_amount_double;
        totalAmount_str = String.valueOf(totalAmount_double);

        tvAmountCharged.setText(ReceiveMoneyDetailScreen.fromCurrencySymbol+" "+MyApplication.addDecimal(ReceiveMoneyDetailScreen.amount));


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
                    MyApplication.hideKeyboard(ReceiveMoneyConfirmScreen.this);            }
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


        //  tvAmountCharged.setText(ReceiveMoneyDetailScreen.toCurrencySymbol+" "+(ReceiveMoneyDetailScreen.currencyValue));

        TextView tvFinger =findViewById(R.id.tvFinger);
        if(MyApplication.setProtection!=null && !MyApplication.setProtection.isEmpty()) {
            if (MyApplication.setProtection.equalsIgnoreCase("Activate")) {
                // tvFinger.setVisibility(View.VISIBLE);
            } else {
                // tvFinger.setVisibility(View.GONE);
            }
        }else{
            // tvFinger.setVisibility(View.VISIBLE);
        }
        tvFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.biometricAuth(receivemoneyconfirmationscreenC, new BioMetric_Responce_Handler() {
                    @Override
                    public void success(String success) {
                        try {
                            btnConfirm.setVisibility(View.GONE);
                            String encryptionDatanew = AESEncryption.getAESEncryption(MyApplication.getSaveString("pin",MyApplication.appInstance).toString().trim());
                           // ReceiveMoneyDetailScreen.dataToSend.put( "pin",encryptionDatanew);

                            mpin_final_api();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(String failure) {
                        MyApplication.showToast(receivemoneyconfirmationscreenC,failure);
                    }
                });
            }
        });



        setOnCLickListener();
        service_Provider_api();

    }



    private void setOnCLickListener() {
        btnConfirm.setOnClickListener(receivemoneyconfirmationscreenC);
        btnCancel.setOnClickListener(receivemoneyconfirmationscreenC);

    }
    HiddenPassTransformationMethod hiddenPassTransformationMethod=new HiddenPassTransformationMethod();
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {

            case R.id.ll_resendOtp: {


                if (new InternetCheck().isConnected(ReceiveMoneyConfirmScreen.this)) {

                    MyApplication.showloader(ReceiveMoneyConfirmScreen.this, getString(R.string.please_wait));

                    otp_generate_api();

                } else {
                    Toast.makeText(ReceiveMoneyConfirmScreen.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                }
            }
            break;

            case R.id.btnConfirm:
            {


                if (selectClickType.equalsIgnoreCase("select_otp")) {

                    if (validation_otp_detail()) {
                        if (new InternetCheck().isConnected(ReceiveMoneyConfirmScreen.this)) {
                            MyApplication.showloader(ReceiveMoneyConfirmScreen.this, getString(R.string.please_wait));
                            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                                return;
                            }
                            mLastClickTime = SystemClock.elapsedRealtime();
                            otp_verify_api();

                        } else {
                            Toast.makeText(ReceiveMoneyConfirmScreen.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                        }
                    }
                } else if (selectClickType.equalsIgnoreCase("select_subscriber_mpin")) {
                    if (validation_mpin_detail()) {
                        if (new InternetCheck().isConnected(ReceiveMoneyConfirmScreen.this)) {
                            MyApplication.showloader(ReceiveMoneyConfirmScreen.this, getString(R.string.please_wait));


                            api_mpin_subscriber();

                        } else {
                            Toast.makeText(ReceiveMoneyConfirmScreen.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                        }
                    }
                } else if (selectClickType.equalsIgnoreCase("select_mpin")) {

                    if(pinLinear.getVisibility()==View.VISIBLE){
                        if (validation_mpin_detail()) {

                            if (new InternetCheck().isConnected(ReceiveMoneyConfirmScreen.this)) {

                                MyApplication.showloader(ReceiveMoneyConfirmScreen.this, getString(R.string.please_wait));

                                mpin_final_api();

                            } else {
                                Toast.makeText(ReceiveMoneyConfirmScreen.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                            }
                        }
                    }else {

                        MyApplication.biometricAuth(ReceiveMoneyConfirmScreen.this, new BioMetric_Responce_Handler() {
                            @Override
                            public void success(String success) {
                                try {

                                    //  String encryptionDatanew = AESEncryption.getAESEncryption(MyApplication.getSaveString("pin",MyApplication.appInstance).toString().trim());
                                    mpinStr = MyApplication.getSaveString("pin", MyApplication.appInstance);

                                    if (new InternetCheck().isConnected(ReceiveMoneyConfirmScreen.this)) {

                                        MyApplication.showloader(ReceiveMoneyConfirmScreen.this, getString(R.string.please_wait));

                                        mpin_final_api();

                                    } else {
                                        Toast.makeText(ReceiveMoneyConfirmScreen.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failure(String failure) {
                              //  MyApplication.showToast(ReceiveMoneyConfirmScreen.this, failure);

                                pinLinear.setVisibility(View.VISIBLE);


                            }

                        });
                    }



/*
                    if (validation_mpin_detail()) {
                        if (new InternetCheck().isConnected(ReceiveMoneyConfirmScreen.this)) {
                            MyApplication.showloader(ReceiveMoneyConfirmScreen.this, getString(R.string.please_wait));


                            mpin_final_api();

                        } else {
                            Toast.makeText(ReceiveMoneyConfirmScreen.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                        }
                    }
*/


                }
            }
            break;
                    case R.id.btnCancel:
                        finish();
                        break;
                }

        }

    private void mpin_final_api() {

        try {

            JSONObject dataObjectrequest = new JSONObject();

            dataObjectrequest.put("srcWalletOwnerCode",ReceiveMoneyDetailScreen.receiverCode);
            dataObjectrequest.put("desWalletOwnerCode", MyApplication.getSaveString("walletOwnerCode", ReceiveMoneyConfirmScreen.this));
            dataObjectrequest.put("srcCurrencyCode", ReceiveMoneyDetailScreen.fromCurrencyCode);
            dataObjectrequest.put("desCurrencyCode", ReceiveMoneyDetailScreen.toCurrencyCode);
            dataObjectrequest.put("value", ReceiveMoneyDetailScreen.amount);
            dataObjectrequest.put("transactionType", "113092");
            String encryptionDatanew = AESEncryption.getAESEncryption(mpinStr);

            dataObjectrequest.put("pin", encryptionDatanew);
            dataObjectrequest.put("serviceCode", serviceCode_from_serviceCategory);
            dataObjectrequest.put("serviceCategoryCode", serviceCategoryCode_from_serviceCategory);
            dataObjectrequest.put("serviceProviderCode", serviceProviderCode_from_serviceCategory);
            dataObjectrequest.put("channelTypeCode", "100002");

            System.out.println("ReceiveMoneyConfirmScreen REQUEST================"+dataObjectrequest.toString());
            String requestNo=AESEncryption.getAESEncryption(dataObjectrequest.toString());
            JSONObject jsonObjectA=null;
            try{
                jsonObjectA=new JSONObject();
                jsonObjectA.put("request",requestNo);

             //   jsonObjectA.put("request","d4690aa3afc5377668fe577fa0944565a862f8542abf7071ca1d7781ee0ff817474da9b286ce89a94f5fc45f5b4d34a70b43ff2c4de02f3814754875210b427b1741d890bb18d28ed86b9016dbbb1cd36f9e15d25fb7fb1e4949d00f3f078f288ccf963297dae6f7764dff60a9dbf710f6a8420ee4e9c1e3fd9935a54d5c9486d37574c7bdc0715b613ae717bb1c051c90d4ba5d364277996d92b1cab1969e012669be90dce63f31d25a7506f7022b1df9389a6cdc8dbd4f186dbd2b27b1dc50fa51d3fa19e148fd9fa1f451124c82dfc1c4d4cb656645d9fd5591f3ab534afb2f290fd64196f6c2618acda345d503d62f4a971eddc0f24a01b65653288aa8b792eee2b1fe4a07518e53b30790ff265083d7e87d81822e23e47a1ce60ed61ae82c0b4386469efdc1bbf03479f6f0f3822843efbd21a5c82633c9a7fed9bbf9f1");
            }catch (Exception e){

            }

            API.POST_CASHOUT_MPIN("ewallet/api/v1/walletTransfer/merchantCashOut", jsonObjectA, languageToUse, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {


                    try {

                        receiptJson=jsonObject;

                        // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"116204\",\"requestTime\":\"Wed Oct 20 19:51:47 IST 2021\",\"responseTime\":\"Wed Oct 20 19:51:47 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletTransfer\":{\"code\":\"116204\",\"srcWalletCode\":\"1000024941\",\"desWalletCode\":\"1000022471\",\"srcWalletOwnerCode\":\"1000002606\",\"desWalletOwnerCode\":\"1000002488\",\"srcWalletTypeCode\":\"100008\",\"desWalletTypeCode\":\"100008\",\"srcCurrencyCode\":\"100062\",\"desCurrencyCode\":\"100062\",\"srcCurrencyName\":\"GNF\",\"desCurrencyName\":\"GNF\",\"srcCurrencySymbol\":\"Fr\",\"desCurrencySymbol\":\"Fr\",\"value\":2130,\"createdBy\":\"101917\",\"creationDate\":\"2021-10-20 19:51:47\",\"fee\":1000,\"finalAmount\":1000,\"srcWalletOwner\":{\"id\":110500,\"code\":\"1000002606\",\"walletOwnerCategoryCode\":\"100000\",\"ownerName\":\"TATASnegal\",\"mobileNumber\":\"8888888882\",\"businessTypeCode\":\"100001\",\"businessTypeName\":\"Telecom\",\"idProofNumber\":\"44444444444\",\"email\":\"kundan.kumar@esteltelecom.com\",\"status\":\"Active\",\"state\":\"Approved\",\"stage\":\"Document\",\"idProofTypeCode\":\"100004\",\"idProofTypeName\":\"MILITARY ID CARD\",\"notificationLanguage\":\"en\",\"notificationTypeCode\":\"100000\",\"notificationName\":\"EMAIL\",\"registerCountryCode\":\"100092\",\"registerCountryName\":\"Guinea\",\"createdBy\":\"100250\",\"modifiedBy\":\"100308\",\"creationDate\":\"2021-10-01T09:01:15.968+0530\",\"modificationDate\":\"2021-10-01T09:10:25.037+0530\",\"walletExists\":true,\"profileTypeCode\":\"100000\",\"profileTypeName\":\"tier1\",\"walletCurrencyList\":[\"100014\",\"100013\",\"100012\",\"100007\",\"100010\",\"100008\",\"100005\",\"100002\",\"100001\",\"100003\",\"100069\",\"100062\",\"100004\",\"100000\",\"100028\",\"100027\",\"100026\",\"100024\",\"100021\",\"100020\",\"100019\",\"100017\",\"100015\",\"100018\",\"100058\"],\"walletOwnerCatName\":\"Institute\",\"requestedSource\":\"ADMIN\",\"regesterCountryDialCode\":\"+224\",\"walletOwnerCode\":\"1000002606\"},\"desWalletOwner\":{\"id\":110382,\"code\":\"1000002488\",\"walletOwnerCategoryCode\":\"100010\",\"ownerName\":\"Kundan\",\"mobileNumber\":\"118110111\",\"idProofNumber\":\"vc12345\",\"email\":\"kundan.kumar@esteltelecom.com\",\"status\":\"Active\",\"state\":\"Approved\",\"stage\":\"Document\",\"idProofTypeCode\":\"100006\",\"idProofTypeName\":\"OTHER\",\"idExpiryDate\":\"2021-09-29\",\"notificationLanguage\":\"en\",\"notificationTypeCode\":\"100000\",\"notificationName\":\"EMAIL\",\"gender\":\"M\",\"dateOfBirth\":\"1960-01-26\",\"lastName\":\"New\",\"issuingCountryCode\":\"100092\",\"issuingCountryName\":\"Guinea\",\"registerCountryCode\":\"100092\",\"registerCountryName\":\"Guinea\",\"createdBy\":\"100375\",\"modifiedBy\":\"100322\",\"creationDate\":\"2021-09-16T17:08:49.796+0530\",\"modificationDate\":\"2021-09-16T17:10:17.009+0530\",\"walletExists\":true,\"profileTypeCode\":\"100001\",\"profileTypeName\":\"tier2\",\"walletOwnerCatName\":\"Subscriber\",\"occupationTypeCode\":\"100002\",\"occupationTypeName\":\"Others\",\"requestedSource\":\"ADMIN\",\"regesterCountryDialCode\":\"+224\",\"issuingCountryDialCode\":\"+224\",\"walletOwnerCode\":\"1000002488\"},\"taxConfigurationList\":[{\"taxTypeCode\":\"100133\",\"taxTypeName\":\"Financial Tax\",\"value\":130,\"taxAvailBy\":\"Fee Amount\"}],\"transactionType\":\"CASH-IN\"}}");

                        String resultCode = jsonObject.getString("resultCode");
                        String resultDescription = jsonObject.getString("resultDescription");


                        System.out.println("get response"+resultDescription);
                        if (resultCode.equalsIgnoreCase("0")) {
                            ll_successPage.setVisibility(View.VISIBLE);
                            linera_all.setVisibility(View.GONE);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    ll_successPage.setVisibility(View.GONE);
                                    linera_all.setVisibility(View.VISIBLE);
                                }
                            }, 2000);

                            JSONObject walletTransfer = jsonObject.getJSONObject("walletTransfer");
                            JSONObject walletTransfersrcWalletOwner= walletTransfer.getJSONObject("srcWalletOwner");
                            JSONObject walletTransferdesWalletOwner= walletTransfer.getJSONObject("desWalletOwner");


                                System.out.println("get value"+walletTransfersrcWalletOwner.getString("email"));




                                Intent intent=new Intent(ReceiveMoneyConfirmScreen.this, ReceiveMoneyReceiptScreen.class);
                               intent.putExtra("email",walletTransfersrcWalletOwner.getString("email"));
                            intent.putExtra("mobileNumber",walletTransfersrcWalletOwner.getString("mobileNumber"));
                            intent.putExtra("ownerName",walletTransfersrcWalletOwner.getString("ownerName"));
                            intent.putExtra("lastName",walletTransfersrcWalletOwner.getString("lastName"));
                            intent.putExtra("creationDate",walletTransfer.getString("creationDate"));

                            intent.putExtra("emailrec",walletTransferdesWalletOwner.getString("email"));
                            intent.putExtra("mobileNumberrec",walletTransferdesWalletOwner.getString("mobileNumber"));
                            intent.putExtra("ownerNamerec",walletTransferdesWalletOwner.getString("ownerName"));
                            intent.putExtra("lastNamerec",walletTransferdesWalletOwner.getString("lastName"));

                            startActivity(intent);
                                MyApplication.hideLoader();

                            }


                         else {
                            MyApplication.hideLoader();

                            Toast.makeText(ReceiveMoneyConfirmScreen.this, resultDescription, Toast.LENGTH_LONG).show();
                            //  finish();
                        }


                    } catch (Exception e) {
                        MyApplication.hideLoader();

                        Toast.makeText(ReceiveMoneyConfirmScreen.this, e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }


                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(ReceiveMoneyConfirmScreen.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    }
                    else

                        Toast.makeText(ReceiveMoneyConfirmScreen.this, aFalse, Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (Exception e)
        {
            Toast.makeText(ReceiveMoneyConfirmScreen.this,e.toString(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }



    private void api_mpin_subscriber() {
        try{

            JSONObject jsonObject=new JSONObject();

            String encryptionDatanew = AESEncryption.getAESEncryption(mpinStr);
            jsonObject.put("pin", encryptionDatanew.toLowerCase(Locale.ROOT));
            jsonObject.put("mobileNumber",tvMobile.getText().toString());


            API.POST_CASHOUT_MPIN("ewallet/api/v1/walletOwnerUser/verifyMPin",jsonObject,languageToUse,new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {

                        //JSONObject jsonObject = new JSONObject("");

                        if (jsonObject.has("error")) {

                            String error = jsonObject.getString("error");
                            String error_message = jsonObject.getString("error_message");

                            Toast.makeText(ReceiveMoneyConfirmScreen.this, error_message, Toast.LENGTH_LONG).show();

                            if(error.equalsIgnoreCase("1251")) {

                                Intent i = new Intent(ReceiveMoneyConfirmScreen.this, VerifyLoginAccountScreen.class);
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
                                pinText.setText(getString(R.string.pin_capital));
                                btnConfirm.setText(getString(R.string.Submit));
                                ll_otp.setVisibility(View.GONE);
                                ll_resendOtp.setVisibility(View.GONE);
                                pinLinear.setVisibility(View.VISIBLE);
                                tvFinger.setVisibility(View.VISIBLE);


                            }

                            else {
                                Toast.makeText(ReceiveMoneyConfirmScreen.this, resultDescription, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    catch (Exception e)
                    {
                        Toast.makeText(ReceiveMoneyConfirmScreen.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(ReceiveMoneyConfirmScreen.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(ReceiveMoneyConfirmScreen.this, aFalse, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (Exception e){

            MyApplication.hideLoader();
            MyApplication.showToast(ReceiveMoneyConfirmScreen.this,e.toString());
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


    private void otp_verify_api() {
        try{

            JSONObject jsonObject=new JSONObject();

            jsonObject.put("transTypeCode","113092");      // Temporary Hard Code acording to Praveen
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

                            Toast.makeText(ReceiveMoneyConfirmScreen.this, error_message, Toast.LENGTH_LONG).show();

                            if(error.equalsIgnoreCase("1251")) {

                                Intent i = new Intent(ReceiveMoneyConfirmScreen.this, VerifyLoginAccountScreen.class);
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
                                btnConfirm.setText(getString(R.string.verify_subscriber_pin));
                                pinLinear.setVisibility(View.VISIBLE);
                                ll_otp.setVisibility(View.GONE);

                                ll_resendOtp.setVisibility(View.GONE);


                            }

                            else {
                                Toast.makeText(ReceiveMoneyConfirmScreen.this, resultDescription, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    catch (Exception e)
                    {
                        Toast.makeText(ReceiveMoneyConfirmScreen.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(ReceiveMoneyConfirmScreen.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(ReceiveMoneyConfirmScreen.this, aFalse, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (Exception e){

            MyApplication.hideLoader();
            MyApplication.showToast(ReceiveMoneyConfirmScreen.this,e.toString());
        }

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



    private void otp_generate_api() {
        try{

            JSONObject jsonObject=new JSONObject();



            jsonObject.put("transTypeCode","113092");
            jsonObject.put("subscriberWalletOwnerCode",ReceiveMoneyDetailScreen.receiverCode);




            API.POST_GET_OTP("ewallet/api/v1/otp",jsonObject,new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {


                    try {

                        //  JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1771883\",\"requestTime\":\"Mon Oct 18 18:05:40 IST 2021\",\"responseTime\":\"Mon Oct 18 18:05:40 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\"}");

                        if (jsonObject.has("error")) {
                            MyApplication.hideLoader();


                            String error = jsonObject.getString("error");
                            String error_message = jsonObject.getString("error_message");

                            Toast.makeText(ReceiveMoneyConfirmScreen.this, error_message, Toast.LENGTH_LONG).show();

                            if(error.equalsIgnoreCase("1251"))
                            {

                                Intent i = new Intent(ReceiveMoneyConfirmScreen.this, VerifyLoginAccountScreen.class);
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


                                Toast.makeText(ReceiveMoneyConfirmScreen.this, getString(R.string.otp_has_send_sucessfully_subscriber_register), Toast.LENGTH_LONG).show();

                                btnConfirm.setText(getString(R.string.otp_verify));
                                selectClickType="select_otp";




                                ll_resendOtp.setVisibility(View.VISIBLE);
                                MyApplication.hideLoader();


                            }

                            else {
                                MyApplication.hideLoader();

                                Toast.makeText(ReceiveMoneyConfirmScreen.this, resultDescription, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    catch (Exception e)
                    {
                        MyApplication.hideLoader();

                        Toast.makeText(ReceiveMoneyConfirmScreen.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }


                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(ReceiveMoneyConfirmScreen.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    }

                    else {

                        Toast.makeText(ReceiveMoneyConfirmScreen.this, aFalse, Toast.LENGTH_SHORT).show();
                    }

                    //  MyApplication.showToast(LoginPin.this,aFalse);

                }
            });

        }catch (Exception e){

            MyApplication.hideLoader();

            MyApplication.showToast(ReceiveMoneyConfirmScreen.this,e.toString());

        }

    }

    public static JSONArray taxConfigList;
    public void callPostAPI() {
        MyApplication.showloader(receivemoneyconfirmationscreenC, getString(R.string.pleasewait));

        //  System.out.println("ReceiveMoney Request :"+ReceiveMoneyDetailScreen.dataToSend.toString());


        try {

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("srcWalletOwnerCode", MyApplication.getSaveString("walletOwnerCode", ReceiveMoneyConfirmScreen.this));
            jsonObject.put("desWalletOwnerCode", ReceiveMoneyDetailScreen.receiverCode);
            jsonObject.put("srcCurrencyCode", "100062");
            jsonObject.put("desCurrencyCode", "100062");
            jsonObject.put("value", ReceiveMoneyDetailScreen.amount);
            String encryptionDatanew = AESEncryption.getAESEncryption(mpinStr);
            jsonObject.put("pin", encryptionDatanew);
            jsonObject.put("transactionType", "100000");         // Hard Code according  to Deepak
            jsonObject.put("channelTypeCode", "100000");           // Hard Code according  to Deepak
            jsonObject.put("serviceCode", serviceCode_from_serviceCategory);
            jsonObject.put("serviceCategoryCode", serviceCategoryCode_from_serviceCategory);  // Hard Code according  to Deepak
            jsonObject.put("serviceProviderCode", serviceProviderCode_from_serviceCategory);  // Hard Code according  to Deepak


            System.out.println("Receive money REQUEST================" + jsonObject.toString());
           /* String requestNo = AESEncryption.getAESEncryption(jsonObject.toString());
            JSONObject jsonObjectA = null;
            try {
                jsonObjectA = new JSONObject();
                jsonObjectA.put("request", requestNo);
            } catch (Exception e) {

            }*/
/*
            API.POST_REQEST_WH_NEW("ewallet/api/v1/walletTransfer/merchantCashOut", jsonObjectA,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            if (jsonObject.optString("resultCode").equalsIgnoreCase("0")) {
                                MyApplication.showToast(receivemoneyconfirmationscreenC, jsonObject.optString("resultDescription"));
                                receiptJson = jsonObject;
                           */
/* JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("remittance");
                            if(jsonObjectAmountDetails.has("taxConfigurationList")) {
                                taxConfigList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
                            }else{
                                taxConfigList=null;
                            }*//*

                                btnConfirm.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(receivemoneyconfirmationscreenC, TransactionSuccessScreen.class);
                                intent.putExtra("ReceiveMoney", "ReceiveMoney");
                                startActivity(intent);
                                // {"transactionId":"2432","requestTime":"Fri Dec 25 05:51:11 IST 2020","responseTime":"Fri Dec 25 05:51:12 IST 2020","resultCode":"0","resultDescription":"Transaction Successful","remittance":{"code":"1000000327","walletOwnerCode":"1000000750","transactionType":"SEND REMITTANCE","senderCode":"1000000750","receiverCode":"AGNT202012","fromCurrencyCode":"100069","fromCurrencyName":"INR","fromCurrencySymbol":"₹","toCurrencyCode":"100069","toCurrencyName":"INR","toCurrencySymbol":"₹","amount":200,"amountToPaid":200,"fee":0,"tax":"0.0","conversionRate":0,"confirmationCode":"MMZJBJHYAAX","transactionReferenceNo":"1000000327","transactionDateTime":"2020-12-25 05:51:12","sender":{"id":1887,"code":"1000000750","firstName":"mahi","lastName":"kumar","mobileNumber":"88022255363","gender":"M","idProofTypeCode":"100000","idProofTypeName":"Passport","idProofNumber":"3333","idExpiryDate":"2025-12-20","dateOfBirth":"1960-01-05","email":"infomahendra2009@gmail.com","issuingCountryCode":"100001","issuingCountryName":"Albania","status":"Active","creationDate":"2020-12-14 11:17:33","registerCountryCode":"100102","registerCountryName":"India","ownerName":"mahi"},"receiver":{"id":1895,"code":"AGNT202012","firstName":"Rajesh","lastName":"Kumar","mobileNumber":"9821184601","gender":"M","idProofTypeCode":"100000","idProofTypeName":"Passport","idProofNumber":"DFZ123456","idExpiryDate":"2030-09-08","dateOfBirth":"1989-01-05","email":"abhishek.kumar2@esteltelecom.com","issuingCountryCode":"100102","issuingCountryName":"India","status":"Active","creationDate":"2020-12-14 14:00:23","createdBy":"100250","modificationDate":"2020-12-14 14:00:56","modifiedBy":"100250","registerCountryCode":"100102","registerCountryName":"India","ownerName":"Rajesh"}}}
                            } else {
                                btnConfirm.setVisibility(View.VISIBLE);
                                MyApplication.showToast(receivemoneyconfirmationscreenC, jsonObject.optString("resultDescription"));
                            }
                        }

                        @Override
                        public void failure(String aFalse) {
                            MyApplication.hideLoader();
                            btnConfirm.setVisibility(View.VISIBLE);
                        }
                    });
*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
        private void service_Provider_api() {

        // Hard Code Final Deepak

        API.GET_CASHOUT_DETAILS("ewallet/api/v1/serviceProvider/serviceCategory?serviceCode=100015&serviceCategoryCode=REMON&status=Y", languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {


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

                     //   agent_details_api_walletownerUser();


                    } else {
                        MyApplication.hideLoader();

                        Toast.makeText(ReceiveMoneyConfirmScreen.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                } catch (Exception e) {
                    MyApplication.hideLoader();

                    Toast.makeText(ReceiveMoneyConfirmScreen.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(ReceiveMoneyConfirmScreen.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

}
