package com.agent.cashmoovui.transactionhistory_walletscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.activity.OtherOption;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class WalletTransactionDetails extends AppCompatActivity {
    public static WalletTransactionDetails wallettransdetailsC;
    ImageView imgBack,imgHome;
    TextView txt_trans_type_name,txt_from_owner_name,txt_from_amount,txt_trans_id,
            txt_creation_date,txt_status,txt_success,txt_commission_amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_transaction_details);
        wallettransdetailsC=this;
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
                MyApplication.hideKeyboard(WalletTransactionDetails.this);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(WalletTransactionDetails.this);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    private void getIds() {
        txt_trans_type_name = findViewById(R.id.txt_trans_type_name);
        txt_from_owner_name = findViewById(R.id.txt_from_owner_name);
        txt_from_amount = findViewById(R.id.txt_from_amount);
        txt_trans_id = findViewById(R.id.txt_trans_id);
        txt_creation_date = findViewById(R.id.txt_creation_date);
        txt_status = findViewById(R.id.txt_status);
        txt_success = findViewById(R.id.txt_success);
        txt_commission_amount = findViewById(R.id.txt_commission_amount);

        if (getIntent().getExtras() != null) {
            String transType = (getIntent().getStringExtra("TRANSTYPE"));
            String fromOwnerName = (getIntent().getStringExtra("FROMWALLETOWNERNAME"));
            String toOwnerName = (getIntent().getStringExtra("TOWALLETOWNERNAME"));
            String fromAmount = (getIntent().getStringExtra("FROMAMOUNT"));
            String transId = (getIntent().getStringExtra("TRANSID"));
            String creationDate = (getIntent().getStringExtra("CREATIONDATE"));
            String status = (getIntent().getStringExtra("STATUS"));
            String commissionAmount = (getIntent().getStringExtra("COMMISSIONAMOUNT"));
            String walletTypeCode="100008";
           if((getIntent().getStringExtra("WALLETTYPECODE")!=null) ){
                 walletTypeCode = (getIntent().getStringExtra("WALLETTYPECODE"));
            }

            String fromWalletOwnerMsisdn = (getIntent().getStringExtra("FROMMSISDN"));
            String toWalletOwnerMsisdn = (getIntent().getStringExtra("TOMSISDN"));
            String transactionAmount = (getIntent().getStringExtra("TRANSACTIONAMOUNT"));

            txt_trans_type_name.setText(getString(R.string.transaction_type)+" - "+transType);
            txt_from_owner_name.setText("From"+" : "+fromWalletOwnerMsisdn+"("+fromOwnerName+")"+" ,\n"+"To"+" : "+toWalletOwnerMsisdn+"("+toOwnerName+")");
            if(walletTypeCode.equalsIgnoreCase("100009")){
                txt_commission_amount.setText(commissionAmount);
            }else{
                txt_commission_amount.setText(fromAmount);
            }

            txt_commission_amount.setText(fromAmount);
            txt_from_amount.setText(getString(R.string.transaction_amount_receiptPage)+" : "+transactionAmount);
            txt_trans_id.setText(getString(R.string.transaction_id_colon)+" "+transId);
            txt_status.setText(getString(R.string.status)+" : "+status);
            txt_success.setText(getString(R.string.transaction_successful));
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                Date date = null;
                date = inputFormat.parse(creationDate);
                String formattedDate = outputFormat.format(date);
                txt_creation_date.setText(getString(R.string.date)+" : "+formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }

    }


}