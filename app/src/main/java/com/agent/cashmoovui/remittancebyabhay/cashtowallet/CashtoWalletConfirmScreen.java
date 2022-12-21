package com.agent.cashmoovui.remittancebyabhay.cashtowallet;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
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
import com.agent.cashmoovui.remittancebyabhay.local.LocalRemittanceActivity;
import com.agent.cashmoovui.set_pin.AESEncryption;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class CashtoWalletConfirmScreen extends AppCompatActivity implements View.OnClickListener {
    public static CashtoWalletConfirmScreen cashtowalletconfirmC;
    public static TextView tvTransAmount;
    private TextView tvAgentCode,tvTransferMode,tvSenderMSISDN,tvBenefiMSISDN,tvSendCurrency,tvBenefiCurrency,
            tvConvRate,tvFee,tvAmountCharged,tvAmountPaid,tvComment,tax_label,tax_r,vat_label,vat_r;
    private LinearLayout amountpaidLinear,tax_label_layout,vat_label_layout;
    private EditText etPin;
    private Button btnCancel,btnConfirm;
    boolean  isPasswordVisible;
    double finalamount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_to_wallet_confirm_screen);
        cashtowalletconfirmC=this;
        getIds();
    }


    private void getIds() {
        tvAgentCode = findViewById(R.id.tvAgentCode);
        tvTransferMode = findViewById(R.id.tvTransferMode);
        tvSenderMSISDN = findViewById(R.id.tvSenderMSISDN);
        tvBenefiMSISDN = findViewById(R.id.tvBenefiMSISDN);
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
        amountpaidLinear=findViewById(R.id.amountpaidLinear);

        tvAgentCode.setText(MyApplication.getSaveString("walletOwnerCode", cashtowalletconfirmC));
        tvTransferMode.setText(getString(R.string.cash_to_wallet));

        tvSenderMSISDN.setText(CashtoWalletSenderKYC.senderNumber);
        tvBenefiMSISDN.setText(CashtoWalletReceiverKYC.recNumber);
        tvSendCurrency.setText(LocalRemittanceCashtowalletActivity.fromCurrency);
        tvBenefiCurrency.setText("GNF");
        tvTransAmount.setText(LocalRemittanceCashtowalletActivity.fromCurrencySymbol+" "+MyApplication.addDecimal(LocalRemittanceCashtowalletActivity.amount));
        tvConvRate.setText(MyApplication.addDecimalfive(LocalRemittanceCashtowalletActivity.rate));
        tvFee.setText(LocalRemittanceCashtowalletActivity.fromCurrencySymbol+" "+MyApplication.addDecimal(LocalRemittanceCashtowalletActivity.fee));
        tvAmountPaid.setText(LocalRemittanceCashtowalletActivity.toCurrencySymbolnew+" "+MyApplication.addDecimal(LocalRemittanceCashtowalletActivity.currencyValue));
       // tvComment.setText(LocalRemittanceCashtowalletActivity.etComment.getText().toString());


        String tamount=tvTransAmount.getText().toString();
        String paid=tvAmountPaid.getText().toString();

        System.out.println("get amount"+tamount);
        System.out.println("get paid"+paid);

        if(!tamount.equalsIgnoreCase(paid)){
            amountpaidLinear.setVisibility(View.VISIBLE);
        }else{
            amountpaidLinear.setVisibility(View.GONE);
        }
        finalamount=Double.parseDouble(LocalRemittanceCashtowalletActivity.fee)+Double.parseDouble(LocalRemittanceCashtowalletActivity.amount);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DecimalFormat df = new DecimalFormat("0.00",symbols);
        if(LocalRemittanceCashtowalletActivity.taxConfigurationList!=null){
            if(LocalRemittanceCashtowalletActivity.taxConfigurationList.length()==1){
                tax_label_layout.setVisibility(View.VISIBLE);
                tax_label.setText(MyApplication.getTaxStringnew(LocalRemittanceCashtowalletActivity.taxConfigurationList.optJSONObject(0).optString("taxTypeName"))+" :");
                tax_r.setText(LocalRemittanceCashtowalletActivity.fromCurrencySymbol+" "+MyApplication.addDecimal(""+LocalRemittanceCashtowalletActivity.taxConfigurationList.optJSONObject(0).optDouble("value")));
                finalamount=Double.parseDouble(LocalRemittanceCashtowalletActivity.fee)+Double.parseDouble(LocalRemittanceCashtowalletActivity.amount)+Double.parseDouble(LocalRemittanceCashtowalletActivity.taxConfigurationList.optJSONObject(0).optString("value"));
            }
            if(LocalRemittanceCashtowalletActivity.taxConfigurationList.length()==2){
                tax_label_layout.setVisibility(View.VISIBLE);
                tax_label.setText(MyApplication.getTaxStringnew(LocalRemittanceCashtowalletActivity.taxConfigurationList.optJSONObject(0).optString("taxTypeName"))+" :");
                tax_r.setText(LocalRemittanceCashtowalletActivity.fromCurrencySymbol+" "+MyApplication.addDecimal(""+LocalRemittanceCashtowalletActivity.taxConfigurationList.optJSONObject(0).optDouble("value")));

                vat_label_layout.setVisibility(View.VISIBLE);
                vat_label.setText(MyApplication.getTaxStringnew(LocalRemittanceCashtowalletActivity.taxConfigurationList.optJSONObject(1).optString("taxTypeName"))+" :");
                vat_r.setText(LocalRemittanceCashtowalletActivity.fromCurrencySymbol+" "+MyApplication.addDecimal(""+LocalRemittanceCashtowalletActivity.taxConfigurationList.optJSONObject(1).optDouble("value")));
                finalamount=Double.parseDouble(LocalRemittanceCashtowalletActivity.fee)+Double.parseDouble(LocalRemittanceCashtowalletActivity.amount)+Double.parseDouble(LocalRemittanceCashtowalletActivity.taxConfigurationList.optJSONObject(0).optString("value"))+Double.parseDouble(LocalRemittanceCashtowalletActivity.taxConfigurationList.optJSONObject(1).optString("value"));
            }
        }

        tvAmountCharged.setText(LocalRemittanceCashtowalletActivity.fromCurrencySymbol+" "+MyApplication.addDecimal(""+finalamount));


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
                    MyApplication.hideKeyboard(cashtowalletconfirmC);            }
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
        btnCancel.setOnClickListener(cashtowalletconfirmC);
        btnConfirm.setOnClickListener(cashtowalletconfirmC);

    }

    JSONObject remitJson=new JSONObject();
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btnConfirm:
                if (etPin.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(cashtowalletconfirmC, getString(R.string.val_pin));
                    return;
                }
                if (etPin.getText().toString().trim().length() < 4) {
                    MyApplication.showErrorToast(cashtowalletconfirmC, getString(R.string.val_valid_pin));
                    return;
                }
                try {
                    etPin.setClickable(false);
                    btnConfirm.setVisibility(View.GONE);
                    String encryptionDatanew = AESEncryption.getAESEncryption(etPin.getText().toString().trim());

                        remitJson.put("walletOwnerCode",tvAgentCode.getText().toString());
                        remitJson.put("transactionType","SENDREMITTANCE");
                        remitJson.put("senderCode",CashtoWalletSenderKYC.sendorCustomerJsonObj.optJSONObject("customer").optString("code"));
                       remitJson.put("receiverCode",CashtoWalletReceiverKYC.receiverCode);
                        remitJson.put("fromCurrencyCode",LocalRemittanceCashtowalletActivity.fromCurrencyCode);
                        remitJson.put("toCurrencyCode","100062");
                        remitJson.put("amount",LocalRemittanceCashtowalletActivity.amount);
                        remitJson.put("receiveMode","WALLET");
                        remitJson.put("conversionRate",LocalRemittanceCashtowalletActivity.rate);
                        remitJson.put("pin", encryptionDatanew);
                        remitJson.put("comments","");
                        remitJson.put("exchangeRateCode",LocalRemittanceCashtowalletActivity.exRateCode);
                        remitJson.put("channelTypeCode",MyApplication.channelTypeCode);
                        remitJson.put("serviceCode",CashtoWalletSenderKYC.serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode"));
                        remitJson.put("serviceCategoryCode",CashtoWalletSenderKYC.serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode"));
                        remitJson.put("serviceProviderCode",CashtoWalletSenderKYC.serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code"));
                        remitJson.put("sendCountryCode",CashtoWalletSenderKYC.sendCountryCode);
                        remitJson.put("receiveCountryCode",CashtoWalletSenderKYC.recCountryCode);
                        remitJson.put("firstName",CashtoWalletSenderKYC.et_sender_firstName.getText().toString());
                        remitJson.put("mobileNumber",CashtoWalletSenderKYC.et_sender_phoneNumber.getText().toString());
                       // remitJso  n.put("remitType","International Remit");

                    System.out.println("remitJson"+remitJson);

                        callPostAPI();

                } catch (Exception e) {
                    e.printStackTrace();
                }

              //  System.out.println("dataToSend---" + cashtowalletconfirmC.dataToSend.toString());

                break;
            case R.id.btnCancel:
                finish();
                break;
        }

    }

    public static JSONObject receiptJson=new JSONObject();
    public static JSONArray taxConfigList;
    public void callPostAPI(){

        MyApplication.showloader(cashtowalletconfirmC,"Please Wait...");

        String requestNo=AESEncryption.getAESEncryption(remitJson.toString());
        JSONObject jsonObjectA=null;
        try{
            jsonObjectA=new JSONObject();
            jsonObjectA.put("request",requestNo);
        }catch (Exception e){

        }
        API.POST_REQEST_WH_NEW("ewallet/api/v1/remittance/cashToWallet", jsonObjectA,
                new Api_Responce_Handler() {
                    @Override
                    public void success(JSONObject jsonObject) {
                        MyApplication.hideLoader();
                        if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                            MyApplication.showToast(cashtowalletconfirmC,jsonObject.optString("resultDescription"));
                            receiptJson=jsonObject;
                            JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("walletTransfer");
                            if(jsonObjectAmountDetails.has("taxConfigurationList")) {
                                taxConfigList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
                            }else{
                                taxConfigList=null;
                            }
                            btnConfirm.setVisibility(View.VISIBLE);
                            Intent intent=new Intent(cashtowalletconfirmC, TransactionSuccessScreen.class);
                            intent.putExtra("SENDINTENT","CASHTOWALLET");
                            startActivity(intent);
                            // {"transactionId":"2432","requestTime":"Fri Dec 25 05:51:11 IST 2020","responseTime":"Fri Dec 25 05:51:12 IST 2020","resultCode":"0","resultDescription":"Transaction Successful","remittance":{"code":"1000000327","walletOwnerCode":"1000000750","transactionType":"SEND REMITTANCE","senderCode":"1000000750","receiverCode":"AGNT202012","fromCurrencyCode":"100069","fromCurrencyName":"INR","fromCurrencySymbol":"₹","toCurrencyCode":"100069","toCurrencyName":"INR","toCurrencySymbol":"₹","amount":200,"amountToPaid":200,"fee":0,"tax":"0.0","conversionRate":0,"confirmationCode":"MMZJBJHYAAX","transactionReferenceNo":"1000000327","transactionDateTime":"2020-12-25 05:51:12","sender":{"id":1887,"code":"1000000750","firstName":"mahi","lastName":"kumar","mobileNumber":"88022255363","gender":"M","idProofTypeCode":"100000","idProofTypeName":"Passport","idProofNumber":"3333","idExpiryDate":"2025-12-20","dateOfBirth":"1960-01-05","email":"infomahendra2009@gmail.com","issuingCountryCode":"100001","issuingCountryName":"Albania","status":"Active","creationDate":"2020-12-14 11:17:33","registerCountryCode":"100102","registerCountryName":"India","ownerName":"mahi"},"receiver":{"id":1895,"code":"AGNT202012","firstName":"Rajesh","lastName":"Kumar","mobileNumber":"9821184601","gender":"M","idProofTypeCode":"100000","idProofTypeName":"Passport","idProofNumber":"DFZ123456","idExpiryDate":"2030-09-08","dateOfBirth":"1989-01-05","email":"abhishek.kumar2@esteltelecom.com","issuingCountryCode":"100102","issuingCountryName":"India","status":"Active","creationDate":"2020-12-14 14:00:23","createdBy":"100250","modificationDate":"2020-12-14 14:00:56","modifiedBy":"100250","registerCountryCode":"100102","registerCountryName":"India","ownerName":"Rajesh"}}}
                        }else{
                            etPin.setClickable(true);
                            btnConfirm.setVisibility(View.VISIBLE);
                            MyApplication.showToast(cashtowalletconfirmC,jsonObject.optString("resultDescription"));
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
