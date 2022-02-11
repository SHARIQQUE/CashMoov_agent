package com.agent.cashmoovui.remittancebyabhay;

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
import java.text.SimpleDateFormat;

public class InternationalRemittanceReceiptScreen extends AppCompatActivity implements View.OnClickListener {
    public static InternationalRemittanceReceiptScreen internationalremitreceiptscreenC;
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
        setContentView(R.layout.activity_international_remittance_receipt_screen);
        internationalremitreceiptscreenC=this;
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

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
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


        DecimalFormat df = new DecimalFormat("0.000");
        tvTransRefNo.setText(InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("transactionReferenceNo"));
        tvConfCode.setText(InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("confirmationCode"));
        tvTransType.setText(InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("transactionType"));
        tvDateOfTrans.setText(InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("transactionDateTime"));
        tvSendCurrency.setText(InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("fromCurrencyName"));
        tvBenefiCurrency.setText(InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("toCurrencyName"));
        tvSendCountry.setText(InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optJSONObject("sender").optString("countryName"));
        tvRecCountry.setText(InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optJSONObject("receiver").optString("countryName"));
        tvSendName.setText(InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optJSONObject("sender").optString("firstName")+" "+
                InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optJSONObject("sender").optString("lastName"));
        tvSendPhoneNo.setText(InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optJSONObject("sender").optString("mobileNumber"));
        tvBenefiName.setText(InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optJSONObject("receiver").optString("firstName")+" "+
                InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optJSONObject("receiver").optString("lastName"));
        tvBenefiPhoneNo.setText(InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optJSONObject("receiver").optString("mobileNumber"));

        tvTransAmount.setText(MyApplication.addDecimal(InternationalRemittanceConfirmScreen.tvTransAmount.getText().toString()));
        tvConvRate.setText(InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("conversionRate"));
        tvFee.setText(InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol")+" "
                + df.format(InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optDouble("fee")));
        tvAmountPaid.setText(InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("toCurrencySymbol")+" "+df.format(InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optDouble("amountToPaid")));
        tvAmountCharged.setText(InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol")+" "+df.format(InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optDouble("amount")));


        if(InternationalRemittanceConfirmScreen.taxConfigList!=null){
            if(InternationalRemittanceConfirmScreen.taxConfigList.length()==1){
                tax1_layout.setVisibility(View.VISIBLE);
                tax1_lable.setText(InternationalRemittanceConfirmScreen.taxConfigList.optJSONObject(0).optString("taxTypeName")+" :");
                tax1_value.setText(InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol")+" "+df.format(InternationalRemittanceConfirmScreen.taxConfigList.optJSONObject(0).optDouble("value")));
                // finalamount=Double.parseDouble(String.valueOf(ToSubscriber.fee))+Double.parseDouble(ToSubscriber.etAmount.getText().toString())+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"));
            }
            if(InternationalRemittanceConfirmScreen.taxConfigList.length()==2){
                tax1_layout.setVisibility(View.VISIBLE);
                tax1_lable.setText(InternationalRemittanceConfirmScreen.taxConfigList.optJSONObject(0).optString("taxTypeName")+" :");
                tax1_value.setText(InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol")+" "+df.format(InternationalRemittanceConfirmScreen.taxConfigList.optJSONObject(0).optDouble("value")));

                tax2_layout.setVisibility(View.VISIBLE);
                tax2_lable.setText(InternationalRemittanceConfirmScreen.taxConfigList.optJSONObject(1).optString("taxTypeName")+" :");
                tax2_value.setText(InternationalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol")+" "+df.format(InternationalRemittanceConfirmScreen.taxConfigList.optJSONObject(1).optDouble("value")));
                // finalamount=Double.parseDouble(String.valueOf(ToSubscriber.fee))+Double.parseDouble(ToSubscriber.etAmount.getText().toString())+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"))+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"));
            }
        }


        setOnCLickListener();

    }

    private void setOnCLickListener() {
        btnCloseReceipt.setOnClickListener(internationalremitreceiptscreenC);
        btnShareReceipt.setOnClickListener(internationalremitreceiptscreenC);

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
