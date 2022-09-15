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
import com.agent.cashmoovui.model.Taxmodel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class WalletTransactionDetails extends AppCompatActivity {
    public static WalletTransactionDetails wallettransdetailsC;
    ImageView imgBack,imgHome;
    TextView txt_trans_type_name,txt_from_owner_name,txt_from_amount,txt_trans_id,txt_financialtax,txt_fee,
            txt_creation_date,txt_status,txt_success,txt_postbalance,txt_commission_amount;


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
        txt_financialtax=findViewById(R.id.txt_financialtax);
        txt_fee=findViewById(R.id.txt_fee);
        txt_commission_amount = findViewById(R.id.txt_commission_amount);
        txt_postbalance=findViewById(R.id.txt_postbalance);



        Bundle b = getIntent().getExtras();


        if (getIntent().getExtras() != null) {
            String transType = (getIntent().getStringExtra("TRANSTYPE"));
            String fromOwnerName = (getIntent().getStringExtra("FROMWALLETOWNERNAME"));
            String toOwnerName = (getIntent().getStringExtra("TOWALLETOWNERNAME"));
            String fromAmount = (getIntent().getStringExtra("FROMAMOUNT"));
            String transId = (getIntent().getStringExtra("TRANSID"));
            String creationDate = (getIntent().getStringExtra("CREATIONDATE"));
            String status = (getIntent().getStringExtra("STATUS"));
            String tax = (getIntent().getStringExtra("taxvalue"));
            double  srcpostbalance = b.getDouble("srcpostbalance");
            double  fee = b.getDouble("fee");

            String commissionAmount = (getIntent().getStringExtra("COMMISSIONAMOUNT"));
            String walletTypeCode="100008";
           if((getIntent().getStringExtra("WALLETTYPECODE")!=null) ){
                 walletTypeCode = (getIntent().getStringExtra("WALLETTYPECODE"));
            }

            String fromWalletOwnerMsisdn = (getIntent().getStringExtra("FROMMSISDN"));
            String toWalletOwnerMsisdn = (getIntent().getStringExtra("TOMSISDN"));
            String transactionAmount = (getIntent().getStringExtra("TRANSACTIONAMOUNT"));
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);

            DecimalFormat df = new DecimalFormat("0.00",symbols);

            txt_trans_type_name.setText(getString(R.string.transaction_type)+" - "+transType);
            txt_fee.setText(getString(R.string.fee_colon)+df.format(fee));
           txt_postbalance.setText(getString(R.string.post_balance_colon) +String.format("%.2f", srcpostbalance));
            txt_from_owner_name.setText(getString(R.string.from)+" : "+fromWalletOwnerMsisdn+"("+fromOwnerName+")"+" ,\n"+getString(R.string.to)+" : "+toWalletOwnerMsisdn+"("+toOwnerName+")");
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
            try {

                Gson gson = new Gson();

                Type userListType = new TypeToken<ArrayList<Taxmodel>>(){}.getType();

                ArrayList<Taxmodel> userArray = gson.fromJson(tax, userListType);
                for(Taxmodel user : userArray) {
                    String taxvaluename=user.getTaxTypeName();
                    if(taxvaluename.equalsIgnoreCase("VAT")){
                        txt_financialtax.setText(getString(R.string.Taxvat) + " :" + " " +MyApplication.addDecimal(user.getValue()));

                    }else{
                       if(taxvaluename.equalsIgnoreCase("Financial Tax")){
                           txt_financialtax.setText(getString(R.string.Taxfinancial) + " :" + " " +MyApplication.addDecimal(user.getValue()));

                       }
                    }

                    System.out.println("get user" + MyApplication.addDecimal(user.getValue()));
                }


            } catch (Exception e) {

            }

            if(txt_financialtax.getText().toString().isEmpty()||
                    txt_financialtax.getText().toString().trim().equalsIgnoreCase("N/A")){
                txt_financialtax.setText( "TAX : 0.00" );
            }

        }

    }


}