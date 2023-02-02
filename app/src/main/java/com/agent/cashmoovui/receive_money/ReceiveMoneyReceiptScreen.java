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
import com.agent.cashmoovui.remittancebyabhay.cashtowallet.CashtoWalletConfirmScreen;
import com.agent.cashmoovui.remittancebyabhay.international.InternationalRemittanceActivity;
import com.agent.cashmoovui.remittancebyabhay.international.InternationalRemittanceConfirmScreen;
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

public class ReceiveMoneyReceiptScreen extends AppCompatActivity implements View.OnClickListener {
    public static ReceiveMoneyReceiptScreen receiveMoneyReceiptScreenC;
    Button btnCloseReceipt,btnShareReceipt;
    TextView tvTransRefNo,tvSubscriberMobile,tvrate,tvConfCode,tvProvider,tvTransType,tvMobile,tvName,tvTransId,tvCurrency,tvFee,tvTransAmt,tvAmountPaid,tvAmountCharged,
            tax1_lable,tax1_value,tax2_lable,tax2_value;
    LinearLayout linConfCode,tax1_layout,tax2_layout;
    View rootView;

    private TextView tvDateOfTrans,tvTranstype,tvsendercurrency,tvReceivercurrency,tvSendCurrencycode,
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
        tvDateOfTrans=findViewById(R.id.tvDateOfTrans);
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
        btnCloseReceipt = findViewById(R.id.btnCloseReceipt);
        btnCloseReceipt.setOnClickListener(this);
        btnShareReceipt=findViewById(R.id.btnShareReceipt);
        btnShareReceipt.setOnClickListener(this);
        tvTransRefNo = findViewById(R.id.tvTransRefNo);


        tax1_layout = findViewById(R.id.tax1_layout);
        tax2_layout = findViewById(R.id.tax2_layout);
        tax1_lable = findViewById(R.id.tax1_lable);
        tax1_value = findViewById(R.id.tax1_value);
        tax2_lable = findViewById(R.id.tax2_lable);
        tax2_value = findViewById(R.id.tax2_value);
        tvTranstype.setText(ReceiveMoneyConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("transactionType"));
        tvsendercurrency.setText(ReceiveMoneyConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("srcCurrencySymbol"));
        tvReceivercurrency.setText(ReceiveMoneyConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("desCurrencySymbol"));
        tvSendCurrencycode.setText(ReceiveMoneyConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("srcCurrencyCode"));
        tvReceiverCurrencyCode.setText(ReceiveMoneyConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("desCurrencyCode"));
        tvAmount.setText(ReceiveMoneyConfirmScreen.tvAmountCharged.getText().toString());
        tvAmounttobpaid.setText(ReceiveMoneyConfirmScreen.tvAmountPaid.getText().toString());
        tvTransAmount.setText(ReceiveMoneyConfirmScreen.tvTransAmounts.getText().toString());
        tvFee.setText(ReceiveMoneyConfirmScreen.tvFee.getText().toString());
        tvTransRefNo.setText(ReceiveMoneyConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("code"));

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
            String creationDate =(String) bollean.get("creationDate");

            tvSendemail.setText(emailstr);

            System.out.println("get date "+creationDate);


            tvSendName.setText(ownerName + " "+lastName);
            tvSendPhoneNo.setText(mobileNumber);
            tvReceiverName.setText(ownerNamerec+ " "+lastNamerec);
            tvReceiverPhoneNo.setText(mobileNumberrec);
            tvReceiveremail.setText(emailrec);
            tvDateOfTrans.setText((creationDate));

            if(ReceiveMoneyDetailScreen.taxConfigurationList!=null){
                if(ReceiveMoneyDetailScreen.taxConfigurationList.length()==1){
                    tax1_layout.setVisibility(View.VISIBLE);
                    tax1_lable.setText(MyApplication.getTaxStringnew(ReceiveMoneyDetailScreen.taxConfigurationList.optJSONObject(0).optString("taxTypeName"))+" :");
                    tax1_value.setText(ReceiveMoneyDetailScreen.fromCurrencySymbol+" "+MyApplication.addDecimal(""+ReceiveMoneyDetailScreen.taxConfigurationList.optJSONObject(0).optDouble("value")));
                    // finalamount=Double.parseDouble(InternationalRemittanceActivity.fee)+Double.parseDouble(InternationalRemittanceActivity.amount)+Double.parseDouble(InternationalRemittanceActivity.taxConfigurationList.optJSONObject(0).optString("value"));
                }
                if(ReceiveMoneyDetailScreen.taxConfigurationList.length()==2){
                    tax1_layout.setVisibility(View.VISIBLE);
                    tax1_lable.setText(MyApplication.getTaxStringnew(InternationalRemittanceConfirmScreen.taxConfigList.optJSONObject(0).optString("taxTypeName") )+ ":");
                    tax1_value.setText(ReceiveMoneyConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("fromCurrencySymbol") + " " + MyApplication.addDecimal(""+ReceiveMoneyConfirmScreen.taxConfigList.optJSONObject(0).optDouble("value")));

                    tax2_layout.setVisibility(View.VISIBLE);
                    tax2_lable.setText(MyApplication.getTaxStringnew(InternationalRemittanceActivity.taxConfigurationList.optJSONObject(1).optString("taxTypeName"))+" :");
                    tax2_value.setText(ReceiveMoneyDetailScreen.fromCurrencySymbol+" "+MyApplication.addDecimal(""+ReceiveMoneyDetailScreen.taxConfigurationList.optJSONObject(1).optDouble("value")));
                    // finalamount=Double.parseDouble(InternationalRemittanceActivity.fee)+Double.parseDouble(InternationalRemittanceActivity.amount)+Double.parseDouble(InternationalRemittanceActivity.taxConfigurationList.optJSONObject(0).optString("value"))+Double.parseDouble(InternationalRemittanceActivity.taxConfigurationList.optJSONObject(1).optString("value"));
                }
            }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShareReceipt:
                btnShareReceipt.setVisibility(View.VISIBLE);
                Bitmap bitmap=getScreenShot(rootView);
                createImageFile(bitmap);
                //store(bitmap,"test.jpg");
                break;
            case R.id.btnCloseReceipt:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

        }
    }

    public static Bitmap getScreenShot(View view) {
        View screenView = view;
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public  void createImageFile(Bitmap bm)  {
        try {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(System.currentTimeMillis());
            File storageDir = new File(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/");
            if (!storageDir.exists())
                storageDir.mkdirs();
            File image = File.createTempFile(
                    timeStamp,
                    ".jpeg",
                    storageDir
            );

            System.out.println(image.getAbsolutePath());
            if (image.exists()) image.delete();
            //   Log.i("LOAD", root + fname);
            try {
                FileOutputStream out = new FileOutputStream(image);
                bm.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            shareImage(image);
        }catch (Exception e){

        }
    }

    private void shareImage(File file){
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.share_screenshot)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_app_available), Toast.LENGTH_SHORT).show();
        }
    }


}






