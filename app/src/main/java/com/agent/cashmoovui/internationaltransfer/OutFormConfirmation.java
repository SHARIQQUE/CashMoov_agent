package com.agent.cashmoovui.internationaltransfer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.agent.cashmoovui.LogoutAppCompactActivity;

import com.agent.cashmoovui.HiddenPassTransformationMethod;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;

import com.agent.cashmoovui.activity.TransactionSuccessScreen;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.apiCalls.BioMetric_Responce_Handler;
import com.agent.cashmoovui.set_pin.AESEncryption;
import com.suke.widget.SwitchButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class OutFormConfirmation extends LogoutAppCompactActivity implements View.OnClickListener {
    public static OutFormConfirmation tosubscriberconfirmscreenC;
    // ImageView imgBack;
    Button btnConfirm,btnCancel;
    LinearLayout tax_label_layout,vat_label_layout,bearLay,pinLinear;
    public static TextView tvrate,tvProvider,tvMobile,tvName,tvConfCode,tvCurrency,tvTransAmount,tvAmountPaid,tvAmountCharged,tvFee,tax_label,tax_r,vat_label,vat_r;
    EditText etPin;
    double finalamount;
    ImageView icPin;
    SwitchButton switch_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_screen);
        tosubscriberconfirmscreenC=this;
        //setBackMenu();
        getIds();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //  private void setBackMenu() {
    //       imgBack = findViewById(R.id.imgBack);
    //      imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSupportNavigateUp();
//            }
//        });
//    }

    private void getIds() {
        tvrate=findViewById(R.id.tvrate);
        tvrate.setText(MyApplication.addDecimalfive(Outform.rate));
        pinLinear=findViewById(R.id.pinLinear);
        bearLay=findViewById(R.id.bearLay);
        bearLay.setVisibility(View.GONE);
        tvProvider = findViewById(R.id.tvProvider);
        tvMobile = findViewById(R.id.tvMobile);
        tvName = findViewById(R.id.tvName);
        // tvConfCode = findViewById(R.id.tvConfCode);
        tvCurrency = findViewById(R.id.tvCurrency);
        tvTransAmount = findViewById(R.id.tvTransAmount);
        tvAmountPaid = findViewById(R.id.tvAmountPaid);
        tvAmountCharged = findViewById(R.id.tvAmountCharged);
        tvFee = findViewById(R.id.tvFee);
        etPin = findViewById(R.id.etPin);
        etPin.setTransformationMethod(hiddenPassTransformationMethod);
        icPin = findViewById(R.id.icPin);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);

        tax_r=findViewById(R.id.tax_r);
        vat_r=findViewById(R.id.vat_r);
        tax_label=findViewById(R.id.tax_label);
        vat_label=findViewById(R.id.vat_label);
        tax_label_layout=findViewById(R.id.tax_label_layout);
        vat_label_layout=findViewById(R.id.vat_label_layout);

        tvProvider.setText(Outform.serviceProvider);
        tvMobile.setText(Outform.mobileNo);
        tvName.setText(Outform.ownerName+" "+Outform.lastName);
        //  tvConfCode.setText(Outform.mobileNo);
        tvCurrency.setText(Outform.currency);
        tvTransAmount.setText(Outform.currencySymbol+" "+MyApplication.addDecimal(Outform.etAmount.getText().toString().replace(",","")));
        tvAmountPaid.setText(Outform.countrycode+" "+Outform.currencyValue);

        tvFee.setText(Outform.currencySymbol+" "+Outform.fee);

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
                    MyApplication.hideKeyboard(tosubscriberconfirmscreenC);            }
        });


        TextView tvFinger =findViewById(R.id.tvFinger);
        if(MyApplication.setProtection!=null && !MyApplication.setProtection.isEmpty()) {
            if (MyApplication.setProtection.equalsIgnoreCase("Activate")) {
                tvFinger.setVisibility(View.VISIBLE);
            } else {
                tvFinger.setVisibility(View.GONE);
            }
        }else{
            tvFinger.setVisibility(View.GONE);
        }
        tvFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.biometricAuth(tosubscriberconfirmscreenC, new BioMetric_Responce_Handler() {
                    @Override
                    public void success(String success) {
                        try {
                            etPin.setClickable(false);
                            btnConfirm.setVisibility(View.GONE);
                            String encryptionDatanew = AESEncryption.getAESEncryption(MyApplication.getSaveString("pin",MyApplication.appInstance).toString().trim());
                            Outform.jsonObjectNew.put( "pin",encryptionDatanew);

                            if(switch_button.isChecked()) {
                                dataToSendBear.put("pin", encryptionDatanew);
                            }
                            callPostAPI();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(String failure) {
                        MyApplication.showToast(tosubscriberconfirmscreenC,failure);
                    }
                });
            }
        });


        switch_button=findViewById(R.id.switch_button);
        switch_button.setVisibility(View.GONE);
        switch_button.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
                if(isChecked){
                    callApiAmountDetailsIbear();
                }else{
                    getIds();
                }
            }
        });

        //finalamount=Double.parseDouble(Outform.fee)+Double.parseDouble(Outform.etAmount.getText().toString().replace(",",""));

        finalamount=Double.parseDouble(Outform.fee)+Double.parseDouble(Outform.etAmount.getText().toString().replace(",",""));
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DecimalFormat df = new DecimalFormat("0.00",symbols);
        if(Outform.taxConfigurationList!=null){


            if(Outform.taxConfigurationList.length()==1){
                tax_label_layout.setVisibility(View.VISIBLE);
                tax_label.setText(MyApplication.getTaxString(Outform.taxConfigurationList.optJSONObject(0).optString("taxTypeName")));
                tax_r.setText(Outform.currencySymbol+" "+MyApplication.addDecimal(""+Outform.taxConfigurationList.optJSONObject(0).optDouble("value")));
                finalamount=(Double.parseDouble(Outform.fee)+Double.parseDouble(Outform.etAmount.getText().toString().replace(",",""))+Double.parseDouble(Outform.taxConfigurationList.optJSONObject(0).optString("value")));
            }
            if(Outform.taxConfigurationList.length()==2){
                tax_label_layout.setVisibility(View.VISIBLE);
                tax_label.setText(MyApplication.getTaxString(Outform.taxConfigurationList.optJSONObject(0).optString("taxTypeName")));
                tax_r.setText(Outform.currencySymbol+" "+MyApplication.addDecimal(""+Outform.taxConfigurationList.optJSONObject(0).optDouble("value")));

                vat_label_layout.setVisibility(View.VISIBLE);
                vat_label.setText(MyApplication.getTaxString(Outform.taxConfigurationList.optJSONObject(1).optString("taxTypeName")));
                vat_r.setText(Outform.currencySymbol+" "+MyApplication.addDecimal(""+Outform.taxConfigurationList.optJSONObject(1).optDouble("value")));
                finalamount=(Double.parseDouble(Outform.fee)+Double.parseDouble(Outform.etAmount.getText().toString().replace(",",""))+Double.parseDouble(Outform.taxConfigurationList.optJSONObject(0).optString("value"))+Double.parseDouble(Outform.taxConfigurationList.optJSONObject(1).optString("value")));
            }
        }



        tvAmountCharged.setText(Outform.currencySymbol+" "+MyApplication.addDecimal(""+finalamount));




        setOnCLickListener();

    }

    private void setOnCLickListener() {
        icPin.setOnClickListener(tosubscriberconfirmscreenC);
        btnConfirm.setOnClickListener(tosubscriberconfirmscreenC);
        btnCancel.setOnClickListener(tosubscriberconfirmscreenC);

    }
    HiddenPassTransformationMethod hiddenPassTransformationMethod=new HiddenPassTransformationMethod();
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icPin:
                if(etPin.getTransformationMethod().equals(hiddenPassTransformationMethod)){
                    icPin.setImageResource(R.drawable.ic_show);
                    //Show Password
                    etPin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    icPin.setImageResource(R.drawable.ic_hide);
                    //Hide Password
                    etPin.setTransformationMethod(hiddenPassTransformationMethod);

                }
                break;
            case R.id.btnConfirm:

                if(pinLinear.getVisibility()==View.VISIBLE){
                    if (etPin.getText().toString().trim().isEmpty()) {
                        MyApplication.showErrorToast(OutFormConfirmation.this, getString(R.string.val_pin));
                        return;
                    }
                    if (etPin.getText().toString().trim().length() < 4) {
                        MyApplication.showErrorToast(OutFormConfirmation.this, getString(R.string.val_valid_pin));
                        return;
                    }
                    try {
                        pinLinear.setVisibility(View.VISIBLE);

                        etPin.setClickable(false);
                        btnConfirm.setVisibility(View.GONE);
                        String encryptionDatanew = AESEncryption.getAESEncryption(etPin.getText().toString().trim());
                        Outform.jsonObjectNew.put( "pin",encryptionDatanew);
                        callPostAPI();
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }else {
                    MyApplication.biometricAuth(OutFormConfirmation.this, new BioMetric_Responce_Handler() {
                        @Override
                        public void success(String success) {
                            try {
                                etPin.setClickable(false);
                                btnConfirm.setVisibility(View.GONE);
                                String encryptionDatanew = AESEncryption.getAESEncryption(MyApplication.getSaveString("pin",MyApplication.appInstance).toString().trim());
                                Outform.jsonObjectNew.put( "pin",encryptionDatanew);

                                callPostAPI();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failure(String failure) {
                            //MyApplication.showToast(OutFormConfirmation.this,failure);
                            pinLinear.setVisibility(View.VISIBLE);

                        }
                    });




                }



                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }

    public static JSONObject receiptJson=new JSONObject();
    public static JSONArray taxConfigList;


    String ReceiptJSOn="{\"transactionId\":\"130288\",\"requestTime\":\"Fri Jun 24 06:13:03 UTC 2022\",\"responseTime\":\"Fri Jun 24 06:04:42 UTC 2022\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"remittance\":{\"code\":\"1000003209\",\"walletOwnerCode\":\"1000003966\",\"transactionType\":\"Wallet to Cash International\",\"senderCode\":\"1000003966\",\"receiverCode\":\"1000002458\",\"fromCurrencyCode\":\"100062\",\"fromCurrencyName\":\"GNF\",\"fromCurrencySymbol\":\"GNF\",\"toCurrencyCode\":\"100018\",\"toCurrencyName\":\"XOF\",\"toCurrencySymbol\":\"CFA\",\"comments\":\"test\",\"amount\":12360.0,\"amountToPaid\":670.0,\"fee\":2000.0,\"tax\":\"360.0\",\"conversionRate\":0.067,\"confirmationCode\":\"MM883846807\",\"transactionReferenceNo\":\"130288\",\"transactionDateTime\":\"2022-06-24 06:04:42\",\"sender\":{\"id\":111817,\"code\":\"1000003966\",\"firstName\":\"Rahul\",\"lastName\":\"Singh\",\"mobileNumber\":\"9910859185\",\"gender\":\"M\",\"idProofTypeCode\":\"100004\",\"idProofTypeName\":\"MILITARY ID CARD\",\"idProofNumber\":\"123456\",\"idExpiryDate\":\"2021-11-09\",\"dateOfBirth\":\"1988-07-07\",\"email\":\"rahul.singh@esteltelecom.com\",\"issuingCountryCode\":\"100092\",\"issuingCountryName\":\"Guinea\",\"status\":\"Active\",\"creationDate\":\"2022-05-12 18:15:55\",\"modificationDate\":\"2022-06-17 14:29:43\",\"registerCountryCode\":\"100092\",\"registerCountryName\":\"Guinea\",\"ownerName\":\"Rahul\",\"regesterCountryDialCode\":\"+224\"},\"receiver\":{\"id\":2362,\"code\":\"1000002458\",\"firstName\":\"Hardy\",\"lastName\":\"Versovy\",\"mobileNumber\":\"111111111\",\"gender\":\"M\",\"countryCode\":\"100195\",\"countryName\":\"Senegal\",\"status\":\"Active\",\"creationDate\":\"2022-06-24 11:34:32\",\"createdBy\":\"102809\",\"dialCode\":\"+221\"},\"taxConfigurationList\":[{\"taxTypeCode\":\"100131\",\"taxTypeName\":\"VAT\",\"value\":360.0,\"taxAvailBy\":\"Fee Amount\"}],\"channelTypeCode\":\"100002\",\"sendCountryCode\":\"100092\",\"receiveCountryCode\":\"100195\",\"sendCountryName\":\"Guinea\",\"receiveCountryName\":\"Senegal\",\"walletToCash\":true,\"walletOwnerUserName\":\"Rahul\"}}";

    /*public void callPostAPI(){

        try {
            JSONObject jsonObject = new JSONObject(ReceiptJSOn);
            if (jsonObject.optString("resultCode").equalsIgnoreCase("0")) {
                MyApplication.showToast(tosubscriberconfirmscreenC, jsonObject.optString("resultDescription"));
                receiptJson = jsonObject;
                JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("remittance");
                if (jsonObjectAmountDetails.has("taxConfigurationList")) {
                    taxConfigList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
                } else {
                    taxConfigList = null;
                }
                btnConfirm.setVisibility(View.VISIBLE);
                Intent intent = new Intent(tosubscriberconfirmscreenC, TransactionSuccessScreen.class);
                intent.putExtra("SENDINTENT", "OTOSUB");
                startActivity(intent);
                // {"transactionId":"2432","requestTime":"Fri Dec 25 05:51:11 IST 2020","responseTime":"Fri Dec 25 05:51:12 IST 2020","resultCode":"0","resultDescription":"Transaction Successful","remittance":{"code":"1000000327","walletOwnerCode":"1000000750","transactionType":"SEND REMITTANCE","senderCode":"1000000750","receiverCode":"AGNT202012","fromCurrencyCode":"100069","fromCurrencyName":"INR","fromCurrencySymbol":"₹","toCurrencyCode":"100069","toCurrencyName":"INR","toCurrencySymbol":"₹","amount":200,"amountToPaid":200,"fee":0,"tax":"0.0","conversionRate":0,"confirmationCode":"MMZJBJHYAAX","transactionReferenceNo":"1000000327","transactionDateTime":"2020-12-25 05:51:12","sender":{"id":1887,"code":"1000000750","firstName":"mahi","lastName":"kumar","mobileNumber":"88022255363","gender":"M","idProofTypeCode":"100000","idProofTypeName":"Passport","idProofNumber":"3333","idExpiryDate":"2025-12-20","dateOfBirth":"1960-01-05","email":"infomahendra2009@gmail.com","issuingCountryCode":"100001","issuingCountryName":"Albania","status":"Active","creationDate":"2020-12-14 11:17:33","registerCountryCode":"100102","registerCountryName":"India","ownerName":"mahi"},"receiver":{"id":1895,"code":"AGNT202012","firstName":"Rajesh","lastName":"Kumar","mobileNumber":"9821184601","gender":"M","idProofTypeCode":"100000","idProofTypeName":"Passport","idProofNumber":"DFZ123456","idExpiryDate":"2030-09-08","dateOfBirth":"1989-01-05","email":"abhishek.kumar2@esteltelecom.com","issuingCountryCode":"100102","issuingCountryName":"India","status":"Active","creationDate":"2020-12-14 14:00:23","createdBy":"100250","modificationDate":"2020-12-14 14:00:56","modifiedBy":"100250","registerCountryCode":"100102","registerCountryName":"India","ownerName":"Rajesh"}}}
            } else {
                etPin.setClickable(true);
                btnConfirm.setVisibility(View.VISIBLE);
                MyApplication.showToast(tosubscriberconfirmscreenC, jsonObject.optString("resultDescription"));
            }
        }catch (Exception e){

        }

      *//*  MyApplication.showloader(tosubscriberconfirmscreenC,"Please Wait...");
        JSONObject clone=null;
        try {
            if (switch_button.isChecked()) {
                clone = new JSONObject(dataToSendBear.toString());
            }else{
                clone = new JSONObject(Outform.dataToSend.toString());
            }
        }catch (Exception e){

        }
        System.out.println("Data Send "+clone);

        String requestNo=AESEncryption.getAESEncryption(clone.toString());
        JSONObject jsonObject=null;
        try{
            jsonObject=new JSONObject();
            jsonObject.put("request",requestNo);
        }catch (Exception e){

        }
        API.POST_REQEST_WH_NEW("ewallet/api/v1/walletTransfer/subscriber/cashOut", jsonObject,
                new Api_Responce_Handler() {
                    @Override
                    public void success(JSONObject jsonObject) {
                        MyApplication.hideLoader();
                        if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                            MyApplication.showToast(tosubscriberconfirmscreenC,jsonObject.optString("resultDescription"));
                            receiptJson=jsonObject;
                            JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("walletTransfer");
                            if(jsonObjectAmountDetails.has("taxConfigurationList")) {
                                taxConfigList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
                            }else{
                                taxConfigList=null;
                            }
                            btnConfirm.setVisibility(View.VISIBLE);
                            Intent intent=new Intent(tosubscriberconfirmscreenC, TransactionSuccessScreen.class);
                            intent.putExtra("SENDINTENT","OTOSUB");
                            startActivity(intent);
                            // {"transactionId":"2432","requestTime":"Fri Dec 25 05:51:11 IST 2020","responseTime":"Fri Dec 25 05:51:12 IST 2020","resultCode":"0","resultDescription":"Transaction Successful","remittance":{"code":"1000000327","walletOwnerCode":"1000000750","transactionType":"SEND REMITTANCE","senderCode":"1000000750","receiverCode":"AGNT202012","fromCurrencyCode":"100069","fromCurrencyName":"INR","fromCurrencySymbol":"₹","toCurrencyCode":"100069","toCurrencyName":"INR","toCurrencySymbol":"₹","amount":200,"amountToPaid":200,"fee":0,"tax":"0.0","conversionRate":0,"confirmationCode":"MMZJBJHYAAX","transactionReferenceNo":"1000000327","transactionDateTime":"2020-12-25 05:51:12","sender":{"id":1887,"code":"1000000750","firstName":"mahi","lastName":"kumar","mobileNumber":"88022255363","gender":"M","idProofTypeCode":"100000","idProofTypeName":"Passport","idProofNumber":"3333","idExpiryDate":"2025-12-20","dateOfBirth":"1960-01-05","email":"infomahendra2009@gmail.com","issuingCountryCode":"100001","issuingCountryName":"Albania","status":"Active","creationDate":"2020-12-14 11:17:33","registerCountryCode":"100102","registerCountryName":"India","ownerName":"mahi"},"receiver":{"id":1895,"code":"AGNT202012","firstName":"Rajesh","lastName":"Kumar","mobileNumber":"9821184601","gender":"M","idProofTypeCode":"100000","idProofTypeName":"Passport","idProofNumber":"DFZ123456","idExpiryDate":"2030-09-08","dateOfBirth":"1989-01-05","email":"abhishek.kumar2@esteltelecom.com","issuingCountryCode":"100102","issuingCountryName":"India","status":"Active","creationDate":"2020-12-14 14:00:23","createdBy":"100250","modificationDate":"2020-12-14 14:00:56","modifiedBy":"100250","registerCountryCode":"100102","registerCountryName":"India","ownerName":"Rajesh"}}}
                        }else{
                            etPin.setClickable(true);
                            btnConfirm.setVisibility(View.VISIBLE);
                            MyApplication.showToast(tosubscriberconfirmscreenC,jsonObject.optString("resultDescription"));
                        }
                    }

                    @Override
                    public void failure(String aFalse) {
                        MyApplication.hideLoader();
                        etPin.setClickable(true);
                        btnConfirm.setVisibility(View.VISIBLE);
                    }
                });*//*
    }*/

    public void callPostAPI(){
        MyApplication.showloader(OutFormConfirmation.this,getString(R.string.pleasewait));
      /*  String requestNo=AESEncryption.getAESEncryption(InternationalRecipientDetails.dataToSend.toString());
        JSONObject jsonObjectA=null;
        try{
            jsonObjectA=new JSONObject();
            jsonObjectA.put("request",requestNo);
        }catch (Exception e){

        }*/
        API.POST_REQEST_WH_NEW("ewallet/api/v1/intech/transferOut", Outform.jsonObjectNew,
                new Api_Responce_Handler() {
                    @Override
                    public void success(JSONObject jsonObject) {
                        MyApplication.hideLoader();
                        if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                            MyApplication.showToast(OutFormConfirmation.this,jsonObject.optString("resultDescription"));
                            receiptJson=jsonObject;
                            JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("intechResponse");
                            if(jsonObjectAmountDetails.has("taxConfigurationList")) {
                                taxConfigList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
                            }else{
                                taxConfigList=null;
                            }
                            btnConfirm.setVisibility(View.VISIBLE);
                            Intent intent=new Intent(tosubscriberconfirmscreenC, TransactionSuccessScreen.class);
                            intent.putExtra("SENDINTENT","OTOSUB");
                            startActivity(intent);
                            // {"transactionId":"2432","requestTime":"Fri Dec 25 05:51:11 IST 2020","responseTime":"Fri Dec 25 05:51:12 IST 2020","resultCode":"0","resultDescription":"Transaction Successful","remittance":{"code":"1000000327","walletOwnerCode":"1000000750","transactionType":"SEND REMITTANCE","senderCode":"1000000750","receiverCode":"AGNT202012","fromCurrencyCode":"100069","fromCurrencyName":"INR","fromCurrencySymbol":"₹","toCurrencyCode":"100069","toCurrencyName":"INR","toCurrencySymbol":"₹","amount":200,"amountToPaid":200,"fee":0,"tax":"0.0","conversionRate":0,"confirmationCode":"MMZJBJHYAAX","transactionReferenceNo":"1000000327","transactionDateTime":"2020-12-25 05:51:12","sender":{"id":1887,"code":"1000000750","firstName":"mahi","lastName":"kumar","mobileNumber":"88022255363","gender":"M","idProofTypeCode":"100000","idProofTypeName":"Passport","idProofNumber":"3333","idExpiryDate":"2025-12-20","dateOfBirth":"1960-01-05","email":"infomahendra2009@gmail.com","issuingCountryCode":"100001","issuingCountryName":"Albania","status":"Active","creationDate":"2020-12-14 11:17:33","registerCountryCode":"100102","registerCountryName":"India","ownerName":"mahi"},"receiver":{"id":1895,"code":"AGNT202012","firstName":"Rajesh","lastName":"Kumar","mobileNumber":"9821184601","gender":"M","idProofTypeCode":"100000","idProofTypeName":"Passport","idProofNumber":"DFZ123456","idExpiryDate":"2030-09-08","dateOfBirth":"1989-01-05","email":"abhishek.kumar2@esteltelecom.com","issuingCountryCode":"100102","issuingCountryName":"India","status":"Active","creationDate":"2020-12-14 14:00:23","createdBy":"100250","modificationDate":"2020-12-14 14:00:56","modifiedBy":"100250","registerCountryCode":"100102","registerCountryName":"India","ownerName":"Rajesh"}}}
                        }else{
                            etPin.setClickable(true);
                            btnConfirm.setVisibility(View.VISIBLE);
                            MyApplication.showToast(OutFormConfirmation.this,jsonObject.optString("resultDescription"));
                        }
                    }

                    @Override
                    public void failure(String aFalse) {
                        MyApplication.hideLoader();
                        etPin.setClickable(true);
                        btnConfirm.setVisibility(View.VISIBLE);
                    }
                });

    }
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
    DecimalFormat df = new DecimalFormat("0.00",symbols);
    JSONObject dataToSendBear;
    private void callApiAmountDetailsIbear() {
        try {
            //MyApplication.showloader(cashinC, "Please wait!");

            // dataToSend.put("transactionType","104591");
            //            dataToSend.put("desWalletOwnerCode",walletOwner.optJSONArray("walletOwnerList").optJSONObject(0).optString("walletOwnerCode"));
            //            dataToSend.put("srcWalletOwnerCode",MyApplication.getSaveString("walletOwnerCode",tosubscriberC));
            //            dataToSend.put("srcCurrencyCode","100062");
            //            dataToSend.put("desCurrencyCode","100062");
            //            dataToSend.put("value",etAmount.getText().toString());
            //            dataToSend.put("channelTypeCode",MyApplication.channelTypeCode);
            //            dataToSend.put("serviceCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode"));
            //            dataToSend.put("serviceCategoryCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode"));
            //            dataToSend.put("serviceProviderCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code"));
            //http://192.168.1.171:8081/ewallet/api/v1/exchangeRate/getBearerFee?
            // value=100&channelTypeCode=100000&serviceCode=100000&
            // serviceCategoryCode=100024&serviceProviderCode=100112&walletOwnerCode=1000002724
            API.GET("ewallet/api/v1/exchangeRate/getBearerFee?"+
                            "&value="+Outform.dataToSend.optString("value")+
                            "&channelTypeCode="+MyApplication.channelTypeCode+
                            "&serviceCode="+Outform.dataToSend.optString("serviceCode")
                            +"&serviceCategoryCode="+Outform.dataToSend.optString("serviceCategoryCode")+
                            "&serviceProviderCode="+Outform.dataToSend.optString("serviceProviderCode")+
                            "&walletOwnerCode="+MyApplication.getSaveString("walletOwnerCode", tosubscriberconfirmscreenC),
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            // MyApplication.hideLoader();
                            System.out.println("ToSubscriber response======="+jsonObject.toString());
                            if (jsonObject != null) {
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    if(jsonObject.has("exchangeRate")) {
                                        JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("exchangeRate");
                                        String fee = df.format(jsonObjectAmountDetails.optDouble("fee"));
                                        tvFee.setText(Outform.currencySymbol + " " + Outform.fee + "+ Bear Fee " + fee);
                                        Double transAmount = jsonObjectAmountDetails.optDouble("fee") + Double.parseDouble(Outform.etAmount.getText().toString().replace(",",""));
                                        Double paidAmount = jsonObjectAmountDetails.optDouble("fee") + Double.parseDouble(Outform.currencyValue);
                                        Double chargeAmount = jsonObjectAmountDetails.optDouble("fee") + finalamount;
                                        tvTransAmount.setText(Outform.currencySymbol + " " + MyApplication.addDecimal(""+transAmount));
                                        tvAmountPaid.setText(Outform.currencySymbol + " " + MyApplication.addDecimal(""+paidAmount));

                                        tvAmountCharged.setText(Outform.currencySymbol + " " + MyApplication.addDecimal(""+chargeAmount));


                                        try {
                                            dataToSendBear = new JSONObject();
                                            dataToSendBear.put("transactionType", "104591");
                                            dataToSendBear.put("desWalletOwnerCode", Outform.dataToSend.optString("desWalletOwnerCode"));
                                            dataToSendBear.put("srcWalletOwnerCode", MyApplication.getSaveString("walletOwnerCode", tosubscriberconfirmscreenC));
                                            dataToSendBear.put("srcCurrencyCode", "100062");
                                            dataToSendBear.put("desCurrencyCode", "100062");
                                            dataToSendBear.put("value", transAmount);
                                            dataToSendBear.put("channelTypeCode", MyApplication.channelTypeCode);
                                            dataToSendBear.put("serviceCode", Outform.dataToSend.optString("serviceCode"));
                                            dataToSendBear.put("serviceCategoryCode", Outform.dataToSend.optString("serviceCategoryCode"));
                                            dataToSendBear.put("serviceProviderCode", Outform.dataToSend.optString("serviceProviderCode"));
                                            dataToSendBear.put("bearerAllow", true);
                                            dataToSendBear.put("bearerFee", fee);

                                            dataToSendBear.put("transactionCoordinate", MainActivity.transactionCoordinate);
                                            dataToSendBear.put("transactionArea", MainActivity.transactionArea);
                                            dataToSendBear.put("isGpsOn", true);

                                        } catch (Exception e) {

                                        }
                                    }else{
                                        MyApplication.showToast(tosubscriberconfirmscreenC,"Template not found");
                                        switch_button.setChecked(false);
                                    }
                                   /* AmountDetailsInfoModel.AmountDetails amountDetails = new AmountDetailsInfoModel.AmountDetails(
                                            jsonObjectAmountDetails.optInt("fee"),
                                            jsonObjectAmountDetails.optInt("receiverFee"),
                                            jsonObjectAmountDetails.optInt("receiverTax"),
                                            jsonObjectAmountDetails.optString("value", "N/A"),
                                            jsonObjectAmountDetails.optString("currencyValue", "N/A")

                                    );

                                    AmountDetailsInfoModel amountDetailsInfoModel = new AmountDetailsInfoModel(
                                            jsonObject.optString("transactionId", "N/A"),
                                            jsonObject.optString("requestTime", "N/A"),
                                            jsonObject.optString("responseTime", "N/A"),
                                            jsonObject.optString("resultCode", "N/A"),
                                            jsonObject.optString("resultDescription", "N/A"),
                                            amountDetails
                                    );

                                    currencyValue= df.format(jsonObjectAmountDetails.optDouble("currencyValue"));
                                    fee = df.format(jsonObjectAmountDetails.optDouble("fee"));
                                    receiverFee= jsonObjectAmountDetails.optInt("receiverFee");
                                    receiverTax = jsonObjectAmountDetails.optInt("receiverTax");*/
//                                    int tax = receiverFee+receiverTax;
//                                    if(currencyValue<tax){
//                                        tvSend.setVisibility(View.GONE);
//                                        MyApplication.showErrorToast(tosubscriberC,getString(R.string.fee_tax_greater_than_trans_amt));
//                                    }else{
//                                        tvSend.setVisibility(View.VISIBLE);
//                                    }




                                } else {
                                    MyApplication.showToast(tosubscriberconfirmscreenC,jsonObject.optString("resultDescription", "N/A"));
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



}
