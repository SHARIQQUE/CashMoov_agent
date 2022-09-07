package com.agent.cashmoovui.overdraft;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.adapter.OverDraftAdapterRecycle;
import com.agent.cashmoovui.adapter.OverDraftviewconfigAdapterRecycle;
import com.agent.cashmoovui.adapter.RecordAdapter;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.cash_in.CashIn;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.model.OverdraftViewconfigModel;
import com.agent.cashmoovui.set_pin.AESEncryption;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class OverdraftLimitViewConfigServices extends AppCompatActivity {

    ImageView imgBack,imgHome;
   String profileTypeCode_fromServer="",profileTypeName_fromServer="",walletOwnerName_fromServer="",entityCode_from_creditLimitAllocation="";

    RecyclerView recyclerView;

    ArrayList<OverdraftViewconfigModel> arrayList_overdraftviewconfig;

    MyApplication applicationComponentClass;
    String languageToUse = "";



    String amountstr="",agentName_from_walletOwner="",currencyCode,amountStrcheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        applicationComponentClass = (MyApplication) getApplicationContext();

        languageToUse = applicationComponentClass.getmSharedPreferences().getString("languageToUse", "");

        if (languageToUse.trim().length() == 0) {
            languageToUse = "en";
        }



        setContentView(R.layout.activity_overdraft_limit_viewconfig_services);


        recyclerView = (RecyclerView)findViewById(R.id.recyclerView_viewconfig);

         api_ViewConfig();

        setBackMenu();


       /* RecordAdapter recordAdapter = new RecordAdapter(OverdraftLimitViewConfigServices.this, strArray);
        spinner_record.setAdapter(recordAdapter);
*/





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
                MyApplication.hideKeyboard(OverdraftLimitViewConfigServices.this);
                onSupportNavigateUp();
            }
        });








    }


    //}












    private void api_ViewConfig() {

        String USER_CODE_FROM_TOKEN_AGENTDETAILS = MyApplication.getSaveString("userCode", OverdraftLimitViewConfigServices.this);

        MyApplication.showloader(OverdraftLimitViewConfigServices.this, getString(R.string.please_wait));

        API.GET_TRANSFER_DETAILS("ewallet/api/v1/creditLimitConfigServices/allByServices?walletOwnerCategoryCode="+OverdraftLimit.walletOwnerCategoryCode
                +"&profileTypeCode="+OverdraftLimit.profileTypeCode,languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    //  JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1937292\",\"requestTime\":\"Wed Nov 03 11:27:43 IST 2021\",\"responseTime\":\"Wed Nov 03 11:27:43 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"pageable\":{\"totalRecords\":1},\"creditLimitAllocationList\":[{\"id\":313,\"code\":\"100330\",\"walletOwnerCode\":\"1000002785\",\"walletOwnerName\":\"sharique agent\",\"currencyCode\":\"100062\",\"currencyName\":\"GNF\",\"currencySymbol\":\"Fr\",\"amount\":1000.0,\"validityDays\":90,\"status\":\"Approved\",\"creationDate\":\"2021-11-01T13:54:19.122+0530\",\"modificationDate\":\"2021-11-01T13:54:19.122+0530\"}]}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");


                    arrayList_overdraftviewconfig= new ArrayList<>();
                    arrayList_overdraftviewconfig.clear();


                    if (resultCode.equalsIgnoreCase("0")) {

                        recyclerView.removeAllViewsInLayout();
                        recyclerView.setVisibility(View.VISIBLE);
                      //  linearLayout_record.setVisibility(View.VISIBLE);



                        JSONArray jsonArray = jsonObject.getJSONArray("creditLimitConfigServicesBeanList");
                        for(int i=0;i<jsonArray.length();i++) {

                            OverdraftViewconfigModel overDraftModalviewconfig = new OverdraftViewconfigModel();

                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);

                            // String walletOwnerCode = jsonObject3.getString("walletOwnerCode");


                            if (jsonObject3.optString("chnTypeCode").equalsIgnoreCase("100002")) {


                                if (jsonObject3.has("channelTypeName")) {
                                    String channelTypeName = jsonObject3.getString("channelTypeName");
                                    overDraftModalviewconfig.setChannelTypeName(channelTypeName);
                                }

                                if (jsonObject3.has("ewalletServicesName")) {
                                    String ewalletServicesName = jsonObject3.getString("ewalletServicesName");
                                    overDraftModalviewconfig.setEwalletServicesName(ewalletServicesName);
                                }


                                if (jsonObject3.has("servicesCategoryName")) {
                                    String servicesCategoryName = jsonObject3.getString("servicesCategoryName");
                                    overDraftModalviewconfig.setServicesCategoryName(servicesCategoryName);
                                }

                                if (jsonObject3.has("servicesProviderName")) {
                                    String servicesProviderName = jsonObject3.getString("servicesProviderName");
                                    overDraftModalviewconfig.setServicesProviderName(servicesProviderName);
                                }

                                if (jsonObject3.has("status")) {
                                    String status = jsonObject3.getString("status");
                                    overDraftModalviewconfig.setStatus(status);
                                }

                                arrayList_overdraftviewconfig.add(overDraftModalviewconfig);



                            }

                        }

                        if(arrayList_overdraftviewconfig.size()>0) {
                            recyclerView.setLayoutManager(new LinearLayoutManager(OverdraftLimitViewConfigServices.this));

                            OverDraftviewconfigAdapterRecycle adpter = new OverDraftviewconfigAdapterRecycle(OverdraftLimitViewConfigServices.this, arrayList_overdraftviewconfig);
                            recyclerView.setAdapter(adpter);
                        }else{
                            MyApplication.showToast(OverdraftLimitViewConfigServices.this,"No Data AVailable");
                            finish();
                        }


                    } else {
                        Toast.makeText(OverdraftLimitViewConfigServices.this, resultDescription, Toast.LENGTH_LONG).show();

                        recyclerView.setVisibility(View.GONE);

                        //  finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(OverdraftLimitViewConfigServices.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(OverdraftLimitViewConfigServices.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }






}