package com.agent.cashmoovui.internationaltransfer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.adapter.OutAdapter;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.listeners.OperatorListenersnew;
import com.agent.cashmoovui.model.OutTransferModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OutTransfer extends AppCompatActivity implements OperatorListenersnew {
    public static OutTransfer billpayC;
    ImageView imgBack,imgHome;
    RecyclerView rvOperator;
    private ArrayList<String> serviceProviderList = new ArrayList<>();
    private ArrayList<OutTransferModel.OutModel> serviceProviderModelList = new ArrayList<>();

    private ArrayList<OutTransferModel.OutModel> operatorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billpay);
        billpayC=this;
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
                MyApplication.hideKeyboard(billpayC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.isFirstTime=false;
                MyApplication.hideKeyboard(billpayC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

TextView  opt_text;
    private void getIds() {
        rvOperator = findViewById(R.id.rvOperator);
        opt_text = findViewById(R.id.opt_text);
        opt_text.setText("Choose the Service");


      //  callJSON();

        CallApiOutboundServiceJsonList();

        // callwalletOwner();
    }

    public static String serviceProvider,mobile,currency,currencySymbol;
    public static JSONObject serviceCategory = new JSONObject();

    public void callwalletOwner(){

        MyApplication.showloader(billpayC,"Please Wait...");
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
                                    currency = data.optString("currencyName");
                                    currencySymbol = data.optString("currencySymbol");
                                }

                            }


                        }
                    }

                    }else{
                      MyApplication.showToast(billpayC,jsonObject.optString("resultDescription"));
                }

            }


            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(billpayC,aFalse);
            }
        });

       // callApioperatorProvider();
        //callJSON();
        CallApiOutboundServiceJsonList();

    }




    private void callApioperatorProvider() {
        try {

            API.GET("ewallet/api/v1/providerServiceItem/allByCriteria?serviceCode=100023&serviceCategoryCode=TRTWLT",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {

                                //serviceProviderModelList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                  //  serviceProvider = serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("name");
                                    serviceCategory = jsonObject;
                                    JSONArray walletOwnerListArr = serviceCategory.optJSONArray("providerserviceitemslist");
                                    if(walletOwnerListArr!=null&& walletOwnerListArr.length()>0) {
                                        for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                            JSONObject data = walletOwnerListArr.optJSONObject(i);
                                            serviceProviderModelList.add(new OutTransferModel.OutModel(
                                                    data.optInt("id"),
                                                    data.optString("code"),
                                                    data.optString("serviceCode"),
                                                    data.optString("serviceCategoryCode"),
                                                    data.optString("serviceProviderCode"),
                                                    data.optString("serviceItemId"),
                                                    data.optString("nameFr"),
                                                    data.optString("name"),
                                                    data.optString("status"),
                                                    data.optString("state"),
                                                    data.optString("creationDate")
                                            ));

                                        }

                                        setData(serviceProviderModelList);
                                    }

                                } else {
                                    MyApplication.showToast(billpayC,jsonObject.optString("resultDescription", "N/A"));
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


    public  void CallApiOutboundServiceJsonList(){
        MyApplication.showloader(OutTransfer.this,"Please wait");
        API.GET("ewallet/api/v1/productMaster/allByCriteria?serviceCategoryCode=TRTWLT&status=Y",
                new Api_Responce_Handler() {
                    @Override
                    public void success(JSONObject jsonObject) {
                        MyApplication.hideLoader();
                        if (jsonObject != null) {

                            //serviceProviderModelList.clear();
                            if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                //  serviceProvider = serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("name");
                                serviceCategory = jsonObject;
                                JSONArray walletOwnerListArr = serviceCategory.optJSONArray("productMasterList");
                                if(walletOwnerListArr!=null&& walletOwnerListArr.length()>0) {
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        serviceProviderModelList.add(new OutTransferModel.OutModel(
                                                data.optInt("id"),
                                                data.optString("code"),
                                                data.optString("serviceCode"),
                                                data.optString("serviceCategoryCode"),
                                                data.optString("serviceProviderCode"),
                                                data.optString("serviceItemId"),
                                                data.optString("productName"),
                                                data.optString("productName"),
                                                data.optString("status"),
                                                data.optString("state"),
                                                data.optString("creationDate")
                                        ));

                                    }

                                    setData(serviceProviderModelList);
                                }

                            } else {
                                MyApplication.showToast(billpayC,jsonObject.optString("resultDescription", "N/A"));
                            }
                        }


                    }

                    @Override
                    public void failure(String aFalse) {
                        MyApplication.hideLoader();

                    }
                });
    }
    private void callJSON() {
        try {
            JSONObject jsonObject=new JSONObject("{\n" +
                    "  \"transactionId\": \"3317694\",\n" +
                    "  \"requestTime\": \"Mon Jun 20 05:57:02 UTC 2022\",\n" +
                    "  \"responseTime\": \"Mon Jun 20 05:57:02 UTC 2022\",\n" +
                    "  \"resultCode\": \"0\",\n" +
                    "  \"resultDescription\": \"Transaction Successful\",\n" +
                    "  \"providerserviceitemslist\": [\n" +
                    "    {\n" +
                    "      \"id\": 4,\n" +
                    "      \"code\": \"100003\",\n" +
                    "      \"serviceCode\": \"100023\",\n" +
                    "      \"serviceCategoryCode\": \"TRTWLT\",\n" +
                    "      \"serviceProviderCode\": \"100160\",\n" +
                    "      \"serviceItemId\": \"65\",\n" +
                    "      \"nameFr\": \"CashMoov à Wave\",\n" +
                    "      \"name\": \"CashMoov to Wave\",\n" +
                    "      \"status\": \"Y\",\n" +
                    "      \"state\": \"A\",\n" +
                    "      \"creationDate\": \"1654702502031\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 3,\n" +
                    "      \"code\": \"100002\",\n" +
                    "      \"serviceCode\": \"100023\",\n" +
                    "      \"serviceCategoryCode\": \"TRTWLT\",\n" +
                    "      \"serviceProviderCode\": \"100160\",\n" +
                    "      \"serviceItemId\": \"14\",\n" +
                    "      \"nameFr\": \"CashMoov vers la monnaie électronique\",\n" +
                    "      \"name\": \"CashMoov to E-Money\",\n" +
                    "      \"status\": \"Y\",\n" +
                    "      \"state\": \"A\",\n" +
                    "      \"creationDate\": \"1654702498600\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 2,\n" +
                    "      \"code\": \"100001\",\n" +
                    "      \"serviceCode\": \"100023\",\n" +
                    "      \"serviceCategoryCode\": \"TRTWLT\",\n" +
                    "      \"serviceProviderCode\": \"100160\",\n" +
                    "      \"serviceItemId\": \"17\",\n" +
                    "      \"nameFr\": \"CashMoov pour libérer de l'argent\",\n" +
                    "      \"name\": \"CashMoov to Free Money\",\n" +
                    "      \"status\": \"Y\",\n" +
                    "      \"state\": \"A\",\n" +
                    "      \"creationDate\": \"1654702493431\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 1,\n" +
                    "      \"code\": \"100000\",\n" +
                    "      \"serviceCode\": \"100023\",\n" +
                    "      \"serviceCategoryCode\": \"TRTWLT\",\n" +
                    "      \"serviceProviderCode\": \"100160\",\n" +
                    "      \"serviceItemId\": \"5\",\n" +
                    "      \"nameFr\": \"CashMoov à Orange Money\",\n" +
                    "      \"name\": \"CashMoov to Orange Money\",\n" +
                    "      \"status\": \"Y\",\n" +
                    "      \"state\": \"A\",\n" +
                    "      \"creationDate\": \"1654702369521\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}");
            if (jsonObject != null) {

                //serviceProviderModelList.clear();
                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                    //  serviceProvider = serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("name");
                    serviceCategory = jsonObject;
                    JSONArray walletOwnerListArr = serviceCategory.optJSONArray("providerserviceitemslist");
                    if(walletOwnerListArr!=null&& walletOwnerListArr.length()>0) {
                        for (int i = 0; i < walletOwnerListArr.length(); i++) {
                            JSONObject data = walletOwnerListArr.optJSONObject(i);
                            serviceProviderModelList.add(new OutTransferModel.OutModel(
                                    data.optInt("id"),
                                    data.optString("code"),
                                    data.optString("serviceCode"),
                                    data.optString("serviceCategoryCode"),
                                    data.optString("serviceProviderCode"),
                                    data.optString("serviceItemId"),
                                    data.optString("nameFr"),
                                    data.optString("name"),
                                    data.optString("status"),
                                    data.optString("state"),
                                    data.optString("creationDate")
                            ));

                        }

                        setData(serviceProviderModelList);
                    }

                } else {
                    MyApplication.showToast(billpayC,jsonObject.optString("resultDescription", "N/A"));
                }
            }

        } catch (Exception e) {

        }



    }

    private void setData(List<OutTransferModel.OutModel> operatorList){
        OutAdapter operatorAdapter = new OutAdapter(billpayC,operatorList);
        rvOperator.setHasFixedSize(true);
        rvOperator.setLayoutManager(new GridLayoutManager(this,3));
        rvOperator.setAdapter(operatorAdapter);
    }



    public static String operatorCode,operatorNname,serviceItemId;
    @Override
    public void onOperatorListItemClick(String code, String name,String serviceItemId) {
        operatorCode = code;
        operatorNname = name;
        this.serviceItemId=serviceItemId;
        Intent intent = new Intent(billpayC, Outform.class);
        startActivity(intent);
    }
}