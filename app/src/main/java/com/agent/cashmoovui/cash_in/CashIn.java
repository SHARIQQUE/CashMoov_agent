package com.agent.cashmoovui.cash_in;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.login.LoginMsis;
import com.agent.cashmoovui.login.LoginPin;
import com.agent.cashmoovui.otp.VerifyLoginAccountScreen;
import com.agent.cashmoovui.set_pin.AESEncryption;
import com.agent.cashmoovui.settings.EditProfile;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;

public class CashIn  extends AppCompatActivity implements View.OnClickListener {
    public static LoginPin loginpinC;
    ImageButton qrCode_imageButton;
    ImageView imgBack,imgHome;
    View rootView;

    String walletOwnerCode="";

    EditText etPin;
    TextView exportReceipt_textview,tv_nextClick,rp_tv_senderName,rp_tv_mobileNumber,rp_tv_businessType,rp_tv_email,rp_tv_country,rp_tv_receiverName,rp_tv_transactionAmount
            ,rp_tv_fees_reveiewPage,receiptPage_tv_stransactionType, receiptPage_tv_dateOfTransaction, receiptPage_tv_transactionAmount,
            receiptPage_tv_amount_to_be_credit, receiptPage_tv_fee, receiptPage_tv_financialtax, receiptPage_tv_transaction_receiptNo,receiptPage_tv_sender_name,
            receiptPage_tv_sender_phoneNo,
            receiptPage_tv_receiver_name, receiptPage_tv_receiver_phoneNo, close_receiptPage_textview,rp_tv_financialTax,rp_tv_amount_to_be_charge,rp_tv_amount_to_be_credit,previous_reviewClick_textview,confirm_reviewClick_textview;
    LinearLayout ll_page_1,ll_reviewPage,ll_receiptPage,main_layout;

    MyApplication applicationComponentClass;
    String languageToUse = "";

    EditText edittext_mobileNuber,edittext_amount,et_mpin;

    String mobileNoStr="",amountstr="",desWalletOwnerCode_from_currency="";

    String walletOwnerCode_mssis_agent="",walletOwnerCode_subs, senderNameAgent="";

    String  currencyCode_agent="",countryCode_agent="",currencyName_agent="";

    String tax_financial="",fees_amount,totalAmount_str,receivernameStr="";
    Double tax_financial_double=0.0,amountstr_double=0.0,fees_amount_double=0.0,totalAmount_double=0.0;

   String mpinStr="";


    String  serviceCode_from_serviceCategory="",serviceCategoryCode_from_serviceCategory="",serviceProviderCode_from_serviceCategory;




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


            setContentView(R.layout.activity_cashin);
            setBackMenu();

            rootView = getWindow().getDecorView().findViewById(R.id.main_layout);
            //     First page

            ll_page_1 = (LinearLayout) findViewById(R.id.ll_page_1);

            tv_nextClick = (TextView) findViewById(R.id.tv_nextClick);
            edittext_mobileNuber = (EditText) findViewById(R.id.edittext_mobileNuber);
            edittext_amount = (EditText) findViewById(R.id.edittext_amount);

            //    Reveiw page

            ll_reviewPage = (LinearLayout) findViewById(R.id.ll_reviewPage);


            rp_tv_senderName = (TextView) findViewById(R.id.rp_tv_senderName);
            rp_tv_mobileNumber = (TextView) findViewById(R.id.rp_tv_mobileNumber);
            rp_tv_businessType = (TextView) findViewById(R.id.rp_tv_businessType);
            rp_tv_email = (TextView) findViewById(R.id.rp_tv_email);
            rp_tv_country = (TextView) findViewById(R.id.rp_tv_country);
            rp_tv_receiverName = (TextView) findViewById(R.id.rp_tv_receiverName);
            rp_tv_transactionAmount = (TextView) findViewById(R.id.rp_tv_transactionAmount);
            rp_tv_fees_reveiewPage = (TextView) findViewById(R.id.rp_tv_fees_reveiewPage);
            rp_tv_financialTax = (TextView) findViewById(R.id.rp_tv_financialTax);
            rp_tv_amount_to_be_charge = (TextView) findViewById(R.id.rp_tv_amount_to_be_charge);
            rp_tv_amount_to_be_credit = (TextView) findViewById(R.id.rp_tv_amount_to_be_credit);


            et_mpin = (EditText) findViewById(R.id.et_mpin);
            previous_reviewClick_textview = (TextView) findViewById(R.id.previous_reviewClick_textview);
            confirm_reviewClick_textview = (TextView) findViewById(R.id.confirm_reviewClick_textview);

            //    Receipt page

            ll_receiptPage = (LinearLayout) findViewById(R.id.ll_receiptPage);
            main_layout = (LinearLayout) findViewById(R.id.main_layout);
            exportReceipt_textview = (TextView) findViewById(R.id.exportReceipt_textview);
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

            edittext_mobileNuber.setEnabled(true);


            walletOwnerCode_mssis_agent = MyApplication.getSaveString("USERCODE", CashIn.this);



        }
        catch (Exception e)
        {
            Toast.makeText(CashIn.this, e.toString(), Toast.LENGTH_LONG).show();

        }

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
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    boolean validation_mobile_Details()
    {

        mobileNoStr = edittext_mobileNuber.getText().toString().trim();
        amountstr = edittext_amount.getText().toString().trim();


        if(mobileNoStr.isEmpty()) {

            MyApplication.showErrorToast(this,getString(R.string.val_phone));

            return false;
        }

        else if(mobileNoStr.length() < 9) {

            MyApplication.showErrorToast(this,getString(R.string.val_phone));

            return false;
        }

       else if(amountstr.isEmpty()) {

            MyApplication.showErrorToast(this,getString(R.string.plz_enter_amount));

            return false;
        }

        else if(amountstr.trim().length() < 4) {

            MyApplication.showErrorToast(this,getString(R.string.minimum_amount_1000));

            return false;
        }


        return true;
    }


    private void subscriber_details_api_walletownerUser() {

        String walletOwnerCategoryCode =  MyApplication.getSaveString("walletOwnerCategoryCode",CashIn.this);

        walletOwnerCategoryCode ="100010"; // HARD CODE FINAL ACORDING TO PARVEEN


        API.GET_CASHIN_DETAILS("ewallet/api/v1/walletOwner/all?walletOwnerCategoryCode="+walletOwnerCategoryCode+"&mobileNumber="+mobileNoStr+"&offset=0&limit=500",languageToUse,new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {

                        //JSONObject jsonObject = new JSONObject("{"transactionId":"1789327","requestTime":"Wed Oct 20 15:55:16 IST 2021","responseTime":"Wed Oct 20 15:55:16 IST 2021","resultCode":"0","resultDescription":"Transaction Successful","pageable":{"limit":500,"offset":0,"totalRecords":1},"walletOwnerList":[{"id":110382,"code":"1000002488","walletOwnerCategoryCode":"100010","ownerName":"Kundan","mobileNumber":"118110111","idProofNumber":"vc12345","email":"kundan.kumar@esteltelecom.com","status":"Active","state":"Approved","stage":"Document","idProofTypeCode":"100006","idProofTypeName":"OTHER","idExpiryDate":"2021-09-29","notificationLanguage":"en","notificationTypeCode":"100000","notificationName":"EMAIL","gender":"M","dateOfBirth":"1960-01-26","lastName":"New","issuingCountryCode":"100092","issuingCountryName":"Guinea","registerCountryCode":"100092","registerCountryName":"Guinea","createdBy":"100375","modifiedBy":"100322","creationDate":"2021-09-16T17:08:49.796+0530","modificationDate":"2021-09-16T17:10:17.009+0530","walletExists":true,"profileTypeCode":"100001","profileTypeName":"tier2","walletOwnerCatName":"Subscriber","occupationTypeCode":"100002","occupationTypeName":"Others","requestedSource":"ADMIN","regesterCountryDialCode":"+224","issuingCountryDialCode":"+224","walletOwnerCode":"1000002488"}]}");

                        String resultCode =  jsonObject.getString("resultCode");
                        String resultDescription =  jsonObject.getString("resultDescription");

                        if(resultCode.equalsIgnoreCase("0")) {



                          //  Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();


                            JSONArray jsonArray = jsonObject.getJSONArray("walletOwnerList");
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                walletOwnerCode_subs = jsonObject2.getString("walletOwnerCode");

                                rp_tv_mobileNumber.setText(MyApplication.getSaveString("USERNAME",CashIn.this));
                                rp_tv_email.setText(jsonObject2.getString("email"));
                                rp_tv_country.setText(jsonObject2.getString("issuingCountryName"));

                                receivernameStr=jsonObject2.getString("ownerName");
                                rp_tv_receiverName.setText(receivernameStr);


//                                JSONObject walletTransfer = jsonObject.getJSONObject("walletTransfer");
//                                JSONObject srcWalletOwner = walletTransfer.getJSONObject("srcWalletOwner");
//                                rp_tv_businessType.setText(srcWalletOwner.getString("businessTypeName"));


                            }

                            currency_api();

                        }

                            else {
                                Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();
                              //  finish();
                            }


                    }
                    catch (Exception e)
                    {
                        Toast.makeText(CashIn.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

           }


                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();
                    Toast.makeText(CashIn.this, aFalse, Toast.LENGTH_SHORT).show();
                    finish();

                     }
            });


    }

    private void currency_api() {

        API.GET_CASHIN_DETAILS("ewallet/api/v1/walletOwnerCountryCurrency/"+walletOwnerCode_subs,languageToUse,new Api_Responce_Handler() {
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

                            currencyName_agent = jsonObject2.getString("currencyName");
                            countryCode_agent = jsonObject2.getString("countryCurrencyCode");
                            currencyCode_agent = jsonObject2.getString("currencyCode");
                            desWalletOwnerCode_from_currency = jsonObject2.getString("walletOwnerCode");

                        }



                      //  Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();


                       exchange_rate_api();


                    }

                    else {
                        Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(CashIn.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashIn.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void service_Provider_api() {

        // Hard Code Final Deepak

        API.GET_CASHIN_DETAILS("ewallet/api/v1/serviceProvider/serviceCategory?serviceCode=100003&serviceCategoryCode=100011&status=Y",languageToUse,new Api_Responce_Handler() {
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

                        agent_details_api_walletownerUser();


                    }

                    else {
                        Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(CashIn.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashIn.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    private void agent_details_api_walletownerUser() {

        String USER_CODE_FROM_TOKEN_AGENTDETAILS =  MyApplication.getSaveString("userCode",CashIn.this);


        API.GET_CASHIN_DETAILS("ewallet/api/v1/walletOwnerUser/"+USER_CODE_FROM_TOKEN_AGENTDETAILS,languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                   // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1789408\",\"requestTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"responseTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwnerUser\":{\"id\":2171,\"code\":\"101917\",\"firstName\":\"TATASnegal\",\"userName\":\"TATASnegal5597\",\"mobileNumber\":\"8888888882\",\"email\":\"kundan.kumar@esteltelecom.com\",\"walletOwnerUserTypeCode\":\"100000\",\"walletOwnerUserTypeName\":\"Supervisor\",\"walletOwnerCategoryCode\":\"100000\",\"walletOwnerCategoryName\":\"Institute\",\"userCode\":\"1000002606\",\"status\":\"Active\",\"state\":\"Approved\",\"gender\":\"M\",\"idProofTypeCode\":\"100004\",\"idProofTypeName\":\"MILITARY ID CARD\",\"idProofNumber\":\"44444444444\",\"creationDate\":\"2021-10-01T09:04:07.330+0530\",\"notificationName\":\"EMAIL\",\"notificationLanguage\":\"en\",\"createdBy\":\"100308\",\"modificationDate\":\"2021-10-20T14:59:00.791+0530\",\"modifiedBy\":\"100308\",\"addressList\":[{\"id\":3569,\"walletOwnerUserCode\":\"101917\",\"addTypeCode\":\"100001\",\"addTypeName\":\"Commercial\",\"regionCode\":\"100068\",\"regionName\":\"Boke\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"city\":\"100022\",\"cityName\":\"Dubreka\",\"addressLine1\":\"delhi\",\"status\":\"Inactive\",\"creationDate\":\"2021-10-01T09:04:07.498+0530\",\"createdBy\":\"100250\",\"modificationDate\":\"2021-10-03T09:52:57.407+0530\",\"modifiedBy\":\"100308\"}],\"workingDaysList\":[{\"id\":3597,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100000\",\"weekdaysName\":\"Monday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.518+0530\"},{\"id\":3598,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100001\",\"weekdaysName\":\"Tuesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.528+0530\"},{\"id\":3599,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100002\",\"weekdaysName\":\"Wednesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.538+0530\"},{\"id\":3600,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100003\",\"weekdaysName\":\"Thursday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.547+0530\"},{\"id\":3601,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100004\",\"weekdaysName\":\"Friday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.556+0530\"},{\"id\":3602,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100005\",\"weekdaysName\":\"Saturday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.565+0530\"},{\"id\":3603,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100006\",\"weekdaysName\":\"Sunday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"2:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.573+0530\"}],\"macEnabled\":false,\"ipEnabled\":false,\"resetCredReqInit\":false,\"notificationTypeCode\":\"100000\"}}");

                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");

                    if(resultCode.equalsIgnoreCase("0")) {

                         JSONObject walletOwnerUser = jsonObject.getJSONObject("walletOwnerUser");

                          senderNameAgent=walletOwnerUser.getString("firstName");
                          rp_tv_senderName.setText(senderNameAgent);

                        subscriber_details_api_walletownerUser();



                    }

                    else {
                        Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(CashIn.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashIn.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void allByCriteria_walletOwnerCode_api() {


        API.GET_CASHIN_DETAILS("ewallet/api/v1/channel/allByCriteria?walletOwnerCode="+walletOwnerCode_mssis_agent,languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    //JSONObject jsonObject = new JSONObject("{"transactionId":"1789322","requestTime":"Wed Oct 20 15:53:33 IST 2021","responseTime":"Wed Oct 20 15:53:33 IST 2021","resultCode":"0","resultDescription":"Transaction Successful","walletOwnerCountryCurrencyList":[{"id":7452,"code":"107451","walletOwnerCode":"1000002488","currencyCode":"100062","currencyName":"GNF","currencySymbol":"Fr","countryCurrencyCode":"100076","inBound":true,"outBound":true,"status":"Active"}]}");

                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");

                    if(resultCode.equalsIgnoreCase("0")) {



                        ll_page_1.setVisibility(View.GONE);
                        ll_reviewPage.setVisibility(View.VISIBLE);
                        ll_receiptPage.setVisibility(View.GONE);


                    }

                    else {
                        Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();
                     //   finish();
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(CashIn.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashIn.this, aFalse, Toast.LENGTH_SHORT).show();
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


    private void mpin_final_api() {

        try {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("srcWalletOwnerCode",walletOwnerCode_mssis_agent);  // Agent  srcWalletOwnerCode

//                    jsonObject.put("desWalletOwnerCode","1000002488");  // Subscriber
//                    jsonObject.put("srcCurrencyCode","100062");         // source  srcWalletOwnerCode
//                    jsonObject.put("desCurrencyCode","100062");         //  Subscriber


                    jsonObject.put("desWalletOwnerCode",desWalletOwnerCode_from_currency);  // Subscriber
                  //  jsonObject.put("srcCurrencyCode","100062");         // source  srcWalletOwnerCode
                  //  jsonObject.put("desCurrencyCode","100062");         //  Subscriber
            jsonObject.put("srcCurrencyCode",currencyCode_agent);         // source  srcWalletOwnerCode
            jsonObject.put("desCurrencyCode","100062");         //  Subscriber


            jsonObject.put("value",amountstr);

                    String encryptionDatanew = AESEncryption.getAESEncryption(mpinStr);
                    jsonObject.put("pin",encryptionDatanew);


                    jsonObject.put("transactionType","100000");         // Hard Code according  to Deepak
                    jsonObject.put("channelTypeCode","2");           // Hard Code according  to Deepak
                  //  jsonObject.put("serviceCode","100003");          // Hard Code according  to Deepak
                    jsonObject.put("serviceCode",serviceCode_from_serviceCategory);
                  //  jsonObject.put("serviceCategoryCode","100011");  // Hard Code according  to Deepak
                    jsonObject.put("serviceCategoryCode",serviceCategoryCode_from_serviceCategory);  // Hard Code according  to Deepak
                    jsonObject.put("serviceProviderCode",serviceProviderCode_from_serviceCategory);  // Hard Code according  to Deepak
                   // jsonObject.put("serviceProviderCode","100106");  // Hard Code according  to Deepak




        API.POST_CASHIN_MPIN("ewallet/api/v1/walletTransfer/cashIn", jsonObject, languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                  // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"116204\",\"requestTime\":\"Wed Oct 20 19:51:47 IST 2021\",\"responseTime\":\"Wed Oct 20 19:51:47 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletTransfer\":{\"code\":\"116204\",\"srcWalletCode\":\"1000024941\",\"desWalletCode\":\"1000022471\",\"srcWalletOwnerCode\":\"1000002606\",\"desWalletOwnerCode\":\"1000002488\",\"srcWalletTypeCode\":\"100008\",\"desWalletTypeCode\":\"100008\",\"srcCurrencyCode\":\"100062\",\"desCurrencyCode\":\"100062\",\"srcCurrencyName\":\"GNF\",\"desCurrencyName\":\"GNF\",\"srcCurrencySymbol\":\"Fr\",\"desCurrencySymbol\":\"Fr\",\"value\":2130,\"createdBy\":\"101917\",\"creationDate\":\"2021-10-20 19:51:47\",\"fee\":1000,\"finalAmount\":1000,\"srcWalletOwner\":{\"id\":110500,\"code\":\"1000002606\",\"walletOwnerCategoryCode\":\"100000\",\"ownerName\":\"TATASnegal\",\"mobileNumber\":\"8888888882\",\"businessTypeCode\":\"100001\",\"businessTypeName\":\"Telecom\",\"idProofNumber\":\"44444444444\",\"email\":\"kundan.kumar@esteltelecom.com\",\"status\":\"Active\",\"state\":\"Approved\",\"stage\":\"Document\",\"idProofTypeCode\":\"100004\",\"idProofTypeName\":\"MILITARY ID CARD\",\"notificationLanguage\":\"en\",\"notificationTypeCode\":\"100000\",\"notificationName\":\"EMAIL\",\"registerCountryCode\":\"100092\",\"registerCountryName\":\"Guinea\",\"createdBy\":\"100250\",\"modifiedBy\":\"100308\",\"creationDate\":\"2021-10-01T09:01:15.968+0530\",\"modificationDate\":\"2021-10-01T09:10:25.037+0530\",\"walletExists\":true,\"profileTypeCode\":\"100000\",\"profileTypeName\":\"tier1\",\"walletCurrencyList\":[\"100014\",\"100013\",\"100012\",\"100007\",\"100010\",\"100008\",\"100005\",\"100002\",\"100001\",\"100003\",\"100069\",\"100062\",\"100004\",\"100000\",\"100028\",\"100027\",\"100026\",\"100024\",\"100021\",\"100020\",\"100019\",\"100017\",\"100015\",\"100018\",\"100058\"],\"walletOwnerCatName\":\"Institute\",\"requestedSource\":\"ADMIN\",\"regesterCountryDialCode\":\"+224\",\"walletOwnerCode\":\"1000002606\"},\"desWalletOwner\":{\"id\":110382,\"code\":\"1000002488\",\"walletOwnerCategoryCode\":\"100010\",\"ownerName\":\"Kundan\",\"mobileNumber\":\"118110111\",\"idProofNumber\":\"vc12345\",\"email\":\"kundan.kumar@esteltelecom.com\",\"status\":\"Active\",\"state\":\"Approved\",\"stage\":\"Document\",\"idProofTypeCode\":\"100006\",\"idProofTypeName\":\"OTHER\",\"idExpiryDate\":\"2021-09-29\",\"notificationLanguage\":\"en\",\"notificationTypeCode\":\"100000\",\"notificationName\":\"EMAIL\",\"gender\":\"M\",\"dateOfBirth\":\"1960-01-26\",\"lastName\":\"New\",\"issuingCountryCode\":\"100092\",\"issuingCountryName\":\"Guinea\",\"registerCountryCode\":\"100092\",\"registerCountryName\":\"Guinea\",\"createdBy\":\"100375\",\"modifiedBy\":\"100322\",\"creationDate\":\"2021-09-16T17:08:49.796+0530\",\"modificationDate\":\"2021-09-16T17:10:17.009+0530\",\"walletExists\":true,\"profileTypeCode\":\"100001\",\"profileTypeName\":\"tier2\",\"walletOwnerCatName\":\"Subscriber\",\"occupationTypeCode\":\"100002\",\"occupationTypeName\":\"Others\",\"requestedSource\":\"ADMIN\",\"regesterCountryDialCode\":\"+224\",\"issuingCountryDialCode\":\"+224\",\"walletOwnerCode\":\"1000002488\"},\"taxConfigurationList\":[{\"taxTypeCode\":\"100133\",\"taxTypeName\":\"Financial Tax\",\"value\":130,\"taxAvailBy\":\"Fee Amount\"}],\"transactionType\":\"CASH-IN\"}}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {

                        ll_page_1.setVisibility(View.GONE);
                        ll_reviewPage.setVisibility(View.GONE);
                        ll_receiptPage.setVisibility(View.VISIBLE);

                        receiptPage_tv_stransactionType.setText("CASH-IN");
                        receiptPage_tv_transactionAmount.setText(amountstr);
                        receiptPage_tv_fee.setText(fees_amount);
                        receiptPage_tv_financialtax.setText(tax_financial);


                        receiptPage_tv_transaction_receiptNo.setText(jsonObject.getString("transactionId"));
                        receiptPage_tv_dateOfTransaction.setText(jsonObject.getString("responseTime"));
                        receiptPage_tv_amount_to_be_credit.setText(amountstr);

                        receiptPage_tv_sender_name.setText(senderNameAgent);
                        receiptPage_tv_sender_phoneNo.setText(MyApplication.getSaveString("USERNAME",CashIn.this));

                        receiptPage_tv_sender_name.setText(senderNameAgent);
                        receiptPage_tv_sender_phoneNo.setText(MyApplication.getSaveString("USERNAME",CashIn.this));

                        receiptPage_tv_receiver_name.setText(receivernameStr);
                        receiptPage_tv_receiver_phoneNo.setText(mobileNoStr);


                    } else {
                        Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();
                        //  finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(CashIn.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();

                if (aFalse.equalsIgnoreCase("1251")) {
                    Intent i = new Intent(CashIn.this, VerifyLoginAccountScreen.class);
                    startActivity(i);
                    finish();
                }
                else

                Toast.makeText(CashIn.this, aFalse, Toast.LENGTH_SHORT).show();
            }
        });

    }
        catch (Exception e)
        {
            Toast.makeText(CashIn.this,e.toString(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }

    private void exchange_rate_api() {

       // API.GET_CASHIN_DETAILS("ewallet/api/v1/exchangeRate/getAmountDetails?sendCurrencyCode=100062&receiveCurrencyCode=100062&sendCountryCode=100092&receiveCountryCode=&currencyValue=1000&channelTypeCode=100000&serviceCode=100003&serviceCategoryCode=100011&serviceProviderCode=100106&walletOwnerCode=1000002606&remitAgentCode=1000002606&payAgentCode=1000002488",languageToUse,new Api_Responce_Handler() {





        API.GET_CASHIN_DETAILS("ewallet/api/v1/exchangeRate/getAmountDetails?sendCurrencyCode="+currencyCode_agent+"&receiveCurrencyCode=100062&sendCountryCode="+countryCode_agent+"&receiveCountryCode=" +
                "&currencyValue="+amountstr+"&channelTypeCode=2&serviceCode="+serviceCode_from_serviceCategory+"&serviceCategoryCode="+serviceCategoryCode_from_serviceCategory+"&serviceProviderCode="+serviceProviderCode_from_serviceCategory+"&walletOwnerCode="+walletOwnerCode_mssis_agent+"&remitAgentCode="+walletOwnerCode_mssis_agent+"&payAgentCode=1000002488",languageToUse,new Api_Responce_Handler() {
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
                        rp_tv_fees_reveiewPage.setText(fees_amount);

                      //  credit_amount=exchangeRate.getString("currencyValue");


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







                        rp_tv_financialTax.setText(tax_financial);

                         tax_financial_double = Double.parseDouble(tax_financial);
                       //  credit_amount_double = Double.parseDouble(credit_amount);
                         fees_amount_double = Double.parseDouble(fees_amount);
                        amountstr_double = Double.parseDouble(amountstr);

                        totalAmount_double = tax_financial_double+amountstr_double+fees_amount_double;
                         totalAmount_str = String.valueOf(totalAmount_double);
                         rp_tv_amount_to_be_charge.setText(totalAmount_str);

                        amountstr = String.valueOf(amountstr_double);
                        rp_tv_transactionAmount.setText(amountstr);
                        rp_tv_amount_to_be_credit.setText(amountstr);

                        allByCriteria_walletOwnerCode_api();

                    }

                    else {
                        Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(CashIn.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(CashIn.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_nextClick: {


                if (validation_mobile_Details()) {


                    if (new InternetCheck().isConnected(CashIn.this)) {

                        MyApplication.showloader(CashIn.this, getString(R.string.getting_user_info));
                        service_Provider_api();

                    } else {
                        Toast.makeText(CashIn.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                    }
                }
            }

            break;

            case R.id.confirm_reviewClick_textview: {

                if (validation_mpin_detail()) {

                    if (new InternetCheck().isConnected(CashIn.this)) {

                        MyApplication.showloader(CashIn.this, getString(R.string.getting_user_info));

                        mpin_final_api();

                    } else {
                        Toast.makeText(CashIn.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
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
                        edittext_mobileNuber.setEnabled(true);

                    }
                    else {


                        String[] qrData = str.split("\\:");
                        mobileNoStr=qrData[0];
                        edittext_mobileNuber.setText(mobileNoStr);
                        edittext_mobileNuber.setEnabled(false);



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

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.share_screenshot)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_app_available), Toast.LENGTH_SHORT).show();
        }
    }


}