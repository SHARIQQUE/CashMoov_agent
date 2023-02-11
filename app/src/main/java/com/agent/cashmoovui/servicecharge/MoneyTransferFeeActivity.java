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


public class MoneyTransferFeeActivity extends AppCompatActivity implements View.OnClickListener{
    public static MoneyTransferFeeActivity moneytransferfeeC;
    ImageView imgBack,imgHome;
    CardView cardSellFloat,cardTransferFloat;
    TextView tvServiceName;
    Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_money_transfer_fee);
        moneytransferfeeC=this;
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
        cardSellFloat = findViewById(R.id.cardSellFloat);
        cardTransferFloat = findViewById(R.id.cardTransferFloat);
        tvServiceName = findViewById(R.id.tvServiceName);
        tvServiceName.setText(getString(R.string.money_transfer));
        btnClose = findViewById(R.id.btnClose);

        setOnCLickListener();


    }

    private void setOnCLickListener() {
        cardSellFloat.setOnClickListener(moneytransferfeeC);
        cardTransferFloat.setOnClickListener(moneytransferfeeC);
        btnClose.setOnClickListener(moneytransferfeeC);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.cardSellFloat:
                intent = new Intent(moneytransferfeeC, ServiceChargeDetails.class);
                intent.putExtra("FEEINTENT",getString(R.string.sell_Float));
                startActivity(intent);
                break;
            case R.id.cardTransferFloat:
                intent = new Intent(moneytransferfeeC, ServiceChargeDetails.class);
                intent.putExtra("FEEINTENT",getString(R.string.transfer_float));
                startActivity(intent);
                break;
            case R.id.btnClose:
                finish();
                break;
        }

    }



}
