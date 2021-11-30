package com.agent.cashmoovui.transactionhistory_walletscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class WalletTransactionDetails extends AppCompatActivity {
    public static WalletTransactionDetails wallettransdetailsC;
    ImageView imgBack,imgHome;
    TextView txt_trans_type_name,txt_from_owner_name,txt_from_amount,txt_trans_id,txt_creation_date,txt_status,txt_success;


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
        txt_trans_type_name = findViewById(R.id.txt_trans_type_name);
        txt_from_owner_name = findViewById(R.id.txt_from_owner_name);
        txt_from_amount = findViewById(R.id.txt_from_amount);
        txt_trans_id = findViewById(R.id.txt_trans_id);
        txt_creation_date = findViewById(R.id.txt_creation_date);
        txt_status = findViewById(R.id.txt_status);
        txt_success = findViewById(R.id.txt_success);

        if (getIntent().getExtras() != null) {
            String transType = (getIntent().getStringExtra("TRANSTYPE"));
            String fromOwnerName = (getIntent().getStringExtra("FROMWALLETOWNERNAME"));
            String fromAmount = (getIntent().getStringExtra("FROMAMOUNT"));
            String transId = (getIntent().getStringExtra("TRANSID"));
            String creationDate = (getIntent().getStringExtra("CREATIONDATE"));
            String status = (getIntent().getStringExtra("STATUS"));
            txt_trans_type_name.setText(getString(R.string.transaction_type)+" - "+transType);
            txt_from_owner_name.setText(fromOwnerName);
            txt_from_amount.setText(fromAmount);
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