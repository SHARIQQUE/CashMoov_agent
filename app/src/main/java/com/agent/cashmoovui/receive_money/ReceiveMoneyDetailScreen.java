package com.agent.cashmoovui.receive_money;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.agent.cashmoovui.AddContact;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.model.CountryCurrencyInfoModel;
import com.agent.cashmoovui.model.CountryInfoModel;
import com.agent.cashmoovui.model.ServiceProviderModel;
import com.agent.cashmoovui.model.SubscriberInfoModel;
import com.agent.cashmoovui.otp.VerifyLoginAccountScreen;
import com.agent.cashmoovui.remittancebyabhay.cashtowallet.CashtoWalletSenderKYC;
import com.agent.cashmoovui.remittancebyabhay.cashtowallet.LocalRemittanceCashtowalletActivity;
import com.aldoapps.autoformatedittext.AutoFormatUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class ReceiveMoneyDetailScreen extends AppCompatActivity implements View.OnClickListener {
    public static ReceiveMoneyDetailScreen cashtowalletbenefikycC;
    ImageView imgBack,imgHome;
    boolean isCustomerData;
    public static final int REQUEST_CODE = 1;
    DatePickerDialog picker;
    public static TextView spinner_senderCurrency,spinner_receiverCurrency,tvAmtCurr,tvAmtPaidCurr,convertionRate_first_page,
            fees_first_page,tax_first_page,tvNext;
    RadioGroup radio_group;
    RadioButton rb_wallet;
    public static AutoCompleteTextView et_destination_mobileNumber;
    public static EditText et_destination_firstName,et_destination_lastName,edittext_amount,edittext_amount_pay;

    private ArrayList<String> sendingCountryList = new ArrayList<>();
    private ArrayList<CountryInfoModel.Country> sendCountryModelList = new ArrayList<>();

    private ArrayList<String> sendCurrencyList = new ArrayList<>();
    private ArrayList<CountryCurrencyInfoModel.CountryCurrency> sendCurrencyModelList = new ArrayList<>();

    private ArrayList<String> recCountryList = new ArrayList<>();
    private ArrayList<CountryInfoModel.Country> recCountryModelList = new ArrayList<CountryInfoModel.Country>();

    private ArrayList<String> recCurrencyList = new ArrayList<>();
    private ArrayList<CountryCurrencyInfoModel.CountryCurrency> recCurrencyModelList = new ArrayList<>();

    private SpinnerDialog spinnerDialogSendingCurr,spinnerDialogRecCurr;
    public static String receiverFee="",serviceProvider,sendCountryCode,sendCountryName,recCountryCode,recCountryName;
    String languageToUse = "";
    MyApplication applicationComponentClass;
    private String serviceCode_from_serviceCategory,serviceCategoryCode_from_serviceCategory,serviceProviderCode_from_serviceCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applicationComponentClass = (MyApplication) getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_money_kyc_details);
        cashtowalletbenefikycC=this;
        languageToUse = applicationComponentClass.getmSharedPreferences().getString("languageToUse", "");

        if (languageToUse.trim().length() == 0) {
            languageToUse = "en";
        }


        Locale locale = new Locale(languageToUse);

        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
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
                MyApplication.hideKeyboard(cashtowalletbenefikycC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(cashtowalletbenefikycC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    public static String amount,currencyValue,fee,rate,exRateCode, fromCurrency,fromCurrencySymbol,fromCurrencyCode,
            toCurrency,toCurrencySymbol,toCurrencyCode;
    public boolean isSet=false;
    public static JSONObject walletOwner = new JSONObject();

    private void getIds() {
        spinner_senderCurrency = findViewById(R.id.spinner_senderCurrency);
        spinner_receiverCurrency = findViewById(R.id.spinner_receiverCurrency);
        tvAmtCurr = findViewById(R.id.tvAmtCurr);
        tvAmtPaidCurr = findViewById(R.id.tvAmtPaidCurr);
        edittext_amount = findViewById(R.id.edittext_amount);
        edittext_amount_pay = findViewById(R.id.edittext_amount_pay);
        et_destination_mobileNumber = findViewById(R.id.et_destination_mobileNumber);
        et_destination_firstName = findViewById(R.id.et_destination_firstName);
        et_destination_lastName = findViewById(R.id.et_destination_lastName);
        fees_first_page = findViewById(R.id.fees_first_page);
        tax_first_page = findViewById(R.id.tax_first_page);
        radio_group = findViewById(R.id.radio_group);
        rb_wallet = findViewById(R.id.rb_wallet);
        tvNext = findViewById(R.id.tvNext);

        et_destination_firstName.setEnabled(false);

        et_destination_lastName.setEnabled(false);

        spinner_senderCurrency.setText("GNF");
        spinner_senderCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogSendingCurr!=null)
                    spinnerDialogSendingCurr.showSpinerDialog();
            }
        });


        et_destination_mobileNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (et_destination_mobileNumber.getRight() - et_destination_mobileNumber.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here


                        Intent intent = new Intent(cashtowalletbenefikycC, AddContact.class);
                        startActivityForResult(intent , REQUEST_CODE);

                        return true;
                    }
                }
                return false;
            }
        });

        et_destination_mobileNumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                if(value.contains(",")) {
                    String[] list = value.split(",");
                    isSet = true;
                    if (list.length == 3) {
                        et_destination_mobileNumber.setText(list[0]);
                        et_destination_firstName.setText(list[1]);
                        et_destination_lastName.setText(list[2]);

//                        etComment.requestFocus();
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.showSoftInput(etComment, InputMethodManager.SHOW_IMPLICIT);
                    } else {
                        et_destination_mobileNumber.setText(list[0]);
                        et_destination_firstName.setText(list[1]);
//                        etLname.requestFocus();
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.showSoftInput(etLname, InputMethodManager.SHOW_IMPLICIT);
                    }
                }else{
                    et_destination_firstName.setText("");
                    et_destination_lastName.setText("");
                    // spinner_senderCurrency.setText(getString(R.string.sending_currencey));
                    spinner_receiverCurrency.setText(getString(R.string.receive_courency));
                }

            }
        });

        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);


        et_destination_mobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (new InternetCheck().isConnected(cashtowalletbenefikycC)) {

                    Matcher m = p.matcher(s);
                    if(s.length()>=9 && m.matches()){
                        if(isSet) {
                            isSet=false;
                        }else{
                            et_destination_firstName.setText("");
                            et_destination_lastName.setText("");
                            // spinner_senderCurrency.setText(getString(R.string.sending_currencey));
                            spinner_receiverCurrency.setText(getString(R.string.receive_courency));
                            callApiSubsriberList();

                        }
                    }else{
                        et_destination_firstName.setText("");
                        et_destination_lastName.setText("");
                        // spinner_senderCurrency.setText(getString(R.string.sending_currencey));
                        spinner_receiverCurrency.setText(getString(R.string.receive_courency));

                    }

                } else {
                    Toast.makeText(cashtowalletbenefikycC, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
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

                if (new InternetCheck().isConnected(cashtowalletbenefikycC)) {

//                    if (spinner_receiverCurrency.getSelectedItem()==null||spinner_receiverCurrency.getSelectedItem().toString().isEmpty()||spinner_receiverCurrency.getSelectedItem().toString().equals("Receiving Currency")) {
//                        MyApplication.showErrorToast(InternationalRemittance.this, getString(R.string.plz_select_receive_currency));
//                        return;
//                    }

                    if (isFormatting) {
                        return;
                    }

                    if (s.length()>1) {
                        formatInput(edittext_amount,s, s.length(), s.length());
                        callApiExchangeRate();

                    } else {
                        fees_first_page.setText("");
                        tax_first_page.setText("");
                        edittext_amount_pay.getText().clear();
                    }

                    isFormatting = false;

                } else {
                    Toast.makeText(cashtowalletbenefikycC, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                }
            }
        });

        callApiCountry();

      //  callApiSendCurrency();
        setOnCLickListener();
        callApiRecCountry();
        service_Provider_api();




    }



    private void setOnCLickListener() {
        tvNext.setOnClickListener(cashtowalletbenefikycC);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvNext:

                if (et_destination_mobileNumber.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(cashtowalletbenefikycC, getString(R.string.val_phone));
                    return;
                }
                if (et_destination_mobileNumber.getText().toString().trim().length() < 9) {
                    MyApplication.showErrorToast(cashtowalletbenefikycC, getString(R.string.enter_phone_no_val));
                    return;
                }
               /* if (et_destination_firstName.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(cashtowalletbenefikycC, getString(R.string.val_fname));
                    return;
                }
                if (et_destination_firstName.getText().toString().trim().length() < 3) {
                    MyApplication.showErrorToast(cashtowalletbenefikycC, getString(R.string.val_fname_len));
                    return;
                }
                if (!et_destination_lastName.getText().toString().trim().isEmpty()&&et_destination_lastName.getText().toString().trim().length() < 3) {
                    MyApplication.showErrorToast(cashtowalletbenefikycC, getString(R.string.val_lname_len));
                    return;
                }*/

                if (spinner_senderCurrency.getText().toString().equals(getString(R.string.sending_currencey))) {
                    MyApplication.showErrorToast(cashtowalletbenefikycC, getString(R.string.select_currency));
                    return;
                }
                if (spinner_receiverCurrency.getText().toString().equals(getString(R.string.receive_courency))) {
                    MyApplication.showErrorToast(cashtowalletbenefikycC, getString(R.string.plz_select_receive_currency));
                    return;
                }
                if(edittext_amount.getText().toString().trim().replace(",","").isEmpty()) {
                    MyApplication.showErrorToast(cashtowalletbenefikycC,getString(R.string.val_amount));
                    return;
                }
                if(edittext_amount.getText().toString().trim().replace(",","").equals("0")||edittext_amount.getText().toString().replace(",","").trim().equals(".")||edittext_amount.getText().toString().trim().replace(",","").equals(".0")||
                        edittext_amount.getText().toString().trim().replace(",","").equals("0.")||edittext_amount.getText().toString().trim().replace(",","").equals("0.0")||edittext_amount.getText().toString().trim().replace(",","").equals("0.00")){
                    MyApplication.showErrorToast(cashtowalletbenefikycC,getString(R.string.val_valid_amount));
                    return;
                }


                Double fee=0.00;
                Double amout=0.00;
                if(receiverFee.isEmpty()&&receiverFee==null){

                }else {
                    fee = Double.parseDouble(receiverFee)+taxtest;
                    amout = Double.parseDouble(edittext_amount.getText().toString().trim().replace(",", ""));
                    System.out.println("get feeval" + fee);
                    System.out.println("get amoutval" + amout);

                }
                if(fee>amout){
                    System.out.println("check amount"+fee+"   "+amout);
                    MyApplication.showErrorToast(cashtowalletbenefikycC,getString(R.string.feetaxvalidation));
                    return;

                }
                recNumber=et_destination_mobileNumber.getText().toString().trim();

                otp_generate_api();




                // callApiPostReceiver();

                break;

        }
    }

    private void otp_generate_api() {
        try{

            JSONObject jsonObject=new JSONObject();



            jsonObject.put("transTypeCode","113092");
            jsonObject.put("subscriberWalletOwnerCode",receiverCode);




            API.POST_GET_OTP("ewallet/api/v1/otp",jsonObject,new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {


                    try {

                        //  JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1771883\",\"requestTime\":\"Mon Oct 18 18:05:40 IST 2021\",\"responseTime\":\"Mon Oct 18 18:05:40 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\"}");

                        if (jsonObject.has("error")) {
                            MyApplication.hideLoader();


                            String error = jsonObject.getString("error");
                            String error_message = jsonObject.getString("error_message");

                         //   Toast.makeText(ReceiveMoneyDetailScreen.this, error_message, Toast.LENGTH_LONG).show();

                            if(error.equalsIgnoreCase("1251"))
                            {

                                Intent i = new Intent(ReceiveMoneyDetailScreen.this, VerifyLoginAccountScreen.class);
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


                                Intent i = new Intent(ReceiveMoneyDetailScreen.this, ReceiveMoneyConfirmScreen.class);
                                startActivity(i);


                                Toast.makeText(ReceiveMoneyDetailScreen.this, getString(R.string.otp_has_send_sucessfully_subscriber_register), Toast.LENGTH_LONG).show();


                                MyApplication.hideLoader();


                            }

                            else {
                                MyApplication.hideLoader();

                                Toast.makeText(ReceiveMoneyDetailScreen.this, resultDescription, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    catch (Exception e)
                    {
                        MyApplication.hideLoader();

                        Toast.makeText(ReceiveMoneyDetailScreen.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }


                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(ReceiveMoneyDetailScreen.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    }

                    else {

                        Toast.makeText(ReceiveMoneyDetailScreen.this, aFalse, Toast.LENGTH_SHORT).show();
                    }

                    //  MyApplication.showToast(LoginPin.this,aFalse);

                }
            });

        }catch (Exception e){

            MyApplication.hideLoader();

          //  MyApplication.showToast(ReceiveMoneyDetailScreen.this,e.toString());

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
                                        //  if (data.optString("code").equalsIgnoreCase(MyApplication.getSaveString("COUNTRYCODE_AGENT", cashtowalletsenderkycC))) {
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

                                    }
                                    //}

                                    for(int i=0;i<sendCountryModelList.size();i++){
                                        if(MyApplication.getSaveString("COUNTRYCODE_AGENT", cashtowalletbenefikycC).equalsIgnoreCase(
                                                sendCountryModelList.get(i).getCode()
                                        )){
                                            sendCountryCode = sendCountryModelList.get(i).getCode();
                                            sendCountryName = sendCountryModelList.get(i).getName();
                                            //  spinner_senderCurrency.setText(getString(R.string.sending_currencey_star));
                                            //   txt_benefi_phone.setText(benefiCountryModelList.get(position).dialCode);
                                            //callApiSendCurrency(sendCountryModelList.get(i).getCode());

                                          //  callApiSendCurrencynew();

                                        }
                                    }


                                    //  callApiRegions(sendCountryCode);
                                    // callApiSendCurrency(MyApplication.getSaveString("COUNTRYCODE_AGENT", cashtowalletsenderkycC));

                                } else {
                                    MyApplication.showToast(ReceiveMoneyDetailScreen.this,jsonObject.optString("resultDescription", "N/A"));
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

    public static String recNumber;
    private void callApiSendCurrency() {
        try {

            API.GET("ewallet/api/v1/countryCurrency/country/"+ sendCountryCode,
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

                                    spinnerDialogSendingCurr = new SpinnerDialog(cashtowalletbenefikycC, sendCurrencyList, getString(R.string.select_currency), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
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


                                } else {
                                  //  MyApplication.showToast(cashtowalletbenefikycC,jsonObject.optString("resultDescription", "  "));
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


    private void callApiSubsriberList() {
        try {

            // MyApplication.showloader(TransferToAccountActivity.this, "Please wait!");
            API.GET("ewallet/api/v1/walletOwner/all?walletOwnerCategoryCode=100010&mobileNumber="+
                            et_destination_mobileNumber.getText().toString()+"&offset=0&limit=500",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                subscriberList.clear();
                                if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
                                    walletOwner = jsonObject;
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("walletOwnerList");
                                    int pcount=0;
                                    int index=0;
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject jsonObjectSubscriber = walletOwnerListArr.optJSONObject(i);
                                        Iterator<?> keys = jsonObjectSubscriber.keys();
                                        JSONObject countObj=new JSONObject();
                                        int count=0;
                                        while( keys.hasNext() ) {
                                            count++;
                                            String key = (String) keys.next();

                                        }
                                        if(count>pcount){
                                            index=i;
                                            pcount=count;
                                        }

                                    }


                                    JSONObject jsonObjectSubscriber =  walletOwnerListArr.optJSONObject(index);

                                    SubscriberInfoModel.Subscriber subscriber = new SubscriberInfoModel.Subscriber(
                                            jsonObjectSubscriber.optInt("id"),
                                            jsonObjectSubscriber.optBoolean("walletExists"),
                                            jsonObjectSubscriber.optString("code", "N/A"),
                                            jsonObjectSubscriber.optString("walletOwnerCategoryCode"),
                                            jsonObjectSubscriber.optString("ownerName"),
                                            jsonObjectSubscriber.optString("mobileNumber"),
                                            jsonObjectSubscriber.optString("idProofNumber", "N/A"),
                                            jsonObjectSubscriber.optString("email", "N/A"),
                                            jsonObjectSubscriber.optString("status", "N/A"),
                                            jsonObjectSubscriber.optString("state", "N/A"),
                                            jsonObjectSubscriber.optString("stage", "N/A"),
                                            jsonObjectSubscriber.optString("idProofTypeCode", "N/A"),
                                            jsonObjectSubscriber.optString("idProofTypeName", "N/A"),
                                            jsonObjectSubscriber.optString("notificationLanguage", "N/A"),
                                            jsonObjectSubscriber.optString("notificationTypeCode", "N/A"),
                                            jsonObjectSubscriber.optString("notificationName", "N/A"),
                                            jsonObjectSubscriber.optString("gender", "N/A"),
                                            jsonObjectSubscriber.optString("dateOfBirth", "N/A"),
                                            jsonObjectSubscriber.optString("lastName", "N/A"),
                                            jsonObjectSubscriber.optString("registerCountryCode", "N/A"),
                                            jsonObjectSubscriber.optString("registerCountryName", "N/A"),
                                            jsonObjectSubscriber.optString("creationDate", "N/A"),
                                            jsonObjectSubscriber.optString("profileTypeCode", "N/A"),
                                            jsonObjectSubscriber.optString("profileTypeName", "N/A"),
                                            jsonObjectSubscriber.optString("walletOwnerCatName", "N/A"),
                                            jsonObjectSubscriber.optString("occupationTypeCode", "N/A"),
                                            jsonObjectSubscriber.optString("occupationTypeName", "N/A"),
                                            jsonObjectSubscriber.optString("requestedSource", "N/A"),
                                            jsonObjectSubscriber.optString("regesterCountryDialCode", "N/A"),
                                            jsonObjectSubscriber.optString("walletOwnerCode", "N/A")

                                    );


                                    SubscriberInfoModel subscriberInfoModel = new SubscriberInfoModel(
                                            jsonObject.optString("transactionId", "N/A"),
                                            jsonObject.optString("requestTime", "N/A"),
                                            jsonObject.optString("responseTime", "N/A"),
                                            jsonObject.optString("resultCode", "N/A"),
                                            jsonObject.optString("resultDescription", "N/A"),
                                            subscriber
                                    );

                                    setSubscriberdata(subscriberInfoModel);


                                } else {
                                    setSubscriberdataf("No Data");
                                    // MyApplication.showToast(jsonObject.optString("resultDescription", "N/A"));
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

    public static String receiverCode;
    private ArrayList<String> subscriberList = new ArrayList<String>();

    private ArrayAdapter<String> adapter;
    private void setSubscriberdataf(String subscriberInfoModel) {
        isCustomerData = false;
        subscriberList.clear();

        subscriberList.add(""+""+subscriberInfoModel+""+"");
        adapter = new ArrayAdapter<String>(cashtowalletbenefikycC,R.layout.item_select, subscriberList);
        et_destination_mobileNumber.setAdapter(adapter);
//        et_destination_mobileNumber.setThreshold(9);
//        et_destination_mobileNumber.showDropDown();

        et_destination_firstName.setText("");
        et_destination_lastName.setText("");
        //spinner_senderCurrency.setText(getString(R.string.sending_currencey));
        spinner_receiverCurrency.setText(getString(R.string.receive_courency));

        receiverCode = "";
    }

    private void setSubscriberdata(SubscriberInfoModel subscriberInfoModel) {
        isCustomerData = true;
        SubscriberInfoModel.Subscriber data = subscriberInfoModel.getSubscriber();

        subscriberList.add(data.getMobileNumber() + "," + data.getOwnerName() + "," + data.getLastName());
        adapter = new ArrayAdapter<String>(cashtowalletbenefikycC, R.layout.item_select, subscriberList);
        et_destination_mobileNumber.setAdapter(adapter);
//        et_destination_mobileNumber.setThreshold(9);
//        et_destination_mobileNumber.showDropDown();

        et_destination_firstName.setText(data.getOwnerName());
        et_destination_lastName.setText(data.getLastName());
        receiverCode = data.getCode();

            callApiRecCurrency(data.getRegisterCountryCode());
            callApiSendCurrency();






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
                                    spinnerDialogRecCurr = new SpinnerDialog(cashtowalletbenefikycC, recCurrencyList, getString(R.string.select_currency), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
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

                                } else {
                                 //   MyApplication.showToast(cashtowalletbenefikycC,jsonObject.optString("resultDescription", "  "));
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

    private void callApiRecCountry() {
        try {

            API.GET("ewallet/api/v1/country/all",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            if (jsonObject != null) {
                                recCountryList.clear();
                                recCountryModelList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("countryList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        // if (!MyApplication.getSaveString("COUNTRYCODE_AGENT", cashtowalletsenderkycC).equalsIgnoreCase(data.optString("countryCode"))) {
                                        recCountryModelList.add(new CountryInfoModel.Country(
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

                                        recCountryList.add(data.optString("name").trim());

                                    }

                                    // }

                                    for(int i=0;i<recCountryModelList.size();i++){
                                        if(MyApplication.getSaveString("COUNTRYCODE_AGENT", cashtowalletbenefikycC).equalsIgnoreCase(
                                                recCountryModelList.get(i).getCode()
                                        )){
                                            //spinner_issuingCountry.setText(getString(R.string.valid_beneficiary_currency));
                                            //   txt_benefi_phone.setText(benefiCountryModelList.get(position).dialCode);
                                            recCountryCode = recCountryModelList.get(i).getCode();
                                            // callApiRecCurrency(recCountryCode);

                                        }
                                    }




                                } else {
                                    MyApplication.showToast(cashtowalletbenefikycC,jsonObject.optString("resultDescription", "N/A"));
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
    Double taxtest=0.00;
    private void callApiExchangeRate() {
        try {



            //MyApplication.showloader(cashinC, "Please wait!");
            API.GET("ewallet/api/v1/exchangeRate/getAmountDetails?sendCurrencyCode="+
                            sendCurrencyModelList.get((Integer) spinner_senderCurrency.getTag()).getCurrencyCode()
                            +"&receiveCurrencyCode="+recCurrencyModelList.get((Integer) spinner_receiverCurrency.getTag()).getCurrencyCode()
                            +"&sendCountryCode="+ sendCountryCode+
                            "&receiveCountryCode="+recCountryCode
                            +"&currencyValue="+ edittext_amount.getText().toString().replace(",","") + "&channelTypeCode="+MyApplication.channelTypeCode+
                            "&serviceCode=" + serviceCode_from_serviceCategory+
                            "&serviceCategoryCode=" +"REMON"
                            + "&serviceProviderCode=" + serviceProviderCode_from_serviceCategory
                            + "&walletOwnerCode=" + receiverCode+
                    "&payAgentCode=" + receiverCode+
                    "&remitAgentCode=" + MyApplication.getSaveString("walletOwnerCode", ReceiveMoneyDetailScreen.this),



  /*  private void callApiExchangeRate() {
        try {
            //MyApplication.showloader(cashinC, "Please wait!");
            API.GET("ewallet/api/v1/exchangeRate/getAmountDetails?sendCurrencyCode="+ "100062"
                            +"&receiveCurrencyCode="+"100062"
                            +"&sendCountryCode="+sendCountryCode
                            +"&receiveCountryCode="+recCountryCode
                            +"&currencyValue="+ edittext_amount.getText().toString().replace(",","") + "&channelTypeCode="+MyApplication.channelTypeCode
                           + "&serviceCode=" + "100015"+
                            "&serviceCategoryCode=" + "REMON"
                            + "&serviceProviderCode=" + "1100173"
                            + "&walletOwnerCode=" + MyApplication.getSaveString("walletOwnerCode", ReceiveMoneyDetailScreen.this));
*/
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            // MyApplication.hideLoader();
                            System.out.println("CashtowWallet response======="+jsonObject.toString());
                            if (jsonObject != null) {
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    if(edittext_amount.getText().toString().trim().replace(",","").length()>0) {
                                        JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("exchangeRate");

                                        currencyValue = df.format(jsonObjectAmountDetails.optDouble("currencyValue"));
                                        fee =df.format(jsonObjectAmountDetails.optDouble("fee"));
                                        rate = jsonObjectAmountDetails.optString("value");
                                        exRateCode = jsonObjectAmountDetails.optString("code");
                                        //receiverFee= jsonObjectAmountDetails.optInt("receiverFee");
                                        //receiverTax = jsonObjectAmountDetails.optInt("receiverTax");
                                        //etAmountNew.setText(currencyValue);
                                        fees_first_page.setText((fee+""));
                                        edittext_amount_pay.setText(MyApplication.addDecimal(""+currencyValue));
                                        amount = jsonObjectAmountDetails.optDouble("currencyValue")+"";
                                         receiverFee=jsonObjectAmountDetails.optString("receiverFee");

                                         System.out.println("get fee"+receiverFee);


//                                    int tax = receiverFee+receiverTax;
//                                    if(currencyValue<tax){
//                                        tvSend.setVisibility(View.GONE);
//                                        MyApplication.showErrorToast(tononsubscriberC,getString(R.string.fee_tax_greater_than_trans_amt));
//                                    }else{
//                                        tvSend.setVisibility(View.VISIBLE);
//                                    }

                                        if (jsonObjectAmountDetails.has("taxConfigurationList")) {
                                            taxConfigurationList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
                                            taxtest=taxConfigurationList.optJSONObject(0).optDouble("value");
                                            tax_first_page.setText(MyApplication.addDecimal(""+taxConfigurationList.optJSONObject(0).optDouble("value")));
                                            // amountTobeCharged_first_page.setText(MyApplication.addDecimal(""+Double.parseDouble(edittext_amount.getText().toString().trim()) + taxConfigurationList.optJSONObject(0).optDouble("value")));

                                        } else {
                                            taxConfigurationList = null;
                                            tax_first_page.setText(MyApplication.addDecimal("0.00"));
                                            taxtest=0.00;
                                            //amountTobeCharged_first_page.setText(MyApplication.addDecimal(""+Double.parseDouble(edittext_amount.getText().toString().trim())));

                                        }

                                        if(jsonObjectAmountDetails.has("receiverTax")) {
                                            taxConfigurationList=null;
                                            tax_first_page.setText(MyApplication.addDecimal("0.00"));
                                        }

                                        tvNext.setEnabled(true);
                                    }
                                } else {
                                    tvNext.setEnabled(false);
                                    MyApplication.showToast(cashtowalletbenefikycC,jsonObject.optString("resultDescription", "N/A"));
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


//    public static JSONObject benefiCustomerJsonObj = new JSONObject();
//
//    public void callApiPostReceiver(){
//
//        JSONObject benefiJson=new JSONObject();
//        try {
//            benefiJson.put("firstName",et_destination_firstName.getText().toString().trim());
//            benefiJson.put("lastName",et_destination_lastName.getText().toString().trim());
//            benefiJson.put("mobileNumber",et_destination_mobileNumber.getText().toString().trim());
//            if(spinner_senderCurrency.getTag()!=null){
//                benefiJson.put("idProofTypeCode",idProofTypeModelList.get((Integer) spinner_destination_idprooftype.getTag()).getCode());
//            }else{
//                benefiJson.put("idProofTypeCode",idprooftypecode);
//            }
//            if(spinner_receiverCurrency.getTag()!=null){
//                benefiJson.put("regionCode",regionModelList.get((Integer) spinner_destination_region.getTag()).getCode());
//            }else{
//                benefiJson.put("regionCode",regioncode);
//            }
//
//            benefiJson.put("city",et_destination_city.getText().toString().trim());
//            benefiJson.put("address",et_destination_address.getText().toString().trim());
//            if(spinner_destination_issuingCountry.getTag()!=null){
//                benefiJson.put("issuingCountryCode",issuingCountryModelList.get((Integer) spinner_destination_issuingCountry.getTag()).getCountryCode());
//            }else{
//                benefiJson.put("issuingCountryCode",issuingcountrycode);
//            }
//            if(spinner_destination_gender.getTag()!=null){
//                benefiJson.put("gender",benefiGenderModelList.get((Integer) spinner_destination_gender.getTag()).getCode());
//            }else{
//                benefiJson.put("gender",gendercode);
//            }
//
//            if (code.isEmpty()||code==null) {
//            } else{
//                benefiJson.put("code",code);
//            }
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        if(isCustomerData) {
//            MyApplication.showloader(cashtowalletbenefikycC,"Please Wait...");
//            API.PUT("ewallet/api/v1/customer/sender",benefiJson,
//                    new Api_Responce_Handler() {
//                        @Override
//                        public void success(JSONObject jsonObject) {
//                            MyApplication.hideLoader();
//                            if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
//                                benefiCustomerJsonObj = jsonObject;
//                                Intent i = new Intent(cashtowalletbenefikycC, InternationalRemittanceConfirmScreen.class);
//                                startActivity(i);
//
//                            }else{
//                                MyApplication.showToast(cashtowalletbenefikycC,jsonObject.optString("resultDescription"));
//                            }
//                        }
//
//                        @Override
//                        public void failure(String aFalse) {
//                            MyApplication.hideLoader();
//
//                        }
//                    });
//        }else{
//            MyApplication.showloader(cashtowalletbenefikycC,"Please Wait...");
//            API.POST_REQEST_WH_NEW("ewallet/api/v1/customer/sender",benefiJson,
//                    new Api_Responce_Handler() {
//                        @Override
//                        public void success(JSONObject jsonObject) {
//                            MyApplication.hideLoader();
//                            if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
//                                benefiCustomerJsonObj = jsonObject;
//                                Intent i = new Intent(cashtowalletbenefikycC, InternationalRemittanceConfirmScreen.class);
//                                startActivity(i);
//
//                            }else{
//                                MyApplication.showToast(cashtowalletbenefikycC,jsonObject.optString("resultDescription"));
//                            }
//                        }
//
//                        @Override
//                        public void failure(String aFalse) {
//                            MyApplication.hideLoader();
//
//                        }
//                    });
//        }
//    }

    private boolean isFormatting;
    private int prevCommaAmount;
    private void formatInput(EditText editText,CharSequence s, int start, int count) {


        if(MyApplication.checkMinMax(ReceiveMoneyDetailScreen.this,s,editText
                ,MyApplication.CashToWalletMinValue,MyApplication.CashToWalletMaxValue)){
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

                        Toast.makeText(ReceiveMoneyDetailScreen.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                } catch (Exception e) {
                    MyApplication.hideLoader();

                    Toast.makeText(ReceiveMoneyDetailScreen.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(ReceiveMoneyDetailScreen.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }
    private void callApiSendCurrencynew() {
        try {

            API.GET("ewallet/api/v1/walletOwnerCountryCurrency/"+MyApplication.getSaveString("walletOwnerCode", ReceiveMoneyDetailScreen.this),
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

                                   // sendCountryName.setVisibility(View.VISIBLE);

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

                                spinnerDialogSendingCurr = new SpinnerDialog(ReceiveMoneyDetailScreen.this, sendCurrencyList, getString(R.string.select_currency), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
                                spinnerDialogSendingCurr.setCancellable(true); // for cancellable
                                spinnerDialogSendingCurr.setShowKeyboard(false);// for open keyboard by default
                                spinnerDialogSendingCurr.bindOnSpinerListener(new OnSpinerItemClick() {
                                    @Override
                                    public void onClick(String item, int position) {
                                        //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                        spinner_senderCurrency.setText(item);
                                        spinner_senderCurrency.setTag(position);
                                       // sendCountryName.setVisibility(View.VISIBLE);
                                        fromCurrency=sendCurrencyModelList.get(position).getCountryName();
                                      //  fromCurrencynew = sendCurrencyModelList.get(position).getCurrCode();
                                        fromCurrencySymbol = sendCurrencyModelList.get(position).getCurrencySymbol();
                                        fromCurrencyCode = sendCurrencyModelList.get(position).getCurrencyCode();
                                        tvAmtCurr.setText(fromCurrencySymbol);
                                        // txt_curr_symbol_paid.setText(benefiCurrencyModelList.get(position).currencySymbol);
                                        edittext_amount.getText().clear();
                                        edittext_amount_pay.getText().clear();
                                       // toCurrencySymbolnew ="GNF";

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

                                spinnerDialogRecCurr = new SpinnerDialog(ReceiveMoneyDetailScreen.this, recCurrencyList, getString(R.string.select_currency), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
                                spinnerDialogRecCurr.setCancellable(true); // for cancellable
                                spinnerDialogRecCurr.setShowKeyboard(false);// for open keyboard by default
                                spinnerDialogRecCurr.bindOnSpinerListener(new OnSpinerItemClick() {
                                    @Override
                                    public void onClick(String item, int position) {
                                        //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                        spinner_receiverCurrency.setText(item);
                                        spinner_receiverCurrency.setTag(position);
                                       // receivingcurrecncyText.setVisibility(View.VISIBLE);
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
                                MyApplication.showToast(ReceiveMoneyDetailScreen.this,jsonObject.optString("resultDescription", "  "));
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

