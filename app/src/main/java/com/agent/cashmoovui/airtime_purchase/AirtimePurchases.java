package com.agent.cashmoovui.airtime_purchase;

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
import android.os.SystemClock;
import android.text.Editable;
import android.text.InputFilter;
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
import androidx.biometric.BiometricManager;

import com.agent.cashmoovui.AddContact;
import com.agent.cashmoovui.HiddenPassTransformationMethod;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.activity.OtherOption;
import com.agent.cashmoovui.adapter.CommonBaseAdapterSecond;
import com.agent.cashmoovui.adapter.CustomeBaseAdapterAllCountry;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.apiCalls.BioMetric_Responce_Handler;
import com.agent.cashmoovui.cash_in.CashIn;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.login.LoginPin;
import com.agent.cashmoovui.otp.VerifyLoginAccountScreen;
import com.agent.cashmoovui.set_pin.AESEncryption;
import com.aldoapps.autoformatedittext.AutoFormatUtil;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

public class AirtimePurchases extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    String currencySymbol_sender="";
    String operatorName_from_operatorList="",serviceProviderCode_from_operatorList="",serviceCategoryCode_from_operatorList="",sPName_from_operatorList="",sPCode_from_operatorList="";

    String operator_code_from_operatorList="";
    String productCode_code_fromapi="";

    ImageView imgBack,imgHome;
    public static LoginPin loginpinC;

    String selectOperatorList="";
    boolean  isPasswordVisible;
    String currencyName_subscriber="",currencyCode_subscriber="";

    private long mLastClickTime = 0;


    String selectCountryName= "";
    String selectCountryCode= "",code_from_product_allByCriteria="";

    String currencyName_from_currency="",walletOwnerCode_from_currency="";
    String countryCurrencyCode_from_currency="";
    String mobileNoStr="";

    String receiver_name_str="",receiver_emailId_str="",receiver_country_str="",sender_emailId_str="",sender_country_str="";

    ArrayList<String> arrayList_senderCountryName;
    ArrayList<String> arrayList_senderCountryCode;


    ArrayList<String> arrayList_Name_PRODUCTTYPE;
    ArrayList<String> arrayList_Code_PRODUCTTYPE;
    ArrayList<String> arrayList_status_PRODUCTTYPE;


    String  serviceCode_from_allSellFloat ="",serviceCategoryCode_from_allSellFloat="",serviceProviderCode_from_allSellFloat="",srcCurrencyCode_from_allSellFloat="",desCurrencyCode_from_allSellFloat="",srcWalletOwnerCode_from_allSellFloat="",desWalletOwnerCode_from_allSellFloat="";


    Spinner spinner_operator,spinner_senderCountry;
    View rootView;

    EditText etPin;
    TextView tvAmtCurr,tvContinue,receiptPage_tv_mobileNumber,operator_tv_receipt,vendorTransId_tv_receiptPagen,vendorTransId_tv_receiptPage,receiptPage_tv_sender_emailId,receiptPage_tv_sender_country,receiptPage_tv_receiver_emailId,receiptPage_tv_receiver_country,rp_tv_convertionrate,exportReceipt_textview,tv_nextClick,rp_tv_agentName,rp_tv_mobileNumber,rp_tv_businessType,rp_tv_email,rp_tv_country,rp_tv_operator,rp_tv_totalAmount
            ,rp_tv_fees_reveiewPage,receiptPage_tv_stransactionType, receiptPage_tv_dateOfTransaction, receiptPage_tv_transactionAmount,
            receiptPage_tv_amount, receiptPage_tv_fee, receiptPage_tv_financialtax, receiptPage_tv_transaction_receiptNo,receiptPage_tv_sender_name,
            receiptPage_tv_sender_phoneNo,
            receiptPage_tv_receiver_name, receiptPage_tv_receiver_phoneNo, close_receiptPage_textview,rp_tv_excise_tax,rp_tv_amount_to_be_charge,rp_tv_transactionAmount,previous_reviewClick_textview,confirm_reviewClick_textview;
    LinearLayout ll_page_1,ll_reviewPage,ll_receiptPage,main_layout,ll_successPage,pinLinear;

    MyApplication applicationComponentClass;
    String languageToUse = "";

    EditText edittext_amount,et_mpin,edittext_mobileNo;


    String amountstr="",agentName_from_walletOwner="", businessTypeName_walletOwnerCategoryCode="",email_walletOwnerCategoryCode="";

    String walletOwnerCode_mssis_agent="",walletOwnerCode_subs, senderNameAgent="";

    String  currencyCode_agent="",countryCode_agent="",currencyName_agent="",countryName_agent="";

    String tax_financial="0.00",fees_amount,totalAmount_str,receivernameStr="";
    Double tax_financial_double=0.0,amountstr_double=0.0,fees_amount_double=0.0,totalAmount_double=0.0;

    String mpinStr="";


    String  serviceCode_from_serviceCategory="",serviceCategoryCode_from_serviceCategory="",serviceProviderCode_from_serviceCategory;


    ArrayList<String> arrayList_instititueName;
    ArrayList<String> arrayList_instititueCode;

    String selectInstititueName="";
    String selectInstititueCode="";
    String serviceCode_from_operatorList="";


    ArrayList<String> arrayList_serviceCategoryCode = new ArrayList<String>();
    ArrayList<String> arrayList_ServiceCode = new ArrayList<String>();
    ArrayList<String> arrayList_serviceProviderCode = new ArrayList<String>();
    ArrayList<String> arrayList_serviceProviderName = new ArrayList<String>();
    ArrayList<String> arrayList_OperatorListName = new ArrayList<String>();
    ArrayList<String> arrayList_OperatorListCode = new ArrayList<String>();




    public static final int REQUEST_CODE = 1;




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


            setContentView(R.layout.airtime_purchases);
            setBackMenu();


            rootView = getWindow().getDecorView().findViewById(R.id.main_layout);



            //     First page

            ll_page_1 = (LinearLayout) findViewById(R.id.ll_page_1);

            tv_nextClick = (TextView) findViewById(R.id.tv_nextClick);
            tvAmtCurr = findViewById(R.id.tvAmtCurr);
            edittext_amount = (EditText) findViewById(R.id.edittext_amount);
            edittext_mobileNo = (EditText) findViewById(R.id.edittext_mobileNo);
            pinLinear=findViewById(R.id.pinLinear);


           String mobilelength=MyApplication.getSaveString("MobileLength",MyApplication.appInstance);

            edittext_mobileNo.setFilters(new InputFilter[] {
                    new InputFilter.LengthFilter(Integer.parseInt(mobilelength))});

            edittext_amount.setFilters(new InputFilter[] {
                    new InputFilter.LengthFilter(MyApplication.amountLength)});
            edittext_mobileNo.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;

                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        if(event.getRawX() >= (edittext_mobileNo.getRight() - edittext_mobileNo.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here


                            Intent intent = new Intent(AirtimePurchases.this,
                                    AddContact.class);
                            startActivityForResult(intent , REQUEST_CODE);

                            return true;
                        }
                    }
                    return false;
                }
            });

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
                        formatInput(edittext_amount,s, s.length(), s.length());

                    }

                    isFormatting = false;



                }
            });


            //    Reveiw page


            ll_reviewPage = (LinearLayout) findViewById(R.id.ll_reviewPage);

            spinner_senderCountry = (Spinner) findViewById(R.id.spinner_senderCountry);
            spinner_senderCountry.setOnItemSelectedListener(this);


            rp_tv_agentName = (TextView) findViewById(R.id.rp_tv_agentName);
            rp_tv_mobileNumber = (TextView) findViewById(R.id.rp_tv_mobileNumber);
            rp_tv_businessType = (TextView) findViewById(R.id.rp_tv_businessType);
            rp_tv_email = (TextView) findViewById(R.id.rp_tv_email);
            rp_tv_country = (TextView) findViewById(R.id.rp_tv_country);
            rp_tv_operator = (TextView) findViewById(R.id.rp_tv_operator);
            rp_tv_totalAmount = (TextView) findViewById(R.id.rp_tv_totalAmount);
            rp_tv_fees_reveiewPage = (TextView) findViewById(R.id.rp_tv_fees_reveiewPage);
            rp_tv_excise_tax = (TextView) findViewById(R.id.rp_tv_excise_tax);
            rp_tv_amount_to_be_charge = (TextView) findViewById(R.id.rp_tv_amount_to_be_charge);
            rp_tv_transactionAmount = (TextView) findViewById(R.id.rp_tv_transactionAmount);


            et_mpin = (EditText) findViewById(R.id.et_mpin);
            previous_reviewClick_textview = (TextView) findViewById(R.id.previous_reviewClick_textview);
            confirm_reviewClick_textview = (TextView) findViewById(R.id.confirm_reviewClick_textview);

            TextView tvFinger =findViewById(R.id.tvFinger);
            if(MyApplication.setProtection!=null && !MyApplication.setProtection.isEmpty()) {
                if (MyApplication.setProtection.equalsIgnoreCase("Activate")) {
                   // tvFinger.setVisibility(View.VISIBLE);
                } else {
                   // tvFinger.setVisibility(View.GONE);
                }
            }else{
               // tvFinger.setVisibility(View.VISIBLE);
            }
            tvFinger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.biometricAuth(AirtimePurchases.this, new BioMetric_Responce_Handler() {
                        @Override
                        public void success(String success) {
                            try {

                              //  String encryptionDatanew = AESEncryption.getAESEncryption(MyApplication.getSaveString("pin",MyApplication.appInstance).toString().trim());
                                mpinStr=MyApplication.getSaveString("pin",MyApplication.appInstance);

                                if (new InternetCheck().isConnected(AirtimePurchases.this)) {

                                    MyApplication.showloader(AirtimePurchases.this, getString(R.string.please_wait));

                                    api_allByCriteria_msisdnPrefix();

                                } else {
                                    Toast.makeText(AirtimePurchases.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failure(String failure) {
                            MyApplication.showToast(AirtimePurchases.this,failure);
                        }
                    });
                }
            });



            //    Receipt page

            ll_receiptPage = (LinearLayout) findViewById(R.id.ll_receiptPage);
            main_layout = (LinearLayout) findViewById(R.id.main_layout);
            exportReceipt_textview = (TextView) findViewById(R.id.exportReceipt_textview);
            rp_tv_convertionrate = (TextView) findViewById(R.id.rp_tv_convertionrate);
            exportReceipt_textview.setOnClickListener(this);


            ll_successPage = (LinearLayout) findViewById(R.id.ll_successPage);
            tvContinue = (TextView) findViewById(R.id.tvContinue);
            tvContinue.setOnClickListener(this);

            receiptPage_tv_transaction_receiptNo = (TextView) findViewById(R.id.receiptPage_tv_transaction_receiptNo);
            receiptPage_tv_stransactionType = (TextView) findViewById(R.id.receiptPage_tv_stransactionType);
            receiptPage_tv_dateOfTransaction = (TextView) findViewById(R.id.receiptPage_tv_dateOfTransaction);
            receiptPage_tv_transactionAmount = (TextView) findViewById(R.id.receiptPage_tv_transactionAmount);
            receiptPage_tv_amount = (TextView) findViewById(R.id.receiptPage_tv_amount);
            receiptPage_tv_fee = (TextView) findViewById(R.id.receiptPage_tv_fee);
            receiptPage_tv_financialtax = (TextView) findViewById(R.id.receiptPage_tv_financialtax);
            receiptPage_tv_sender_name = (TextView) findViewById(R.id.receiptPage_tv_sender_name);
            receiptPage_tv_sender_phoneNo = (TextView) findViewById(R.id.receiptPage_tv_sender_phoneNo);
          //  receiptPage_tv_receiver_name = (TextView) findViewById(R.id.receiptPage_tv_receiver_name);
            receiptPage_tv_receiver_phoneNo = (TextView) findViewById(R.id.receiptPage_tv_receiver_phoneNo);
         //   receiptPage_tv_receiver_emailId  = (TextView) findViewById(R.id.receiptPage_tv_receiver_emailId);

        //    receiptPage_tv_sender_emailId  = (TextView) findViewById(R.id.receiptPage_tv_sender_emailId);
           receiptPage_tv_mobileNumber  = (TextView) findViewById(R.id.receiptPage_tv_mobileNumber);
            vendorTransId_tv_receiptPage  = (TextView) findViewById(R.id.vendorTransId_tv_receiptPage);
            vendorTransId_tv_receiptPagen= (TextView) findViewById(R.id.vendorTransId_tv_receiptPagen);
            receiptPage_tv_sender_country  = (TextView) findViewById(R.id.receiptPage_tv_sender_country);
            operator_tv_receipt  = (TextView) findViewById(R.id.operator_tv_receipt);

            receiptPage_tv_receiver_country = (TextView) findViewById(R.id.receiptPage_tv_receiver_country);
            close_receiptPage_textview = (TextView) findViewById(R.id.close_receiptPage_textview);

            tv_nextClick.setOnClickListener(this);
            previous_reviewClick_textview.setOnClickListener(this);
            confirm_reviewClick_textview.setOnClickListener(this);
            close_receiptPage_textview.setOnClickListener(this);

            walletOwnerCode_mssis_agent = MyApplication.getSaveString("walletOwnerCode", AirtimePurchases.this);



            spinner_operator= (Spinner) findViewById(R.id.spinner_operator);
            spinner_operator.setOnItemSelectedListener(this);
            spinner_operator.setEnabled(false);

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
                        MyApplication.hideKeyboard(AirtimePurchases.this);            }
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

            edittext_mobileNo.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    edittext_amount.setText("");
                    spinner_operator.setSelection(0);

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length()>=9) {
                        callApiMsisdnPrefix(s.toString());
                    }else{


                    }


                }

            });



            if (new InternetCheck().isConnected(AirtimePurchases.this)) {

               // MyApplication.showloader(AirtimePurchases.this, getString(R.string.getting_user_info));

                api_serviceCategoryCode_operatorList();


            } else {
                Toast.makeText(AirtimePurchases.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
            }


        }
        catch (Exception e)
        {
            Toast.makeText(AirtimePurchases.this, e.toString(), Toast.LENGTH_LONG).show();

        }

    }


    private void callApiMsisdnPrefix(String s) {

        String firstTwodigits = s.substring(0,2);
        API.GET_TRANSFER_DETAILS("ewallet/api/v1/operator/allByCriteria?msisdnPrefix=224"+firstTwodigits+"&status=Y",languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {



                MyApplication.hideLoader();

                try {


                    // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1947422\",\"requestTime\":\"Thu Nov 04 13:37:44 IST 2021\",\"responseTime\":\"Thu Nov 04 13:37:44 IST 2021\",\"resultCode\":\"1085\",\"resultDescription\":\"Operator Not Found\"}\n");


                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {


                        JSONArray jsonArray = jsonObject.optJSONArray("operatorList");
                        for(int i = 0;i<arrayList_OperatorListCode.size();i++){
                           // String  operatorCode = jsonObject2.getString("code");
                            if(arrayList_OperatorListCode.get(i).equalsIgnoreCase(jsonArray.optJSONObject(0).optString("code"))){
                                spinner_operator.setSelection(i);
                                edittext_amount.requestFocus();
                            }
                        }
                        // MyApplication.showloader(AirtimePurchases.this, getString(R.string.getting_user_info));


                        //mpin_final_api();

                    } else {
                        Toast.makeText(AirtimePurchases.this, resultDescription, Toast.LENGTH_LONG).show();
//
//                        if(resultDescription.equalsIgnoreCase("Operator Not Found"))
//                        {
//                            // Toast.makeText(AirtimePurchases.this, resultDescription, Toast.LENGTH_LONG).show();
//                            MyApplication.showloader(AirtimePurchases.this, getString(R.string.getting_user_info));
//
//                            // mpin_final_api();
//                        }
//                        else
//                        {
//                            Toast.makeText(AirtimePurchases.this, resultDescription, Toast.LENGTH_LONG).show();
//
//                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(AirtimePurchases.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(AirtimePurchases.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


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
                MyApplication.hideKeyboard(AirtimePurchases.this);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(AirtimePurchases.this);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    boolean validation_mobile_Details() {



        amountstr = edittext_amount.getText().toString().trim().replace(",","");
        mobileNoStr = edittext_mobileNo.getText().toString().trim();


        if(mobileNoStr.isEmpty()) {


            Toast.makeText(AirtimePurchases.this, getString(R.string.please_enter_mobileno), Toast.LENGTH_LONG).show();


            return false;
        }

        else if(mobileNoStr.length() < 9) {

            Toast.makeText(AirtimePurchases.this, getString(R.string.enter_phone_no_val), Toast.LENGTH_LONG).show();


            return false;
        }

        else if (spinner_operator.getSelectedItemPosition()==0) {

            Toast.makeText(AirtimePurchases.this, getString(R.string.select_operator), Toast.LENGTH_LONG).show();



            return false;
        }

//        else if (spinner_senderCountry.getSelectedItemPosition()==0) {
//
//            Toast.makeText(AirtimePurchases.this, getString(R.string.val_select_country), Toast.LENGTH_LONG).show();
//
//
//            return false;
//        }



        else if (amountstr.isEmpty()) {

            Toast.makeText(AirtimePurchases.this, getString(R.string.plz_enter_amount), Toast.LENGTH_LONG).show();


            return false;
        }

        else  if(Double.parseDouble(edittext_amount.getText().toString().trim().replace(",",""))<MyApplication.ToCreditPurchaseMinAmount) {
            MyApplication.showErrorToast(AirtimePurchases.this,getString(R.string.val_amount_min)+" "+MyApplication.ToCreditPurchaseMinAmount);
            return false;
        }

        else   if(Double.parseDouble(edittext_amount.getText().toString().trim().replace(",",""))>MyApplication.ToCreditPurchaseMaxAmount) {
            MyApplication.showErrorToast(AirtimePurchases.this,getString(R.string.val_amount_max)+" "+MyApplication.ToCreditPurchaseMaxAmount);
            return false;


        }

        return true;
    }








    private void api_serviceCategoryCode_operatorList() {


        API.GET_TRANSFER_DETAILS("ewallet/api/v1/operator/allByCriteria?serviceCode=100009&serviceCategoryCode=100021&status=Y&offset=0&limit=200",languageToUse,new Api_Responce_Handler() {


            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {


                   // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1947419\",\"requestTime\":\"Thu Nov 04 13:37:44 IST 2021\",\"responseTime\":\"Thu Nov 04 13:37:44 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"operatorList\":[{\"id\":53,\"code\":\"100051\",\"serviceCode\":\"100009\",\"serviceName\":\"Airtime Purchase\",\"serviceCategoryCode\":\"100021\",\"serviceCategoryName\":\"Mobile Prepaid\",\"serviceProviderCode\":\"100136\",\"serviceProviderName\":\"Noble\",\"name\":\"MTN\",\"status\":\"Active\",\"state\":\"Approved\",\"creationDate\":\"1629205304212\",\"modificationDate\":\"1629208381374\",\"fileName\":\"MTN.png\",\"msisdnsPrefix\":[\"2246624\"]},{\"id\":52,\"code\":\"100050\",\"serviceCode\":\"100009\",\"serviceName\":\"Airtime Purchase\",\"serviceCategoryCode\":\"100021\",\"serviceCategoryName\":\"Mobile Prepaid\",\"serviceProviderCode\":\"100136\",\"serviceProviderName\":\"Noble\",\"name\":\"CELLCOM\",\"status\":\"Active\",\"state\":\"Approved\",\"creationDate\":\"1629205252008\",\"modificationDate\":\"1629361031532\",\"msisdnsPrefix\":[\"2246555\"]},{\"id\":51,\"code\":\"100049\",\"serviceCode\":\"100009\",\"serviceName\":\"Airtime Purchase\",\"serviceCategoryCode\":\"100021\",\"serviceCategoryName\":\"Mobile Prepaid\",\"serviceProviderCode\":\"100136\",\"serviceProviderName\":\"Noble\",\"name\":\"ORANGE\",\"status\":\"Active\",\"state\":\"Approved\",\"creationDate\":\"1629205195334\",\"modificationDate\":\"1629207213760\",\"msisdnsPrefix\":[\"2246293\"]}]}");

                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");


                    arrayList_OperatorListCode.add(getString(R.string.select_operator_star));
                    arrayList_OperatorListName.add(getString(R.string.select_operator_star));
                    arrayList_ServiceCode.add(getString(R.string.select_operator_star));
                    arrayList_serviceCategoryCode.add(getString(R.string.select_operator_star));
                    arrayList_serviceProviderName.add(getString(R.string.select_operator_star));
                    arrayList_serviceProviderCode.add(getString(R.string.select_operator_star));

                    if(resultCode.equalsIgnoreCase("0")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("operatorList");

                        for(int i=0;i<jsonArray.length();i++) {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);


                            String  operatorCode = jsonObject2.getString("code");
                            String  name_from_operatorList = jsonObject2.getString("name");
                           // String image_from_operatorList = jsonObject2.getString("fileName");
                            String serviceCode_from_operatorList = jsonObject2.getString("serviceCode");
                            String serviceCategoryCode = jsonObject2.getString("serviceCategoryCode");

                            String serviceProviderName_from_operatorList = jsonObject2.getString("serviceProviderName");
                            String serviceProviderCode = jsonObject2.getString("serviceProviderCode");

                            arrayList_OperatorListCode.add(operatorCode);
                            arrayList_OperatorListName.add(name_from_operatorList);

                            arrayList_ServiceCode.add(serviceCode_from_operatorList);
                            arrayList_serviceCategoryCode.add(serviceCategoryCode);
                            arrayList_serviceProviderCode.add(serviceProviderCode);

                            //  arrayList_serviceProviderName.add(serviceProviderName_from_operatorList);
                          //  arrayList_serviceProviderCode.add(serviceProviderCode_from_operatorList);

                        }

                        CommonBaseAdapterSecond arraadapter2 = new CommonBaseAdapterSecond(AirtimePurchases.this, arrayList_OperatorListName);

//                         if(countryCode_agent.equalsIgnoreCase(""))
//                                {
//                                    spinner_operator.setSelection(1);
//
//                                }


                        spinner_operator.setAdapter(arraadapter2);





                        api_country_all();

                    }

                    else {
                        Toast.makeText(AirtimePurchases.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(AirtimePurchases.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(AirtimePurchases.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }



    private void api_master_PRODUCTTYPE() {

      //  MyApplication.showloader(AirtimePurchases.this, getString(R.string.getting_user_info));


        API.GET_TRANSFER_DETAILS("ewallet/api/v1/master/PRODUCTTYPE",languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {


                   // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"6050\",\"requestedBy\":\"102112\",\"requestTime\":\"Thu Nov 04 13:37:44 IST 2021\",\"responseTime\":\"Thu Nov 04 13:37:44 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"productTypeList\":[{\"id\":1,\"code\":\"100000\",\"name\":\"Fixed\",\"status\":\"Active\",\"creationDate\":\"2020-12-03T11:12:04.273+0530\"},{\"id\":2,\"code\":\"100001\",\"name\":\"Flexi\",\"status\":\"Active\",\"creationDate\":\"2020-12-03T11:12:04.273+0530\"}]}");

                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");



                    arrayList_Name_PRODUCTTYPE = new ArrayList<>();
                    arrayList_Code_PRODUCTTYPE = new ArrayList<>();
                    arrayList_status_PRODUCTTYPE = new ArrayList<>();

                    if(resultCode.equalsIgnoreCase("0")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("productTypeList");

                        for(int i=0;i<jsonArray.length();i++) {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);


                            String name_from_master_PRODUCTTYPE = jsonObject2.getString("name");
                            String code_from_master_PRODUCTTYPE = jsonObject2.getString("code");
                            String status_from_master_PRODUCTTYPE = jsonObject2.getString("status");

                            arrayList_Name_PRODUCTTYPE.add(name_from_master_PRODUCTTYPE);
                            arrayList_Code_PRODUCTTYPE.add(code_from_master_PRODUCTTYPE);
                            arrayList_status_PRODUCTTYPE.add(status_from_master_PRODUCTTYPE);

                        }

                        currency_api();


                    }

                    else {
                        Toast.makeText(AirtimePurchases.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(AirtimePurchases.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(AirtimePurchases.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void currency_api() {

        //MyApplication.showloader(AirtimePurchases.this, getString(R.string.please_wait));

        String userCode_agentCode_from_mssid =  MyApplication.getSaveString("USERCODE", AirtimePurchases.this);


        API.GET_TRANSFER_DETAILS("ewallet/api/v1/walletOwnerCountryCurrency/" + userCode_agentCode_from_mssid, languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    //JSONObject jsonObject = new JSONObject("{"transactionId":"1789322","requestTime":"Wed Oct 20 15:53:33 IST 2021","responseTime":"Wed Oct 20 15:53:33 IST 2021","resultCode":"0","resultDescription":"Transaction Successful","walletOwnerCountryCurrencyList":[{"id":7452,"code":"107451","walletOwnerCode":"1000002488","currencyCode":"100062","currencyName":"GNF","currencySymbol":"Fr","countryCurrencyCode":"100076","inBound":true,"outBound":true,"status":"Active"}]}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("walletOwnerCountryCurrencyList");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            String  currencyName_agent_temp = jsonObject2.getString("currencyName");
                            if (currencyName_agent_temp.equalsIgnoreCase("GNF")) {
                                currencyCode_agent = jsonObject2.getString("currencyCode");
                                currencyName_agent = jsonObject2.getString("currencyName");
                                currencySymbol_sender = jsonObject2.getString("currencySymbol");
                                tvAmtCurr.setText(currencySymbol_sender);

                            } else {

                            }

                        }



                        api_serviceProvider_serviceCategory_serviceCode();


                    } else {
                        Toast.makeText(AirtimePurchases.this, resultDescription, Toast.LENGTH_LONG).show();
                       // finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(AirtimePurchases.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(AirtimePurchases.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }



    private void api_country_all() {


       // MyApplication.showloader(AirtimePurchases.this, getString(R.string.getting_user_info));


        API.GET_TRANSFER_DETAILS("ewallet/api/v1/country/all", languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {


                    //  JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1840087\",\"requestTime\":\"Tue Oct 26 00:01:09 IST 2021\",\"responseTime\":\"Tue Oct 26 00:01:09 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"countryList\":[{\"id\":1,\"code\":\"100000\",\"isoCode\":\"AFG\",\"name\":\"Afghanistan\",\"countryCode\":\"AF\",\"status\":\"Active\",\"dialCode\":\"+93\",\"mobileLength\":\"9\",\"currencyCode\":\"AFN\",\"currencySymbol\":\"؋\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:16.312+0530\"},{\"id\":249,\"code\":\"100248\",\"isoCode\":\"ALA\",\"name\":\"Åland Islands\",\"countryCode\":\"AX\",\"status\":\"Active\",\"dialCode\":\"+358\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:26.875+0530\"},{\"id\":2,\"code\":\"100001\",\"isoCode\":\"ALB\",\"name\":\"Albania\",\"countryCode\":\"AL\",\"status\":\"Active\",\"dialCode\":\"+355\",\"mobileLength\":\"9\",\"currencyCode\":\"ALL\",\"currencySymbol\":\"L\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:16.630+0530\"},{\"id\":3,\"code\":\"100002\",\"isoCode\":\"DZA\",\"name\":\"Algeria\",\"countryCode\":\"DZ\",\"status\":\"Active\",\"dialCode\":\"+213\",\"mobileLength\":\"9\",\"currencyCode\":\"DZD\",\"currencySymbol\":\"د.ج\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:16.915+0530\"},{\"id\":4,\"code\":\"100003\",\"isoCode\":\"ASM\",\"name\":\"American Samoa\",\"countryCode\":\"AS\",\"status\":\"Active\",\"dialCode\":\"+1684\",\"mobileLength\":\"10\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:17.218+0530\"},{\"id\":5,\"code\":\"100004\",\"isoCode\":\"AND\",\"name\":\"Andorra\",\"countryCode\":\"AD\",\"status\":\"Active\",\"dialCode\":\"+376\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:17.508+0530\"},{\"id\":6,\"code\":\"100005\",\"isoCode\":\"AGO\",\"name\":\"Angola\",\"countryCode\":\"AO\",\"status\":\"Active\",\"dialCode\":\"+244\",\"currencyCode\":\"AOA\",\"currencySymbol\":\"Kz\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:17.790+0530\"},{\"id\":7,\"code\":\"100006\",\"isoCode\":\"AIA\",\"name\":\"Anguilla\",\"countryCode\":\"AI\",\"status\":\"Active\",\"dialCode\":\"+1264\",\"mobileLength\":\"7\",\"currencyCode\":\"XCD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:18.078+0530\"},{\"id\":8,\"code\":\"100007\",\"isoCode\":\"ATA\",\"name\":\"Antarctica\",\"countryCode\":\"AQ\",\"status\":\"Active\",\"dialCode\":\"+672\",\"currencyCode\":\"AUD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:18.362+0530\"},{\"id\":9,\"code\":\"100008\",\"isoCode\":\"ATG\",\"name\":\"Antigua and Barbuda\",\"countryCode\":\"AG\",\"status\":\"Active\",\"dialCode\":\"+1268\",\"mobileLength\":\"7\",\"currencyCode\":\"XCD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:18.638+0530\"},{\"id\":10,\"code\":\"100009\",\"isoCode\":\"ARG\",\"name\":\"Argentina\",\"countryCode\":\"AR\",\"status\":\"Active\",\"dialCode\":\"+54\",\"mobileLength\":\"11\",\"currencyCode\":\"ARS\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:18.916+0530\"},{\"id\":11,\"code\":\"100010\",\"isoCode\":\"ARM\",\"name\":\"Armenia\",\"countryCode\":\"AM\",\"status\":\"Active\",\"dialCode\":\"+374\",\"mobileLength\":\"8\",\"currencyCode\":\"AMD\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:19.215+0530\"},{\"id\":12,\"code\":\"100011\",\"isoCode\":\"ABW\",\"name\":\"Aruba\",\"countryCode\":\"AW\",\"status\":\"Active\",\"dialCode\":\"+297\",\"mobileLength\":\"7\",\"currencyCode\":\"AWG\",\"currencySymbol\":\"ƒ\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:19.506+0530\"},{\"id\":13,\"code\":\"100012\",\"isoCode\":\"AUS\",\"name\":\"Australia\",\"countryCode\":\"AU\",\"status\":\"Active\",\"dialCode\":\"+61\",\"mobileLength\":\"9\",\"currencyCode\":\"AUD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:19.782+0530\"},{\"id\":14,\"code\":\"100013\",\"isoCode\":\"AUT\",\"name\":\"Austria\",\"countryCode\":\"AT\",\"status\":\"Active\",\"dialCode\":\"+43\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:20.067+0530\"},{\"id\":15,\"code\":\"100014\",\"isoCode\":\"AZE\",\"name\":\"Azerbaijan\",\"countryCode\":\"AZ\",\"status\":\"Active\",\"dialCode\":\"+994\",\"currencyCode\":\"AZN\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:20.358+0530\"},{\"id\":16,\"code\":\"100015\",\"isoCode\":\"BHS\",\"name\":\"Bahamas\",\"countryCode\":\"BS\",\"status\":\"Active\",\"dialCode\":\"+1242\",\"mobileLength\":\"7\",\"currencyCode\":\"BSD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:20.645+0530\"},{\"id\":17,\"code\":\"100016\",\"isoCode\":\"BHR\",\"name\":\"Bahrain\",\"countryCode\":\"BH\",\"status\":\"Active\",\"dialCode\":\"+973\",\"mobileLength\":\"8\",\"currencyCode\":\"BHD\",\"currencySymbol\":\".د.ب\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:20.923+0530\"},{\"id\":18,\"code\":\"100017\",\"isoCode\":\"BGD\",\"name\":\"Bangladesh\",\"countryCode\":\"BD\",\"status\":\"Active\",\"dialCode\":\"+880\",\"mobileLength\":\"10\",\"currencyCode\":\"BDT\",\"currencySymbol\":\"৳\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:21.206+0530\"},{\"id\":19,\"code\":\"100018\",\"isoCode\":\"BRB\",\"name\":\"Barbados\",\"countryCode\":\"BB\",\"status\":\"Active\",\"dialCode\":\"+1246\",\"mobileLength\":\"7\",\"currencyCode\":\"BBD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:21.499+0530\"},{\"id\":20,\"code\":\"100019\",\"isoCode\":\"BLR\",\"name\":\"Belarus\",\"countryCode\":\"BY\",\"status\":\"Active\",\"dialCode\":\"+375\",\"mobileLength\":\"9\",\"currencyCode\":\"BYN\",\"currencySymbol\":\"Br\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:21.784+0530\"},{\"id\":21,\"code\":\"100020\",\"isoCode\":\"BEL\",\"name\":\"Belgium\",\"countryCode\":\"BE\",\"status\":\"Active\",\"dialCode\":\"+32\",\"mobileLength\":\"9\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:22.068+0530\"},{\"id\":22,\"code\":\"100021\",\"isoCode\":\"BLZ\",\"name\":\"Belize\",\"countryCode\":\"BZ\",\"status\":\"Active\",\"dialCode\":\"+501\",\"currencyCode\":\"BZD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:22.351+0530\"},{\"id\":23,\"code\":\"100022\",\"isoCode\":\"BEN\",\"name\":\"Benin\",\"countryCode\":\"BJ\",\"status\":\"Active\",\"dialCode\":\"+229\",\"mobileLength\":\"8\",\"currencyCode\":\"XOF\",\"currencySymbol\":\"CFA\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:22.629+0530\"},{\"id\":24,\"code\":\"100023\",\"isoCode\":\"BMU\",\"name\":\"Bermuda\",\"countryCode\":\"BM\",\"status\":\"Active\",\"dialCode\":\"+1441\",\"mobileLength\":\"7\",\"currencyCode\":\"BMD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:22.944+0530\"},{\"id\":25,\"code\":\"100024\",\"isoCode\":\"BTN\",\"name\":\"Bhutan\",\"countryCode\":\"BT\",\"status\":\"Active\",\"dialCode\":\"+975\",\"currencyCode\":\"BTN\",\"currencySymbol\":\"Nu.\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:23.222+0530\"},{\"id\":26,\"code\":\"100025\",\"isoCode\":\"BOL\",\"name\":\"Bolivia (Plurinational State of)\",\"countryCode\":\"BO\",\"status\":\"Active\",\"dialCode\":\"+591\",\"mobileLength\":\"8\",\"currencyCode\":\"BOB\",\"currencySymbol\":\"Bs.\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:23.500+0530\"},{\"id\":27,\"code\":\"100026\",\"isoCode\":\"BES\",\"name\":\"Bonaire, Sint Eustatius and Saba\",\"countryCode\":\"BQ\",\"status\":\"Active\",\"dialCode\":\"+5997\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:23.790+0530\"},{\"id\":28,\"code\":\"100027\",\"isoCode\":\"BIH\",\"name\":\"Bosnia and Herzegovina\",\"countryCode\":\"BA\",\"status\":\"Active\",\"dialCode\":\"+387\",\"mobileLength\":\"8\",\"currencyCode\":\"BAM\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:24.076+0530\"},{\"id\":29,\"code\":\"100028\",\"isoCode\":\"BWA\",\"name\":\"Botswana\",\"countryCode\":\"BW\",\"status\":\"Active\",\"dialCode\":\"+267\",\"mobileLength\":\"8\",\"currencyCode\":\"BWP\",\"currencySymbol\":\"P\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:24.362+0530\"},{\"id\":30,\"code\":\"100029\",\"isoCode\":\"BVT\",\"name\":\"Bouvet Island\",\"countryCode\":\"BV\",\"status\":\"Active\",\"currencyCode\":\"NOK\",\"currencySymbol\":\"kr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:24.652+0530\"},{\"id\":31,\"code\":\"100030\",\"isoCode\":\"BRA\",\"name\":\"Brazil\",\"countryCode\":\"BR\",\"status\":\"Active\",\"dialCode\":\"+55\",\"mobileLength\":\"11\",\"currencyCode\":\"BRL\",\"currencySymbol\":\"R$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:24.947+0530\"},{\"id\":32,\"code\":\"100031\",\"isoCode\":\"IOT\",\"name\":\"British Indian Ocean Territory\",\"countryCode\":\"IO\",\"status\":\"Active\",\"dialCode\":\"+246\",\"mobileLength\":\"7\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:25.239+0530\"},{\"id\":33,\"code\":\"100032\",\"isoCode\":\"BRN\",\"name\":\"Brunei Darussalam\",\"countryCode\":\"BN\",\"status\":\"Active\",\"dialCode\":\"+673\",\"currencyCode\":\"BND\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:25.525+0530\"},{\"id\":34,\"code\":\"100033\",\"isoCode\":\"BGR\",\"name\":\"Bulgaria\",\"countryCode\":\"BG\",\"status\":\"Active\",\"dialCode\":\"+359\",\"currencyCode\":\"BGN\",\"currencySymbol\":\"лв\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:25.807+0530\"},{\"id\":35,\"code\":\"100034\",\"isoCode\":\"BFA\",\"name\":\"Burkina Faso\",\"countryCode\":\"BF\",\"status\":\"Active\",\"dialCode\":\"+226\",\"mobileLength\":\"8\",\"currencyCode\":\"XOF\",\"currencySymbol\":\"CFA\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:26.080+0530\"},{\"id\":36,\"code\":\"100035\",\"isoCode\":\"BDI\",\"name\":\"Burundi\",\"countryCode\":\"BI\",\"status\":\"Active\",\"dialCode\":\"+257\",\"mobileLength\":\"8\",\"currencyCode\":\"BIF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:26.372+0530\"},{\"id\":37,\"code\":\"100036\",\"isoCode\":\"CPV\",\"name\":\"Cabo Verde\",\"countryCode\":\"CV\",\"status\":\"Active\",\"dialCode\":\"+238\",\"mobileLength\":\"7\",\"currencyCode\":\"CVE\",\"currencySymbol\":\"Esc\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:26.650+0530\"},{\"id\":38,\"code\":\"100037\",\"isoCode\":\"KHM\",\"name\":\"Cambodia\",\"countryCode\":\"KH\",\"status\":\"Active\",\"dialCode\":\"+855\",\"mobileLength\":\"9\",\"currencyCode\":\"KHR\",\"currencySymbol\":\"៛\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:26.940+0530\"},{\"id\":39,\"code\":\"100038\",\"isoCode\":\"CMR\",\"name\":\"Cameroon\",\"countryCode\":\"CM\",\"status\":\"Active\",\"dialCode\":\"+237\",\"mobileLength\":\"10\",\"currencyCode\":\"XAF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:27.225+0530\"},{\"id\":40,\"code\":\"100039\",\"isoCode\":\"CAN\",\"name\":\"Canada\",\"countryCode\":\"CA\",\"status\":\"Active\",\"dialCode\":\"+1\",\"mobileLength\":\"10\",\"currencyCode\":\"CAD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:27.517+0530\"},{\"id\":41,\"code\":\"100040\",\"isoCode\":\"CYM\",\"name\":\"Cayman Islands\",\"countryCode\":\"KY\",\"status\":\"Active\",\"dialCode\":\"+1345\",\"mobileLength\":\"7\",\"currencyCode\":\"KYD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:27.817+0530\"},{\"id\":42,\"code\":\"100041\",\"isoCode\":\"CAF\",\"name\":\"Central African Republic\",\"countryCode\":\"CF\",\"status\":\"Active\",\"dialCode\":\"+236\",\"currencyCode\":\"XAF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:28.100+0530\"},{\"id\":43,\"code\":\"100042\",\"isoCode\":\"TCD\",\"name\":\"Chad\",\"countryCode\":\"TD\",\"status\":\"Active\",\"dialCode\":\"+235\",\"mobileLength\":\"8\",\"currencyCode\":\"XAF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:28.391+0530\"},{\"id\":44,\"code\":\"100043\",\"isoCode\":\"CHL\",\"name\":\"Chile\",\"countryCode\":\"CL\",\"status\":\"Active\",\"dialCode\":\"+56\",\"mobileLength\":\"9\",\"currencyCode\":\"CLP\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:28.682+0530\"},{\"id\":45,\"code\":\"100044\",\"isoCode\":\"CHN\",\"name\":\"China\",\"countryCode\":\"CN\",\"status\":\"Active\",\"dialCode\":\"+86\",\"mobileLength\":\"11\",\"currencyCode\":\"CNY\",\"currencySymbol\":\"¥\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:28.957+0530\"},{\"id\":46,\"code\":\"100045\",\"isoCode\":\"CXR\",\"name\":\"Christmas Island\",\"countryCode\":\"CX\",\"status\":\"Active\",\"dialCode\":\"+61\",\"currencyCode\":\"AUD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:29.230+0530\"},{\"id\":47,\"code\":\"100046\",\"isoCode\":\"CCK\",\"name\":\"Cocos (Keeling) Islands\",\"countryCode\":\"CC\",\"status\":\"Active\",\"dialCode\":\"+61\",\"currencyCode\":\"AUD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:29.508+0530\"},{\"id\":48,\"code\":\"100047\",\"isoCode\":\"COL\",\"name\":\"Colombia\",\"countryCode\":\"CO\",\"status\":\"Active\",\"dialCode\":\"+57\",\"mobileLength\":\"10\",\"currencyCode\":\"COP\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:29.779+0530\"},{\"id\":49,\"code\":\"100048\",\"isoCode\":\"COM\",\"name\":\"Comoros\",\"countryCode\":\"KM\",\"status\":\"Active\",\"dialCode\":\"+269\",\"currencyCode\":\"KMF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:30.059+0530\"},{\"id\":51,\"code\":\"100050\",\"isoCode\":\"COG\",\"name\":\"Congo\",\"countryCode\":\"CG\",\"status\":\"Active\",\"dialCode\":\"+242\",\"currencyCode\":\"XAF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:30.615+0530\"},{\"id\":50,\"code\":\"100049\",\"isoCode\":\"COD\",\"name\":\"Congo (Democratic Republic of the)\",\"countryCode\":\"CD\",\"status\":\"Active\",\"dialCode\":\"+243\",\"mobileLength\":\"9\",\"currencyCode\":\"CDF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:30.330+0530\"},{\"id\":52,\"code\":\"100051\",\"isoCode\":\"COK\",\"name\":\"Cook Islands\",\"countryCode\":\"CK\",\"status\":\"Active\",\"dialCode\":\"+682\",\"mobileLength\":\"5\",\"currencyCode\":\"NZD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:30.905+0530\"},{\"id\":53,\"code\":\"100052\",\"isoCode\":\"CRI\",\"name\":\"Costa Rica\",\"countryCode\":\"CR\",\"status\":\"Active\",\"dialCode\":\"+506\",\"mobileLength\":\"8\",\"currencyCode\":\"CRC\",\"currencySymbol\":\"₡\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:31.181+0530\"},{\"id\":59,\"code\":\"100058\",\"isoCode\":\"CIV\",\"name\":\"Côte d'Ivoire\",\"countryCode\":\"CI\",\"status\":\"Active\",\"dialCode\":\"+225\",\"mobileLength\":\"8\",\"currencyCode\":\"XOF\",\"currencySymbol\":\"CFA\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:32.879+0530\"},{\"id\":54,\"code\":\"100053\",\"isoCode\":\"HRV\",\"name\":\"Croatia\",\"countryCode\":\"HR\",\"status\":\"Active\",\"dialCode\":\"+385\",\"mobileLength\":\"9\",\"currencyCode\":\"HRK\",\"currencySymbol\":\"kn\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:31.463+0530\"},{\"id\":55,\"code\":\"100054\",\"isoCode\":\"CUB\",\"name\":\"Cuba\",\"countryCode\":\"CU\",\"status\":\"Active\",\"dialCode\":\"+53\",\"currencyCode\":\"CUC\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:31.742+0530\"},{\"id\":56,\"code\":\"100055\",\"isoCode\":\"CUW\",\"name\":\"Curaçao\",\"countryCode\":\"CW\",\"status\":\"Active\",\"dialCode\":\"+599\",\"currencyCode\":\"ANG\",\"currencySymbol\":\"ƒ\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:32.032+0530\"},{\"id\":57,\"code\":\"100056\",\"isoCode\":\"CYP\",\"name\":\"Cyprus\",\"countryCode\":\"CY\",\"status\":\"Active\",\"dialCode\":\"+357\",\"mobileLength\":\"8\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:32.311+0530\"},{\"id\":58,\"code\":\"100057\",\"isoCode\":\"CZE\",\"name\":\"Czech Republic\",\"countryCode\":\"CZ\",\"status\":\"Active\",\"dialCode\":\"+420\",\"mobileLength\":\"9\",\"currencyCode\":\"CZK\",\"currencySymbol\":\"Kč\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:32.597+0530\"},{\"id\":60,\"code\":\"100059\",\"isoCode\":\"DNK\",\"name\":\"Denmark\",\"countryCode\":\"DK\",\"status\":\"Active\",\"dialCode\":\"+45\",\"mobileLength\":\"8\",\"currencyCode\":\"DKK\",\"currencySymbol\":\"kr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:33.160+0530\"},{\"id\":61,\"code\":\"100060\",\"isoCode\":\"DJI\",\"name\":\"Djibouti\",\"countryCode\":\"DJ\",\"status\":\"Active\",\"dialCode\":\"+253\",\"currencyCode\":\"DJF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:33.451+0530\"},{\"id\":62,\"code\":\"100061\",\"isoCode\":\"DMA\",\"name\":\"Dominica\",\"countryCode\":\"DM\",\"status\":\"Active\",\"dialCode\":\"+1767\",\"mobileLength\":\"7\",\"currencyCode\":\"XCD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:33.727+0530\"},{\"id\":63,\"code\":\"100062\",\"isoCode\":\"DOM\",\"name\":\"Dominican Republic\",\"countryCode\":\"DO\",\"status\":\"Active\",\"dialCode\":\"+1809\",\"mobileLength\":\"7\",\"currencyCode\":\"DOP\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:34.011+0530\"},{\"id\":64,\"code\":\"100063\",\"isoCode\":\"ECU\",\"name\":\"Ecuador\",\"countryCode\":\"EC\",\"status\":\"Active\",\"dialCode\":\"+593\",\"mobileLength\":\"9\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:34.291+0530\"},{\"id\":65,\"code\":\"100064\",\"isoCode\":\"EGY\",\"name\":\"Egypt\",\"countryCode\":\"EG\",\"status\":\"Active\",\"dialCode\":\"+20\",\"mobileLength\":\"10\",\"currencyCode\":\"EGP\",\"currencySymbol\":\"£\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:34.571+0530\"},{\"id\":66,\"code\":\"100065\",\"isoCode\":\"SLV\",\"name\":\"El Salvador\",\"countryCode\":\"SV\",\"status\":\"Active\",\"dialCode\":\"+503\",\"mobileLength\":\"8\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:34.851+0530\"},{\"id\":67,\"code\":\"100066\",\"isoCode\":\"GNQ\",\"name\":\"Equatorial Guinea\",\"countryCode\":\"GQ\",\"status\":\"Active\",\"dialCode\":\"+240\",\"currencyCode\":\"XAF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:35.123+0530\"},{\"id\":68,\"code\":\"100067\",\"isoCode\":\"ERI\",\"name\":\"Eritrea\",\"countryCode\":\"ER\",\"status\":\"Active\",\"dialCode\":\"+291\",\"currencyCode\":\"ERN\",\"currencySymbol\":\"Nfk\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:35.396+0530\"},{\"id\":69,\"code\":\"100068\",\"isoCode\":\"EST\",\"name\":\"Estonia\",\"countryCode\":\"EE\",\"status\":\"Active\",\"dialCode\":\"+372\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:35.691+0530\"},{\"id\":71,\"code\":\"100070\",\"isoCode\":\"ETH\",\"name\":\"Ethiopia\",\"countryCode\":\"ET\",\"status\":\"Active\",\"dialCode\":\"+251\",\"currencyCode\":\"ETB\",\"currencySymbol\":\"Br\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:36.248+0530\"},{\"id\":72,\"code\":\"100071\",\"isoCode\":\"FLK\",\"name\":\"Falkland Islands (Malvinas)\",\"countryCode\":\"FK\",\"status\":\"Active\",\"dialCode\":\"+500\",\"mobileLength\":\"5\",\"currencyCode\":\"FKP\",\"currencySymbol\":\"£\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:36.543+0530\"},{\"id\":73,\"code\":\"100072\",\"isoCode\":\"FRO\",\"name\":\"Faroe Islands\",\"countryCode\":\"FO\",\"status\":\"Active\",\"dialCode\":\"+298\",\"mobileLength\":\"5\",\"currencyCode\":\"DKK\",\"currencySymbol\":\"kr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:36.822+0530\"},{\"id\":74,\"code\":\"100073\",\"isoCode\":\"FJI\",\"name\":\"Fiji\",\"countryCode\":\"FJ\",\"status\":\"Active\",\"dialCode\":\"+679\",\"mobileLength\":\"7\",\"currencyCode\":\"FJD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:37.105+0530\"},{\"id\":75,\"code\":\"100074\",\"isoCode\":\"FIN\",\"name\":\"Finland\",\"countryCode\":\"FI\",\"status\":\"Active\",\"dialCode\":\"+358\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:37.386+0530\"},{\"id\":76,\"code\":\"100075\",\"isoCode\":\"FRA\",\"name\":\"France\",\"countryCode\":\"FR\",\"status\":\"Active\",\"dialCode\":\"+33\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:37.665+0530\"},{\"id\":77,\"code\":\"100076\",\"isoCode\":\"GUF\",\"name\":\"French Guiana\",\"countryCode\":\"GF\",\"status\":\"Active\",\"dialCode\":\"+594\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:37.946+0530\"},{\"id\":78,\"code\":\"100077\",\"isoCode\":\"PYF\",\"name\":\"French Polynesia\",\"countryCode\":\"PF\",\"status\":\"Active\",\"dialCode\":\"+689\",\"currencyCode\":\"XPF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:38.226+0530\"},{\"id\":79,\"code\":\"100078\",\"isoCode\":\"ATF\",\"name\":\"French Southern Territories\",\"countryCode\":\"TF\",\"status\":\"Active\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:38.499+0530\"},{\"id\":80,\"code\":\"100079\",\"isoCode\":\"GAB\",\"name\":\"Gabon\",\"countryCode\":\"GA\",\"status\":\"Active\",\"dialCode\":\"+241\",\"currencyCode\":\"XAF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:38.771+0530\"},{\"id\":81,\"code\":\"100080\",\"isoCode\":\"GMB\",\"name\":\"Gambia\",\"countryCode\":\"GM\",\"status\":\"Active\",\"dialCode\":\"+220\",\"mobileLength\":\"7\",\"currencyCode\":\"GMD\",\"currencySymbol\":\"D\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:39.055+0530\"},{\"id\":82,\"code\":\"100081\",\"isoCode\":\"GEO\",\"name\":\"Georgia\",\"countryCode\":\"GE\",\"status\":\"Active\",\"dialCode\":\"+995\",\"currencyCode\":\"GEL\",\"currencySymbol\":\"ლ\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:39.349+0530\"},{\"id\":83,\"code\":\"100082\",\"isoCode\":\"DEU\",\"name\":\"Germany\",\"countryCode\":\"DE\",\"status\":\"Active\",\"dialCode\":\"+49\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:39.623+0530\"},{\"id\":84,\"code\":\"100083\",\"isoCode\":\"GHA\",\"name\":\"Ghana\",\"countryCode\":\"GH\",\"status\":\"Active\",\"dialCode\":\"+233\",\"mobileLength\":\"9\",\"currencyCode\":\"GHS\",\"currencySymbol\":\"₵\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:39.907+0530\"},{\"id\":85,\"code\":\"100084\",\"isoCode\":\"GIB\",\"name\":\"Gibraltar\",\"countryCode\":\"GI\",\"status\":\"Active\",\"dialCode\":\"+350\",\"currencyCode\":\"GIP\",\"currencySymbol\":\"£\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:40.187+0530\"},{\"id\":86,\"code\":\"100085\",\"isoCode\":\"GRC\",\"name\":\"Greece\",\"countryCode\":\"GR\",\"status\":\"Active\",\"dialCode\":\"+30\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:40.467+0530\"},{\"id\":87,\"code\":\"100086\",\"isoCode\":\"GRL\",\"name\":\"Greenland\",\"countryCode\":\"GL\",\"status\":\"Active\",\"dialCode\":\"+299\",\"currencyCode\":\"DKK\",\"currencySymbol\":\"kr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:40.763+0530\"},{\"id\":88,\"code\":\"100087\",\"isoCode\":\"GRD\",\"name\":\"Grenada\",\"countryCode\":\"GD\",\"status\":\"Active\",\"dialCode\":\"+1473\",\"mobileLength\":\"7\",\"currencyCode\":\"XCD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:41.044+0530\"},{\"id\":89,\"code\":\"100088\",\"isoCode\":\"GLP\",\"name\":\"Guadeloupe\",\"countryCode\":\"GP\",\"status\":\"Active\",\"dialCode\":\"+590\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:41.323+0530\"},{\"id\":90,\"code\":\"100089\",\"isoCode\":\"GUM\",\"name\":\"Guam\",\"countryCode\":\"GU\",\"status\":\"Active\",\"dialCode\":\"+1671\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:41.597+0530\"},{\"id\":91,\"code\":\"100090\",\"isoCode\":\"GTM\",\"name\":\"Guatemala\",\"countryCode\":\"GT\",\"status\":\"Active\",\"dialCode\":\"+502\",\"mobileLength\":\"8\",\"currencyCode\":\"GTQ\",\"currencySymbol\":\"Q\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:41.904+0530\"},{\"id\":92,\"code\":\"100091\",\"isoCode\":\"GGY\",\"name\":\"Guernsey\",\"countryCode\":\"GG\",\"status\":\"Active\",\"dialCode\":\"+44\",\"currencyCode\":\"GBP\",\"currencySymbol\":\"£\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:42.179+0530\"},{\"id\":93,\"code\":\"100092\",\"isoCode\":\"GIN\",\"name\":\"Guinea\",\"countryCode\":\"GN\",\"status\":\"Active\",\"dialCode\":\"+224\",\"mobileLength\":\"9\",\"currencyCode\":\"GNF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":true,\"creationDate\":\"2020-08-11T15:04:42.459+0530\",\"nameFr\":\"Guinée\"},{\"id\":94,\"code\":\"100093\",\"isoCode\":\"GNB\",\"name\":\"Guinea-Bissau\",\"countryCode\":\"GW\",\"status\":\"Active\",\"dialCode\":\"+245\",\"mobileLength\":\"9\",\"currencyCode\":\"XOF\",\"currencySymbol\":\"CFA\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:42.729+0530\"},{\"id\":95,\"code\":\"100094\",\"isoCode\":\"GUY\",\"name\":\"Guyana\",\"countryCode\":\"GY\",\"status\":\"Active\",\"dialCode\":\"+592\",\"mobileLength\":\"7\",\"currencyCode\":\"GYD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:43.033+0530\"},{\"id\":96,\"code\":\"100095\",\"isoCode\":\"HTI\",\"name\":\"Haiti\",\"countryCode\":\"HT\",\"status\":\"Active\",\"dialCode\":\"+509\",\"mobileLength\":\"8\",\"currencyCode\":\"HTG\",\"currencySymbol\":\"G\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:43.316+0530\"},{\"id\":97,\"code\":\"100096\",\"isoCode\":\"HMD\",\"name\":\"Heard Island and McDonald Islands\",\"countryCode\":\"HM\",\"status\":\"Active\",\"currencyCode\":\"AUD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:43.598+0530\"},{\"id\":98,\"code\":\"100097\",\"isoCode\":\"VAT\",\"name\":\"Holy See\",\"countryCode\":\"VA\",\"status\":\"Active\",\"dialCode\":\"+379\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:43.879+0530\"},{\"id\":99,\"code\":\"100098\",\"isoCode\":\"HND\",\"name\":\"Honduras\",\"countryCode\":\"HN\",\"status\":\"Active\",\"dialCode\":\"+504\",\"mobileLength\":\"8\",\"currencyCode\":\"HNL\",\"currencySymbol\":\"L\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:44.160+0530\"},{\"id\":100,\"code\":\"100099\",\"isoCode\":\"HKG\",\"name\":\"Hong Kong\",\"countryCode\":\"HK\",\"status\":\"Active\",\"dialCode\":\"+852\",\"currencyCode\":\"HKD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:44.442+0530\"},{\"id\":101,\"code\":\"100100\",\"isoCode\":\"HUN\",\"name\":\"Hungary\",\"countryCode\":\"HU\",\"status\":\"Active\",\"dialCode\":\"+36\",\"currencyCode\":\"HUF\",\"currencySymbol\":\"Ft\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:44.714+0530\"},{\"id\":102,\"code\":\"100101\",\"isoCode\":\"ISL\",\"name\":\"Iceland\",\"countryCode\":\"IS\",\"status\":\"Active\",\"dialCode\":\"+354\",\"currencyCode\":\"ISK\",\"currencySymbol\":\"kr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:44.988+0530\"},{\"id\":103,\"code\":\"100102\",\"isoCode\":\"IND\",\"name\":\"India\",\"countryCode\":\"IN\",\"status\":\"Active\",\"dialCode\":\"+91\",\"mobileLength\":\"10\",\"currencyCode\":\"INR\",\"currencySymbol\":\"₹\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:45.281+0530\",\"nameFr\":\"Inde\"},{\"id\":104,\"code\":\"100103\",\"isoCode\":\"IDN\",\"name\":\"Indonesia\",\"countryCode\":\"ID\",\"status\":\"Active\",\"dialCode\":\"+62\",\"mobileLength\":\"9\",\"currencyCode\":\"IDR\",\"currencySymbol\":\"Rp\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:45.630+0530\"},{\"id\":105,\"code\":\"100104\",\"isoCode\":\"IRN\",\"name\":\"Iran (Islamic Republic of)\",\"countryCode\":\"IR\",\"status\":\"Active\",\"dialCode\":\"+98\",\"currencyCode\":\"IRR\",\"currencySymbol\":\"﷼\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:45.918+0530\"},{\"id\":106,\"code\":\"100105\",\"isoCode\":\"IRQ\",\"name\":\"Iraq\",\"countryCode\":\"IQ\",\"status\":\"Active\",\"dialCode\":\"+964\",\"mobileLength\":\"10\",\"currencyCode\":\"IQD\",\"currencySymbol\":\"ع.د\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:46.198+0530\"},{\"id\":107,\"code\":\"100106\",\"isoCode\":\"IRL\",\"name\":\"Ireland\",\"countryCode\":\"IE\",\"status\":\"Active\",\"dialCode\":\"+353\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:46.498+0530\"},{\"id\":108,\"code\":\"100107\",\"isoCode\":\"IMN\",\"name\":\"Isle of Man\",\"countryCode\":\"IM\",\"status\":\"Active\",\"dialCode\":\"+44\",\"currencyCode\":\"GBP\",\"currencySymbol\":\"£\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:46.790+0530\"},{\"id\":109,\"code\":\"100108\",\"isoCode\":\"ISR\",\"name\":\"Israel\",\"countryCode\":\"IL\",\"status\":\"Active\",\"dialCode\":\"+972\",\"mobileLength\":\"9\",\"currencyCode\":\"ILS\",\"currencySymbol\":\"₪\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:47.087+0530\"},{\"id\":110,\"code\":\"100109\",\"isoCode\":\"ITA\",\"name\":\"Italy\",\"countryCode\":\"IT\",\"status\":\"Active\",\"dialCode\":\"+39\",\"mobileLength\":\"10\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:47.381+0530\"},{\"id\":111,\"code\":\"100110\",\"isoCode\":\"JAM\",\"name\":\"Jamaica\",\"countryCode\":\"JM\",\"status\":\"Active\",\"dialCode\":\"+1876\",\"mobileLength\":\"7\",\"currencyCode\":\"JMD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:47.666+0530\"},{\"id\":112,\"code\":\"100111\",\"isoCode\":\"JPN\",\"name\":\"Japan\",\"countryCode\":\"JP\",\"status\":\"Active\",\"dialCode\":\"+81\",\"currencyCode\":\"JPY\",\"currencySymbol\":\"¥\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:47.938+0530\"},{\"id\":113,\"code\":\"100112\",\"isoCode\":\"JEY\",\"name\":\"Jersey\",\"countryCode\":\"JE\",\"status\":\"Active\",\"dialCode\":\"+44\",\"currencyCode\":\"GBP\",\"currencySymbol\":\"£\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:48.218+0530\"},{\"id\":114,\"code\":\"100113\",\"isoCode\":\"JOR\",\"name\":\"Jordan\",\"countryCode\":\"JO\",\"status\":\"Active\",\"dialCode\":\"+962\",\"mobileLength\":\"9\",\"currencyCode\":\"JOD\",\"currencySymbol\":\"د.ا\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:48.489+0530\"},{\"id\":115,\"code\":\"100114\",\"isoCode\":\"KAZ\",\"name\":\"Kazakhstan\",\"countryCode\":\"KZ\",\"status\":\"Active\",\"dialCode\":\"+76\",\"mobileLength\":\"10\",\"currencyCode\":\"KZT\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:48.779+0530\"},{\"id\":116,\"code\":\"100115\",\"isoCode\":\"KEN\",\"name\":\"Kenya\",\"countryCode\":\"KE\",\"status\":\"Active\",\"dialCode\":\"+254\",\"mobileLength\":\"9\",\"currencyCode\":\"KES\",\"currencySymbol\":\"Sh\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:49.060+0530\"},{\"id\":117,\"code\":\"100116\",\"isoCode\":\"KIR\",\"name\":\"Kiribati\",\"countryCode\":\"KI\",\"status\":\"Active\",\"dialCode\":\"+686\",\"currencyCode\":\"AUD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:49.340+0530\"},{\"id\":118,\"code\":\"100117\",\"isoCode\":\"PRK\",\"name\":\"Korea (Democratic People's Republic of)\",\"countryCode\":\"KP\",\"status\":\"Active\",\"dialCode\":\"+850\",\"currencyCode\":\"KPW\",\"currencySymbol\":\"₩\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:49.630+0530\"},{\"id\":119,\"code\":\"100118\",\"isoCode\":\"KOR\",\"name\":\"Korea (Republic of)\",\"countryCode\":\"KR\",\"status\":\"Active\",\"dialCode\":\"+82\",\"currencyCode\":\"KRW\",\"currencySymbol\":\"₩\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:49.916+0530\"},{\"id\":120,\"code\":\"100119\",\"isoCode\":\"KWT\",\"name\":\"Kuwait\",\"countryCode\":\"KW\",\"status\":\"Active\",\"dialCode\":\"+965\",\"mobileLength\":\"8\",\"currencyCode\":\"KWD\",\"currencySymbol\":\"د.ك\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:50.198+0530\"},{\"id\":121,\"code\":\"100120\",\"isoCode\":\"KGZ\",\"name\":\"Kyrgyzstan\",\"countryCode\":\"KG\",\"status\":\"Active\",\"dialCode\":\"+996\",\"mobileLength\":\"9\",\"currencyCode\":\"KGS\",\"currencySymbol\":\"с\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:50.485+0530\"},{\"id\":122,\"code\":\"100121\",\"isoCode\":\"LAO\",\"name\":\"Lao People's Democratic Republic\",\"countryCode\":\"LA\",\"status\":\"Active\",\"dialCode\":\"+856\",\"mobileLength\":\"10\",\"currencyCode\":\"LAK\",\"currencySymbol\":\"₭\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:50.767+0530\"},{\"id\":123,\"code\":\"100122\",\"isoCode\":\"LVA\",\"name\":\"Latvia\",\"countryCode\":\"LV\",\"status\":\"Active\",\"dialCode\":\"+371\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:51.057+0530\"},{\"id\":124,\"code\":\"100123\",\"isoCode\":\"LBN\",\"name\":\"Lebanon\",\"countryCode\":\"LB\",\"status\":\"Active\",\"dialCode\":\"+961\",\"currencyCode\":\"LBP\",\"currencySymbol\":\"ل.ل\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:51.344+0530\"},{\"id\":125,\"code\":\"100124\",\"isoCode\":\"LSO\",\"name\":\"Lesotho\",\"countryCode\":\"LS\",\"status\":\"Active\",\"dialCode\":\"+266\",\"currencyCode\":\"LSL\",\"currencySymbol\":\"L\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:51.616+0530\"},{\"id\":126,\"code\":\"100125\",\"isoCode\":\"LBR\",\"name\":\"Liberia\",\"countryCode\":\"LR\",\"status\":\"Active\",\"dialCode\":\"+231\",\"mobileLength\":\"9\",\"currencyCode\":\"LRD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:51.890+0530\"},{\"id\":127,\"code\":\"100126\",\"isoCode\":\"LBY\",\"name\":\"Libya\",\"countryCode\":\"LY\",\"status\":\"Active\",\"dialCode\":\"+218\",\"currencyCode\":\"LYD\",\"currencySymbol\":\"ل.د\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:52.167+0530\"},{\"id\":128,\"code\":\"100127\",\"isoCode\":\"LIE\",\"name\":\"Liechtenstein\",\"countryCode\":\"LI\",\"status\":\"Active\",\"dialCode\":\"+423\",\"currencyCode\":\"CHF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:52.445+0530\"},{\"id\":129,\"code\":\"100128\",\"isoCode\":\"LTU\",\"name\":\"Lithuania\",\"countryCode\":\"LT\",\"status\":\"Active\",\"dialCode\":\"+370\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:52.726+0530\"},{\"id\":130,\"code\":\"100129\",\"isoCode\":\"LUX\",\"name\":\"Luxembourg\",\"countryCode\":\"LU\",\"status\":\"Active\",\"dialCode\":\"+352\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:53.004+0530\"},{\"id\":131,\"code\":\"100130\",\"isoCode\":\"MAC\",\"name\":\"Macao\",\"countryCode\":\"MO\",\"status\":\"Active\",\"dialCode\":\"+853\",\"currencyCode\":\"MOP\",\"currencySymbol\":\"P\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:53.286+0530\"},{\"id\":180,\"code\":\"100179\",\"isoCode\":\"MKD\",\"name\":\"Macedonia (the former Yugoslav Republic of)\",\"countryCode\":\"MK\",\"status\":\"Active\",\"dialCode\":\"+389\",\"currencyCode\":\"MKD\",\"currencySymbol\":\"ден\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:07.168+0530\"},{\"id\":132,\"code\":\"100131\",\"isoCode\":\"MDG\",\"name\":\"Madagascar\",\"countryCode\":\"MG\",\"status\":\"Active\",\"dialCode\":\"+261\",\"mobileLength\":\"9\",\"currencyCode\":\"MGA\",\"currencySymbol\":\"Ar\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:53.567+0530\"},{\"id\":133,\"code\":\"100132\",\"isoCode\":\"MWI\",\"name\":\"Malawi\",\"countryCode\":\"MW\",\"status\":\"Active\",\"dialCode\":\"+265\",\"currencyCode\":\"MWK\",\"currencySymbol\":\"MK\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:53.838+0530\"},{\"id\":134,\"code\":\"100133\",\"isoCode\":\"MYS\",\"name\":\"Malaysia\",\"countryCode\":\"MY\",\"status\":\"Active\",\"dialCode\":\"+60\",\"mobileLength\":\"11\",\"currencyCode\":\"MYR\",\"currencySymbol\":\"RM\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:54.129+0530\"},{\"id\":135,\"code\":\"100134\",\"isoCode\":\"MDV\",\"name\":\"Maldives\",\"countryCode\":\"MV\",\"status\":\"Active\",\"dialCode\":\"+960\",\"currencyCode\":\"MVR\",\"currencySymbol\":\".ރ\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:54.428+0530\"},{\"id\":136,\"code\":\"100135\",\"isoCode\":\"MLI\",\"name\":\"Mali\",\"countryCode\":\"ML\",\"status\":\"Active\",\"dialCode\":\"+223\",\"mobileLength\":\"8\",\"currencyCode\":\"XOF\",\"currencySymbol\":\"CFA\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:54.701+0530\"},{\"id\":137,\"code\":\"100136\",\"isoCode\":\"MLT\",\"name\":\"Malta\",\"countryCode\":\"MT\",\"status\":\"Active\",\"dialCode\":\"+356\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:54.975+0530\"},{\"id\":138,\"code\":\"100137\",\"isoCode\":\"MHL\",\"name\":\"Marshall Islands\",\"countryCode\":\"MH\",\"status\":\"Active\",\"dialCode\":\"+692\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:55.256+0530\"},{\"id\":139,\"code\":\"100138\",\"isoCode\":\"MTQ\",\"name\":\"Martinique\",\"countryCode\":\"MQ\",\"status\":\"Active\",\"dialCode\":\"+596\",\"mobileLength\":\"9\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:55.550+0530\"},{\"id\":140,\"code\":\"100139\",\"isoCode\":\"MRT\",\"name\":\"Mauritania\",\"countryCode\":\"MR\",\"status\":\"Active\",\"dialCode\":\"+222\",\"currencyCode\":\"MRO\",\"currencySymbol\":\"UM\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:55.832+0530\"},{\"id\":141,\"code\":\"100140\",\"isoCode\":\"MUS\",\"name\":\"Mauritius\",\"countryCode\":\"MU\",\"status\":\"Active\",\"dialCode\":\"+230\",\"currencyCode\":\"MUR\",\"currencySymbol\":\"₨\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:56.122+0530\"},{\"id\":142,\"code\":\"100141\",\"isoCode\":\"MYT\",\"name\":\"Mayotte\",\"countryCode\":\"YT\",\"status\":\"Active\",\"dialCode\":\"+262\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:56.403+0530\"},{\"id\":143,\"code\":\"100142\",\"isoCode\":\"MEX\",\"name\":\"Mexico\",\"countryCode\":\"MX\",\"status\":\"Active\",\"dialCode\":\"+52\",\"mobileLength\":\"10\",\"currencyCode\":\"MXN\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:56.690+0530\"},{\"id\":144,\"code\":\"100143\",\"isoCode\":\"FSM\",\"name\":\"Micronesia (Federated States of)\",\"countryCode\":\"FM\",\"status\":\"Active\",\"dialCode\":\"+691\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:56.965+0530\"},{\"id\":145,\"code\":\"100144\",\"isoCode\":\"MDA\",\"name\":\"Moldova (Republic of)\",\"countryCode\":\"MD\",\"status\":\"Active\",\"dialCode\":\"+373\",\"mobileLength\":\"8\",\"currencyCode\":\"MDL\",\"currencySymbol\":\"L\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:57.252+0530\"},{\"id\":146,\"code\":\"100145\",\"isoCode\":\"MCO\",\"name\":\"Monaco\",\"countryCode\":\"MC\",\"status\":\"Active\",\"dialCode\":\"+377\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:57.534+0530\"},{\"id\":147,\"code\":\"100146\",\"isoCode\":\"MNG\",\"name\":\"Mongolia\",\"countryCode\":\"MN\",\"status\":\"Active\",\"dialCode\":\"+976\",\"currencyCode\":\"MNT\",\"currencySymbol\":\"₮\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:57.816+0530\"},{\"id\":148,\"code\":\"100147\",\"isoCode\":\"MNE\",\"name\":\"Montenegro\",\"countryCode\":\"ME\",\"status\":\"Active\",\"dialCode\":\"+382\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:58.109+0530\"},{\"id\":149,\"code\":\"100148\",\"isoCode\":\"MSR\",\"name\":\"Montserrat\",\"countryCode\":\"MS\",\"status\":\"Active\",\"dialCode\":\"+1664\",\"mobileLength\":\"7\",\"currencyCode\":\"XCD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:58.387+0530\"},{\"id\":150,\"code\":\"100149\",\"isoCode\":\"MAR\",\"name\":\"Morocco\",\"countryCode\":\"MA\",\"status\":\"Active\",\"dialCode\":\"+212\",\"mobileLength\":\"9\",\"currencyCode\":\"MAD\",\"currencySymbol\":\"د.م.\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:58.674+0530\"},{\"id\":151,\"code\":\"100150\",\"isoCode\":\"MOZ\",\"name\":\"Mozambique\",\"countryCode\":\"MZ\",\"status\":\"Active\",\"dialCode\":\"+258\",\"mobileLength\":\"9\",\"currencyCode\":\"MZN\",\"currencySymbol\":\"MT\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:58.956+0530\"},{\"id\":152,\"code\":\"100151\",\"isoCode\":\"MMR\",\"name\":\"Myanmar\",\"countryCode\":\"MM\",\"status\":\"Active\",\"dialCode\":\"+95\",\"mobileLength\":\"10\",\"currencyCode\":\"MMK\",\"currencySymbol\":\"Ks\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:59.231+0530\"},{\"id\":153,\"code\":\"100152\",\"isoCode\":\"NAM\",\"name\":\"Namibia\",\"countryCode\":\"NA\",\"status\":\"Active\",\"dialCode\":\"+264\",\"currencyCode\":\"NAD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:59.523+0530\"},{\"id\":154,\"code\":\"100153\",\"isoCode\":\"NRU\",\"name\":\"Nauru\",\"countryCode\":\"NR\",\"status\":\"Active\",\"dialCode\":\"+674\",\"mobileLength\":\"7\",\"currencyCode\":\"AUD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:59.804+0530\"},{\"id\":155,\"code\":\"100154\",\"isoCode\":\"NPL\",\"name\":\"Nepal\",\"countryCode\":\"NP\",\"status\":\"Active\",\"dialCode\":\"+977\",\"mobileLength\":\"10\",\"currencyCode\":\"NPR\",\"currencySymbol\":\"₨\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:00.085+0530\"},{\"id\":156,\"code\":\"100155\",\"isoCode\":\"NLD\",\"name\":\"Netherlands\",\"countryCode\":\"NL\",\"status\":\"Active\",\"dialCode\":\"+31\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:00.368+0530\"},{\"id\":157,\"code\":\"100156\",\"isoCode\":\"NCL\",\"name\":\"New Caledonia\",\"countryCode\":\"NC\",\"status\":\"Active\",\"dialCode\":\"+687\",\"currencyCode\":\"XPF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:00.640+0530\"},{\"id\":158,\"code\":\"100157\",\"isoCode\":\"NZL\",\"name\":\"New Zealand\",\"countryCode\":\"NZ\",\"status\":\"Active\",\"dialCode\":\"+64\",\"currencyCode\":\"NZD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:00.917+0530\"},{\"id\":159,\"code\":\"100158\",\"isoCode\":\"NIC\",\"name\":\"Nicaragua\",\"countryCode\":\"NI\",\"status\":\"Active\",\"dialCode\":\"+505\",\"mobileLength\":\"8\",\"currencyCode\":\"NIO\",\"currencySymbol\":\"C$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:01.212+0530\"},{\"id\":160,\"code\":\"100159\",\"isoCode\":\"NER\",\"name\":\"Niger\",\"countryCode\":\"NE\",\"status\":\"Active\",\"dialCode\":\"+227\",\"mobileLength\":\"8\",\"currencyCode\":\"XOF\",\"currencySymbol\":\"CFA\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:01.508+0530\"},{\"id\":161,\"code\":\"100160\",\"isoCode\":\"NGA\",\"name\":\"Nigeria\",\"countryCode\":\"NG\",\"status\":\"Active\",\"dialCode\":\"+234\",\"mobileLength\":\"10\",\"currencyCode\":\"NGN\",\"currencySymbol\":\"₦\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:01.794+0530\"},{\"id\":162,\"code\":\"100161\",\"isoCode\":\"NIU\",\"name\":\"Niue\",\"countryCode\":\"NU\",\"status\":\"Active\",\"dialCode\":\"+683\",\"currencyCode\":\"NZD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:02.072+0530\"},{\"id\":163,\"code\":\"100162\",\"isoCode\":\"NFK\",\"name\":\"Norfolk Island\",\"countryCode\":\"NF\",\"status\":\"Active\",\"dialCode\":\"+672\",\"currencyCode\":\"AUD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:02.366+0530\"},{\"id\":164,\"code\":\"100163\",\"isoCode\":\"MNP\",\"name\":\"Northern Mariana Islands\",\"countryCode\":\"MP\",\"status\":\"Active\",\"dialCode\":\"+1670\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:02.640+0530\"},{\"id\":165,\"code\":\"100164\",\"isoCode\":\"NOR\",\"name\":\"Norway\",\"countryCode\":\"NO\",\"status\":\"Active\",\"dialCode\":\"+47\",\"currencyCode\":\"NOK\",\"currencySymbol\":\"kr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:02.915+0530\"},{\"id\":166,\"code\":\"100165\",\"isoCode\":\"OMN\",\"name\":\"Oman\",\"countryCode\":\"OM\",\"status\":\"Active\",\"dialCode\":\"+968\",\"currencyCode\":\"OMR\",\"currencySymbol\":\"ر.ع.\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:03.207+0530\"},{\"id\":167,\"code\":\"100166\",\"isoCode\":\"PAK\",\"name\":\"Pakistan\",\"countryCode\":\"PK\",\"status\":\"Active\",\"dialCode\":\"+92\",\"mobileLength\":\"10\",\"currencyCode\":\"PKR\",\"currencySymbol\":\"₨\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:03.481+0530\"},{\"id\":168,\"code\":\"100167\",\"isoCode\":\"PLW\",\"name\":\"Palau\",\"countryCode\":\"PW\",\"status\":\"Active\",\"dialCode\":\"+680\",\"currencyCode\":\"(none)\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:03.763+0530\"},{\"id\":169,\"code\":\"100168\",\"isoCode\":\"PSE\",\"name\":\"Palestine, State of\",\"countryCode\":\"PS\",\"status\":\"Active\",\"dialCode\":\"+970\",\"currencyCode\":\"ILS\",\"currencySymbol\":\"₪\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:04.033+0530\"},{\"id\":170,\"code\":\"100169\",\"isoCode\":\"PAN\",\"name\":\"Panama\",\"countryCode\":\"PA\",\"status\":\"Active\",\"dialCode\":\"+507\",\"mobileLength\":\"8\",\"currencyCode\":\"PAB\",\"currencySymbol\":\"B/.\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:04.306+0530\"},{\"id\":171,\"code\":\"100170\",\"isoCode\":\"PNG\",\"name\":\"Papua New Guinea\",\"countryCode\":\"PG\",\"status\":\"Active\",\"dialCode\":\"+675\",\"mobileLength\":\"8\",\"currencyCode\":\"PGK\",\"currencySymbol\":\"K\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:04.594+0530\"},{\"id\":172,\"code\":\"100171\",\"isoCode\":\"PRY\",\"name\":\"Paraguay\",\"countryCode\":\"PY\",\"status\":\"Active\",\"dialCode\":\"+595\",\"mobileLength\":\"9\",\"currencyCode\":\"PYG\",\"currencySymbol\":\"₲\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:04.883+0530\"},{\"id\":173,\"code\":\"100172\",\"isoCode\":\"PER\",\"name\":\"Peru\",\"countryCode\":\"PE\",\"status\":\"Active\",\"dialCode\":\"+51\",\"mobileLength\":\"9\",\"currencyCode\":\"PEN\",\"currencySymbol\":\"S/.\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:05.176+0530\"},{\"id\":174,\"code\":\"100173\",\"isoCode\":\"PHL\",\"name\":\"Philippines\",\"countryCode\":\"PH\",\"status\":\"Active\",\"dialCode\":\"+63\",\"mobileLength\":\"10\",\"currencyCode\":\"PHP\",\"currencySymbol\":\"₱\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:05.488+0530\"},{\"id\":175,\"code\":\"100174\",\"isoCode\":\"PCN\",\"name\":\"Pitcairn\",\"countryCode\":\"PN\",\"status\":\"Active\",\"dialCode\":\"+64\",\"currencyCode\":\"NZD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:05.769+0530\"},{\"id\":176,\"code\":\"100175\",\"isoCode\":\"POL\",\"name\":\"Poland\",\"countryCode\":\"PL\",\"status\":\"Active\",\"dialCode\":\"+48\",\"mobileLength\":\"9\",\"currencyCode\":\"PLN\",\"currencySymbol\":\"zł\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:06.048+0530\"},{\"id\":177,\"code\":\"100176\",\"isoCode\":\"PRT\",\"name\":\"Portugal\",\"countryCode\":\"PT\",\"status\":\"Active\",\"dialCode\":\"+351\",\"mobileLength\":\"9\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:06.332+0530\"},{\"id\":178,\"code\":\"100177\",\"isoCode\":\"PRI\",\"name\":\"Puerto Rico\",\"countryCode\":\"PR\",\"status\":\"Active\",\"dialCode\":\"+1787\",\"mobileLength\":\"7\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:06.611+0530\"},{\"id\":179,\"code\":\"100178\",\"isoCode\":\"QAT\",\"name\":\"Qatar\",\"countryCode\":\"QA\",\"status\":\"Active\",\"dialCode\":\"+974\",\"currencyCode\":\"QAR\",\"currencySymbol\":\"ر.ق\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:06.891+0530\"},{\"id\":184,\"code\":\"100183\",\"isoCode\":\"REU\",\"name\":\"Réunion\",\"countryCode\":\"RE\",\"status\":\"Active\",\"dialCode\":\"+262\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:08.291+0530\"},{\"id\":181,\"code\":\"100180\",\"isoCode\":\"ROU\",\"name\":\"Romania\",\"countryCode\":\"RO\",\"status\":\"Active\",\"dialCode\":\"+40\",\"mobileLength\":\"9\",\"currencyCode\":\"RON\",\"currencySymbol\":\"lei\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:07.452+0530\"},{\"id\":182,\"code\":\"100181\",\"isoCode\":\"RUS\",\"name\":\"Russian Federation\",\"countryCode\":\"RU\",\"status\":\"Active\",\"dialCode\":\"+7\",\"mobileLength\":\"10\",\"currencyCode\":\"RUB\",\"currencySymbol\":\"₽\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:07.724+0530\"},{\"id\":183,\"code\":\"100182\",\"isoCode\":\"RWA\",\"name\":\"Rwanda\",\"countryCode\":\"RW\",\"status\":\"Active\",\"dialCode\":\"+250\",\"mobileLength\":\"9\",\"currencyCode\":\"RWF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:08.006+0530\"},{\"id\":185,\"code\":\"100184\",\"isoCode\":\"BLM\",\"name\":\"Saint Barthélemy\",\"countryCode\":\"BL\",\"status\":\"Active\",\"dialCode\":\"+590\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:08.570+0530\"},{\"id\":186,\"code\":\"100185\",\"isoCode\":\"SHN\",\"name\":\"Saint Helena, Ascension and Tristan da Cunha\",\"countryCode\":\"SH\",\"status\":\"Active\",\"dialCode\":\"+290\",\"currencyCode\":\"SHP\",\"currencySymbol\":\"£\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:08.852+0530\"},{\"id\":187,\"code\":\"100186\",\"isoCode\":\"KNA\",\"name\":\"Saint Kitts and Nevis\",\"countryCode\":\"KN\",\"status\":\"Active\",\"dialCode\":\"+1869\",\"mobileLength\":\"7\",\"currencyCode\":\"XCD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:09.150+0530\"},{\"id\":188,\"code\":\"100187\",\"isoCode\":\"LCA\",\"name\":\"Saint Lucia\",\"countryCode\":\"LC\",\"status\":\"Active\",\"dialCode\":\"+1758\",\"mobileLength\":\"7\",\"currencyCode\":\"XCD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:09.423+0530\"},{\"id\":189,\"code\":\"100188\",\"isoCode\":\"MAF\",\"name\":\"Saint Martin (French part)\",\"countryCode\":\"MF\",\"status\":\"Active\",\"dialCode\":\"+590\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:09.701+0530\"},{\"id\":190,\"code\":\"100189\",\"isoCode\":\"SPM\",\"name\":\"Saint Pierre and Miquelon\",\"countryCode\":\"PM\",\"status\":\"Active\",\"dialCode\":\"+508\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:09.977+0530\"},{\"id\":191,\"code\":\"100190\",\"isoCode\":\"VCT\",\"name\":\"Saint Vincent and the Grenadines\",\"countryCode\":\"VC\",\"status\":\"Active\",\"dialCode\":\"+1784\",\"mobileLength\":\"7\",\"currencyCode\":\"XCD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:10.254+0530\"},{\"id\":192,\"code\":\"100191\",\"isoCode\":\"WSM\",\"name\":\"Samoa\",\"countryCode\":\"WS\",\"status\":\"Active\",\"dialCode\":\"+685\",\"mobileLength\":\"7\",\"currencyCode\":\"WST\",\"currencySymbol\":\"T\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:10.539+0530\"},{\"id\":193,\"code\":\"100192\",\"isoCode\":\"SMR\",\"name\":\"San Marino\",\"countryCode\":\"SM\",\"status\":\"Active\",\"dialCode\":\"+378\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:10.823+0530\"},{\"id\":194,\"code\":\"100193\",\"isoCode\":\"STP\",\"name\":\"Sao Tome and Principe\",\"countryCode\":\"ST\",\"status\":\"Active\",\"dialCode\":\"+239\",\"currencyCode\":\"STD\",\"currencySymbol\":\"Db\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:11.108+0530\"},{\"id\":195,\"code\":\"100194\",\"isoCode\":\"SAU\",\"name\":\"Saudi Arabia\",\"countryCode\":\"SA\",\"status\":\"Active\",\"dialCode\":\"+966\",\"currencyCode\":\"SAR\",\"currencySymbol\":\"ر.س\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:11.424+0530\"},{\"id\":196,\"code\":\"100195\",\"isoCode\":\"SEN\",\"name\":\"Senegal\",\"countryCode\":\"SN\",\"status\":\"Active\",\"dialCode\":\"+221\",\"mobileLength\":\"9\",\"currencyCode\":\"XOF\",\"currencySymbol\":\"CFA\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:11.705+0530\"},{\"id\":197,\"code\":\"100196\",\"isoCode\":\"SRB\",\"name\":\"Serbia\",\"countryCode\":\"RS\",\"status\":\"Active\",\"dialCode\":\"+381\",\"currencyCode\":\"RSD\",\"currencySymbol\":\"дин.\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:11.975+0530\"},{\"id\":198,\"code\":\"100197\",\"isoCode\":\"SYC\",\"name\":\"Seychelles\",\"countryCode\":\"SC\",\"status\":\"Active\",\"dialCode\":\"+248\",\"currencyCode\":\"SCR\",\"currencySymbol\":\"₨\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:12.250+0530\"},{\"id\":199,\"code\":\"100198\",\"isoCode\":\"SLE\",\"name\":\"Sierra Leone\",\"countryCode\":\"SL\",\"status\":\"Active\",\"dialCode\":\"+232\",\"mobileLength\":\"8\",\"currencyCode\":\"SLL\",\"currencySymbol\":\"Le\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:12.526+0530\"},{\"id\":200,\"code\":\"100199\",\"isoCode\":\"SGP\",\"name\":\"Singapore\",\"countryCode\":\"SG\",\"status\":\"Active\",\"dialCode\":\"+65\",\"mobileLength\":\"8\",\"currencyCode\":\"BND\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:12.809+0530\"},{\"id\":201,\"code\":\"100200\",\"isoCode\":\"SXM\",\"name\":\"Sint Maarten (Dutch part)\",\"countryCode\":\"SX\",\"status\":\"Active\",\"dialCode\":\"+1721\",\"currencyCode\":\"ANG\",\"currencySymbol\":\"ƒ\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:13.090+0530\"},{\"id\":202,\"code\":\"100201\",\"isoCode\":\"SVK\",\"name\":\"Slovakia\",\"countryCode\":\"SK\",\"status\":\"Active\",\"dialCode\":\"+421\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:13.377+0530\"},{\"id\":203,\"code\":\"100202\",\"isoCode\":\"SVN\",\"name\":\"Slovenia\",\"countryCode\":\"SI\",\"status\":\"Active\",\"dialCode\":\"+386\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:13.657+0530\"},{\"id\":204,\"code\":\"100203\",\"isoCode\":\"SLB\",\"name\":\"Solomon Islands\",\"countryCode\":\"SB\",\"status\":\"Active\",\"dialCode\":\"+677\",\"currencyCode\":\"SBD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:13.939+0530\"},{\"id\":205,\"code\":\"100204\",\"isoCode\":\"SOM\",\"name\":\"Somalia\",\"countryCode\":\"SO\",\"status\":\"Active\",\"dialCode\":\"+252\",\"currencyCode\":\"SOS\",\"currencySymbol\":\"Sh\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:14.216+0530\"},{\"id\":206,\"code\":\"100205\",\"isoCode\":\"ZAF\",\"name\":\"South Africa\",\"countryCode\":\"ZA\",\"status\":\"Active\",\"dialCode\":\"+27\",\"mobileLength\":\"9\",\"currencyCode\":\"ZAR\",\"currencySymbol\":\"R\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:14.512+0530\"},{\"id\":207,\"code\":\"100206\",\"isoCode\":\"SGS\",\"name\":\"South Georgia and the South Sandwich Islands\",\"countryCode\":\"GS\",\"status\":\"Active\",\"dialCode\":\"+500\",\"currencyCode\":\"GBP\",\"currencySymbol\":\"£\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:14.788+0530\"},{\"id\":208,\"code\":\"100207\",\"isoCode\":\"SSD\",\"name\":\"South Sudan\",\"countryCode\":\"SS\",\"status\":\"Active\",\"dialCode\":\"+211\",\"currencyCode\":\"SSP\",\"currencySymbol\":\"£\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:15.067+0530\"},{\"id\":209,\"code\":\"100208\",\"isoCode\":\"ESP\",\"name\":\"Spain\",\"countryCode\":\"ES\",\"status\":\"Active\",\"dialCode\":\"+34\",\"mobileLength\":\"9\",\"currencyCode\":\"EUR\",\"currencySymbol\":\"€\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:15.346+0530\"},{\"id\":210,\"code\":\"100209\",\"isoCode\":\"LKA\",\"name\":\"Sri Lanka\",\"countryCode\":\"LK\",\"status\":\"Active\",\"dialCode\":\"+94\",\"mobileLength\":\"9\",\"currencyCode\":\"LKR\",\"currencySymbol\":\"Rs\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:15.630+0530\"},{\"id\":211,\"code\":\"100210\",\"isoCode\":\"SDN\",\"name\":\"Sudan\",\"countryCode\":\"SD\",\"status\":\"Active\",\"dialCode\":\"+249\",\"currencyCode\":\"SDG\",\"currencySymbol\":\"ج.س.\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:15.899+0530\"},{\"id\":212,\"code\":\"100211\",\"isoCode\":\"SUR\",\"name\":\"Suriname\",\"countryCode\":\"SR\",\"status\":\"Active\",\"dialCode\":\"+597\",\"mobileLength\":\"7\",\"currencyCode\":\"SRD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:16.194+0530\"},{\"id\":213,\"code\":\"100212\",\"isoCode\":\"SJM\",\"name\":\"Svalbard and Jan Mayen\",\"countryCode\":\"SJ\",\"status\":\"Active\",\"dialCode\":\"+4779\",\"currencyCode\":\"NOK\",\"currencySymbol\":\"kr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:16.488+0530\"},{\"id\":70,\"code\":\"100069\",\"isoCode\":\"SWZ\",\"name\":\"Swaziland\",\"countryCode\":\"SZ\",\"status\":\"Active\",\"dialCode\":\"+268\",\"mobileLength\":\"8\",\"currencyCode\":\"SZL\",\"currencySymbol\":\"L\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:04:35.974+0530\"},{\"id\":214,\"code\":\"100213\",\"isoCode\":\"SWE\",\"name\":\"Sweden\",\"countryCode\":\"SE\",\"status\":\"Active\",\"dialCode\":\"+46\",\"currencyCode\":\"SEK\",\"currencySymbol\":\"kr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:16.772+0530\"},{\"id\":215,\"code\":\"100214\",\"isoCode\":\"CHE\",\"name\":\"Switzerland\",\"countryCode\":\"CH\",\"status\":\"Active\",\"dialCode\":\"+41\",\"currencyCode\":\"CHF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:17.044+0530\"},{\"id\":216,\"code\":\"100215\",\"isoCode\":\"SYR\",\"name\":\"Syrian Arab Republic\",\"countryCode\":\"SY\",\"status\":\"Active\",\"dialCode\":\"+963\",\"currencyCode\":\"SYP\",\"currencySymbol\":\"£\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:17.315+0530\"},{\"id\":217,\"code\":\"100216\",\"isoCode\":\"TWN\",\"name\":\"Taiwan\",\"countryCode\":\"TW\",\"status\":\"Active\",\"dialCode\":\"+886\",\"currencyCode\":\"TWD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:17.583+0530\"},{\"id\":218,\"code\":\"100217\",\"isoCode\":\"TJK\",\"name\":\"Tajikistan\",\"countryCode\":\"TJ\",\"status\":\"Active\",\"dialCode\":\"+992\",\"mobileLength\":\"9\",\"currencyCode\":\"TJS\",\"currencySymbol\":\"ЅМ\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:17.863+0530\"},{\"id\":219,\"code\":\"100218\",\"isoCode\":\"TZA\",\"name\":\"Tanzania, United Republic of\",\"countryCode\":\"TZ\",\"status\":\"Active\",\"dialCode\":\"+255\",\"mobileLength\":\"9\",\"currencyCode\":\"TZS\",\"currencySymbol\":\"Sh\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:18.137+0530\"},{\"id\":220,\"code\":\"100219\",\"isoCode\":\"THA\",\"name\":\"Thailand\",\"countryCode\":\"TH\",\"status\":\"Active\",\"dialCode\":\"+66\",\"mobileLength\":\"9\",\"currencyCode\":\"THB\",\"currencySymbol\":\"฿\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:18.412+0530\"},{\"id\":221,\"code\":\"100220\",\"isoCode\":\"TLS\",\"name\":\"Timor-Leste\",\"countryCode\":\"TL\",\"status\":\"Active\",\"dialCode\":\"+670\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:18.688+0530\"},{\"id\":222,\"code\":\"100221\",\"isoCode\":\"TGO\",\"name\":\"Togo\",\"countryCode\":\"TG\",\"status\":\"Active\",\"dialCode\":\"+228\",\"mobileLength\":\"8\",\"currencyCode\":\"XOF\",\"currencySymbol\":\"CFA\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:18.963+0530\"},{\"id\":223,\"code\":\"100222\",\"isoCode\":\"TKL\",\"name\":\"Tokelau\",\"countryCode\":\"TK\",\"status\":\"Active\",\"dialCode\":\"+690\",\"currencyCode\":\"NZD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:19.238+0530\"},{\"id\":224,\"code\":\"100223\",\"isoCode\":\"TON\",\"name\":\"Tonga\",\"countryCode\":\"TO\",\"status\":\"Active\",\"dialCode\":\"+676\",\"mobileLength\":\"7\",\"currencyCode\":\"TOP\",\"currencySymbol\":\"T$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:19.516+0530\"},{\"id\":225,\"code\":\"100224\",\"isoCode\":\"TTO\",\"name\":\"Trinidad and Tobago\",\"countryCode\":\"TT\",\"status\":\"Active\",\"dialCode\":\"+1868\",\"mobileLength\":\"7\",\"currencyCode\":\"TTD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:19.799+0530\"},{\"id\":226,\"code\":\"100225\",\"isoCode\":\"TUN\",\"name\":\"Tunisia\",\"countryCode\":\"TN\",\"status\":\"Active\",\"dialCode\":\"+216\",\"mobileLength\":\"8\",\"currencyCode\":\"TND\",\"currencySymbol\":\"د.ت\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:20.089+0530\"},{\"id\":227,\"code\":\"100226\",\"isoCode\":\"TUR\",\"name\":\"Turkey\",\"countryCode\":\"TR\",\"status\":\"Active\",\"dialCode\":\"+90\",\"mobileLength\":\"10\",\"currencyCode\":\"TRY\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:20.430+0530\"},{\"id\":228,\"code\":\"100227\",\"isoCode\":\"TKM\",\"name\":\"Turkmenistan\",\"countryCode\":\"TM\",\"status\":\"Active\",\"dialCode\":\"+993\",\"currencyCode\":\"TMT\",\"currencySymbol\":\"m\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:20.705+0530\"},{\"id\":229,\"code\":\"100228\",\"isoCode\":\"TCA\",\"name\":\"Turks and Caicos Islands\",\"countryCode\":\"TC\",\"status\":\"Active\",\"dialCode\":\"+1649\",\"mobileLength\":\"7\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:20.991+0530\"},{\"id\":230,\"code\":\"100229\",\"isoCode\":\"TUV\",\"name\":\"Tuvalu\",\"countryCode\":\"TV\",\"status\":\"Active\",\"dialCode\":\"+688\",\"currencyCode\":\"AUD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:21.269+0530\"},{\"id\":231,\"code\":\"100230\",\"isoCode\":\"UGA\",\"name\":\"Uganda\",\"countryCode\":\"UG\",\"status\":\"Active\",\"dialCode\":\"+256\",\"mobileLength\":\"9\",\"currencyCode\":\"UGX\",\"currencySymbol\":\"Sh\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:21.557+0530\"},{\"id\":232,\"code\":\"100231\",\"isoCode\":\"UKR\",\"name\":\"Ukraine\",\"countryCode\":\"UA\",\"status\":\"Active\",\"dialCode\":\"+380\",\"mobileLength\":\"9\",\"currencyCode\":\"UAH\",\"currencySymbol\":\"₴\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:21.835+0530\"},{\"id\":233,\"code\":\"100232\",\"isoCode\":\"ARE\",\"name\":\"United Arab Emirates\",\"countryCode\":\"AE\",\"status\":\"Active\",\"dialCode\":\"+971\",\"mobileLength\":\"9\",\"currencyCode\":\"AED\",\"currencySymbol\":\"د.إ\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:22.109+0530\"},{\"id\":234,\"code\":\"100233\",\"isoCode\":\"GBR\",\"name\":\"United Kingdom of Great Britain and Northern Ireland\",\"countryCode\":\"GB\",\"status\":\"Active\",\"dialCode\":\"+44\",\"currencyCode\":\"GBP\",\"currencySymbol\":\"£\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:22.388+0530\"},{\"id\":235,\"code\":\"100234\",\"isoCode\":\"UMI\",\"name\":\"United States Minor Outlying Islands\",\"countryCode\":\"UM\",\"status\":\"Active\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:22.675+0530\"},{\"id\":236,\"code\":\"100235\",\"isoCode\":\"USA\",\"name\":\"United States of America\",\"countryCode\":\"US\",\"status\":\"Active\",\"dialCode\":\"+1\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:22.957+0530\"},{\"id\":237,\"code\":\"100236\",\"isoCode\":\"URY\",\"name\":\"Uruguay\",\"countryCode\":\"UY\",\"status\":\"Active\",\"dialCode\":\"+598\",\"currencyCode\":\"UYU\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:23.234+0530\"},{\"id\":238,\"code\":\"100237\",\"isoCode\":\"UZB\",\"name\":\"Uzbekistan\",\"countryCode\":\"UZ\",\"status\":\"Active\",\"dialCode\":\"+998\",\"mobileLength\":\"9\",\"currencyCode\":\"UZS\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:23.515+0530\"},{\"id\":239,\"code\":\"100238\",\"isoCode\":\"VUT\",\"name\":\"Vanuatu\",\"countryCode\":\"VU\",\"status\":\"Active\",\"dialCode\":\"+678\",\"mobileLength\":\"7\",\"currencyCode\":\"VUV\",\"currencySymbol\":\"Vt\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:23.801+0530\"},{\"id\":240,\"code\":\"100239\",\"isoCode\":\"VEN\",\"name\":\"Venezuela (Bolivarian Republic of)\",\"countryCode\":\"VE\",\"status\":\"Active\",\"dialCode\":\"+58\",\"mobileLength\":\"10\",\"currencyCode\":\"VEF\",\"currencySymbol\":\"Bs F\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:24.080+0530\"},{\"id\":241,\"code\":\"100240\",\"isoCode\":\"VNM\",\"name\":\"Viet Nam\",\"countryCode\":\"VN\",\"status\":\"Active\",\"dialCode\":\"+84\",\"mobileLength\":\"10\",\"currencyCode\":\"VND\",\"currencySymbol\":\"₫\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:24.358+0530\"},{\"id\":242,\"code\":\"100241\",\"isoCode\":\"VGB\",\"name\":\"Virgin Islands (British)\",\"countryCode\":\"VG\",\"status\":\"Active\",\"dialCode\":\"+1284\",\"mobileLength\":\"7\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:24.640+0530\"},{\"id\":243,\"code\":\"100242\",\"isoCode\":\"VIR\",\"name\":\"Virgin Islands (U.S.)\",\"countryCode\":\"VI\",\"status\":\"Active\",\"dialCode\":\"+1340\",\"currencyCode\":\"USD\",\"currencySymbol\":\"$\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:24.925+0530\"},{\"id\":244,\"code\":\"100243\",\"isoCode\":\"WLF\",\"name\":\"Wallis and Futuna\",\"countryCode\":\"WF\",\"status\":\"Active\",\"dialCode\":\"+681\",\"currencyCode\":\"XPF\",\"currencySymbol\":\"Fr\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:25.210+0530\"},{\"id\":245,\"code\":\"100244\",\"isoCode\":\"ESH\",\"name\":\"Western Sahara\",\"countryCode\":\"EH\",\"status\":\"Active\",\"dialCode\":\"+212\",\"currencyCode\":\"MAD\",\"currencySymbol\":\"د.م.\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:25.488+0530\"},{\"id\":246,\"code\":\"100245\",\"isoCode\":\"YEM\",\"name\":\"Yemen\",\"countryCode\":\"YE\",\"status\":\"Active\",\"dialCode\":\"+967\",\"mobileLength\":\"10\",\"currencyCode\":\"YER\",\"currencySymbol\":\"﷼\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:25.788+0530\"},{\"id\":247,\"code\":\"100246\",\"isoCode\":\"ZMB\",\"name\":\"Zambia\",\"countryCode\":\"ZM\",\"status\":\"Active\",\"dialCode\":\"+260\",\"mobileLength\":\"9\",\"currencyCode\":\"ZMW\",\"currencySymbol\":\"ZK\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:26.061+0530\"},{\"id\":248,\"code\":\"100247\",\"isoCode\":\"ZWE\",\"name\":\"Zimbabwe\",\"countryCode\":\"ZW\",\"status\":\"Active\",\"dialCode\":\"+263\",\"mobileLength\":\"9\",\"currencyCode\":\"BWP\",\"currencySymbol\":\"P\",\"subscriberAllowed\":false,\"creationDate\":\"2020-08-11T15:05:26.590+0530\"}]}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");


                    if (resultCode.equalsIgnoreCase("0")) {


                        arrayList_senderCountryName = new ArrayList<>();
                        arrayList_senderCountryCode = new ArrayList<>();

                       // arrayList_senderCountryName.add(getString(R.string.valid_select_country));
                       // arrayList_senderCountryCode.add(getString(R.string.valid_select_country));

                        arrayList_senderCountryName.add("Guinea");
                        arrayList_senderCountryCode.add("Guinea");



                        //Toast.makeText(LocalRemittance.this, resultDescription, Toast.LENGTH_LONG).show();

//                        JSONArray jsonArray_countryList = jsonObject.getJSONArray("countryList");
//                        for (int i = 0; i < jsonArray_countryList.length(); i++) {
//
//                            JSONObject jsonObject2 = jsonArray_countryList.getJSONObject(i);
//
//                            int country_id = jsonObject2.getInt("id");
//                            String country_code = jsonObject2.getString("code");
//                            String country_isoCode = jsonObject2.getString("isoCode");
//                            String countryCode_from_countryList_str = jsonObject2.getString("name");
//                            String country_status = jsonObject2.getString("status");
//
//
//                            selectCountryCode = String.valueOf(country_id);
//
//                            arrayList_senderCountryCode.add(selectCountryCode);
//                            arrayList_senderCountryName.add(countryCode_from_countryList_str);
//
//                        }

                        CustomeBaseAdapterAllCountry adapterGender4= new CustomeBaseAdapterAllCountry(AirtimePurchases.this, arrayList_senderCountryName);
                        spinner_senderCountry.setAdapter(adapterGender4);
                        spinner_senderCountry.setEnabled(false);


                        api_master_PRODUCTTYPE();

                    } else {
                        Toast.makeText(AirtimePurchases.this, resultDescription, Toast.LENGTH_LONG).show();
                        //  finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(AirtimePurchases.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(AirtimePurchases.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void api_product_allByCriteria_serviceCategoryCode(String serviceCode_from_operatorList) {


      //  ewallet/api/v1/product/allByCriteria?serviceCategoryCode=100021&operatorCode=100051
        API.GET_TRANSFER_DETAILS("ewallet/api/v1/product/allByCriteria?serviceCategoryCode=100021&operatorCode="+serviceCode_from_operatorList,languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {


                   // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1947425\",\"requestTime\":\"Thu Nov 04 13:37:49 IST 2021\",\"responseTime\":\"Thu Nov 04 13:37:49 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"pageable\":{\"totalRecords\":1},\"productList\":[{\"id\":29,\"code\":\"100030\",\"serviceCategoryCode\":\"100021\",\"serviceCategoryName\":\"Mobile Prepaid\",\"operatorCode\":\"100051\",\"operatorName\":\"MTN\",\"productTypeCode\":\"100001\",\"productTypeName\":\"Flexi\",\"name\":\"Recharge MTN\",\"value\":0.0,\"description\":\"Recharge MTN\",\"minValue\":1.0,\"maxValue\":1000000.0,\"status\":\"Active\",\"state\":\"Approved\",\"creationDate\":\"2021-08-17T19:21:50.442+0530\",\"vendorProductCode\":\"P_1XMGSSA\"}]}");

                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");

                    if(resultCode.equalsIgnoreCase("0")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("productList");

                        for(int i=0;i<jsonArray.length();i++) {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);


                              code_from_product_allByCriteria = jsonObject2.getString("code");
                            String  serviceCategoryCode_from_product_allByCriteria = jsonObject2.getString("serviceCategoryCode");
                            String  serviceCategoryName_from_product_allByCriteria = jsonObject2.getString("serviceCategoryName");
                            String  operatorCode_from_product_allByCriteria = jsonObject2.getString("operatorCode");

                        }



                    }

                    else {
                        Toast.makeText(AirtimePurchases.this, resultDescription, Toast.LENGTH_LONG).show();
                      //  finish();
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(AirtimePurchases.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(AirtimePurchases.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    private void api_exchange_rate() {



      //  MyApplication.showloader(AirtimePurchases.this, getString(R.string.getting_user_info));

        API.GET_TRANSFER_DETAILS("ewallet/api/v1/exchangeRate/getAmountDetails?sendCurrencyCode=" + currencyCode_agent +
                        "&receiveCurrencyCode="+
                        currencyCode_agent+"&sendCountryCode=" + countryCode_agent + "&receiveCountryCode="+"100092"+
                        "&currencyValue=" + amountstr + "&channelTypeCode=100002&serviceCode=" +
                        serviceCode_from_serviceCategory + "&serviceCategoryCode=" + serviceCategoryCode_from_serviceCategory +
                        "&serviceProviderCode=" +
                        serviceProviderCode_from_serviceCategory + "&walletOwnerCode=" + walletOwnerCode_mssis_agent +"&productCode="+code_from_product_allByCriteria,languageToUse,

    //  API.GET_TRANSFER_DETAILS("ewallet/api/v1/exchangeRate/getAmountDetails?sendCurrencyCode=100062&receiveCurrencyCode=" +
//                "100062&sendCountryCode="+"100102"+"&receiveCountryCode="+"100102"+
//                "&currencyValue="+amountstr+"&channelTypeCode=100000&serviceCode="+
//                serviceCode_from_serviceCategory+"&serviceCategoryCode="+serviceCategoryCode_from_serviceCategory+
//                "&serviceProviderCode="+serviceProviderCode_from_serviceCategory+"&walletOwnerCode="+walletOwnerCode_mssis_agent+
//                "&productCode="+code_from_product_allByCriteria,languageToUse,

                new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    //JSONObject jsonObject = new JSONObject("{"transactionId":"1789322","requestTime":"Wed Oct 20 15:53:33 IST 2021","responseTime":"Wed Oct 20 15:53:33 IST 2021","resultCode":"0","resultDescription":"Transaction Successful","walletOwnerCountryCurrencyList":[{"id":7452,"code":"107451","walletOwnerCode":"1000002488","currencyCode":"100062","currencyName":"GNF","currencySymbol":"Fr","countryCurrencyCode":"100076","inBound":true,"outBound":true,"status":"Active"}]}");

                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");

                    if(resultCode.equalsIgnoreCase("0")) {



                        //Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();

                        JSONObject exchangeRate = jsonObject.getJSONObject("exchangeRate");

                        fees_amount = exchangeRate.getString("fee");


                        TextView rp_tv_excise_tax_l=findViewById(R.id.rp_tv_excise_tax_l);

                     /*   if(exchangeRate.has("receiverTax")){
                            tax_financial=MyApplication.addDecimal("0.00");
                        }*/



                        if(!exchangeRate.has("receiverTax")) {
                            if (exchangeRate.has("taxConfigurationList")) {
                                JSONArray jsonArray = exchangeRate.getJSONArray("taxConfigurationList");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    tax_financial = jsonObject2.getString("value");
                                            rp_tv_excise_tax_l.setText(MyApplication.getTaxString(jsonObject2.getString("taxTypeName")));
                                }
                            } else {

                                rp_tv_excise_tax_l.setText(MyApplication.getTaxString("TAX"));
                                tax_financial = exchangeRate.getString("value");
                            }
                        }

                        rp_tv_convertionrate.setText(currencySymbol_sender+" " +"0.0");




                        rp_tv_fees_reveiewPage.setText(currencySymbol_sender+" " +MyApplication.addDecimal(fees_amount+""));
                        rp_tv_excise_tax.setText(currencySymbol_sender+" " +MyApplication.addDecimal(Double.parseDouble(tax_financial)+""));
                       TextView receiptPage_tv_financialtaxl=findViewById(R.id.receiptPage_tv_financialtaxl);
                        receiptPage_tv_financialtaxl.setText(rp_tv_excise_tax_l.getText().toString());

                        receiptPage_tv_financialtax.setText(currencySymbol_sender+" " +MyApplication.addDecimal(tax_financial+""));
                        rp_tv_transactionAmount.setText(currencySymbol_sender+" " +MyApplication.addDecimal(amountstr+""));

                        tax_financial_double = Double.parseDouble(tax_financial);
                        amountstr_double = Double.parseDouble(amountstr);
                        fees_amount_double = Double.parseDouble(fees_amount);

                        totalAmount_double = tax_financial_double+amountstr_double+fees_amount_double;
                        totalAmount_str = String.valueOf(totalAmount_double);
                        rp_tv_amount_to_be_charge.setText(currencySymbol_sender+" " +MyApplication.addDecimal(totalAmount_str+""));

                        rp_tv_totalAmount.setText(currencySymbol_sender+" " + MyApplication.addDecimal(totalAmount_str+""));


                        ll_page_1.setVisibility(View.GONE);
                        ll_reviewPage.setVisibility(View.VISIBLE);
                        ll_receiptPage.setVisibility(View.GONE);
                        ll_successPage.setVisibility(View.GONE);
                        et_mpin.setText("");



                    }

                    else {
                        Toast.makeText(AirtimePurchases.this, resultDescription, Toast.LENGTH_LONG).show();
                      //  finish();
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(AirtimePurchases.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(AirtimePurchases.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void api_serviceProvider_serviceCategory_serviceCode() {


        //MyApplication.showloader(AirtimePurchases.this, getString(R.string.getting_user_info));

        // Hard Code Final Deepak

        API.GET_TRANSFER_DETAILS("ewallet/api/v1/serviceProvider/serviceCategory?serviceCode=100009&serviceCategoryCode=100021&status=Y", languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

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


                      //  api_product_allByCriteria_serviceCategoryCode(); //annu



                    } else {
                        Toast.makeText(AirtimePurchases.this, resultDescription, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    Toast.makeText(AirtimePurchases.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(AirtimePurchases.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    private void api_allByCriteria_msisdnPrefix() {

        String number = edittext_mobileNo.getText().toString();
        String firstTwodigits = number.substring(0,2);
        API.GET_TRANSFER_DETAILS("ewallet/api/v1/operator/allByCriteria?msisdnPrefix=224"+firstTwodigits+"&status=Y",languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                MyApplication.hideLoader();

                try {


                   // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1947422\",\"requestTime\":\"Thu Nov 04 13:37:44 IST 2021\",\"responseTime\":\"Thu Nov 04 13:37:44 IST 2021\",\"resultCode\":\"1085\",\"resultDescription\":\"Operator Not Found\"}\n");


                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {


                        MyApplication.showloader(AirtimePurchases.this, getString(R.string.please_wait));
                        confirm_reviewClick_textview.setEnabled(true);
                        confirm_reviewClick_textview.setClickable(true);


                        mpin_final_api();

                    } else {
                        confirm_reviewClick_textview.setEnabled(false);
                        confirm_reviewClick_textview.setClickable(false);

                        if(resultDescription.equalsIgnoreCase("Operator Not Found"))
                        {
                           // Toast.makeText(AirtimePurchases.this, resultDescription, Toast.LENGTH_LONG).show();
                            MyApplication.showloader(AirtimePurchases.this, getString(R.string.please_wait));

                            mpin_final_api();
                        }
                        else
                        {
                            Toast.makeText(AirtimePurchases.this, resultDescription, Toast.LENGTH_LONG).show();
                            confirm_reviewClick_textview.setEnabled(false);
                            confirm_reviewClick_textview.setClickable(false);

                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(AirtimePurchases.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(AirtimePurchases.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    private void api_walletOwnerUser() {


        MyApplication.showloader(AirtimePurchases.this, getString(R.string.please_wait));

        String USER_CODE_FROM_TOKEN_AGENTDETAILS = MyApplication.getSaveString("userCode", AirtimePurchases.this);


        API.GET_TRANSFER_DETAILS("ewallet/api/v1/walletOwnerUser/" + USER_CODE_FROM_TOKEN_AGENTDETAILS, languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1789408\",\"requestTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"responseTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwnerUser\":{\"id\":2171,\"code\":\"101917\",\"firstName\":\"TATASnegal\",\"userName\":\"TATASnegal5597\",\"mobileNumber\":\"8888888882\",\"email\":\"kundan.kumar@esteltelecom.com\",\"walletOwnerUserTypeCode\":\"100000\",\"walletOwnerUserTypeName\":\"Supervisor\",\"walletOwnerCategoryCode\":\"100000\",\"walletOwnerCategoryName\":\"Institute\",\"userCode\":\"1000002606\",\"status\":\"Active\",\"state\":\"Approved\",\"gender\":\"M\",\"idProofTypeCode\":\"100004\",\"idProofTypeName\":\"MILITARY ID CARD\",\"idProofNumber\":\"44444444444\",\"creationDate\":\"2021-10-01T09:04:07.330+0530\",\"notificationName\":\"EMAIL\",\"notificationLanguage\":\"en\",\"createdBy\":\"100308\",\"modificationDate\":\"2021-10-20T14:59:00.791+0530\",\"modifiedBy\":\"100308\",\"addressList\":[{\"id\":3569,\"walletOwnerUserCode\":\"101917\",\"addTypeCode\":\"100001\",\"addTypeName\":\"Commercial\",\"regionCode\":\"100068\",\"regionName\":\"Boke\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"city\":\"100022\",\"cityName\":\"Dubreka\",\"addressLine1\":\"delhi\",\"status\":\"Inactive\",\"creationDate\":\"2021-10-01T09:04:07.498+0530\",\"createdBy\":\"100250\",\"modificationDate\":\"2021-10-03T09:52:57.407+0530\",\"modifiedBy\":\"100308\"}],\"workingDaysList\":[{\"id\":3597,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100000\",\"weekdaysName\":\"Monday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.518+0530\"},{\"id\":3598,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100001\",\"weekdaysName\":\"Tuesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.528+0530\"},{\"id\":3599,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100002\",\"weekdaysName\":\"Wednesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.538+0530\"},{\"id\":3600,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100003\",\"weekdaysName\":\"Thursday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.547+0530\"},{\"id\":3601,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100004\",\"weekdaysName\":\"Friday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.556+0530\"},{\"id\":3602,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100005\",\"weekdaysName\":\"Saturday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.565+0530\"},{\"id\":3603,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100006\",\"weekdaysName\":\"Sunday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"2:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.573+0530\"}],\"macEnabled\":false,\"ipEnabled\":false,\"resetCredReqInit\":false,\"notificationTypeCode\":\"100000\"}}");


                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {

                        JSONObject walletOwnerUser = jsonObject.getJSONObject("walletOwnerUser");

                        if(walletOwnerUser.has("issuingCountryCode")){
                            countryCode_agent = walletOwnerUser.getString("issuingCountryCode");
                        }else{
                            countryCode_agent = "";
                        }
                        if(walletOwnerUser.has("issuingCountryName")){
                            countryName_agent = walletOwnerUser.getString("issuingCountryName");
                        }else{
                            countryName_agent = "";
                        }

                        rp_tv_agentName.setText(agentName_from_walletOwner);
                        rp_tv_mobileNumber.setText(mobileNoStr);
                        rp_tv_businessType.setText(businessTypeName_walletOwnerCategoryCode);

                        rp_tv_operator.setText(operatorName_from_operatorList);



                        api_exchange_rate();



                    } else {
                        Toast.makeText(AirtimePurchases.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(AirtimePurchases.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(AirtimePurchases.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    boolean validation_mpin_detail()
    {

        mpinStr = et_mpin.getText().toString();

        if(mpinStr.trim().isEmpty()) {


            Toast.makeText(AirtimePurchases.this, getString(R.string.please_enter_4_digit_mpin), Toast.LENGTH_LONG).show();

            return false;
        }

        else if(mpinStr.trim().length() == 4) {


            return true;
        }
        else {

            Toast.makeText(AirtimePurchases.this, getString(R.string.please_enter_4_digit_mpin), Toast.LENGTH_LONG).show();


            return false;
        }


    }


    private void mpin_final_api() {

        try {

//            {
//                   "requestType": "recharge",
//                    "channel": "SELFCARE",
//                    "operator": "100051",
//
//                    "serviceCode": "100009",
//                    "serviceCategoryCode": "100021",
//                    "serviceProviderCode": "100136",
//
//                    "fromCurrencyCode": "100062",
//                    "accountNumber": "9990063618",
//                    "amount": "1000",
//                    "pin": "2c4f28e2bcaa93f552f0313493ec4940",
//                    "productCode": "100030"
//            }


            JSONObject jsonObject = new JSONObject();

            jsonObject.put("requestType","recharge");
            jsonObject.put("channel","SELFCARE");
            jsonObject.put("operator",operator_code_from_operatorList);
            jsonObject.put("productCode",code_from_product_allByCriteria);
            jsonObject.put("serviceCode",serviceCode_from_operatorList);
            jsonObject.put("serviceCategoryCode",serviceCategoryCode_from_operatorList);
            jsonObject.put("serviceProviderCode",serviceProviderCode_from_operatorList);
            jsonObject.put("fromCurrencyCode",currencyCode_agent);
            jsonObject.put("accountNumber",mobileNoStr);
            jsonObject.put("amount",amountstr);
            String encryptionDatanew = AESEncryption.getAESEncryption(mpinStr);
            jsonObject.put("pin",encryptionDatanew);

            String requestNo=AESEncryption.getAESEncryption(jsonObject.toString());
            JSONObject jsonObjectA=null;
            try{
                jsonObjectA=new JSONObject();
                jsonObjectA.put("request",requestNo);
            }catch (Exception e){

            }
            API.POST_TRANSFERDETAILS("ewallet/api/v1/recharge/mobile-prepaid", jsonObjectA, languageToUse, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {


                      //  JSONObject jsonObject = new JSONObject("{\"transactionId\":\"115313\",\"requestTime\":\"Wed Nov 17 14:57:03 IST 2021\",
                        //  \"responseTime\":\"Wed Nov 17 14:57:07 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",
                        //  \"recharge\":{\"requestType\":\"topup\",\"channel\":\"SELFCARE\",\"operator\":\"ORANGE\",\"accountNumber\":\"629300694\",\"amount\":\"1000.0\",\"totalAmount\":\"1000.0\",\"fee\":\"0.0\",\"tax\":\"0.0\",\"vendorTransId\":\"624505\",\"vendorResultCode\":\"000\",\"vendorResultDescription\":\"Successful\",\"resultDescription\":\"Transaction Successful\",\"creationDate\":\"2021-11-17T14:57:07.637+0530\"}}");

                        String resultCode = jsonObject.getString("resultCode");
                        String resultDescription = jsonObject.getString("resultDescription");

                        if (resultCode.equalsIgnoreCase("0")) {

                            confirm_reviewClick_textview.setEnabled(true);
                            confirm_reviewClick_textview.setClickable(true);

                            ll_page_1.setVisibility(View.GONE);
                            ll_reviewPage.setVisibility(View.GONE);
                            ll_receiptPage.setVisibility(View.GONE);
                            ll_successPage.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ll_page_1.setVisibility(View.GONE);
                                    ll_reviewPage.setVisibility(View.GONE);
                                    ll_successPage.setVisibility(View.GONE);
                                    ll_receiptPage.setVisibility(View.VISIBLE);
                                }
                            }, 2000);


                            receiptPage_tv_stransactionType.setText(getString(R.string.airtime_purchase));
                            receiptPage_tv_transactionAmount.setText(currencySymbol_sender+" " +MyApplication.addDecimal(amountstr));
                            receiptPage_tv_fee.setText(currencySymbol_sender+" " +MyApplication.addDecimal(fees_amount));


                            receiptPage_tv_transaction_receiptNo.setText(jsonObject.getString("transactionId"));
                            receiptPage_tv_dateOfTransaction.setText(MyApplication.convertUTCToLocaldate(jsonObject.optJSONObject("recharge").
                                    getString("creationDate")));
                            receiptPage_tv_amount.setText(currencySymbol_sender+" " +MyApplication.addDecimal(totalAmount_str));


                          //  receiptPage_tv_sender_name.setText(agentName_from_walletOwner);
                         //   receiptPage_tv_sender_phoneNo.setText(MyApplication.getSaveString("USERNAME", AirtimePurchases.this));
                           // receiptPage_tv_sender_emailId.setText(sender_emailId_str);
                          //  receiptPage_tv_sender_country.setText(sender_country_str);
                          //  receiptPage_tv_receiver_name.setText(receiver_name_str);
                          //  receiptPage_tv_receiver_phoneNo.setText(mobileNoStr);
                         //   receiptPage_tv_receiver_emailId.setText(receiver_emailId_str);
                          //  receiptPage_tv_receiver_country.setText(receiver_country_str);

                            receiptPage_tv_mobileNumber.setText(mobileNoStr);

                           vendorTransId_tv_receiptPage.setText(jsonObject.getString("transactionId"));
                            vendorTransId_tv_receiptPagen.setText(jsonObject.optJSONObject("recharge").getString("vendorTransId"));
                            operator_tv_receipt.setText(operatorName_from_operatorList);







                            //  receiptPage_tv_sender_emailId.setText("");
                           // receiptPage_tv_sender_country.setText("");





                        } else {
                            confirm_reviewClick_textview.setEnabled(true);
                            confirm_reviewClick_textview.setClickable(true);

                            Toast.makeText(AirtimePurchases.this, resultDescription, Toast.LENGTH_LONG).show();
                            //  finish();
                        }


                    } catch (Exception e) {
                        confirm_reviewClick_textview.setEnabled(true);
                        confirm_reviewClick_textview.setClickable(true);

                        Toast.makeText(AirtimePurchases.this, e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }


                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(AirtimePurchases.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    }
                    else

                        Toast.makeText(AirtimePurchases.this, aFalse, Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (Exception e)
        {
            Toast.makeText(AirtimePurchases.this,e.toString(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_nextClick: {


                if (validation_mobile_Details()) {


                    if (new InternetCheck().isConnected(AirtimePurchases.this)) {

                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        api_walletOwnerUser();

                    } else {
                        Toast.makeText(AirtimePurchases.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                    }
                }
            }

            break;

            case R.id.confirm_reviewClick_textview:{
                {

                    if(pinLinear.getVisibility()==View.VISIBLE){
                        if (validation_mpin_detail()) {
                            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                                return;
                            }
                            mLastClickTime = SystemClock.elapsedRealtime();
                            confirm_reviewClick_textview.setEnabled(false);
                            confirm_reviewClick_textview.setClickable(false);


                            if (new InternetCheck().isConnected(AirtimePurchases.this)) {

                              //  MyApplication.showloader(AirtimePurchases.this, getString(R.string.please_wait));

                                api_allByCriteria_msisdnPrefix();

                            } else {
                                Toast.makeText(AirtimePurchases.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                            }
                        }
                    }else {
                        MyApplication.biometricAuth(AirtimePurchases.this, new BioMetric_Responce_Handler() {
                            @Override
                            public void success(String success) {
                                try {

                                    //  String encryptionDatanew = AESEncryption.getAESEncryption(MyApplication.getSaveString("pin",MyApplication.appInstance).toString().trim());
                                    mpinStr = MyApplication.getSaveString("pin", MyApplication.appInstance);

                                    if (new InternetCheck().isConnected(AirtimePurchases.this)) {

                                       // MyApplication.showloader(AirtimePurchases.this, getString(R.string.please_wait));

                                        api_allByCriteria_msisdnPrefix();

                                    } else {
                                        Toast.makeText(AirtimePurchases.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failure(String failure) {

                               // MyApplication.showToast(AirtimePurchases.this, failure);

                                pinLinear.setVisibility(View.VISIBLE);


                            }
                        });
                    }
//new update
              /*  BiometricManager biometricManager = androidx.biometric.BiometricManager.from(CashIn.this);
                switch (biometricManager.canAuthenticate()) {

                    // this means we can use biometric sensor
                    case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:

                        Toast.makeText(CashIn.this, getString(R.string.device_not_contain_fingerprint), Toast.LENGTH_SHORT).show();
                        pinLinear.setVisibility(View.VISIBLE);
                        if (validation_mpin_detail()) {

                            if (new InternetCheck().isConnected(CashIn.this)) {

                                MyApplication.showloader(CashIn.this, getString(R.string.please_wait));

                                mpin_final_api();

                            } else {
                                Toast.makeText(CashIn.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                            }
                            // msgText.setText("You can use the fingerprint sensor to login");
                            // msgText.setTextColor(Color.parseColor("#fafafa"));
                            return;
                        }
                    case BiometricManager.BIOMETRIC_SUCCESS:

                        // msgText.setText("You can use the fingerprint sensor to login");
                        // msgText.setTextColor(Color.parseColor("#fafafa"));
                            MyApplication.biometricAuth(CashIn.this, new BioMetric_Responce_Handler() {
                                @Override
                                public void success(String success) {
                                    try {

                                        //  String encryptionDatanew = AESEncryption.getAESEncryption(MyApplication.getSaveString("pin",MyApplication.appInstance).toString().trim());
                                        mpinStr = MyApplication.getSaveString("pin", MyApplication.appInstance);

                                        if (new InternetCheck().isConnected(CashIn.this)) {

                                            MyApplication.showloader(CashIn.this, getString(R.string.please_wait));

                                            mpin_final_api();

                                        } else {
                                            Toast.makeText(CashIn.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void failure(String failure) {
                                    MyApplication.showToast(CashIn.this, failure);

                                }

                            });
                        }
*/






               /* if (validation_mpin_detail()) {

                    if (new InternetCheck().isConnected(CashIn.this)) {

                        MyApplication.showloader(CashIn.this, getString(R.string.please_wait));

                        mpin_final_api();

                    } else {
                        Toast.makeText(CashIn.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                    }
                }*/

                }
            }

            break;

            case R.id.exportReceipt_textview: {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) { // 1000 = 1second
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                close_receiptPage_textview.setVisibility(View.VISIBLE);
                exportReceipt_textview.setVisibility(View.VISIBLE);
                Bitmap bitmap = getScreenShot(rootView);
                createImageFile(bitmap);
                //store(bitmap, "test.jpg");
            }

            break;

            case R.id.previous_reviewClick_textview: {

                ll_page_1.setVisibility(View.VISIBLE);
                ll_reviewPage.setVisibility(View.GONE);
                ll_receiptPage.setVisibility(View.GONE);
                ll_successPage.setVisibility(View.GONE);
                pinLinear.setVisibility(View.GONE);
            }
            break;

            case R.id.tvContinue: {

                ll_page_1.setVisibility(View.GONE);
                ll_reviewPage.setVisibility(View.GONE);
                ll_successPage.setVisibility(View.GONE);
                ll_receiptPage.setVisibility(View.VISIBLE);

            }
            break;

            case R.id.close_receiptPage_textview:
            {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) { // 1000 = 1second
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
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

            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {


                String requiredValue = data.getStringExtra("PHONE");
                MyApplication.contactValidation(requiredValue,edittext_mobileNo);

               // edittext_mobileNo.setText(requiredValue);

            }
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

    @Override
    public void onBackPressed() {

        ll_page_1.setVisibility(View.VISIBLE);
        ll_reviewPage.setVisibility(View.GONE);
        ll_successPage.setVisibility(View.GONE);
        ll_receiptPage.setVisibility(View.GONE);
        pinLinear.setVisibility(View.GONE);
          super.onBackPressed();
    }


//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
//    }


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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {




            case R.id.spinner_operator:
            {

                 if(spinner_operator.getSelectedItemPosition()==0)
                 {
                    // Toast.makeText(AirtimePurchases.this, selectOperatorList, Toast.LENGTH_SHORT).show();
                 }
                 else
                 {

                     operator_code_from_operatorList = arrayList_OperatorListCode.get(i);
                     operatorName_from_operatorList = arrayList_OperatorListName.get(i);
                     serviceCode_from_operatorList = arrayList_ServiceCode.get(i);
                     serviceCategoryCode_from_operatorList = arrayList_serviceCategoryCode.get(i);
                     serviceProviderCode_from_operatorList = arrayList_serviceProviderCode.get(i);


                     MyApplication.showloader(AirtimePurchases.this, getString(R.string.please_wait));



                     api_product_allByCriteria_serviceCategoryCode(operator_code_from_operatorList);



                 }
            }

            break;

            case R.id.spinner_senderCountry:
            {

                selectCountryName = arrayList_senderCountryName.get(i);
                selectCountryCode = arrayList_senderCountryCode.get(i);



                //  Toast.makeText(LocalRemittance.this, genderSelect_name, Toast.LENGTH_SHORT).show();
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


    private boolean isFormatting;
    private int prevCommaAmount;
    private void formatInput(EditText editText,CharSequence s, int start, int count) {

        if(MyApplication.checkMinMax(AirtimePurchases.this,s,editText
                ,MyApplication.ToCreditPurchaseMinAmount,MyApplication.ToCreditPurchaseMaxAmount)){
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