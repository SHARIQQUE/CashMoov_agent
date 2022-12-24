package com.agent.cashmoovui.remittancebyabhay.international;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.agent.cashmoovui.HiddenPassTransformationMethod;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.activity.TransactionSuccessScreen;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.apiCalls.BioMetric_Responce_Handler;
import com.agent.cashmoovui.cash_in.CashIn;
import com.agent.cashmoovui.remittancebyabhay.local.LocalRemittanceActivity;
import com.agent.cashmoovui.set_pin.AESEncryption;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class InternationalRemittanceConfirmScreen extends AppCompatActivity implements View.OnClickListener {
    public static InternationalRemittanceConfirmScreen internationalremitconfirmC;
    public static TextView tvTransAmount;
    private TextView tvAgentCode,tvSenderCode,tvBenefiCode,tvSendCurrency,tvBenefiCurrency,
            tvConvRate,tvFee,tvAmountCharged,tvAmountPaid,tvComment,tax_label,tax_r,vat_label,vat_r;
    private LinearLayout tax_label_layout,vat_label_layout,pinLenear;
    private EditText etPin;
    private Button btnCancel,btnConfirm;
    boolean  isPasswordVisible;
    double finalamount;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_international_remittance_confirm_screen);
        internationalremitconfirmC=this;
        getIds();
    }


    private void getIds() {
        tvAgentCode = findViewById(R.id.tvAgentCode);
        tvSenderCode = findViewById(R.id.tvSenderCode);
        tvBenefiCode = findViewById(R.id.tvBenefiCode);
        tvSendCurrency = findViewById(R.id.tvSendCurrency);
        tvBenefiCurrency = findViewById(R.id.tvBenefiCurrency);
        tvTransAmount = findViewById(R.id.tvTransAmount);
        tvConvRate = findViewById(R.id.tvConvRate);
        tvFee = findViewById(R.id.tvFee);
        tvAmountCharged = findViewById(R.id.tvAmountCharged);
        tvAmountPaid = findViewById(R.id.tvAmountPaid);
        tvComment = findViewById(R.id.tvComment);
        etPin = findViewById(R.id.etPin);
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);

        tax_r=findViewById(R.id.tax_r);
        vat_r=findViewById(R.id.vat_r);
        tax_label=findViewById(R.id.tax_label);
        vat_label=findViewById(R.id.vat_label);
        tax_label_layout=findViewById(R.id.tax_label_layout);
        vat_label_layout=findViewById(R.id.vat_label_layout);
        pinLenear=findViewById(R.id.pinLenear);

        String nameOwner=MyApplication.getSaveString("FIRSTNAME_USERINFO", internationalremitconfirmC)+
                MyApplication.getSaveString("LASTNAME_USERINFO", internationalremitconfirmC);
        tvAgentCode.setText(nameOwner);

     String transctionamout=MyApplication.getSaveString("amount1",getApplicationContext());
        String amountformat=MyApplication.getSaveString("amountformat",getApplicationContext());


        tvSenderCode.setText(InternationalRemittanceSenderKYC.sendorCustomerJsonObj.optJSONObject("customer").optString("mobileNumber"));
        tvBenefiCode.setText(InternationalRemittanceBenefiKYC.benefiCustomerJsonObj.optJSONObject("customer").optString("mobileNumber"));
        tvSendCurrency.setText(InternationalRemittanceActivity.fromCurrency);
        tvBenefiCurrency.setText(InternationalRemittanceActivity.toCurrency);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);

        DecimalFormat df = new DecimalFormat("0.00",symbols);

        if( MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")){
            tvTransAmount.setText(InternationalRemittanceActivity.fromCurrencySymbol+" "+MyApplication.addDecimal(transctionamout));
        }else{
            tvTransAmount.setText(InternationalRemittanceActivity.fromCurrencySymbol+" "+(MyApplication.addDecimal(InternationalRemittanceActivity.amount)));

        }
        tvConvRate.setText(MyApplication.addDecimalfive(InternationalRemittanceActivity.rate));
        tvFee.setText(InternationalRemittanceActivity.fromCurrencySymbol+" "+MyApplication.addDecimal(InternationalRemittanceActivity.fee));
        tvAmountPaid.setText(InternationalRemittanceActivity.toCurrencySymbol+" "+MyApplication.addDecimal(InternationalRemittanceActivity.currencyValue));
        tvComment.setText(InternationalRemittanceBenefiKYC.etComment.getText().toString());

        finalamount=Double.parseDouble(InternationalRemittanceActivity.fee)+Double.parseDouble(InternationalRemittanceActivity.amount);

        if(InternationalRemittanceActivity.taxConfigurationList!=null){
            if(InternationalRemittanceActivity.taxConfigurationList.length()==1){
                tax_label_layout.setVisibility(View.VISIBLE);
                tax_label.setText(MyApplication.getTaxStringnew(InternationalRemittanceActivity.taxConfigurationList.optJSONObject(0).optString("taxTypeName"))+" :");
                tax_r.setText(InternationalRemittanceActivity.fromCurrencySymbol+" "+MyApplication.addDecimal(""+InternationalRemittanceActivity.taxConfigurationList.optJSONObject(0).optDouble("value")));
                finalamount=Double.parseDouble(InternationalRemittanceActivity.fee)+Double.parseDouble(InternationalRemittanceActivity.amount)+Double.parseDouble(InternationalRemittanceActivity.taxConfigurationList.optJSONObject(0).optString("value"));
            }
            if(InternationalRemittanceActivity.taxConfigurationList.length()==2){
                tax_label_layout.setVisibility(View.VISIBLE);
                tax_label.setText(MyApplication.getTaxStringnew(InternationalRemittanceConfirmScreen.taxConfigList.optJSONObject(0).optString("taxTypeName") )+ ":");
                tax_r.setText(InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol") + " " + MyApplication.addDecimal(""+InternationalRemittanceConfirmScreen.taxConfigList.optJSONObject(0).optDouble("value")));

                vat_label_layout.setVisibility(View.VISIBLE);
                vat_label.setText(MyApplication.getTaxStringnew(InternationalRemittanceActivity.taxConfigurationList.optJSONObject(1).optString("taxTypeName"))+" :");
                vat_r.setText(InternationalRemittanceActivity.fromCurrencySymbol+" "+MyApplication.addDecimal(""+InternationalRemittanceActivity.taxConfigurationList.optJSONObject(1).optDouble("value")));
                finalamount=Double.parseDouble(InternationalRemittanceActivity.fee)+Double.parseDouble(InternationalRemittanceActivity.amount)+Double.parseDouble(InternationalRemittanceActivity.taxConfigurationList.optJSONObject(0).optString("value"))+Double.parseDouble(InternationalRemittanceActivity.taxConfigurationList.optJSONObject(1).optString("value"));
            }
        }
       String val= MyApplication.getSaveString("amountchagre",getApplicationContext());


        tvAmountCharged.setText(InternationalRemittanceActivity.fromCurrencySymbol+" "+(""+val));


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
                    MyApplication.hideKeyboard(internationalremitconfirmC);            }
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


        setOnCLickListener();

    }

    private void setOnCLickListener() {
        btnCancel.setOnClickListener(internationalremitconfirmC);
        btnConfirm.setOnClickListener(internationalremitconfirmC);

    }

    JSONObject remitJson=new JSONObject();
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btnConfirm:


                if(pinLenear.getVisibility()==View.VISIBLE) {


                    try {
                        if (etPin.getText().toString().trim().isEmpty()) {
                            MyApplication.showErrorToast(internationalremitconfirmC, getString(R.string.val_pin));
                            return;
                        }
                        if (etPin.getText().toString().trim().length() < 4) {
                            MyApplication.showErrorToast(internationalremitconfirmC, getString(R.string.val_valid_pin));
                            return;
                        }

                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();


                        btnConfirm.setEnabled(false);
                        btnConfirm.setClickable(false);
                        MyApplication.showloader(InternationalRemittanceConfirmScreen.this,"Please Wait...");
                        etPin.setClickable(false);

                        String encryptionDatanew = AESEncryption.getAESEncryption(etPin.getText().toString().trim());

                            remitJson.put("walletOwnerCode", MyApplication.getSaveString("walletOwnerCode", internationalremitconfirmC));
                            JSONObject put = remitJson.put("transactionType", "SENDREMITTANCE");

                            remitJson.put("senderCode", InternationalRemittanceSenderKYC.sendorCustomerJsonObj.optJSONObject("customer").optString("code"));
                            remitJson.put("receiverCode", InternationalRemittanceBenefiKYC.benefiCustomerJsonObj.optJSONObject("customer").optString("code"));
                            remitJson.put("fromCurrencyCode", InternationalRemittanceActivity.fromCurrencyCode);
                            remitJson.put("toCurrencyCode", InternationalRemittanceActivity.toCurrencyCode);


                            String amountfrench = MyApplication.getSaveString("amount1", getApplicationContext());
                            System.out.println("get bb" + amountfrench);
                            String amountformat = MyApplication.getSaveString("amountformat", getApplicationContext());

                            if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {
                                remitJson.put("amount", amountfrench);
                            } else {
                                remitJson.put("amount", amountformat);

                            }

                            remitJson.put("conversionRate", InternationalRemittanceActivity.rate);
                            remitJson.put("pin", encryptionDatanew);
                            remitJson.put("comments", tvComment.getText().toString());
                            remitJson.put("exchangeRateCode", InternationalRemittanceActivity.exRateCode);
                            remitJson.put("channelTypeCode", MyApplication.channelTypeCode);
                            remitJson.put("serviceCode", InternationalRemittanceActivity.serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode"));
                            remitJson.put("serviceCategoryCode", InternationalRemittanceActivity.serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode"));
                            remitJson.put("serviceProviderCode", InternationalRemittanceActivity.serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code"));
                            remitJson.put("sendCountryCode", InternationalRemittanceActivity.sendCountryCode);
                            remitJson.put("receiveCountryCode", InternationalRemittanceActivity.recCountryCode);
                            remitJson.put("remitType", getString(R.string.International_Remittance));

                            System.out.println("get json remit" + remitJson);


                            tvSenderCode.setText(InternationalRemittanceSenderKYC.sendorCustomerJsonObj.optJSONObject("customer").optString("code"));
                            tvBenefiCode.setText(InternationalRemittanceBenefiKYC.benefiCustomerJsonObj.optJSONObject("customer").optString("code"));


                            callPostAPI();


                    }catch (Exception e){

                    }

                }else {
                    MyApplication.biometricAuth(InternationalRemittanceConfirmScreen.this, new BioMetric_Responce_Handler() {
                        @Override
                        public void success(String success) {

                            try {


                                etPin.setClickable(false);
                                btnConfirm.setVisibility(View.GONE);

                                String encryptionDatanew = AESEncryption.getAESEncryption(MyApplication.getSaveString("pin",MyApplication.appInstance));

                                remitJson.put("walletOwnerCode", MyApplication.getSaveString("walletOwnerCode", internationalremitconfirmC));
                                remitJson.put("transactionType", "SENDREMITTANCE");
                                remitJson.put("senderCode", InternationalRemittanceSenderKYC.sendorCustomerJsonObj.optJSONObject("customer").optString("code"));
                                remitJson.put("receiverCode", InternationalRemittanceBenefiKYC.benefiCustomerJsonObj.optJSONObject("customer").optString("code"));
                                remitJson.put("fromCurrencyCode", InternationalRemittanceActivity.fromCurrencyCode);
                                remitJson.put("toCurrencyCode", InternationalRemittanceActivity.toCurrencyCode);
                                remitJson.put("amount", MyApplication.getSaveString("amount",getApplicationContext()));
                                remitJson.put("conversionRate", InternationalRemittanceActivity.rate);
                                remitJson.put("pin", encryptionDatanew);
                                remitJson.put("comments", tvComment.getText().toString());
                                remitJson.put("exchangeRateCode", InternationalRemittanceActivity.exRateCode);
                                remitJson.put("channelTypeCode", MyApplication.channelTypeCode);
                                remitJson.put("serviceCode", InternationalRemittanceActivity.serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode"));
                                remitJson.put("serviceCategoryCode", InternationalRemittanceActivity.serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode"));
                                remitJson.put("serviceProviderCode", InternationalRemittanceActivity.serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code"));
                                remitJson.put("sendCountryCode", InternationalRemittanceActivity.sendCountryCode);
                                remitJson.put("receiveCountryCode", InternationalRemittanceActivity.recCountryCode);
                                remitJson.put("remitType", "International Remit");


                                tvSenderCode.setText(InternationalRemittanceSenderKYC.sendorCustomerJsonObj.optJSONObject("customer").optString("code"));
                                tvBenefiCode.setText(InternationalRemittanceBenefiKYC.benefiCustomerJsonObj.optJSONObject("customer").optString("code"));

                                callPostAPI();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failure(String failure) {

                            MyApplication.showToast(InternationalRemittanceConfirmScreen.this, failure);

                            pinLenear.setVisibility(View.VISIBLE);


                        }
                    });
                }


                //  System.out.println("dataToSend---" + internationalremitconfirmC.dataToSend.toString());

                break;
            case R.id.btnCancel:
                finish();
                break;
        }

    }

    public static JSONObject receiptJson=new JSONObject();
    public static JSONArray taxConfigList;
    public void callPostAPI(){

     //   MyApplication.showloader(internationalremitconfirmC,"Please Wait...");

        String requestNo=AESEncryption.getAESEncryption(remitJson.toString());
        JSONObject jsonObjectA=null;
        try{
            jsonObjectA=new JSONObject();
            jsonObjectA.put("request",requestNo);
        }catch (Exception e){

        }
        API.POST_REQEST_WH_NEW("ewallet/api/v1/remittance/send", jsonObjectA,
                new Api_Responce_Handler() {
                    @Override
                    public void success(JSONObject jsonObject) {
                        MyApplication.hideLoader();
                        btnConfirm.setEnabled(true);
                        btnConfirm.setClickable(true);


                        if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                            MyApplication.showToast(internationalremitconfirmC,jsonObject.optString("resultDescription"));
                            receiptJson=jsonObject;
                            JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("remittance");
                            if(jsonObjectAmountDetails.has("taxConfigurationList")) {
                                taxConfigList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
                            }else{
                                taxConfigList=null;
                            }
                          //  btnConfirm.setVisibility(View.VISIBLE);
                            Intent intent=new Intent(internationalremitconfirmC, TransactionSuccessScreen.class);
                            intent.putExtra("SENDINTENT","INTERNATIONAL");
                            intent.putExtra("rate",tvConvRate.getText());

                            startActivity(intent);
                            // {"transactionId":"2432","requestTime":"Fri Dec 25 05:51:11 IST 2020","responseTime":"Fri Dec 25 05:51:12 IST 2020","resultCode":"0","resultDescription":"Transaction Successful","remittance":{"code":"1000000327","walletOwnerCode":"1000000750","transactionType":"SEND REMITTANCE","senderCode":"1000000750","receiverCode":"AGNT202012","fromCurrencyCode":"100069","fromCurrencyName":"INR","fromCurrencySymbol":"₹","toCurrencyCode":"100069","toCurrencyName":"INR","toCurrencySymbol":"₹","amount":200,"amountToPaid":200,"fee":0,"tax":"0.0","conversionRate":0,"confirmationCode":"MMZJBJHYAAX","transactionReferenceNo":"1000000327","transactionDateTime":"2020-12-25 05:51:12","sender":{"id":1887,"code":"1000000750","firstName":"mahi","lastName":"kumar","mobileNumber":"88022255363","gender":"M","idProofTypeCode":"100000","idProofTypeName":"Passport","idProofNumber":"3333","idExpiryDate":"2025-12-20","dateOfBirth":"1960-01-05","email":"infomahendra2009@gmail.com","issuingCountryCode":"100001","issuingCountryName":"Albania","status":"Active","creationDate":"2020-12-14 11:17:33","registerCountryCode":"100102","registerCountryName":"India","ownerName":"mahi"},"receiver":{"id":1895,"code":"AGNT202012","firstName":"Rajesh","lastName":"Kumar","mobileNumber":"9821184601","gender":"M","idProofTypeCode":"100000","idProofTypeName":"Passport","idProofNumber":"DFZ123456","idExpiryDate":"2030-09-08","dateOfBirth":"1989-01-05","email":"abhishek.kumar2@esteltelecom.com","issuingCountryCode":"100102","issuingCountryName":"India","status":"Active","creationDate":"2020-12-14 14:00:23","createdBy":"100250","modificationDate":"2020-12-14 14:00:56","modifiedBy":"100250","registerCountryCode":"100102","registerCountryName":"India","ownerName":"Rajesh"}}}
                        }else {

                                etPin.setClickable(true);
                              //  btnConfirm.setVisibility(View.VISIBLE);

                            btnConfirm.setEnabled(true);
                            btnConfirm.setClickable(true);
                                ;

                                MyApplication.showToast(internationalremitconfirmC, jsonObject.optString("resultDescription"));
                            }

                    }

                    @Override
                    public void failure(String aFalse) {
                        MyApplication.hideLoader();
                        etPin.setClickable(true);

                        btnConfirm.setEnabled(true);
                        btnConfirm.setClickable(true);

                        };


                });

    }



}
