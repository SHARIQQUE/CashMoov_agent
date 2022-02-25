package com.agent.cashmoovui.servicecharge;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServiceCharge extends AppCompatActivity implements View.OnClickListener {
    public static ServiceCharge servicechargeC;
    ImageView imgBack,imgHome;
    JSONObject feeData;
    public static JSONObject jsonObjectTestMain=null;
    LinearLayout linRemittance,linCreditPurchase,linBillPay,linMoneyTransfer,linCashIn,linCashOut;
    TextView tvRemittance,tvFeeRemittance,tvCreditPurchase,tvFeeCreditPurchase,tvBillPayment,tvFeeBillPayment,
            tvMoneyTransfer,tvFeeMoneyTransfer,tvCashIn,tvFeeCashIn,tvCashOut,tvFeeCashOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_charge);
        servicechargeC=this;
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
        linRemittance = findViewById(R.id.linRemittance);
        linCreditPurchase = findViewById(R.id.linCreditPurchase);
        linBillPay = findViewById(R.id.linBillPay);
        linMoneyTransfer = findViewById(R.id.linMoneyTransfer);
        linCashIn = findViewById(R.id.linCashIn);
        linCashOut = findViewById(R.id.linCashOut);


        tvRemittance = findViewById(R.id.tvRemittance);
        tvFeeRemittance = findViewById(R.id.tvFeeRemittance);
        tvCreditPurchase = findViewById(R.id.tvCreditPurchase);
        tvFeeCreditPurchase = findViewById(R.id.tvFeeCreditPurchase);
        tvBillPayment = findViewById(R.id.tvBillPayment);
        tvFeeBillPayment = findViewById(R.id.tvFeeBillPayment);
        tvMoneyTransfer = findViewById(R.id.tvMoneyTransfer);
        tvFeeMoneyTransfer = findViewById(R.id.tvFeeMoneyTransfer);
        tvCashIn = findViewById(R.id.tvCashIn);
        tvFeeCashIn = findViewById(R.id.tvFeeCashIn);
        tvCashOut = findViewById(R.id.tvCashOut);
        tvFeeCashOut = findViewById(R.id.tvFeeCashOut);


        setOnCLickListener();

    }

    private void setOnCLickListener() {
        linRemittance.setOnClickListener(servicechargeC);
        linCreditPurchase.setOnClickListener(servicechargeC);
        linBillPay.setOnClickListener(servicechargeC);
        linMoneyTransfer.setOnClickListener(servicechargeC);
        linCashIn.setOnClickListener(servicechargeC);
        linCashOut.setOnClickListener(servicechargeC);
    }

    @Override
    protected void onStart() {
        super.onStart();
        callApiFeeList();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.linRemittance:
                if(tvFeeRemittance.getText().toString().equalsIgnoreCase(getString(R.string.free_service))){
                    MyApplication.showToast(servicechargeC,getString(R.string.range_value_not_available));
                }else{
                    showFeePopup(getString(R.string.remittance));
                }
                break;
            case R.id.linCreditPurchase:
                if(tvFeeCreditPurchase.getText().toString().equalsIgnoreCase(getString(R.string.free_service))){
                    MyApplication.showToast(servicechargeC,getString(R.string.range_value_not_available));
                }else{
                    showAirtimePurchasePopup(getString(R.string.credit_purchase));
                }
                break;
            case R.id.linBillPay:
                if(tvFeeBillPayment.getText().toString().equalsIgnoreCase(getString(R.string.free_service))){
                    MyApplication.showToast(servicechargeC,getString(R.string.range_value_not_available));
                }else{
                    showBillPayPopup(getString(R.string.bill_payment));
                }
                break;
            case R.id.linMoneyTransfer:
                if(tvFeeMoneyTransfer.getText().toString().equalsIgnoreCase(getString(R.string.free_service))){
                    MyApplication.showToast(servicechargeC,getString(R.string.range_value_not_available));
                }else{
                    showTransferPopup(getString(R.string.money_transfer));
                }
                break;
            case R.id.linCashIn:
                if(tvFeeCashIn.getText().toString().equalsIgnoreCase(getString(R.string.free_service))){
                    MyApplication.showToast(servicechargeC,getString(R.string.range_value_not_available));
                }else{
                    showCashInPopup(getString(R.string.cash_In));
                }
                break;
            case R.id.linCashOut:
                if(tvFeeCashOut.getText().toString().equalsIgnoreCase(getString(R.string.free_service))){
                    MyApplication.showToast(servicechargeC,getString(R.string.range_value_not_available));
                }else{
                    showCashOutPopup(getString(R.string.cash_Out));
                }
                break;

        }
    }

    public void showFeePopup(String serviceName) {
        Dialog feeDialog = new Dialog(servicechargeC);
        feeDialog.setContentView(R.layout.popup_money_transfer);

        Button btnClose;
        TextView tvServiceName, txt1, txt2, txt3, txt1_value, txt2_value, txt3_value;
        tvServiceName = feeDialog.findViewById(R.id.tvServiceName);
        txt1 = feeDialog.findViewById(R.id.txt1);
        txt2 = feeDialog.findViewById(R.id.txt2);
        txt3 = feeDialog.findViewById(R.id.txt3);
        txt1_value = feeDialog.findViewById(R.id.txt1_value);
        txt2_value = feeDialog.findViewById(R.id.txt2_value);
        txt3_value = feeDialog.findViewById(R.id.txt3_value);

        if (jsonObjectTestMain != null) {
            JSONArray FeeListArr = jsonObjectTestMain.optJSONArray("data");
            for (int i = 0; i < FeeListArr.length(); i++) {
                JSONObject feeData = FeeListArr.optJSONObject(i);

                JSONArray ChildListArr = feeData.optJSONArray("child");
                for (int j = 0; j < ChildListArr.length(); j++) {
                    JSONObject childData = ChildListArr.optJSONObject(j);

                    if (feeData.optString("ServiceName").equalsIgnoreCase("Remittance")) {
                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {

                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Send Remittance")) {
                                txt1_value.setText(childData.optString("percentFeeValue"));
                            }
                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Receive Remittance")) {
                                txt2_value.setText(childData.optString("percentFeeValue"));
                            }
                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Cash to Wallet")) {
                                txt3_value.setText(childData.optString("percentFeeValue"));
                            }

                        }
                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Send Remittance")) {
                                txt1_value.setText(childData.optString("fixedFeeValue")+" "+getString(R.string.gnf_fixed));
                            }
                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Receive Remittance")) {
                                txt2_value.setText(childData.optString("fixedFeeValue")+" "+getString(R.string.gnf_fixed));
                            }
                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Cash to Wallet")) {
                                txt3_value.setText(childData.optString("fixedFeeValue")+" "+getString(R.string.gnf_fixed));
                            }

                        }
                    }
                }
            }
        }

        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(servicechargeC,ServiceChargeDetails.class);
                i.putExtra("FEEINTENT","Send Remittance");
                startActivity(i);
                feeDialog.dismiss();
            }
        });

        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(servicechargeC,ServiceChargeDetails.class);
                i.putExtra("FEEINTENT","Receive Remittance");
                startActivity(i);
                feeDialog.dismiss();
            }
        });

        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(servicechargeC,ServiceChargeDetails.class);
                i.putExtra("FEEINTENT","Cash to Wallet");
                startActivity(i);
                feeDialog.dismiss();
            }
        });


        btnClose = feeDialog.findViewById(R.id.btnClose);
        btnClose.setText(getString(R.string.close));
        tvServiceName.setText(serviceName);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feeDialog.dismiss();
            }
        });
        //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        feeDialog.show();
    }


    public void showAirtimePurchasePopup(String serviceName) {
        Dialog feeDialog = new Dialog(servicechargeC);
        feeDialog.setContentView(R.layout.popup_airtime_purchase);

        Button btnClose;
        TextView tvServiceName, txt1, txt2, txt3, txt4, txt1_value, txt2_value, txt3_value, txt4_value;
        tvServiceName = feeDialog.findViewById(R.id.tvServiceName);
        tvServiceName.setText(serviceName);
        txt1 = feeDialog.findViewById(R.id.txt1);
        txt2 = feeDialog.findViewById(R.id.txt2);
        txt3 = feeDialog.findViewById(R.id.txt3);
        txt4 = feeDialog.findViewById(R.id.txt4);
//        txt1.setText(getString(R.string.recharge_mobile));
//        txt1.setVisibility(View.VISIBLE);
        txt1_value = feeDialog.findViewById(R.id.txt1_value);
        txt2_value = feeDialog.findViewById(R.id.txt2_value);
        txt3_value = feeDialog.findViewById(R.id.txt3_value);
        txt4_value = feeDialog.findViewById(R.id.txt4_value);

        LinearLayout lin1=feeDialog.findViewById(R.id.lin1);
        LinearLayout lin2=feeDialog.findViewById(R.id.lin2);
        LinearLayout lin3=feeDialog.findViewById(R.id.lin3);
        LinearLayout lin4=feeDialog.findViewById(R.id.lin4);
        lin1.setVisibility(View.GONE);


        if (jsonObjectTestMain != null) {
            JSONArray FeeListArr = jsonObjectTestMain.optJSONArray("data");
            for (int i = 0; i < FeeListArr.length(); i++) {
                JSONObject feeData = FeeListArr.optJSONObject(i);

                JSONArray ChildListArr = feeData.optJSONArray("child");
                for (int j = 0; j < ChildListArr.length(); j++) {
                    JSONObject childData = ChildListArr.optJSONObject(j);

                    if (feeData.optString("ServiceName").equalsIgnoreCase("Airtime Purchase")) {
                       /* if (childData.optString("serviceCategoryCode").equalsIgnoreCase("100021")) {
                            System.out.println("productName==="+childData.optString("productName"));
                            TextView textView1 = new TextView(this);
                            textView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                            textView1.setText(childData.optString("productName"));
                            textView1.setPadding(5,5,5,5);
                            textView1.setTextColor(Color.parseColor("#000000"));
                            textView1.setCompoundDrawablePadding(20);
                            textView1.setTextSize(16);
                            textView1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_circle_outline_24,0,0,0);
                            textView1.setTag("" + j);
                            textView1.setClickable(true);//make your TextView Clickable
                            textView1.setOnClickListener(btnClickListener);

                            linearLayout.addView(textView1);
                        }*/

                        if (childData.optString("productCode").equalsIgnoreCase("100029")) {
                            lin1.setVisibility(View.VISIBLE);
                            txt1.setText(childData.optString("productName"));
                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {
                                if (childData.optString("serviceCategoryName").equalsIgnoreCase("Mobile Prepaid")) {
                                    txt1_value.setText(childData.optString("percentFeeValue"));
                                }

                            }
                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
                                if (childData.optString("serviceCategoryName").equalsIgnoreCase("Mobile Prepaid")) {
                                    txt1_value.setText(childData.optString("fixedFeeValue") + " " + getString(R.string.gnf_fixed));
                                }

                            }
                        }

                        if (childData.optString("productCode").equalsIgnoreCase("100030")) {
                            lin2.setVisibility(View.VISIBLE);
                            txt2.setText(childData.optString("productName"));
                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {
                                if (childData.optString("serviceCategoryName").equalsIgnoreCase("Mobile Prepaid")) {
                                    txt2_value.setText(childData.optString("percentFeeValue"));
                                }

                            }
                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
                                if (childData.optString("serviceCategoryName").equalsIgnoreCase("Mobile Prepaid")) {
                                    txt2_value.setText(childData.optString("fixedFeeValue") + " " + getString(R.string.gnf_fixed));
                                }

                            }
                        }

                        if (childData.optString("productCode").equalsIgnoreCase("100031")) {
                            lin3.setVisibility(View.VISIBLE);
                            txt3.setText(childData.optString("productName"));
                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {
                                if (childData.optString("serviceCategoryName").equalsIgnoreCase("Mobile Prepaid")) {
                                    txt3_value.setText(childData.optString("percentFeeValue"));
                                }

                            }
                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
                                if (childData.optString("serviceCategoryName").equalsIgnoreCase("Mobile Prepaid")) {
                                    txt3_value.setText(childData.optString("fixedFeeValue") + " " + getString(R.string.gnf_fixed));
                                }

                            }
                        }

                        if (childData.optString("productCode").equalsIgnoreCase("ALLPRO")) {
                            lin4.setVisibility(View.VISIBLE);
                            txt4.setText(childData.optString("productName"));
                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {
                                if (childData.optString("serviceCategoryName").equalsIgnoreCase("Mobile Prepaid")) {
                                    txt4_value.setText(childData.optString("percentFeeValue"));
                                }

                            }
                            if (childData.optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
                                if (childData.optString("serviceCategoryName").equalsIgnoreCase("Mobile Prepaid")) {
                                    txt4_value.setText(childData.optString("fixedFeeValue") + " " + getString(R.string.gnf_fixed));
                                }

                            }
                        }




                    }
                }
            }
        }


        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(servicechargeC,ServiceChargeDetails.class);
                i.putExtra("FEEINTENT","Airtime Purchase");
                i.putExtra("PRODUCTCODE","100029");
                startActivity(i);
                feeDialog.dismiss();
            }
        });
        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(servicechargeC,ServiceChargeDetails.class);
                i.putExtra("FEEINTENT","Airtime Purchase");
                i.putExtra("PRODUCTCODE","100030");
                startActivity(i);
                feeDialog.dismiss();
            }
        });
        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(servicechargeC,ServiceChargeDetails.class);
                i.putExtra("FEEINTENT","Airtime Purchase");
                i.putExtra("PRODUCTCODE","100031");
                startActivity(i);
                feeDialog.dismiss();
            }
        });
        txt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(servicechargeC,ServiceChargeDetails.class);
                i.putExtra("FEEINTENT","Airtime Purchase");
                i.putExtra("PRODUCTCODE","ALLPRO");
                startActivity(i);
                feeDialog.dismiss();
            }
        });


        btnClose = feeDialog.findViewById(R.id.btnClose);
        btnClose.setText(getString(R.string.close));
        tvServiceName.setText(serviceName);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feeDialog.dismiss();
            }
        });
        //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        feeDialog.show();
    }

    public void showBillPayPopup(String serviceName) {
        Dialog feeDialog = new Dialog(servicechargeC);
        feeDialog.setContentView(R.layout.popup_airtime_purchase);

        Button btnClose;
        TextView tvServiceName, txt1, txt1_value;
        tvServiceName = feeDialog.findViewById(R.id.tvServiceName);
        tvServiceName.setText(serviceName);
        txt1 = feeDialog.findViewById(R.id.txt1);
        txt1.setText(getString(R.string.recharge_tv));
        txt1_value = feeDialog.findViewById(R.id.txt1_value);

        if (jsonObjectTestMain != null) {
            JSONArray FeeListArr = jsonObjectTestMain.optJSONArray("data");
            for (int i = 0; i < FeeListArr.length(); i++) {
                JSONObject feeData = FeeListArr.optJSONObject(i);

                JSONArray ChildListArr = feeData.optJSONArray("child");
                for (int j = 0; j < ChildListArr.length(); j++) {
                    JSONObject childData = ChildListArr.optJSONObject(j);

                    if (feeData.optString("ServiceName").equalsIgnoreCase("Recharge & Payment")) {
                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {

                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("TV")) {
                                txt1_value.setText(childData.optString("percentFeeValue"));
                            }

                        }
                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("TV")) {
                                txt1_value.setText(childData.optString("fixedFeeValue")+" "+getString(R.string.gnf_fixed));
                            }

                        }
                    }
                }
            }
        }

        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(servicechargeC,ServiceChargeDetails.class);
                i.putExtra("FEEINTENT","Bill Payment");
                startActivity(i);
                feeDialog.dismiss();
            }
        });

        btnClose = feeDialog.findViewById(R.id.btnClose);
        btnClose.setText(getString(R.string.close));
        tvServiceName.setText(serviceName);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feeDialog.dismiss();
            }
        });
        //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        feeDialog.show();
    }

    public void showTransferPopup(String serviceName) {
        Dialog feeDialog = new Dialog(servicechargeC);
        feeDialog.setContentView(R.layout.popup_transfer);

        Button btnClose;
        TextView tvServiceName, txt1,txt2,txt1_value,txt2_value;
        tvServiceName = feeDialog.findViewById(R.id.tvServiceName);
        tvServiceName.setText(serviceName);
        txt1 = feeDialog.findViewById(R.id.txt1);
        txt2 = feeDialog.findViewById(R.id.txt2);
        txt1.setText(getString(R.string.sell_float));
        txt2.setText(getString(R.string.transfer_float));
        txt1_value = feeDialog.findViewById(R.id.txt1_value);
        txt2_value = feeDialog.findViewById(R.id.txt2_value);

        if (jsonObjectTestMain != null) {
            JSONArray FeeListArr = jsonObjectTestMain.optJSONArray("data");
            for (int i = 0; i < FeeListArr.length(); i++) {
                JSONObject feeData = FeeListArr.optJSONObject(i);

                JSONArray ChildListArr = feeData.optJSONArray("child");
                for (int j = 0; j < ChildListArr.length(); j++) {
                    JSONObject childData = ChildListArr.optJSONObject(j);

                    if (feeData.optString("ServiceName").equalsIgnoreCase("Money Transfer")) {
                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {

                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Sell Float")) {
                                txt1_value.setText(childData.optString("percentFeeValue"));
                            }
                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Transfer Float")) {
                                txt2_value.setText(childData.optString("percentFeeValue"));
                            }

                        }
                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Sell Float")) {
                                txt1_value.setText(childData.optString("fixedFeeValue")+" "+getString(R.string.gnf_fixed));
                            }
                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Transfer Float")) {
                                txt2_value.setText(childData.optString("percentFeeValue"));
                            }

                        }
                    }
                }
            }
        }

        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(servicechargeC,ServiceChargeDetails.class);
                i.putExtra("FEEINTENT","Sell Float");
                startActivity(i);
                feeDialog.dismiss();
            }
        });

        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(servicechargeC,ServiceChargeDetails.class);
                i.putExtra("FEEINTENT","Transfer Float");
                startActivity(i);
                feeDialog.dismiss();
            }
        });

        btnClose = feeDialog.findViewById(R.id.btnClose);
        btnClose.setText(getString(R.string.close));
        tvServiceName.setText(serviceName);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feeDialog.dismiss();
            }
        });
        //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        feeDialog.show();
    }

    public void showCashInPopup(String serviceName) {
        Dialog feeDialog = new Dialog(servicechargeC);
        feeDialog.setContentView(R.layout.popup_airtime_purchase);

        Button btnClose;
        TextView tvServiceName, txt1, txt1_value;
        tvServiceName = feeDialog.findViewById(R.id.tvServiceName);
        tvServiceName.setText(serviceName);
        txt1 = feeDialog.findViewById(R.id.txt1);
        txt1.setText(getString(R.string.cash_In));
        txt1_value = feeDialog.findViewById(R.id.txt1_value);

        if (jsonObjectTestMain != null) {
            JSONArray FeeListArr = jsonObjectTestMain.optJSONArray("data");
            for (int i = 0; i < FeeListArr.length(); i++) {
                JSONObject feeData = FeeListArr.optJSONObject(i);

                JSONArray ChildListArr = feeData.optJSONArray("child");
                for (int j = 0; j < ChildListArr.length(); j++) {
                    JSONObject childData = ChildListArr.optJSONObject(j);

                    if (feeData.optString("ServiceName").equalsIgnoreCase("Cash")) {
                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {

                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Cash In")) {
                                txt1_value.setText(childData.optString("percentFeeValue"));
                            }

                        }
                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Cash In")) {
                                txt1_value.setText(childData.optString("fixedFeeValue")+" "+getString(R.string.gnf_fixed));
                            }

                        }
                    }
                }
            }
        }

        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(servicechargeC,ServiceChargeDetails.class);
                i.putExtra("FEEINTENT","Cash In");
                startActivity(i);
                feeDialog.dismiss();
            }
        });


        btnClose = feeDialog.findViewById(R.id.btnClose);
        btnClose.setText(getString(R.string.close));
        tvServiceName.setText(serviceName);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feeDialog.dismiss();
            }
        });
        //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        feeDialog.show();
    }

    public void showCashOutPopup(String serviceName) {
        Dialog feeDialog = new Dialog(servicechargeC);
        feeDialog.setContentView(R.layout.popup_airtime_purchase);

        Button btnClose;
        TextView tvServiceName, txt1, txt1_value;
        tvServiceName = feeDialog.findViewById(R.id.tvServiceName);
        tvServiceName.setText(serviceName);
        txt1 = feeDialog.findViewById(R.id.txt1);
        txt1.setText(getString(R.string.cash_Out));
        txt1_value = feeDialog.findViewById(R.id.txt1_value);

        if (jsonObjectTestMain != null) {
            JSONArray FeeListArr = jsonObjectTestMain.optJSONArray("data");
            for (int i = 0; i < FeeListArr.length(); i++) {
                JSONObject feeData = FeeListArr.optJSONObject(i);

                JSONArray ChildListArr = feeData.optJSONArray("child");
                for (int j = 0; j < ChildListArr.length(); j++) {
                    JSONObject childData = ChildListArr.optJSONObject(j);

                    if (feeData.optString("ServiceName").equalsIgnoreCase("Cash")) {
                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Percentage")) {

                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Cash Out")) {
                                txt1_value.setText(childData.optString("percentFeeValue"));
                            }

                        }
                        if (childData.optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
                            if (childData.optString("serviceCategoryName").equalsIgnoreCase("Cash Out")) {
                                txt1_value.setText(childData.optString("fixedFeeValue")+" "+getString(R.string.gnf_fixed));
                            }

                        }
                    }
                }
            }
        }

        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(servicechargeC,ServiceChargeDetails.class);
                i.putExtra("FEEINTENT","Cash Out");
                startActivity(i);
                feeDialog.dismiss();
            }
        });


        btnClose = feeDialog.findViewById(R.id.btnClose);
        btnClose.setText(getString(R.string.close));
        tvServiceName.setText(serviceName);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feeDialog.dismiss();
            }
        });
        //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        feeDialog.show();
    }

    JSONObject payFee;
    JSONObject receiveRemmitanceFee;
    JSONObject cashOutFee;
    JSONObject INTREMFee;
    private void callApiFeeList() {
        try {
            feeData=new JSONObject();
            payFee=new JSONObject();
            receiveRemmitanceFee=new JSONObject();
            cashOutFee=new JSONObject();
            INTREMFee=new JSONObject();
            MyApplication.showloader(servicechargeC,"Please wait!");
            API.GET("ewallet/api/v1/walletOwnerTemplate/walletOwnerCode/fee/"+ MyApplication.getSaveString("walletOwnerCode", servicechargeC),
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            System.out.println("Fee response======="+jsonObject.toString());
                            if (jsonObject != null) {

                                if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){

                                    JSONArray walletOwnerTemplateList=jsonObject.optJSONArray("walletOwnerTemplateList");
                                    JSONObject data=walletOwnerTemplateList.optJSONObject(0);

                                    jsonObjectTestMain=new JSONObject();
                                    JSONArray jsonArrayMain=new JSONArray();
                                    int pos=0;
                                    if(data.has("feeTemplateList")){
                                        JSONArray feeTemplateList=data.optJSONArray("feeTemplateList");
                                        for (int i=0;i<feeTemplateList.length();i++){
                                            JSONObject fee=feeTemplateList.optJSONObject(i);

                                            try {
                                                JSONObject t = new JSONObject();
                                                if(jsonArrayMain.toString().contains(fee.optString("serviceCategoryCode"))){




                                                    JSONArray dataArray=new JSONArray();
                                                    JSONObject dataObject=new JSONObject();
                                                    dataObject.put("serviceCategoryCode", fee.optString("serviceCategoryCode"));
                                                    dataObject.put("serviceCategoryName", fee.optString("serviceCategoryName"));
                                                    if(fee.optString("calculationTypeCode").equalsIgnoreCase("100002")){
                                                        dataObject.put("percentFeeValue", fee.optString("percentFeeValue")+"%");
                                                    }else{
                                                        dataObject.put("fixedFeeValue", fee.optString("fixedFeeValue"));
                                                    }
                                                    dataObject.put("calculationTypeName", fee.optString("calculationTypeName"));
                                                    dataObject.put("minValue", fee.optString("minValue"));
                                                    dataObject.put("maxValue", fee.optString("maxValue"));


                                                    JSONObject prodObj=new JSONObject();
                                                    JSONArray prodArr=new JSONArray();
                                                    if(fee.has("productCode")){
                                                        prodObj.put("productCode", fee.optString("productCode"));
                                                        dataObject.put("productCode", fee.optString("productCode"));
                                                    }
                                                    if(fee.has("productName")){
                                                        prodObj.put("productName", fee.optString("productName"));
                                                        dataObject.put("productName", fee.optString("productName"));
                                                    }
                                                    for(int j=0;j<jsonArrayMain.length();j++){
                                                        if(jsonArrayMain.optJSONObject(j).optString("serviceCategoryCode").equalsIgnoreCase(fee.optString("serviceCategoryCode"))){
                                                            pos=j;

                                                        }
                                                       /* for(int k=0;k<jsonArrayMain.optJSONObject(j).optJSONArray("child").length();k++){
                                                            JSONObject tt=jsonArrayMain.optJSONObject(j).optJSONArray("child").optJSONObject(k);
                                                            System.out.println("ttt"+tt.optString("productCode"));
                                                            System.out.println("ttt    fe"+fee.optString("productCode"));
                                                            if(tt.optString("productCode").isEmpty()){

                                                            }else {
                                                                if (tt.optString("productCode").equalsIgnoreCase
                                                                        (fee.optString("productCode"))) {
                                                                    prdPos = k;
                                                                    jsonArrayMain.optJSONObject(j).optJSONArray("child").optJSONObject(k)
                                                                            .optJSONArray("productArr").put(k + 1, prodObj);
                                                                }
                                                            }

                                                        }*/


                                                    }

                                                    prodArr.put(prodObj);

                                                    dataObject.put("productArr",prodArr);

                                                    dataArray.put(dataObject);
                                                    t.put("child",dataArray);
                                                    //jsonArrayMain.put(t);


                                                    jsonArrayMain.optJSONObject(pos).optJSONArray("child").put(dataObject);

                                                }else{
                                                    t.put("ServiceName", fee.optString("serviceName"));
                                                    t.put("serviceCategoryCode", fee.optString("serviceCategoryCode"));
                                                    t.put("serviceCategoryName", fee.optString("serviceCategoryName"));
                                                    JSONArray dataArray=new JSONArray();
                                                    JSONObject dataObject=new JSONObject();
                                                    dataObject.put("serviceCategoryCode", fee.optString("serviceCategoryCode"));
                                                    dataObject.put("serviceCategoryName", fee.optString("serviceCategoryName"));
                                                    if(fee.optString("calculationTypeCode").equalsIgnoreCase("100002")){
                                                        dataObject.put("percentFeeValue", fee.optString("percentFeeValue")+"%");
                                                    }else{
                                                        dataObject.put("fixedFeeValue", fee.optString("fixedFeeValue"));
                                                    }
                                                    dataObject.put("calculationTypeName", fee.optString("calculationTypeName"));
                                                    dataObject.put("minValue", fee.optString("minValue"));
                                                    dataObject.put("maxValue", fee.optString("maxValue"));
                                                    JSONObject prodObj=new JSONObject();
                                                    JSONArray prodArr=new JSONArray();
                                                    if(fee.has("productCode")){
                                                        prodObj.put("productCode", fee.optString("productCode"));
                                                        dataObject.put("productCode", fee.optString("productCode"));
                                                    }
                                                    if(fee.has("productName")){
                                                        prodObj.put("productName", fee.optString("productName"));
                                                        dataObject.put("productName", fee.optString("productName"));
                                                    }

                                                    /*for(int j=0;j<jsonArrayMain.length();j++){
                                                        if(jsonArrayMain.optJSONObject(j).optString("ServiceName").equalsIgnoreCase(fee.optString("serviceName"))){
                                                            for(int k=0;k<jsonArrayMain.optJSONObject(j).optJSONArray("child").length();k++){
                                                                JSONObject jjj=jsonArrayMain.optJSONObject(j).optJSONArray("child").optJSONObject(k);
                                                                JSONArray pdArr=jjj.optJSONArray("productArr");
                                                                if(pdArr.length()>0){
                                                                    for(int a=0;a<pdArr.length();a++) {
                                                                        if(pdArr.optJSONObject(a).optString("productCode").equalsIgnoreCase(fee.optString("productCode"))){
                                                                            prdPos=a;
                                                                            jsonArrayMain.optJSONObject(pos).optJSONArray("child").optJSONObject(k)
                                                                                    .optJSONArray("productArr").put(a,prodObj);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }*/

                                                    prodArr.put(prodObj);
                                                    dataObject.put("productArr",prodArr);
                                                    dataArray.put(dataObject);
                                                    t.put("child",dataArray);
                                                    jsonArrayMain.put(t);

                                                }




                                            }catch (Exception e){

                                            }

                                        }

                                        try {
                                            jsonObjectTestMain.put("data",jsonArrayMain);
                                            JSONArray FeeListArr = jsonObjectTestMain.optJSONArray("data");
                                            for (int i = 0; i < FeeListArr.length(); i++) {
                                                JSONObject feeData = FeeListArr.optJSONObject(i);
                                                if(feeData.optString("ServiceName").equalsIgnoreCase("Remittance")){
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Percentage")){
                                                        tvFeeRemittance.setText("Paid Service");
                                                        //tvFeeRemittance.setText(feeData.optJSONArray("child").optJSONObject(0).optString("percentFeeValue")+" "+getString(R.string.on_the_transaction));
                                                    }
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Fixed")){
                                                        tvFeeRemittance.setText("Paid Service");
                                                        //tvFeeRemittance.setText(getString(R.string.fee_colon)+" "+feeData.optJSONArray("child").optJSONObject(0).optString("fixedFeeValue")+" "+getString(R.string.gnf_transaction));
                                                    }
                                                }
                                                if(feeData.optString("ServiceName").equalsIgnoreCase("Airtime Purchase")){
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Percentage")){
                                                        tvFeeCreditPurchase.setText("Paid Service");
                                                       // tvFeeCreditPurchase.setText(feeData.optJSONArray("child").optJSONObject(0).optString("percentFeeValue")+" "+getString(R.string.on_the_transaction));
                                                    }
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Fixed")){
                                                        tvFeeCreditPurchase.setText("Paid Service");
                                                        //tvFeeCreditPurchase.setText(getString(R.string.fee_colon)+" "+feeData.optJSONArray("child").optJSONObject(0).optString("fixedFeeValue")+" "+getString(R.string.gnf_transaction));
                                                    }
                                                }
                                                if(feeData.optString("ServiceName").equalsIgnoreCase("Recharge & Payment")){
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Percentage")){
                                                        tvFeeBillPayment.setText("Paid Service");
                                                       // tvFeeBillPayment.setText(feeData.optJSONArray("child").optJSONObject(0).optString("percentFeeValue")+" "+getString(R.string.on_the_transaction));
                                                    }
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Fixed")){
                                                        tvFeeBillPayment.setText("Paid Service");
                                                       // tvFeeBillPayment.setText(getString(R.string.fee_colon)+" "+feeData.optJSONArray("child").optJSONObject(0).optString("fixedFeeValue")+" "+getString(R.string.gnf_transaction));
                                                    }
                                                }
                                                if(feeData.optString("ServiceName").equalsIgnoreCase("Money Transfer")){
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Percentage")){
                                                        tvFeeMoneyTransfer.setText("Paid Service");
                                                       // tvFeeMoneyTransfer.setText(feeData.optJSONArray("child").optJSONObject(0).optString("percentFeeValue")+" "+getString(R.string.on_the_transaction));
                                                    }
                                                    if(feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Fixed")){
                                                        tvFeeMoneyTransfer.setText("Paid Service");
                                                        //tvFeeMoneyTransfer.setText(getString(R.string.fee_colon)+" "+feeData.optJSONArray("child").optJSONObject(0).optString("fixedFeeValue")+" "+getString(R.string.gnf_transaction));
                                                    }
                                                }
                                                if(feeData.optString("ServiceName").equalsIgnoreCase("Cash")){
                                                    if(feeData.optJSONArray("child").length()==1) {
                                                        if (feeData.optJSONArray("child").optJSONObject(0).optString("serviceCategoryName").equalsIgnoreCase("Cash In")) {
                                                            if (feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Percentage")) {
                                                                tvFeeCashIn.setText("Paid Service");
                                                               // tvFeeCashIn.setText(feeData.optJSONArray("child").optJSONObject(0).optString("percentFeeValue") + " " + getString(R.string.on_the_transaction));
                                                            }
                                                            if (feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
                                                                tvFeeCashIn.setText("Paid Service");
                                                               // tvFeeCashIn.setText(getString(R.string.fee_colon) + " " + feeData.optJSONArray("child").optJSONObject(0).optString("fixedFeeValue") + " " + getString(R.string.gnf_transaction));
                                                            }
                                                        }
                                                        if (feeData.optJSONArray("child").optJSONObject(0).optString("serviceCategoryName").equalsIgnoreCase("Cash Out")) {
                                                            if (feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Percentage")) {
                                                                tvFeeCashOut.setText("Paid Service");
                                                               // tvFeeCashOut.setText(feeData.optJSONArray("child").optJSONObject(0).optString("percentFeeValue") + " " + getString(R.string.on_the_transaction));
                                                            }
                                                            if (feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
                                                                tvFeeCashOut.setText("Paid Service");
                                                               // tvFeeCashOut.setText(getString(R.string.fee_colon) + " " + feeData.optJSONArray("child").optJSONObject(0).optString("fixedFeeValue") + " " + getString(R.string.gnf_transaction));
                                                            }
                                                        }
                                                    }

                                                    if(feeData.optJSONArray("child").length()==2) {
                                                            if (feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Percentage")) {
                                                                tvFeeCashOut.setText("Paid Service");
                                                               // tvFeeCashOut.setText(feeData.optJSONArray("child").optJSONObject(0).optString("percentFeeValue") + " " + getString(R.string.on_the_transaction));
                                                            }
                                                            if (feeData.optJSONArray("child").optJSONObject(0).optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
                                                                tvFeeCashOut.setText("Paid Service");
                                                                //tvFeeCashOut.setText(getString(R.string.fee_colon) + " " + feeData.optJSONArray("child").optJSONObject(0).optString("fixedFeeValue") + " " + getString(R.string.gnf_transaction));
                                                            }

                                                            if (feeData.optJSONArray("child").optJSONObject(1).optString("calculationTypeName").equalsIgnoreCase("Percentage")) {
                                                                tvFeeCashIn.setText("Paid Service");
                                                                //tvFeeCashIn.setText(feeData.optJSONArray("child").optJSONObject(1).optString("percentFeeValue") + " " + getString(R.string.on_the_transaction));
                                                            }
                                                            if (feeData.optJSONArray("child").optJSONObject(1).optString("calculationTypeName").equalsIgnoreCase("Fixed")) {
                                                                tvFeeCashIn.setText("Paid Service");
                                                               // tvFeeCashIn.setText(getString(R.string.fee_colon) + " " + feeData.optJSONArray("child").optJSONObject(1).optString("fixedFeeValue") + " " + getString(R.string.gnf_transaction));
                                                            }

                                                    }


                                                    }



                                            }



                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        System.out.println("data  = = = "+jsonObjectTestMain.toString());


                                    }else{
                                        MyApplication.showToast(servicechargeC,"Fee not assign");
                                    }



                                }
                                else {
                                    MyApplication.showToast(servicechargeC,jsonObject.optString("resultDescription"));
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



}