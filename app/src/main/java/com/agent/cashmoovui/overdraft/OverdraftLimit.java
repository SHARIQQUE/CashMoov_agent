package com.agent.cashmoovui.overdraft;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.adapter.CommonBaseAdapterSecond;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.transfer_float.SellFloat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class OverdraftLimit extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView imgBack,imgHome;
    EditText edittext_amount,edittext_validity;
    TextView send_textview;
    String profileTypeCode_fromServer="",profileTypeName_fromServer="",walletOwnerName_fromServer="";

    MyApplication applicationComponentClass;
    String languageToUse = "";
    String  currencyCode_agent="",countryCode_agent="",currencyName_agent="",validityDaysStr;
    Spinner spinner_currency;

    ArrayList<String> arrayList_currecnyName = new ArrayList<String>();
    ArrayList<String> arrayList_currecnyCode = new ArrayList<String>();

    String selectCurrecnyName="";
    String selectCurrecnyCode="";


    String currencyName_from_currency="";
    String countryCurrencyCode_from_currency="";



    String amountstr="",agentName_from_walletOwner="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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



        setContentView(R.layout.activity_overdraft_limit);




        setBackMenu();


        if (new InternetCheck().isConnected(OverdraftLimit.this)) {

            MyApplication.showloader(OverdraftLimit.this, getString(R.string.getting_user_info));


            api_currency();


        } else {
            Toast.makeText(OverdraftLimit.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
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
        edittext_amount = findViewById(R.id.edittext_amount);
        edittext_validity = findViewById(R.id.edittext_validity);
        send_textview = findViewById(R.id.send_textview);

        spinner_currency =(Spinner)findViewById(R.id.spinner_currency);
        spinner_currency.setOnItemSelectedListener(this);

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

        send_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validation_mobile_Details()) {

                    if (new InternetCheck().isConnected(OverdraftLimit.this)) {

                        MyApplication.showloader(OverdraftLimit.this, getString(R.string.getting_user_info));

                        api_walletOwnerUser();



                    } else {
                        Toast.makeText(OverdraftLimit.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }

    private void api_currency() {



        String walletOwnerCode_from_msis =  MyApplication.getSaveString("walletOwnerCode",OverdraftLimit.this);

        API.GET_TRANSFER_DETAILS("ewallet/api/v1/walletOwnerCountryCurrency/"+walletOwnerCode_from_msis,languageToUse,new Api_Responce_Handler() {

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


                            currencyName_agent = jsonObject3.getString("currencyName");
                            countryCode_agent = jsonObject3.getString("countryCurrencyCode");
                            currencyCode_agent = jsonObject3.getString("currencyCode");


                            arrayList_currecnyName.add(currencyName_from_currency);
                            arrayList_currecnyCode.add(countryCurrencyCode_from_currency);

                        }


                        CommonBaseAdapterSecond arraadapter2 = new CommonBaseAdapterSecond(OverdraftLimit.this, arrayList_currecnyName);
                        spinner_currency.setAdapter(arraadapter2);


                    }

                    else {
                        Toast.makeText(OverdraftLimit.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(OverdraftLimit.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(OverdraftLimit.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    private void api_walletOwnerUser() {

        String USER_CODE_FROM_TOKEN_AGENTDETAILS = MyApplication.getSaveString("userCode", OverdraftLimit.this);



        API.GET_TRANSFER_DETAILS("ewallet/api/v1/walletOwnerUser/"+USER_CODE_FROM_TOKEN_AGENTDETAILS,languageToUse,new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {


                MyApplication.hideLoader();

                try {

                    //    JSONObject jsonObject1 = new JSONObject("{\"transactionId\":\"1927802\",\"requestTime\":\"Tue Nov 02 13:03:30 IST 2021\",\"responseTime\":\"Tue Nov 02 13:03:30 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwner\":{\"id\":110679,\"code\":\"1000002785\",\"walletOwnerCategoryCode\":\"100000\",\"ownerName\":\"sharique agent\",\"mobileNumber\":\"9990063618\",\"businessTypeCode\":\"100008\",\"businessTypeName\":\"Goldsmith\",\"lineOfBusiness\":\"gffg\",\"idProofNumber\":\"trt465656\",\"email\":\"sharique9718@gmail.com\",\"status\":\"Active\",\"state\":\"Approved\",\"stage\":\"Document\",\"idProofTypeCode\":\"100005\",\"idProofTypeName\":\"COMPANY REGISTRATION NUMBER\",\"idExpiryDate\":\"2021-10-22\",\"notificationLanguage\":\"en\",\"notificationTypeCode\":\"100000\",\"notificationName\":\"EMAIL\",\"issuingCountryCode\":\"100102\",\"issuingCountryName\":\"India\",\"registerCountryCode\":\"100102\",\"registerCountryName\":\"India\",\"createdBy\":\"100250\",\"modifiedBy\":\"100308\",\"creationDate\":\"2021-10-19T22:38:48.969+0530\",\"modificationDate\":\"2021-11-01T13:49:14.892+0530\",\"walletExists\":true,\"profileTypeCode\":\"100000\",\"profileTypeName\":\"tier1\",\"walletCurrencyList\":[\"100018\",\"100017\",\"100069\",\"100020\",\"100004\",\"100029\",\"100062\",\"100003\"],\"walletOwnerCatName\":\"Institute\",\"requestedSource\":\"ADMIN\",\"regesterCountryDialCode\":\"+91\",\"issuingCountryDialCode\":\"+91\",\"walletOwnerCode\":\"1000002785\"}}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {

                        JSONObject walletOwnerUser = jsonObject.getJSONObject("walletOwnerUser");


                        String userCode_from_walletCode = walletOwnerUser.getString("userCode");
                        walletOwnerName_fromServer = walletOwnerUser.getString("firstName");


                        api_wallet_walletOwner(userCode_from_walletCode);


                    } else {
                        Toast.makeText(OverdraftLimit.this, resultDescription, Toast.LENGTH_LONG).show();
                    }



                } catch (Exception e) {
                    Toast.makeText(OverdraftLimit.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(OverdraftLimit.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {



            case R.id.spinner_currency:
            {
                selectCurrecnyName = arrayList_currecnyName.get(i);
                selectCurrecnyCode = arrayList_currecnyCode.get(i);



                if (spinner_currency.getSelectedItemPosition()==0)
                {

                    amountstr="100000";
                    validityDaysStr="90";

                    edittext_amount.setEnabled(false);
                    edittext_validity.setEnabled(false);

                    edittext_amount.setText(amountstr);
                    edittext_validity.setText(validityDaysStr);

                }


                else
                {
                    amountstr="100000";
                    validityDaysStr="90";

                    edittext_amount.setEnabled(false);
                    edittext_validity.setEnabled(false);

                    edittext_amount.setText(amountstr);
                    edittext_validity.setText(validityDaysStr);
                }



                //  Toast.makeText(OverdraftLimit.this, ""+selectCurrecnyName+"("+selectCurrecnyCode+")", Toast.LENGTH_SHORT).show();
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

    private void api_creditLimitAllocation() {

        String walletOwnerCode_from_mssid =  MyApplication.getSaveString("walletOwnerCode",OverdraftLimit.this);



        API.GET_TRANSFER_DETAILS("ewallet/api/v1/creditLimitAllocation/all?walletOwnerCode="+walletOwnerCode_from_mssid, languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                  //  JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1937292\",\"requestTime\":\"Wed Nov 03 11:27:43 IST 2021\",\"responseTime\":\"Wed Nov 03 11:27:43 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"pageable\":{\"totalRecords\":1},\"creditLimitAllocationList\":[{\"id\":313,\"code\":\"100330\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"currencyCode\":\"100062\",\"currencyName\":\"GNF\",\"currencySymbol\":\"Fr\",\"amount\":1000.0,\"validityDays\":90,\"status\":\"Approved\",\"creationDate\":\"2021-11-01T13:54:19.122+0530\",\"modificationDate\":\"2021-11-01T13:54:19.122+0530\"}]}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("creditLimitAllocationList");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);

                            String walletOwnerCode = jsonObject3.getString("walletOwnerCode");
                        }



                        api_creditLimitConfig();


                    } else {
                        Toast.makeText(OverdraftLimit.this, resultDescription, Toast.LENGTH_LONG).show();
                      //  finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(OverdraftLimit.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(OverdraftLimit.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    private void api_final_dataApproval() {

        try {

//            {
//                "dataApprovalList": [
//                {
//                        "featureCode": "100068",
//                        "entityCode": "100338",
//                        "entityName": "sharique agent",
//                        "actionType": "Created",
//                         "comments": "",
//                         "status": "U",
//                         "assignTo": "",
//
//                         "entity": {}
//                         "updatedInformation": {},
//                }
//              ]
//            }

              JSONObject jsonObject_0 = new JSONObject();
              JSONObject jsonObject = new JSONObject();
              JSONObject jsonObject_entity = new JSONObject();
              JSONObject jsonObject_updatedInformation = new JSONObject();


              jsonObject.put("featureCode","100068");
              jsonObject.put("entityCode","100338");
              jsonObject.put("entityName",walletOwnerName_fromServer);
              jsonObject.put("actionType","Created");
              jsonObject.put("comments","");
              jsonObject.put("status","U");
              jsonObject.put("assignTo","");


            jsonObject.put("entity",jsonObject_entity);
            jsonObject.put("updatedInformation",jsonObject_updatedInformation);

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject);

            jsonObject_0.put("dataApprovalList", jsonArray);

            System.out.println(jsonObject_0);
            System.out.println(jsonObject_0);




        String userCode_agentCode_from_mssid =  MyApplication.getSaveString("USERCODE",OverdraftLimit.this);


        API.POST_TRANSFER("ewallet/api/v1/dataApproval",jsonObject_0 ,languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {


                   // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1937311\",\"requestTime\":\"Wed Nov 03 11:27:51 IST 2021\",\"responseTime\":\"Wed Nov 03 11:27:51 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"reversalMessageList\":[]}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {

                        alert_dialogue_sh("Your overdraft request created successfully and send for approval");

                    } else {
                        Toast.makeText(OverdraftLimit.this, resultDescription, Toast.LENGTH_LONG).show();
                        //  finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(OverdraftLimit.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(OverdraftLimit.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }


    private void api_creditLimitConfig() {

        String USER_CODE_FROM_TOKEN_AGENTDETAILS = MyApplication.getSaveString("userCode", OverdraftLimit.this);


        API.GET_TRANSFER_DETAILS("ewallet/api/v1/creditLimitConfig/all?walletOwnerCategoryCode=100000&profileTypeCode=100000",languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    //  JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1937292\",\"requestTime\":\"Wed Nov 03 11:27:43 IST 2021\",\"responseTime\":\"Wed Nov 03 11:27:43 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"pageable\":{\"totalRecords\":1},\"creditLimitAllocationList\":[{\"id\":313,\"code\":\"100330\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"currencyCode\":\"100062\",\"currencyName\":\"GNF\",\"currencySymbol\":\"Fr\",\"amount\":1000.0,\"validityDays\":90,\"status\":\"Approved\",\"creationDate\":\"2021-11-01T13:54:19.122+0530\",\"modificationDate\":\"2021-11-01T13:54:19.122+0530\"}]}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {



                        JSONArray jsonArray = jsonObject.getJSONArray("creditLimitConfigList");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);

                            String  walletOwnerCategoryCode = jsonObject3.getString("walletOwnerCategoryCode");
                            String  walletOwnerCategoryName = jsonObject3.getString("walletOwnerCategoryName");

                              profileTypeCode_fromServer = jsonObject3.getString("profileTypeCode");
                              profileTypeName_fromServer = jsonObject3.getString("profileTypeName");


                        }




                        api_final_dataApproval();

                    } else {
                        Toast.makeText(OverdraftLimit.this, resultDescription, Toast.LENGTH_LONG).show();
                       // finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(OverdraftLimit.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(OverdraftLimit.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void api_wallet_walletOwner(String userCode_from_walletCode) {



        API.GET_TRANSFER_DETAILS("ewallet/api/v1/wallet/walletOwner/" + userCode_from_walletCode, languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1937292\",\"requestTime\":\"Wed Nov 03 11:27:43 IST 2021\",\"responseTime\":\"Wed Nov 03 11:27:43 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletList\":[{\"id\":28360,\"code\":\"1000028519\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"currencyCode\":\"100020\",\"currencyName\":\"BTN\",\"currencySymbol\":\"Nu.\",\"walletTypeCode\":\"100008\",\"walletTypeName\":\"Main Wallet\",\"minValue\":0,\"maxValue\":0,\"value\":0,\"minTransValue\":100,\"maxTransValue\":100000,\"allocatedValue\":0,\"alertValue\":10000,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":0},{\"id\":28351,\"code\":\"1000028510\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"currencyCode\":\"100069\",\"currencyName\":\"INR\",\"currencySymbol\":\"₹\",\"walletTypeCode\":\"100011\",\"walletTypeName\":\"Overdraft Wallet\",\"minValue\":0,\"maxValue\":0,\"value\":0,\"minTransValue\":0,\"maxTransValue\":0,\"allocatedValue\":0,\"alertValue\":0,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":0},{\"id\":28354,\"code\":\"1000028513\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"currencyCode\":\"100029\",\"currencyName\":\"BIF\",\"currencySymbol\":\"Fr\",\"walletTypeCode\":\"100011\",\"walletTypeName\":\"Overdraft Wallet\",\"minValue\":0,\"maxValue\":0,\"value\":0,\"minTransValue\":0,\"maxTransValue\":0,\"allocatedValue\":0,\"alertValue\":0,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":0},{\"id\":28346,\"code\":\"1000028505\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"currencyCode\":\"100062\",\"currencyName\":\"GNF\",\"currencySymbol\":\"Fr\",\"walletTypeCode\":\"100009\",\"walletTypeName\":\"Comission Wallet\",\"minValue\":0,\"maxValue\":0,\"value\":4992744,\"minTransValue\":0,\"maxTransValue\":0,\"allocatedValue\":0,\"alertValue\":0,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":0},{\"id\":28355,\"code\":\"1000028514\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"currencyCode\":\"100062\",\"currencyName\":\"GNF\",\"currencySymbol\":\"Fr\",\"walletTypeCode\":\"100011\",\"walletTypeName\":\"Overdraft Wallet\",\"minValue\":0,\"maxValue\":100000,\"value\":1000,\"minTransValue\":0,\"maxTransValue\":0,\"allocatedValue\":0,\"alertValue\":0,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":90,\"overDraftExpiryDate\":\"2022-01-30T00:00:00.000+0530\"},{\"id\":28356,\"code\":\"1000028515\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"currencyCode\":\"100003\",\"currencyName\":\"USD\",\"currencySymbol\":\"$\",\"walletTypeCode\":\"100011\",\"walletTypeName\":\"Overdraft Wallet\",\"minValue\":0,\"maxValue\":0,\"value\":0,\"minTransValue\":0,\"maxTransValue\":0,\"allocatedValue\":0,\"alertValue\":0,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":0},{\"id\":28357,\"code\":\"1000028516\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"currencyCode\":\"100018\",\"currencyName\":\"XOF\",\"currencySymbol\":\"CFA\",\"walletTypeCode\":\"100008\",\"walletTypeName\":\"Main Wallet\",\"minValue\":0,\"maxValue\":0,\"value\":0,\"minTransValue\":100,\"maxTransValue\":100000,\"allocatedValue\":0,\"alertValue\":10000,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":0},{\"id\":28358,\"code\":\"1000028517\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"currencyCode\":\"100017\",\"currencyName\":\"BZD\",\"currencySymbol\":\"$\",\"walletTypeCode\":\"100008\",\"walletTypeName\":\"Main Wallet\",\"minValue\":0,\"maxValue\":0,\"value\":0,\"minTransValue\":100,\"maxTransValue\":100000,\"allocatedValue\":0,\"alertValue\":10000,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":0},{\"id\":28359,\"code\":\"1000028518\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"currencyCode\":\"100069\",\"currencyName\":\"INR\",\"currencySymbol\":\"₹\",\"walletTypeCode\":\"100008\",\"walletTypeName\":\"Main Wallet\",\"minValue\":0,\"maxValue\":0,\"value\":0,\"minTransValue\":100,\"maxTransValue\":100000,\"allocatedValue\":0,\"alertValue\":10000,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":0},{\"id\":28340,\"code\":\"1000028499\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"currencyCode\":\"100018\",\"currencyName\":\"XOF\",\"currencySymbol\":\"CFA\",\"walletTypeCode\":\"100009\",\"walletTypeName\":\"Comission Wallet\",\"minValue\":0,\"maxValue\":0,\"value\":0,\"minTransValue\":0,\"maxTransValue\":0,\"allocatedValue\":0,\"alertValue\":0,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":0},{\"id\":28341,\"code\":\"1000028500\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"currencyCode\":\"100017\",\"currencyName\":\"BZD\",\"currencySymbol\":\"$\",\"walletTypeCode\":\"100009\",\"walletTypeName\":\"Comission Wallet\",\"minValue\":0,\"maxValue\":0,\"value\":0,\"minTransValue\":0,\"maxTransValue\":0,\"allocatedValue\":0,\"alertValue\":0,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":0},{\"id\":28349,\"code\":\"1000028508\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"currencyCode\":\"100018\",\"currencyName\":\"XOF\",\"currencySymbol\":\"CFA\",\"walletTypeCode\":\"100011\",\"walletTypeName\":\"Overdraft Wallet\",\"minValue\":0,\"maxValue\":0,\"value\":0,\"minTransValue\":0,\"maxTransValue\":0,\"allocatedValue\":0,\"alertValue\":0,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":0},{\"id\":28350,\"code\":\"1000028509\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"currencyCode\":\"100017\",\"currencyName\":\"BZD\",\"currencySymbol\":\"$\",\"walletTypeCode\":\"100011\",\"walletTypeName\":\"Overdraft Wallet\",\"minValue\":0,\"maxValue\":0,\"value\":0,\"minTransValue\":0,\"maxTransValue\":0,\"allocatedValue\":0,\"alertValue\":0,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":0},{\"id\":28353,\"code\":\"1000028512\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"currencyCode\":\"100004\",\"currencyName\":\"EUR\",\"currencySymbol\":\"€\",\"walletTypeCode\":\"100011\",\"walletTypeName\":\"Overdraft Wallet\",\"minValue\":0,\"maxValue\":0,\"value\":0,\"minTransValue\":0,\"maxTransValue\":0,\"allocatedValue\":0,\"alertValue\":0,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":0},{\"id\":28363,\"code\":\"1000028522\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"currencyCode\":\"100062\",\"currencyName\":\"GNF\",\"currencySymbol\":\"Fr\",\"walletTypeCode\":\"100008\",\"walletTypeName\":\"Main Wallet\",\"minValue\":0,\"maxValue\":0,\"value\":938380,\"minTransValue\":100,\"maxTransValue\":100000,\"allocatedValue\":65930,\"alertValue\":10000,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":0},{\"id\":28352,\"code\":\"1000028511\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"currencyCode\":\"100020\",\"currencyName\":\"BTN\",\"currencySymbol\":\"Nu.\",\"walletTypeCode\":\"100011\",\"walletTypeName\":\"Overdraft Wallet\",\"minValue\":0,\"maxValue\":0,\"value\":0,\"minTransValue\":0,\"maxTransValue\":0,\"allocatedValue\":0,\"alertValue\":0,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":0},{\"id\":28361,\"code\":\"1000028520\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"currencyCode\":\"100004\",\"currencyName\":\"EUR\",\"currencySymbol\":\"€\",\"walletTypeCode\":\"100008\",\"walletTypeName\":\"Main Wallet\",\"minValue\":0,\"maxValue\":0,\"value\":0,\"minTransValue\":100,\"maxTransValue\":100000,\"allocatedValue\":0,\"alertValue\":10000,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":0},{\"id\":28362,\"code\":\"1000028521\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"currencyCode\":\"100029\",\"currencyName\":\"BIF\",\"currencySymbol\":\"Fr\",\"walletTypeCode\":\"100008\",\"walletTypeName\":\"Main Wallet\",\"minValue\":0,\"maxValue\":0,\"value\":0,\"minTransValue\":100,\"maxTransValue\":100000,\"allocatedValue\":0,\"alertValue\":10000,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":0},{\"id\":28364,\"code\":\"1000028523\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"currencyCode\":\"100003\",\"currencyName\":\"USD\",\"currencySymbol\":\"$\",\"walletTypeCode\":\"100008\",\"walletTypeName\":\"Main Wallet\",\"minValue\":0,\"maxValue\":0,\"value\":0,\"minTransValue\":100,\"maxTransValue\":100000,\"allocatedValue\":0,\"alertValue\":10000,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":0},{\"id\":28342,\"code\":\"1000028501\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"currencyCode\":\"100069\",\"currencyName\":\"INR\",\"currencySymbol\":\"₹\",\"walletTypeCode\":\"100009\",\"walletTypeName\":\"Comission Wallet\",\"minValue\":0,\"maxValue\":0,\"value\":0,\"minTransValue\":0,\"maxTransValue\":0,\"allocatedValue\":0,\"alertValue\":0,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":0},{\"id\":28343,\"code\":\"1000028502\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"currencyCode\":\"100020\",\"currencyName\":\"BTN\",\"currencySymbol\":\"Nu.\",\"walletTypeCode\":\"100009\",\"walletTypeName\":\"Comission Wallet\",\"minValue\":0,\"maxValue\":0,\"value\":0,\"minTransValue\":0,\"maxTransValue\":0,\"allocatedValue\":0,\"alertValue\":0,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":0},{\"id\":28344,\"code\":\"1000028503\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"currencyCode\":\"100004\",\"currencyName\":\"EUR\",\"currencySymbol\":\"€\",\"walletTypeCode\":\"100009\",\"walletTypeName\":\"Comission Wallet\",\"minValue\":0,\"maxValue\":0,\"value\":0,\"minTransValue\":0,\"maxTransValue\":0,\"allocatedValue\":0,\"alertValue\":0,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":0},{\"id\":28345,\"code\":\"1000028504\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"currencyCode\":\"100029\",\"currencyName\":\"BIF\",\"currencySymbol\":\"Fr\",\"walletTypeCode\":\"100009\",\"walletTypeName\":\"Comission Wallet\",\"minValue\":0,\"maxValue\":0,\"value\":0,\"minTransValue\":0,\"maxTransValue\":0,\"allocatedValue\":0,\"alertValue\":0,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":0},{\"id\":28347,\"code\":\"1000028506\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"currencyCode\":\"100003\",\"currencyName\":\"USD\",\"currencySymbol\":\"$\",\"walletTypeCode\":\"100009\",\"walletTypeName\":\"Comission Wallet\",\"minValue\":0,\"maxValue\":0,\"value\":0,\"minTransValue\":0,\"maxTransValue\":0,\"allocatedValue\":0,\"alertValue\":0,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":0},{\"id\":28348,\"code\":\"1000028507\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"walletOwnerMsisdn\":\"9990063618\",\"walletTypeCode\":\"100010\",\"walletTypeName\":\"Loyalty Wallet\",\"minValue\":0,\"maxValue\":0,\"value\":0,\"minTransValue\":0,\"maxTransValue\":0,\"allocatedValue\":0,\"alertValue\":0,\"notifyMisdns\":0,\"balance\":0,\"status\":\"Active\",\"state\":\"Approved\",\"walletOwnerCategoryCode\":\"100000\",\"validityDays\":0}]}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("walletList");
                        for(int i=0;i<jsonArray.length();i++)
                        {

                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);

                         //   String  walletOwnerName = jsonObject3.getString("walletOwnerName");
                          //  String  currencyCode = jsonObject3.getString("currencyCode");
                          //  String  currencyName = jsonObject3.getString("currencyName");
                          //  String  walletOwnerCode = jsonObject3.getString("walletOwnerCode");


                        }




                        api_creditLimitAllocation();



                    } else {
                        Toast.makeText(OverdraftLimit.this, resultDescription, Toast.LENGTH_LONG).show();
                       // finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(OverdraftLimit.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(OverdraftLimit.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    boolean validation_mobile_Details() {

        amountstr = edittext_amount.getText().toString().trim();
        validityDaysStr = edittext_validity.getText().toString().trim();

       if (spinner_currency.getSelectedItemPosition()==0) {

            MyApplication.showErrorToast(this, getString(R.string.select_currency));

            return false;
        }



        else if (amountstr.isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.plz_enter_amount));

            return false;
        }


        return true;
    }




}