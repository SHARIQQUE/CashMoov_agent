package com.agent.cashmoovui.transactionhistory_walletscreen;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.activity.NotificationList;
import com.agent.cashmoovui.activity.ShowProfileQr;
import com.agent.cashmoovui.adapter.MiniStatementTransAdapter;
import com.agent.cashmoovui.adapter.SearchAdapterTransactionDetails;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.listeners.MiniStatemetListners;
import com.agent.cashmoovui.model.MiniStatementTrans;
import com.agent.cashmoovui.model.transaction.CurrencyModel;
import com.agent.cashmoovui.model.UserDetail;
import com.agent.cashmoovui.settings.Profile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.skhugh.simplepulltorefresh.PullToRefreshLayout;
import org.json.JSONArray;
import org.json.JSONObject;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class TransactionHistoryMainPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener,TransactionListLisners, MiniStatemetListners {

    SmoothBottomBar bottomBar;
    String searchStr="";
    EditText edittext_search;
    ImageView imgNotification,imgQR,search_imageView;
    TextView main_wallet_value_textview;
    ArrayList<UserDetail> arrayList_modalDetails;
    CardView cardMainWallet,cardCommissionWallet,cardOverdraftWallet;

    private SpinnerDialog spinnerDialogImstitute,spinnerDialogCurrency;

    private List<MiniStatementTrans> miniStatementTransList = new ArrayList<>();
    TransactionListAdapterNew transactionListAdapterNew;


    private List<TransactionModel> transactionList = new ArrayList<>();


    RecyclerView recyclerView;

    MyApplication applicationComponentClass;
    String languageToUse = "",select_currency_name="",select_currency_code="",select_walletType_name="",select_walletValue_name="";

    ArrayList<String> arrayList_currecnyName = new ArrayList<String>();
    ArrayList<String> arrayList_currecnyCode = new ArrayList<String>();
    ArrayList<String> arrayList_walletTypeName = new ArrayList<String>();
    ArrayList<String> arrayList_wallet_value = new ArrayList<String>();

    TextView insitute_textview,agent_textview,insitute_branch,mainwallet_textview,overdraft_value_heding_textview,commision_wallet_textview,overdraft_wallet_textview,commisionwallet_value_textview;

    TextView spinner_currency;

    SearchAdapterTransactionDetails adpter;
    String walletCode;
    TextView tvRefresh,tvView,tvViewHide;
    private NestedScrollView nestedSV;
    private ProgressBar loadingPB;
    int page = 0, limit = 20;



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

        setContentView(R.layout.transaction_history_mainpage);

        bottomBar = findViewById(R.id.bottomBar);
        imgNotification = findViewById(R.id.imgNotification);
        imgNotification.setOnClickListener(this);
        imgQR = findViewById(R.id.imgQR);
        imgQR.setOnClickListener(this);
        MyApplication.hideKeyboard(this);
        bottomBar.setItemActiveIndex(1);
        bottomBar.setBarIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));


        cardMainWallet = findViewById(R.id.cardMainWallet);
        cardCommissionWallet = findViewById(R.id.cardCommissionWallet);
        cardOverdraftWallet = findViewById(R.id.cardOverdraftWallet);
        cardMainWallet.setOnClickListener(this);
        cardCommissionWallet.setOnClickListener(this);
        cardOverdraftWallet.setOnClickListener(this);

        cardMainWallet.setEnabled(true);
        cardCommissionWallet.setEnabled(true);
        cardOverdraftWallet.setEnabled(false);

        tvRefresh = findViewById(R.id.tvRefresh);
        tvRefresh.setOnClickListener(this);
        tvView = findViewById(R.id.tvView);
        tvView.setOnClickListener(this);
        tvViewHide = findViewById(R.id.tvViewHide);
        tvViewHide.setOnClickListener(this);
        loadingPB = findViewById(R.id.loadingPB);
        nestedSV = findViewById(R.id.nestedSV);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        spinner_currency= findViewById(R.id.spinner_currency);
        spinner_currency.setText("Select Currency");
        spinner_currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerDialogCurrency!=null)
                    spinnerDialogCurrency.showSpinerDialog();
            }
        });

        insitute_textview =(TextView)findViewById(R.id.insitute_textview);
        insitute_branch =(TextView)findViewById(R.id.insitute_branch);
        insitute_branch.setOnClickListener(this);

        agent_textview =(TextView)findViewById(R.id.agent_textview);
        agent_textview.setOnClickListener(this);

        mainwallet_textview =(TextView)findViewById(R.id.mainwallet_textview);
        commision_wallet_textview =(TextView)findViewById(R.id.commision_wallet_textview);
        search_imageView =(ImageView) findViewById(R.id.search_imageView);
        search_imageView.setOnClickListener(this);
        overdraft_wallet_textview =(TextView)findViewById(R.id.overdraft_wallet_textview);
        main_wallet_value_textview =(TextView)findViewById(R.id.main_wallet_value_textview);
        commisionwallet_value_textview =(TextView)findViewById(R.id.commisionwallet_value_textview);
        overdraft_value_heding_textview =(TextView)findViewById(R.id.overdraft_value_heding_textview);

        if(MyApplication.getSaveString("walletOwnerCategoryCode", TransactionHistoryMainPage.this).equalsIgnoreCase(MyApplication.InstituteCode)){
            insitute_textview.setClickable(false);
            insitute_textview.setVisibility(View.GONE);
        }
        if(MyApplication.getSaveString("walletOwnerCategoryCode",TransactionHistoryMainPage.this).equalsIgnoreCase(MyApplication.AgentCode)){
            agent_textview.setClickable(false);
            insitute_textview.setVisibility(View.GONE);
            agent_textview.setVisibility(View.GONE);

        }
        if(MyApplication.getSaveString("walletOwnerCategoryCode",TransactionHistoryMainPage.this).equalsIgnoreCase(MyApplication.BranchCode)){
            insitute_branch.setClickable(false);
            insitute_textview.setVisibility(View.GONE);
            agent_textview.setVisibility(View.GONE);
            insitute_branch.setVisibility(View.GONE);
        }

        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int bottomId) {
                if (bottomId == 0) {
                    Intent i = new Intent(TransactionHistoryMainPage.this, MainActivity.class);
                    startActivity(i);
                    //  finish();
                }
                if (bottomId == 1) {



//                    Intent i = new Intent(WalletScreen.this, WalletScreen.class);
//                    startActivity(i);
//                    finish();


                }
                if (bottomId == 2) {
                    Intent i = new Intent(TransactionHistoryMainPage.this, Profile.class);
                    startActivity(i);
                    //  finish();
                }
                return true;
            }
        });


        edittext_search =(EditText)findViewById(R.id.edittext_search);
        edittext_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (new InternetCheck().isConnected(TransactionHistoryMainPage.this)) {

                    searchStr = edittext_search.getText().toString().trim();

                   // adpter.filter(s.toString());


                } else {
                    Toast.makeText(TransactionHistoryMainPage.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                }
            }
        });

        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    // page+=20;
                    limit+=20;
                    loadingPB.setVisibility(View.VISIBLE);
                    callApiMiniStatementTrans(walletCode,walletTypeCode,page,limit);
                }
            }
        });



        if (new InternetCheck().isConnected(TransactionHistoryMainPage.this)) {
            MyApplication.showloader(TransactionHistoryMainPage.this, getString(R.string.please_wait));
            callApiFromCurrency(MyApplication.getSaveString("userCountryCode", TransactionHistoryMainPage.this));



           /* if( MyApplication.currencyModelArrayList.size()<1) {
                MyApplication.showloader(TransactionHistoryMainPage.this, getString(R.string.getting_user_info));
                callApiFromCurrency(MyApplication.getSaveString("userCountryCode", TransactionHistoryMainPage.this));
            }else{
                createList();
            }*/

        } else {
            Toast.makeText(TransactionHistoryMainPage.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
        }

        /* pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pulltorefresh);
        pullToRefreshLayout.setPullToRefreshListener(new PullToRefreshListener() {
            // Start refreshing stuff
            @Override
            public void onStartRefresh(@Nullable View view) {
                //callApiFromCurrency(MyApplication.getSaveString("userCountryCode", TransactionHistoryMainPage.this));
            }
        });*/

        tvView.setVisibility(View.VISIBLE);
        tvViewHide.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        loadingPB.setVisibility(View.GONE);
        tvRefresh.setVisibility(View.GONE);

    }

    PullToRefreshLayout pullToRefreshLayout;
    @Override
    protected void onRestart() {
        super.onRestart();
        MyApplication.hideKeyboard(this);
        bottomBar.setItemActiveIndex(1);
        bottomBar.setBarIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.hideKeyboard(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.hideKeyboard(this);
    }
    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.hideKeyboard(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyApplication.hideKeyboard(this);
        if(MyApplication.getSaveString("walletOwnerCategoryCode", TransactionHistoryMainPage.this).equalsIgnoreCase(MyApplication.InstituteCode)){
            insitute_textview.setClickable(false);
            insitute_textview.setVisibility(View.GONE);
            MyApplication.AgentPage=false;
            MyApplication.BranchPage=false;
            MyApplication.InstPage=true;
        }
        if(MyApplication.getSaveString("walletOwnerCategoryCode",TransactionHistoryMainPage.this).equalsIgnoreCase(MyApplication.AgentCode)){
            agent_textview.setClickable(false);
            insitute_textview.setVisibility(View.GONE);
            agent_textview.setVisibility(View.GONE);
            MyApplication.AgentPage=true;
            MyApplication.BranchPage=false;
            MyApplication.InstPage=false;

        }
        if(MyApplication.getSaveString("walletOwnerCategoryCode",TransactionHistoryMainPage.this).equalsIgnoreCase(MyApplication.BranchCode)){
            insitute_branch.setClickable(false);
            insitute_textview.setVisibility(View.GONE);
            agent_textview.setVisibility(View.GONE);
            insitute_branch.setVisibility(View.GONE);
            MyApplication.AgentPage=false;
            MyApplication.BranchPage=true;
            MyApplication.InstPage=false;
        }



    }

    private void api_wallet_walletOwner() {

        String usercode_from_msis =  MyApplication.getSaveString("walletOwnerCode", TransactionHistoryMainPage.this);

        API.GET_TRANSFER_DETAILS("ewallet/api/v1/wallet/walletOwner/"+usercode_from_msis,languageToUse,new Api_Responce_Handler() {

            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    arrayList_modalDetails=new ArrayList<>();

                    arrayList_modalDetails.clear();



                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");

                    if(resultCode.equalsIgnoreCase("0")) {

                         createList(jsonObject);


                    }



                    else {
                        Toast.makeText(TransactionHistoryMainPage.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(TransactionHistoryMainPage.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(TransactionHistoryMainPage.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    private void getTransactionList() {

        String usercode_from_msis =  MyApplication.getSaveString("USERCODE", TransactionHistoryMainPage.this);


        API.GET("ewallet/api/v1/transaction/all?srcWalletOwnerCode="+usercode_from_msis+"&offset=0&limit=100", new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                transactionList.clear();
                String taxName;

                if(jsonObject != null && jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                    JSONArray dataArray = jsonObject.optJSONArray("transactionsList");
                    if(dataArray!=null&&dataArray.length()>0) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject data = dataArray.optJSONObject(i);
                            if(data.has("taxConfigurationList")){
                                JSONArray taxArray = data.optJSONArray("taxConfigurationList");
                                taxName = taxArray.optJSONObject(0).optString("taxTypeName");
                            }else{
                                taxName = "N/A";
                            }
                            transactionList.add(new TransactionModel(
                                    data.optInt("id"),
                                    data.optString("code"),
                                    data.optString("transactionId"),
                                    data.optString("transTypeCode"),
                                    data.optString("transTypeName"),
                                    data.optString("srcWalletOwnerCode"),
                                    data.optString("srcWalletOwnerName"),
                                    data.optString("desWalletOwnerCode"),
                                    data.optString("desWalletOwnerName"),
                                    data.optString("srcWalletCode"),
                                    data.optString("desWalletCode"),
                                    data.optString("srcCurrencyCode"),
                                    data.optString("srcCurrencyName"),
                                    data.optString("srcCurrencySymbol"),
                                    data.optString("desCurrencyCode"),
                                    data.optString("desCurrencyName"),
                                    data.optString("desCurrencySymbol"),
                                    data.optInt("transactionAmount"),
                                    data.optString("tax"),
                                    data.optString("resultCode"),
                                    data.optString("resultDescription"),
                                    data.optString("creationDate"),
                                    data.optString("createdBy"),
                                    data.optString("status"),
                                    data.optBoolean("transactionReversed"),
                                    data.optInt("srcMobileNumber"),
                                    data.optInt("destMobileNumber"),
                                    data.optBoolean("receiverBearer"),
                                    data.optString("rechargeNumber"),
                                    data.optInt("fee"),
                                    taxName,
                                    data.optInt("value"),
                                    data.optInt("srcPreBalance"),
                                    data.optInt("destPreBalance"),
                                    data.optInt("srcPostBalance"),
                                    data.optInt("destPostBalance")));

                        }

                        setDatas(transactionList);



                    }
                }else{
                    MyApplication.showToast(TransactionHistoryMainPage.this,jsonObject.optString("resultDescription"));
                }


            }

            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(TransactionHistoryMainPage.this,aFalse);
            }
        });
    }

    private void setDatas(List<TransactionModel> transactionList) {

        transactionListAdapterNew = new TransactionListAdapterNew(TransactionHistoryMainPage.this,transactionList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(transactionListAdapterNew);

    }


  /*  ///////////-----code by Abhay-----////////////*/

    String wallettypecode;
    private void callApiMiniStatementTrans(String walletCode, String walletTypeCode, int page, int limit) {
        try {
            wallettypecode = walletTypeCode;
            //miniStatementTransList.clear();
            //setData(miniStatementTransList,walletTypeCode);
            //MyApplication.showloader(TransactionHistoryMainPage.this,"Please wait!");
            API.GET("ewallet/api/v1/miniStatement/allByCriteria?"+"walletCode="+walletCode+"&selectedCategory="+walletTypeCode+"&offset="+page+"&limit="+limit,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            //MyApplication.hideLoader();

                            if (jsonObject != null) {
                                miniStatementTransList.clear();
                                String name,msisdn,toName,tomssisdn;

                                if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                                    if(walletTypeCode.equalsIgnoreCase("100008")){
                                        //tvViewHide.setText("Main Wallet transaction history");
                                    }
                                    if(walletTypeCode.equalsIgnoreCase("100009")){
                                        //tvViewHide.setText("Commission Wallet transaction history");
                                    }
                                    JSONObject jsonObjectMiniStatementTrans = jsonObject.optJSONObject("miniStatement");
                                    JSONArray miniStatementTransListArr = jsonObjectMiniStatementTrans.optJSONArray("walletTransactionList");
                                    if(miniStatementTransListArr!=null&& miniStatementTransListArr.length()>0){
                                        for (int i = 0; i < miniStatementTransListArr.length(); i++) {
                                            JSONObject data = miniStatementTransListArr.optJSONObject(i);
                                            if(data.has("receiverCustomer")){
                                                tomssisdn = data.optJSONObject("receiverCustomer").optString("mobileNumber");
                                                toName = data.optJSONObject("receiverCustomer").optString("firstName")+" "+data.optJSONObject("receiverCustomer").optString("lastName");
                                            }else{
                                                tomssisdn = data.optString("toWalletOwnerMsisdn").trim();
                                                toName =  data.optString("toWalletOwnerName").trim();

                                            }

                                            if(data.has("senderCustomer")){
                                                msisdn = data.optJSONObject("senderCustomer").optString("mobileNumber");
                                                name = data.optJSONObject("senderCustomer").optString("firstName")+" "+data.optJSONObject("receiverCustomer").optString("lastName");
                                            }else{
                                                msisdn = data.optString("fromWalletOwnerMsisdn").trim();
                                                name =  data.optString("fromWalletOwnerName").trim();

                                            }

                                            miniStatementTransList.add(new MiniStatementTrans(data.optInt("id"),
                                                    data.optString("code"),
                                                    data.optString("transactionId"),
                                                    data.optString("fromWalletOwnerCode").trim(),
                                                    data.optString("toWalletOwnerCode").trim(),
                                                    name,
                                                    toName,
                                                    msisdn,
                                                    tomssisdn,
                                                    data.optString("fromWalletCode").trim(),
                                                    data.optString("fromWalletName").trim(),
                                                    data.optString("fromCurrencyCode").trim(),
                                                    data.optString("toCurrencyCode").trim(),
                                                    data.optString("fromCurrencyName").trim(),
                                                    data.optString("toCurrencyName").trim(),
                                                    data.optString("fromCurrencySymbol").trim(),
                                                    data.optString("toCurrencySymbol").trim(),
                                                    data.optString("transactionTypeCode").trim(),
                                                    data.optString("transactionTypeName").trim(),
                                                    data.optString("creationDate").trim(),
                                                    data.optString("comReceiveWalletCode").trim(),
                                                    data.optString("taxAsJson").trim(),
                                                    data.optString("holdingAccountCode").trim(),
                                                    data.optString("status").trim(),
                                                    data.optDouble("fromAmount"),
                                                    data.optDouble("toAmount"),
                                                    data.optDouble("comReceiveAmount"),
                                                    data.optDouble("srcPostBalance"),
                                                    data.optDouble("srcPreviousBalance"),
                                                    data.optDouble("destPreviousBalance"),
                                                    data.optDouble("destPostBalance"),
                                                    data.optDouble("commissionAmountForInstitute"),
                                                    data.optDouble("commissionAmountForAgent"),
                                                    data.optDouble("commissionAmountForBranch"),
                                                    data.optDouble("commissionAmountForMerchant"),
                                                    data.optDouble("commissionAmountForOutlet"),
                                                    data.optDouble("transactionAmount"),
                                                    data.optDouble("principalAmount"),
                                                    data.optString("fromWalletOwnerSurname").trim(),
                                                    data.optString("fromWalletTypeCode").trim(),
                                                    data.optBoolean("isReverse")));
                                        }

                                        setData(miniStatementTransList,walletTypeCode);
                                        loadingPB.setVisibility(View.GONE);
                                    }

                                } else {
                                    loadingPB.setVisibility(View.GONE);
                                    setData(miniStatementTransList,walletTypeCode);
                                    MyApplication.showToast(TransactionHistoryMainPage.this,jsonObject.optString("resultDescription"));
                                }

                            }
                        }

                        @Override
                        public void failure(String aFalse) {
                           // MyApplication.hideLoader();
                            loadingPB.setVisibility(View.GONE);

                        }
                    });

        } catch (Exception e) {

        }

    }

    private void setData(List<MiniStatementTrans> miniStatementTransList,String walletTypeCode){
        MiniStatementTransAdapter miniStatementTransAdapter = new MiniStatementTransAdapter(TransactionHistoryMainPage.this,miniStatementTransList,walletTypeCode);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(miniStatementTransAdapter);
    }

    String walletTypeCode="100008";
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvRefresh:
                walletCode = MyApplication.currencyModelArrayList.get(SpinnerPos).code;
                page = 0;
                limit = 20;
                loadingPB.setVisibility(View.VISIBLE);
                callApiMiniStatementTrans(walletCode,walletTypeCode, page, limit);
                break;
            case R.id.tvView:
                tvViewHide.setVisibility(View.VISIBLE);
                tvView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                tvRefresh.setVisibility(View.VISIBLE);
                loadingPB.setVisibility(View.VISIBLE);
                page = 0;
                limit = 20;
                callApiMiniStatementTrans(walletCode,walletTypeCode, page, limit);
                break;
            case R.id.tvViewHide:
                page=0;
                limit=20;
                tvView.setVisibility(View.VISIBLE);
                tvViewHide.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                tvRefresh.setVisibility(View.GONE);
                loadingPB.setVisibility(View.GONE);
                break;

            case R.id.cardMainWallet:
                page = 0;
                limit = 20;
                walletCode = MyApplication.currencyModelArrayList.get(SpinnerPos).code;
                walletTypeCode="100008";
                callApiMiniStatementTrans(walletCode,walletTypeCode, page, limit);

                break;
            case R.id.cardCommissionWallet:
                page = 0;
                limit = 20;
                walletCode = MyApplication.currencyModelArrayList.get(SpinnerPos).Ccode;
                walletTypeCode="100009";
                callApiMiniStatementTrans(walletCode,walletTypeCode, page, limit);
                break;
            case R.id.cardOverdraftWallet:
                page = 0;
                limit = 20;
                walletCode = MyApplication.currencyModelArrayList.get(SpinnerPos).Ocode;
                walletTypeCode="100011";
                callApiMiniStatementTrans(walletCode,walletTypeCode, page, limit);
                break;
            case R.id.imgNotification:
                Intent in = new Intent(TransactionHistoryMainPage.this, NotificationList.class);
                startActivity(in);
                break;
            case R.id.imgQR:
                Intent intent = new Intent(TransactionHistoryMainPage.this, ShowProfileQr.class);
                startActivity(intent);
                break;

            case R.id.search_imageView:

               // Toast.makeText(TransactionHistoryMainPage.this, "---------search_imageView-------", Toast.LENGTH_LONG).show();

                break;

            case R.id.agent_textview:

                Intent i = new Intent(TransactionHistoryMainPage.this, TransactionHistoryAgent.class);
                startActivity(i);

                break;

            case R.id.insitute_branch:

                Intent iiiii = new Intent(TransactionHistoryMainPage.this, TransactionHistoryBranch.class);
                startActivity(iiiii);

                break;



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
                                    MyApplication.showToast(TransactionHistoryMainPage.this,jsonObject.optString("resultDescription", "  "));
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

    ArrayList arrayList=new ArrayList();

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



        }

       /* CurrencyListTransaction arraadapter2 = new CurrencyListTransaction(TransactionHistoryMainPage.this, MyApplication.currencyModelArrayList);
        spinner_currency.setAdapter(arraadapter2);
*/


        spinnerDialogCurrency = new SpinnerDialog(TransactionHistoryMainPage.this, arrayList, "Select Currency", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation

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

     //  String currencyName_mssis_agent = MyApplication.getSaveString("CURRENCYNAME_AGENT", TransactionHistoryMainPage.this);
       String currencyName_mssis_agent = currencyCode;  // no currency tag is comming in MSSID

        for (int i = 0; i < MyApplication.currencyModelArrayList.size(); i++) {
            if (currencyName_mssis_agent.equalsIgnoreCase(MyApplication.currencyModelArrayList.get(i).getCurrencyName()))
            {
                walletCode = MyApplication.currencyModelArrayList.get(i).code;
                System.out.println("currency Symbol"+MyApplication.currencyModelArrayList.get(i).currencySymbol);
                setSelctionCurrency(i);
                MyApplication.currencySymbol=MyApplication.currencyModelArrayList.get(i).currencySymbol;
            }
        }

      //  pullToRefreshLayout.refreshDone();

       // MyApplication.showloader(TransactionHistoryMainPage.this, getString(R.string.getting_user_info));

      //  api_transactionHistory_all();

       // getTransactionList();

       // callApiMiniStatementTrans(walletCode,"100008");

    }


    public void setSelctionCurrency(int i){
        SpinnerPos = i;
        MyApplication.currencySymbol=MyApplication.currencyModelArrayList.get(i).currencySymbol;
        walletCode = MyApplication.currencyModelArrayList.get(i).code;
        mainwallet_textview.setText(MyApplication.currencyModelArrayList.get(i).mainWalletValue);
        commision_wallet_textview.setText(MyApplication.currencyModelArrayList.get(i).commisionWalletValue);
        overdraft_wallet_textview.setText(MyApplication.currencyModelArrayList.get(i).overdraftWalletValue);
        spinner_currency.setText(MyApplication.currencyModelArrayList.get(i).currencyName);

        page = 0;
        limit = 20;
        loadingPB.setVisibility(View.VISIBLE);
        callApiMiniStatementTrans(walletCode,walletTypeCode, page, limit);

    }

   /* public void createList(){
        *//*CurrencyListTransaction arraadapter2 = new CurrencyListTransaction(TransactionHistoryMainPage.this, MyApplication.currencyModelArrayList);
        spinner_currency.setAdapter(arraadapter2);*//*


        //  String currencyName_mssis_agent = MyApplication.getSaveString("CURRENCYNAME_AGENT", TransactionHistoryMainPage.this);
        String currencyName_mssis_agent = currencyCode;  // no currency tag is comming in MSSID

        for (int i = 0; i < MyApplication.currencyModelArrayList.size(); i++) {
            if (currencyName_mssis_agent.equalsIgnoreCase(MyApplication.currencyModelArrayList.get(i).getCurrencyName()))
            {
                walletCode = MyApplication.currencyModelArrayList.get(i).code;
                spinner_currency.setSelection(i);
            }
        }
    }*/

    int SpinnerPos;
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinner_currency:
            {

                try {



//                 //   Toast.makeText(TransactionHistoryMainPage.this, MyApplication.currencyModelArrayList.get(i).mainWalletValue.toString()+"---commisiiom---"
//                            +MyApplication.currencyModelArrayList.get(i).commisionWalletValue.toString()+
//                            "----overdraft"+MyApplication.currencyModelArrayList.get(i).overdraftWalletValue.toString(), Toast.LENGTH_SHORT).show()
//                    ;

//                    select_currency_name = arrayList_currecnyName.get(i);
//                    select_currency_code = arrayList_currecnyCode.get(i);
//                    select_walletType_name = arrayList_walletTypeName.get(i);
//                    select_walletValue_name = arrayList_wallet_value.get(i);

//                    if(spinner_currency.getSelectedItemPosition()==0)
//                    {
//                        main_wallet_value_textview.setText("");
//                        commisionwallet_value_textview.setText("");
//                        overdraft_value_heding_textview.setText("");
//                    }
//                    else {

//                        if(select_walletType_name.equalsIgnoreCase("Main Wallet"))
//                        {
//                            main_wallet_value_textview.setText(select_walletValue_name);
//                        }
//                        else if(select_walletType_name.equalsIgnoreCase("Comission Wallet"))
//                        {
//                            commisionwallet_value_textview.setText(select_walletValue_name);
//                        }
//                        else if(select_walletType_name.equalsIgnoreCase("Overdraft Wallet"))
//                        {
//                            overdraft_value_heding_textview.setText(select_walletValue_name);
//                        }




                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(TransactionHistoryMainPage.this, e.toString(), Toast.LENGTH_LONG).show();

                }

            }

            break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onTransactionViewItemClick(String transId, String transType, String transDate, String source, String destination, int sourceMsisdn, int destMsisdn, String symbol, int amount, int fee, String taxType, String tax, int postBalance, String status) {
        Dialog dialog = new Dialog(TransactionHistoryMainPage.this, R.style.AppTheme);  //android.R.style.Theme_Translucent_NoTitleBar
        dialog.setContentView(R.layout.dialog_view_trans_details);

        //get ids
        TextView etTransId = dialog.findViewById(R.id.etTransId);
        TextView etTransType = dialog.findViewById(R.id.etTransType);
        TextView etTransDate = dialog.findViewById(R.id.etTransDate);
        TextView etSource = dialog.findViewById(R.id.etSource);
        TextView etDestination = dialog.findViewById(R.id.etDestination);
        TextView etSourcMSISDN = dialog.findViewById(R.id.etSourcMSISDN);
        TextView etDestMSISDN = dialog.findViewById(R.id.etDestMSISDN);
        TextView etAmount = dialog.findViewById(R.id.etAmount);
        TextView etFee = dialog.findViewById(R.id.etFee);
        TextView etTaxType = dialog.findViewById(R.id.etTaxType);
        TextView etTax = dialog.findViewById(R.id.etTax);
        TextView etPostBalance = dialog.findViewById(R.id.etPostBalance);
        TextView etStatus = dialog.findViewById(R.id.etStatus);

        //set values
        etTransId.setText(transId);
        etTransType.setText(transType);
        etSource.setText(source);
        etDestination.setText(destination);
        etSourcMSISDN.setText(String.valueOf(sourceMsisdn));
        etDestMSISDN.setText(String.valueOf(destMsisdn));
        etAmount.setText(symbol+" "+MyApplication.addDecimal(String.valueOf((amount))));
        etFee.setText(MyApplication.addDecimal(String.valueOf((fee))));
        etTaxType.setText(taxType);
        etTax.setText(MyApplication.addDecimal(String.valueOf((tax))));
        etPostBalance.setText(symbol+" "+MyApplication.addDecimal(String.valueOf((postBalance))));
        etStatus.setText(status);
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
            Date date = null;
            date = inputFormat.parse(transDate);
            String formattedDate = outputFormat.format(date);
            etTransDate.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dialog.findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    @Override
    public void onMiniStatementListItemClick(String transactionTypeName, String fromWalletOwnerName,
                                             String toWalletOwnerName, String fromWalletOwnerMsisdn,
                                             String currencySymbol, double fromAmount, String transactionId,
                                             String creationDate, String status,
                                             double commissionAmount,String toWalletOwnerMsisdn,double transactionAmount) {
//        String name="";
//        if(fromWalletOwnerName.isEmpty()||fromWalletOwnerName==null){
//            name = walletOwnerMsisdn;
//        }else{
//            name = fromWalletOwnerName+" ("+walletOwnerMsisdn+")";
//        }
        NumberFormat df = DecimalFormat.getInstance();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        df.setRoundingMode(RoundingMode.DOWN);
        Intent intent = new Intent(TransactionHistoryMainPage.this, WalletTransactionDetails.class);
        intent.putExtra("TRANSTYPE",transactionTypeName);
        intent.putExtra("FROMWALLETOWNERNAME",fromWalletOwnerName);
        intent.putExtra("TOWALLETOWNERNAME",toWalletOwnerName);
        intent.putExtra("FROMAMOUNT",MyApplication.currencySymbol+" "+df.format(fromAmount));
        intent.putExtra("TRANSID",transactionId);
        intent.putExtra("CREATIONDATE",creationDate);
        intent.putExtra("STATUS",status);
        intent.putExtra("COMMISSIONAMOUNT",commissionAmount);
        intent.putExtra("WALLETTYPECODE",wallettypecode);
        intent.putExtra("FROMMSISDN",fromWalletOwnerMsisdn);
        intent.putExtra("TOMSISDN",toWalletOwnerMsisdn);
        intent.putExtra("TRANSACTIONAMOUNT",MyApplication.currencySymbol+" "+df.format(transactionAmount));
        startActivity(intent);
    }




}