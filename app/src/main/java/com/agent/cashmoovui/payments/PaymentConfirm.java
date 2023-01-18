package com.agent.cashmoovui.payments;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.agent.cashmoovui.set_pin.AESEncryption;
import org.json.JSONArray;
import org.json.JSONObject;

public class PaymentConfirm extends AppCompatActivity implements View.OnClickListener {
    public static PaymentConfirm paymentconfirmC;
    // ImageView imgBack;
    Button btnConfirm,btnCancel;
    public static TextView tvProvider,tvAccNo,tvOperatorName,tvCurrency,tvTransAmount,tvAmountPaid,tvAmountCharged,tvFee,tax_label,tax_r,vat_label,vat_r;
    EditText etPin;
    double finalamount;
    LinearLayout tax_label_layout,vat_label_layout,pinLinear;
    CardView cardBearFee;
    boolean isPasswordVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirm);
        paymentconfirmC=this;
        //setBackMenu();
        getIds();
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
        tvAccNo = findViewById(R.id.tvAccNo);
        tvOperatorName = findViewById(R.id.tvOperatorName);
        tvCurrency = findViewById(R.id.tvCurrency);
        tvTransAmount = findViewById(R.id.tvTransAmount);
        tvAmountPaid = findViewById(R.id.tvAmountPaid);
        tvAmountCharged = findViewById(R.id.tvAmountCharged);
        tvFee = findViewById(R.id.tvFee);
        etPin = findViewById(R.id.etPin);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);
        pinLinear=findViewById(R.id.pinLinear);
        tax_r=findViewById(R.id.tax_r);
        vat_r=findViewById(R.id.vat_r);
        tax_label=findViewById(R.id.tax_label);
        vat_label=findViewById(R.id.vat_label);
        tax_label_layout=findViewById(R.id.tax_label_layout);
        vat_label_layout=findViewById(R.id.vat_label_layout);

        // tvProvider.setText(Payments.serviceProvider);
        tvAccNo.setText(PaymentDetails.etAccountNo.getText().toString());
        tvOperatorName.setText(Payments.operatorName);
        // tvCurrency.setText(Payments.currency);

        tvTransAmount.setText(Payments.currencySymbol+" "+MyApplication.addDecimal(PaymentDetails.etAmount.getText().toString().replace(",","")));
        tvAmountPaid.setText(Payments.currencySymbol+" "+ MyApplication.addDecimal(String.valueOf(PaymentDetails.currencyValue)));
        tvFee.setText(Payments.currencySymbol+" "+ MyApplication.addDecimal(String.valueOf(PaymentDetails.fee)));

        finalamount=Double.parseDouble(String.valueOf(PaymentDetails.fee))+Double.parseDouble(PaymentDetails.etAmount.getText().toString().replace(",",""));

        if(PaymentDetails.taxConfigurationList!=null){
            if(PaymentDetails.taxConfigurationList.length()==1){
                tax_label_layout.setVisibility(View.VISIBLE);
                tax_label.setText(MyApplication.getTaxString(PaymentDetails.taxConfigurationList.optJSONObject(0).optString("taxTypeName")));
                tax_r.setText(Payments.currencySymbol+" "+MyApplication.addDecimal(PaymentDetails.taxConfigurationList.optJSONObject(0).optString("value")));
                finalamount=Double.parseDouble(String.valueOf(PaymentDetails.fee))+Double.parseDouble(PaymentDetails.etAmount.getText().toString().replace(",",""))+Double.parseDouble(PaymentDetails.taxConfigurationList.optJSONObject(0).optString("value"));
            }
            if(PaymentDetails.taxConfigurationList.length()==2){
                tax_label_layout.setVisibility(View.VISIBLE);
                tax_label.setText(MyApplication.getTaxString(PaymentDetails.taxConfigurationList.optJSONObject(0).optString("taxTypeName")));
                tax_r.setText(Payments.currencySymbol+" "+MyApplication.addDecimal(PaymentDetails.taxConfigurationList.optJSONObject(0).optString("value")));

                vat_label_layout.setVisibility(View.VISIBLE);
                vat_label.setText(MyApplication.getTaxString(PaymentDetails.taxConfigurationList.optJSONObject(1).optString("taxTypeName")));
                vat_r.setText(Payments.currencySymbol+" "+MyApplication.addDecimal(PaymentDetails.taxConfigurationList.optJSONObject(1).optString("value")));
                finalamount=Double.parseDouble(String.valueOf(PaymentDetails.fee))+Double.parseDouble(PaymentDetails.etAmount.getText().toString().replace(",",""))+Double.parseDouble(PaymentDetails.taxConfigurationList.optJSONObject(0).optString("value"))+Double.parseDouble(PaymentDetails.taxConfigurationList.optJSONObject(0).optString("value"));
            }
        }

        tvAmountCharged.setText(Payments.currencySymbol+" "+MyApplication.addDecimal(Double.toString(finalamount)));

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
                    MyApplication.hideKeyboard(paymentconfirmC);            }
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



        TextView tvFinger =findViewById(R.id.tvFinger);
        if(MyApplication.setProtection!=null && !MyApplication.setProtection.isEmpty()) {
            if (MyApplication.setProtection.equalsIgnoreCase("Activate")) {
                //tvFinger.setVisibility(View.VISIBLE);
            } else {
               // tvFinger.setVisibility(View.GONE);
            }
        }else{
          //  tvFinger.setVisibility(View.VISIBLE);
        }
        tvFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.biometricAuth(PaymentConfirm.this, new BioMetric_Responce_Handler() {
                    @Override
                    public void success(String success) {
                        try {

                            String encryptionDatanew = AESEncryption.getAESEncryption(MyApplication.getSaveString("pin",MyApplication.appInstance).toString().trim());
                            PaymentDetails.dataToSend.put( "pin",encryptionDatanew);
                            callPostAPI();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(String failure) {
                        MyApplication.showToast(PaymentConfirm.this,failure);
                    }
                });
            }
        });


        setOnCLickListener();

    }

    private void setOnCLickListener() {
        btnConfirm.setOnClickListener(paymentconfirmC);
        btnCancel.setOnClickListener(paymentconfirmC);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btnConfirm: {

                {

                    if(pinLinear.getVisibility()==View.VISIBLE){
                        if (etPin.getText().toString().trim().isEmpty()) {
                            MyApplication.showErrorToast(paymentconfirmC, getString(R.string.val_pin));
                            return;
                        }
                        if (etPin.getText().toString().trim().length() < 4) {
                            MyApplication.showErrorToast(paymentconfirmC, getString(R.string.val_valid_pin));
                            return;
                        }
                        try {
                            etPin.setClickable(false);
                            btnConfirm.setVisibility(View.GONE);
                            String encryptionDatanew = AESEncryption.getAESEncryption(etPin.getText().toString().trim());
                            PaymentDetails.dataToSend.put("pin", encryptionDatanew);
                            callPostAPI();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return;
                    }else {
                        MyApplication.biometricAuth(PaymentConfirm.this, new BioMetric_Responce_Handler() {
                            @Override
                            public void success(String success) {
                                try {

                                    String encryptionDatanew = AESEncryption.getAESEncryption(MyApplication.getSaveString("pin", MyApplication.appInstance).toString().trim());
                                    PaymentDetails.dataToSend.put("pin", encryptionDatanew);
                                    callPostAPI();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failure(String failure) {
                                MyApplication.showToast(PaymentConfirm.this, failure);
                                pinLinear.setVisibility(View.VISIBLE);
                            }

                        });
                    }





                }

              /*  BiometricManager biometricManager = androidx.biometric.BiometricManager.from(PaymentConfirm.this);
                switch (biometricManager.canAuthenticate()) {

                    // this means we can use biometric sensor
                    case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:

                        Toast.makeText(PaymentConfirm.this, getString(R.string.device_not_contain_fingerprint), Toast.LENGTH_SHORT).show();
                        pinLinear.setVisibility(View.VISIBLE);

                        if (etPin.getText().toString().trim().isEmpty()) {
                            MyApplication.showErrorToast(paymentconfirmC, getString(R.string.val_pin));
                            return;
                        }
                        if (etPin.getText().toString().trim().length() < 4) {
                            MyApplication.showErrorToast(paymentconfirmC, getString(R.string.val_valid_pin));
                            return;
                        }
                        try {
                            etPin.setClickable(false);
                            btnConfirm.setVisibility(View.GONE);
                            String encryptionDatanew = AESEncryption.getAESEncryption(etPin.getText().toString().trim());
                            PaymentDetails.dataToSend.put("pin", encryptionDatanew);
                            callPostAPI();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return;
                    case BiometricManager.BIOMETRIC_SUCCESS:

                        MyApplication.biometricAuth(PaymentConfirm.this, new BioMetric_Responce_Handler() {
                            @Override
                            public void success(String success) {
                                try {

                                    String encryptionDatanew = AESEncryption.getAESEncryption(MyApplication.getSaveString("pin", MyApplication.appInstance).toString().trim());
                                    PaymentDetails.dataToSend.put("pin", encryptionDatanew);
                                    callPostAPI();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failure(String failure) {
                                MyApplication.showToast(PaymentConfirm.this, failure);
                                pinLinear.setVisibility(View.VISIBLE);
                            }
                        });
                }*/
            }
               /* if(etPin.getText().toString().trim().isEmpty()){
                    MyApplication.showErrorToast(paymentconfirmC,getString(R.string.val_pin));
                    return;
                }
                if(etPin.getText().toString().trim().length()<4){
                    MyApplication.showErrorToast(paymentconfirmC,getString(R.string.val_valid_pin));
                    return;
                }
                try {
                    etPin.setClickable(false);
                    btnConfirm.setVisibility(View.GONE);
                    String encryptionDatanew = AESEncryption.getAESEncryption(etPin.getText().toString().trim());
                    PaymentDetails.dataToSend.put( "pin",encryptionDatanew);
                    callPostAPI();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("dataToSend---"+PaymentDetails.dataToSend.toString());*/
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }

    public static JSONObject receiptJson=new JSONObject();
    public static JSONArray taxConfigList;
    public void callPostAPI(){
        MyApplication.showloader(paymentconfirmC,getString(R.string.pleasewait));
        String requestNo=AESEncryption.getAESEncryption(PaymentDetails.dataToSend.toString());
        JSONObject jsonObjectA=null;
        try{
            jsonObjectA=new JSONObject();
            jsonObjectA.put("request",requestNo);
        }catch (Exception e){

        }
        API.POST_REQEST_WH_NEW("ewallet/api/v1/recharge/payment", jsonObjectA,
                new Api_Responce_Handler() {
                    @Override
                    public void success(JSONObject jsonObject) {
                        MyApplication.hideLoader();
                        if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                            MyApplication.showToast(paymentconfirmC,jsonObject.optString("resultDescription"));
                            receiptJson=jsonObject;
                            JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("recharge");
                            if(jsonObjectAmountDetails.has("taxConfigurationList")) {
                                taxConfigList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
                            }else{
                                taxConfigList=null;
                            }
                            btnConfirm.setVisibility(View.VISIBLE);
                            Intent intent=new Intent(paymentconfirmC, TransactionSuccessScreen.class);
                            intent.putExtra("SENDINTENT","Payments");
                            startActivity(intent);
                            // {"transactionId":"2432","requestTime":"Fri Dec 25 05:51:11 IST 2020","responseTime":"Fri Dec 25 05:51:12 IST 2020","resultCode":"0","resultDescription":"Transaction Successful","remittance":{"code":"1000000327","walletOwnerCode":"1000000750","transactionType":"SEND REMITTANCE","senderCode":"1000000750","receiverCode":"AGNT202012","fromCurrencyCode":"100069","fromCurrencyName":"INR","fromCurrencySymbol":"₹","toCurrencyCode":"100069","toCurrencyName":"INR","toCurrencySymbol":"₹","amount":200,"amountToPaid":200,"fee":0,"tax":"0.0","conversionRate":0,"confirmationCode":"MMZJBJHYAAX","transactionReferenceNo":"1000000327","transactionDateTime":"2020-12-25 05:51:12","sender":{"id":1887,"code":"1000000750","firstName":"mahi","lastName":"kumar","mobileNumber":"88022255363","gender":"M","idProofTypeCode":"100000","idProofTypeName":"Passport","idProofNumber":"3333","idExpiryDate":"2025-12-20","dateOfBirth":"1960-01-05","email":"infomahendra2009@gmail.com","issuingCountryCode":"100001","issuingCountryName":"Albania","status":"Active","creationDate":"2020-12-14 11:17:33","registerCountryCode":"100102","registerCountryName":"India","ownerName":"mahi"},"receiver":{"id":1895,"code":"AGNT202012","firstName":"Rajesh","lastName":"Kumar","mobileNumber":"9821184601","gender":"M","idProofTypeCode":"100000","idProofTypeName":"Passport","idProofNumber":"DFZ123456","idExpiryDate":"2030-09-08","dateOfBirth":"1989-01-05","email":"abhishek.kumar2@esteltelecom.com","issuingCountryCode":"100102","issuingCountryName":"India","status":"Active","creationDate":"2020-12-14 14:00:23","createdBy":"100250","modificationDate":"2020-12-14 14:00:56","modifiedBy":"100250","registerCountryCode":"100102","registerCountryName":"India","ownerName":"Rajesh"}}}
                        }else{
                            etPin.setClickable(true);
                            btnConfirm.setVisibility(View.VISIBLE);
                            MyApplication.showToast(paymentconfirmC,jsonObject.optString("resultDescription"));
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


}

