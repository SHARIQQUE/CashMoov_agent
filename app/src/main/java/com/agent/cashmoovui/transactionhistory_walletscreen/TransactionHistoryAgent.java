package com.agent.cashmoovui.transactionhistory_walletscreen;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.adapter.SearchAdapteAgentDetails;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.login.LoginMsis;
import com.agent.cashmoovui.model.UserDetailAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class TransactionHistoryAgent extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener,CallBackRecycleViewClick {

    String searchStr="";
    EditText edittext_search;
    ImageView search_imageView;
    TextView main_wallet_value_textview;
    ArrayList<UserDetailAgent> arrayList_modalDetails;

    RecyclerView recyclerView_agent;

    MyApplication applicationComponentClass;
    String languageToUse = "",select_currency_name="",select_currency_code="",select_walletType_name="",select_walletValue_name="";

    ArrayList<String> arrayList_currecnyName = new ArrayList<String>();
    ArrayList<String> arrayList_currecnyCode = new ArrayList<String>();
    ArrayList<String> arrayList_walletTypeName = new ArrayList<String>();
    ArrayList<String> arrayList_wallet_value = new ArrayList<String>();

    TextView close_receiptPage_textview,insitute_textview,insitute_branch,agent_textview,mainwallet_textview,overdraft_value_heding_textview,commision_wallet_textview,overdraft_wallet_textview,commisionwallet_value_textview;

    Spinner spinner_currency;


    SearchAdapteAgentDetails adpter;


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

        setContentView(R.layout.transaction_history_agent);

    try {

            recyclerView_agent = (RecyclerView) findViewById(R.id.recyclerView_agent);


        spinner_currency = (Spinner) findViewById(R.id.spinner_currency);
        spinner_currency.setOnItemSelectedListener(this);


        close_receiptPage_textview = (TextView) findViewById(R.id.close_receiptPage_textview);
        close_receiptPage_textview.setOnClickListener(this);
        insitute_textview = (TextView) findViewById(R.id.insitute_textview);
        insitute_branch = (TextView) findViewById(R.id.insitute_branch);
        agent_textview = (TextView) findViewById(R.id.agent_textview);

        mainwallet_textview = (TextView) findViewById(R.id.mainwallet_textview);
        commision_wallet_textview = (TextView) findViewById(R.id.commision_wallet_textview);
        search_imageView = (ImageView) findViewById(R.id.search_imageView);
        search_imageView.setOnClickListener(this);
        overdraft_wallet_textview = (TextView) findViewById(R.id.overdraft_wallet_textview);
        main_wallet_value_textview = (TextView) findViewById(R.id.main_wallet_value_textview);
        commisionwallet_value_textview = (TextView) findViewById(R.id.commisionwallet_value_textview);
        overdraft_value_heding_textview = (TextView) findViewById(R.id.overdraft_value_heding_textview);


        edittext_search = (EditText) findViewById(R.id.edittext_search);
        edittext_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (new InternetCheck().isConnected(TransactionHistoryAgent.this)) {

                    searchStr = edittext_search.getText().toString().trim();

                    adpter.filter(s.toString());


                } else {
                    Toast.makeText(TransactionHistoryAgent.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                }
            }
        });


        if (new InternetCheck().isConnected(TransactionHistoryAgent.this)) {

            MyApplication.showloader(TransactionHistoryAgent.this, getString(R.string.getting_user_info));

            api_transactionHistory_agent();

        } else {
            Toast.makeText(TransactionHistoryAgent.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
        }

    } catch (Exception e)
        {
            Toast.makeText(TransactionHistoryAgent.this,e.toString(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
            finish();
        }

    }






    private void api_transactionHistory_agent() {

        // http://202.131.144.130:8081/ewallet/api/v1/transaction/all?srcWalletOwnerCode=1000002692&resultCode=0&offset=0&limit=5000


        String usercode_from_msis =  MyApplication.getSaveString("USERCODE", TransactionHistoryAgent.this);

        API.GET_TRANSFER_DETAILS("ewallet/api/v1/walletOwner/all/parent/"+usercode_from_msis,languageToUse,new Api_Responce_Handler() {

            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    arrayList_modalDetails=new ArrayList<>();
                    UserDetailAgent userDetailAgent;// = new UserDetailAgent();

                    arrayList_modalDetails.clear();


                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");

                    if(resultCode.equalsIgnoreCase("0")) {



                        recyclerView_agent.removeAllViewsInLayout();
                        recyclerView_agent.setVisibility(View.VISIBLE);


                        if(jsonObject.has("walletOwner")) {

                            JSONObject jsonObject1_walletOwner= jsonObject.getJSONObject("walletOwner");


                            if(jsonObject1_walletOwner.has("walletOwnerChildList")) {

                                JSONArray jsonArray = jsonObject1_walletOwner.getJSONArray("walletOwnerChildList");


                                for (int i = 0; i < jsonArray.length(); i++) {

                                     userDetailAgent = new UserDetailAgent();



                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                                    if(jsonObject1.has("ownerName"))
                                    {

                                        String  ownerName = jsonObject1.getString("ownerName");
                                        userDetailAgent.setOwnerName(ownerName);

                                    }

                                    if(jsonObject1.has("mobileNumber"))
                                    {
                                        String  mobileNumber = jsonObject1.getString("mobileNumber");

                                        userDetailAgent.setMobileNumber(mobileNumber);

                                    }

                                    if(jsonObject1.has("email"))
                                    {
                                        String  email = jsonObject1.getString("email");

                                        userDetailAgent.setEmail(email);
                                    }
                                    if(jsonObject1.has("issuingCountryName"))
                                    {
                                        String  issuingCountryName = jsonObject1.getString("issuingCountryName");
                                        userDetailAgent.setIssuingCountryName(issuingCountryName);


                                    }

                                    if(jsonObject1.has("walletOwnerCode"))
                                    {

                                        String  walletOwnerCode = jsonObject1.getString("walletOwnerCode");
                                        userDetailAgent.setWalletOwnerCode(walletOwnerCode);

                                    }

                                    arrayList_modalDetails.add(userDetailAgent);

                                }




                            }


                        }




                        recyclerView_agent.setLayoutManager(new LinearLayoutManager(TransactionHistoryAgent.this));

                        adpter= new  SearchAdapteAgentDetails(TransactionHistoryAgent.this,arrayList_modalDetails,TransactionHistoryAgent.this);
                        recyclerView_agent.setAdapter(adpter);

                    }

                    else {
                        Toast.makeText(TransactionHistoryAgent.this, resultDescription, Toast.LENGTH_LONG).show();
                        recyclerView_agent.setVisibility(View.GONE);

                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(TransactionHistoryAgent.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    finish();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(TransactionHistoryAgent.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {



            case R.id.search_imageView:

                Toast.makeText(TransactionHistoryAgent.this, "---------search_imageView-------", Toast.LENGTH_LONG).show();

                break;

            case R.id.close_receiptPage_textview:

                finish();
                break;


        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinner_currency:
            {

                try {


                    select_currency_name = arrayList_currecnyName.get(i);
                    select_currency_code = arrayList_currecnyCode.get(i);
                    select_walletType_name = arrayList_walletTypeName.get(i);
                    select_walletValue_name = arrayList_wallet_value.get(i);

                    if(spinner_currency.getSelectedItemPosition()==0)
                    {
                        main_wallet_value_textview.setText("");
                        commisionwallet_value_textview.setText("");
                        overdraft_value_heding_textview.setText("");
                    }
                    else {

                        if(select_walletType_name.equalsIgnoreCase("Main Wallet"))
                        {
                            main_wallet_value_textview.setText(select_walletValue_name);
                        }
                        else if(select_walletType_name.equalsIgnoreCase("Comission Wallet"))
                        {
                            commisionwallet_value_textview.setText(select_walletValue_name);
                        }
                        else if(select_walletType_name.equalsIgnoreCase("Overdraft Wallet"))
                        {
                            overdraft_value_heding_textview.setText(select_walletValue_name);
                        }

                    }


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(TransactionHistoryAgent.this, e.toString(), Toast.LENGTH_LONG).show();

                }

            }

            break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void callBackReycleView(String code_walletOwnerChildList) {

        MyApplication.saveString("code_walletOwnerChildList",code_walletOwnerChildList, TransactionHistoryAgent.this);

        Intent i = new Intent(TransactionHistoryAgent.this, TransactionHistoryAgentRecycleClick.class);
        startActivity(i);

    }
}