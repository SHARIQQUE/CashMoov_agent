package com.agent.cashmoovui.transfer_float;

import static com.agent.cashmoovui.apiCalls.CommonData.sellfloat_allSellFloat_featureCode;
import static com.agent.cashmoovui.apiCalls.CommonData.sellfloat_walletOwnerCategoryCode;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.adapter.CommonBaseAdapter;
import com.agent.cashmoovui.adapter.CommonBaseAdapterSecond;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.cash_in.CashIn;
import com.agent.cashmoovui.cashout.CashOutCodeSubscriber;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.login.LoginPin;
import com.agent.cashmoovui.otp.VerifyLoginAccountScreen;
import com.agent.cashmoovui.set_pin.AESEncryption;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class TransferFloats extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {


    public static LoginPin loginpinC;
    ImageButton qrCode_imageButton;

    String currencyName_from_currency="",walletOwnerCode_from_currency="";
    String countryCurrencyCode_from_currency="";
    String mobileNoStr="";

    String walletOwnerCode_from_walletOwnerChildList="";

    String receiver_name_str="",receiver_emailId_str="",receiver_country_str="",sender_emailId_str="",sender_country_str="",countryCode_agent="";

    boolean  isPasswordVisible;



    String  serviceCode_from_allSellFloat ="",serviceCategoryCode_from_allSellFloat="",serviceProviderCode_from_allSellFloat="",srcCurrencyCode_from_allSellFloat="",desCurrencyCode_from_allSellFloat="",srcWalletOwnerCode_from_allSellFloat="",desWalletOwnerCode_from_allSellFloat="";


    Spinner  spinner_insititue,spinner_currency;
    View rootView;

    EditText etPin;
    TextView receiptPage_tv_sender_emailId,receiptPage_tv_sender_country,receiptPage_tv_receiver_emailId,receiptPage_tv_receiver_country,rp_tv_convertionrate,exportReceipt_textview,tv_nextClick,rp_tv_agentName,rp_tv_mobileNumber,rp_tv_businessType,rp_tv_email,rp_tv_country,rp_tv_receiverName,rp_tv_transactionAmount
            ,rp_tv_fees_reveiewPage,receiptPage_tv_stransactionType, receiptPage_tv_dateOfTransaction, receiptPage_tv_transactionAmount,
            receiptPage_tv_amount_to_be_credit, receiptPage_tv_fee, receiptPage_tv_financialtax, receiptPage_tv_transaction_receiptNo,receiptPage_tv_sender_name,
            receiptPage_tv_sender_phoneNo,
            receiptPage_tv_receiver_name, receiptPage_tv_receiver_phoneNo, close_receiptPage_textview,rp_tv_excise_tax,rp_tv_amount_to_be_charge,rp_tv_amount_paid,previous_reviewClick_textview,confirm_reviewClick_textview;
    LinearLayout ll_page_1,ll_reviewPage,ll_receiptPage,main_layout;

    MyApplication applicationComponentClass;
    String languageToUse = "";

    EditText edittext_amount,et_mpin,edittext_mobileNo;


    String amountstr="",agentName_from_walletOwner="", businessTypeName_walletOwnerCategoryCode="",email_walletOwnerCategoryCode="";

    String walletOwnerCode_mssis_agent="",walletOwnerCode_subs, senderNameAgent="";

    String  currencyCode_agent="",currencyName_agent="";

    String  currencyCode_subscriber="",countryCode_subscriber="",currencyName_subscriber="",agentCode_subscriber;


    String tax_financial="",fees_amount,totalAmount_str,receivernameStr="";
    Double tax_financial_double=0.0,amountstr_double=0.0,fees_amount_double=0.0,totalAmount_double=0.0;

    String mpinStr="";


    String  serviceCode_from_serviceCategory="",serviceCategoryCode_from_serviceCategory="",serviceProviderCode_from_serviceCategory;





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


            setContentView(R.layout.transfer_flaots);


            rootView = getWindow().getDecorView().findViewById(R.id.main_layout);



            //     First page

            ll_page_1 = (LinearLayout) findViewById(R.id.ll_page_1);

            tv_nextClick = (TextView) findViewById(R.id.tv_nextClick);
            edittext_amount = (EditText) findViewById(R.id.edittext_amount);
            edittext_mobileNo = (EditText) findViewById(R.id.edittext_mobileNo);

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

            //    Receipt page

            ll_receiptPage = (LinearLayout) findViewById(R.id.ll_receiptPage);
            main_layout = (LinearLayout) findViewById(R.id.main_layout);
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
            receiptPage_tv_receiver_emailId  = (TextView) findViewById(R.id.receiptPage_tv_receiver_emailId);

            receiptPage_tv_sender_emailId  = (TextView) findViewById(R.id.receiptPage_tv_sender_emailId);
            receiptPage_tv_sender_country  = (TextView) findViewById(R.id.receiptPage_tv_sender_country);

            receiptPage_tv_receiver_country = (TextView) findViewById(R.id.receiptPage_tv_receiver_country);
            close_receiptPage_textview = (TextView) findViewById(R.id.close_receiptPage_textview);
            qrCode_imageButton = (ImageButton) findViewById(R.id.qrCode_imageButton);


            qrCode_imageButton.setOnClickListener(this);
            tv_nextClick.setOnClickListener(this);
            previous_reviewClick_textview.setOnClickListener(this);
            confirm_reviewClick_textview.setOnClickListener(this);
            close_receiptPage_textview.setOnClickListener(this);

            walletOwnerCode_mssis_agent = MyApplication.getSaveString("USERCODE", TransferFloats.this);


            spinner_insititue= (Spinner) findViewById(R.id.spinner_insititue);
            spinner_insititue.setOnItemSelectedListener(this);


            spinner_currency= (Spinner) findViewById(R.id.spinner_currency);
            spinner_currency.setOnItemSelectedListener(this);



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
                                et_mpin.setTransformationMethod(PasswordTransformationMethod.getInstance());
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


            api_currency();




        }
        catch (Exception e)
        {
            Toast.makeText(TransferFloats.this, e.toString(), Toast.LENGTH_LONG).show();

        }

    }


    private void api_walletOwner() {


        String userCode_agentCode_from_mssid =  MyApplication.getSaveString("USERCODE", TransferFloats.this);


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
                        sender_emailId_str = jsonObject_walletOwner.getString("email");
                        sender_country_str = jsonObject_walletOwner.getString("issuingCountryName");

                        rp_tv_country.setText(sender_country_str);
                        rp_tv_email.setText(sender_emailId_str);



                    } else {
                        Toast.makeText(TransferFloats.this, resultDescription, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    Toast.makeText(TransferFloats.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(TransferFloats.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void api_walletOwner_msisdn() {


        API.GET_TRANSFER_DETAILS("ewallet/api/v1/walletOwner/msisdn/"+mobileNoStr,languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {



                MyApplication.hideLoader();

                try {


                    //    JSONObject jsonObject1 = new JSONObject("{\"transactionId\":\"1927802\",\"requestTime\":\"Tue Nov 02 13:03:30 IST 2021\",\"responseTime\":\"Tue Nov 02 13:03:30 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwner\":{\"id\":110679,\"code\":\"1000002785\",\"walletOwnerCategoryCode\":\"100000\",\"ownerName\":\"sharique agent\",\"mobileNumber\":\"9990063618\",\"businessTypeCode\":\"100008\",\"businessTypeName\":\"Goldsmith\",\"lineOfBusiness\":\"gffg\",\"idProofNumber\":\"trt465656\",\"email\":\"sharique9718@gmail.com\",\"status\":\"Active\",\"state\":\"Approved\",\"stage\":\"Document\",\"idProofTypeCode\":\"100005\",\"idProofTypeName\":\"COMPANY REGISTRATION NUMBER\",\"idExpiryDate\":\"2021-10-22\",\"notificationLanguage\":\"en\",\"notificationTypeCode\":\"100000\",\"notificationName\":\"EMAIL\",\"issuingCountryCode\":\"100102\",\"issuingCountryName\":\"India\",\"registerCountryCode\":\"100102\",\"registerCountryName\":\"India\",\"createdBy\":\"100250\",\"modifiedBy\":\"100308\",\"creationDate\":\"2021-10-19T22:38:48.969+0530\",\"modificationDate\":\"2021-11-01T13:49:14.892+0530\",\"walletExists\":true,\"profileTypeCode\":\"100000\",\"profileTypeName\":\"tier1\",\"walletCurrencyList\":[\"100018\",\"100017\",\"100069\",\"100020\",\"100004\",\"100029\",\"100062\",\"100003\"],\"walletOwnerCatName\":\"Institute\",\"requestedSource\":\"ADMIN\",\"regesterCountryDialCode\":\"+91\",\"issuingCountryDialCode\":\"+91\",\"walletOwnerCode\":\"1000002785\"}}");


                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {

                        JSONObject jsonObject_walletOwner = jsonObject.getJSONObject("walletOwner");
                        receiver_name_str = jsonObject_walletOwner.getString("ownerName");
                        receiver_emailId_str = jsonObject_walletOwner.getString("email");
                        receiver_country_str = jsonObject_walletOwner.getString("registerCountryName");

                        walletOwnerCode_from_walletOwnerChildList = jsonObject_walletOwner.getString("code");



                        api_allSellFloat_featureCode();



                    } else {
                        Toast.makeText(TransferFloats.this, resultDescription, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    Toast.makeText(TransferFloats.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(TransferFloats.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    boolean validation_mobile_Details() {

        amountstr = edittext_amount.getText().toString().trim();
        mobileNoStr = edittext_mobileNo.getText().toString().trim();

        if(mobileNoStr.isEmpty()) {

            MyApplication.showErrorToast(this,getString(R.string.val_phone));

            return false;
        }

        else if(mobileNoStr.length() < 9) {

            MyApplication.showErrorToast(this,getString(R.string.val_phone));

            return false;
        }


       else if (amountstr.isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.amount_to_paid_without_star));

            return false;
        }


        return true;
    }






    private void api_currency() {

        String userCode_agentCode_from_mssid =  MyApplication.getSaveString("USERCODE", TransferFloats.this);

        API.GET_TRANSFER_DETAILS("ewallet/api/v1/walletOwnerCountryCurrency/"+userCode_agentCode_from_mssid,languageToUse,new Api_Responce_Handler() {

            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {


                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");

                   if(resultCode.equalsIgnoreCase("0")) {


                        arrayList_currecnyName.add(0,getString(R.string.select_currency_star));
                        arrayList_currecnyCode.add(0,getString(R.string.select_currency_star));

                        JSONArray jsonArray = jsonObject.getJSONArray("walletOwnerCountryCurrencyList");
                        for(int i=0;i<jsonArray.length();i++)
                        {

                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);

                            currencyName_from_currency = jsonObject3.getString("currencyName");
                            countryCurrencyCode_from_currency = jsonObject3.getString("countryCurrencyCode");
                            walletOwnerCode_from_currency = jsonObject3.getString("walletOwnerCode");




                            if(jsonObject3.has("currencyName")) {

                                String  currencyName_subscriber_temp = jsonObject3.getString("currencyName");
                                if (currencyName_subscriber_temp.equalsIgnoreCase("GNF")) {
                                    currencyName_subscriber = jsonObject3.getString("currencyName");
                                    currencyCode_subscriber = jsonObject3.getString("currencyCode");

                                } else {

                                }
                            }

                            arrayList_currecnyName.add(currencyName_from_currency);
                            arrayList_currecnyCode.add(countryCurrencyCode_from_currency);

                        }


                        CommonBaseAdapterSecond arraadapter2 = new CommonBaseAdapterSecond(TransferFloats.this, arrayList_currecnyName);
                        spinner_currency.setAdapter(arraadapter2);


                        api_walletOwner();
                    }

                    else {
                        Toast.makeText(TransferFloats.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(TransferFloats.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(TransferFloats.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }



    private void api_allSellFloat_featureCode() {

        String userCode_agentCode_from_mssid =  MyApplication.getSaveString("USERCODE", TransferFloats.this);

        API.GET_TRANSFER_DETAILS("ewallet/api/v1/walletTransfer/allSellFloat?featureCode="+sellfloat_allSellFloat_featureCode+"&srcWalletOwnerCode="+walletOwnerCode_from_currency+"&offset=0&limit=10",languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    //    JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1924722\",\"requestTime\":\"Tue Nov 02 09:07:39 IST 2021\",\"responseTime\":\"Tue Nov 02 09:07:39 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"pageable\":{\"totalRecords\":2},\"walletOperationList\":[{\"id\":152211,\"code\":\"1000152373\",\"featureCode\":\"100076\",\"desWalletCode\":\"1000028775\",\"srcWalletCode\":\"1000028522\",\"srcWalletOwnerCode\":\"1000002785\",\"srcWalletOwnerName\":\"sharique agent\",\"srcCurrencyCode\":\"100062\",\"srcCurrencyName\":\"GNF\",\"srcWalletTypeCode\":\"100008\",\"srcWalletTypeName\":\"Main Wallet\",\"desWalletTypeCode\":\"100008\",\"desWalletTypeName\":\"Main Wallet\",\"desWalletOwnerCode\":\"1000002820\",\"desWalletOwnerName\":\"Oceans Forex\",\"desWalletOwnerNumber\":\"605218330333\",\"amount\":1000.0,\"channelTypeCode\":\"100000\",\"desCurrencyCode\":\"100062\",\"desCurrencyName\":\"GNF\",\"status\":\"Pending\",\"createdBy\":\"102068\",\"creationDate\":\"2021-11-02T09:07:37.950+0530\",\"tax\":50.0,\"fee\":1000.0,\"exchangeRate\":0.0,\"finalAmount\":2050.0,\"srcCurrencySymbol\":\"Fr\",\"desCurrencySymbol\":\"Fr\",\"transactionType\":\"101611\",\"serviceCode\":\"100000\",\"serviceCategoryCode\":\"100016\",\"serviceProviderCode\":\"100109\"},{\"id\":152207,\"code\":\"1000152369\",\"featureCode\":\"100076\",\"desWalletCode\":\"1000028686\",\"srcWalletCode\":\"1000028522\",\"srcWalletOwnerCode\":\"1000002785\",\"srcWalletOwnerName\":\"sharique agent\",\"srcCurrencyCode\":\"100062\",\"srcCurrencyName\":\"GNF\",\"srcWalletTypeCode\":\"100008\",\"srcWalletTypeName\":\"Main Wallet\",\"desWalletTypeCode\":\"100008\",\"desWalletTypeName\":\"Main Wallet\",\"desWalletOwnerCode\":\"1000002817\",\"desWalletOwnerName\":\"Rahul Inst\",\"desWalletOwnerNumber\":\"9910859186\",\"amount\":1000.0,\"channelTypeCode\":\"100000\",\"desCurrencyCode\":\"100062\",\"desCurrencyName\":\"GNF\",\"status\":\"Pending\",\"createdBy\":\"102068\",\"creationDate\":\"2021-11-01T13:36:27.890+0530\",\"tax\":50.0,\"fee\":1000.0,\"exchangeRate\":0.0,\"finalAmount\":2050.0,\"srcCurrencySymbol\":\"Fr\",\"desCurrencySymbol\":\"Fr\",\"transactionType\":\"101611\",\"serviceCode\":\"100000\",\"serviceCategoryCode\":\"100016\",\"serviceProviderCode\":\"100109\"}]}");

                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");

                    if(resultCode.equalsIgnoreCase("0")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("walletOperationList");

                        for(int i=0;i<jsonArray.length();i++) {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            serviceCode_from_allSellFloat = jsonObject2.getString("serviceCode");
                            serviceCategoryCode_from_allSellFloat= jsonObject2.getString("serviceCategoryCode");
                            srcCurrencyCode_from_allSellFloat= jsonObject2.getString("srcCurrencyCode");
                            desCurrencyCode_from_allSellFloat= jsonObject2.getString("desCurrencyCode");


                            srcWalletOwnerCode_from_allSellFloat= jsonObject2.getString("srcWalletOwnerCode");
                            desWalletOwnerCode_from_allSellFloat= jsonObject2.getString("desWalletOwnerCode");


                            Double tax_double=0.0,fee_double=0.0,exchangeRate_double=0.0,finalAmount_double=0.0;


                            String  tax_from_allSellFloat="",fee_from_allSellFloat="",exchangeRate_from_allSellFloat="",finalAmount_from_allSellFloat="";



                            // rp_tv_tax.setText("Fr " +jsonObject2.getInt("tax"));





                            if(jsonObject2.has("serviceProviderCode"))
                            {
                                serviceProviderCode_from_allSellFloat = jsonObject2.getString("serviceProviderCode");
                            }
                            else {
                                serviceProviderCode_from_allSellFloat = "";
                            }

                        }


                        api_serviceProvider();



                    }

                    else {
                        Toast.makeText(TransferFloats.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(TransferFloats.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(TransferFloats.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void api_currency_subscriber() {



        API.GET_CASHOUT_DETAILS("ewallet/api/v1/walletOwnerCountryCurrency/" + walletOwnerCode_from_walletOwnerChildList, languageToUse, new Api_Responce_Handler() {
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
                        Toast.makeText(TransferFloats.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(TransferFloats.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(TransferFloats.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void api_currency_sender() {


// ewallet/api/v1/walletOwnerCountryCurrency/1000002692
        API.GET_CASHIN_DETAILS("ewallet/api/v1/walletOwnerCountryCurrency/"+walletOwnerCode_mssis_agent,languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

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
                        Toast.makeText(TransferFloats.this, resultDescription, Toast.LENGTH_LONG).show();
                        //finish();
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(TransferFloats.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(TransferFloats.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    private void api_exchange_rate() {


        http://202.131.144.130:8081/ewallet/api/v1/exchangeRate/getAmountDetails?sendCurrencyCode=100062&receiveCurrencyCode=
        // 100076&sendCountryCode=100092&receiveCountryCode=&currencyValue=1500&channelTypeCode=100002&serviceCode=100000&
        // serviceCategoryCode=100017&serviceProviderCode=100108&walletOwnerCode=1000002692&
        // remitAgentCode=1000002692&payAgentCode=1000002692




                API.GET_CASHOUT_DETAILS("ewallet/api/v1/exchangeRate/getAmountDetails?sendCurrencyCode=" + currencyCode_agent +
                        "&receiveCurrencyCode="+currencyCode_subscriber+"&sendCountryCode=" + countryCode_agent + "&receiveCountryCode="+""+
                        "&currencyValue=" + amountstr + "&channelTypeCode=100002&serviceCode=" + serviceCode_from_serviceCategory + "&serviceCategoryCode=" +
                        serviceCategoryCode_from_serviceCategory + "&serviceProviderCode=" +
                        serviceProviderCode_from_serviceCategory + "&walletOwnerCode=" + walletOwnerCode_mssis_agent + "&remitAgentCode=" +
                        walletOwnerCode_mssis_agent + "&payAgentCode="+walletOwnerCode_from_walletOwnerChildList,languageToUse, new Api_Responce_Handler() {


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



                        if(exchangeRate.has("taxConfigurationList"))
                        {
                            JSONArray jsonArray = exchangeRate.getJSONArray("taxConfigurationList");
                            for(int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                tax_financial = jsonObject2.getString("value");
                            }
                        }
                        else {
                            tax_financial = exchangeRate.getString("value");
                        }


                        rp_tv_convertionrate.setText("Fr " +"0.0");
                        rp_tv_fees_reveiewPage.setText("Fr "+fees_amount);
                        rp_tv_excise_tax.setText("Fr " +tax_financial);
                        rp_tv_amount_paid.setText("Fr " +amountstr);

                        tax_financial_double = Double.parseDouble(tax_financial);
                        amountstr_double = Double.parseDouble(amountstr);
                        fees_amount_double = Double.parseDouble(fees_amount);

                        totalAmount_double = tax_financial_double+amountstr_double+fees_amount_double;
                        totalAmount_str = String.valueOf(totalAmount_double);
                        rp_tv_amount_to_be_charge.setText("Fr " +totalAmount_str);




                        ll_page_1.setVisibility(View.GONE);
                        ll_reviewPage.setVisibility(View.VISIBLE);
                        ll_receiptPage.setVisibility(View.GONE);




                    }

                    else {
                        Toast.makeText(TransferFloats.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(TransferFloats.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(TransferFloats.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }




    private void api_serviceProvider() {

        MyApplication.showloader(TransferFloats.this, getString(R.string.getting_user_info));

        String serviceCategoryCode_hardcode="100017";   // 100017 is hard code according to praveen 19 Nov

        String serviceCode_LoginApi = MyApplication.getSaveString("serviceCode_LoginApi", TransferFloats.this);


      //  Toast.makeText(TransferFloats.this, "---------------"+serviceCode_LoginApi, Toast.LENGTH_LONG).show();

        //  Toast.makeText(SellFloat.this, "------------ test 100000", Toast.LENGTH_LONG).show();

        API.GET_TRANSFER_DETAILS("ewallet/api/v1/serviceProvider/serviceCategory?serviceCode="+serviceCode_LoginApi+"&serviceCategoryCode="+serviceCategoryCode_hardcode+"&status=Y",languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

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
                        Toast.makeText(TransferFloats.this, resultDescription, Toast.LENGTH_LONG).show();
                       // finish();
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(TransferFloats.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(TransferFloats.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }



    private void api_walletOwnerUser() {

        String USER_CODE_FROM_TOKEN_AGENTDETAILS = MyApplication.getSaveString("userCode", TransferFloats.this);


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

                        String  issuingCountryName = walletOwnerUser.getString("issuingCountryName");

                        countryCode_agent = walletOwnerUser.getString("issuingCountryCode");


                        rp_tv_agentName.setText(agentName_from_walletOwner);
                        rp_tv_mobileNumber.setText(MyApplication.getSaveString("USERNAME", TransferFloats.this));
                        rp_tv_businessType.setText(businessTypeName_walletOwnerCategoryCode);

                      //  rp_tv_receiverName.setText(selectInstititueName);
                        rp_tv_transactionAmount.setText(amountstr);



                api_currency_sender();


                    } else {
                        Toast.makeText(TransferFloats.this, resultDescription, Toast.LENGTH_LONG).show();
                        //finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(TransferFloats.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(TransferFloats.this, aFalse, Toast.LENGTH_SHORT).show();
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

            String mobileNumber_login = MyApplication.getSaveString("USERNAME", TransferFloats.this);



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

                            mpin_final_api();

                        } else {
                            Toast.makeText(TransferFloats.this, resultDescription, Toast.LENGTH_LONG).show();
                            //  finish();
                        }


                    } catch (Exception e) {
                        Toast.makeText(TransferFloats.this, e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }


                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(TransferFloats.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    }
                    else

                        Toast.makeText(TransferFloats.this, aFalse, Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (Exception e)
        {
            Toast.makeText(TransferFloats.this,e.toString(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }

    private void mpin_final_api() {

        try {

//            {
//                "transactionType": "101612",
//                    "srcWalletOwnerCode": "1000002846",
//                    "desWalletOwnerCode": "1000002855",
//                    "srcCurrencyCode": "100062",
//                    "desCurrencyCode": "100062",
//                    "value": "1000",
//                    "pin": "2c4f28e2bcaa93f552f0313493ec4940",
//                    "channelTypeCode": "100000",
//                    "serviceCode": "100000",
//                    "serviceCategoryCode": "100017",
//                    "serviceProviderCode": "100108",
//                    "mobileNumber": "97181968499"
//            }

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("transactionType","101612"); // Hard code

            jsonObject.put("srcWalletOwnerCode",srcWalletOwnerCode_from_allSellFloat);
            jsonObject.put("desWalletOwnerCode",desWalletOwnerCode_from_allSellFloat);

            jsonObject.put("srcCurrencyCode",currencyCode_agent);
            jsonObject.put("desCurrencyCode",currencyCode_subscriber);


            jsonObject.put("value",amountstr);


            String encryptionDatanew = AESEncryption.getAESEncryption(mpinStr);
            jsonObject.put("pin",encryptionDatanew);

            jsonObject.put("channelTypeCode","100000"); // Hard code

            jsonObject.put("serviceCode",serviceCategoryCode_from_serviceCategory);
            jsonObject.put("serviceCategoryCode",serviceCategoryCode_from_serviceCategory);
            jsonObject.put("serviceProviderCode",serviceProviderCode_from_serviceCategory);
         //   jsonObject.put("mobileNumber",mobileNumberStr);


            API.POST_TRANSFERDETAILS("ewallet/api/v1/walletTransfer/float", jsonObject, languageToUse, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {


                        // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"118470\",\"requestTime\":\"Thu Nov 04 03:22:59 IST 2021\",\"responseTime\":\"Thu Nov 04 03:22:59 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletTransfer\":{\"code\":\"118470\",\"srcWalletCode\":\"1000029190\",\"desWalletCode\":\"1000029322\",\"srcWalletOwnerCode\":\"1000002846\",\"desWalletOwnerCode\":\"1000002855\",\"srcWalletTypeCode\":\"100008\",\"desWalletTypeCode\":\"100008\",\"srcCurrencyCode\":\"100062\",\"desCurrencyCode\":\"100062\",\"srcCurrencyName\":\"GNF\",\"desCurrencyName\":\"GNF\",\"srcCurrencySymbol\":\"Fr\",\"desCurrencySymbol\":\"Fr\",\"value\":1000.0,\"createdBy\":\"102112\",\"creationDate\":\"2021-11-04 03:22:59\",\"fee\":0.0,\"finalAmount\":1000.0,\"srcWalletOwner\":{\"id\":110740,\"code\":\"1000002846\",\"walletOwnerCategoryCode\":\"100000\",\"ownerName\":\"sharique agent\",\"mobileNumber\":\"9990063618\",\"businessTypeCode\":\"100010\",\"businessTypeName\":\"Restaurant\",\"lineOfBusiness\":\"788\",\"idProofNumber\":\"id1234\",\"email\":\"sharique9718@gmail.com\",\"status\":\"Active\",\"state\":\"Approved\",\"stage\":\"Document\",\"idProofTypeCode\":\"100000\",\"idProofTypeName\":\"PASSPORT\",\"idExpiryDate\":\"2021-11-11\",\"notificationLanguage\":\"en\",\"notificationTypeCode\":\"100000\",\"notificationName\":\"EMAIL\",\"issuingCountryCode\":\"100102\",\"issuingCountryName\":\"India\",\"registerCountryCode\":\"100102\",\"registerCountryName\":\"India\",\"createdBy\":\"100250\",\"modifiedBy\":\"100308\",\"creationDate\":\"2021-11-03T21:20:23.606+0530\",\"modificationDate\":\"2021-11-03T21:27:28.112+0530\",\"walletExists\":true,\"profileTypeCode\":\"100000\",\"profileTypeName\":\"tier1\",\"walletCurrencyList\":[\"100018\",\"100017\",\"100069\",\"100020\",\"100004\",\"100029\",\"100062\",\"100003\"],\"walletOwnerCatName\":\"Institute\",\"requestedSource\":\"ADMIN\",\"regesterCountryDialCode\":\"+91\",\"issuingCountryDialCode\":\"+91\",\"walletOwnerCode\":\"1000002846\"},\"desWalletOwner\":{\"id\":110749,\"code\":\"1000002855\",\"walletOwnerParentCode\":\"1000002846\",\"walletOwnerCategoryCode\":\"100002\",\"ownerName\":\"sharique agnet witin hir\",\"mobileNumber\":\"97181968499\",\"businessTypeCode\":\"100006\",\"businessTypeName\":\"service delivery\",\"lineOfBusiness\":\"1234\",\"idProofNumber\":\"id12345\",\"email\":\"sharique9718@gmail.com\",\"status\":\"Active\",\"state\":\"Approved\",\"stage\":\"Document\",\"idProofTypeCode\":\"100001\",\"idProofTypeName\":\"NATIONAL IDENTITY CARD\",\"idExpiryDate\":\"2021-11-20\",\"notificationLanguage\":\"en\",\"notificationTypeCode\":\"100000\",\"notificationName\":\"EMAIL\",\"gender\":\"M\",\"dateOfBirth\":\"1960-01-13\",\"lastName\":\"sharque\",\"registerCountryCode\":\"100102\",\"registerCountryName\":\"India\",\"createdBy\":\"100250\",\"modifiedBy\":\"100308\",\"creationDate\":\"2021-11-04T03:19:39.311+0530\",\"modificationDate\":\"2021-11-04T03:22:59.827+0530\",\"walletExists\":true,\"profileTypeCode\":\"100000\",\"profileTypeName\":\"tier1\",\"walletCurrencyList\":[\"100018\",\"100017\",\"100069\",\"100020\",\"100004\",\"100029\",\"100062\",\"100003\"],\"walletOwnerCatName\":\"Agent\",\"requestedSource\":\"ADMIN\",\"regesterCountryDialCode\":\"+91\",\"walletOwnerCode\":\"1000002855\"},\"transactionType\":\"TRANSFER FLOAT\"}}");

                        String resultCode = jsonObject.getString("resultCode");
                        String resultDescription = jsonObject.getString("resultDescription");

                        if (resultCode.equalsIgnoreCase("0")) {


                            ll_page_1.setVisibility(View.GONE);
                            ll_reviewPage.setVisibility(View.GONE);
                            ll_receiptPage.setVisibility(View.VISIBLE);



                            receiptPage_tv_stransactionType.setText(" TRANSFER FLOAT");
                            receiptPage_tv_transactionAmount.setText(amountstr);
                            receiptPage_tv_fee.setText(fees_amount);
                            receiptPage_tv_financialtax.setText(tax_financial);


                            receiptPage_tv_transaction_receiptNo.setText(jsonObject.getString("transactionId"));
                            receiptPage_tv_dateOfTransaction.setText(jsonObject.getString("responseTime"));
                            receiptPage_tv_amount_to_be_credit.setText(amountstr);

                            receiptPage_tv_sender_name.setText(agentName_from_walletOwner);
                            receiptPage_tv_sender_phoneNo.setText(MyApplication.getSaveString("USERNAME", TransferFloats.this));

                            receiptPage_tv_sender_emailId.setText(sender_emailId_str);
                            receiptPage_tv_sender_country.setText(sender_country_str);



                            receiptPage_tv_receiver_name.setText(receiver_name_str);
                            receiptPage_tv_receiver_phoneNo.setText(mobileNoStr);

                            receiptPage_tv_receiver_emailId.setText(receiver_emailId_str);
                            receiptPage_tv_receiver_country.setText(receiver_country_str);






                            //  receiptPage_tv_sender_emailId.setText("");
                           // receiptPage_tv_sender_country.setText("");





                        } else {
                            Toast.makeText(TransferFloats.this, resultDescription, Toast.LENGTH_LONG).show();
                            //  finish();
                        }


                    } catch (Exception e) {
                        Toast.makeText(TransferFloats.this, e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }


                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(TransferFloats.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    }
                    else

                        Toast.makeText(TransferFloats.this, aFalse, Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (Exception e)
        {
            Toast.makeText(TransferFloats.this,e.toString(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_nextClick: {


                if (validation_mobile_Details()) {


                    if (new InternetCheck().isConnected(TransferFloats.this)) {

                        MyApplication.showloader(TransferFloats.this, getString(R.string.getting_user_info));


                        api_walletOwner_msisdn();



                    } else {
                        Toast.makeText(TransferFloats.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                    }
                }
            }

            break;

            case R.id.confirm_reviewClick_textview: {

                if (validation_mpin_detail()) {

                    if (new InternetCheck().isConnected(TransferFloats.this)) {

                        MyApplication.showloader(TransferFloats.this, getString(R.string.getting_user_info));

                        mpin_verify();

                    } else {
                        Toast.makeText(TransferFloats.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                    }
                }

            }
            break;

            case R.id.exportReceipt_textview: {

                Bitmap bitmap = getScreenShot(rootView);
                store(bitmap, "test.jpg");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    public static Bitmap getScreenShot(View view) {
        View screenView = view;
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public  void store(Bitmap bm, String fileName){
        final  String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        shareImage(file);
    }

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


            case R.id.spinner_currency:
            {
                selectCurrecnyName = arrayList_currecnyName.get(i);
                selectCurrecnyCode = arrayList_currecnyCode.get(i);

                System.out.println(selectCurrecnyCode);

                //  Toast.makeText(SellFloat.this, ""+selectCurrecnyName+"("+selectCurrecnyCode+")", Toast.LENGTH_SHORT).show();
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


}