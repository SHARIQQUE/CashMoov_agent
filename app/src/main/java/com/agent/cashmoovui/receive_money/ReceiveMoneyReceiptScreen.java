package com.agent.cashmoovui.receive_money;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.model.CountryCurrencyInfoModel;
import com.agent.cashmoovui.model.CountryInfoModel;
import com.agent.cashmoovui.model.ServiceProviderModel;
import com.aldoapps.autoformatedittext.AutoFormatUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class ReceiveMoneyReceiptScreen extends AppCompatActivity {
    public static ReceiveMoneyReceiptScreen receiveMoneyReceiptScreenC;
    Button btnClose,btnShareReceipt;
    TextView tvSubscriberMobile,tvrate,tvConfCode,tvProvider,tvTransType,tvMobile,tvName,tvTransId,tvCurrency,tvFee,tvTransAmt,tvAmountPaid,tvAmountCharged,
            tax1_lable,tax1_value,tax2_lable,tax2_value;
    LinearLayout linConfCode,tax1_layout,tax2_layout;
    View rootView;

    private TextView tvTranstype,tvsendercurrency,tvReceivercurrency,tvSendCurrencycode,
            tvReceiverCurrencyCode,tvRecCountry,tvTransAmount,tvReceiverName,
            tvAmounttobpaid,tvSendName,tvSendPhoneNo,tvSendemail,
            tvAmount,tvReceiverPhoneNo,tvReceiveremail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_money_kyc_receipt);
        receiveMoneyReceiptScreenC=this;
        rootView = getWindow().getDecorView().findViewById(R.id.lin);

        getIds();
    }






    private void getIds() {
        tvTranstype=findViewById(R.id.tvTranstype);
        tvsendercurrency=findViewById(R.id.tvsendercurrency);
        tvReceivercurrency=findViewById(R.id.tvReceivercurrency);
        tvSendCurrencycode=findViewById(R.id.tvSendCurrencycode);
        tvReceiverCurrencyCode=findViewById(R.id.tvReceiverCurrencyCode);
        tvAmount=findViewById(R.id.tvAmount);
        tvAmounttobpaid=findViewById(R.id.tvAmounttobpaid);
        tvTransAmount=findViewById(R.id.tvTransAmount);
        tvFee=findViewById(R.id.tvFee);
        tvSendName=findViewById(R.id.tvSendName);
        tvSendPhoneNo=findViewById(R.id.tvSendPhoneNo);
        tvSendemail=findViewById(R.id.tvSendemail);
        tvReceiverName=findViewById(R.id.tvReceiverName);
        tvReceiverPhoneNo=findViewById(R.id.tvReceiverPhoneNo);
        tvReceiveremail=findViewById(R.id.tvReceiveremail);

        tvTranstype.setText(ReceiveMoneyConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("transactionType"));
        tvsendercurrency.setText(ReceiveMoneyConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("srcCurrencySymbol"));
        tvReceivercurrency.setText(ReceiveMoneyConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("desCurrencySymbol"));
        tvSendCurrencycode.setText(ReceiveMoneyConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("srcCurrencyCode"));
        tvReceiverCurrencyCode.setText(ReceiveMoneyConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("desCurrencyCode"));
        tvAmount.setText(MyApplication.addDecimal(ReceiveMoneyDetailScreen.amount));
        tvAmounttobpaid.setText(ReceiveMoneyConfirmScreen.tvAmountPaid.getText().toString());
        tvTransAmount.setText(ReceiveMoneyConfirmScreen.tvTransAmounts.getText().toString());
        tvFee.setText(ReceiveMoneyConfirmScreen.tvFee.getText().toString());

        Intent iin= getIntent();
        Bundle bollean = iin.getExtras();

        if(bollean!=null)
        {
            String emailstr =(String) bollean.get("email");
            String mobileNumber =(String) bollean.get("mobileNumber");
            String ownerName =(String) bollean.get("ownerName");
            String lastName =(String) bollean.get("lastName");
            String emailrec =(String) bollean.get("emailrec");
            String mobileNumberrec =(String) bollean.get("mobileNumberrec");
            String ownerNamerec =(String) bollean.get("ownerNamerec");
            String lastNamerec =(String) bollean.get("lastNamerec");

            tvSendemail.setText(emailstr);


            tvSendName.setText(ownerName + " "+lastName);
            tvSendPhoneNo.setText(mobileNumber);
            tvReceiverName.setText(ownerNamerec+ " "+lastNamerec);
            tvReceiverPhoneNo.setText(mobileNumberrec);
            tvReceiveremail.setText(emailrec);

        }
       // tvSendName.setText(ReceiveMoneyConfirmScreen.walletTransfernew.optJSONObject("srcWalletOwner").optString("ownerName")+ " "+ ReceiveMoneyConfirmScreen.walletTransfernew.optJSONObject("srcWalletOwner").optString("lastName"));
       /* tvSendPhoneNo.setText(ReceiveMoneyConfirmScreen.mobilenumber);
        tvSendemail.setText(ReceiveMoneyConfirmScreen.email);
        tvReceiverName.setText(ReceiveMoneyConfirmScreen.ownernamereceiver+ ""+ReceiveMoneyConfirmScreen.lastnamereceiver);
        tvReceiverPhoneNo.setText(ReceiveMoneyConfirmScreen.mobilenumberreceiver);
        tvReceiveremail.setText(ReceiveMoneyConfirmScreen.emailreceiver);*/



    }





    @Override
    public void onBackPressed() {
        MyApplication.isFirstTime=false;
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }
}






