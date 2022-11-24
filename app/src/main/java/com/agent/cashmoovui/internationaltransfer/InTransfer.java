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

public class InTransfer extends AppCompatActivity implements OperatorListenersnew {
    public static InTransfer billpayC;
    ImageView imgBack,imgHome;
    RecyclerView rvOperator;
    private ArrayList<String> serviceProviderList = new ArrayList<>();
    private ArrayList<OutTransferModel.OutModel> serviceProviderModelList = new ArrayList<>();

    private ArrayList<OutTransferModel.OutModel> operatorList = new ArrayList<>();

    TextView opt_text;
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


    private void getIds() {
        rvOperator = findViewById(R.id.rvOperator);
        opt_text = findViewById(R.id.opt_text);
        opt_text.setText("Choose the Service");

       // callJSON();
       // callwalletOwner();
        CallApiOutboundServiceJsonList();
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
        callJSON();

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
        MyApplication.showloader(InTransfer.this,"Please wait");
        API.GET("ewallet/api/v1/productMaster/allByCriteria?serviceCategoryCode=TRFWLT&status=Y",
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
                    "  \"transactionId\": \"3320329\",\n" +
                    "  \"requestTime\": \"Mon Jun 20 11:22:02 UTC 2022\",\n" +
                    "  \"responseTime\": \"Mon Jun 20 11:22:02 UTC 2022\",\n" +
                    "  \"resultCode\": \"0\",\n" +
                    "  \"resultDescription\": \"Transaction Successful\",\n" +
                    "  \"providerserviceitemslist\": [\n" +
                    "    {\n" +
                    "      \"id\": 9,\n" +
                    "      \"code\": \"100008\",\n" +
                    "      \"serviceCode\": \"100023\",\n" +
                    "      \"serviceCategoryCode\": \"TRFWLT\",\n" +
                    "      \"serviceProviderCode\": \"100161\",\n" +
                    "      \"serviceItemId\": \"43\",\n" +
                    "      \"nameFr\": \"Wizall à CashMoov\",\n" +
                    "      \"name\": \"Wizall to CashMoov\",\n" +
                    "      \"status\": \"Y\",\n" +
                    "      \"state\": \"A\",\n" +
                    "      \"creationDate\": \"1654702733133\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 8,\n" +
                    "      \"code\": \"100007\",\n" +
                    "      \"serviceCode\": \"100023\",\n" +
                    "      \"serviceCategoryCode\": \"TRFWLT\",\n" +
                    "      \"serviceProviderCode\": \"100161\",\n" +
                    "      \"serviceItemId\": \"69\",\n" +
                    "      \"nameFr\": \"Faites signe à CashMoov\",\n" +
                    "      \"name\": \"Wave to CashMoov\",\n" +
                    "      \"status\": \"Y\",\n" +
                    "      \"state\": \"A\",\n" +
                    "      \"creationDate\": \"1654702733133\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 7,\n" +
                    "      \"code\": \"100006\",\n" +
                    "      \"serviceCode\": \"100023\",\n" +
                    "      \"serviceCategoryCode\": \"TRFWLT\",\n" +
                    "      \"serviceProviderCode\": \"100161\",\n" +
                    "      \"serviceItemId\": \"19\",\n" +
                    "      \"nameFr\": \"Monnaie électronique vers CashMoov\",\n" +
                    "      \"name\": \"E-Money to CashMoov\",\n" +
                    "      \"status\": \"Y\",\n" +
                    "      \"state\": \"A\",\n" +
                    "      \"creationDate\": \"1654702733133\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 6,\n" +
                    "      \"code\": \"100005\",\n" +
                    "      \"serviceCode\": \"100023\",\n" +
                    "      \"serviceCategoryCode\": \"TRFWLT\",\n" +
                    "      \"serviceProviderCode\": \"100161\",\n" +
                    "      \"serviceItemId\": \"18\",\n" +
                    "      \"nameFr\": \"Argent gratuit pour CashMoov\",\n" +
                    "      \"name\": \"Free Money to CashMoov\",\n" +
                    "      \"status\": \"Y\",\n" +
                    "      \"state\": \"A\",\n" +
                    "      \"creationDate\": \"1654702733133\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 5,\n" +
                    "      \"code\": \"100004\",\n" +
                    "      \"serviceCode\": \"100023\",\n" +
                    "      \"serviceCategoryCode\": \"TRFWLT\",\n" +
                    "      \"serviceProviderCode\": \"100161\",\n" +
                    "      \"serviceItemId\": \"15\",\n" +
                    "      \"nameFr\": \"Orange Money vers CashMoov\",\n" +
                    "      \"name\": \"Orange Money to CashMoov\",\n" +
                    "      \"status\": \"Y\",\n" +
                    "      \"state\": \"A\",\n" +
                    "      \"creationDate\": \"1654702733133\"\n" +
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
    public void onOperatorListItemClick(String code, String name ,String serviceItemId) {
        operatorCode = code;
        operatorNname = name;
        this.serviceItemId=serviceItemId;
        Intent intent = new Intent(billpayC, Inform.class);
        startActivity(intent);
    }
}