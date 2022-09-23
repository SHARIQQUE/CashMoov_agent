package com.agent.cashmoovui.transfer_float;

import static com.agent.cashmoovui.apiCalls.CommonData.sellfloat_walletOwnerCategoryCode;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.text.method.DigitsKeyListener;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agent.cashmoovui.AddContact;
import com.agent.cashmoovui.HiddenPassTransformationMethod;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.activity.OtherOption;
import com.agent.cashmoovui.adapter.RecordAdapter;
import com.agent.cashmoovui.adapter.SellFloatAdapterRecycle;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.apiCalls.BioMetric_Responce_Handler;
import com.agent.cashmoovui.cash_in.CashIn;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.login.LoginPin;
import com.agent.cashmoovui.model.InstituteListModel;
import com.agent.cashmoovui.otp.VerifyLoginAccountScreen;
import com.agent.cashmoovui.set_pin.AESEncryption;
import com.aldoapps.autoformatedittext.AutoFormatUtil;
import com.blikoon.qrcodescanner.QrCodeActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class SellFloat extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener, CallBackSellFloatRecycleViewClick {

    String[] strArray = {"10", "25", "50", "100"};

    String recordString = "10";
    ImageView imgBack, imgHome;
    public static LoginPin loginpinC;
    ImageButton qrCode_imageButton;
    ImageView contact;
    public EditText mEnterinstituteEdittext, mInstitutenameEdittext;

    RecyclerView recyclerView;


    String currencyName_from_currency = "";

    ArrayList<SellFloatModal> arrayList_sellFloat;


    boolean isPasswordVisible;

    String currencyCode_agent = "", countryCode_agent = "", currencyName_agent = "", countryName_agent;

    String currencyCode_subscriber = "", countryCode_subscriber = "", currencyName_subscriber = "", desWalletOwnerCode_from_allSellFloat = "";


    Spinner spinner_record;
    TextView spinner_insititue, spinner_currency, tvAmtCurr;

    View rootView;

    EditText etPin;
    TextView rp_tv_comment,rp_tv_convertionrate, exportReceipt_textview, tv_nextClick, rp_tv_agentName, rp_tv_mobileNumber, rp_tv_businessType, rp_tv_email, rp_tv_country, rp_tv_receivermobile,rp_tv_receiverName, rp_tv_transactionAmount, rp_tv_fees_reveiewPage, receiptPage_tv_stransactionType, receiptPage_tv_dateOfTransaction, receiptPage_tv_transactionAmount,
            receiptPage_tv_amount_to_be_credit, receiptPage_tv_fee, receiptPage_tv_financialtax, receiptPage_tv_transaction_receiptNo, receiptPage_tv_sender_name,
            receiptPage_tv_sender_phoneNo,
            receiptPage_tv_receiver_name, receiptPage_tv_receiver_phoneNo, close_receiptPage_textview, tvContinue, rp_tv_excise_tax, rp_tv_amount_to_be_charge, rp_tv_amount_paid, previous_reviewClick_textview, confirm_reviewClick_textview;
    LinearLayout pinLinearselffloat,taxselffloatLinear,ll_page_1, ll_reviewPage, ll_receiptPage, main_layout, ll_successPage, linearLayout_record;

    MyApplication applicationComponentClass;
    String languageToUse = "";

    EditText edittext_amount, et_mpin,et_fp_reason_sending;
    private SpinnerDialog spinnerDialogImstitute, spinnerDialogCurrency;

    String amountstr = "", agentName_from_walletOwner = "", businessTypeName_walletOwnerCategoryCode = "", email_walletOwnerCategoryCode = "";

    String walletOwnerCode_mssis_agent = "", walletOwnerCode_subs, senderNameAgent = "";

    String select_insitute_name = "", select_insitute_code = "", select_insitute_currencyCode = "", select_insitute_countryCode = "";

    String tax_financial = "",tax_financialtypename, fees_amount, totalAmount_str, receivernameStr = "";
    Double tax_financial_double = 0.0, amountstr_double = 0.0, fees_amount_double = 0.0, totalAmount_double = 0.0;

    String mpinStr = "";


    String serviceCode_from_serviceCategory = "", serviceCategoryCode_from_serviceCategory = "", serviceProviderCode_from_serviceCategory;


    ArrayList<String> arrayList_instititueName;
    ArrayList<String> arrayList_instititueCode;
    ArrayList<String> arrayList_instititue_countryCode;


    ArrayList<String> arrayList_currecnyName = new ArrayList<String>();
    ArrayList<String> arrayList_currecnyCode = new ArrayList<String>();
    ArrayList<String> arrayList_currencySymbol = new ArrayList<String>();


    String select_currecnyName = "";
    String select_currecnyCode = "";

    String currencySymbol_sender = "";
    String currencySymbol_receiver = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        applicationComponentClass = (MyApplication) getApplicationContext();


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


        setContentView(R.layout.sellfloat);
        setBackMenu();

        rootView = getWindow().getDecorView().findViewById(R.id.main_layout);


        //     First page

        ll_page_1 = (LinearLayout) findViewById(R.id.ll_page_1);
        taxselffloatLinear=findViewById(R.id.taxselffloatLinear);
        tv_nextClick = (TextView) findViewById(R.id.tv_nextClick);
        edittext_amount = (EditText) findViewById(R.id.edittext_amount);
        et_fp_reason_sending=findViewById(R.id.et_fp_reason_sending);
        rp_tv_comment=findViewById(R.id.rp_tv_comment);
        pinLinearselffloat=findViewById(R.id.pinLinearselffloat);
        //  contact = (ImageView) findViewById(R.id.contact);
          /*  contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SellFloat.this,
                            AddContact.class);
                    startActivityForResult(intent , REQUEST_CODE);
                }
            });*/


        edittext_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if (isFormatting) {
                    return;
                }

                if (s.length() > 0) {
                    formatInput(edittext_amount, s, s.length(), s.length());




                }

                isFormatting = false;


            }
        });


        //    Reveiw page

        ll_reviewPage = (LinearLayout) findViewById(R.id.ll_reviewPage);


        rp_tv_agentName = (TextView) findViewById(R.id.rp_tv_agentName);
        rp_tv_mobileNumber = (TextView) findViewById(R.id.rp_tv_mobileNumber);
        rp_tv_businessType = (TextView) findViewById(R.id.rp_tv_businessType);
        rp_tv_email = (TextView) findViewById(R.id.rp_tv_email);
        rp_tv_country = (TextView) findViewById(R.id.rp_tv_country);
        rp_tv_receivermobile=findViewById(R.id.rp_tv_receivermobile);
        rp_tv_receiverName = (TextView) findViewById(R.id.rp_tv_receiverName);
        rp_tv_transactionAmount = (TextView) findViewById(R.id.rp_tv_transactionAmount);
        rp_tv_fees_reveiewPage = (TextView) findViewById(R.id.rp_tv_fees_reveiewPage);
        rp_tv_excise_tax = (TextView) findViewById(R.id.rp_tv_excise_tax);
        rp_tv_amount_to_be_charge = (TextView) findViewById(R.id.rp_tv_amount_to_be_charge);
        rp_tv_amount_paid = (TextView) findViewById(R.id.rp_tv_amount_paid);


        et_mpin = (EditText) findViewById(R.id.et_mpin);
        previous_reviewClick_textview = (TextView) findViewById(R.id.previous_reviewClick_textview);
        confirm_reviewClick_textview = (TextView) findViewById(R.id.confirm_reviewClick_textview);

        //    Receipt page

        tvContinue = (TextView) (findViewById(R.id.tvContinue));

        tvContinue.setOnClickListener(this);

        ll_receiptPage = (LinearLayout) findViewById(R.id.ll_receiptPage);
        main_layout = (LinearLayout) findViewById(R.id.main_layout);
        ll_successPage = (LinearLayout) findViewById(R.id.ll_successPage);
        linearLayout_record = (LinearLayout) findViewById(R.id.linearLayout_record);
        exportReceipt_textview = (TextView) findViewById(R.id.exportReceipt_textview);
        rp_tv_convertionrate = (TextView) findViewById(R.id.rp_tv_convertionrate);
        exportReceipt_textview.setOnClickListener(this);
        rp_tv_convertionrate.setVisibility(View.GONE);
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


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        qrCode_imageButton.setOnClickListener(this);
        tv_nextClick.setOnClickListener(this);
        previous_reviewClick_textview.setOnClickListener(this);
        confirm_reviewClick_textview.setOnClickListener(this);
        close_receiptPage_textview.setOnClickListener(this);

        walletOwnerCode_mssis_agent = MyApplication.getSaveString("walletOwnerCode", SellFloat.this);


        mEnterinstituteEdittext = findViewById(R.id.enterinstituteEdittext);
        mInstitutenameEdittext = findViewById(R.id.institutenameEdittext);
        mInstitutenameEdittext.setEnabled(false);
         mEnterinstituteEdittext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (mEnterinstituteEdittext.getRight() - mEnterinstituteEdittext.getCompoundDrawables()[RIGHT].getBounds().width())) {
                        int selection = mEnterinstituteEdittext.getSelectionEnd();
                        Intent intent = new Intent(SellFloat.this,
                                AddContact.class);
                        startActivityForResult(intent, REQUEST_CODE);

                    }
                }
                return false;
            }
        });





        mEnterinstituteEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 9) {
                    mInstitutenameEdittext.setText("");
                    for (int i = 0; i < arrayList_instititueCode.size(); i++) {
                        if (mEnterinstituteEdittext.getText().toString().equalsIgnoreCase(arrayList_instititueCode.get(i))) {
                            String institutename = arrayList_instititueName.get(i).replaceAll("[0-9]", "");// prints awhefqoakwfn
                            String institutenamenew = institutename.replaceAll("[(){}]","");


                           // Toast.makeText(applicationComponentClass, "get name"+institutenamenew, Toast.LENGTH_SHORT).show();
                            mInstitutenameEdittext.setText(institutenamenew);
                            tv_nextClick.setVisibility(View.VISIBLE);
                            MyApplication.hideKeyboard(SellFloat.this);
                            setSelction(i);


                        }

                    }
                    if(mInstitutenameEdittext.getText().toString().trim().isEmpty()){
                        MyApplication.showToast(SellFloat.this,getString(R.string.check_another_institute));
                        tv_nextClick.setVisibility(View.GONE);
                        mInstitutenameEdittext.setText("");
                    }

                }else {
                    if(s.length()<=9){
                        tv_nextClick.setVisibility(View.GONE);
                        mInstitutenameEdittext.setText("");

                    }
                }

            }
        });



        //  spinner_insititue.setText("Select Institute");
/*
            spinner_insititue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (spinnerDialogImstitute!=null)
                        spinnerDialogImstitute.showSpinerDialog();
                }
            });
*/

            spinner_record= (Spinner) findViewById(R.id.spinner_record);
            spinner_record.setOnItemSelectedListener(this);


        tvAmtCurr = findViewById(R.id.tvAmtCurr);
            spinner_currency= findViewById(R.id.spinner_currency);
        spinner_currency.setText(getString(R.string.select_currency));
            spinner_currency.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (spinnerDialogCurrency!=null)
                        spinnerDialogCurrency.showSpinerDialog();
                }
            });



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
                    MyApplication.hideKeyboard(SellFloat.this);            }
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




            if (new InternetCheck().isConnected(SellFloat.this)) {


                api_insititute();




            } else {
                Toast.makeText(SellFloat.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
            }

            RecordAdapter recordAdapter = new RecordAdapter(SellFloat.this, strArray);
            spinner_record.setAdapter(recordAdapter);




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
                MyApplication.hideKeyboard(SellFloat.this);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(SellFloat.this);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    ArrayList<InstituteListModel>instituteListModelArrayList = new ArrayList<>();
    private void api_insititute() {


        API.GET_TRANSFER_DETAILS("ewallet/api/v1/walletOwner/all?walletOwnerCategoryCode="+sellfloat_walletOwnerCategoryCode,languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {


                        arrayList_instititueName = new ArrayList<String>();
                        arrayList_instititueCode = new ArrayList<String>();
                        arrayList_instititue_countryCode = new ArrayList<String>();



                       // arrayList_instititueName.add(0,getString(R.string.select_institute_star));
                       // arrayList_instititueCode.add(0,getString(R.string.select_institute_star));
                       // arrayList_instititue_countryCode.add(0,getString(R.string.select_institute_star));



                        String ownerNameTemp="";
                        String mobileNumberTemp="";

                        instituteListModelArrayList.clear();

                        JSONArray jsonArray = jsonObject.getJSONArray("walletOwnerList");
                        for(int i=0;i<jsonArray.length();i++) {

                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                            if (jsonObject3.optString("state").equalsIgnoreCase("Approved")) {


                            instituteListModelArrayList.add(new InstituteListModel(
                                    jsonObject3.optString("code"),
                                    jsonObject3.optString("walletOwnerCategoryCode"),
                                    jsonObject3.optString("ownerName"),
                                    jsonObject3.optString("mobileNumber"),
                                    jsonObject3.optString("email"),
                                    jsonObject3.optString("dateOfBirth"),
                                    jsonObject3.optString("registerCountryCode"),
                                    jsonObject3.optString("registerCountryName"),
                                    jsonObject3.optString("walletOwnerCode")
                            ));


                            if (jsonObject3.has("walletOwnerCode")) {
                                walletOwnerCode_subs = jsonObject3.getString("walletOwnerCode");
                            }


                            if (jsonObject3.has("registerCountryCode")) {
                                countryCode_subscriber = jsonObject3.getString("registerCountryCode");
                            }

                            if (jsonObject3.has("ownerName")) {
                                ownerNameTemp = jsonObject3.getString("ownerName");
                                String mobileNumber_temp;

                                if (jsonObject3.has("mobileNumber")) {
                                    mobileNumber_temp = jsonObject3.getString("mobileNumber");
                                    arrayList_instititueName.add(mobileNumber_temp + "(" + ownerNameTemp + ")");
                                } else {
                                    arrayList_instititueName.add(ownerNameTemp);
                                }


                            }

                            if (jsonObject3.has("mobileNumber")) {
                                mobileNumberTemp = jsonObject3.getString("mobileNumber");
                                arrayList_instititueCode.add(mobileNumberTemp);
                            }


                            if (jsonObject3.has("issuingCountryCode")) {
                                String countryCode_insititute = jsonObject3.getString("issuingCountryCode");
                                arrayList_instititue_countryCode.add(countryCode_insititute);
                            }

                            if (jsonObject3.has("businessTypeName")) {
                                businessTypeName_walletOwnerCategoryCode = jsonObject3.getString("businessTypeName");
                            }


                            // countryName_walletOwnerCategoryCode = jsonObject3.getString("issuingCountryName");
                        }

                        }


                        System.out.println("get name list"+jsonObject);
                        System.out.println(arrayList_instititueCode);




                        spinnerDialogImstitute = new SpinnerDialog(SellFloat.this, arrayList_instititueName, "Select Institute Type", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation

                        spinnerDialogImstitute.setCancellable(true); // for cancellable
                        spinnerDialogImstitute.setShowKeyboard(false);// for open keyboard by default
                        spinnerDialogImstitute.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, int position) {
                                //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                setSelction(position);
                               // spBusinessType.setTag(position);

                            }
                        });
                        api_allSellFloat_featureCode(recordString);

                        //api_currency_spinner_details();


                    } else {
                        Toast.makeText(SellFloat.this, resultDescription, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    Toast.makeText(SellFloat.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(SellFloat.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void api_walletOwner() {


        String userCode_agentCode_from_mssid =  MyApplication.getSaveString("USERCODE",SellFloat.this);


        API.GET_TRANSFER_DETAILS("ewallet/api/v1/walletOwner/"+userCode_agentCode_from_mssid,languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {



                MyApplication.hideLoader();

                try {


                    //    JSONObject jsonObject1 = new JSONObject("{\"transactionId\":\"1927802\",\"requestTime\":\"Tue Nov 02 13:03:30 IST 2021\",\"responseTime\":\"Tue Nov 02 13:03:30 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwner\":{\"id\":110679,\"code\":\"1000002785\",\"walletOwnerCategoryCode\":\"100000\",\"ownerName\":\"sharique agent\",\"mobileNumber\":\"9990063618\",\"businessTypeCode\":\"100008\",\"businessTypeName\":\"Goldsmith\",\"lineOfBusiness\":\"gffg\",\"idProofNumber\":\"trt465656\",\"email\":\"sharique9718@gmail.com\",\"status\":\"Active\",\"state\":\"Approved\",\"stage\":\"Document\",\"idProofTypeCode\":\"100005\",\"idProofTypeName\":\"COMPANY REGISTRATION NUMBER\",\"idExpiryDate\":\"2021-10-22\",\"notificationLanguage\":\"en\",\"notificationTypeCode\":\"100000\",\"notificationName\":\"EMAIL\",\"issuingCountryCode\":\"100102\",\"issuingCountryName\":\"India\",\"registerCountryCode\":\"100102\",\"registerCountryName\":\"India\",\"createdBy\":\"100250\",\"modifiedBy\":\"100308\",\"creationDate\":\"2021-10-19T22:38:48.969+0530\",\"modificationDate\":\"2021-11-01T13:49:14.892+0530\",\"walletExists\":true,\"profileTypeCode\":\"100000\",\"profileTypeName\":\"tier1\",\"walletCurrencyList\":[\"100018\",\"100017\",\"100069\",\"100020\",\"100004\",\"100029\",\"100062\",\"100003\"],\"walletOwnerCatName\":\"Institute\",\"requestedSource\":\"ADMIN\",\"regesterCountryDialCode\":\"+91\",\"issuingCountryDialCode\":\"+91\",\"walletOwnerCode\":\"1000002785\"}}");


                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {

                        String walletOwnerCategoryCodeTemp="";
                        String ownerNameTemp="";

                        JSONObject jsonObject_walletOwner = jsonObject.getJSONObject("walletOwner");

                        walletOwnerCategoryCodeTemp = jsonObject_walletOwner.getString("walletOwnerCategoryCode");

                        agentName_from_walletOwner = jsonObject_walletOwner.getString("ownerName");



                    } else {
                        Toast.makeText(SellFloat.this, resultDescription, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    Toast.makeText(SellFloat.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(SellFloat.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }



    boolean validation_mobile_Details() {



        amountstr = edittext_amount.getText().toString().trim().replace(",","");


        if (mEnterinstituteEdittext.getText().toString().isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.enter_institute_number));

            return false;
        }


        else if (spinner_currency.getText().equals(getString(R.string.select_currency))) {

            MyApplication.showErrorToast(this, getString(R.string.select_currency));

            return false;
        }



        else if (amountstr.isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.amount_to_paid_without_star));

            return false;
        }
        else  if(Double.parseDouble(edittext_amount.getText().toString().trim().replace(",",""))<MyApplication.ToSelfFloatMinAmount) {
            MyApplication.showErrorToast(SellFloat.this,getString(R.string.val_amount_min)+" "+MyApplication.ToSelfFloatMinAmount);
            return false;
        }

        else   if(Double.parseDouble(edittext_amount.getText().toString().trim().replace(",",""))>MyApplication.ToSelfFloatMaxAmount) {
            MyApplication.showErrorToast(SellFloat.this,getString(R.string.val_amount_max)+" "+MyApplication.ToSelfFloatMaxAmount);
            return false;


        }

        return true;
    }






    private void api_currency_spinner_details(String userCode_agentCode_from_mssid) {

       // String userCode_agentCode_from_mssid =  MyApplication.getSaveString("USERCODE",SellFloat.this);

        API.GET_TRANSFER_DETAILS("ewallet/api/v1/walletOwnerCountryCurrency/"+userCode_agentCode_from_mssid,languageToUse,new Api_Responce_Handler() {

            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {


                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");

                    arrayList_currecnyName.clear();
                    arrayList_currecnyCode.clear();
                    arrayList_currencySymbol.clear();

                    if(resultCode.equalsIgnoreCase("0")) {


                       /* arrayList_currecnyName.add(0,getString(R.string.select_currency_star));
                        arrayList_currecnyCode.add(0,getString(R.string.select_currency_star));
                        arrayList_currencySymbol.add(0,getString(R.string.select_currency_star));
*/
                        JSONArray jsonArray = jsonObject.getJSONArray("walletOwnerCountryCurrencyList");
                        for(int i=0;i<jsonArray.length();i++)
                        {

                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);

                            String currencyName = jsonObject3.getString("currencyName");
                            String currencyCode = jsonObject3.getString("currencyCode");
                            String currencySymbol = jsonObject3.getString("currencySymbol");


                            arrayList_currecnyName.add(currencyName);
                            arrayList_currecnyCode.add(currencyCode);
                            arrayList_currencySymbol.add(currencySymbol);


                        }




                        spinnerDialogCurrency = new SpinnerDialog(SellFloat.this, arrayList_currecnyName, getString(R.string.select_currency), R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation

                        spinnerDialogCurrency.setCancellable(true); // for cancellable
                        spinnerDialogCurrency.setShowKeyboard(false);// for open keyboard by default
                        spinnerDialogCurrency.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, int position) {
                                //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                setSelctionCurrency(position);
                                // spBusinessType.setTag(position);

                            }
                        });



                        api_walletOwner();
                    }

                    else {
                        Toast.makeText(SellFloat.this, resultDescription, Toast.LENGTH_LONG).show();
                        //finish();
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(SellFloat.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(SellFloat.this, aFalse, Toast.LENGTH_SHORT).show();
                //finish();

            }
        });


    }


    private void api_currency_subscriber() {

        API.GET_CASHOUT_DETAILS("ewallet/api/v1/walletOwnerCountryCurrency/" + walletOwnerCode_subs, languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {


                try {

                    //JSONObject jsonObject = new JSONObject("{"transactionId":"1789322","requestTime":"Wed Oct 20 15:53:33 IST 2021","responseTime":"Wed Oct 20 15:53:33 IST 2021","resultCode":"0","resultDescription":"Transaction Successful","walletOwnerCountryCurrencyList":[{"id":7452,"code":"107451","walletOwnerCode":"1000002488","currencyCode":"100062","currencyName":"GNF","currencySymbol":"Fr","countryCurrencyCode":"100076","inBound":true,"outBound":true,"status":"Active"}]}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("walletOwnerCountryCurrencyList");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            if(jsonObject2.has("currencyName")) {

                                String  currencyName_subscriber_temp = jsonObject2.getString("currencyName");
                                if (currencyName_subscriber_temp.equalsIgnoreCase("GNF")) {
                                    currencyName_subscriber = jsonObject2.getString("currencyName");
                                    currencyCode_subscriber = jsonObject2.getString("currencyCode");

                                } else {

                                }
                            }
                        }


                        api_exchange_rate();



                    } else {
                        MyApplication.hideLoader();

                        Toast.makeText(SellFloat.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                } catch (Exception e) {
                    MyApplication.hideLoader();

                    Toast.makeText(SellFloat.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(SellFloat.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void api_currency_sender() {

        API.GET_CASHIN_DETAILS("ewallet/api/v1/walletOwnerCountryCurrency/"+walletOwnerCode_mssis_agent,languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {


                try {

                    //JSONObject jsonObject = new JSONObject("{"transactionId":"1789322","requestTime":"Wed Oct 20 15:53:33 IST 2021","responseTime":"Wed Oct 20 15:53:33 IST 2021","resultCode":"0","resultDescription":"Transaction Successful","walletOwnerCountryCurrencyList":[{"id":7452,"code":"107451","walletOwnerCode":"1000002488","currencyCode":"100062","currencyName":"GNF","currencySymbol":"Fr","countryCurrencyCode":"100076","inBound":true,"outBound":true,"status":"Active"}]}");

                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");

                    if(resultCode.equalsIgnoreCase("0")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("walletOwnerCountryCurrencyList");
                        for(int i=0;i<jsonArray.length();i++) {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);


                            if(jsonObject2.has("currencyName")) {

                                String  currencyName_agent_temp = jsonObject2.getString("currencyName");
                               // if(data.optString("currencyCode").equalsIgnoreCase(MyApplication.getSaveString("countryCode_Loginpage",MainActivity.this)))
                                if (currencyName_agent_temp.equalsIgnoreCase("GNF")) {
                                    currencyCode_agent = jsonObject2.getString("currencyCode");
                                    currencyName_agent = jsonObject2.getString("currencyName");

                                } else {

                                }
                            }

                        }


                        api_currency_subscriber();

                    }

                    else {
                        MyApplication.hideLoader();

                        Toast.makeText(SellFloat.this, resultDescription, Toast.LENGTH_LONG).show();
                        // finish();
                    }


                }
                catch (Exception e)
                {
                    MyApplication.hideLoader();

                    Toast.makeText(SellFloat.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(SellFloat.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void api_allSellFloat_featureCode( String recordString) {



        String userCode_agentCode_from_mssid =  MyApplication.getSaveString("USERCODE",SellFloat.this);

        String featureCode_hardCode="100076";   // 100076 is hard code according to praveen 19 Nov

        API.GET_TRANSFER_DETAILS("ewallet/api/v1/walletTransfer/allSellFloat?featureCode="+featureCode_hardCode+"&srcWalletOwnerCode="+userCode_agentCode_from_mssid+"&offset=0&limit="+recordString,languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    //    JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1924722\",\"requestTime\":\"Tue Nov 02 09:07:39 IST 2021\",\"responseTime\":\"Tue Nov 02 09:07:39 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"pageable\":{\"totalRecords\":2},\"walletOperationList\":[{\"id\":152211,\"code\":\"1000152373\",\"featureCode\":\"100076\",\"desWalletCode\":\"1000028775\",\"srcWalletCode\":\"1000028522\",\"srcWalletOwnerCode\":\"1000002785\",\"srcWalletOwnerName\":\"sharique agent\",\"srcCurrencyCode\":\"100062\",\"srcCurrencyName\":\"GNF\",\"srcWalletTypeCode\":\"100008\",\"srcWalletTypeName\":\"Main Wallet\",\"desWalletTypeCode\":\"100008\",\"desWalletTypeName\":\"Main Wallet\",\"desWalletOwnerCode\":\"1000002820\",\"desWalletOwnerName\":\"Oceans Forex\",\"desWalletOwnerNumber\":\"605218330333\",\"amount\":1000.0,\"channelTypeCode\":\"100000\",\"desCurrencyCode\":\"100062\",\"desCurrencyName\":\"GNF\",\"status\":\"Pending\",\"createdBy\":\"102068\",\"creationDate\":\"2021-11-02T09:07:37.950+0530\",\"tax\":50.0,\"fee\":1000.0,\"exchangeRate\":0.0,\"finalAmount\":2050.0,\"srcCurrencySymbol\":\"Fr\",\"desCurrencySymbol\":\"Fr\",\"transactionType\":\"101611\",\"serviceCode\":\"100000\",\"serviceCategoryCode\":\"100016\",\"serviceProviderCode\":\"100109\"},{\"id\":152207,\"code\":\"1000152369\",\"featureCode\":\"100076\",\"desWalletCode\":\"1000028686\",\"srcWalletCode\":\"1000028522\",\"srcWalletOwnerCode\":\"1000002785\",\"srcWalletOwnerName\":\"sharique agent\",\"srcCurrencyCode\":\"100062\",\"srcCurrencyName\":\"GNF\",\"srcWalletTypeCode\":\"100008\",\"srcWalletTypeName\":\"Main Wallet\",\"desWalletTypeCode\":\"100008\",\"desWalletTypeName\":\"Main Wallet\",\"desWalletOwnerCode\":\"1000002817\",\"desWalletOwnerName\":\"Rahul Inst\",\"desWalletOwnerNumber\":\"9910859186\",\"amount\":1000.0,\"channelTypeCode\":\"100000\",\"desCurrencyCode\":\"100062\",\"desCurrencyName\":\"GNF\",\"status\":\"Pending\",\"createdBy\":\"102068\",\"creationDate\":\"2021-11-01T13:36:27.890+0530\",\"tax\":50.0,\"fee\":1000.0,\"exchangeRate\":0.0,\"finalAmount\":2050.0,\"srcCurrencySymbol\":\"Fr\",\"desCurrencySymbol\":\"Fr\",\"transactionType\":\"101611\",\"serviceCode\":\"100000\",\"serviceCategoryCode\":\"100016\",\"serviceProviderCode\":\"100109\"}]}");

                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");


                    arrayList_sellFloat= new ArrayList<>();
                    arrayList_sellFloat.clear();

                    if(resultCode.equalsIgnoreCase("0")) {

                        recyclerView.removeAllViewsInLayout();
                        recyclerView.setVisibility(View.VISIBLE);
                        linearLayout_record.setVisibility(View.VISIBLE);


                        if(jsonObject.has("walletOperationList")) {

                            JSONArray jsonArray_walletOperationList = jsonObject.getJSONArray("walletOperationList");

                            for (int i = 0; i < jsonArray_walletOperationList.length(); i++) {

                                SellFloatModal sellFloatModal = new SellFloatModal();


                                JSONObject jsonObject2 = jsonArray_walletOperationList.getJSONObject(i);



                                if(jsonObject2.has("srcWalletOwnerCode")) {
                                    String  sellFloat_srcWalletOwnerCode = jsonObject2.getString("srcWalletOwnerCode");

                                    sellFloatModal.setSellFloat_srcWalletOwnerCode(sellFloat_srcWalletOwnerCode);

                                }

                                if(jsonObject2.has("srcWalletOwnerName")) {
                                    String  sellFloat_srcWalletOwnerName = jsonObject2.getString("srcWalletOwnerName");

                                    sellFloatModal.setSellFloat_srcWalletOwnerName(sellFloat_srcWalletOwnerName);

                                }

                                if(jsonObject2.has("desWalletOwnerName")) {
                                    String  sellFloat_desWalletOwnerName = jsonObject2.getString("desWalletOwnerName");

                                    sellFloatModal.setSellFloat_desWalletOwnerName(sellFloat_desWalletOwnerName);

                                }


                                if(jsonObject2.has("desWalletOwnerCode")) {
                                    String  sellFloat_desWalletOwnerCode = jsonObject2.getString("desWalletOwnerCode");

                                    sellFloatModal.setSellFloat_desWalletOwnerCode(sellFloat_desWalletOwnerCode);

                                    desWalletOwnerCode_from_allSellFloat = jsonObject2.getString("desWalletOwnerCode");

                                }

                                if(jsonObject2.has("desWalletOwnerNumber")) {
                                    String  sellFloat_desWalletOwnerNumber = jsonObject2.getString("desWalletOwnerNumber");
                                    sellFloatModal.setSellFloat_desWalletOwnerNumber(sellFloat_desWalletOwnerNumber);
                                }

                                if(jsonObject2.has("desCurrencyName")) {
                                    String  sellFloat_desCurrencyName = jsonObject2.getString("desCurrencyName");
                                    sellFloatModal.setSellFloat_desCurrencyName(sellFloat_desCurrencyName);
                                }

                                if(jsonObject2.has("amount")) {
                                    Double  sellFloat_amount = jsonObject2.optDouble("amount");
                                    sellFloatModal.setSellFloat_amount(sellFloat_amount);
                                }

                                if(jsonObject2.has("exchangeRate")) {
                                    Double  sellFloat_aexchangeRate = jsonObject2.optDouble("exchangeRate");
                                    sellFloatModal.setSellFloat_exchangeRate(sellFloat_aexchangeRate);
                                }

                                if(jsonObject2.has("tax")) {
                                    Double  sellFloat_Tax = jsonObject2.optDouble("tax");
                                    sellFloatModal.setSellFloat_tax(sellFloat_Tax);

                                    System.out.println("get tax"+sellFloat_Tax);
                                }

                                if(jsonObject2.has("fee")) {
                                    Double  sellFloat_fee = jsonObject2.optDouble("fee");
                                    sellFloatModal.setSellFloat_fee(sellFloat_fee);
                                }






                                if(jsonObject2.has("finalAmount")) {
                                    Double  sellFloat_finalAmount = jsonObject2.optDouble("finalAmount");
                                    sellFloatModal.setSellFloat_finalAmount(sellFloat_finalAmount);
                                }

                                if(jsonObject2.has("desCurrencyName")) {
                                    String  sellFloat_desCurrencyName = jsonObject2.optString("desCurrencyName");
                                    sellFloatModal.setSellFloat_desCurrencyName(sellFloat_desCurrencyName);
                                }

                                if(jsonObject2.has("status")) {
                                    String  sellFloat_status = jsonObject2.optString("status");
                                    sellFloatModal.setSellFloat_status(sellFloat_status);
                                }


                                if(jsonObject2.has("status")) {
                                    String  sellFloat_status = jsonObject2.optString("status");
                                    sellFloatModal.setSellFloat_status(sellFloat_status);
                                }

                                if(jsonObject2.has("creationDate")) {
                                    String  sellFloat_creationDate = jsonObject2.optString("creationDate");
                                    sellFloatModal.setSellFloat_creationDate(sellFloat_creationDate);
                                }


                                arrayList_sellFloat.add(sellFloatModal);
                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(SellFloat.this));
                            SellFloatAdapterRecycle adpter= new SellFloatAdapterRecycle(SellFloat.this,arrayList_sellFloat,SellFloat.this);
                            recyclerView.setAdapter(adpter);

                        }
                    }

                    else {
                        //Toast.makeText(SellFloat.this, resultDescription, Toast.LENGTH_LONG).show();
                        recyclerView.setVisibility(View.GONE);
                        linearLayout_record.setVisibility(View.GONE);

                    }
                }



                catch (Exception e)
                {
                    Toast.makeText(SellFloat.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(SellFloat.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void api_exchange_rate() {


        API.GET_CASHOUT_DETAILS("ewallet/api/v1/exchangeRate/getAmountDetails?sendCurrencyCode=" + select_currecnyCode +
                "&receiveCurrencyCode="+select_currecnyCode+"&sendCountryCode=" + countryCode_agent + "&receiveCountryCode="+countryCode_subscriber+
                "&currencyValue=" + amountstr + "&channelTypeCode=100002&serviceCode=" + serviceCode_from_serviceCategory + "&serviceCategoryCode=" + serviceCategoryCode_from_serviceCategory + "&serviceProviderCode=" +
                serviceProviderCode_from_serviceCategory + "&walletOwnerCode=" + walletOwnerCode_mssis_agent + "&remitAgentCode=" +
                walletOwnerCode_mssis_agent + "&payAgentCode="+instituteListModelArrayList.get(pos).getWalletOwnerCode(),languageToUse, new Api_Responce_Handler() {


            @Override
            public void success(JSONObject jsonObject) {


                try {

                    //JSONObject jsonObject = new JSONObject("{"transactionId":"1789322","requestTime":"Wed Oct 20 15:53:33 IST 2021","responseTime":"Wed Oct 20 15:53:33 IST 2021","resultCode":"0","resultDescription":"Transaction Successful","walletOwnerCountryCurrencyList":[{"id":7452,"code":"107451","walletOwnerCode":"1000002488","currencyCode":"100062","currencyName":"GNF","currencySymbol":"Fr","countryCurrencyCode":"100076","inBound":true,"outBound":true,"status":"Active"}]}");

                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");

                    if(resultCode.equalsIgnoreCase("0")) {



                        //Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();

                        JSONObject exchangeRate = jsonObject.getJSONObject("exchangeRate");

                        fees_amount = exchangeRate.getString("fee");


                        if(!exchangeRate.has("receiverTax")) {
                            if (exchangeRate.has("taxConfigurationList")) {
                                JSONArray jsonArray = exchangeRate.getJSONArray("taxConfigurationList");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    tax_financial = jsonObject2.getString("value");
                                    tax_financialtypename = MyApplication.getTaxString(jsonObject2.getString("taxTypeName"));

                                }
                            } else {
                                tax_financial = exchangeRate.getString("value");
                            }
                        }
                        taxselffloatLinear.setVisibility(View.VISIBLE);

                      //  rp_tv_convertionrate.setText(MyApplication.addDecimal(currencySymbol_receiver));
                        rp_tv_fees_reveiewPage.setText(currencySymbol_receiver+" "+MyApplication.addDecimal(fees_amount));
                       if(tax_financialtypename.equalsIgnoreCase("VAT")){
                           rp_tv_excise_tax.setText(getString(R.string.Taxvat)+":"  + " "+currencySymbol_receiver+" "+tax_financial);

                       }else if(tax_financialtypename.equalsIgnoreCase("Financial Tax")){
                           rp_tv_excise_tax.setText(getString(R.string.Taxfinancial)+":"  + " "+currencySymbol_receiver+" "+tax_financial);

                       }else{
                           rp_tv_excise_tax.setText(tax_financialtypename+":"  + " "+currencySymbol_receiver+" "+tax_financial);

                       }
                        rp_tv_amount_paid.setText(currencySymbol_receiver+" "+MyApplication.addDecimal(amountstr));

                        tax_financial_double = Double.parseDouble(tax_financial);
                        amountstr_double = Double.parseDouble(amountstr);

                        totalAmount_double = tax_financial_double+amountstr_double+Double.parseDouble(fees_amount);
                        totalAmount_str = String.valueOf(totalAmount_double);
                        rp_tv_amount_to_be_charge.setText(currencySymbol_receiver+" "+ MyApplication.addDecimal(totalAmount_str));
                        rp_tv_comment.setText(et_fp_reason_sending.getText().toString());




                        ll_page_1.setVisibility(View.GONE);
                        ll_reviewPage.setVisibility(View.VISIBLE);
                        ll_receiptPage.setVisibility(View.GONE);

                        MyApplication.hideLoader();



                    }

                    else {
                        MyApplication.hideLoader();

                        Toast.makeText(SellFloat.this, resultDescription, Toast.LENGTH_LONG).show();
                        //  finish();
                    }


                }
                catch (Exception e)
                {
                    MyApplication.hideLoader();

                    Toast.makeText(SellFloat.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(SellFloat.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }




    private void api_serviceProvider() {


        String serviceCategoryCode_hardcode="100016";   // 100016 is hard code according to praveen 19 Nov

        String serviceCode_LoginApi = MyApplication.getSaveString("serviceCode_LoginApi", SellFloat.this);


        //   Toast.makeText(SellFloat.this, "---------------"+serviceCode_LoginApi, Toast.LENGTH_LONG).show();


        API.GET_TRANSFER_DETAILS("ewallet/api/v1/serviceProvider/serviceCategory?serviceCode="+serviceCode_LoginApi+"&serviceCategoryCode="+serviceCategoryCode_hardcode+"&status=Y",languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {


                try {

                    // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1789408\",\"requestTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"responseTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwnerUser\":{\"id\":2171,\"code\":\"101917\",\"firstName\":\"TATASnegal\",\"userName\":\"TATASnegal5597\",\"mobileNumber\":\"8888888882\",\"email\":\"kundan.kumar@esteltelecom.com\",\"walletOwnerUserTypeCode\":\"100000\",\"walletOwnerUserTypeName\":\"Supervisor\",\"walletOwnerCategoryCode\":\"100000\",\"walletOwnerCategoryName\":\"Institute\",\"userCode\":\"1000002606\",\"status\":\"Active\",\"state\":\"Approved\",\"gender\":\"M\",\"idProofTypeCode\":\"100004\",\"idProofTypeName\":\"MILITARY ID CARD\",\"idProofNumber\":\"44444444444\",\"creationDate\":\"2021-10-01T09:04:07.330+0530\",\"notificationName\":\"EMAIL\",\"notificationLanguage\":\"en\",\"createdBy\":\"100308\",\"modificationDate\":\"2021-10-20T14:59:00.791+0530\",\"modifiedBy\":\"100308\",\"addressList\":[{\"id\":3569,\"walletOwnerUserCode\":\"101917\",\"addTypeCode\":\"100001\",\"addTypeName\":\"Commercial\",\"regionCode\":\"100068\",\"regionName\":\"Boke\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"city\":\"100022\",\"cityName\":\"Dubreka\",\"addressLine1\":\"delhi\",\"status\":\"Inactive\",\"creationDate\":\"2021-10-01T09:04:07.498+0530\",\"createdBy\":\"100250\",\"modificationDate\":\"2021-10-03T09:52:57.407+0530\",\"modifiedBy\":\"100308\"}],\"workingDaysList\":[{\"id\":3597,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100000\",\"weekdaysName\":\"Monday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.518+0530\"},{\"id\":3598,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100001\",\"weekdaysName\":\"Tuesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.528+0530\"},{\"id\":3599,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100002\",\"weekdaysName\":\"Wednesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.538+0530\"},{\"id\":3600,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100003\",\"weekdaysName\":\"Thursday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.547+0530\"},{\"id\":3601,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100004\",\"weekdaysName\":\"Friday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.556+0530\"},{\"id\":3602,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100005\",\"weekdaysName\":\"Saturday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.565+0530\"},{\"id\":3603,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100006\",\"weekdaysName\":\"Sunday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"2:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.573+0530\"}],\"macEnabled\":false,\"ipEnabled\":false,\"resetCredReqInit\":false,\"notificationTypeCode\":\"100000\"}}");

                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");

                    if(resultCode.equalsIgnoreCase("0")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("serviceProviderList");

                        for(int i=0;i<jsonArray.length();i++) {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            serviceCode_from_serviceCategory = jsonObject2.getString("serviceCode");
                            serviceCategoryCode_from_serviceCategory = jsonObject2.getString("serviceCategoryCode");
                            serviceProviderCode_from_serviceCategory = jsonObject2.getString("code");

                        }

                        api_walletOwnerUser();

                    }

                    else {
                        MyApplication.hideLoader();

                        Toast.makeText(SellFloat.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
                catch (Exception e)
                {
                    MyApplication.hideLoader();

                    Toast.makeText(SellFloat.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(SellFloat.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void api_walletOwnerUser() {

        String USER_CODE_FROM_TOKEN_AGENTDETAILS = MyApplication.getSaveString("userCode", SellFloat.this);


        API.GET_CASHOUT_DETAILS("ewallet/api/v1/walletOwnerUser/" + USER_CODE_FROM_TOKEN_AGENTDETAILS, languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {


                try {

                    // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1789408\",\"requestTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"responseTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwnerUser\":{\"id\":2171,\"code\":\"101917\",\"firstName\":\"TATASnegal\",\"userName\":\"TATASnegal5597\",\"mobileNumber\":\"8888888882\",\"email\":\"kundan.kumar@esteltelecom.com\",\"walletOwnerUserTypeCode\":\"100000\",\"walletOwnerUserTypeName\":\"Supervisor\",\"walletOwnerCategoryCode\":\"100000\",\"walletOwnerCategoryName\":\"Institute\",\"userCode\":\"1000002606\",\"status\":\"Active\",\"state\":\"Approved\",\"gender\":\"M\",\"idProofTypeCode\":\"100004\",\"idProofTypeName\":\"MILITARY ID CARD\",\"idProofNumber\":\"44444444444\",\"creationDate\":\"2021-10-01T09:04:07.330+0530\",\"notificationName\":\"EMAIL\",\"notificationLanguage\":\"en\",\"createdBy\":\"100308\",\"modificationDate\":\"2021-10-20T14:59:00.791+0530\",\"modifiedBy\":\"100308\",\"addressList\":[{\"id\":3569,\"walletOwnerUserCode\":\"101917\",\"addTypeCode\":\"100001\",\"addTypeName\":\"Commercial\",\"regionCode\":\"100068\",\"regionName\":\"Boke\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"city\":\"100022\",\"cityName\":\"Dubreka\",\"addressLine1\":\"delhi\",\"status\":\"Inactive\",\"creationDate\":\"2021-10-01T09:04:07.498+0530\",\"createdBy\":\"100250\",\"modificationDate\":\"2021-10-03T09:52:57.407+0530\",\"modifiedBy\":\"100308\"}],\"workingDaysList\":[{\"id\":3597,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100000\",\"weekdaysName\":\"Monday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.518+0530\"},{\"id\":3598,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100001\",\"weekdaysName\":\"Tuesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.528+0530\"},{\"id\":3599,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100002\",\"weekdaysName\":\"Wednesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.538+0530\"},{\"id\":3600,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100003\",\"weekdaysName\":\"Thursday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.547+0530\"},{\"id\":3601,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100004\",\"weekdaysName\":\"Friday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.556+0530\"},{\"id\":3602,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100005\",\"weekdaysName\":\"Saturday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.565+0530\"},{\"id\":3603,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100006\",\"weekdaysName\":\"Sunday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"2:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.573+0530\"}],\"macEnabled\":false,\"ipEnabled\":false,\"resetCredReqInit\":false,\"notificationTypeCode\":\"100000\"}}");


                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {

                        JSONObject walletOwnerUser = jsonObject.getJSONObject("walletOwnerUser");

                        //String  issuingCountryName = walletOwnerUser.getString("issuingCountryName");

                        //countryCode_agent = walletOwnerUser.getString("issuingCountryCode");
                        //countryName_agent = walletOwnerUser.getString("issuingCountryName");

                        countryName_agent = MyApplication.getSaveString("COUNTRYNAME_AGENT",SellFloat.this);
                        countryCode_agent = MyApplication.getSaveString("COUNTRYCODE_AGENT",SellFloat.this);

                        rp_tv_agentName.setText(agentName_from_walletOwner);
                        rp_tv_mobileNumber.setText(MyApplication.getSaveString("USERNAME",SellFloat.this));
                        rp_tv_businessType.setText(businessTypeName_walletOwnerCategoryCode);
                        rp_tv_country.setText(countryName_agent);








                        rp_tv_receiverName.setText(mInstitutenameEdittext.getText().toString());




                        rp_tv_receivermobile.setText(mEnterinstituteEdittext.getText().toString());
                        rp_tv_transactionAmount.setText(MyApplication.addDecimal(amountstr));




                        rp_tv_email.setText(email_walletOwnerCategoryCode = walletOwnerUser.getString("email"));

                        MyApplication.hideLoader();




                        api_currency_sender();





                    } else {
                        MyApplication.hideLoader();

                        Toast.makeText(SellFloat.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                } catch (Exception e) {
                    MyApplication.hideLoader();

                    Toast.makeText(SellFloat.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(SellFloat.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }



    boolean validation_mpin_detail()
    {

        mpinStr = et_mpin.getText().toString();

        if(mpinStr.trim().isEmpty()) {
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

    private void mpin_verify() {

        try {


            JSONObject jsonObject = new JSONObject();

            String mobileNumber_login = MyApplication.getSaveString("USERNAME",SellFloat.this);



            String encryptionDatanew = AESEncryption.getAESEncryption(mpinStr);
            jsonObject.put("pin",encryptionDatanew);
            jsonObject.put("mobileNumber",mobileNumber_login);

            API.POST_TRANSFERDETAILS("ewallet/api/v1/walletOwnerUser/verifyMPin/", jsonObject, languageToUse, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {

                        // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1930977\",\"requestTime\":\"Tue Nov 02 15:26:38 IST 2021\",\"responseTime\":\"Tue Nov 02 15:26:38 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\"}");

                        String resultCode = jsonObject.getString("resultCode");
                        String resultDescription = jsonObject.getString("resultDescription");

                        if (resultCode.equalsIgnoreCase("0")) {

                            api_mpin();

                        } else {
                            Toast.makeText(SellFloat.this, resultDescription, Toast.LENGTH_LONG).show();
                            //  finish();
                        }


                    } catch (Exception e) {
                        Toast.makeText(SellFloat.this, e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }


                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(SellFloat.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    }
                    else

                        Toast.makeText(SellFloat.this, aFalse, Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (Exception e)
        {
            Toast.makeText(SellFloat.this,e.toString(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }

    public void createJson(JSONObject jsonObject){


        JSONObject mainObj=new JSONObject();
        JSONArray mainArray=new JSONArray();
        JSONObject dataObj=new JSONObject();

        JSONObject jsonObject_walletOperation = new JSONObject();
        jsonObject_walletOperation=jsonObject.optJSONObject("walletOperation");



        try{

            dataObj.put("actionType","Created");
            dataObj.put("assignTo","");
            dataObj.put("comments","");
            dataObj.put("entityCode",jsonObject_walletOperation.optString("code"));
            dataObj.put("entityName",jsonObject_walletOperation.optString("srcWalletOwnerName"));
            dataObj.put("entityCode",jsonObject_walletOperation.optString("code"));
            dataObj.put("featureCode","100076");
            dataObj.put("status","U");

            JSONObject oneObj=new JSONObject();
            JSONObject twoObj=new JSONObject();
            twoObj.put("code",jsonObject_walletOperation.optString("code"));
            twoObj.put("featureCode","100076");
            twoObj.put("desWalletCode",jsonObject_walletOperation.optString("desWalletCode"));
            twoObj.put("srcWalletCode",jsonObject_walletOperation.optString("srcWalletCode"));
            twoObj.put("srcWalletOwnerCode",jsonObject_walletOperation.optString("srcWalletOwnerCode"));
            twoObj.put("srcWalletOwnerName",jsonObject_walletOperation.optString("srcWalletOwnerName"));
            twoObj.put("srcCurrencyCode",jsonObject_walletOperation.optString("srcCurrencyCode"));
            twoObj.put("srcCurrencyName",jsonObject_walletOperation.optString("srcCurrencyName"));
            twoObj.put("srcWalletTypeCode",jsonObject_walletOperation.optString("srcWalletTypeCode"));
            twoObj.put("srcWalletTypeName",jsonObject_walletOperation.optString("srcWalletTypeName"));
            twoObj.put("desWalletTypeCode",jsonObject_walletOperation.optString("desWalletTypeCode"));
            twoObj.put("desWalletTypeName",jsonObject_walletOperation.optString("desWalletTypeName"));
            twoObj.put("desWalletOwnerCode",jsonObject_walletOperation.optString("desWalletOwnerCode"));
            twoObj.put("desWalletOwnerName",jsonObject_walletOperation.optString("desWalletOwnerName"));
            twoObj.put("desWalletOwnerNumber",jsonObject_walletOperation.optString("desWalletOwnerNumber"));
            twoObj.put("amount",jsonObject_walletOperation.optString("amount"));
            twoObj.put("channelTypeCode",jsonObject_walletOperation.optString("channelTypeCode"));
            twoObj.put("desCurrencyCode",jsonObject_walletOperation.optString("desCurrencyCode"));
            twoObj.put("desCurrencyName",jsonObject_walletOperation.optString("desCurrencyName"));
            twoObj.put("status",jsonObject_walletOperation.optString("status"));
            twoObj.put("createdBy",jsonObject_walletOperation.optString("createdBy"));
            twoObj.put("creationDate",jsonObject_walletOperation.optString("creationDate"));

            twoObj.put("tax",jsonObject_walletOperation.optString("tax"));
            twoObj.put("fee",jsonObject_walletOperation.optString("fee"));
            twoObj.put("finalAmount",jsonObject_walletOperation.optString("finalAmount"));
            twoObj.put("srcCurrencySymbol",jsonObject_walletOperation.optString("srcCurrencySymbol"));
            twoObj.put("desCurrencySymbol",jsonObject_walletOperation.optString("desCurrencySymbol"));
            twoObj.put("transactionType",jsonObject_walletOperation.optString("transactionType"));
            twoObj.put("serviceCode",jsonObject_walletOperation.optString("serviceCode"));
            twoObj.put("serviceCategoryCode",jsonObject_walletOperation.optString("serviceCategoryCode"));
            twoObj.put("serviceProviderCode",jsonObject_walletOperation.optString("serviceProviderCode"));

            dataObj.put("entity",twoObj);
            dataObj.put("updatedInformation",oneObj);
            mainArray.put(dataObj);

            mainObj.put("dataApprovalList",mainArray);

            System.out.println("jsososm============"+mainObj.toString());

            api_approval(mainObj);



        }

        catch (Exception e){

            Toast.makeText(SellFloat.this, e.toString(), Toast.LENGTH_LONG).show();

        }


    }

    private void api_approval(JSONObject mainObj) {

        try {


            API.POST_TRANSFERDETAILS("ewallet/api/v1/dataApproval", mainObj, languageToUse, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {


                        // JSONObject jsonObject = new JSONObject();

                        String resultCode = jsonObject.getString("resultCode");
                        String resultDescription = jsonObject.getString("resultDescription");

                        if (resultCode.equalsIgnoreCase("0")) {

                            ll_page_1.setVisibility(View.GONE);
                            ll_reviewPage.setVisibility(View.GONE);
                            ll_successPage.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    alert_dialogue_sh("Sell Float Added successfully and sent for approval ");


                                }
                            }, 2000);


                        } else {
                            Toast.makeText(SellFloat.this, resultDescription, Toast.LENGTH_LONG).show();
                            //  finish();
                        }


                    } catch (Exception e) {
                        Toast.makeText(SellFloat.this, e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }


                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(SellFloat.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    }
                    else

                        Toast.makeText(SellFloat.this, aFalse, Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (Exception e)
        {
            Toast.makeText(SellFloat.this,e.toString(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }

    private void api_mpin() {

        try {

//
//            {
//                    "transactionType": "101611",
//                    "srcWalletOwnerCode": "1000002623",
//                    "desWalletOwnerCode": "1000002638",
//                    "srcCurrencyCode": "100008",
//                    "desCurrencyCode": "100008",
//                    "value": "1000",
//                    "channelTypeCode": "100000",
//                    "serviceCode": "100000",
//                    "serviceCategoryCode": "100016",
//                    "serviceProviderCode": "100047"
//            }


            JSONObject jsonObject = new JSONObject();

            jsonObject.put("transactionType","101611"); // Hard code
            jsonObject.put("channelTypeCode","100000"); // Hard code


            jsonObject.put("srcWalletOwnerCode",walletOwnerCode_mssis_agent);

            //   jsonObject.put("desWalletOwnerCode",select_insitute_code);   // Not working

            jsonObject.put("desWalletOwnerCode",instituteListModelArrayList.get(pos).getWalletOwnerCode());


            //  Toast.makeText(SellFloat.this, "----srcWalletOwnerCode--------------"+walletOwnerCode_mssis_agent, Toast.LENGTH_LONG).show();
            //  Toast.makeText(SellFloat.this, "---------desWalletOwnerCode---------"+desWalletOwnerCode_from_allSellFloat, Toast.LENGTH_LONG).show();


            jsonObject.put("srcCurrencyCode",select_currecnyCode);
            jsonObject.put("desCurrencyCode",select_currecnyCode);


            jsonObject.put("value",amountstr);


//                    jsonObject.put("serviceCode",serviceCategoryCode_from_serviceCategory);          // Change according to Portal 19 Nov
//                    jsonObject.put("serviceCategoryCode",serviceCategoryCode_from_serviceCategory); // Change according to Portal 19 Nov
//                    jsonObject.put("serviceProviderCode",serviceProviderCode_from_serviceCategory); // Change according to Portal 19 Nov

            jsonObject.put("serviceCode",serviceCode_from_serviceCategory);
            jsonObject.put("serviceCategoryCode",serviceCategoryCode_from_serviceCategory);
            jsonObject.put("serviceProviderCode",serviceProviderCode_from_serviceCategory);
            String requestNo=AESEncryption.getAESEncryption(jsonObject.toString());
            JSONObject jsonObjectA=null;
            try{
                jsonObjectA=new JSONObject();
                jsonObjectA.put("request",requestNo);
            }catch (Exception e){

            }

            API.POST_TRANSFERDETAILS("ewallet/api/v1/walletTransfer/sellFloat/", jsonObjectA, languageToUse, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {


                        //  JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1930978\",\"requestTime\":\"Tue Nov 02 15:27:01 IST 2021\",\"responseTime\":\"Tue Nov 02 15:27:01 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOperation\":{\"code\":\"1000152390\",\"featureCode\":\"100076\",\"desWalletCode\":\"1000028686\",\"srcWalletCode\":\"1000028522\",\"srcWalletOwnerCode\":\"1000002785\",\"srcWalletOwnerName\":\"sharique agent\",\"srcCurrencyCode\":\"100062\",\"srcCurrencyName\":\"GNF\",\"srcWalletTypeCode\":\"100008\",\"srcWalletTypeName\":\"Main Wallet\",\"desWalletTypeCode\":\"100008\",\"desWalletTypeName\":\"Main Wallet\",\"desWalletOwnerCode\":\"1000002817\",\"desWalletOwnerName\":\"Rahul Inst\",\"desWalletOwnerNumber\":\"9910859186\",\"amount\":1500,\"channelTypeCode\":\"100000\",\"desCurrencyCode\":\"100062\",\"desCurrencyName\":\"GNF\",\"status\":\"Pending\",\"createdBy\":\"102068\",\"creationDate\":\"2021-11-02T15:27:01.364+0530\",\"tax\":0,\"fee\":0,\"finalAmount\":1500,\"srcCurrencySymbol\":\"Fr\",\"desCurrencySymbol\":\"Fr\",\"transactionType\":\"101611\",\"serviceCode\":\"100020\",\"serviceCategoryCode\":\"ALL011\",\"serviceProviderCode\":\"100109\"}}");

                        String resultCode = jsonObject.getString("resultCode");
                        String resultDescription = jsonObject.getString("resultDescription");

                        if (resultCode.equalsIgnoreCase("0")) {



//
//                        {"transactionId":"1788378","requestTime":"Fri Nov 19 14:39:32 IST 2021",
//                                "responseTime":"Fri Nov 19 14:39:32 IST 2021","resultCode":"0","resultDescription":"Transaction Successful",
//                                "walletOperation":{"code":"1000152285","featureCode":"100076","desWalletCode":"1000026588",
//                                "srcWalletCode":"1000026741","srcWalletOwnerCode":"1000002692","srcWalletOwnerName":"sharique agent",
//                                "srcCurrencyCode":"100062","srcCurrencyName":"GNF","srcWalletTypeCode":"100008","srcWalletTypeName":
//                            "Main Wallet","desWalletTypeCode":"100008","desWalletTypeName":"Main Wallet","desWalletOwnerCode":"1000002689",
//                                    "desWalletOwnerName":"sharique agent","desWalletOwnerNumber":"9990063618","amount":1500,"channelTypeCode":"100000",
//                                    "desCurrencyCode":"100062","desCurrencyName":"GNF","status":"Pending","createdBy":"101979","creationDate":
//                            "2021-11-19T14:39:32.587+0530","tax":1.3,"fee":10,"finalAmount":1511.3,"srcCurrencySymbol":"Fr","desCurrencySymbol":
//                            "Fr","transactionType":"101611","serviceCode":"100020",
//                                "serviceCategoryCode":"ALL011","serviceProviderCode":"100109"}}




                            createJson(jsonObject);



                        } else {
                            Toast.makeText(SellFloat.this, resultDescription, Toast.LENGTH_LONG).show();
                            //  finish();
                        }


                    } catch (Exception e) {
                        Toast.makeText(SellFloat.this, e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }


                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(SellFloat.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    }
                    else

                        Toast.makeText(SellFloat.this, aFalse, Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (Exception e)
        {
            Toast.makeText(SellFloat.this,e.toString(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_nextClick: {


                if (validation_mobile_Details()) {


                    if (new InternetCheck().isConnected(SellFloat.this)) {

                        MyApplication.showloader(SellFloat.this, getString(R.string.please_wait));



                        api_serviceProvider();



                    } else {
                        Toast.makeText(SellFloat.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                    }
                }
            }

            break;

            case R.id.tvContinue: {



                alert_dialogue_sh("Sell Float Added successfully and sent for approval ");


            }


            break;

            case R.id.confirm_reviewClick_textview: {

                {

                    if(pinLinearselffloat.getVisibility()==View.VISIBLE){
                        if (validation_mpin_detail()) {

                                if (new InternetCheck().isConnected(SellFloat.this)) {

                                MyApplication.showloader(SellFloat.this, getString(R.string.please_wait));

                                mpin_verify();

                            } else {
                                Toast.makeText(SellFloat.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                            }


                        }
                    }else {
                        MyApplication.biometricAuth(SellFloat.this, new BioMetric_Responce_Handler() {
                            @Override
                            public void success(String success) {
                                try {

                                    //  String encryptionDatanew = AESEncryption.getAESEncryption(MyApplication.getSaveString("pin",MyApplication.appInstance).toString().trim());
                                    mpinStr = MyApplication.getSaveString("pin", MyApplication.appInstance);

                                    if (new InternetCheck().isConnected(SellFloat.this)) {

                                        MyApplication.showloader(SellFloat.this, getString(R.string.please_wait));

                                        mpin_verify();


                                    } else {
                                        Toast.makeText(SellFloat.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                            @Override
                            public void failure(String failure) {

                                MyApplication.showToast(SellFloat.this, failure);

                                pinLinearselffloat.setVisibility(View.VISIBLE);


                            }
                        });
                    }


                }




              /*  if (validation_mpin_detail()) {

                    if (new InternetCheck().isConnected(TransferFloats.this)) {

                        MyApplication.showloader(TransferFloats.this, getString(R.string.please_wait));

                        mpin_verify();

                    } else {
                        Toast.makeText(TransferFloats.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                    }
                }
*/
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


                qrScan();


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

    public void qrScan(){

        Intent i = new Intent(SellFloat.this, QrCodeActivity.class);
        startActivityForResult( i,REQUEST_CODE_QR_SCAN);
    }

    public static final int REQUEST_CODE = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {


            String requiredValue = data.getStringExtra("PHONE");

            MyApplication.contactValidation(requiredValue,mEnterinstituteEdittext);

            int position = arrayList_instititueCode.indexOf(requiredValue);

            if (position == -1) {
                System.out.println("get position"+position);

                Toast.makeText(SellFloat.this, "Institute not found!", Toast.LENGTH_SHORT).show();

            } else {
                setSelction(position);
            }



        }
        if (resultCode != Activity.RESULT_OK) {
            Log.d("LOGTAG", "COULD NOT GET A GOOD RESULT.");
            if (data == null)
                   return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (result != null) {
                androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(SellFloat.this).create();
                alertDialog.setTitle(getString(R.string.scan_error));
                alertDialog.setMessage(getString(R.string.val_scan_error_content));
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;

        }


        if (requestCode == REQUEST_CODE_QR_SCAN) {

            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.d("LOGTAG", "Have scan result in your app activity :" + result);

            String[] date=result.split(":");

            callwalletOwnerDetailsQR(date[0]);



        }
    }

    public void callwalletOwnerDetailsQR(String Code){

        API.GET("ewallet/api/v1/walletOwner/"+Code, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){

                    try {

                        // walletOwner

                        if (jsonObject.has("walletOwner")) {

                            JSONObject jsonObject_walletOwner = jsonObject.getJSONObject("walletOwner");

                            if (jsonObject_walletOwner.has("mobileNumber")) {

                               String select_insitute_code_temp = jsonObject_walletOwner.getString("mobileNumber");

                                 if(!select_insitute_code_temp.equalsIgnoreCase(arrayList_instititueCode.get(pos))){
                                     Toast.makeText(SellFloat.this, "Wallet owner not found", Toast.LENGTH_SHORT).show();
                                     mEnterinstituteEdittext.setText(select_insitute_code_temp);
                                     mEnterinstituteEdittext.setEnabled(false);

                                 }else{
                                     mEnterinstituteEdittext.setText(select_insitute_code_temp);
                                     mEnterinstituteEdittext.setEnabled(false);

                                 }




                                    for (int i = 0; i < arrayList_instititueCode.size(); i++) {
                                        if (select_insitute_code_temp.equalsIgnoreCase(arrayList_instititueCode.get(i)))
                                        {

                                           setSelction(i);
                                        }
                                    }

                            }
                            else{
                                MyApplication.showToast(SellFloat.this,"Institute not found!");

                            }

                        }
                    }

                    catch (Exception e)
                    {

                    }




                    //  callwalletOwnerCountryCurrency();
                }else{

                    MyApplication.showToast(SellFloat.this,jsonObject.optString("resultDescription"));


                }

            }

            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(SellFloat.this,aFalse);
            }
        });
    }





    @Override
    public void onBackPressed() {



        if(ll_reviewPage.getVisibility()==View.VISIBLE){
            ll_page_1.setVisibility(View.VISIBLE);
            ll_reviewPage.setVisibility(View.GONE);
            ll_successPage.setVisibility(View.GONE);
            ll_receiptPage.setVisibility(View.GONE);
            return;
        }

        super.onBackPressed();



    }


    private static final int REQUEST_CODE_QR_SCAN = 101;

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


    int pos;



    public void setSelction(int pos){
       // spinner_insititue.setText(arrayList_instititueName.get(pos));
        select_insitute_name = arrayList_instititueName.get(pos);
        select_insitute_code = arrayList_instititueCode.get(pos);

            api_currency_spinner_details(instituteListModelArrayList.get(pos).getWalletOwnerCode());
        this.pos = pos;
    }

    public void setSelctionCurrency(int pos){
        tvAmtCurr.setText(arrayList_currencySymbol.get(pos));
        select_currecnyName = arrayList_currecnyName.get(pos);
        select_currecnyCode = arrayList_currecnyCode.get(pos);
        currencySymbol_receiver = arrayList_currencySymbol.get(pos);

        spinner_currency.setText(arrayList_currecnyName.get(pos));
        edittext_amount.requestFocus();

    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {


            case R.id.spinner_insititue:
            {
            }
            break;


            case R.id.spinner_record:
            {

                recordString =strArray[i];

                if(recordString.equalsIgnoreCase("10"))
                {

                    MyApplication.showloader(SellFloat.this, getString(R.string.please_wait));


                    api_allSellFloat_featureCode(recordString);
                }

                else if(recordString.equalsIgnoreCase("25"))
                {
                    MyApplication.showloader(SellFloat.this, getString(R.string.please_wait));

                    api_allSellFloat_featureCode(recordString);
                }

                else if(recordString.equalsIgnoreCase("50"))
                {
                    MyApplication.showloader(SellFloat.this, getString(R.string.please_wait));

                    api_allSellFloat_featureCode(recordString);
                }


                else if(recordString.equalsIgnoreCase("100"))
                {
                    MyApplication.showloader(SellFloat.this, getString(R.string.please_wait));


                    api_allSellFloat_featureCode(recordString);
                }


            }
            break;


            case R.id.spinner_currency:
            {


            }

            break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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


    public void alertdialouge_recycleiView(String transactionDetails) {

        try {


            Dialog operationDialog = new Dialog(SellFloat.this);
            operationDialog.setContentView(R.layout.alert_dialogue_sellfloat);

            Button closeButton;
            TextView senderWalletOwnerCode_textview, receiverWalletOwnerCode_textview, senderName_textview, receiverName_textview, currencyName_textview, amount_textview, fee_textview, tax_textview, exchangeRate_textview, receiver_msisdn_textview,final_amount_textview, create_on_textview, status_textview;
            closeButton = operationDialog.findViewById(R.id.closeButton);

            String[] strArray = transactionDetails.split("\\|");
            System.out.println("get value"+transactionDetails);
            System.out.println(strArray);


            senderWalletOwnerCode_textview = operationDialog.findViewById(R.id.senderWalletOwnerCode_textview);
            receiverWalletOwnerCode_textview = operationDialog.findViewById(R.id.receiverWalletOwnerCode_textview);
            senderName_textview = operationDialog.findViewById(R.id.senderName_textview);
            receiverName_textview = operationDialog.findViewById(R.id.receiverName_textview);
            currencyName_textview = operationDialog.findViewById(R.id.currencyName_textview);
            amount_textview = operationDialog.findViewById(R.id.amount_textview);
            fee_textview = operationDialog.findViewById(R.id.fee_textview);
            tax_textview = operationDialog.findViewById(R.id.tax_textview);
            exchangeRate_textview = operationDialog.findViewById(R.id.exchangeRate_textview);
            final_amount_textview = operationDialog.findViewById(R.id.final_amount_textview);
            create_on_textview = operationDialog.findViewById(R.id.create_on_textview);
            status_textview = operationDialog.findViewById(R.id.status_textview);
            receiver_msisdn_textview=operationDialog.findViewById(R.id.receiver_msisdn_textview);

            senderWalletOwnerCode_textview.setText(strArray[1]);
            receiverWalletOwnerCode_textview.setText(strArray[2]);
            senderName_textview.setText(strArray[3]);
            receiverName_textview.setText(strArray[4]);
            currencyName_textview.setText(strArray[5]);
            amount_textview.setText(MyApplication.addDecimal(strArray[6]));
            fee_textview.setText(MyApplication.addDecimal(strArray[7]));
            tax_textview.setText(MyApplication.addDecimal(strArray[8]));
            exchangeRate_textview.setText(MyApplication.addDecimal(strArray[9]));
            final_amount_textview.setText(MyApplication.addDecimalthree(strArray[10]));

            System.out.println("get value"+strArray[8]);

            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                Date date = null;
                date = inputFormat.parse(strArray[11]);
                String formattedDate = outputFormat.format(date);
                create_on_textview.setText(formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }



            status_textview.setText(strArray[12]);
            receiver_msisdn_textview.setText(strArray[13]);



            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    operationDialog.dismiss();
                }
            });
            //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            operationDialog.show();
        } catch (Exception e) {

            Toast.makeText(SellFloat.this, e.toString(), Toast.LENGTH_LONG).show();

        }

    }



    @Override
    public void callBackSellFloat (String transactionDetails){

        alertdialouge_recycleiView(transactionDetails);
    }


    private boolean isFormatting;
    private int prevCommaAmount;
    private void formatInput(EditText editText,CharSequence s, int start, int count) {

        if(MyApplication.checkMinMax(SellFloat.this,s,editText
                ,MyApplication.ToSelfFloatMinAmount,MyApplication.ToSelfFloatMaxAmount)){
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
