package com.agent.cashmoovui.servicecharge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.adapter.FeeDetailsAdapter;
import com.agent.cashmoovui.model.FeeDetailModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ServiceChargeDetails extends AppCompatActivity implements View.OnClickListener {
    public static ServiceChargeDetails servicechargedetailsC;
    // ImageView imgBack;
    Button btnClose;
    public static TextView tvName;
    String checkIntent;
    ArrayList<FeeDetailModel>feeDetailModelArrayList= new ArrayList<>();
    RecyclerView rvFeeDetail;


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
            tvName.setText(checkIntent);
        }

        if(checkIntent.equalsIgnoreCase("Subscriber")){
            feeDetailModelArrayList.clear();
            if (ServiceCharge.jsonObjectTestMain != null) {
                JSONArray FeeListArr = ServiceCharge.jsonObjectTestMain.optJSONArray("data");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("child");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if(childData.optString("serviceCategoryCode").equalsIgnoreCase("100024")){
                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        childData.optString("minValue")+"  -  "+
                                                childData.optString("maxValue"),
                                        childData.optString("percentFeeValue")
                                ));
                            }else{
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        childData.optString("minValue")+"  -  "+
                                                childData.optString("maxValue"),
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

        if(checkIntent.equalsIgnoreCase("Non Subscriber")){
            feeDetailModelArrayList.clear();
            if (ServiceCharge.jsonObjectTestMain != null) {
                JSONArray FeeListArr = ServiceCharge.jsonObjectTestMain.optJSONArray("data");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("child");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if(childData.optString("serviceCategoryCode").equalsIgnoreCase("NONSUB")){
                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        childData.optString("minValue")+"  -  "+
                                                childData.optString("maxValue"),
                                        childData.optString("percentFeeValue")
                                ));
                            }else{
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        childData.optString("minValue")+"  -  "+
                                                childData.optString("maxValue"),
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

        if(checkIntent.equalsIgnoreCase("International")){
            feeDetailModelArrayList.clear();
            if (ServiceCharge.jsonObjectTestMain != null) {
                JSONArray FeeListArr = ServiceCharge.jsonObjectTestMain.optJSONArray("data");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("child");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if(childData.optString("serviceCategoryCode").equalsIgnoreCase("INTREM")){
                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        childData.optString("minValue")+"  -  "+
                                                childData.optString("maxValue"),
                                        childData.optString("percentFeeValue")
                                ));
                            }else{
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        childData.optString("minValue")+"  -  "+
                                                childData.optString("maxValue"),
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

        if(checkIntent.equalsIgnoreCase("Airtime Purchase")){
            feeDetailModelArrayList.clear();
            if (ServiceCharge.jsonObjectTestMain != null) {
                JSONArray FeeListArr = ServiceCharge.jsonObjectTestMain.optJSONArray("data");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("child");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if(childData.optString("serviceCategoryCode").equalsIgnoreCase("100021")){
                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        childData.optString("minValue")+"  -  "+
                                                childData.optString("maxValue"),
                                        childData.optString("percentFeeValue")
                                ));
                            }else{
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        childData.optString("minValue")+"  -  "+
                                                childData.optString("maxValue"),
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

        if(checkIntent.equalsIgnoreCase("Bill Payment")){
            feeDetailModelArrayList.clear();
            if (ServiceCharge.jsonObjectTestMain != null) {
                JSONArray FeeListArr = ServiceCharge.jsonObjectTestMain.optJSONArray("data");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("child");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if(childData.optString("serviceCategoryCode").equalsIgnoreCase("100028")){
                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        childData.optString("minValue")+"  -  "+
                                                childData.optString("maxValue"),
                                        childData.optString("percentFeeValue")
                                ));
                            }else{
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        childData.optString("minValue")+"  -  "+
                                                childData.optString("maxValue"),
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

        if(checkIntent.equalsIgnoreCase("Pay")){
            feeDetailModelArrayList.clear();
            if (ServiceCharge.jsonObjectTestMain != null) {
                JSONArray FeeListArr = ServiceCharge.jsonObjectTestMain.optJSONArray("data");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("child");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if(childData.optString("serviceCategoryCode").equalsIgnoreCase("100057")){
                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        childData.optString("minValue")+"  -  "+
                                                childData.optString("maxValue"),
                                        childData.optString("percentFeeValue")
                                ));
                            }else{
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        childData.optString("minValue")+"  -  "+
                                                childData.optString("maxValue"),
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

        if(checkIntent.equalsIgnoreCase("Cash Withdrawal")){
            feeDetailModelArrayList.clear();
            if (ServiceCharge.jsonObjectTestMain != null) {
                JSONArray FeeListArr = ServiceCharge.jsonObjectTestMain.optJSONArray("data");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("child");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if(childData.optString("serviceCategoryCode").equalsIgnoreCase("CSHPIC")){
                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        childData.optString("minValue")+"  -  "+
                                                childData.optString("maxValue"),
                                        childData.optString("percentFeeValue")
                                ));
                            }else{
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        childData.optString("minValue")+"  -  "+
                                                childData.optString("maxValue"),
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

        if(checkIntent.equalsIgnoreCase("Receive Remittance")){
            feeDetailModelArrayList.clear();
            if (ServiceCharge.jsonObjectTestMain != null) {
                JSONArray FeeListArr = ServiceCharge.jsonObjectTestMain.optJSONArray("data");
                for (int i = 0; i < FeeListArr.length(); i++) {
                    JSONObject feeData = FeeListArr.optJSONObject(i);

                    JSONArray ChildListArr = feeData.optJSONArray("child");
                    for (int j = 0; j < ChildListArr.length(); j++) {
                        JSONObject childData = ChildListArr.optJSONObject(j);

                        if(childData.optString("serviceCategoryCode").equalsIgnoreCase("REMON")){
                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        childData.optString("minValue")+"  -  "+
                                                childData.optString("maxValue"),
                                        childData.optString("percentFeeValue")
                                ));
                            }else{
                                feeDetailModelArrayList.add(new FeeDetailModel(
                                        childData.optString("minValue")+"  -  "+
                                                childData.optString("maxValue"),
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


}


