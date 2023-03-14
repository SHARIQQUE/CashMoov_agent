package com.agent.cashmoovui.servicecharge;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agent.cashmoovui.LogoutAppCompactActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.adapter.FeeDetailsAdapter;
import com.agent.cashmoovui.model.FeeDetailModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ServiceChargeDetails extends LogoutAppCompactActivity implements View.OnClickListener {
    public static ServiceChargeDetails servicechargedetailsC;
    // ImageView imgBack;
    Button btnClose;
    public static TextView tvName;
    String checkIntent,checkProductCodeIntent;
    ArrayList<FeeDetailModel>feeDetailModelArrayList= new ArrayList<>();
    RecyclerView rvFeeDetail;
    private long mLastClickTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_charge_details);
        servicechargedetailsC=this;
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
//        imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSupportNavigateUp();
//            }
//        });
//    }

    private void getIds() {
        rvFeeDetail = findViewById(R.id.rvFeeDetail);
        tvName = findViewById(R.id.tvName);
        btnClose = findViewById(R.id.btnClose);

        if (getIntent().getExtras() != null) {
            checkIntent = (getIntent().getStringExtra("FEEINTENT"));
                      System.out.println("get send"+checkIntent);


            if(checkIntent.equalsIgnoreCase(getString(R.string.send_remittance))){
                tvName.setText(getString(R.string.send));
            }else if(checkIntent.equalsIgnoreCase(getString(R.string.receive_remittance))){

                tvName.setText(getString(R.string.receive));


            }else{
                tvName.setText(checkIntent);

            }
            checkProductCodeIntent = (getIntent().getStringExtra("PRODUCTCODE"));



          //  if( MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")){

        }

        if(checkIntent.equalsIgnoreCase(getString(R.string.send_remittance))){
            feeDetailModelArrayList.clear();
            if (ServiceCharge.jsonObjectTestMain != null) {
                JSONArray FeeListArr = ServiceCharge.jsonObjectTestMain.optJSONArray("data");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("child");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if(childData.optString("serviceCategoryCode").equalsIgnoreCase("100001")){
                            if (childData.optString("calculationTypeName").equalsIgnoreCase(getString(R.string.Percentage))) {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("percentFeeValue")
                                ));
                            }else{
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("fixedFeeValue")
                                ));
                            }
                        }
                    }

                }

                setData(feeDetailModelArrayList);
                //System.out.println("FeeDetailLlist---"+feeDetailModelArrayList.toString());

            }
        }

        if(checkIntent.equalsIgnoreCase(getString(R.string.receive_remittance))){
            feeDetailModelArrayList.clear();
            if (ServiceCharge.jsonObjectTestMain != null) {
                JSONArray FeeListArr = ServiceCharge.jsonObjectTestMain.optJSONArray("data");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("child");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if(childData.optString("serviceCategoryCode").equalsIgnoreCase("100018")){
                            if (childData.optString("calculationTypeName").equalsIgnoreCase(getString(R.string.Percentage))) {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("percentFeeValue")
                                ));
                            }else{
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("fixedFeeValue")
                                ));
                            }
                        }
                    }

                }

                setData(feeDetailModelArrayList);
                //System.out.println("FeeDetailLlist---"+feeDetailModelArrayList.toString());

            }
        }

        if(checkIntent.equalsIgnoreCase(getString(R.string.cash_to_wallet))){
            feeDetailModelArrayList.clear();
            if (ServiceCharge.jsonObjectTestMain != null) {
                JSONArray FeeListArr = ServiceCharge.jsonObjectTestMain.optJSONArray("data");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("child");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if(childData.optString("serviceCategoryCode").equalsIgnoreCase("100061")){
                            if (childData.optString("calculationTypeName").equalsIgnoreCase(getString(R.string.Percentage))) {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("percentFeeValue")
                                ));
                            }else{
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("fixedFeeValue")
                                ));
                            }
                        }
                    }

                }

                setData(feeDetailModelArrayList);
                //System.out.println("FeeDetailLlist---"+feeDetailModelArrayList.toString());

            }
        }

        if(checkIntent.equalsIgnoreCase(getString(R.string.airtime_purchase))){
            feeDetailModelArrayList.clear();
            if (AirtimeFeeActivity.mainJsonObject != null) {
                JSONArray FeeListArr = AirtimeFeeActivity.mainJsonObject.optJSONArray("walletOwnerTemplateList");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("feeTemplateList");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if (childData.optString("calculationTypeCode").equalsIgnoreCase("100002")) {
                            feeDetailModelArrayList.add(new FeeDetailModel(
                                            MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue"))) + "  -  " +
                                                    MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))) +
                                            "   (" + childData.optString("productName").replaceAll("Recharge ", "") + ")",
                                    childData.optString("percentFeeValue")+"%"
                            ));
                        } else {
                            feeDetailModelArrayList.add(new FeeDetailModel(
                                    MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue"))) + "  -  " +
                                            MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))) +
                                            "   (" + childData.optString("productName").replaceAll("Recharge ", "") + ")",
                                    childData.optString("fixedFeeValue")
                            ));
                        }
                    }

                }

                setData(feeDetailModelArrayList);
                //System.out.println("FeeDetailLlist---"+feeDetailModelArrayList.toString());

            }
        }


        if(checkIntent.equalsIgnoreCase(getString(R.string.bill_payment))){
            feeDetailModelArrayList.clear();
            if (BillPayFeeActivity.mainJsonObject != null) {
                JSONArray FeeListArr = BillPayFeeActivity.mainJsonObject.optJSONArray("walletOwnerTemplateList");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("feeTemplateList");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if (childData.optString("calculationTypeName").equalsIgnoreCase(getString(R.string.Percentage))) {
                            feeDetailModelArrayList.add(new FeeDetailModel(
                                    MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue"))) + "  -  " +
                                            MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))) +
                                            "   (" + childData.optString("productName").replaceAll("Recharge ", "") + ")",
                                    childData.optString("percentFeeValue")+"%"
                            ));
                        } else {
                            feeDetailModelArrayList.add(new FeeDetailModel(
                                    MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue"))) + "  -  " +
                                            MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))) +
                                            "   (" + childData.optString("productName").replaceAll("Recharge ", "") + ")",
                                    childData.optString("fixedFeeValue")
                            ));
                        }
                    }

                }

                setData(feeDetailModelArrayList);
                //System.out.println("FeeDetailLlist---"+feeDetailModelArrayList.toString());

            }
        }


        if(checkIntent.equalsIgnoreCase(getString(R.string.sell_Float))){
            feeDetailModelArrayList.clear();
            if (ServiceCharge.jsonObjectTestMain != null) {
                JSONArray FeeListArr = ServiceCharge.jsonObjectTestMain.optJSONArray("data");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("child");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if(childData.optString("serviceCategoryCode").equalsIgnoreCase("100016")){
                            if (childData.optString("calculationTypeName").equalsIgnoreCase(getString(R.string.Percentage))) {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("percentFeeValue")
                                ));
                            }else{
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("fixedFeeValue")
                                ));
                            }
                        }
                    }

                }

                setData(feeDetailModelArrayList);
                //System.out.println("FeeDetailLlist---"+feeDetailModelArrayList.toString());

            }
        }

        if(checkIntent.equalsIgnoreCase(getString(R.string.international_transfer))){
            feeDetailModelArrayList.clear();
            if (ServiceCharge.jsonObjectTestMain != null) {
                JSONArray FeeListArr = ServiceCharge.jsonObjectTestMain.optJSONArray("data");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("child");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if(childData.optString("serviceCategoryCode").equalsIgnoreCase("TRTWLT")){
                            if (childData.optString("calculationTypeName").equalsIgnoreCase(getString(R.string.Percentage))) {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("percentFeeValue")
                                ));
                            }else{
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("fixedFeeValue")
                                ));
                            }
                        }
                    }

                }

                setData(feeDetailModelArrayList);
                //System.out.println("FeeDetailLlist---"+feeDetailModelArrayList.toString());

            }
        }
        if(checkIntent.equalsIgnoreCase(getString(R.string.commision_Transfer))){
            feeDetailModelArrayList.clear();
            if (ServiceCharge.jsonObjectTestMain != null) {
                JSONArray FeeListArr = ServiceCharge.jsonObjectTestMain.optJSONArray("data");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("child");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if(childData.optString("serviceCategoryCode").equalsIgnoreCase("COMTRF")){
                            if (childData.optString("calculationTypeName").equalsIgnoreCase(getString(R.string.Percentage))) {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("percentFeeValue")
                                ));
                            }else{
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("fixedFeeValue")
                                ));
                            }
                        }
                    }

                }

                setData(feeDetailModelArrayList);
                //System.out.println("FeeDetailLlist---"+feeDetailModelArrayList.toString());

            }
        }

        if(checkIntent.equalsIgnoreCase(getString(R.string.transfer_float))){
            feeDetailModelArrayList.clear();
            if (ServiceCharge.jsonObjectTestMain != null) {
                JSONArray FeeListArr = ServiceCharge.jsonObjectTestMain.optJSONArray("data");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("child");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if(childData.optString("serviceCategoryCode").equalsIgnoreCase("100017")){
                            if (childData.optString("calculationTypeName").equalsIgnoreCase(getString(R.string.Percentage))) {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("percentFeeValue")
                                ));
                            }else{
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("fixedFeeValue")
                                ));
                            }
                        }
                    }

                }

                setData(feeDetailModelArrayList);
                //System.out.println("FeeDetailLlist---"+feeDetailModelArrayList.toString());

            }
        }

        if(checkIntent.equalsIgnoreCase(getString(R.string.cash_In))){
            feeDetailModelArrayList.clear();
            if (ServiceCharge.jsonObjectTestMain != null) {
                JSONArray FeeListArr = ServiceCharge.jsonObjectTestMain.optJSONArray("data");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("child");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if(childData.optString("serviceCategoryCode").equalsIgnoreCase("100011")){
                            if (childData.optString("calculationTypeName").equalsIgnoreCase(getString(R.string.Percentage))) {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("percentFeeValue")
                                ));
                            }else{
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("fixedFeeValue")
                                ));
                            }
                        }
                    }

                }

                setData(feeDetailModelArrayList);
                //System.out.println("FeeDetailLlist---"+feeDetailModelArrayList.toString());

            }
        }

        if(checkIntent.equalsIgnoreCase(getString(R.string.cash_Out))){
            feeDetailModelArrayList.clear();
            if (ServiceCharge.jsonObjectTestMain != null) {
                JSONArray FeeListArr = ServiceCharge.jsonObjectTestMain.optJSONArray("data");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("child");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if(childData.optString("serviceCategoryCode").equalsIgnoreCase("100012")){
                            if (childData.optString("calculationTypeName").equalsIgnoreCase(getString(R.string.Percentage))) {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("percentFeeValue")
                                ));
                            }else{
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        MyApplication.addDecimal(String.valueOf(childData.optDouble("minValue")))+"  -  "+
                                                MyApplication.addDecimal(String.valueOf(childData.optDouble("maxValue"))),
                                        childData.optString("fixedFeeValue")
                                ));
                            }
                        }
                    }

                }

                setData(feeDetailModelArrayList);
                //System.out.println("FeeDetailLlist---"+feeDetailModelArrayList.toString());

            }
        }


        setOnCLickListener();

    }

    private void setOnCLickListener() {
        btnClose.setOnClickListener(servicechargedetailsC);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btnClose:
                finish();
                break;
        }
    }

    private void setData(List<FeeDetailModel> feeDetailModelArrayList){


        FeeDetailsAdapter feeDetailsAdapter = new FeeDetailsAdapter(servicechargedetailsC,feeDetailModelArrayList);
        rvFeeDetail.setHasFixedSize(true);
        rvFeeDetail.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        rvFeeDetail.setAdapter(feeDetailsAdapter);
    }

    public String callf(Double data){
        return String.format("%.2f",data);
    }


}


