package com.agent.cashmoovui.servicecharge;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.agent.cashmoovui.R;

public class RemittanceFeeActivity extends AppCompatActivity implements View.OnClickListener{
    public static RemittanceFeeActivity remittancefeeC;
    ImageView imgBack,imgHome;
    CardView cardSendRemit,cardReceiveRemit,cardCashToWallet;
    TextView tvServiceName;
    Button btnClose;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_remittance_fee);
        remittancefeeC=this;
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
        cardSendRemit = findViewById(R.id.cardSendRemit);
        cardReceiveRemit = findViewById(R.id.cardReceiveRemit);
        cardCashToWallet = findViewById(R.id.cardCashToWallet);
        tvServiceName = findViewById(R.id.tvServiceName);
        tvServiceName.setText(getString(R.string.remittance));
        btnClose = findViewById(R.id.btnClose);

        setOnCLickListener();


    }

    private void setOnCLickListener() {
        cardSendRemit.setOnClickListener(remittancefeeC);
        cardReceiveRemit.setOnClickListener(remittancefeeC);
        cardCashToWallet.setOnClickListener(remittancefeeC);
        btnClose.setOnClickListener(remittancefeeC);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.cardSendRemit:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                intent = new Intent(remittancefeeC, ServiceChargeDetails.class);
                intent.putExtra("FEEINTENT",getString(R.string.send_remittance));
                startActivity(intent);
                break;
            case R.id.cardReceiveRemit:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                intent = new Intent(remittancefeeC, ServiceChargeDetails.class);
                intent.putExtra("FEEINTENT",getString(R.string.receive_remittance));
                startActivity(intent);
                break;
            case R.id.cardCashToWallet:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                intent = new Intent(remittancefeeC, ServiceChargeDetails.class);
                intent.putExtra("FEEINTENT",getString(R.string.cash_to_wallet));
                startActivity(intent);
                break;
            case R.id.btnClose:
                finish();
                break;
        }

    }



}
