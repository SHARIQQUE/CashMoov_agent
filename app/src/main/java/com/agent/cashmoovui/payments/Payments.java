package com.agent.cashmoovui.payments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import com.agent.cashmoovui.LogoutAppCompactActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.activity.OtherOption;
import com.agent.cashmoovui.adapter.OperatorAdapter;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.listeners.OperatorListeners;
import com.agent.cashmoovui.model.OperatorModel;
import com.agent.cashmoovui.model.ServiceProviderModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Payments extends LogoutAppCompactActivity implements OperatorListeners {
    public static Payments paymentsC;
    ImageView imgBack,imgHome;
    RecyclerView rvOperator;

    private ArrayList<String> serviceProviderList = new ArrayList<>();
    private ArrayList<ServiceProviderModel.ServiceProvider> serviceProviderModelList = new ArrayList<>();

    private ArrayList<OperatorModel> operatorList = new ArrayList<>();
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        paymentsC=this;
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
                MyApplication.hideKeyboard(paymentsC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(paymentsC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    private void getIds() {
        rvOperator = findViewById(R.id.rvOperator);


        callwalletOwner();
    }

    public static String serviceProvider,mobile,currency,currencySymbol;
    public static JSONObject serviceCategory = new JSONObject();

    public void callwalletOwner(){

        MyApplication.showloader(paymentsC,getString(R.string.pleasewait));
        API.GET("ewallet/api/v1/wallet/walletOwner/"+MyApplication.getSaveString("walletOwnerCode",getApplicationContext()), new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                if (jsonObject != null) {

                    if(jsonObject.optString("resultCode").equalsIgnoreCase("0")) {
                        if (jsonObject.has("walletList") && jsonObject.optJSONArray("walletList") != null) {
                            JSONArray walletOwnerListArr = jsonObject.optJSONArray("walletList");
                            for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                JSONObject data = walletOwnerListArr.optJSONObject(i);
                                if (data.optString("walletTypeCode").equalsIgnoreCase("100008")) {
                                    mobile = data.optString("walletOwnerMsisdn");
                                   // MyApplication.showToast(paymentsC,"CurrencySymbol--"+currencySymbol);
                                }

                            }


                        }
                    }

                    callApiFromCurrency();

                }else{
                    MyApplication.showToast(paymentsC,jsonObject.optString("resultDescription"));
                }

            }


            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(paymentsC,aFalse);
            }
        });


    }

    private void callApiFromCurrency() {
        try {

            API.GET("ewallet/api/v1/walletOwnerCountryCurrency/"+MyApplication.getSaveString("walletOwnerCode",paymentsC),
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {

                                if(jsonObject.optString("resultCode", "  ").equalsIgnoreCase("0")){
                                    JSONArray walletOwnerCurrListArr = jsonObject.optJSONArray("walletOwnerCountryCurrencyList");
                                    for (int i = 0; i < walletOwnerCurrListArr.length(); i++) {
                                        JSONObject data = walletOwnerCurrListArr.optJSONObject(i);
                                        if (data.optString("currencyCode").equalsIgnoreCase("100062")) {
                                            currency = data.optString("currencyName");
                                            currencySymbol = data.optString("currencySymbol");
                                        }

                                    }
                                    callApioperatorProvider();

                                } else {
                                    MyApplication.showToast(paymentsC,jsonObject.optString("resultDescription", "  "));
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


    private void callApioperatorProvider() {
        try {

            API.GET("ewallet/api/v1/operator/allByCriteria?serviceCode=100001&serviceCategoryCode=100028&status=Y&offset=0&limit=200",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                operatorList.clear();
                                //serviceProviderModelList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    //  serviceProvider = serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("name");
                                    serviceCategory = jsonObject;
                                    JSONArray walletOwnerListArr = serviceCategory.optJSONArray("operatorList");
                                    if(walletOwnerListArr!=null&& walletOwnerListArr.length()>0) {
                                        for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                            JSONObject data = walletOwnerListArr.optJSONObject(i);
                                            operatorList.add(new OperatorModel(
                                                    data.optInt("id"),
                                                    data.optString("code"),
                                                    data.optString("creationDate"),
                                                    data.optString("modificationDate"),
                                                    data.optString("name"),
                                                    data.optString("serviceCategoryCode"),
                                                    data.optString("serviceCategoryName"),
                                                    data.optString("serviceCode"),
                                                    data.optString("serviceName"),
                                                    data.optString("serviceProviderCode"),
                                                    data.optString("serviceProviderName"),
                                                    data.optString("state"),
                                                    data.optString("status")

                                            ));

                                        }

                                        setData(operatorList);
                                    }

                                } else {
                                    MyApplication.showToast(paymentsC,jsonObject.optString("resultDescription", "N/A"));
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

    private void setData(List<OperatorModel> operatorList){
        OperatorAdapter operatorAdapter = new OperatorAdapter(paymentsC,operatorList);
        rvOperator.setHasFixedSize(true);
        rvOperator.setLayoutManager(new GridLayoutManager(this,3));
        rvOperator.setAdapter(operatorAdapter);

    }


    public static String operatorCode,operatorName;
    @Override
    public void onOperatorListItemClick(String code, String name) {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        operatorCode = code;
        operatorName = name;
        Intent intent = new Intent(paymentsC, PaymentsProduct.class);
        startActivity(intent);
    }
}