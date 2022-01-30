package com.agent.cashmoovui.payments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.adapter.ProductAdapter;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.listeners.ProductListeners;
import com.agent.cashmoovui.model.ProductModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class PaymentsProduct extends AppCompatActivity implements ProductListeners {
    public static PaymentsProduct paymentsproductC;
    ImageView imgBack, imgHome;
    RecyclerView rvProduct;
    private ArrayList<ProductModel> productList = new ArrayList<>();

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

    }


    private void getIds() {
        rvProduct = findViewById(R.id.rvProduct);
        callApiProductProvider();

    }

    public static JSONObject productCategory = new JSONObject();

    private void callApiProductProvider() {

        try {
            MyApplication.showloader(paymentsproductC,"Please Wait...");
            API.GET("ewallet/api/v1/product/allByCriteria?serviceCategoryCode="+
                            Payments.serviceCategory.optJSONArray("operatorList").optJSONObject(0).optString("serviceCategoryCode")
                            +"&operatorCode="+Payments.operatorCode,
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
                                                    data.optString("name"),
                                                    data.optString("operatorCode"),
                                                    data.optString("operatorName"),
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


    private void setData(List<ProductModel> productList){
        ProductAdapter productAdapter = new ProductAdapter(paymentsproductC,productList);
        rvProduct.setHasFixedSize(true);
        rvProduct.setLayoutManager(new GridLayoutManager(this,3));
        rvProduct.setAdapter(productAdapter);

    }

    public static String productCode,productName;

    @Override
    public void onProductListItemClick(String code, String name) {
        productCode = code;
        productName = name;
        Intent intent = new Intent(paymentsproductC, PaymentDetails.class);
        startActivity(intent);
    }
}
