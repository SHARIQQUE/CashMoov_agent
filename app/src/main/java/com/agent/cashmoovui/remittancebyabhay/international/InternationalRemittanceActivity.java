package com.agent.cashmoovui.remittancebyabhay.international;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.agent.cashmoovui.LogoutAppCompactActivity;
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
import com.agent.cashmoovui.remittancebyabhay.cashtowallet.CashtoWalletSenderKYC;
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

public class InternationalRemittanceActivity extends LogoutAppCompactActivity implements View.OnClickListener {
    public static InternationalRemittanceActivity internationalC;
    ImageView imgBack,imgHome;
    private TextView spinner_provider,spinner_senderCountry,spinner_senderCurrency,
            spinner_receiverCountry,spinner_receiverCurrency,convertionRate_first_page,fees_first_page,
            tax_first_page,amountTobeCharged_first_page,tvAmtCurr,tvAmtPaidCurr,tvNext;
    private EditText edittext_amount,edittext_amount_pay;

    String amountpay_temp_str="";
    String mobilelength="";

    Double amount_reverse=0.0;

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
    private boolean isAmt=true;
    private boolean isAmtPaid=true;
    String amoutvalidationcheck="";
    private String nextbtn="";
    private boolean isCheckAmountPaid=true;
    private boolean isCheckAmount=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_international_remittance);
        internationalC=this;
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
                MyApplication.hideKeyboard(internationalC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(internationalC);
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

        //  edittext_amount_pay.setEnabled(false);

        edittext_amount.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(MyApplication.amountLength)});


        edittext_amount_pay.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(MyApplication.amountLength)});
        spinner_provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - MyApplication.mLastClickTime < 1000) { // 1000 = 1second
                    return;
                }
                MyApplication.mLastClickTime = SystemClock.elapsedRealtime();
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
                if (SystemClock.elapsedRealtime() - MyApplication.mLastClickTime < 1000) { // 1000 = 1second
                    return;
                }
                MyApplication.mLastClickTime = SystemClock.elapsedRealtime();
                if (spinnerDialogSendingCurr!=null)
                    spinnerDialogSendingCurr.showSpinerDialog();
            }
        });
        spinner_receiverCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - MyApplication.mLastClickTime < 1000) { // 1000 = 1second
                    return;
                }
                MyApplication.mLastClickTime = SystemClock.elapsedRealtime();
                if (spinnerDialogRecCountry!=null)
                    spinnerDialogRecCountry.showSpinerDialog();
            }
        });
        spinner_receiverCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - MyApplication.mLastClickTime < 1000) { // 1000 = 1second
                    return;
                }
                MyApplication.mLastClickTime = SystemClock.elapsedRealtime();
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
                if(isCheckAmount){


               //isAmt=true;
                    isAmtPaid = true;
                convertionRate_first_page.setText("");
                fees_first_page.setText("");
                tax_first_page.setText("");
                //amountTobePaid_first_page.setText("");
                amountTobeCharged_first_page.setText("");
                edittext_amount_pay.getText().clear();
                tvNext.setText(getString(R.string.Calculate));
            }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (new InternetCheck().isConnected(internationalC)) {

//                    if (spinner_receiverCurrency.getSelectedItem()==null||spinner_receiverCurrency.getSelectedItem().toString().isEmpty()||spinner_receiverCurrency.getSelectedItem().toString().equals("Receiving Currency")) {
//                        MyApplication.showErrorToast(InternationalRemittance.this, getString(R.string.plz_select_receive_currency));
//                        return;
//                    }


                    MyApplication applicationComponentClass = (MyApplication) getApplicationContext();
                    String languageToUse = applicationComponentClass.getmSharedPreferences().getString("languageToUse", "");

                   /* if(languageToUse.equalsIgnoreCase("fr")){
                        callApiExchangeRate();
                    }else {
                    }*/
                    if (isFormatting) {
                        return;
                    }


                    if (s.length() > 1) {


                        formatInput(edittext_amount, s, s.length(), s.length());

                        if (isAmt) {
                            edittext_amount_pay.setEnabled(false);
                            isAmtPaid = false;
                          //  callApiExchangeRate();
                            amoutvalidationcheck="firstvalidation";
                            tvNext.setText(getString(R.string.Calculate));
                            nextbtn="calculation";
                        } else {
                            edittext_amount_pay.setEnabled(true);
                            isAmtPaid = true;
                        }
                    } else {
                        isAmtPaid = true;
                        edittext_amount_pay.setEnabled(true);
                        convertionRate_first_page.setText("");
                        fees_first_page.setText("");
                        tax_first_page.setText("");
                        //amountTobePaid_first_page.setText("");
                        amountTobeCharged_first_page.setText("");
                        edittext_amount_pay.getText().clear();
                    }

                    isFormatting = false;



                       /* if (s.length()>1) {
                            formatInput(edittext_amount, s, s.length(), s.length());

                            callApiExchangeRate();

                        }else {
                            convertionRate_first_page.setText("");
                            fees_first_page.setText("");
                            tax_first_page.setText("");
                            //amountTobePaid_first_page.setText("");
                            amountTobeCharged_first_page.setText("");
                            edittext_amount_pay.getText().clear();
                        }

                        isFormatting = false;

*/






                } else {
                    Toast.makeText(internationalC, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                }
            }
        });


        edittext_amount_pay.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isCheckAmountPaid){


                isAmt = true;
                convertionRate_first_page.setText("");
                fees_first_page.setText("");
                tax_first_page.setText("");
                //amountTobePaid_first_page.setText("");
                amountTobeCharged_first_page.setText("");
                edittext_amount.getText().clear();
                    tvNext.setText(getString(R.string.Calculate));

                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (isFormatting) {
                    return;
                }

                if(s.length()>1) {


                    formatInput(edittext_amount_pay, s, s.length(), s.length());

                    if (isAmtPaid) {
                        edittext_amount.setEnabled(false);
                        isAmt = false;
                        //callApiExchangeRatenew();
                        amoutvalidationcheck="secondvalidation";
                        tvNext.setText(getString(R.string.Calculate));
                        nextbtn="calculation";
                    } else {
                        edittext_amount.setEnabled(true);
                        isAmt = true;
                    }
                } else {
                    isAmt = true;
                    edittext_amount.setEnabled(true);
                    convertionRate_first_page.setText("");
                    fees_first_page.setText("");
                    tax_first_page.setText("");
                    //amountTobePaid_first_page.setText("");
                    amountTobeCharged_first_page.setText("");
                    edittext_amount.getText().clear();
                }

                isFormatting = false;



            }


        });


        callApiSendCurrencynew();

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        tvNext.setOnClickListener(internationalC);
    }

    @Override
    public void onClick(View v) {


        if(spinner_provider.getText().toString().equals(getString(R.string.valid_select_provider))) {
            MyApplication.showErrorToast(internationalC,getString(R.string.plz_select_provider));
            return;
        }
        if(spinner_senderCountry.getText().toString().equals(getString(R.string.sending_country_star))) {
            MyApplication.showErrorToast(internationalC,getString(R.string.sending_country_star));
            return;
        }
        if(spinner_senderCurrency.getText().toString().equals(getString(R.string.sending_currencey_star))) {
            MyApplication.showErrorToast(internationalC,getString(R.string.sending_currencey_star));
            return;
        }
        if(spinner_receiverCountry.getText().toString().equals(getString(R.string.receive_country))) {
            MyApplication.showErrorToast(internationalC,getString(R.string.receive_country));
            return;
        }
        if(spinner_receiverCurrency.getText().toString().equals(getString(R.string.valid_beneficiary_currency))) {
            MyApplication.showErrorToast(internationalC,getString(R.string.receive_courency));
            return;
        }

        if(isAmt){
            //MyApplication.showToast(internationalC,"amount");
            if(edittext_amount.getText().toString().trim().replace(",","").isEmpty()) {
                MyApplication.showErrorToast(internationalC,getString(R.string.val_amount));
                return;
            }
            if(edittext_amount.getText().toString().trim().replace(",","").equals("0")||edittext_amount.getText().toString().trim().replace(",","").equals(".")||edittext_amount.getText().toString().trim().replace(",","").equals(".0")||
                    edittext_amount.getText().toString().trim().replace(",","").equals("0.")||edittext_amount.getText().toString().trim().replace(",","").equals("0.0")||edittext_amount.getText().toString().trim().replace(",","").equals("0.00")){
                MyApplication.showErrorToast(internationalC,getString(R.string.val_valid_amount));
                return;
            }
            if (Double.parseDouble(edittext_amount.getText().toString().trim().replace(",", "")) < MyApplication.RemittanceMinValue) {
                MyApplication.showErrorToast(internationalC, getString(R.string.val_amount_min) + " " + MyApplication.RemittanceMinValue);
                return;
            }

            if (Double.parseDouble(edittext_amount.getText().toString().trim().replace(",", "")) > MyApplication.RemittanceMaxValue) {
                MyApplication.showErrorToast(internationalC, getString(R.string.val_amount_max) + " " + MyApplication.RemittanceMaxValue);
                return;


            }
            MyApplication.showloader(internationalC,getString(R.string.pleasewait));
          //  callApiExchangeRate();

            if(nextbtn.equalsIgnoreCase("calculation")){
                isCheckAmountPaid=false;

                callApiExchangeRate();
                return;
            }


        }else{
            //MyApplication.showToast(internationalC,"amountpay");
            if(edittext_amount_pay.getText().toString().trim().replace(",","").isEmpty()) {
                MyApplication.showErrorToast(internationalC,getString(R.string.val_amount));
                return;
            }
            if(edittext_amount_pay.getText().toString().trim().replace(",","").equals("0")||edittext_amount_pay.getText().toString().trim().replace(",","").equals(".")||edittext_amount.getText().toString().trim().replace(",","").equals(".0")||
                    edittext_amount_pay.getText().toString().trim().replace(",","").equals("0.")||edittext_amount_pay.getText().toString().trim().replace(",","").equals("0.0")||edittext_amount.getText().toString().trim().replace(",","").equals("0.00")){
                MyApplication.showErrorToast(internationalC,getString(R.string.val_valid_amount));
                return;
            }
           /* if (Double.parseDouble(edittext_amount_pay.getText().toString().trim().replace(",", "")) < MyApplication.RemittanceMinValue) {
                MyApplication.showErrorToast(internationalC, getString(R.string.val_amount_min) + " " + MyApplication.RemittanceMinValue);
                return;
            }

            if (Double.parseDouble(edittext_amount_pay.getText().toString().trim().replace(",", "")) > MyApplication.RemittanceMaxValue) {
                MyApplication.showErrorToast(internationalC, getString(R.string.val_amount_max) + " " + MyApplication.RemittanceMaxValue);
                return;


            }*/
            MyApplication.showloader(internationalC,getString(R.string.pleasewait));
            //  callApiExchangeRate();

            if(nextbtn.equalsIgnoreCase("calculation")){
                isCheckAmount=false;
                callApiExchangeRatenew();

                return;
            }
        }


        Intent i = new Intent(internationalC, InternationalRemittanceSenderKYC.class);
        MyApplication.saveString("mobilelength",mobilelength,InternationalRemittanceActivity.this);
        startActivity(i);




      /*  Intent i = new Intent(internationalC, InternationalRemittanceSenderKYC.class);
        MyApplication.saveString("mobilelength",mobilelength,InternationalRemittanceActivity.this);
        startActivity(i);*/
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
                                    spinnerDialogSerProvider = new SpinnerDialog(internationalC, serviceProviderList, getString(R.string.val_select_service_provider), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation


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
                                    MyApplication.showToast(internationalC,jsonObject.optString("resultDescription", "N/A"));
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
                                        if (data.optString("code").equalsIgnoreCase(MyApplication.getSaveString("COUNTRYCODE_AGENT", internationalC))) {
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
                                            sendCountryCode = data.optString("code");
                                            sendCountryName = data.optString("name");

                                        }
                                    }

                                    spinnerDialogSendingCountry= new SpinnerDialog(internationalC, sendingCountryList, getString(R.string.valid_select_country), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
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

                                    callApiSendCurrency(MyApplication.getSaveString("COUNTRYCODE_AGENT", internationalC));

                                } else {
                                    MyApplication.showToast(internationalC,jsonObject.optString("resultDescription", "N/A"));
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
                                if(jsonObject.optString("resultCode", "  ").equalsIgnoreCase("0")){
                                    JSONObject countryCurrObj = jsonObject.optJSONObject("country");
                                    JSONArray countryCurrencyListArr = countryCurrObj.optJSONArray("countryCurrencyList");
                                    for (int i = 0; i < countryCurrencyListArr.length(); i++) {
                                        JSONObject data = countryCurrencyListArr.optJSONObject(i);
                                        if( data.optBoolean("inBound")) {
                                            if (compareCurrency.contains(data.optString("currencyCode"))) {
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
                                        }
                                    }

                                    tvAmtCurr.setText("");
                                    for(int i=0;i<sendCurrencyModelList.size();i++){
                                        if(countryCurrObj.optString("currencySymbol").equalsIgnoreCase(
                                                sendCurrencyModelList.get(i).getCurrencySymbol()
                                        )){
                                            spinner_senderCurrency.setText(sendCurrencyModelList.get(i).getCurrCode() );
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

                                    spinnerDialogSendingCurr = new SpinnerDialog(internationalC, sendCurrencyList, getString(R.string.select_currency), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
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

                                    callApiRecCountry();

                                } else {
                                    MyApplication.showToast(internationalC,jsonObject.optString("resultDescription", "  "));
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

    private void callApiRecCountry() {
        try {

            API.GET("ewallet/api/v1/countryRemittance/all",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            if (jsonObject != null) {
                                recCountryList.clear();
                                recCountryModelList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("countryRemittanceList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        if (!MyApplication.getSaveString("COUNTRYCODE_AGENT", internationalC).equalsIgnoreCase(data.optString("countryCode"))) {
                                            if (data.optBoolean("remitReceiving")) {
                                                recCountryModelList.add(new CountryRemittanceInfoModel.RemitCountry(

                                                        data.optInt("id"),
                                                        data.optString("code"),
                                                        data.optString("countryCode"),
                                                        data.optString("countryIsoCode"),
                                                        data.optString("countryName"),
                                                        data.optString("createdBy"),
                                                        data.optString("creationDate"),
                                                        data.optString("currencyCode"),
                                                        data.optString("currencySymbol"),
                                                        data.optString("dialCode"),
                                                        data.optString("mobileLength"),
                                                        data.optString("modificationDate"),
                                                        data.optString("modifiedBy"),
                                                        data.optString("state"),
                                                        data.optString("status"),
                                                        data.optBoolean("remitReceiving"),
                                                        data.optBoolean("remitSending")
                                                ));

                                                recCountryList.add(data.optString("countryName").trim());

                                            }
                                        }
                                    }

                                    spinnerDialogRecCountry= new SpinnerDialog(internationalC, recCountryList, getString(R.string.valid_select_country), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
                                    spinnerDialogRecCountry.setCancellable(true); // for cancellable
                                    spinnerDialogRecCountry.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogRecCountry.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spinner_receiverCountry.setText(item);
                                            spinner_receiverCountry.setTag(position);
                                            spinner_receiverCurrency.setText(getString(R.string.valid_beneficiary_currency));
                                            //   txt_benefi_phone.setText(benefiCountryModelList.get(position).dialCode);
                                            recCountryCode = recCountryModelList.get(position).getCountryCode();
                                            recCountryName = recCountryModelList.get(position).getCountryName();
                                            mobilelength=String.valueOf(recCountryModelList.get(position).getMobileLength());
                                            System.out.println("get length"+mobilelength);

                                            callApiRecCurrency(recCountryCode);
                                        }
                                    });


                                } else {
                                    MyApplication.showToast(internationalC,jsonObject.optString("resultDescription", "N/A"));
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

    private void callApiRecCurrency(String code) {
        try {

            API.GET("ewallet/api/v1/countryCurrency/country/"+code,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                recCurrencyList.clear();
                                recCurrencyModelList.clear();
                                if(jsonObject.optString("resultCode", "  ").equalsIgnoreCase("0")){
                                    JSONObject countryCurrObj = jsonObject.optJSONObject("country");
                                    JSONArray countryCurrencyListArr = countryCurrObj.optJSONArray("countryCurrencyList");
                                    for (int i = 0; i < countryCurrencyListArr.length(); i++) {
                                        JSONObject data = countryCurrencyListArr.optJSONObject(i);
                                        if(data.optBoolean("outBound")) {
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

                                    spinnerDialogRecCurr = new SpinnerDialog(internationalC, recCurrencyList, getString(R.string.select_currency), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
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

                                    callApiRecCountry();

                                } else {
                                    MyApplication.showToast(internationalC,jsonObject.optString("resultDescription", "  "));
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

    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
    DecimalFormat df = new DecimalFormat("0.00",symbols);
    public static JSONArray taxConfigurationList;

    private void callApiExchangeRate() {
        try {
            MyApplication.saveString("amount1",edittext_amount.getText().toString().replace(",",""),getApplicationContext());

            //MyApplication.showloader(cashinC, "Please wait!");
            API.GET("ewallet/api/v1/exchangeRate/getAmountDetails?sendCurrencyCode="+
                            sendCurrencyModelList.get((Integer) spinner_senderCurrency.getTag()).getCurrencyCode()
                            +"&receiveCurrencyCode="+recCurrencyModelList.get((Integer) spinner_receiverCurrency.getTag()).getCurrencyCode()
                            +"&sendCountryCode="+ sendCountryCode+
                            "&receiveCountryCode="+recCountryCode
                            +"&currencyValue="+ edittext_amount.getText().toString().replace(",","") + "&channelTypeCode="+MyApplication.channelTypeCode+
                            "&serviceCode=" + serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode")+
                            "&serviceCategoryCode=" + serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode")
                            + "&serviceProviderCode=" + serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code")
                            + "&walletOwnerCode=" + MyApplication.getSaveString("walletOwnerCode", internationalC),

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
                                        fees_first_page.setText(MyApplication.addDecimal(fee+""));
                                        edittext_amount_pay.setText(MyApplication.addDecimal(currencyValue));
                                        amount = edittext_amount.getText().toString().trim().replace(",","");
                                        amountpay_temp_str=amount;
                                        MyApplication.saveString("amount",amountpay_temp_str,getApplicationContext());

                                        System.out.println("get amount sdf"+amount);
                                        MyApplication.saveString("amountformat",amount,getApplicationContext());
                                        MyApplication.saveString("engamout",amountpay_temp_str,getApplicationContext());
                                        System.out.println("get sonufff"+amountpay_temp_str);
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
                                            Double amountcharge=Double.parseDouble(amount)+ taxConfigurationList.optJSONObject(0).optDouble("value")+
                                                    jsonObjectAmountDetails.optDouble("fee");
                                            amountTobeCharged_first_page.setText(MyApplication.addDecimal(amountcharge+""));

                                            MyApplication.saveString("amountchagre",amountTobeCharged_first_page.getText().toString(),getApplicationContext());
                                        } else {
                                            taxConfigurationList = null;
                                            tax_first_page.setText(MyApplication.addDecimal("0.00"));
                                            Double teret=Double.parseDouble(edittext_amount.getText().toString().trim().replace(",",""))+ jsonObjectAmountDetails.optDouble("fee");
                                            amountTobeCharged_first_page.setText(MyApplication.addDecimal(teret+""));
                                            MyApplication.saveString("amountchagre",amountTobeCharged_first_page.getText().toString(),getApplicationContext());

                                        }

                                        if(jsonObjectAmountDetails.has("receiverTax")) {
                                            taxConfigurationList=null;
                                            tax_first_page.setText(MyApplication.addDecimal("0.00"));
                                            amountTobeCharged_first_page.setText(MyApplication.addDecimal(""+Double.parseDouble(edittext_amount.getText().toString().trim().replace(",",""))));
                                            MyApplication.saveString("amountchagre",amountTobeCharged_first_page.getText().toString(),getApplicationContext());

                                        }
                                        tvNext.setEnabled(true);
                                    }
                                } else {
                                    tvNext.setEnabled(false);
                                    MyApplication.showToast(internationalC,jsonObject.optString("resultDescription", "N/A"));
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

    private void callApiExchangeRate_again() {
        try {

            MyApplication.saveString("amount1",amountpay_temp_str,getApplicationContext());


            //MyApplication.showloader(cashinC, "Please wait!");
            API.GET("ewallet/api/v1/exchangeRate/getAmountDetails?sendCurrencyCode="+
                            sendCurrencyModelList.get((Integer) spinner_senderCurrency.getTag()).getCurrencyCode()
                            +"&receiveCurrencyCode="+recCurrencyModelList.get((Integer) spinner_receiverCurrency.getTag()).getCurrencyCode()
                            +"&sendCountryCode="+ sendCountryCode+
                            "&receiveCountryCode="+recCountryCode
                            +"&currencyValue="+ amountpay_temp_str + "&channelTypeCode="+MyApplication.channelTypeCode+
                            "&serviceCode=" + serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode")+
                            "&serviceCategoryCode=" + serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode")
                            + "&serviceProviderCode=" + serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code")
                            + "&walletOwnerCode=" + MyApplication.getSaveString("walletOwnerCode", internationalC),

                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            // MyApplication.hideLoader();
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
                                        fees_first_page.setText(MyApplication.addDecimal(fee+""));

                                        //  edittext_amount_pay.setText(MyApplication.addDecimal(currencyValue)); // annu


                                        amount = edittext_amount.getText().toString().trim().replace(",","");
                                        System.out.println("get amount sdf"+amount);
                                        MyApplication.saveString("amountformat",amountpay_temp_str,getApplicationContext());

                                        //  MyApplication.saveString("amountformat",edittext_amount.getText().toString(),getApplicationContext());


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
                                            amountTobeCharged_first_page.setText(MyApplication.addDecimal(df.format(Double.parseDouble(amountpay_temp_str)
                                                    + taxConfigurationList.optJSONObject(0).optDouble("value")+
                                                    jsonObjectAmountDetails.optDouble("fee"))));
                                            MyApplication.saveString("amountchagre",amountTobeCharged_first_page.getText().toString(),getApplicationContext());


                                        } else {
                                            taxConfigurationList = null;
                                            tax_first_page.setText(MyApplication.addDecimal("0.00"));
                                            Double teret=Double.parseDouble(amountpay_temp_str)+ jsonObjectAmountDetails.optDouble("fee");
                                            amountTobeCharged_first_page.setText(MyApplication.addDecimal(teret+""));

                                            // amountTobeCharged_first_page.setText(MyApplication.addDecimal(""+Double.parseDouble(amountpay_temp_str)));
                                            MyApplication.saveString("amountchagre",amountTobeCharged_first_page.getText().toString(),getApplicationContext());

                                        }

                                        if(jsonObjectAmountDetails.has("receiverTax")) {
                                            taxConfigurationList=null;
                                            tax_first_page.setText(MyApplication.addDecimal("0.00"));
                                            amountTobeCharged_first_page.setText(MyApplication.addDecimal(""+Double.parseDouble(amountpay_temp_str)));
                                            MyApplication.saveString("amountchagre",amountTobeCharged_first_page.getText().toString(),getApplicationContext());

                                        }

                                        tvNext.setEnabled(true);
                                    }
                                } else {
                                    tvNext.setEnabled(false);
                                    MyApplication.showToast(internationalC,jsonObject.optString("resultDescription", "N/A"));
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


    private void callApiExchangeRatenew() {
        try {
            //MyApplication.showloader(cashinC, "Please wait!");
            API.GET("ewallet/api/v1/exchangeRate/getAmountDetails?sendCurrencyCode="+
                            sendCurrencyModelList.get((Integer) spinner_senderCurrency.getTag()).getCurrencyCode()
                            +"&receiveCurrencyCode="+recCurrencyModelList.get((Integer) spinner_receiverCurrency.getTag()).getCurrencyCode()
                            +"&sendCountryCode="+ sendCountryCode+
                            "&receiveCountryCode="+recCountryCode
                            +"&currencyValue="+ edittext_amount_pay.getText().toString().replace(",","") + "&channelTypeCode="+MyApplication.channelTypeCode+
                            "&serviceCode=" + serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode")+
                            "&serviceCategoryCode=" + serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode")
                            + "&serviceProviderCode=" + serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code")
                            + "&walletOwnerCode=" + MyApplication.getSaveString("walletOwnerCode", internationalC),

                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                             MyApplication.hideLoader();
                            System.out.println("International response======="+jsonObject.toString());
                            if (jsonObject != null) {
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    if(edittext_amount_pay.getText().toString().trim().replace(",","").length()>0) {
                                        JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("exchangeRate");

                                        currencyValue = df.format(jsonObjectAmountDetails.optDouble("currencyValue"));
                                        fee = df.format(jsonObjectAmountDetails.optDouble("fee"));
                                        rate = jsonObjectAmountDetails.optString("value");
                                        exRateCode = jsonObjectAmountDetails.optString("code");
                                        //receiverFee= jsonObjectAmountDetails.optInt("receiverFee");
                                        //receiverTax = jsonObjectAmountDetails.optInt("receiverTax");
                                        //etAmountNew.setText(currencyValue);


                                        //  convertionRate_first_page.setText(MyApplication.addDecimalthreenew(rate));
                                        //  fees_first_page.setText(MyApplication.addDecimal(fee+""));


                                        Double amountpay=Double.parseDouble(edittext_amount_pay.getText().toString().trim().replace(",",""))/Double.parseDouble(rate);

                                        try {


                                            Double amountpay_temp_double = Double.parseDouble(edittext_amount_pay.getText().toString().trim().replace(",","")) / Double.parseDouble(rate);
                                            amountpay_temp_str = df.format(amountpay_temp_double);
                                            System.out.println("get abhi"+amountpay);

                                            MyApplication.saveString("amount",amountpay_temp_str,getApplicationContext());




                                            edittext_amount.setText(MyApplication.addDecimal(amountpay+""));

                                            amount = edittext_amount.getText().toString().trim().replace(",","");

                                            tvNext.setText(getString(R.string.next));

                                            nextbtn="next";

                                            MyApplication.saveString("amount",edittext_amount.getText().toString(),getApplicationContext());

                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }

//                                    int tax = receiverFee+receiverTax;
//                                    if(currencyValue<tax){
//                                        tvSend.setVisibility(View.GONE);
//                                        MyApplication.showErrorToast(tononsubscriberC,getString(R.string.fee_tax_greater_than_trans_amt));
//                                    }else{
//                                        tvSend.setVisibility(View.VISIBLE);
//                                    }

  /*                                      if (jsonObjectAmountDetails.has("taxConfigurationList")) {
                                            taxConfigurationList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
                                            tax_first_page.setText(MyApplication.addDecimal(""+taxConfigurationList.optJSONObject(0).optDouble("value")));
                                            amountTobeCharged_first_page.setText(MyApplication.addDecimal(df.format(Double.parseDouble(edittext_amount.getText().toString().trim().replace(",",""))
                                                    + taxConfigurationList.optJSONObject(0).optDouble("value")+
                                                    jsonObjectAmountDetails.optDouble("fee"))));

                                        } else {
                                            taxConfigurationList = null;
                                            tax_first_page.setText(MyApplication.addDecimal("0.00"));
                                            amountTobeCharged_first_page.setText(MyApplication.addDecimal(""+Double.parseDouble(edittext_amount.getText().toString().trim().replace(",",""))));

                                        }

                                        if(jsonObjectAmountDetails.has("receiverTax")) {
                                            taxConfigurationList=null;
                                            tax_first_page.setText(MyApplication.addDecimal("0.00"));
                                            amountTobeCharged_first_page.setText(MyApplication.addDecimal(""+Double.parseDouble(edittext_amount.getText().toString().trim().replace(",",""))));

                                        }

   */



                                        callApiExchangeRate_again();

                                        tvNext.setEnabled(true);
                                    }
                                } else {
                                    tvNext.setEnabled(false);
                                    MyApplication.showToast(internationalC,jsonObject.optString("resultDescription", "N/A"));
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
        if(MyApplication.checkMinMax(internationalC,s,editText
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

    ArrayList<String> compareCurrency=new ArrayList<>();


    private void callApiSendCurrencynew() {
        try {

            API.GET("ewallet/api/v1/walletOwnerCountryCurrency/"+MyApplication.getSaveString("walletOwnerCode", internationalC),
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {

                                compareCurrency.clear();
                                JSONArray jsonArraynew=jsonObject.optJSONArray("walletOwnerCountryCurrencyList");
                                for(int i=0; i<jsonArraynew.length(); i++) {
                                    try {
                                        JSONObject data = jsonArraynew.getJSONObject(i);
                                        compareCurrency.add(data.optString("currencyCode"));


                                    }catch(Exception e){

                                    }




                                }




                                callApiserviceProvider();

                                // callApiRecCountry();

                            } else {
                                // MyApplication.showToast(localC,jsonObject.optString("resultDescription", "  "));
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
