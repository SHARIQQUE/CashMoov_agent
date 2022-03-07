package com.agent.cashmoovui.servicecharge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.agent.cashmoovui.R;

public class CashOutFeeActivity extends AppCompatActivity implements View.OnClickListener{
    public static CashOutFeeActivity cashoutfeeC;
    ImageView imgBack,imgHome;
    CardView cardCashOut;
    TextView tvServiceName;
    Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_cashout_fee);
        cashoutfeeC=this;
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
        cardCashOut = findViewById(R.id.cardCashOut);
        tvServiceName = findViewById(R.id.tvServiceName);
        tvServiceName.setText(getString(R.string.cash_Out));
        btnClose = findViewById(R.id.btnClose);

        setOnCLickListener();


    }

    private void setOnCLickListener() {
        cardCashOut.setOnClickListener(cashoutfeeC);
        btnClose.setOnClickListener(cashoutfeeC);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.cardCashOut:
                intent = new Intent(cashoutfeeC, ServiceChargeDetails.class);
                intent.putExtra("FEEINTENT","Cash Out");
                startActivity(intent);
                break;
            case R.id.btnClose:
                finish();
                break;
        }

    }



}
