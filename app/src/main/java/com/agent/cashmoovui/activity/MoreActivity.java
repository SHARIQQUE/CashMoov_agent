package com.agent.cashmoovui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.overdraft.OverdraftLimit;
import com.agent.cashmoovui.remittancebyabhay.RemittanceOption;
import com.agent.cashmoovui.remittancebyabhay.cashtowallet.CashtoWalletSenderKYC;
import com.agent.cashmoovui.transfer_float.TransferOption;
import com.agent.cashmoovui.wallet_owner.WalletOwnerMenu;


public class MoreActivity extends AppCompatActivity implements View.OnClickListener {
    public static MoreActivity moreC;
    ImageView imgBack,imgHome;
    CardView cardTransfer,cardOverdraft,cardWalletOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        moreC=this;
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
                MyApplication.hideKeyboard(moreC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(moreC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private void getIds() {
        cardTransfer = findViewById(R.id.cardTransfer);
        cardOverdraft = findViewById(R.id.cardOverdraft);
        cardWalletOwner = findViewById(R.id.cardWalletOwner);

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        cardTransfer.setOnClickListener(moreC);
        cardOverdraft.setOnClickListener(moreC);
        cardWalletOwner.setOnClickListener(moreC);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.cardTransfer:
//                if(!MyApplication.showTransfer){
//                    MyApplication.showToast(moreC,getString(R.string.service_not_available));
//                }else {
//                    i = new Intent(moreC, TransferOption.class);
//                    startActivity(i);
//                }
                i = new Intent(moreC, TransferOption.class);
                startActivity(i);
                break;
            case R.id.cardOverdraft:
                i = new Intent(moreC, OverdraftLimit.class);
                startActivity(i);
                break;
            case R.id.cardWalletOwner:
                i = new Intent(moreC, WalletOwnerMenu.class);
                startActivity(i);
                break;

        }

    }


}
