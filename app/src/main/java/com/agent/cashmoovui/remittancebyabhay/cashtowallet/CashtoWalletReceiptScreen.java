package com.agent.cashmoovui.remittancebyabhay.cashtowallet;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CashtoWalletReceiptScreen extends AppCompatActivity implements View.OnClickListener {
    public static CashtoWalletReceiptScreen cashtowalletreceiptscreenC;
    private TextView tvTransRefNo,tvConfCode,tvTransType,tvDateOfTrans,tvSendCurrency,tvBenefiCurrency,
            tvSendCountry,tvRecCountry,tvTransAmount,tvConvRate,tvFee,tvAmountCharged,tvAmountPaid,
            tvSendName,tvSendPhoneNo,tvBenefiName,tvBenefiPhoneNo,
            tax1_lable,tax1_value,tax2_lable,tax2_value;
    private LinearLayout linConfCode,tax1_layout,tax2_layout;
    private Button btnCloseReceipt,btnShareReceipt;
    View rootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_to_wallet_receipt_screen);
        cashtowalletreceiptscreenC=this;
        rootView = getWindow().getDecorView().findViewById(R.id.lin);
        getIds();
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


//    public  void store(Bitmap bm, String fileName){
//        final  String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
//        File dir = new File(dirPath);
//        if(!dir.exists())
//            dir.mkdirs();
//        File file = new File(dirPath, fileName);
//        try {
//            FileOutputStream fOut = new FileOutputStream(file);
//            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
//            fOut.flush();
//            fOut.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        shareImage(file);
//    }

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

    private void getIds() {
        tvTransRefNo = findViewById(R.id.tvTransRefNo);
        tvConfCode = findViewById(R.id.tvConfCode);
        tvTransType = findViewById(R.id.tvTransType);
        tvDateOfTrans = findViewById(R.id.tvDateOfTrans);
        tvSendCurrency = findViewById(R.id.tvSendCurrency);
        tvBenefiCurrency = findViewById(R.id.tvBenefiCurrency);
        tvSendCountry = findViewById(R.id.tvSendCountry);
        tvRecCountry = findViewById(R.id.tvRecCountry);
        tvTransAmount = findViewById(R.id.tvTransAmount);
        tvConvRate = findViewById(R.id.tvConvRate);
        tvFee = findViewById(R.id.tvFee);
        tvAmountCharged = findViewById(R.id.tvAmountCharged);
        tvAmountPaid = findViewById(R.id.tvAmountPaid);
        tvSendName = findViewById(R.id.tvSendName);
        tvSendPhoneNo = findViewById(R.id.tvSendPhoneNo);
        tvBenefiName = findViewById(R.id.tvBenefiName);
        tvBenefiPhoneNo = findViewById(R.id.tvBenefiPhoneNo);
        btnCloseReceipt = findViewById(R.id.btnCloseReceipt);
        btnShareReceipt = findViewById(R.id.btnShareReceipt);

        tax1_layout = findViewById(R.id.tax1_layout);
        tax2_layout = findViewById(R.id.tax2_layout);
        tax1_lable = findViewById(R.id.tax1_lable);
        tax1_value = findViewById(R.id.tax1_value);
        tax2_lable = findViewById(R.id.tax2_lable);
        tax2_value = findViewById(R.id.tax2_value);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DecimalFormat df = new DecimalFormat("0.00",symbols);
        tvTransRefNo.setText(CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("code"));
       // tvConfCode.setText(CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("confirmationCode"));
        tvTransType.setText(CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("transactionType"));
        tvDateOfTrans.setText(CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("creationDate"));
        tvSendCurrency.setText(CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("srcCurrencyName"));
        tvBenefiCurrency.setText(CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("desCurrencyName"));
        tvSendCountry.setText(CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").optJSONObject("srcWalletOwner").optString("registerCountryName"));
        tvRecCountry.setText(CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").optJSONObject("desWalletOwner").optString("registerCountryName"));
        tvSendName.setText(CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").optJSONObject("srcWalletOwner").optString("ownerName")+" "+
                CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").optJSONObject("srcWalletOwner").optString("lastName"));
        tvSendPhoneNo.setText(CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").optJSONObject("srcWalletOwner").optString("mobileNumber"));
        tvBenefiName.setText(CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").optJSONObject("desWalletOwner").optString("ownerName")+" "+
                CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").optJSONObject("desWalletOwner").optString("lastName"));
        tvBenefiPhoneNo.setText(CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").optJSONObject("desWalletOwner").optString("mobileNumber"));

        tvTransAmount.setText((CashtoWalletConfirmScreen.tvTransAmount.getText().toString()));
        if(CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").has("conversionRate")){
            tvConvRate.setText(MyApplication.addDecimalthreenew(CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("conversionRate")));
        }else{
            tvConvRate.setText("0.000");
        }

        tvFee.setText(CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("srcCurrencySymbol")+" "
                + 	MyApplication.addDecimal(""+CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").optDouble("fee")));
        tvAmountPaid.setText(MyApplication.addDecimal(CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("desCurrencySymbol")+" "+	MyApplication.addDecimal(""+CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").optDouble("finalAmount"))));
        tvAmountCharged.setText(MyApplication.addDecimal(CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("srcCurrencySymbol")+" "+	MyApplication.addDecimal(""+CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").optDouble("value"))));


        if(CashtoWalletConfirmScreen.taxConfigList!=null){
            if(CashtoWalletConfirmScreen.taxConfigList.length()==1){
                tax1_layout.setVisibility(View.VISIBLE);
                tax1_lable.setText(MyApplication.getTaxString(CashtoWalletConfirmScreen.taxConfigList.optJSONObject(0).optString("taxTypeName"))+" :");
                tax1_value.setText(CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("srcCurrencySymbol")+" "+	MyApplication.addDecimal(""+CashtoWalletConfirmScreen.taxConfigList.optJSONObject(0).optDouble("value")));
                // finalamount=Double.parseDouble(String.valueOf(ToSubscriber.fee))+Double.parseDouble(ToSubscriber.etAmount.getText().toString())+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"));
            }
            if(CashtoWalletConfirmScreen.taxConfigList.length()==2){
                tax1_layout.setVisibility(View.VISIBLE);
                tax1_lable.setText(MyApplication.getTaxString(CashtoWalletConfirmScreen.taxConfigList.optJSONObject(0).optString("taxTypeName"))+" :");
                tax1_value.setText(CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("srcCurrencySymbol")+" "+	MyApplication.addDecimal(""+CashtoWalletConfirmScreen.taxConfigList.optJSONObject(0).optDouble("value")));

                tax2_layout.setVisibility(View.VISIBLE);
                tax2_lable.setText(MyApplication.getTaxString(CashtoWalletConfirmScreen.taxConfigList.optJSONObject(1).optString("taxTypeName"))+" :");
                tax2_value.setText(CashtoWalletConfirmScreen.receiptJson.optJSONObject("walletTransfer").optString("srcCurrencySymbol")+" "+	MyApplication.addDecimal(""+CashtoWalletConfirmScreen.taxConfigList.optJSONObject(1).optDouble("value")));
                // finalamount=Double.parseDouble(String.valueOf(ToSubscriber.fee))+Double.parseDouble(ToSubscriber.etAmount.getText().toString())+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"))+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"));
            }
        }


        setOnCLickListener();

    }

    private void setOnCLickListener() {
        btnCloseReceipt.setOnClickListener(cashtowalletreceiptscreenC);
        btnShareReceipt.setOnClickListener(cashtowalletreceiptscreenC);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCloseReceipt:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.btnShareReceipt:
                btnShareReceipt.setVisibility(View.GONE);
                Bitmap bitmap=getScreenShot(rootView);
                createImageFile(bitmap);
                //store(bitmap,"test.jpg");
                break;

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }


}
