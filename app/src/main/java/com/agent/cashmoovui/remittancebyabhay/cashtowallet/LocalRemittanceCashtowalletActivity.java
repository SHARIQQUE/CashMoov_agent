package com.agent.cashmoovui.remittancebyabhay.cashtowallet;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
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
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.model.CountryCurrencyInfoModel;
import com.agent.cashmoovui.model.CountryInfoModel;
import com.agent.cashmoovui.model.CountryRemittanceInfoModel;
import com.agent.cashmoovui.model.ServiceProviderModel;
import com.agent.cashmoovui.remittancebyabhay.local.LocalRemittanceActivity;
import com.agent.cashmoovui.remittancebyabhay.local.LocalRemittanceSenderKYC;
import com.aldoapps.autoformatedittext.AutoFormatUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class LocalRemittanceCashtowalletActivity extends AppCompatActivity implements View.OnClickListener {
    public static LocalRemittanceCashtowalletActivity localC;
    ImageView imgBack,imgHome;
    private TextView receivingcurrecncyText,sendigncurrecncyText,spinner_provider,spinner_senderCountry,spinner_senderCurrency,
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

    private String nextbtn="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_remittance_cashtowallet);
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
            amount,currencyValue,fee,rate,exRateCode, fromCurrency,fromCurrencynew,fromCurrencySymbol,fromCurrencyCode,
            toCurrency,toCurrencySymbol,toCurrencyCode,toCurrencySymbolnew;
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
        sendigncurrecncyText=findViewById(R.id.sendigncurrecncyText);
        receivingcurrecncyText=findViewById(R.id.receivingcurrecncyText);
        convertionRate_first_page = findViewById(R.id.convertionRate_first_page);
        fees_first_page = findViewById(R.id.fees_first_page);
        tax_first_page = findViewById(R.id.tax_first_page);
        amountTobeCharged_first_page = findViewById(R.id.amountTobeCharged_first_page);
        tvNext = findViewById(R.id.tvNext);

        edittext_amount.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(MyApplication.amountLength)});

        edittext_amount_pay.setEnabled(false);
        receivingcurrecncyText.setVisibility(View.VISIBLE);
        receivingcurrecncyText.setText(getString(R.string.receive_courency_start));
        spinner_receiverCurrency.setText("GNF");
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
      /*  spinner_receiverCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogRecCurr!=null)
                    spinnerDialogRecCurr.showSpinerDialog();
            }
        });*/


        edittext_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                convertionRate_first_page.setText("");
                edittext_amount_pay.setText("");
                fees_first_page.setText("");
                tax_first_page.setText("");
                amountTobeCharged_first_page.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (new InternetCheck().isConnected(localC)) {

//                    if (spinner_receiverCurrency.getSelectedItem()==null||spinner_receiverCurrency.getSelectedItem().toString().isEmpty()||spinner_receiverCurrency.getSelectedItem().toString().equals("Receiving Currency")) {
//                        MyApplication.showErrorToast(InternationalRemittance.this, getString(R.string.plz_select_receive_currency));
//                        return;
//                    }
                    if (isFormatting) {
                        return;
                    }

                    if (s.length()>1) {
                        formatInput(edittext_amount, s, s.length(), s.length());
                        // callApiExchangeRate();
                        tvNext.setText(getString(R.string.Calculate));
                        nextbtn="calculation";

                    } else {
                        convertionRate_first_page.setText("");
                        fees_first_page.setText("");
                        tax_first_page.setText("");
                        //amountTobePaid_first_page.setText("");
                        amountTobeCharged_first_page.setText("");
                        edittext_amount_pay.getText().clear();
                    }

                    isFormatting = false;

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
        if(spinner_senderCurrency.getText().toString().equals(getString(R.string.select_currencey_star))) {
            MyApplication.showErrorToast(localC,getString(R.string.send_curr_empyty));
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
        if(edittext_amount.getText().toString().trim().replace(",","").isEmpty()) {
            MyApplication.showErrorToast(localC,getString(R.string.val_amount));
            return;
        }
        if(edittext_amount.getText().toString().trim().replace(",","").equals("0")||edittext_amount.getText().toString().trim().replace(",","").equals(".")||edittext_amount.getText().toString().trim().replace(",","").equals(".0")||
                edittext_amount.getText().toString().trim().replace(",","").equals("0.")||edittext_amount.getText().toString().trim().replace(",","").equals("0.0")||edittext_amount.getText().toString().trim().replace(",","").equals("0.00")){
            MyApplication.showErrorToast(localC,getString(R.string.val_valid_amount));
            return;
        }

        if(Double.parseDouble(edittext_amount.getText().toString().trim().replace(",",""))<MyApplication.RemittanceMinValue) {
            MyApplication.showErrorToast(localC,getString(R.string.val_amount_min)+" "+MyApplication.RemittanceMinValue);
            return ;
        }

        if(Double.parseDouble(edittext_amount.getText().toString().trim().replace(",",""))>MyApplication.RemittanceMaxValue) {
            MyApplication.showErrorToast(localC,getString(R.string.val_amount_max)+" "+MyApplication.RemittanceMaxValue);
            return ;


        }
        MyApplication.showloader(localC,getString(R.string.pleasewait));
        callApiExchangeRate();

        if(nextbtn.equalsIgnoreCase("next")){
            Intent i = new Intent(localC, CashtoWalletSenderKYC.class);
            startActivity(i);
        }else if(nextbtn.equalsIgnoreCase("calculation")){
            callApiExchangeRate();
        }


      /*  Intent i = new Intent(localC, CashtoWalletSenderKYC.class);
        startActivity(i);*/
    }

    private void callApiserviceProvider() {
        try {

            API.GET("ewallet/api/v1/serviceProvider/serviceCategory?serviceCode=100002&serviceCategoryCode=100061&status=Y",
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
                                    spinnerDialogSerProvider = new SpinnerDialog(localC, serviceProviderList, getString(R.string.val_select_service_provider), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation


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
                                                    data.optInt("id"), data.optInt("id"),
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

                                            spinner_senderCountry.setText(data.optString("name"));
                                            spinner_receiverCountry.setText(data.optString("name"));
                                            sendCountryCode = data.optString("code");
                                            sendCountryName = data.optString("name");
                                            recCountryCode = recCountryCode;
                                            recCountryName = sendCountryName;
                                            System.out.println("get list"+data.optString("currencyCode"));





                                        }
                                    }


                                    spinnerDialogSendingCountry= new SpinnerDialog(localC, sendingCountryList, getString(R.string.valid_select_country), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
                                    spinnerDialogSendingCountry.setCancellable(true); // for cancellable
                                    spinnerDialogSendingCountry.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogSendingCountry.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spinner_senderCountry.setText(item);
                                            spinner_senderCountry.setTag(position);
                                            // spinner_senderCurrency.setText(getString(R.string.sending_currencey_star));
                                            sendigncurrecncyText.setVisibility(View.VISIBLE);
                                            //   txt_benefi_phone.setText(benefiCountryModelList.get(position).dialCode);

                                            callApiSendCurrencynew();
                                            // callApiSendCurrency(sendCountryModelList.get(position).getCode());
                                        }
                                    });

                                    spinnerDialogRecCountry= new SpinnerDialog(localC, recCountryList, getString(R.string.valid_select_country), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
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
                                            // callApiSendCurrency(recCountryModelList.get(position).getCode());
                                            callApiSendCurrencynew();
                                        }
                                    });

                                    callApiSendCurrencynew();
                                    // callApiSendCurrency(MyApplication.getSaveString("COUNTRYCODE_AGENT", localC));

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

/*
    private void callApiSendCurrency(String code) {
        try {

            API.GET("ewallet/api/v1/countryCurrency/country/"+code,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            System.out.println("get wallet");

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
                                        if(data.optBoolean("outBound")) {
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
                                        }

                                        if(data.optBoolean("inBound")) {
                                            recCurrencyModelList.add(new CountryCurrencyInfoModel.CountryCurrency(
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

                                            recCurrencyList.add(data.optString("currCode").trim());
                                        }


                                    }

                                    tvAmtCurr.setText("");
                                    for(int i=0;i<sendCurrencyModelList.size();i++){
                                        if(countryCurrObj.optString("currencySymbol").equalsIgnoreCase(
                                                sendCurrencyModelList.get(i).getCurrencySymbol()
                                        )){
                                            spinner_senderCurrency.setText(sendCurrencyModelList.get(i).getCurrCode());
                                            spinner_senderCurrency.setTag(i);
                                            sendigncurrecncyText.setVisibility(View.VISIBLE);
                                            fromCurrency = sendCurrencyModelList.get(i).getCurrCode();
                                            fromCurrencySymbol = sendCurrencyModelList.get(i).getCurrencySymbol();
                                            fromCurrencyCode = sendCurrencyModelList.get(i).getCurrencyCode();
                                            tvAmtCurr.setText(fromCurrencySymbol);
                                            // txt_curr_symbol_paid.setText(benefiCurrencyModelList.get(position).currencySymbol);
                                            edittext_amount.getText().clear();
                                            edittext_amount_pay.getText().clear();

                                        }
                                    }

                                    spinnerDialogSendingCurr = new SpinnerDialog(localC, sendCurrencyList, getString(R.string.select_currency), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
                                    spinnerDialogSendingCurr.setCancellable(true); // for cancellable
                                    spinnerDialogSendingCurr.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogSendingCurr.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spinner_senderCurrency.setText(item);
                                            spinner_senderCurrency.setTag(position);
                                            sendigncurrecncyText.setVisibility(View.VISIBLE);
                                            fromCurrency = sendCurrencyModelList.get(position).getCurrCode();
                                            fromCurrencySymbol = sendCurrencyModelList.get(position).getCurrencySymbol();
                                            fromCurrencyCode = sendCurrencyModelList.get(position).getCurrencyCode();
                                            tvAmtCurr.setText(fromCurrencySymbol);
                                            // txt_curr_symbol_paid.setText(benefiCurrencyModelList.get(position).currencySymbol);
                                            edittext_amount.getText().clear();
                                            edittext_amount_pay.getText().clear();
                                        }
                                    });

                                    System.out.println("Rece list Local  "+recCurrencyModelList.toString());
                                    System.out.println("Send list Local  "+sendCountryModelList.toString());

                                    tvAmtPaidCurr.setText("GNF");
                                    for(int i=0;i<recCurrencyModelList.size();i++){
                                        if(countryCurrObj.optString("currencySymbol").equalsIgnoreCase(
                                                recCurrencyModelList.get(i).getCurrencySymbol()
                                        )){
                                            //  spinner_receiverCurrency.setText(recCurrencyModelList.get(i).getCurrCode() );
                                            //  spinner_receiverCurrency.setTag(i);
                                            receivingcurrecncyText.setVisibility(View.VISIBLE);
                                            toCurrency = recCurrencyModelList.get(i).getCurrCode();
                                            toCurrencySymbol = recCurrencyModelList.get(i).getCurrencySymbol();
                                            toCurrencyCode = recCurrencyModelList.get(i).getCurrencyCode();
                                            //  tvAmtPaidCurr.setText(toCurrencySymbol);
                                            // txt_curr_symbol_paid.setText(benefiCurrencyModelList.get(position).currencySymbol);

                                            edittext_amount.getText().clear();
                                            edittext_amount_pay.getText().clear();

                                        }
                                    }

                                    spinnerDialogRecCurr = new SpinnerDialog(localC, recCurrencyList, getString(R.string.select_currency), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
                                    spinnerDialogRecCurr.setCancellable(true); // for cancellable
                                    spinnerDialogRecCurr.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogRecCurr.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spinner_receiverCurrency.setText(item);
                                            spinner_receiverCurrency.setTag(position);
                                            receivingcurrecncyText.setVisibility(View.VISIBLE);
                                            toCurrency = recCurrencyModelList.get(position).getCurrCode();
                                            toCurrencySymbol = recCurrencyModelList.get(position).getCurrencySymbol();
                                            toCurrencyCode = recCurrencyModelList.get(position).getCurrencyCode();
                                            //  tvAmtPaidCurr.setText(toCurrencySymbol);
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
*/




    private void callApiSendCurrencynew() {
        try {

            API.GET("ewallet/api/v1/walletOwnerCountryCurrency/"+MyApplication.getSaveString("walletOwnerCode", LocalRemittanceCashtowalletActivity.this),
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                sendCurrencyList.clear();
                                sendCurrencyModelList.clear();
                                recCurrencyList.clear();
                                recCurrencyModelList.clear();

                                JSONArray countryCurrencyListArr=jsonObject.optJSONArray("walletOwnerCountryCurrencyList");
                                for (int i = 0; i < countryCurrencyListArr.length(); i++) {




                                    JSONObject data = countryCurrencyListArr.optJSONObject(i);
                                    if(data.optBoolean("outBound")) {
                                        sendCurrencyModelList.add(new CountryCurrencyInfoModel.CountryCurrency(
                                                data.optInt("id"),
                                                data.optString("code"),
                                                data.optString("countryCode"),
                                                data.optString("countryName"),
                                                data.optString("currencyCode"),
                                                data.optString("currencyName"),
                                                data.optString("currencySymbol"),
                                                data.optString("status"),
                                                data.optBoolean("inBound"),
                                                data.optBoolean("outBound")

                                        ));

                                        sendCurrencyList.add(data.optString("currencyName").trim());
                                    }

                                    if(data.optBoolean("inBound")) {
                                        recCurrencyModelList.add(new CountryCurrencyInfoModel.CountryCurrency(
                                                data.optInt("id"),
                                                data.optString("code"),
                                                data.optString("countryCode"),
                                                data.optString("countryName"),
                                                data.optString("currencyCode"),
                                                data.optString("currencyName"),
                                                data.optString("currencySymbol"),
                                                data.optString("status"),
                                                data.optBoolean("inBound"),
                                                data.optBoolean("outBound")
                                        ));

                                        recCurrencyList.add(data.optString("currencyName").trim());
                                    }

                                    sendigncurrecncyText.setVisibility(View.VISIBLE);

                                    System.out.println("get name"+data.optString("currencyName"));
                                   /* if(data.optString("currencyName").equalsIgnoreCase("GNF")) {
                                        spinner_senderCurrency.setText(sendCurrencyModelList.get(i).getCurrencyName());
                                        tvAmtCurr.setText(sendCurrencyModelList.get(i).getCurrencyName());
                                    }
*/
                                }

                                //tvAmtCurr.setText("");

/*
                                for(int i=0;i<sendCurrencyModelList.size();i++){
                                    if(countryCurrObj.optString("currencySymbol").equalsIgnoreCase(
                                            sendCurrencyModelList.get(i).getCurrencySymbol()
                                    )){
                                        spinner_senderCurrency.setText(sendCurrencyModelList.get(i).getCurrCode());
                                        spinner_senderCurrency.setTag(i);
                                        sendigncurrecncyText.setVisibility(View.VISIBLE);
                                        fromCurrency = sendCurrencyModelList.get(i).getCurrCode();
                                        fromCurrencySymbol = sendCurrencyModelList.get(i).getCurrencySymbol();
                                        fromCurrencyCode = sendCurrencyModelList.get(i).getCurrencyCode();
                                        tvAmtCurr.setText(fromCurrencySymbol);
                                        // txt_curr_symbol_paid.setText(benefiCurrencyModelList.get(position).currencySymbol);
                                        edittext_amount.getText().clear();
                                        edittext_amount_pay.getText().clear();

                                    }
                                }
*/

                                spinnerDialogSendingCurr = new SpinnerDialog(localC, sendCurrencyList, getString(R.string.select_currency), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
                                spinnerDialogSendingCurr.setCancellable(true); // for cancellable
                                spinnerDialogSendingCurr.setShowKeyboard(false);// for open keyboard by default
                                spinnerDialogSendingCurr.bindOnSpinerListener(new OnSpinerItemClick() {
                                    @Override
                                    public void onClick(String item, int position) {
                                        //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                        spinner_senderCurrency.setText(item);
                                        spinner_senderCurrency.setTag(position);
                                        sendigncurrecncyText.setVisibility(View.VISIBLE);
                                        fromCurrency=sendCurrencyModelList.get(position).getCountryName();
                                        fromCurrencynew = sendCurrencyModelList.get(position).getCurrCode();
                                        fromCurrencySymbol = sendCurrencyModelList.get(position).getCurrencySymbol();
                                        fromCurrencyCode = sendCurrencyModelList.get(position).getCurrencyCode();
                                        tvAmtCurr.setText(fromCurrencySymbol);
                                        // txt_curr_symbol_paid.setText(benefiCurrencyModelList.get(position).currencySymbol);
                                        edittext_amount.getText().clear();
                                        edittext_amount_pay.getText().clear();
                                        toCurrencySymbolnew ="GNF";

                                        System.out.println("get sending"+fromCurrencySymbol);

                                    }
                                });

                                System.out.println("Rece list Local  "+recCurrencyModelList.toString());
                                System.out.println("Send list Local  "+sendCountryModelList.toString());

                                tvAmtPaidCurr.setText("GNF");
/*
                                for(int i=0;i<recCurrencyModelList.size();i++){
                                    if(countryCurrObj.optString("currencySymbol").equalsIgnoreCase(
                                            recCurrencyModelList.get(i).getCurrencySymbol()
                                    )){
                                        //  spinner_receiverCurrency.setText(recCurrencyModelList.get(i).getCurrCode() );
                                        //  spinner_receiverCurrency.setTag(i);
                                        receivingcurrecncyText.setVisibility(View.VISIBLE);
                                        toCurrency = recCurrencyModelList.get(i).getCurrCode();
                                        toCurrencySymbol = recCurrencyModelList.get(i).getCurrencySymbol();
                                        toCurrencyCode = recCurrencyModelList.get(i).getCurrencyCode();
                                        //  tvAmtPaidCurr.setText(toCurrencySymbol);
                                        // txt_curr_symbol_paid.setText(benefiCurrencyModelList.get(position).currencySymbol);
                                        toCurrencySymbolnew ="GNF";

                                        edittext_amount.getText().clear();
                                        edittext_amount_pay.getText().clear();

                                    }
                                }
*/

                                spinnerDialogRecCurr = new SpinnerDialog(localC, recCurrencyList, getString(R.string.select_currency), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
                                spinnerDialogRecCurr.setCancellable(true); // for cancellable
                                spinnerDialogRecCurr.setShowKeyboard(false);// for open keyboard by default
                                spinnerDialogRecCurr.bindOnSpinerListener(new OnSpinerItemClick() {
                                    @Override
                                    public void onClick(String item, int position) {
                                        //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                        spinner_receiverCurrency.setText(item);
                                        spinner_receiverCurrency.setTag(position);
                                        receivingcurrecncyText.setVisibility(View.VISIBLE);
                                        toCurrency = recCurrencyModelList.get(position).getCurrCode();
                                        toCurrencySymbol = recCurrencyModelList.get(position).getCurrencySymbol();
                                        toCurrencyCode = recCurrencyModelList.get(position).getCurrencyCode();
                                        //  tvAmtPaidCurr.setText(toCurrencySymbol);
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
//                                    spinnerDialogRecCountry= new SpinnerDialog(localC, recCountryList, getString(R.string.valid_select_country), R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
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
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
    DecimalFormat df = new DecimalFormat("0.00",symbols);
    public static JSONArray taxConfigurationList;

    private void callApiExchangeRate() {
        try {
            //MyApplication.showloader(cashinC, "Please wait!");
            API.GET("ewallet/api/v1/exchangeRate/getAmountDetails?sendCurrencyCode="+
                            sendCurrencyModelList.get((Integer) spinner_senderCurrency.getTag()).getCurrencyCode()
                            +"&receiveCurrencyCode="+"100062"
                            +"&sendCountryCode="+ sendCountryCode+
                            "&receiveCountryCode="+"100092"
                            +"&currencyValue="+ edittext_amount.getText().toString().replace(",","") + "&channelTypeCode="+MyApplication.channelTypeCode+
                            "&serviceCode=" + serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode")+
                            "&serviceCategoryCode=" + serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode")
                            + "&serviceProviderCode=" + serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code")
                            + "&walletOwnerCode=" + MyApplication.getSaveString("walletOwnerCode", localC),

                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            System.out.println("International response======="+jsonObject.toString());
                            if (jsonObject != null) {
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    if(edittext_amount.getText().toString().trim().replace(",","").length()>0) {
                                        JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("exchangeRate");

                                        currencyValue = df.format(jsonObjectAmountDetails.optDouble("currencyValue"));
                                        fee = df.format(jsonObjectAmountDetails.optDouble("fee"));
                                        rate = jsonObjectAmountDetails.optString("value");
                                        exRateCode = jsonObjectAmountDetails.optString("code");

                                        //receiverFee= jsonObjectAmountDetails.optInt("receiverFee");
                                        //receiverTax = jsonObjectAmountDetails.optInt("receiverTax");
                                        //etAmountNew.setText(currencyValue);
                                        convertionRate_first_page.setText(MyApplication.addDecimalfive(rate));
                                        fees_first_page.setText(MyApplication.addDecimal(fee));
                                        edittext_amount_pay.setText(MyApplication.addDecimal(currencyValue));
                                        amount = edittext_amount.getText().toString().trim().replace(",","");
                                        tvNext.setText(getString(R.string.next));

                                        nextbtn="next";

//                                    int tax = receiverFee+receiverTax;
//                                    if(currencyValue<tax){
//                                        tvSend.setVisibility(View.GONE);
//                                        MyApplication.showErrorToast(tononsubscriberC,getString(R.string.fee_tax_greater_than_trans_amt));
//                                    }else{
//                                        tvSend.setVisibility(View.VISIBLE);
//                                    }

                                        if (jsonObjectAmountDetails.has("taxConfigurationList")) {
                                            taxConfigurationList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
                                            tax_first_page.setText(MyApplication.addDecimal(""+taxConfigurationList.optJSONObject(0).optDouble("value")));
                                            amountTobeCharged_first_page.setText(MyApplication.addDecimal(
                                                    df.format(Double.parseDouble(edittext_amount.getText().toString().trim().replace(",",""))
                                                            + taxConfigurationList.optJSONObject(0).optDouble("value")+
                                                            jsonObjectAmountDetails.optDouble("fee"))));

                                        } else {
                                            taxConfigurationList = null;
                                            tax_first_page.setText(MyApplication.addDecimal("0.00"));
                                            amountTobeCharged_first_page.setText(MyApplication.addDecimal(""+
                                                    Double.parseDouble(edittext_amount.getText().toString().trim().replace(",",""))));

                                        }
                                        if(jsonObjectAmountDetails.has("receiverTax")) {
                                            taxConfigurationList = null;
                                            tax_first_page.setText(MyApplication.addDecimal("0.00"));
                                            amountTobeCharged_first_page.setText(MyApplication.addDecimal(""+
                                                    Double.parseDouble(edittext_amount.getText().toString().trim().replace(",",""))));

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

    private boolean isFormatting;
    private int prevCommaAmount;
    private void formatInput(EditText editText,CharSequence s, int start, int count) {

        if(MyApplication.checkMinMax(localC,s,editText
                ,MyApplication.RemittanceMinValue,MyApplication.RemittanceMaxValue)){
            return;
        }
        if( MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")){
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
