package com.agent.cashmoovui.remittancebyabhay.local;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.activity.OtherOption;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.model.CountryCurrencyInfoModel;
import com.agent.cashmoovui.model.CountryInfoModel;
import com.agent.cashmoovui.model.CountryRemittanceInfoModel;
import com.agent.cashmoovui.model.ServiceProviderModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class LocalRemittanceActivity extends AppCompatActivity implements View.OnClickListener {
    public static LocalRemittanceActivity localC;
    ImageView imgBack,imgHome;
    private TextView spinner_provider,spinner_senderCountry,spinner_senderCurrency,
            spinner_receiverCountry,spinner_receiverCurrency,convertionRate_first_page,fees_first_page,
            tax_first_page,amountTobeCharged_first_page,tvAmtCurr,tvAmtPaidCurr,tvNext;
    private EditText edittext_amount,edittext_amount_pay;

    private ArrayList<String> serviceProviderList = new ArrayList<>();
    private ArrayList<ServiceProviderModel.ServiceProvider> serviceProviderModelList = new ArrayList<>();

    private ArrayList<String> sendingCountryList = new ArrayList<>();
    private ArrayList<CountryInfoModel.Country> sendCountryModelList = new ArrayList<>();

    private ArrayList<String> sendCurrencyList = new ArrayList<>();
    private ArrayList<CountryCurrencyInfoModel.CountryCurrency> sendCurrencyModelList = new ArrayList<>();

    private ArrayList<String> recCountryList = new ArrayList<>();
    private ArrayList<CountryRemittanceInfoModel.RemitCountry> recCountryModelList = new ArrayList<>();

    private ArrayList<String> recCurrencyList = new ArrayList<>();
    private ArrayList<CountryCurrencyInfoModel.CountryCurrency> recCurrencyModelList = new ArrayList<>();

    private SpinnerDialog spinnerDialogSerProvider,spinnerDialogSendingCountry,spinnerDialogSendingCurr,
            spinnerDialogRecCountry,spinnerDialogRecCurr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_remittance);
        localC=this;
        setBackMenu();
        getIds();
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
                MyApplication.hideKeyboard(localC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(localC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    public static String serviceProvider,sendCountryCode,sendCountryName,recCountryCode,recCountryName,
            amount,currencyValue,fee,rate,exRateCode, fromCurrency,fromCurrencySymbol,fromCurrencyCode,
            toCurrency,toCurrencySymbol,toCurrencyCode;
    public static int receiverFee,receiverTax;
    public static JSONObject serviceCategory = new JSONObject();

    private void getIds() {
        spinner_provider = findViewById(R.id.spinner_provider);
        spinner_senderCountry = findViewById(R.id.spinner_senderCountry);
        spinner_senderCurrency = findViewById(R.id.spinner_senderCurrency);
        spinner_receiverCountry = findViewById(R.id.spinner_receiverCountry);
        spinner_receiverCurrency = findViewById(R.id.spinner_receiverCurrency);
        edittext_amount = findViewById(R.id.edittext_amount);
        tvAmtCurr = findViewById(R.id.tvAmtCurr);
        edittext_amount_pay = findViewById(R.id.edittext_amount_pay);
        tvAmtPaidCurr = findViewById(R.id.tvAmtPaidCurr);
        convertionRate_first_page = findViewById(R.id.convertionRate_first_page);
        fees_first_page = findViewById(R.id.fees_first_page);
        tax_first_page = findViewById(R.id.tax_first_page);
        amountTobeCharged_first_page = findViewById(R.id.amountTobeCharged_first_page);
        tvNext = findViewById(R.id.tvNext);

        edittext_amount_pay.setEnabled(false);

        spinner_provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogSerProvider!=null)
                    spinnerDialogSerProvider.showSpinerDialog();
            }
        });

//        spinner_senderCountry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (spinnerDialogSendingCountry!=null)
//                    spinnerDialogSendingCountry.showSpinerDialog();
//            }
//        });

        spinner_senderCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogSendingCurr!=null)
                    spinnerDialogSendingCurr.showSpinerDialog();
            }
        });
//        spinner_receiverCountry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (spinnerDialogRecCountry!=null)
//                    spinnerDialogRecCountry.showSpinerDialog();
//            }
//        });
        spinner_receiverCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogRecCurr!=null)
                    spinnerDialogRecCurr.showSpinerDialog();
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

            if (new InternetCheck().isConnected(localC)) {

//                    if (spinner_receiverCurrency.getSelectedItem()==null||spinner_receiverCurrency.getSelectedItem().toString().isEmpty()||spinner_receiverCurrency.getSelectedItem().toString().equals("Receiving Currency")) {
//                        MyApplication.showErrorToast(InternationalRemittance.this, getString(R.string.plz_select_receive_currency));
//                        return;
//                    }
                if (s.length()>1) {

                    callApiExchangeRate();

                } else {
                    convertionRate_first_page.setText("");
                    fees_first_page.setText("");
                    tax_first_page.setText("");
                    //amountTobePaid_first_page.setText("");
                    amountTobeCharged_first_page.setText("");
                    edittext_amount_pay.getText().clear();
                }

            } else {
                Toast.makeText(localC, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
            }
        }
    });
    


    callApiserviceProvider();

    setOnCLickListener();

}


    private void setOnCLickListener() {
        tvNext.setOnClickListener(localC);
    }

    @Override
    public void onClick(View v) {
        if(spinner_provider.getText().toString().equals(getString(R.string.valid_select_provider))) {
            MyApplication.showErrorToast(localC,getString(R.string.plz_select_provider));
            return;
        }
        if(spinner_senderCountry.getText().toString().equals(getString(R.string.sending_country_star))) {
            MyApplication.showErrorToast(localC,getString(R.string.sending_country_star));
            return;
        }
        if(spinner_senderCurrency.getText().toString().equals(getString(R.string.sending_currencey_star))) {
            MyApplication.showErrorToast(localC,getString(R.string.sending_currencey_star));
            return;
        }
        if(spinner_receiverCountry.getText().toString().equals(getString(R.string.receive_country))) {
            MyApplication.showErrorToast(localC,getString(R.string.receive_country));
            return;
        }
        if(spinner_receiverCurrency.getText().toString().equals(getString(R.string.receive_courency))) {
            MyApplication.showErrorToast(localC,getString(R.string.receive_courency));
            return;
        }
        if(edittext_amount.getText().toString().trim().isEmpty()) {
            MyApplication.showErrorToast(localC,getString(R.string.val_amount));
            return;
        }
        if(edittext_amount.getText().toString().trim().equals("0")||edittext_amount.getText().toString().trim().equals(".")||edittext_amount.getText().toString().trim().equals(".0")||
                edittext_amount.getText().toString().trim().equals("0.")||edittext_amount.getText().toString().trim().equals("0.0")||edittext_amount.getText().toString().trim().equals("0.00")){
            MyApplication.showErrorToast(localC,getString(R.string.val_valid_amount));
            return;
        }

        Intent i = new Intent(localC, LocalRemittanceSenderKYC.class);
        startActivity(i);
    }

    private void callApiserviceProvider() {
        try {

            API.GET("ewallet/api/v1/serviceProvider/serviceCategory?serviceCode=100002&serviceCategoryCode=100001&status=Y",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                serviceProviderList.clear();
                                serviceProviderModelList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    serviceCategory = jsonObject;
                                    serviceProvider = serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("name");

                                    JSONArray walletOwnerListArr = serviceCategory.optJSONArray("serviceProviderList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        serviceProviderModelList.add(new ServiceProviderModel.ServiceProvider(
                                                data.optInt("id"),
                                                data.optString("code"),
                                                data.optString("creationDate"),
                                                data.optString("name"),
                                                data.optString("serviceCategoryCode"),
                                                data.optString("serviceCategoryName"),
                                                data.optString("serviceCode"),
                                                data.optString("serviceName"),
                                                data.optString("serviceProviderMasterCode"),
                                                data.optString("status")

                                        ));
                                        serviceProviderList.add(data.optString("name").trim());
                                        spinner_provider.setText(data.optString("name"));


                                    }

                                    //  spinnerDialog=new SpinnerDialog(selltransferC,instituteList,"Select or Search City","CANCEL");// With No Animation
                                    spinnerDialogSerProvider = new SpinnerDialog(localC, serviceProviderList, "Select Service Provider", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation


                                    spinnerDialogSerProvider.setCancellable(true); // for cancellable
                                    spinnerDialogSerProvider.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogSerProvider.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spinner_provider.setText(item);
                                            spinner_provider.setTag(position);

                                        }
                                    });

                                    callApiCountry();
                                } else {
                                    MyApplication.showToast(localC,jsonObject.optString("resultDescription", "N/A"));
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

    private void callApiCountry() {
        try {

            API.GET("ewallet/api/v1/country/all",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            if (jsonObject != null) {
                                sendingCountryList.clear();
                                sendCountryModelList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){

                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("countryList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        if (data.optString("code").equalsIgnoreCase(MyApplication.getSaveString("COUNTRYCODE_AGENT", localC))) {
                                            sendCountryModelList.add(new CountryInfoModel.Country(
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

                                            sendingCountryList.add(data.optString("name").trim());
                                            recCurrencyModelList = sendCurrencyModelList;
                                            recCurrencyList = sendingCountryList;
                                            spinner_senderCountry.setText(data.optString("name"));
                                            spinner_receiverCountry.setText(data.optString("name"));
                                            sendCountryCode = data.optString("code");
                                            sendCountryName = data.optString("name");
                                            recCountryCode = sendCountryCode;
                                            recCountryName = sendCountryName;


                                        }
                                    }

                                    spinnerDialogSendingCountry= new SpinnerDialog(localC, sendingCountryList, "Select Country", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
                                    spinnerDialogSendingCountry.setCancellable(true); // for cancellable
                                    spinnerDialogSendingCountry.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogSendingCountry.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spinner_senderCountry.setText(item);
                                            spinner_senderCountry.setTag(position);
                                            spinner_senderCurrency.setText(getString(R.string.sending_currencey_star));
                                            //   txt_benefi_phone.setText(benefiCountryModelList.get(position).dialCode);
                                            callApiSendCurrency(sendCountryModelList.get(position).getCode());
                                        }
                                    });

                                    spinnerDialogRecCountry= new SpinnerDialog(localC, recCountryList, "Select Country", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
                                    spinnerDialogRecCountry.setCancellable(true); // for cancellable
                                    spinnerDialogRecCountry.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogRecCountry.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spinner_receiverCountry.setText(item);
                                            spinner_receiverCountry.setTag(position);
                                            spinner_receiverCountry.setText(getString(R.string.sending_currencey_star));
                                            //   txt_benefi_phone.setText(benefiCountryModelList.get(position).dialCode);
                                            callApiSendCurrency(recCountryModelList.get(position).getCode());
                                        }
                                    });

                                    callApiSendCurrency(MyApplication.getSaveString("COUNTRYCODE_AGENT", localC));

                                } else {
                                    MyApplication.showToast(localC,jsonObject.optString("resultDescription", "N/A"));
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

    private void callApiSendCurrency(String code) {
        try {

            API.GET("ewallet/api/v1/countryCurrency/country/"+code,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                sendCurrencyList.clear();
                                sendCurrencyModelList.clear();
                                recCurrencyList.clear();
                                recCurrencyModelList.clear();
                                if(jsonObject.optString("resultCode", "  ").equalsIgnoreCase("0")){
                                    JSONObject countryCurrObj = jsonObject.optJSONObject("country");
                                    JSONArray countryCurrencyListArr = countryCurrObj.optJSONArray("countryCurrencyList");
                                    for (int i = 0; i < countryCurrencyListArr.length(); i++) {
                                        JSONObject data = countryCurrencyListArr.optJSONObject(i);
                                        sendCurrencyModelList.add(new CountryCurrencyInfoModel.CountryCurrency(
                                                data.optInt("id"),
                                                data.optString("code"),
                                                data.optString("countryCode"),
                                                data.optString("countryName"),
                                                data.optString("createdBy"),
                                                data.optString("creationDate"),
                                                data.optString("currCode"),
                                                data.optString("currencyCode"),
                                                data.optString("currencyName"),
                                                data.optString("currencySymbol"),
                                                data.optString("dialCode"),
                                                data.optInt("mobileLength"),
                                                data.optString("modificationDate"),
                                                data.optString("modifiedBy"),
                                                data.optString("state"),
                                                data.optString("status"),
                                                data.optBoolean("inBound"),
                                                data.optBoolean("outBound")

                                        ));

                                        sendCurrencyList.add(data.optString("currCode").trim());
                                        recCurrencyModelList = sendCurrencyModelList;
                                        recCurrencyList = sendCurrencyList;

                                    }

                                    tvAmtCurr.setText("");
                                    for(int i=0;i<sendCurrencyModelList.size();i++){
                                        if(countryCurrObj.optString("currencySymbol").equalsIgnoreCase(
                                                sendCurrencyModelList.get(i).getCurrencySymbol()
                                        )){
                                            spinner_senderCurrency.setText(sendCurrencyModelList.get(i).getCurrCode());
                                            spinner_senderCurrency.setTag(i);
                                            fromCurrency = sendCurrencyModelList.get(i).getCurrCode();
                                            fromCurrencySymbol = sendCurrencyModelList.get(i).getCurrencySymbol();
                                            fromCurrencyCode = sendCurrencyModelList.get(i).getCurrencyCode();
                                            tvAmtCurr.setText(fromCurrencySymbol);
                                            // txt_curr_symbol_paid.setText(benefiCurrencyModelList.get(position).currencySymbol);
                                            edittext_amount.getText().clear();
                                            edittext_amount_pay.getText().clear();

                                        }
                                    }

                                    spinnerDialogSendingCurr = new SpinnerDialog(localC, sendCurrencyList, "Select Currency", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
                                    spinnerDialogSendingCurr.setCancellable(true); // for cancellable
                                    spinnerDialogSendingCurr.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogSendingCurr.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spinner_senderCurrency.setText(item);
                                            spinner_senderCurrency.setTag(position);
                                            fromCurrency = sendCurrencyModelList.get(position).getCurrCode();
                                            fromCurrencySymbol = sendCurrencyModelList.get(position).getCurrencySymbol();
                                            fromCurrencyCode = sendCurrencyModelList.get(position).getCurrencyCode();
                                            tvAmtCurr.setText(fromCurrencySymbol);
                                            // txt_curr_symbol_paid.setText(benefiCurrencyModelList.get(position).currencySymbol);
                                            edittext_amount.getText().clear();
                                            edittext_amount_pay.getText().clear();
                                        }
                                    });

                                    tvAmtPaidCurr.setText("");
                                    for(int i=0;i<recCurrencyModelList.size();i++){
                                        if(countryCurrObj.optString("currencySymbol").equalsIgnoreCase(
                                                recCurrencyModelList.get(i).getCurrencySymbol()
                                        )){
                                            spinner_receiverCurrency.setText(recCurrencyModelList.get(i).getCurrCode() );
                                            spinner_receiverCurrency.setTag(i);
                                            toCurrency = recCurrencyModelList.get(i).getCurrCode();
                                            toCurrencySymbol = recCurrencyModelList.get(i).getCurrencySymbol();
                                            toCurrencyCode = recCurrencyModelList.get(i).getCurrencyCode();
                                            tvAmtPaidCurr.setText(toCurrencySymbol);
                                            // txt_curr_symbol_paid.setText(benefiCurrencyModelList.get(position).currencySymbol);
                                            edittext_amount.getText().clear();
                                            edittext_amount_pay.getText().clear();

                                        }
                                    }

                                    spinnerDialogRecCurr = new SpinnerDialog(localC, recCurrencyList, "Select Currency", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
                                    spinnerDialogRecCurr.setCancellable(true); // for cancellable
                                    spinnerDialogRecCurr.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogRecCurr.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spinner_receiverCurrency.setText(item);
                                            spinner_receiverCurrency.setTag(position);
                                            toCurrency = recCurrencyModelList.get(position).getCurrCode();
                                            toCurrencySymbol = recCurrencyModelList.get(position).getCurrencySymbol();
                                            toCurrencyCode = recCurrencyModelList.get(position).getCurrencyCode();
                                            tvAmtPaidCurr.setText(toCurrencySymbol);
                                            // txt_curr_symbol_paid.setText(benefiCurrencyModelList.get(position).currencySymbol);
                                            edittext_amount.getText().clear();
                                            edittext_amount_pay.getText().clear();
                                        }
                                    });

                                   // callApiRecCountry();

                                } else {
                                    MyApplication.showToast(localC,jsonObject.optString("resultDescription", "  "));
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

//    private void callApiRecCountry() {
//        try {
//
//            API.GET("ewallet/api/v1/countryRemittance/all",
//                    new Api_Responce_Handler() {
//                        @Override
//                        public void success(JSONObject jsonObject) {
//                            MyApplication.hideLoader();
//                            if (jsonObject != null) {
//                                recCountryList.clear();
//                                recCountryModelList.clear();
//                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
//                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("countryRemittanceList");
//                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
//                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
//                                        if (!MyApplication.getSaveString("COUNTRYCODE_AGENT", localC).equalsIgnoreCase(data.optString("countryCode"))) {
//                                            recCountryModelList.add(new CountryRemittanceInfoModel.RemitCountry(
//                                                    data.optInt("id"),
//                                                    data.optString("code"),
//                                                    data.optString("countryCode"),
//                                                    data.optString("countryIsoCode"),
//                                                    data.optString("countryName"),
//                                                    data.optString("createdBy"),
//                                                    data.optString("creationDate"),
//                                                    data.optString("currencyCode"),
//                                                    data.optString("currencySymbol"),
//                                                    data.optString("dialCode"),
//                                                    data.optString("mobileLength"),
//                                                    data.optString("modificationDate"),
//                                                    data.optString("modifiedBy"),
//                                                    data.optString("state"),
//                                                    data.optString("status"),
//                                                    data.optBoolean("remitReceiving"),
//                                                    data.optBoolean("remitSending")
//                                            ));
//
//                                            recCountryList.add(data.optString("countryName").trim());
//
//                                        }
//
//                                    }
//
//                                    spinnerDialogRecCountry= new SpinnerDialog(localC, recCountryList, "Select Country", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
//                                    spinnerDialogRecCountry.setCancellable(true); // for cancellable
//                                    spinnerDialogRecCountry.setShowKeyboard(false);// for open keyboard by default
//                                    spinnerDialogRecCountry.bindOnSpinerListener(new OnSpinerItemClick() {
//                                        @Override
//                                        public void onClick(String item, int position) {
//                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
//                                            spinner_receiverCountry.setText(item);
//                                            spinner_receiverCountry.setTag(position);
//                                            spinner_receiverCurrency.setText(getString(R.string.valid_beneficiary_currency));
//                                            //   txt_benefi_phone.setText(benefiCountryModelList.get(position).dialCode);
//                                            recCountryCode = recCountryModelList.get(position).getCountryCode();
//                                            recCountryName = recCountryModelList.get(position).getCountryName();
//                                            callApiRecCurrency(recCountryCode);
//                                        }
//                                    });
//
//
//                                } else {
//                                    MyApplication.showToast(localC,jsonObject.optString("resultDescription", "N/A"));
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void failure(String aFalse) {
//                            MyApplication.hideLoader();
//
//                        }
//                    });
//
//        } catch (Exception e) {
//
//        }
//
//
//    }
//
//    private void callApiRecCurrency(String code) {
//        try {
//
//            API.GET("ewallet/api/v1/countryCurrency/country/"+code,
//                    new Api_Responce_Handler() {
//                        @Override
//                        public void success(JSONObject jsonObject) {
//                            MyApplication.hideLoader();
//
//                            if (jsonObject != null) {
//                                recCurrencyList.clear();
//                                recCurrencyModelList.clear();
//                                if(jsonObject.optString("resultCode", "  ").equalsIgnoreCase("0")){
//                                    JSONObject countryCurrObj = jsonObject.optJSONObject("country");
//                                    JSONArray countryCurrencyListArr = countryCurrObj.optJSONArray("countryCurrencyList");
//                                    for (int i = 0; i < countryCurrencyListArr.length(); i++) {
//                                        JSONObject data = countryCurrencyListArr.optJSONObject(i);
//                                        recCurrencyModelList.add(new CountryCurrencyInfoModel.CountryCurrency(
//                                                data.optInt("id"),
//                                                data.optString("code"),
//                                                data.optString("countryCode"),
//                                                data.optString("countryName"),
//                                                data.optString("createdBy"),
//                                                data.optString("creationDate"),
//                                                data.optString("currCode"),
//                                                data.optString("currencyCode"),
//                                                data.optString("currencyName"),
//                                                data.optString("currencySymbol"),
//                                                data.optString("dialCode"),
//                                                data.optInt("mobileLength"),
//                                                data.optString("modificationDate"),
//                                                data.optString("modifiedBy"),
//                                                data.optString("state"),
//                                                data.optString("status"),
//                                                data.optBoolean("inBound"),
//                                                data.optBoolean("outBound")
//
//                                        ));
//
//                                        recCurrencyList.add(data.optString("currCode").trim());
//
//                                    }
//
//                                    spinnerDialogRecCurr = new SpinnerDialog(localC, recCurrencyList, "Select Currency", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
//                                    spinnerDialogRecCurr.setCancellable(true); // for cancellable
//                                    spinnerDialogRecCurr.setShowKeyboard(false);// for open keyboard by default
//                                    spinnerDialogRecCurr.bindOnSpinerListener(new OnSpinerItemClick() {
//                                        @Override
//                                        public void onClick(String item, int position) {
//                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
//                                            spinner_receiverCurrency.setText(item);
//                                            spinner_receiverCurrency.setTag(position);
//                                            toCurrency = recCurrencyModelList.get(position).getCurrCode();
//                                            toCurrencySymbol = recCurrencyModelList.get(position).getCurrencySymbol();
//                                            toCurrencyCode = recCurrencyModelList.get(position).getCurrencyCode();
//                                            tvAmtPaidCurr.setText(toCurrencySymbol);
//                                            // txt_curr_symbol_paid.setText(benefiCurrencyModelList.get(position).currencySymbol);
//                                            edittext_amount.getText().clear();
//                                            edittext_amount_pay.getText().clear();
//                                        }
//                                    });
//
//                                    callApiRecCountry();
//
//                                } else {
//                                    MyApplication.showToast(localC,jsonObject.optString("resultDescription", "  "));
//                                }
//                            }
//
//
//                        }
//
//                        @Override
//                        public void failure(String aFalse) {
//                            MyApplication.hideLoader();
//
//                        }
//                    });
//
//        } catch (Exception e) {
//
//        }
//
//    }

    DecimalFormat df = new DecimalFormat("0.000");
    public static JSONArray taxConfigurationList;

    private void callApiExchangeRate() {
        try {
            //MyApplication.showloader(cashinC, "Please wait!");
            API.GET("ewallet/api/v1/exchangeRate/getAmountDetails?sendCurrencyCode="+
                            sendCurrencyModelList.get((Integer) spinner_senderCurrency.getTag()).getCurrencyCode()
                            +"&receiveCurrencyCode="+recCurrencyModelList.get((Integer) spinner_receiverCurrency.getTag()).getCurrencyCode()
                            +"&sendCountryCode="+ sendCountryCode+
                            "&receiveCountryCode="+recCountryCode
                            +"&currencyValue="+ edittext_amount.getText().toString() + "&channelTypeCode="+MyApplication.channelTypeCode+
                            "&serviceCode=" + serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode")+
                            "&serviceCategoryCode=" + serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode")
                            + "&serviceProviderCode=" + serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code")
                            + "&walletOwnerCode=" + MyApplication.getSaveString("USERCODE", localC),

                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            // MyApplication.hideLoader();
                            System.out.println("International response======="+jsonObject.toString());
                            if (jsonObject != null) {
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    if(edittext_amount.getText().toString().trim().length()>0) {
                                        JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("exchangeRate");

                                        currencyValue = df.format(jsonObjectAmountDetails.optDouble("currencyValue"));
                                        fee = df.format(jsonObjectAmountDetails.optDouble("fee"));
                                        rate = jsonObjectAmountDetails.optString("value");
                                        exRateCode = jsonObjectAmountDetails.optString("code");
                                        //receiverFee= jsonObjectAmountDetails.optInt("receiverFee");
                                        //receiverTax = jsonObjectAmountDetails.optInt("receiverTax");
                                        //etAmountNew.setText(currencyValue);
                                        convertionRate_first_page.setText(rate);
                                        fees_first_page.setText(fee);
                                        edittext_amount_pay.setText(currencyValue);
                                        amount = edittext_amount.getText().toString().trim();


//                                    int tax = receiverFee+receiverTax;
//                                    if(currencyValue<tax){
//                                        tvSend.setVisibility(View.GONE);
//                                        MyApplication.showErrorToast(tononsubscriberC,getString(R.string.fee_tax_greater_than_trans_amt));
//                                    }else{
//                                        tvSend.setVisibility(View.VISIBLE);
//                                    }

                                        if (jsonObjectAmountDetails.has("taxConfigurationList")) {
                                            taxConfigurationList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
                                            tax_first_page.setText(df.format(taxConfigurationList.optJSONObject(0).optDouble("value")));
                                            amountTobeCharged_first_page.setText(df.format(Double.parseDouble(edittext_amount.getText().toString().trim()) + taxConfigurationList.optJSONObject(0).optDouble("value")));

                                        } else {
                                            taxConfigurationList = null;
                                            tax_first_page.setText("0.00");
                                            amountTobeCharged_first_page.setText(df.format(Double.parseDouble(edittext_amount.getText().toString().trim())));

                                        }


                                        tvNext.setEnabled(true);
                                    }
                                } else {
                                    tvNext.setEnabled(false);
                                    MyApplication.showToast(localC,jsonObject.optString("resultDescription", "N/A"));
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
