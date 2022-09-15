package com.agent.cashmoovui.remittancebyabhay.cashtowallet;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.agent.cashmoovui.activity.OtherOption;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.cash_in.CashIn;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.model.CityInfoModel;
import com.agent.cashmoovui.model.CountryCurrencyInfoModel;
import com.agent.cashmoovui.model.CountryInfoModel;
import com.agent.cashmoovui.model.CountryRemittanceInfoModel;
import com.agent.cashmoovui.model.SubscriberInfoModel;
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

public class CashtoWalletReceiverKYC extends AppCompatActivity implements View.OnClickListener {
    public static CashtoWalletReceiverKYC cashtowalletbenefikycC;
    ImageView imgBack,imgHome;
    boolean isCustomerData;
    public static final int REQUEST_CODE = 1;
    DatePickerDialog picker;
    private TextView spinner_senderCurrency,spinner_receiverCurrency,tvAmtCurr,tvAmtPaidCurr,convertionRate_first_page,
            fees_first_page,tax_first_page,tvNext;
    RadioGroup radio_group;
    RadioButton rb_wallet;
    public static AutoCompleteTextView et_destination_mobileNumber;
    private EditText et_destination_firstName,et_destination_lastName,edittext_amount,edittext_amount_pay;

    private ArrayList<String> sendingCountryList = new ArrayList<>();
    private ArrayList<CountryInfoModel.Country> sendCountryModelList = new ArrayList<>();

    private ArrayList<String> sendCurrencyList = new ArrayList<>();
    private ArrayList<CountryCurrencyInfoModel.CountryCurrency> sendCurrencyModelList = new ArrayList<>();

    private ArrayList<String> recCountryList = new ArrayList<>();
    private ArrayList<CountryRemittanceInfoModel.RemitCountry> recCountryModelList = new ArrayList<>();

    private ArrayList<String> recCurrencyList = new ArrayList<>();
    private ArrayList<CountryCurrencyInfoModel.CountryCurrency> recCurrencyModelList = new ArrayList<>();

    private SpinnerDialog spinnerDialogSendingCurr,spinnerDialogRecCurr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_to_wallet_benefi_kyc);
        cashtowalletbenefikycC=this;
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
        convertionRate_first_page = findViewById(R.id.convertionRate_first_page);
        fees_first_page = findViewById(R.id.fees_first_page);
        tax_first_page = findViewById(R.id.tax_first_page);
        radio_group = findViewById(R.id.radio_group);
        rb_wallet = findViewById(R.id.rb_wallet);
        tvNext = findViewById(R.id.tvNext);

        edittext_amount_pay.setEnabled(false);



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
                        convertionRate_first_page.setText("");
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


        setOnCLickListener();

        callApiSendCurrency();

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

                else  if(Double.parseDouble(edittext_amount.getText().toString().trim().replace(",",""))<MyApplication.CashToWalletMinValue) {
                    MyApplication.showErrorToast(CashtoWalletReceiverKYC.this,getString(R.string.val_amount_min)+" "+MyApplication.CashToWalletMinValue);
                    return ;
                }

                else   if(Double.parseDouble(edittext_amount.getText().toString().trim().replace(",",""))>MyApplication.CashToWalletMaxValue) {
                    MyApplication.showErrorToast(CashtoWalletReceiverKYC.this, getString(R.string.val_amount_max) + " " + MyApplication.CashToWalletMaxValue);
                    return;
                }
                if (CashtoWalletSenderKYC.et_sender_phoneNumber.getText().toString().trim().equalsIgnoreCase(et_destination_mobileNumber.getText().toString().trim())) {
                    MyApplication.showToast(cashtowalletbenefikycC,getString(R.string.both_msisdn_not_same));
                    return;
                }

                Intent i = new Intent(cashtowalletbenefikycC, CashtoWalletConfirmScreen.class);
                i.putExtra("destmobileNumber", et_destination_mobileNumber.getText().toString());

                startActivity(i);

               // callApiPostReceiver();

                break;

        }
    }

    private void callApiSendCurrency() {
        try {

            API.GET("ewallet/api/v1/countryCurrency/country/"+CashtoWalletSenderKYC.sendCountryCode,
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

                                    spinnerDialogSendingCurr = new SpinnerDialog(cashtowalletbenefikycC, sendCurrencyList, getString(R.string.select_currency), R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
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
                                    MyApplication.showToast(cashtowalletbenefikycC,jsonObject.optString("resultDescription", "  "));
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

        if(data.getRegisterCountryCode()!=null){
            callApiRecCurrency(data.getRegisterCountryCode());
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
                                    spinnerDialogRecCurr = new SpinnerDialog(cashtowalletbenefikycC, recCurrencyList, getString(R.string.select_currency), R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
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
                                    MyApplication.showToast(cashtowalletbenefikycC,jsonObject.optString("resultDescription", "  "));
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
            //MyApplication.showloader(cashinC, "Please wait!");
            API.GET("ewallet/api/v1/exchangeRate/getAmountDetails?sendCurrencyCode="+
                            sendCurrencyModelList.get((Integer) spinner_senderCurrency.getTag()).getCurrencyCode()
                            +"&receiveCurrencyCode="+recCurrencyModelList.get((Integer) spinner_receiverCurrency.getTag()).getCurrencyCode()
                            +"&sendCountryCode="+ CashtoWalletSenderKYC.sendCountryCode+
                            "&receiveCountryCode="+CashtoWalletSenderKYC.recCountryCode
                            +"&currencyValue="+ edittext_amount.getText().toString().replace(",","") + "&channelTypeCode="+MyApplication.channelTypeCode+
                            "&serviceCode=" + CashtoWalletSenderKYC.serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode")+
                            "&serviceCategoryCode=" + CashtoWalletSenderKYC.serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode")
                            + "&serviceProviderCode=" + CashtoWalletSenderKYC.serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code")
                            + "&walletOwnerCode=" + MyApplication.getSaveString("USERCODE", cashtowalletbenefikycC),

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
                                        fee = df.format(jsonObjectAmountDetails.optDouble("fee"));
                                        rate = jsonObjectAmountDetails.optString("value");
                                        exRateCode = jsonObjectAmountDetails.optString("code");
                                        //receiverFee= jsonObjectAmountDetails.optInt("receiverFee");
                                        //receiverTax = jsonObjectAmountDetails.optInt("receiverTax");
                                        //etAmountNew.setText(currencyValue);
                                        convertionRate_first_page.setText(MyApplication.addDecimalthreenew(rate));
                                        fees_first_page.setText(fee);
                                        edittext_amount_pay.setText(currencyValue);
                                        amount = edittext_amount.getText().toString().trim().replace(",","");


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
                                           // amountTobeCharged_first_page.setText(df.format(Double.parseDouble(edittext_amount.getText().toString().trim()) + taxConfigurationList.optJSONObject(0).optDouble("value")));

                                        } else {
                                            taxConfigurationList = null;
                                            tax_first_page.setText("0.00");
                                            //amountTobeCharged_first_page.setText(df.format(Double.parseDouble(edittext_amount.getText().toString().trim())));

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


        if(MyApplication.checkMinMax(CashtoWalletReceiverKYC.this,s,editText
                ,MyApplication.CashToWalletMinValue,MyApplication.CashToWalletMaxValue)){
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

