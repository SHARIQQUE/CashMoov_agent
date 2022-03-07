package com.agent.cashmoovui.payments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.activity.OtherOption;
import com.agent.cashmoovui.adapter.PlanListAdapter;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.listeners.PlanListeners;
import com.agent.cashmoovui.model.ProductModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class PaymentPlanList extends AppCompatActivity implements PlanListeners {
    public static PaymentPlanList paymentplanlistC;
    ImageView imgBack, imgHome;
    RecyclerView rvPlanList;
    private ArrayList<ProductModel> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_plan_list);
        paymentplanlistC = this;
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
                MyApplication.hideKeyboard(paymentplanlistC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(paymentplanlistC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    private void getIds() {
        rvPlanList = findViewById(R.id.rvPlanList);
        callApiPlanList();

    }

    public static JSONObject productCategory = new JSONObject();

    private void callApiPlanList() {
        try {
            MyApplication.showloader(paymentplanlistC,"Please Wait...");
            API.GET("ewallet/api/v1/product/allByCriteria?operatorCode="+PaymentsProduct.operatorCode+"&serviceCategoryCode=100028&productMasterCode="+
                            PaymentsProduct.productCode+"&status=Y",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                           MyApplication.hideLoader();

                            if (jsonObject != null) {
                                productList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    productCategory = jsonObject;
                                    JSONArray walletOwnerListArr = productCategory.optJSONArray("productList");
                                    if(walletOwnerListArr!=null&& walletOwnerListArr.length()>0) {
                                        for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                            JSONObject data = walletOwnerListArr.optJSONObject(i);
                                            productList.add(new ProductModel(
                                                    data.optInt("id"),
                                                    data.optString("code"),
                                                    data.optString("creationDate"),
                                                    data.optString("description"),
                                                    data.optInt("maxValue"),
                                                    data.optInt("minValue"),
                                                    data.optInt("value"),
                                                    data.optString("modificationDate"),
                                                    data.optString("name"),
                                                    data.optString("operatorCode"),
                                                    data.optString("operatorName"),
                                                    data.optString("productMasterCode"),
                                                    data.optString("productTypeCode"),
                                                    data.optString("productTypeName"),
                                                    data.optString("serviceCategoryCode"),
                                                    data.optString("serviceCategoryName"),
                                                    data.optString("state"),
                                                    data.optString("status"),
                                                    data.optString("vendorProductCode")

                                            ));

                                        }

                                        setData(productList);
                                    }

                                } else {
                                    MyApplication.showToast(paymentplanlistC,jsonObject.optString("resultDescription", "N/A"));
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

    private void setData(List<ProductModel> productList){
        PlanListAdapter planListAdapter = new PlanListAdapter(paymentplanlistC,productList);
        rvPlanList.setHasFixedSize(true);
        rvPlanList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        rvPlanList.setAdapter(planListAdapter);

    }

    public static String productCode,productTypeCode;
    public static int productValue;


    @Override
    public void onPlanListItemClick(String code, String typeCode, int value) {
        productCode = code;
        productTypeCode = typeCode;
        productValue = value;
        Intent intent = new Intent(paymentplanlistC, PaymentDetails.class);
        startActivity(intent);
    }
}
