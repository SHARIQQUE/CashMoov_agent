package com.agent.cashmoovui.payments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.activity.OtherOption;
import com.agent.cashmoovui.adapter.ProductAdapter;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.listeners.ProductListeners;
import com.agent.cashmoovui.model.ProductMasterModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class PaymentsProduct extends AppCompatActivity implements ProductListeners {
    public static PaymentsProduct paymentsproductC;
    ImageView imgBack, imgHome;
    RecyclerView rvProduct;
    private ArrayList<ProductMasterModel> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments_product);
        paymentsproductC = this;
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
                MyApplication.hideKeyboard(paymentsproductC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(paymentsproductC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    private void getIds() {
        rvProduct = findViewById(R.id.rvProduct);
        callApiProductProvider();

    }

    public static JSONObject productCategory = new JSONObject();

    private void callApiProductProvider() {

        try {
            MyApplication.showloader(paymentsproductC,getString(R.string.pleasewait));
            API.GET("ewallet/api/v1/productMaster/allByCriteria?operatorCode="+ Payments.operatorCode+"&status=Y",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                productList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    productCategory = jsonObject;
                                    JSONArray walletOwnerListArr = productCategory.optJSONArray("productMasterList");
                                    if(walletOwnerListArr!=null&& walletOwnerListArr.length()>0) {
                                        for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                            JSONObject data = walletOwnerListArr.optJSONObject(i);
                                            productList.add(new ProductMasterModel(
                                                    data.optInt("id"),
                                                    data.optString("code"),
                                                    data.optString("creationDate"),
                                                    data.optString("operatorCode"),
                                                    data.optString("operatorName"),
                                                    data.optString("productName"),
                                                    data.optString("serviceCategoryCode"),
                                                    data.optString("serviceCategoryName"),
                                                    data.optString("state"),
                                                    data.optString("status")

                                            ));

                                        }

                                        setData(productList);
                                    }

                                } else {
                                    MyApplication.showToast(paymentsproductC,jsonObject.optString("resultDescription", "N/A"));
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


    private void setData(List<ProductMasterModel> productList){
        ProductAdapter productAdapter = new ProductAdapter(paymentsproductC,productList);
        rvProduct.setHasFixedSize(true);
        rvProduct.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        rvProduct.setAdapter(productAdapter);

    }

    public static String productCode,operatorCode;


    @Override
    public void onProductListItemClick(String code, String opCode) {
        productCode = code;
        operatorCode = opCode;
        Intent intent = new Intent(paymentsproductC, PaymentPlanList.class);
        startActivity(intent);
    }
}
