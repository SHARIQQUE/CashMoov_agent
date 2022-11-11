package com.agent.cashmoovui.internationaltransfer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.model.AmountDetailsInfoModel;
import com.agent.cashmoovui.model.CountryCurrencyInfoModel;
import com.agent.cashmoovui.model.CountryInfoModel;
import com.agent.cashmoovui.model.SubscriberInfoModel;
import com.agent.cashmoovui.receive_money.AddBeneficiaryToSubscriber;
import com.aldoapps.autoformatedittext.AutoFormatUtil;
import com.blikoon.qrcodescanner.QrCodeActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class Inform extends AppCompatActivity implements View.OnClickListener {
    public static Inform tosubscriberC;
    ImageView imgBack,imgHome;
    public  static TextView spBenifiCurr,spCountry,tvAmtCurr,tvSend,headText,tvFee,tvAmtPaid,tvRate;
    public static String serviceProvider,currencyValue,fee,rate,exRateCode,currency,fromCurrency,fromCurrencySymbol,toCurrencySymbol;
    private boolean isAmt=true;
    private boolean isAmtPaid=true;
    AutoCompleteTextView etSubscriberNo;
    public static EditText etFname,etLname,etAmount,etAmountN;
    TextView etName,etPhone,tvAmtCurrN;
    private boolean isQR;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    public static final int REQUEST_CODE = 1;
    private String current = "";

    private ArrayList<String> benefiCurrencyList = new ArrayList<>();
    public static ArrayList<CountryCurrencyInfoModel.CountryCurrency> benefiCurrencyModelList = new ArrayList<>();

    private ArrayList<String> benefiCountryList = new ArrayList<>();
    public static ArrayList<CountryInfoModel.Country> benefiCountryModelList = new ArrayList<>();

    SpinnerDialog spinnerDialogBenefiCountry;

    private String  countrycode;
    private LinearLayout xofAmountLinear;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_form);
        tosubscriberC=this;
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
                MyApplication.hideKeyboard(tosubscriberC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.isFirstTime=false;
                MyApplication.hideKeyboard(tosubscriberC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    public void qrScan(View v){
        Intent i = new Intent(tosubscriberC, QrCodeActivity.class);
        startActivityForResult( i,REQUEST_CODE_QR_SCAN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK) {

            MyApplication.isContact=false;
            String requiredValue = data.getStringExtra("PHONE");
            MyApplication.contactValidation(requiredValue,etSubscriberNo);

            //  etSubscriberNo.setText(requiredValue);

        }
        if (resultCode != Activity.RESULT_OK) {
            Log.d("LOGTAG", "COULD NOT GET A GOOD RESULT.");
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (result != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(tosubscriberC).create();
                alertDialog.setTitle(getString(R.string.scan_error));
                alertDialog.setMessage(getString(R.string.val_scan_error_content));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
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
            isQR=true;
            callwalletOwnerDetailsQR(date[0]);

        }
    }

    public boolean isSet=false;
    public static  JSONObject dataToSend=new JSONObject();
    public static int receiverFee,receiverTax;

    public static String mobileNo,ownerName,lastName,confCode,currencySymbol;

    private void getIds() {
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etSubscriberNo = findViewById(R.id.etSubscriberNo);
        etFname = findViewById(R.id.etFname);
        etLname = findViewById(R.id.etLname);
        tvAmtCurr = findViewById(R.id.tvAmtCurr);
        etAmount = findViewById(R.id.etAmount);
        etAmountN= findViewById(R.id.etAmountN);
        tvRate = findViewById(R.id.tvRate);
        tvFee = findViewById(R.id.tvFee);
        tvAmtCurrN=findViewById(R.id.tvAmtCurrN);
        spCountry = findViewById(R.id.spinner_senderCountry);
        xofAmountLinear=findViewById(R.id.xofAmountLinear);
       // etSubscriberNo.setText("111111111");
       // etFname.setText("Ravi");
       // etLname.setText("Singh");
       // etAmount.setText("9,329.11");
      //  etAmountN.setText("670");

        mobileNo = etSubscriberNo.getText().toString();
        ownerName =  etFname.getText().toString();
        lastName =  etLname.getText().toString();
        headText = findViewById(R.id.headText);
        headText.setText(InTransfer.operatorNname);

        tvSend = findViewById(R.id.tvSend);
        etFname.setEnabled(true);
        etLname.setEnabled(true);

        etSubscriberNo.setText("775389850");
        etFname.setText("Sandeep");
        etLname.setText("Singh");

        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);
        //  agent_mob_no.setText("9078678111");
        //agent_mob_no.setText("");

        callApiCountry();
        spCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogBenefiCountry!=null)
                    spinnerDialogBenefiCountry.showSpinerDialog();
            }
        });



        etSubscriberNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (etSubscriberNo.getRight() - etSubscriberNo.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here

                        MyApplication.isContact=true;
                        Intent intent = new Intent(Inform.this,
                                AddBeneficiaryToSubscriber.class);
                        startActivityForResult(intent , REQUEST_CODE);

                        return true;
                    }
                }
                return false;
            }
        });

        etSubscriberNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               /* Matcher m = p.matcher(s);
                if(s.length()>=9 && m.matches()){
                    if(isSet) {
                        isSet=false;
                    }else{
                        etFname.getText().clear();
                        etLname.getText().clear();
                        //etAmount.getText().clear();
                        callApiSubsriberList();
                    }
                }else{
                    etFname.getText().clear();
                    etLname.getText().clear();
                }*/
            }
        });
callApiWalletCountryCurrencyJSOn();
        etSubscriberNo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                if(value.contains(",")) {
                    String[] list = value.split(",");
                    isSet = true;
                    etSubscriberNo.setText(list[0]);
                    //etFname.setText(list[1]);
                   // etLname.setText(list[2]);
                   // etAmount.setText("");
                    etAmount.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(etAmount, InputMethodManager.SHOW_IMPLICIT);
                }else{
                    etFname.setText("");
                    etLname.setText("");
                }

            }
        });
        callwalletOwner();
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                  /* if (isFormatting) {
                        return;
                    }*/

                if(s.length()>=1) {
                    // formatInput(etAmount,s, s.length(), s.length());

                    if(isAmt){
                        etAmountN.setEnabled(false);
                        isAmtPaid=false;
                        callApiAmountDetailsstatic();
                    }else{
                        etAmountN.setEnabled(true);
                        isAmtPaid=true;
                    }
                }else{
                    isAmtPaid=true;
                    etAmountN.setEnabled(true);
                    etAmountN.getText().clear();
                    tvFee.setText("");
                   // tvAmtPaid.setText("");
                    tvRate.setText("");
                }


                // isFormatting = false;



            }
        });


        etAmountN.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               /* if (spBenifiCurr.getText().toString().equals(getString(R.string.valid_select_benifi_curr))) {
                    MyApplication.showErrorToast(internationalC, getString(R.string.val_select_curr));
                    return;
                }*/
                /*if (isFormatting) {
                    return;
                }*/

                if(s.length()>=1) {
                    //  formatInput(etAmountNew,s, s.length(), s.length());
                    if(isAmtPaid){
                        etAmount.setEnabled(false);
                        isAmt=false;
                        callApiAmountDetailsstaticreverse();
                    }else{
                        etAmount.setEnabled(true);
                        isAmt=true;
                    }

                }else{
                    isAmt=true;
                    etAmount.setEnabled(true);
                    etAmount.getText().clear();
                    tvFee.setText("");
                   // tvAmtPaid.setText("");
                    tvRate.setText("");
                }

                // isFormatting = false;

            }


        });


        callApiAmountDetailsJSON();
        setOnCLickListener();

    }

    private void callApiAmountDetailsstaticreverse() {
        try {
            //MyApplication.showloader(cashinC, "Please wait!");
            API.GET("ewallet/api/v1/exchangeRate/getAmountDetails?"+"sendCurrencyCode="+"100062"+
                            "&receiveCurrencyCode="+"100018"+
                            "&sendCountryCode="+"1000092"
                            +"&receiveCountryCode="+"100195"+
                            "&currencyValue="+etAmountN.getText().toString().replace(",","")+
                            "&channelTypeCode="+MyApplication.channelTypeCode+
                            "&serviceCode="+"100023"
                            +"&serviceCategoryCode="+"TRFWLT"+
                            "&serviceProviderCode="+"100183"+
                            "&walletOwnerCode="+MyApplication.getSaveString("walletOwnerCode", tosubscriberC),
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            // MyApplication.hideLoader();
                            System.out.println("International response======="+jsonObject.toString());
                            if (jsonObject != null) {
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    if(etAmountN.getText().toString().trim().replace(",","").length()>0) {
                                        JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("exchangeRate");

                                        try {
                                            double currValue = Double.parseDouble(etAmountN.getText().toString().trim().replace(",",""));
                                            double value = jsonObjectAmountDetails.optDouble("value");
                                            if (value == 0 || value == .0 || value == 0.0 || value == 0.00 || value == 0.000) {
                                              //  tvAmtPaid.setText(String.valueOf(currValue));
                                            } else {
                                                String finalValue = MyApplication.addDecimal(""+currValue / value);
                                                // tvAmtPaid.setText(finalValue);
                                                etAmount.setText(finalValue);
                                            }

                                        } catch (Exception e) {
                                        }

                                        currencyValue = etAmountN.getText().toString().trim().replace(",","");
                                        //currencyValue= df.format(jsonObjectAmountDetails.optDouble("currencyValue"));
                                        fee = df.format(jsonObjectAmountDetails.optDouble("fee"));
                                        rate = jsonObjectAmountDetails.optString("value");
                                        exRateCode = jsonObjectAmountDetails.optString("code");
                                        //receiverFee= jsonObjectAmountDetails.optInt("receiverFee");
                                        //receiverTax = jsonObjectAmountDetails.optInt("receiverTax");
                                        //etAmountNew.setText(currencyValue);
                                        tvRate.setText(rate);
                                        tvFee.setText(fee);
                                         //etAmountN.setText(currencyValue);
                                        //tvAmtPaid.setText(currencyValue);

//                                    int tax = receiverFee+receiverTax;
//                                    if(currencyValue<tax){
//                                        tvNext.setVisibility(View.GONE);
//                                        MyApplication.showErrorToast(tononsubscriberC,getString(R.string.fee_tax_greater_than_trans_amt));
//                                    }else{
//                                        tvNext.setVisibility(View.VISIBLE);
//                                    }

                                        if (jsonObjectAmountDetails.has("taxConfigurationList")) {
                                            taxConfigurationList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
                                        } else {
                                            taxConfigurationList = null;
                                        }

                                        if(jsonObjectAmountDetails.has("receiverTax")) {
                                            taxConfigurationList=null;
                                        }
                                    }
                                } else {
                                    MyApplication.showToast(Inform.this,jsonObject.optString("resultDescription", "N/A"));
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
    private void callApiAmountDetailsstatic() {

        try {
            //MyApplication.showloader(cashinC, "Please wait!");
            API.GET("ewallet/api/v1/exchangeRate/getAmountDetails?"+"sendCurrencyCode="+"100062"+
                            "&receiveCurrencyCode="+"100018"+
                            "&sendCountryCode="+"1000092"
                            +"&receiveCountryCode="+"100195"+
                            "&currencyValue="+etAmount.getText().toString().replace(",","")+
                            "&channelTypeCode="+MyApplication.channelTypeCode+
                            "&serviceCode="+"100023"
                            +"&serviceCategoryCode="+"TRFWLT"+
                            "&serviceProviderCode="+"100183"+
                            "&walletOwnerCode="+MyApplication.getSaveString("walletOwnerCode", tosubscriberC),
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            // MyApplication.hideLoader();
                            System.out.println("International response======="+jsonObject.toString());
                            if (jsonObject != null) {
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    if(etAmount.getText().toString().trim().replace(",","").length()>0) {
                                        JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("exchangeRate");

                                        currencyValue =df.format(jsonObjectAmountDetails.optDouble("currencyValue"));
                                        fee = df.format(jsonObjectAmountDetails.optDouble("fee"));
                                        rate = jsonObjectAmountDetails.optString("value");
                                        exRateCode = jsonObjectAmountDetails.optString("code");
                                        //receiverFee= jsonObjectAmountDetails.optInt("receiverFee");
                                        //receiverTax = jsonObjectAmountDetails.optInt("receiverTax");
                                        //etAmountNew.setText(currencyValue);
                                        tvRate.setText(rate);
                                        tvFee.setText(fee);
                                        etAmountN.setText(currencyValue);
                                        // tvAmtPaid.setText(currencyValue);


//                                    int tax = receiverFee+receiverTax;
//                                    if(currencyValue<tax){
//                                        tvNext.setVisibility(View.GONE);
//                                        MyApplication.showErrorToast(tononsubscriberC,getString(R.string.fee_tax_greater_than_trans_amt));
//                                    }else{
//                                        tvNext.setVisibility(View.VISIBLE);
//                                    }

                                        if (jsonObjectAmountDetails.has("taxConfigurationList")) {
                                            taxConfigurationList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
                                        } else {
                                            taxConfigurationList = null;
                                        }
                                        if(jsonObjectAmountDetails.has("receiverTax")) {
                                            taxConfigurationList=null;
                                        }

                                    }


                                } else {
                                    MyApplication.showToast(Inform.this,jsonObject.optString("resultDescription", "N/A"));
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



    private void setOnCLickListener() {
        tvSend.setOnClickListener(tosubscriberC);

    }





    public static JSONObject jsonObjectNew=new JSONObject();
    @Override
    public void onClick(View view) {
        if(etSubscriberNo.getText().toString().trim().isEmpty()) {
            MyApplication.showErrorToast(tosubscriberC,getString(R.string.val_subscriber_no));
            return;
        }
        if(etSubscriberNo.getText().toString().trim().length()<9) {
            MyApplication.showErrorToast(tosubscriberC,getString(R.string.enter_subscriber_no_val));
            return;
        }

        if(spCountry.getText().toString().equals(getString(R.string.valid_select_rec_country))) {
            MyApplication.showErrorToast(tosubscriberC,getString(R.string.val_select_country));
            return;
        }



        if(etAmount.getText().toString().trim().trim().replace(",","").isEmpty()) {
            MyApplication.showErrorToast(tosubscriberC,getString(R.string.val_amount));
            return;
        }
        if(etAmount.getText().toString().trim().replace(",","").trim().equals("0")||etAmount.getText().toString().trim().replace(",","").equals(".")
                ||etAmount.getText().toString().trim().replace(",","").equals(".0")||
                etAmount.getText().toString().trim().replace(",","").equals("0.")||
                etAmount.getText().toString().trim().replace(",","").equals("0.0")||etAmount.getText().toString().trim().replace(",","").equals("0.00")){
            MyApplication.showErrorToast(tosubscriberC,getString(R.string.val_valid_amount));
            return;
        }


        try {
            jsonObjectNew.put("walletOwnerCode",MyApplication.getSaveString("walletOwnerCode",getApplicationContext()));
            jsonObjectNew.put("channel","SELFCARE");
            jsonObjectNew.put("serviceCode","100023");
            jsonObjectNew.put("serviceCategoryCode","TRFWLT");
            // jsonObject.put("dateOfBirth",etDob.getText().toString().trim());
            jsonObjectNew.put("serviceProviderCode","100183");
            jsonObjectNew.put("requestType","transferin");
            jsonObjectNew.put("serviceItemId",InTransfer.serviceItemId);
            jsonObjectNew.put("fromCurrencyCode","100062");
            jsonObjectNew.put("toCurrencyCode","100018");
            jsonObjectNew.put("mobileNumber","775389850");
            jsonObjectNew.put("amount",etAmount.getText().toString());
            //jsonObject.put("pin","38b0059a03897cc6260e73cfe3f070a3");
            jsonObjectNew.put("providerServiceItemCode","100006");
            jsonObjectNew.put("firstName",etFname.getText().toString());
            jsonObjectNew.put("productCode",InTransfer.operatorCode);
            // jsonObjectNew.put("lastName",etLname.getText().toString());

            // jsonObject.put("idProofNumber","");
            // jsonObject.put("idProofTypeCode","");



        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("Inform request======="+jsonObjectNew.toString());
        Intent i=new Intent(Inform.this, InFormConfirmation.class);
        startActivity(i);
        // callsaveDataApi(jsonObjectNew);
       /* if(mobileNo.toString().trim().isEmpty()) {
            new AlertDialog.Builder(this)
                    //.setTitle(getString(R.string.logout))
                    //.setIcon(R.drawable.ic_logout)
                    .setMessage(getString(R.string.msisdn_not_reg_with_subscriber))
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent i = new Intent(tosubscriberC, ToNonSubscriber.class);
                            i.putExtra("TOSUBMSISDN",etSubscriberNo.getText().toString().trim());
                            i.putExtra("TOSUBAMOUNT",etAmount.getText().toString().trim().replace(",",""));
                            startActivity(i);
                            finish();

                        }
                    }).create().show();
            return;
        }*/
        try{
            dataToSend.put("transactionType","104591");
            dataToSend.put("desWalletOwnerCode","100100383");
          //  dataToSend.put("desWalletOwnerCode",walletOwner.optJSONArray("walletOwnerList").optJSONObject(0).optString("walletOwnerCode"));
            dataToSend.put("srcWalletOwnerCode",MyApplication.getSaveString("walletOwnerCode",tosubscriberC));
            dataToSend.put("srcCurrencyCode","100062");
            dataToSend.put("desCurrencyCode","100062");
            dataToSend.put("value",etAmount.getText().toString().trim().replace(",",""));
            dataToSend.put("channelTypeCode",MyApplication.channelTypeCode);
            dataToSend.put("serviceCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode"));
            dataToSend.put("serviceCategoryCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode"));
            dataToSend.put("serviceProviderCode",serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code"));
            dataToSend.put("bearerAllow", false);
            dataToSend.put("bearerFee", 0.0);
            dataToSend.put("transactionCoordinate",MainActivity.transactionCoordinate);
            dataToSend.put("transactionArea",MainActivity.transactionArea);
            dataToSend.put("isGpsOn",true);

            Intent in=new Intent(tosubscriberC, InFormConfirmation.class);
            startActivity(in);
        }catch (Exception e){

        }

    }

    private void callsaveDataApi(JSONObject jsonObject) {

            MyApplication.showloader(Inform.this,"Please wait...");
            API.POST_REQEST_TransferAMount("ewallet/api/v1/intech/transferOut", jsonObject, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {
                    MyApplication.hideLoader();
                    System.out.println("Inform response======="+jsonObject.toString());
                    if(jsonObject!=null){
                        if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                            MyApplication.saveBool("FirstLogin",false,Inform.this);
                            MyApplication.UserMobile=etPhone.getText().toString().trim();
                           // subscriberWalletOwnerCode = jsonObject.optJSONObject("walletOwner").optString("walletOwnerCode");
                            etFname.setText(jsonObject.optJSONObject("walletOwner").optString("ownerName"));
                            etLname.setText(jsonObject.optJSONObject("walletOwner").optString("lastName"));
                            etPhone.setText(jsonObject.optJSONObject("walletOwner").optString("mobileNumber"));


                            Intent i=new Intent(Inform.this, InFormConfirmation.class);
                            startActivity(i);
                            // callApiAddSubscriberAddress(subscriberWalletOwnerCode);
                        }else{
                            MyApplication.showToast(Inform.this,jsonObject.optString("resultDescription"));
                        }

                    }
                }

                @Override
                public void failure(String aFalse) {
                    MyApplication.hideLoader();
                    MyApplication.showToast(Inform.this,aFalse);
                }

            });

        }



    public void callwalletOwner(){

        MyApplication.showloader(tosubscriberC,"Please Wait...");
        API.GET("ewallet/api/v1/wallet/walletOwner/"+MyApplication.getSaveString("walletOwnerCode",getApplicationContext()), new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                   etName.setText(jsonObject.optJSONArray("walletList").optJSONObject(0).optString("walletOwnerName"));
                   etPhone.setText(jsonObject.optJSONArray("walletList").optJSONObject(0).optString("walletOwnerMsisdn"));
                }else{
                    MyApplication.showToast(tosubscriberC,jsonObject.optString("resultDescription"));
                }

            }


            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(tosubscriberC,aFalse);
            }
        });

        callserviceCategory();

    }

    public static JSONObject walletOwner = new JSONObject();
    public static JSONObject serviceCategory = new JSONObject();

    private void callApiSubsriberList() {
        try {
            //walletOwnerCategoryCode
            // MyApplication.showloader(TransferToAccountActivity.this, "Please wait!");
            API.GET("ewallet/api/v1/walletOwner/all?" + "walletOwnerCategoryCode="
                            + "100010" +
                            "&mobileNumber=" + (etSubscriberNo.getText().toString()) + "&offset=" + "0" + "&limit=" + "500",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                subscriberList.clear();
                                if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
                                    walletOwner = jsonObject;
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("walletOwnerList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject jsonObjectSubscriber = walletOwnerListArr.optJSONObject(i);
                                        SubscriberInfoModel.Subscriber subscriber = new SubscriberInfoModel.Subscriber(
                                                jsonObjectSubscriber.optInt("id"),
                                                jsonObjectSubscriber.optBoolean("walletExists"),
                                                jsonObjectSubscriber.optString("code", "N/A"),
                                                jsonObjectSubscriber.optString("walletOwnerCategoryCode", "N/A"),
                                                jsonObjectSubscriber.optString("ownerName"),
                                                jsonObjectSubscriber.optString("mobileNumber", "N/A"),
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
                                                jsonObjectSubscriber.optString("lastName"),
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

                                    }


                                } else {
                                    subscriberList.clear();
                                   // setSubscriberdataf("No Data");
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


    public void callwalletOwnerDetailsQR(String Code){
        API.GET("ewallet/api/v1/walletOwner/"+Code, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                    etSubscriberNo.setText(jsonObject.optJSONObject("walletOwner").optString("mobileNumber","N/A"));
                    //  callwalletOwnerCountryCurrency();
                }else{
                    MyApplication.showToast(tosubscriberC,jsonObject.optString("resultDescription"));
                    etFname.setText("");
                    etLname.setText("");
                    etAmount.setText("");
                }

            }

            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(tosubscriberC,aFalse);
            }
        });
    }


    private ArrayList<String> subscriberList = new ArrayList<String>();

    private ArrayAdapter<String> adapter;
    private void setSubscriberdataf(String subscriberInfoModel) {

        subscriberList.clear();
        mobileNo = "";
//        subscriberList.add(""+""+subscriberInfoModel+""+"");
//        adapter = new ArrayAdapter<String>(tosubscriberC,R.layout.item_select, subscriberList);
//        etSubscriberNo.setAdapter(adapter);
//        etSubscriberNo.setThreshold(9);
//        etSubscriberNo.showDropDown();

        new AlertDialog.Builder(this)
                //.setTitle(getString(R.string.logout))
                //.setIcon(R.drawable.ic_logout)
                .setMessage(getString(R.string.msisdn_not_reg_with_subscriber))
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                      /*  Intent i = new Intent(tosubscriberC, ToNonSubscriber.class);
                        i.putExtra("TOSUBMSISDN",etSubscriberNo.getText().toString().trim());
                        startActivity(i);
                        finish();*/

                    }
                }).create().show();

    }

    String receiverCode="";

    private void setSubscriberdata(SubscriberInfoModel subscriberInfoModel) {
        SubscriberInfoModel.Subscriber data = subscriberInfoModel.getSubscriber();
        mobileNo = data.getMobileNumber();
        ownerName = data.getOwnerName();
        lastName = data.getLastName();
        if (isQR) {
            etSubscriberNo.setText(data.getMobileNumber());
          //  etFname.setText(data.getOwnerName());
          //  etLname.setText(data.getLastName());
            etAmount.setText("");
            etAmount.requestFocus();
            isQR = false;

        } else {
            /*subscriberList.add(data.getMobileNumber() + "," + data.getOwnerName() + "," + data.getLastName());
            adapter = new ArrayAdapter<String>(tosubscriberC, R.layout.item_select, subscriberList);
            */etSubscriberNo.setAdapter(adapter);
            etSubscriberNo.setThreshold(9);
            etSubscriberNo.showDropDown();

        //    etFname.setText(data.getOwnerName());
           // etLname.setText(data.getLastName());
            etAmount.setText("");
            etAmount.requestFocus();
        }

        receiverCode=data.getCode();
        if(receiverCode.equalsIgnoreCase("")){
            etFname.setText("");
            etLname.setText("");
            etAmount.setText("");
        }else{
            callApiWalletCountryCurrency(receiverCode);
        }

    }

    String CurrncyJSON="{\"transactionId\":\"2842171\",\"requestTime\":\"Fri Jun 24 05:41:36 UTC 2022\",\"responseTime\":\"Fri Jun 24 05:33:15 UTC 2022\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwnerCountryCurrencyList\":[{\"id\":13648,\"code\":\"113647\",\"walletOwnerCode\":\"1000004346\",\"currencyCode\":\"100062\",\"currencyName\":\"GNF\",\"currencySymbol\":\"GNF\",\"countryCurrencyCode\":\"100203\",\"inBound\":true,\"outBound\":true,\"status\":\"Active\"}]}";

    private void callApiWalletCountryCurrencyJSOn() {
        try {

            JSONObject jsonObject=new JSONObject(CurrncyJSON);
            if (jsonObject != null) {
                if (jsonObject.optString("resultCode").equalsIgnoreCase("0")) {
                    currency = jsonObject.optJSONArray("walletOwnerCountryCurrencyList").optJSONObject(0).optString("currencyName");
                    currencySymbol = jsonObject.optJSONArray("walletOwnerCountryCurrencyList").optJSONObject(0).optString("currencySymbol");
                    tvAmtCurr.setText(currencySymbol);
                } else {
                    MyApplication.showToast(tosubscriberC,jsonObject.optString("resultDescription"));
                }
            }

        } catch (Exception e) {

        }

    }
    private void callApiWalletCountryCurrency(String code) {
        try {

            API.GET("ewallet/api/v1/walletOwnerCountryCurrency/"+code,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            if (jsonObject != null) {
                                if (jsonObject.optString("resultCode").equalsIgnoreCase("0")) {
                                    currency = jsonObject.optJSONArray("walletOwnerCountryCurrencyList").optJSONObject(0).optString("currencyName");
                                    currencySymbol = jsonObject.optJSONArray("walletOwnerCountryCurrencyList").optJSONObject(0).optString("currencySymbol");
                                    tvAmtCurr.setText(currencySymbol);
                                } else {
                                    MyApplication.showToast(tosubscriberC,jsonObject.optString("resultDescription"));
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

        public void callserviceCategory() {
        API.GET("ewallet/api/v1/serviceProvider/serviceCategory?serviceCode=100000&serviceCategoryCode=100024&status=Y", new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                MyApplication.hideLoader();
                if (jsonObject.optString("resultCode").equalsIgnoreCase("0")) {
                    serviceCategory = jsonObject;
                    serviceProvider = serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("name");

                } else {
                    MyApplication.showToast(tosubscriberC,jsonObject.optString("resultDescription"));
                }

            }

            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(tosubscriberC,aFalse);
            }
        });
    }


    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
    DecimalFormat df = new DecimalFormat("0.00",symbols);
    public static JSONArray taxConfigurationList;
    private void callApiAmountDetails() {
        try {
            //MyApplication.showloader(cashinC, "Please wait!");
            API.GET("ewallet/api/v1/exchangeRate/getAmountDetails?"+"sendCurrencyCode="+"100062"+
                            "&receiveCurrencyCode="+"100062"+
                            "&sendCountryCode="+walletOwner.optJSONArray("walletOwnerList").optJSONObject(0).optString("registerCountryCode")
                            +"&receiveCountryCode="+walletOwner.optJSONArray("walletOwnerList").optJSONObject(0).optString("registerCountryCode")+
                            "&currencyValue="+etAmount.getText().toString().replace(",","")+
                            "&channelTypeCode="+MyApplication.channelTypeCode+
                            "&serviceCode="+serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCode")
                            +"&serviceCategoryCode="+serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("serviceCategoryCode")+
                            "&serviceProviderCode="+serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("code")+
                            "&walletOwnerCode="+MyApplication.getSaveString("walletOwnerCode", tosubscriberC)+
                            "&remitAgentCode="+MyApplication.getSaveString("walletOwnerCode", tosubscriberC)+
                            "&payAgentCode="+walletOwner.optJSONArray("walletOwnerList").optJSONObject(0).optString("walletOwnerCode"),
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            // MyApplication.hideLoader();
                            System.out.println("ToSubscriber response======="+jsonObject.toString());
                            if (jsonObject != null) {
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("exchangeRate");
                                    AmountDetailsInfoModel.AmountDetails amountDetails = new AmountDetailsInfoModel.AmountDetails(
                                            jsonObjectAmountDetails.optInt("fee"),
                                            jsonObjectAmountDetails.optInt("receiverFee"),
                                            jsonObjectAmountDetails.optInt("receiverTax"),
                                            jsonObjectAmountDetails.optString("value", "N/A"),
                                            jsonObjectAmountDetails.optString("currencyValue", "N/A")

                                    );

                                    AmountDetailsInfoModel amountDetailsInfoModel = new AmountDetailsInfoModel(
                                            jsonObject.optString("transactionId", "N/A"),
                                            jsonObject.optString("requestTime", "N/A"),
                                            jsonObject.optString("responseTime", "N/A"),
                                            jsonObject.optString("resultCode", "N/A"),
                                            jsonObject.optString("resultDescription", "N/A"),
                                            amountDetails
                                    );

                                    currencyValue=df.format(jsonObjectAmountDetails.optDouble("currencyValue"));
                                    fee = df.format(jsonObjectAmountDetails.optDouble("fee"));
                                    receiverFee= jsonObjectAmountDetails.optInt("receiverFee");
                                    receiverTax = jsonObjectAmountDetails.optInt("receiverTax");
//                                    int tax = receiverFee+receiverTax;
//                                    if(currencyValue<tax){
//                                        tvSend.setVisibility(View.GONE);
//                                        MyApplication.showErrorToast(tosubscriberC,getString(R.string.fee_tax_greater_than_trans_amt));
//                                    }else{
//                                        tvSend.setVisibility(View.VISIBLE);
//                                    }

                                    if(jsonObjectAmountDetails.has("taxConfigurationList")) {
                                        taxConfigurationList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
                                    }else{
                                        taxConfigurationList=null;
                                    }
                                    if(jsonObjectAmountDetails.has("receiverTax")) {
                                        taxConfigurationList=null;
                                    }

                                } else {
                                    MyApplication.showToast(tosubscriberC,jsonObject.optString("resultDescription", "N/A"));
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


    private void callApiAmountDetailsJSON() {
        try {
            JSONObject jsonObject=new JSONObject(APIAMOUNT);
            if (jsonObject != null) {
                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                    JSONObject jsonObjectAmountDetails = jsonObject.optJSONObject("exchangeRate");
                    AmountDetailsInfoModel.AmountDetails amountDetails = new AmountDetailsInfoModel.AmountDetails(
                            jsonObjectAmountDetails.optInt("fee"),
                            jsonObjectAmountDetails.optInt("receiverFee"),
                            jsonObjectAmountDetails.optInt("receiverTax"),
                            jsonObjectAmountDetails.optString("value", "N/A"),
                            jsonObjectAmountDetails.optString("currencyValue", "N/A")

                    );

                    AmountDetailsInfoModel amountDetailsInfoModel = new AmountDetailsInfoModel(
                            jsonObject.optString("transactionId", "N/A"),
                            jsonObject.optString("requestTime", "N/A"),
                            jsonObject.optString("responseTime", "N/A"),
                            jsonObject.optString("resultCode", "N/A"),
                            jsonObject.optString("resultDescription", "N/A"),
                            amountDetails
                    );

                    currencyValue= df.format(jsonObjectAmountDetails.optDouble("currencyValue"));
                    fee = df.format(jsonObjectAmountDetails.optDouble("fee"));
                    receiverFee= jsonObjectAmountDetails.optInt("receiverFee");
                    receiverTax = jsonObjectAmountDetails.optInt("receiverTax");
//                                    int tax = receiverFee+receiverTax;
//                                    if(currencyValue<tax){
//                                        tvSend.setVisibility(View.GONE);
//                                        MyApplication.showErrorToast(tosubscriberC,getString(R.string.fee_tax_greater_than_trans_amt));
//                                    }else{
//                                        tvSend.setVisibility(View.VISIBLE);
//                                    }

                    if(jsonObjectAmountDetails.has("taxConfigurationList")) {
                        taxConfigurationList = jsonObjectAmountDetails.optJSONArray("taxConfigurationList");
                    }else{
                        taxConfigurationList=null;
                    }


                } else {
                    MyApplication.showToast(tosubscriberC,jsonObject.optString("resultDescription", "N/A"));
                }
            }
        } catch (Exception e) {

        }

    }

    String APIAMOUNT="{\n" +
            "  \"transactionId\": \"2842118\",\n" +
            "  \"requestTime\": \"Fri Jun 24 05:13:03 UTC 2022\",\n" +
            "  \"responseTime\": \"Fri Jun 24 05:04:43 UTC 2022\",\n" +
            "  \"resultCode\": \"0\",\n" +
            "  \"resultDescription\": \"Transaction Successful\",\n" +
            "  \"exchangeRate\": {\n" +
            "    \"id\": 306,\n" +
            "    \"code\": \"100329\",\n" +
            "    \"name\": \"GNFtoXOF\",\n" +
            "    \"value\": \"13.9248\",\n" +
            "    \"sendCurrencyCode\": \"100062\",\n" +
            "    \"receiveCurrencyCode\": \"100018\",\n" +
            "    \"sendCurrencyName\": \"Guinean franc\",\n" +
            "    \"receiveCurrencyName\": \"West African CFA franc\",\n" +
            "    \"sendCountryCode\": \"100092\",\n" +
            "    \"receiveCountryCode\": \"100195\",\n" +
            "    \"sendCountryName\": \"Guinea\",\n" +
            "    \"receiveCountryName\": \"Senegal\",\n" +
            "    \"status\": \"Active\",\n" +
            "    \"state\": \"Approved\",\n" +
            "    \"currencyValue\": \"9329.11\",\n" +
            "    \"createdBy\": \"100322\",\n" +
            "    \"modifiedBy\": \"100375\",\n" +
            "    \"creationDate\": \"2022-03-17T05:30:00.000+0530\",\n" +
            "    \"modificationDate\": \"2022-03-17T05:30:00.000+0530\",\n" +
            "    \"fee\": 0,\n" +
            "    \"taxConfigurationList\": [\n" +
            "      {\n" +
            "        \"taxTypeCode\": \"100131\",\n" +
            "        \"taxTypeName\": \"VAT\",\n" +
            "        \"value\": 0,\n" +
            "        \"taxAvailBy\": \"Fee Amount\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";


    private boolean isFormatting;
    private int prevCommaAmount;
    private void formatInput(EditText editText,CharSequence s, int start, int count) {
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
    private void callApiCountry() {
        try {

            API.GET("ewallet/api/v1/country/all",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            if (jsonObject != null) {
                                benefiCountryList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("countryList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        if (!MyApplication.getSaveString("userCountryCode", tosubscriberC).equalsIgnoreCase(data.optString("code"))) {
                                            benefiCountryModelList.add(new CountryInfoModel.Country(
                                                    data.optInt("id"),
                                                    data.optInt("mobileLength"),
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

                                            benefiCountryList.add(data.optString("name").trim());

                                        }
                                    }

                                    spinnerDialogBenefiCountry= new SpinnerDialog(tosubscriberC, benefiCountryList, "Select Country", R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
                                    spinnerDialogBenefiCountry.setCancellable(true); // for cancellable
                                    spinnerDialogBenefiCountry.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogBenefiCountry.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spCountry.setText(item);
                                            spCountry.setTag(position);
                                             countrycode=benefiCountryModelList.get(position).getCurrencyCode();
                                            tvAmtCurr.setVisibility(View.VISIBLE);
                                             tvAmtCurr.setText(countrycode);
                                            xofAmountLinear.setVisibility(View.VISIBLE);

                                          //  spBenifiCurr.setText(getString(R.string.valid_select_benifi_curr));
                                            //   txt_benefi_phone.setText(benefiCountryModelList.get(position).dialCode);
                                            //callApiCurrency(benefiCountryModelList.get(position).getCode());
                                        }
                                    });


                                } else {
                                    MyApplication.showToast(tosubscriberC,jsonObject.optString("resultDescription", "N/A"));
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