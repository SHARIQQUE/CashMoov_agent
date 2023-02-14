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

import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.transactionhistory_walletscreen.TransactionHistoryMainPage;
import com.agent.cashmoovui.transfer_float.TransferOption;


public class MoneyTransferFeeActivity extends AppCompatActivity implements View.OnClickListener{
    public static MoneyTransferFeeActivity moneytransferfeeC;
    ImageView imgBack,imgHome;
    CardView cardSellFloat,cardTransferFloat,cardinternationaltransfer,cardcommisiontransfer;
    TextView tvServiceName;
    Button btnClose;

    private long mLastClickTime = 0;

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
        cardinternationaltransfer = findViewById(R.id.cardinternationaltransfer);
        cardcommisiontransfer = findViewById(R.id.cardcommisiontransfer);
        tvServiceName = findViewById(R.id.tvServiceName);
        tvServiceName.setText(getString(R.string.transfer));
        btnClose = findViewById(R.id.btnClose);

        if (MyApplication.getSaveString("walletOwnerCategoryCode", MoneyTransferFeeActivity.this).equalsIgnoreCase(MyApplication.AgentCode)) {
            cardSellFloat.setVisibility(View.GONE);

        } else if (MyApplication.getSaveString("walletOwnerCategoryCode", MoneyTransferFeeActivity.this).equalsIgnoreCase(MyApplication.BranchCode)){

            cardSellFloat.setVisibility(View.GONE);
    }

        if (MyApplication.getSaveString("walletOwnerCategoryCode", MoneyTransferFeeActivity.this).equalsIgnoreCase(MyApplication.MerchatCode)) {
            cardSellFloat.setVisibility(View.GONE);
            cardinternationaltransfer.setVisibility(View.GONE);

        } else if (MyApplication.getSaveString("walletOwnerCategoryCode", MoneyTransferFeeActivity.this).equalsIgnoreCase(MyApplication.OutletCode)){

            cardSellFloat.setVisibility(View.GONE);
            cardinternationaltransfer.setVisibility(View.GONE);

        }
        setOnCLickListener();


    }

    private void setOnCLickListener() {
        cardSellFloat.setOnClickListener(moneytransferfeeC);
        cardTransferFloat.setOnClickListener(moneytransferfeeC);
        cardinternationaltransfer.setOnClickListener(moneytransferfeeC);
        cardcommisiontransfer.setOnClickListener(moneytransferfeeC);
        btnClose.setOnClickListener(moneytransferfeeC);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.cardSellFloat:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                intent = new Intent(moneytransferfeeC, ServiceChargeDetails.class);
                intent.putExtra("FEEINTENT",getString(R.string.sell_Float));
                startActivity(intent);
                break;
            case R.id.cardTransferFloat:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                intent = new Intent(moneytransferfeeC, ServiceChargeDetails.class);
                intent.putExtra("FEEINTENT",getString(R.string.transfer_float));
                startActivity(intent);
                break;

            case R.id.cardinternationaltransfer:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                intent = new Intent(moneytransferfeeC, ServiceChargeDetails.class);
                intent.putExtra("FEEINTENT",getString(R.string.international_transfer));
                startActivity(intent);
                break;
            case R.id.cardcommisiontransfer:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                intent = new Intent(moneytransferfeeC, ServiceChargeDetails.class);
                intent.putExtra("FEEINTENT",getString(R.string.commision_Transfer));
                startActivity(intent);
                break;
            case R.id.btnClose:
                finish();
                break;
        }

    }



}
