package com.agent.cashmoovui.transfer_float;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agent.cashmoovui.HiddenPassTransformationMethod;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.activity.OtherOption;
import com.agent.cashmoovui.adapter.CurrencyListCommisionTransaction;
import com.agent.cashmoovui.adapter.TransferCommisionAdapter;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.apiCalls.BioMetric_Responce_Handler;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.login.LoginPin;
import com.agent.cashmoovui.model.transaction.CurrencyModel;
import com.agent.cashmoovui.otp.VerifyLoginAccountScreen;
import com.agent.cashmoovui.set_pin.AESEncryption;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CommissionTransfer extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    ImageView imgBack,imgHome;
    public static LoginPin loginpinC;
    ImageButton qrCode_imageButton;

    RecyclerView recyclerView;



    String select_currencySymbol="";
    String select_currencyCode="";

    String currencyName_from_currency="";
    String countryCurrencyCode_from_currency="";

    boolean  isPasswordVisible;
    String balanceString="";

    Spinner spinner_currency;


    String walletCode;


    String  serviceCode_from_allSellFloat ="",serviceCategoryCode_from_allSellFloat="",serviceProviderCode_from_allSellFloat="",srcCurrencyCode_from_allSellFloat="",desCurrencyCode_from_allSellFloat="",srcWalletOwnerCode_from_allSellFloat="",desWalletOwnerCode_from_allSellFloat="";


    View rootView;

    EditText etPin;
    TextView tvContinue,mainbalance_textview,available_balance,rp_tv_convertionrate,exportReceipt_textview,tv_nextClick,rp_tv_agentName,rp_tv_mobileNumber,rp_tv_businessType,rp_tv_email,rp_tv_country,rp_tv_receiverName,rp_tv_transactionAmount
            ,rp_tv_fees_reveiewPage,receiptPage_tv_stransactionType, receiptPage_tv_dateOfTransaction, receiptPage_tv_transactionAmount,
            receiptPage_tv_amount_to_be_credit, receiptPage_tv_fee, receiptPage_tv_financialtax, receiptPage_tv_transaction_receiptNo,receiptPage_tv_sender_name,
            receiptPage_tv_sender_phoneNo,
            receiptPage_tv_receiver_name, receiptPage_tv_receiver_phoneNo, close_receiptPage_textview,rp_tv_excise_tax,rp_tv_amount_to_be_charge,rp_tv_amount_paid,previous_reviewClick_textview,confirm_reviewClick_textview;
    LinearLayout ll_page_1,ll_reviewPage,ll_receiptPage,main_layout,ll_successPage,linearLayout_record;

    MyApplication applicationComponentClass;
    String languageToUse = "";

    EditText edittext_amount,et_mpin;


    String amountstr="",agentName_from_walletOwner="", businessTypeName_walletOwnerCategoryCode="",email_walletOwnerCategoryCode="";

    String walletOwnerCode_mssis_agent="",walletOwnerCode_subs, senderNameAgent="";

    String  currencyCode_agent="",countryCode_agent="",currencyName_agent="";

    String tax_financial="",fees_amount,totalAmount_str,receivernameStr="";
    Double tax_financial_double=0.0,amountstr_double=0.0,fees_amount_double=0.0,totalAmount_double=0.0;

   String mpinStr="";


    String  serviceCode_from_serviceCategory="",serviceCategoryCode_from_serviceCategory="",serviceProviderCode_from_serviceCategory;


    ArrayList<String> arrayList_instititueName;
    ArrayList<String> arrayList_instititueCode;

    String selectInstititueName="";
    String selectInstititueCode="";


    ArrayList<String> arrayList_currecnyName = new ArrayList<String>();
    ArrayList<String> arrayList_currecnyCode = new ArrayList<String>();
    String selectCurrecnyName="";
    String selectCurrecnyCode="";



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


            setContentView(R.layout.commision_transfer);
            setBackMenu();


            rootView = getWindow().getDecorView().findViewById(R.id.main_layout);


            recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
            linearLayout_record = (LinearLayout) findViewById(R.id.linearLayout_record);


            //     First page

            ll_page_1 = (LinearLayout) findViewById(R.id.ll_page_1);

            tv_nextClick = (TextView) findViewById(R.id.tv_nextClick);
            edittext_amount = (EditText) findViewById(R.id.edittext_amount);
            spinner_currency = (Spinner) findViewById(R.id.spinner_currency);
            spinner_currency.setOnItemSelectedListener(this);

            //    Reveiw page

            ll_reviewPage = (LinearLayout) findViewById(R.id.ll_reviewPage);


            rp_tv_agentName = (TextView) findViewById(R.id.rp_tv_agentName);
            rp_tv_mobileNumber = (TextView) findViewById(R.id.rp_tv_mobileNumber);
            rp_tv_businessType = (TextView) findViewById(R.id.rp_tv_businessType);
            rp_tv_email = (TextView) findViewById(R.id.rp_tv_email);
            rp_tv_country = (TextView) findViewById(R.id.rp_tv_country);
            rp_tv_receiverName = (TextView) findViewById(R.id.rp_tv_receiverName);
            rp_tv_transactionAmount = (TextView) findViewById(R.id.rp_tv_transactionAmount);
            rp_tv_fees_reveiewPage = (TextView) findViewById(R.id.rp_tv_fees_reveiewPage);
            rp_tv_excise_tax = (TextView) findViewById(R.id.rp_tv_excise_tax);
            rp_tv_amount_to_be_charge = (TextView) findViewById(R.id.rp_tv_amount_to_be_charge);
            rp_tv_amount_paid = (TextView) findViewById(R.id.rp_tv_amount_paid);


            et_mpin = (EditText) findViewById(R.id.et_mpin);
            previous_reviewClick_textview = (TextView) findViewById(R.id.previous_reviewClick_textview);
            confirm_reviewClick_textview = (TextView) findViewById(R.id.confirm_reviewClick_textview);


            TextView tvFinger =findViewById(R.id.tvFinger);
            if(MyApplication.setProtection!=null && !MyApplication.setProtection.isEmpty()) {
                if (MyApplication.setProtection.equalsIgnoreCase("Activate")) {
                    tvFinger.setVisibility(View.VISIBLE);
                } else {
                    tvFinger.setVisibility(View.GONE);
                }
            }else{
                tvFinger.setVisibility(View.VISIBLE);
            }
            tvFinger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.biometricAuth(CommissionTransfer.this, new BioMetric_Responce_Handler() {
                        @Override
                        public void success(String success) {
                            try {

                                //  String encryptionDatanew = AESEncryption.getAESEncryption(MyApplication.getSaveString("pin",MyApplication.appInstance).toString().trim());
                                mpinStr=MyApplication.getSaveString("pin",MyApplication.appInstance);

                                if (validation_mobile_Detailsf()) {


                                    if (new InternetCheck().isConnected(CommissionTransfer.this)) {

                                        MyApplication.showloader(CommissionTransfer.this, getString(R.string.please_wait));

                                        mpin_final_api();

                                    } else {
                                        Toast.makeText(CommissionTransfer.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failure(String failure) {
                            MyApplication.showToast(CommissionTransfer.this,failure);
                        }
                    });
                }
            });

            //    Receipt page

            ll_receiptPage = (LinearLayout) findViewById(R.id.ll_receiptPage);
            main_layout = (LinearLayout) findViewById(R.id.main_layout);
            ll_successPage = (LinearLayout) findViewById(R.id.ll_successPage);
            exportReceipt_textview = (TextView) findViewById(R.id.exportReceipt_textview);
            rp_tv_convertionrate = (TextView) findViewById(R.id.rp_tv_convertionrate);
            exportReceipt_textview.setOnClickListener(this);

            receiptPage_tv_transaction_receiptNo = (TextView) findViewById(R.id.receiptPage_tv_transaction_receiptNo);
            receiptPage_tv_stransactionType = (TextView) findViewById(R.id.receiptPage_tv_stransactionType);
            receiptPage_tv_dateOfTransaction = (TextView) findViewById(R.id.receiptPage_tv_dateOfTransaction);
            receiptPage_tv_transactionAmount = (TextView) findViewById(R.id.receiptPage_tv_transactionAmount);
            receiptPage_tv_amount_to_be_credit = (TextView) findViewById(R.id.receiptPage_tv_amount_to_be_credit);
            receiptPage_tv_fee = (TextView) findViewById(R.id.receiptPage_tv_fee);
            receiptPage_tv_financialtax = (TextView) findViewById(R.id.receiptPage_tv_financialtax);
            receiptPage_tv_sender_name = (TextView) findViewById(R.id.receiptPage_tv_sender_name);
            receiptPage_tv_sender_phoneNo = (TextView) findViewById(R.id.receiptPage_tv_sender_phoneNo);
            receiptPage_tv_receiver_name = (TextView) findViewById(R.id.receiptPage_tv_receiver_name);
            receiptPage_tv_receiver_phoneNo = (TextView) findViewById(R.id.receiptPage_tv_receiver_phoneNo);
            close_receiptPage_textview = (TextView) findViewById(R.id.close_receiptPage_textview);
            qrCode_imageButton = (ImageButton) findViewById(R.id.qrCode_imageButton);

            qrCode_imageButton.setOnClickListener(this);
            tv_nextClick.setOnClickListener(this);
            previous_reviewClick_textview.setOnClickListener(this);
            confirm_reviewClick_textview.setOnClickListener(this);
            close_receiptPage_textview.setOnClickListener(this);

            walletOwnerCode_mssis_agent = MyApplication.getSaveString("USERCODE", CommissionTransfer.this);



            available_balance = (TextView)(findViewById(R.id.available_balance));


            mainbalance_textview = (TextView)(findViewById(R.id.mainbalance_textview));

            tvContinue = (TextView)(findViewById(R.id.tvContinue));

            tvContinue.setOnClickListener(this);


            if (new InternetCheck().isConnected(CommissionTransfer.this)) {

                MyApplication.showloader(CommissionTransfer.this, getString(R.string.please_wait));

                api_walletOwnerUser();


            } else {
                Toast.makeText(CommissionTransfer.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
            }

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
                        MyApplication.hideKeyboard(CommissionTransfer.this);            }
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


            if (new InternetCheck().isConnected(CommissionTransfer.this)) {

              //  MyApplication.showloader(CommissionTransfer.this, getString(R.string.getting_user_info));

                callApiFromCurrency(MyApplication.getSaveString("userCountryCode",CommissionTransfer.this));


            } else {
                Toast.makeText(CommissionTransfer.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
            }

        }
        catch (Exception e)
        {
            Toast.makeText(CommissionTransfer.this, e.toString(), Toast.LENGTH_LONG).show();

        }

    }

    String currencyCode;
    private void callApiFromCurrency(String code) {
        try {

            API.GET("ewallet/api/v1/countryCurrency/country/"+code,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {

                                if(jsonObject.optString("resultCode", "  ").equalsIgnoreCase("0")){
                                    currencyCode = jsonObject.optJSONObject("country").optString("currencyCode");
                                    //fromCurrencySymbol = jsonObject.optJSONObject("country").optString("currencySymbol");

                                    api_wallet_walletOwner();

                                } else {
                                    MyApplication.showToast(CommissionTransfer.this,jsonObject.optString("resultDescription", "  "));
                                }
                            }

                            // callApiBenefiCurrency();
                        }

                        @Override
                        public void failure(String aFalse) {
                            MyApplication.hideLoader();

                        }
                    });

        } catch (Exception e) {

        }

    }

    private void api_wallet_walletOwner() {

        String usercode_from_msis =  MyApplication.getSaveString("USERCODE", CommissionTransfer.this);

        API.GET_TRANSFER_DETAILS("ewallet/api/v1/wallet/walletOwner/"+usercode_from_msis,languageToUse,new Api_Responce_Handler() {

            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");

                    if(resultCode.equalsIgnoreCase("0")) {

                        createList(jsonObject);

                    }

                    else {

                        Toast.makeText(CommissionTransfer.this, resultDescription, Toast.LENGTH_LONG).show();
                        recyclerView.setVisibility(View.GONE);
                        linearLayout_record.setVisibility(View.GONE);
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(CommissionTransfer.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CommissionTransfer.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }



    ArrayList arrayList=new ArrayList();
    ArrayList arrayList_temp=new ArrayList();

    public void createList(JSONObject jsonObject){

        arrayList.clear();
        MyApplication.currencyModelArrayList.clear();

        JSONArray jsonArray=jsonObject.optJSONArray("walletList");
        if(jsonArray.length()>0){
            for(int i=0;i<jsonArray.length();i++){
                JSONObject data=jsonArray.optJSONObject(i);
                if(i==0){
                    arrayList.add(data.optString("currencyName"));


                    MyApplication.currencyModelArrayList.add(new CurrencyModel(
                            data.optString("code"),
                            data.optString("code"),
                            data.optString("code"),
                            data.optString("currencyCode"),
                            data.optString("currencyName"),
                            data.optString("currencySymbol"),
                            data.optString("value"),
                            "0.0",
                            "0.0",
                            data.optString("walletOwnerName")
                    ));



                }else{
                    if(data.optString("walletTypeCode").equalsIgnoreCase("100009")){//Commission Wallet
                        if(arrayList.contains(data.optString("currencyName"))){
                            for(int j=0;j<MyApplication.currencyModelArrayList.size();j++){
                                if(MyApplication.currencyModelArrayList.get(j).currencyName.equalsIgnoreCase(data.optString("currencyName"))){
                                    MyApplication.currencyModelArrayList.get(j).setCommisionWalletValue(data.optString("value"));
                                    MyApplication.currencyModelArrayList.get(j).setCcode(data.optString("code"));
                                }
                            }
                        }else{
                            arrayList.add(data.optString("currencyName"));
                            MyApplication.currencyModelArrayList.add(new CurrencyModel(
                                    "",
                                    data.optString("code"),
                                    "",
                                    data.optString("currencyCode"),
                                    data.optString("currencyName"),
                                    data.optString("currencySymbol"),
                                    data.optString("value"),
                                    "0.0",
                                    "0.0",
                                    data.optString("walletOwnerName")
                            ));
                        }

                    }
                    if(data.optString("walletTypeCode").equalsIgnoreCase("100011")){//Overdraft Wallet
                        if(arrayList.contains(data.optString("currencyName"))){
                            for(int j=0;j<MyApplication.currencyModelArrayList.size();j++){
                                if(MyApplication.currencyModelArrayList.get(j).currencyName.equalsIgnoreCase(data.optString("currencyName"))){
                                    MyApplication.currencyModelArrayList.get(j).setOverdraftWalletValue(data.optString("value"));
                                    MyApplication.currencyModelArrayList.get(j).setOcode(data.optString("code"));
                                }
                            }
                        }else{
                            arrayList.add(data.optString("currencyName"));
                            MyApplication.currencyModelArrayList.add(new CurrencyModel(
                                    "",
                                    "",
                                    data.optString("code"),
                                    data.optString("currencyCode"),
                                    data.optString("currencyName"),
                                    data.optString("currencySymbol"),
                                    "0.0",
                                    data.optString("value"),
                                    "0.0",
                                    data.optString("walletOwnerName")
                            ));
                        }
                    }
                    if(data.optString("walletTypeCode").equalsIgnoreCase("100008")){//Main Wallet
                        if(arrayList.contains(data.optString("currencyName"))){
                            for(int j=0;j<MyApplication.currencyModelArrayList.size();j++){

                                if(MyApplication.currencyModelArrayList.get(j).currencyName.equalsIgnoreCase(data.optString("currencyName"))){

                                    MyApplication.currencyModelArrayList.get(j).setMainWalletValue(data.optString("value"));
                                    MyApplication.currencyModelArrayList.get(j).setCode(data.optString("code"));
                                }
                            }
                        }else{
                            arrayList.add(data.optString("currencyName"));
                            MyApplication.currencyModelArrayList.add(new CurrencyModel(
                                    data.optString("code"),
                                    "",
                                    "",
                                    data.optString("currencyCode"),
                                    data.optString("currencyName"),
                                    data.optString("currencySymbol"),
                                    "0.0",
                                    "0.0",
                                    data.optString("value"),
                                    data.optString("walletOwnerName")
                            ));
                        }
                    }

                }

            }
            // JSONArray j=new JSONArray(MyApplication.currencyModelArrayList);
            Gson gson = new GsonBuilder().create();
            JsonArray myCustomArray = gson.toJsonTree(MyApplication.currencyModelArrayList).getAsJsonArray();

            System.out.println("Array List"+myCustomArray.toString());


            MyApplication.currencyModelArrayList_temp.clear();

            for(int i=0;i<MyApplication.currencyModelArrayList.size();i++)
            {
                if(!MyApplication.currencyModelArrayList.get(i).getCommisionWalletValue().equalsIgnoreCase("0.0"))
                {
               //  MyApplication.currencyModelArrayList_temp=MyApplication.currencyModelArrayList.get(i);

                    if(Double.parseDouble(MyApplication.currencyModelArrayList.get(i).getCommisionWalletValue())>0.0)
                         MyApplication.currencyModelArrayList_temp.add(MyApplication.currencyModelArrayList.get(i));
                }
            }


            System.out.println("-------------Array List---------------------"+ MyApplication.currencyModelArrayList_temp.toString());





        }

        CurrencyListCommisionTransaction arraadapter2 = new CurrencyListCommisionTransaction(CommissionTransfer.this, MyApplication.currencyModelArrayList_temp);
        spinner_currency.setAdapter(arraadapter2);



        recyclerView.removeAllViewsInLayout();
        recyclerView.setVisibility(View.VISIBLE);
        linearLayout_record.setVisibility(View.VISIBLE);


        recyclerView.setLayoutManager(new LinearLayoutManager(CommissionTransfer.this));
        TransferCommisionAdapter adpter3 = new TransferCommisionAdapter(CommissionTransfer.this, MyApplication.currencyModelArrayList);
        recyclerView.setAdapter(adpter3);





        //  String currencyName_mssis_agent = MyApplication.getSaveString("CURRENCYNAME_AGENT", TransactionHistoryMainPage.this);
        String currencyName_mssis_agent = currencyCode;  // no currency tag is comming in MSSID

       /* for (int i = 0; i < MyApplication.currencyModelArrayList.size(); i++) {
            if (currencyName_mssis_agent.equalsIgnoreCase(MyApplication.currencyModelArrayList.get(i).getCurrencyName()))
            {
                walletCode = MyApplication.currencyModelArrayList.get(i).code;
                spinner_currency.setSelection(i);
            }
        }*/



        // MyApplication.showloader(TransactionHistoryMainPage.this, getString(R.string.getting_user_info));

        //  api_transactionHistory_all();

        // getTransactionList();

        // callApiMiniStatementTrans(walletCode,"100008");

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
                MyApplication.hideKeyboard(CommissionTransfer.this);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(CommissionTransfer.this);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }



    boolean validation_mobile_Details() {

        amountstr = edittext_amount.getText().toString().trim();
        mpinStr = et_mpin.getText().toString();

        if (amountstr.isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.plz_enter_amount));

            return false;
        }


        else if(mpinStr.trim().isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.please_enter_4_digit_mpin));

            return false;
        }

        else if(mpinStr.trim().length() == 4) {

            return true;
        }
        else {

            MyApplication.showErrorToast(this, getString(R.string.please_enter_4_digit_mpin));

            return false;
        }

    }

    boolean validation_mobile_Detailsf() {

        amountstr = edittext_amount.getText().toString().trim();


        if (amountstr.isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.plz_enter_amount));

            return false;
        }



      return true;

    }









    private void api_walletOwnerUser() {

        String USER_CODE_FROM_TOKEN_AGENTDETAILS = MyApplication.getSaveString("userCode", CommissionTransfer.this);


        API.GET_CASHOUT_DETAILS("ewallet/api/v1/walletOwnerUser/" + USER_CODE_FROM_TOKEN_AGENTDETAILS, languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1789408\",\"requestTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"responseTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwnerUser\":{\"id\":2171,\"code\":\"101917\",\"firstName\":\"TATASnegal\",\"userName\":\"TATASnegal5597\",\"mobileNumber\":\"8888888882\",\"email\":\"kundan.kumar@esteltelecom.com\",\"walletOwnerUserTypeCode\":\"100000\",\"walletOwnerUserTypeName\":\"Supervisor\",\"walletOwnerCategoryCode\":\"100000\",\"walletOwnerCategoryName\":\"Institute\",\"userCode\":\"1000002606\",\"status\":\"Active\",\"state\":\"Approved\",\"gender\":\"M\",\"idProofTypeCode\":\"100004\",\"idProofTypeName\":\"MILITARY ID CARD\",\"idProofNumber\":\"44444444444\",\"creationDate\":\"2021-10-01T09:04:07.330+0530\",\"notificationName\":\"EMAIL\",\"notificationLanguage\":\"en\",\"createdBy\":\"100308\",\"modificationDate\":\"2021-10-20T14:59:00.791+0530\",\"modifiedBy\":\"100308\",\"addressList\":[{\"id\":3569,\"walletOwnerUserCode\":\"101917\",\"addTypeCode\":\"100001\",\"addTypeName\":\"Commercial\",\"regionCode\":\"100068\",\"regionName\":\"Boke\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"city\":\"100022\",\"cityName\":\"Dubreka\",\"addressLine1\":\"delhi\",\"status\":\"Inactive\",\"creationDate\":\"2021-10-01T09:04:07.498+0530\",\"createdBy\":\"100250\",\"modificationDate\":\"2021-10-03T09:52:57.407+0530\",\"modifiedBy\":\"100308\"}],\"workingDaysList\":[{\"id\":3597,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100000\",\"weekdaysName\":\"Monday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.518+0530\"},{\"id\":3598,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100001\",\"weekdaysName\":\"Tuesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.528+0530\"},{\"id\":3599,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100002\",\"weekdaysName\":\"Wednesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.538+0530\"},{\"id\":3600,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100003\",\"weekdaysName\":\"Thursday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.547+0530\"},{\"id\":3601,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100004\",\"weekdaysName\":\"Friday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.556+0530\"},{\"id\":3602,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100005\",\"weekdaysName\":\"Saturday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.565+0530\"},{\"id\":3603,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100006\",\"weekdaysName\":\"Sunday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"2:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.573+0530\"}],\"macEnabled\":false,\"ipEnabled\":false,\"resetCredReqInit\":false,\"notificationTypeCode\":\"100000\"}}");


                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {

                        JSONObject walletOwnerUser = jsonObject.getJSONObject("walletOwnerUser");

                      //String  issuingCountryName = walletOwnerUser.getString("issuingCountryName");

                        rp_tv_agentName.setText(agentName_from_walletOwner);
                        rp_tv_mobileNumber.setText(MyApplication.getSaveString("USERNAME", CommissionTransfer.this));
                        rp_tv_businessType.setText(businessTypeName_walletOwnerCategoryCode);
                        rp_tv_country.setText(MyApplication.getSaveString("COUNTRYNAME_AGENT", CommissionTransfer.this));

                        rp_tv_receiverName.setText(selectInstititueName);
                        rp_tv_transactionAmount.setText(amountstr);

                        rp_tv_email.setText(email_walletOwnerCategoryCode = walletOwnerUser.getString("email"));


                    } else {
                        Toast.makeText(CommissionTransfer.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(CommissionTransfer.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CommissionTransfer.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void mpin_final_api() {

        try {


            JSONObject jsonObject = new JSONObject();


            jsonObject.put("transactionType","113093"); // Hard code
            jsonObject.put("srcWalletOwnerCode",MyApplication.getSaveString("USERCODE", CommissionTransfer.this));
            jsonObject.put("srcCurrencyCode",select_currencyCode);
            jsonObject.put("value",amountstr);
            jsonObject.put("channelTypeCode","100000");

             String encryptionDatanew = AESEncryption.getAESEncryption(mpinStr);
              jsonObject.put("pin",encryptionDatanew);


            // String encryptionDatanew = AESEncryption.getAESEncryption(mpinStr);
            //  jsonObject.put("pin",encryptionDatanew);


            API.POST_TRANSFERDETAILS("ewallet/api/v1/walletTransfer/commissionTransfer/", jsonObject, languageToUse, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {


                        //  JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1930978\",\"requestTime\":\"Tue Nov 02 15:27:01 IST 2021\",\"responseTime\":\"Tue Nov 02 15:27:01 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOperation\":{\"code\":\"1000152390\",\"featureCode\":\"100076\",\"desWalletCode\":\"1000028686\",\"srcWalletCode\":\"1000028522\",\"srcWalletOwnerCode\":\"1000002785\",\"srcWalletOwnerName\":\"sharique agent\",\"srcCurrencyCode\":\"100062\",\"srcCurrencyName\":\"GNF\",\"srcWalletTypeCode\":\"100008\",\"srcWalletTypeName\":\"Main Wallet\",\"desWalletTypeCode\":\"100008\",\"desWalletTypeName\":\"Main Wallet\",\"desWalletOwnerCode\":\"1000002817\",\"desWalletOwnerName\":\"Rahul Inst\",\"desWalletOwnerNumber\":\"9910859186\",\"amount\":1500,\"channelTypeCode\":\"100000\",\"desCurrencyCode\":\"100062\",\"desCurrencyName\":\"GNF\",\"status\":\"Pending\",\"createdBy\":\"102068\",\"creationDate\":\"2021-11-02T15:27:01.364+0530\",\"tax\":0,\"fee\":0,\"finalAmount\":1500,\"srcCurrencySymbol\":\"Fr\",\"desCurrencySymbol\":\"Fr\",\"transactionType\":\"101611\",\"serviceCode\":\"100020\",\"serviceCategoryCode\":\"ALL011\",\"serviceProviderCode\":\"100109\"}}");

                        String resultCode = jsonObject.getString("resultCode");
                        String resultDescription = jsonObject.getString("resultDescription");

                        if (resultCode.equalsIgnoreCase("0")) {

                            ll_page_1.setVisibility(View.GONE);
                            ll_reviewPage.setVisibility(View.GONE);
                            ll_successPage.setVisibility(View.VISIBLE);
                            ll_receiptPage.setVisibility(View.GONE);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    alert_dialogue_sh("Commission transfer successfully to main wallet");

                                }
                            }, 2000);


                        } else {
                            Toast.makeText(CommissionTransfer.this, resultDescription, Toast.LENGTH_LONG).show();
                            //  finish();
                        }


                    } catch (Exception e) {
                        Toast.makeText(CommissionTransfer.this, e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }


                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(CommissionTransfer.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    }
                    else

                        Toast.makeText(CommissionTransfer.this, aFalse, Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (Exception e)
        {
            Toast.makeText(CommissionTransfer.this,e.toString(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_nextClick: {


                if (validation_mobile_Details()) {


                    if (new InternetCheck().isConnected(CommissionTransfer.this)) {

                        MyApplication.showloader(CommissionTransfer.this, getString(R.string.please_wait));

                        mpin_final_api();

                    } else {
                        Toast.makeText(CommissionTransfer.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                    }
                }
            }

            break;

            case R.id.tvContinue: {


                alert_dialogue_sh("Commission transfer successfully to main wallet ");

            }
            break;



            case R.id.exportReceipt_textview: {
                close_receiptPage_textview.setVisibility(View.GONE);
                exportReceipt_textview.setVisibility(View.GONE);
                Bitmap bitmap = getScreenShot(rootView);
                createImageFile(bitmap);
                //store(bitmap, "test.jpg");
            }

                break;

            case R.id.previous_reviewClick_textview: {

                ll_page_1.setVisibility(View.VISIBLE);
                ll_reviewPage.setVisibility(View.GONE);
                ll_receiptPage.setVisibility(View.GONE);
            }
            break;

            case R.id.qrCode_imageButton: {


                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.setPrompt("Scan a barcode or QR Code");
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.initiateScan();


            }

            break;

            case R.id.close_receiptPage_textview:
                 {
//                    ll_page_1.setVisibility(View.VISIBLE);
//                    ll_reviewPage.setVisibility(View.GONE);
//                    ll_receiptPage.setVisibility(View.GONE);

                     Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                     intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                     startActivity(intent);
                     finish();

                 }

            break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {

            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if (result != null) {

                System.out.println(resultCode);

                if (result.getContents() == null) {

                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                }

                else {


                    String str = result.getContents();

                    if (str.equalsIgnoreCase("")) {
                       // 1000002786:TarunMwTest

                        Toast.makeText(this, "QR Code Not Valid", Toast.LENGTH_LONG).show();
                       // edittext_mobileNuber.setEnabled(true);

                    }
                    else {


                        String[] qrData = str.split("\\:");

                       // mobileNoStr=qrData[0];
                       // edittext_mobileNuber.setText(mobileNoStr);
                       // edittext_mobileNuber.setEnabled(false);
                        // Toast.makeText(this, "QR Code  Valid", Toast.LENGTH_LONG).show();

                       }

                }

            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }


        } catch (Exception e) {

            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }

    }

   /* @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }*/


    public static Bitmap getScreenShot(View view) {
        View screenView = view;
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public  void createImageFile(Bitmap bm)  {
        try {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(System.currentTimeMillis());
            File storageDir = new File(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/");
            if (!storageDir.exists())
                storageDir.mkdirs();
            File image = File.createTempFile(
                    timeStamp,
                    ".jpeg",
                    storageDir
            );

            System.out.println(image.getAbsolutePath());
            if (image.exists()) image.delete();
            //   Log.i("LOAD", root + fname);
            try {
                FileOutputStream out = new FileOutputStream(image);
                bm.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            shareImage(image);
        }catch (Exception e){

        }
    }

//    public  void store(Bitmap bm, String fileName){
//        final  String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
//        File dir = new File(dirPath);
//        if(!dir.exists())
//            dir.mkdirs();
//        File file = new File(dirPath, fileName);
//        try {
//            FileOutputStream fOut = new FileOutputStream(file);
//            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
//            fOut.flush();
//            fOut.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        shareImage(file);
//    }

    private void shareImage(File file){
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.share_screenshot)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_app_available), Toast.LENGTH_SHORT).show();
        }
    }




    public void alert_dialogue_sh(String transactionSuccess) {

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.transaction_details))
              //  .setIcon(R.drawable.ic_baseline_translate_blue)
                .setMessage(transactionSuccess)
                .setCancelable(false)

//                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                       finish();
                    }
                }).create().show();
    }
    int SpinnerPos;
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinner_currency:
            {

                try {

                     SpinnerPos = i;
                     walletCode = MyApplication.currencyModelArrayList_temp.get(i).code;
                     select_currencySymbol = MyApplication.currencyModelArrayList_temp.get(i).currencySymbol;
                     select_currencyCode = MyApplication.currencyModelArrayList_temp.get(i).currencyCode;
                     mainbalance_textview.setText(MyApplication.currencyModelArrayList_temp.get(i).mainWalletValue);
                     available_balance.setText("Available balance in Commision Wallet ("+select_currencySymbol+" "+MyApplication.currencyModelArrayList_temp.get(i).commisionWalletValue+" )");



                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(CommissionTransfer.this, e.toString(), Toast.LENGTH_LONG).show();

                }

            }

            break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}