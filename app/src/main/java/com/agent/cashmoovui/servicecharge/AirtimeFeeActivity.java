package com.agent.cashmoovui.servicecharge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.adapter.AirtimeFeeOperatorAdapter;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.listeners.OperatorAirtimeFeeListeners;
import com.agent.cashmoovui.model.OperatorModel;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class AirtimeFeeActivity extends AppCompatActivity implements View.OnClickListener, OperatorAirtimeFeeListeners {
    public static AirtimeFeeActivity airtimefeeC;
    ImageView imgBack,imgHome;
    RecyclerView rvOperator;
    TextView tvServiceName;
    Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_airtime_fee);
        airtimefeeC=this;
        //setBackMenu();
        getIds();
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
//
//    private void setBackMenu() {
//        imgBack = findViewById(R.id.imgBack);
//        imgHome = findViewById(R.id.imgHome);
//
//
//
//        imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSupportNavigateUp();
//            }
//        });
//        imgHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });
//
//    }

    private void getIds() {
        rvOperator = findViewById(R.id.rvOperator);
        btnClose = findViewById(R.id.btnClose);
        tvServiceName = findViewById(R.id.tvServiceName);
        tvServiceName.setText(getString(R.string.airtime_purchase));

        setOnCLickListener();

        callApiAirtimeOperatorProvider();

    }

    private void setOnCLickListener() {
        btnClose.setOnClickListener(airtimefeeC);
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onOperatorAirtimeFeeListItemClick(String code, String name) {
        callAirtimeFee(code);
    }

    private ArrayList<OperatorModel> operatorAirtimeList = new ArrayList<>();
    private void callApiAirtimeOperatorProvider() {
        try {

            MyApplication.showloader(airtimefeeC,"Please Wait...");
            API.GET("ewallet/api/v1/operator/allByCriteria?serviceCode=100009&serviceCategoryCode=100021&status=Y&offset=0&limit=200",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                operatorAirtimeList.clear();
                                //serviceProviderModelList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    //  serviceProvider = serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("name");
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("operatorList");
                                    if(walletOwnerListArr!=null&& walletOwnerListArr.length()>0) {
                                        for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                            JSONObject data = walletOwnerListArr.optJSONObject(i);
                                            operatorAirtimeList.add(new OperatorModel(
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
                                        AirtimeFeeOperatorAdapter airtimeFeeOperatorAdapter = new AirtimeFeeOperatorAdapter(airtimefeeC,operatorAirtimeList);
                                        rvOperator.setHasFixedSize(true);
                                        rvOperator.setLayoutManager(new LinearLayoutManager(airtimefeeC, LinearLayoutManager.VERTICAL,false));
                                        rvOperator.setAdapter(airtimeFeeOperatorAdapter);
                                    }

                                } else {
                                    MyApplication.showToast(airtimefeeC,jsonObject.optString("resultDescription", "N/A"));
                                }
                            }
                        }

                        @Override
                        public void failure(String aFalse) {
                            MyApplication.hideLoader();

                        }
                    });

        } catch (Exception e) {
            MyApplication.hideLoader();
        }


    }

    public static JSONObject mainJsonObject=null;

    private void callAirtimeFee(String operatorCode) {
        try {

            API.GET("ewallet/api/v1/walletOwnerTemplate/walletOwnerCode/fee/"+MyApplication.getSaveString("walletOwnerCode", airtimefeeC)+"/"+operatorCode,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {

                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    mainJsonObject = new JSONObject();
                                    mainJsonObject = jsonObject;
                                    JSONArray walletOwnerTemplateList=jsonObject.optJSONArray("walletOwnerTemplateList");
                                    if(walletOwnerTemplateList!=null&& walletOwnerTemplateList.length()>0) {
                                        JSONObject data=walletOwnerTemplateList.optJSONObject(0);

                                        if(data.has("feeTemplateList")){
                                            JSONArray feeTemplateList=data.optJSONArray("feeTemplateList");
                                            for (int i=0;i<feeTemplateList.length();i++){
                                                JSONObject feeObj=feeTemplateList.optJSONObject(i);
                                                if(feeObj.has("calculationTypeName")) {
                                                    Intent in = new Intent(airtimefeeC, ServiceChargeDetails.class);
                                                    in.putExtra("FEEINTENT","Airtime Purchase");
                                                    //in.putExtra("OPERATORCODE",operatorCode);
                                                    startActivity(in);
                                                }else{
                                                    MyApplication.showToast(airtimefeeC,getString(R.string.range_value_not_available));
                                                }

                                            }


                                        }else{
                                            MyApplication.showToast(airtimefeeC,getString(R.string.range_value_not_available));
                                        }
                                    }


                                } else {
                                    MyApplication.showToast(airtimefeeC,jsonObject.optString("resultDescription", "N/A"));
                                }
                            }
                        }

                        @Override
                        public void failure(String aFalse) {
                            MyApplication.hideLoader();

                        }
                    });

        } catch (Exception e) {
            MyApplication.hideLoader();
        }


    }



}
